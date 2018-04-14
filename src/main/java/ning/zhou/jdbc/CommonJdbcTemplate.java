package ning.zhou.jdbc;

import ning.zhou.bean.*;
import ning.zhou.jdbc.utils.EntityTools;
import ning.zhou.jdbc.utils.SqlMakeTools;
import ning.zhou.utils.EmptyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.util.*;

/**
 * @author 周宁
 * @date 2018/4/13 10:17
 */
public class CommonJdbcTemplate implements CommonJdbcOperations {

    private Class clzz;

    private String tbName;

    private String pk;

    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper;

    public CommonJdbcTemplate(Class clzz, JdbcTemplate jdbcTemplate) {
        this.clzz = clzz;
        this.jdbcTemplate = jdbcTemplate;
        this.tbName = EntityTools.getTableName(clzz);
        this.pk = EntityTools.getPk(clzz);
        this.rowMapper = BeanPropertyRowMapper.newInstance(clzz);
    }

    @Override
    public <E, Id extends Serializable> E queryOne(Id id) throws Exception {
        String sql = "SELECT * FROM " + tbName + " WHERE " + pk + " = ?";
        List<E> result = jdbcTemplate.query(sql, rowMapper, id);
        if (EmptyUtils.isEmpty(result)) {
            return null;
        }
        if (result.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, result.size());
        }
        return result.get(0);
    }

    @Override
    public <E> List<E> query() throws Exception {
        String sql = "SELECT * FROM " + tbName;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public <E> void save(E e) throws Exception {
        String sql = SqlMakeTools.makeSql(clzz, tbName, SQL_INSERT);
        Object[] args = SqlMakeTools.setArgs(e, SQL_INSERT);
        int[] argTypes = SqlMakeTools.setArgTypes(e, SQL_INSERT);
        jdbcTemplate.update(sql.toString(), args, argTypes);
    }

    @Override
    public <E> void batchSave(List<E> list) throws Exception {
        if (list == null || list.size() == 0) {
            return;
        }
        //分页操作
        String sql = SqlMakeTools.makeSql(clzz, tbName, SQL_INSERT);
        int[] argTypes = SqlMakeTools.setArgTypes(list.get(0), SQL_INSERT);
        Integer j = 0;
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            batchArgs.add(SqlMakeTools.setArgs(list.get(i), SQL_INSERT));
            j++;
            if (j.intValue() == BATCH_PAGE_SIZE) {
                jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
                batchArgs = new ArrayList<>();
                j = 0;
            }
        }
        jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
    }

    @Override
    public <E, Id extends Serializable> void delete(Id id) throws Exception {
        String sql = " DELETE FROM " + tbName + " WHERE " + pk + " = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public <E, Id extends Serializable> void batchDelete(List<Id> ids) throws Exception {
        if (EmptyUtils.isNotEmpty(ids)) {
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE FROM " + tbName + " WHERE " + pk + " in (");
            for (int i = 0; i < ids.size(); i++) {
                if (i == ids.size() - 1) {
                    sql.append("?)");
                } else {
                    sql.append("?,");
                }
            }
            jdbcTemplate.update(sql.toString(), ids.toArray());
        }
    }

    @Override
    public <E> void update(E e) throws Exception {
        String sql = SqlMakeTools.makeSql(clzz, tbName, SQL_UPDATE);
        Object[] args = SqlMakeTools.setArgs(e, SQL_UPDATE);
        int[] argTypes = SqlMakeTools.setArgTypes(e, SQL_UPDATE);
        jdbcTemplate.update(sql, args, argTypes);
    }

    @Override
    public <E> void batchUpdate(List<E> list) throws Exception {
        //分页操作
        String sql = SqlMakeTools.makeSql(clzz, tbName, SQL_UPDATE);
        int[] argTypes = SqlMakeTools.setArgTypes(list.get(0), SQL_UPDATE);
        Integer j = 0;
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            batchArgs.add(SqlMakeTools.setArgs(list.get(i), SQL_UPDATE));
            j++;
            if (j.intValue() == BATCH_PAGE_SIZE) {
                jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
                batchArgs = new ArrayList<>();
                j = 0;
            }
        }
        jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
    }

    @Override
    public <E> PageResult<E> pageQuery(Page page) throws Exception {
        return this.pageQueryWithCriteria(page, null);
    }

    @Override
    public <E> PageResult<E> pageQueryWithCriteria(Page page, Criteria criteria) throws Exception {
        String sql = "SELECT * FROM " + tbName;
        Pair<String, Object[]> pair = doCriteria(criteria, new StringBuilder(sql));
        sql = pair.getFirst();
        Object[] params = pair.getSecond();
        String pageSql = "SELECT SQL_CALC_FOUND_ROWS * FROM (" + sql + ") temp LIMIT ?,?";
        params = ArrayUtils.add(params, page.getOffset());
        params = ArrayUtils.add(params, page.getPageSize());
        List<E> paged = jdbcTemplate.query(pageSql, params, rowMapper);
        String countSql = "SELECT FOUND_ROWS() ";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        return PageResult.newPageResult(count, paged);
    }

    @Override
    public <E> PageResult<E> pageAndSortQuery(Page page, Sort sort) throws Exception {
        return this.pageAndSortQueryWithCriteria(page, sort, null);
    }

    @Override
    public <E> PageResult<E> pageAndSortQueryWithCriteria(Page page, Sort sort, Criteria criteria) throws Exception {
        String sql = "SELECT * FROM " + tbName;
        Pair<String, Object[]> pair = doCriteria(criteria, new StringBuilder(sql));
        sql = pair.getFirst();
        Object[] params = pair.getSecond();
        if (sort != null) {
            sql += sort.buildSortSql();
        }
        String pageSql = "SELECT SQL_CALC_FOUND_ROWS * FROM (" + sql + ") temp LIMIT ?,?";
        params = ArrayUtils.add(params, page.getOffset());
        params = ArrayUtils.add(params, page.getPageSize());
        List<E> paged = jdbcTemplate.query(pageSql, params, rowMapper);
        String countSql = "SELECT FOUND_ROWS() ";
        int count = jdbcTemplate.queryForObject(countSql, Integer.class);
        return PageResult.newPageResult(count, paged);
    }

    @Override
    public <E> List<E> queryWithCriteria(Criteria criteria) throws Exception {
        String sql = "SELECT * FROM " + tbName;
        Pair<String, Object[]> pair = doCriteria(criteria, new StringBuilder(sql));
        return jdbcTemplate.query(pair.getFirst(), pair.getSecond(), rowMapper);
    }

    /**
     * 创建条件查询sql和入参
     *
     * @param criteria
     * @param sql
     * @return
     */
    private Pair<String, Object[]> doCriteria(Criteria criteria, StringBuilder sql) {
        Pair<String, Object[]> result = new Pair<>();
        Object[] params = {};
        if (null != criteria && EmptyUtils.isNotEmpty(criteria.getWhereParams())) {
            Set<WhereParam> whereParams = criteria.getWhereParams();
            if (null != criteria && EmptyUtils.isNotEmpty(whereParams)) {
                sql.append(" WHERE ");
                for (WhereParam whereParam : whereParams) {
                    String key = whereParam.getKey();
                    String opt = whereParam.getOpt();
                    Object value = whereParam.getValue();
                    sql.append(key).append(SPACE);
                    if (SQL_IN.equals(opt.toUpperCase())) {
                        sql.append(opt).append(IN_START);
                        if (value instanceof Collection) {
                            Iterator iterator = ((Collection) value).iterator();
                            while (iterator.hasNext()) {
                                params = ArrayUtils.add(params, iterator.next());
                                sql.append("?,");
                            }
                            sql.setLength(sql.length() - 1);
                        } else {
                            sql.append(SPACE).append("?");
                            params = ArrayUtils.add(params, value);
                        }
                        sql.append(IN_END);
                    } else {
                        sql.append(opt).append(SPACE).append("?");
                        params = ArrayUtils.add(params, value);
                    }
                    sql.append(" AND ");
                }
                sql.setLength(sql.length() - 5);
            }
        }
        result.setFirst(sql.toString().replace(", WHERE", " WHERE").replace("AND  OR", "OR"));
        result.setSecond(params);
        return result;
    }
}
