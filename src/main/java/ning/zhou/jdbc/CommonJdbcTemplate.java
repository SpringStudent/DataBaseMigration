package ning.zhou.jdbc;

import ning.zhou.jdbc.utils.EmptyUtils;
import ning.zhou.jdbc.utils.EntityTools;
import ning.zhou.jdbc.utils.SqlMakeTools;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 周宁
 * @date 2018/4/13 10:17
 */
public class CommonJdbcTemplate implements CommonJdbcOperations {

    private Class clzz;

    private String tbName;

    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper;

    public CommonJdbcTemplate(Class clzz, JdbcTemplate jdbcTemplate) {
        this.clzz = clzz;
        this.jdbcTemplate = jdbcTemplate;
        this.tbName = EntityTools.getTableName(clzz);
        this.rowMapper = BeanPropertyRowMapper.newInstance(clzz);
    }

    @Override
    public <E, Id extends Serializable> E queryOne(Id id) throws Exception {
        Field[] fields = clzz.getDeclaredFields();
        String primaryKey = EntityTools.getPrimaryKey(fields);
        String sql = "SELECT * FROM " + tbName + " WHERE " + primaryKey + " = ?";
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
        List<Object[]> batchArgs = new ArrayList<Object[]>();
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
        Field[] fields = clzz.getDeclaredFields();
        String primaryKey = EntityTools.getPrimaryKey(fields);
        String sql = " DELETE FROM " + tbName + " WHERE " + primaryKey + " = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public <E, Id extends Serializable> void deleteAll(List<Id> ids) throws Exception {
        if (EmptyUtils.isNotEmpty(ids)) {
            Field[] fields = clzz.getDeclaredFields();
            String primaryKey = EntityTools.getPrimaryKey(fields);
            StringBuilder sql = new StringBuilder();
            sql.append(" DELETE FROM " + tbName + " WHERE " + primaryKey + " in (");
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
}
