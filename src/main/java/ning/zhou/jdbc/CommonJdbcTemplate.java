package ning.zhou.jdbc;

import ning.zhou.bean.*;
import ning.zhou.jdbc.utils.EntityTools;
import ning.zhou.jdbc.utils.SqlMakeTools;
import ning.zhou.utils.EmptyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CommonJdbcTemplate.class);

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
        logger.info(EXECUTE_SQL, sql);
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
        logger.info(EXECUTE_SQL, sql);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public <E> void save(E e) throws Exception {
        String sql = SqlMakeTools.makeSql(clzz, tbName, SQL_INSERT);
        Object[] args = SqlMakeTools.setArgs(e, SQL_INSERT);
        int[] argTypes = SqlMakeTools.setArgTypes(e, SQL_INSERT);
        logger.info(EXECUTE_SQL, sql);
        jdbcTemplate.update(sql, args, argTypes);
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
        logger.info(EXECUTE_SQL, sql);
        jdbcTemplate.batchUpdate(sql, batchArgs, argTypes);
    }

    @Override
    public <E, Id extends Serializable> void delete(Id id) throws Exception {
        String sql = " DELETE FROM " + tbName + " WHERE " + pk + " = ?";
        logger.info(EXECUTE_SQL, sql, id);
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
            logger.info(EXECUTE_SQL, sql);
            jdbcTemplate.update(sql.toString(), ids.toArray());
        }
    }

    @Override
    public <E> void update(E e) throws Exception {
        String sql = SqlMakeTools.makeSql(clzz, tbName, SQL_UPDATE);
        Object[] args = SqlMakeTools.setArgs(e, SQL_UPDATE);
        int[] argTypes = SqlMakeTools.setArgTypes(e, SQL_UPDATE);
        logger.info(EXECUTE_SQL, sql);
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
        logger.info(EXECUTE_SQL, sql);
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
        logger.info(EXECUTE_SQL, sql);
        return PageResult.newPageResult(count, paged);
    }

    @Override
    public <E> List<E> queryWithCriteria(Criteria criteria) throws Exception {
        String sql = "SELECT * FROM " + tbName;
        Pair<String, Object[]> pair = doCriteria(criteria, new StringBuilder(sql));
        logger.info(EXECUTE_SQL, pair.getFirst());
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
        if (null != criteria) {
            if (EmptyUtils.isNotEmpty(criteria.getWhereParams())) {
                //where 条件参数拼接
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
                        } else if (SQL_IS.equals(opt.toUpperCase())) {
                            sql.append(opt).append(SPACE).append(value);
                        } else {
                            sql.append(opt).append(SPACE).append("?");
                            params = ArrayUtils.add(params, value);
                        }
                        sql.append(" AND ");
                    }
                    sql.setLength(sql.length() - 5);
                }
            }
            //group by条件拼接
            if (EmptyUtils.isNotEmpty(criteria.getGroupByFeilds())) {
                sql.append(SPACE).append(SQL_GROUP_BY).append(SPACE);
                Set<String> groupByFileds = criteria.getGroupByFeilds();
                for (String groupByFiled : groupByFileds) {
                    sql.append(groupByFiled + ",");
                }
                sql.setLength(sql.length() - 1);
            }
            //排序条件拼接
            if (EmptyUtils.isNotEmpty(criteria.getSorts())) {
                sql.append(SPACE).append(SQL_ORDER_BY).append(SPACE);
                Set<Sort> sorts = criteria.getSorts();
                for (Sort sort : sorts) {
                    sql.append(sort.getSortField()).append(SPACE).append(sort.getSortType()).append(",");
                }
                sql.setLength(sql.length() - 1);
            }
        }

        result.setFirst(sql.toString().replace(", WHERE", " WHERE").replace("AND  OR", "OR"));
        result.setSecond(params);
        return result;
    }
}
