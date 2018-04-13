package ning.zhou.jdbc;

import ning.zhou.jdbc.utils.EmptyUtils;
import ning.zhou.jdbc.utils.EntityTools;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.lang.reflect.Field;
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
        
    }

    @Override
    public <E> void save(List<E> list) throws Exception {

    }

    @Override
    public <E, Id extends Serializable> void delete(Id id) throws Exception {

    }

    @Override
    public <E, Id extends Serializable> void deleteAll(List<Id> ids) throws Exception {

    }

    @Override
    public <E> void update(E e) throws Exception {

    }
}
