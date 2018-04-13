package db.migration;

import org.flywaydb.core.api.migration.spring.BaseSpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

import static ning.zhou.ApplicationContextHolder.APPLICATION_CONTEXT;


/**
 * @Author 周宁
 * @Date 2018/4/11 10:58.
 */
public abstract class BaseDataSourceCopyMigration extends BaseSpringJdbcMigration {

    private JdbcTemplate sourceJdbcTemplate;

    public BaseDataSourceCopyMigration() {

        sourceJdbcTemplate = APPLICATION_CONTEXT.getBean("sourceJdbcTemplate", JdbcTemplate.class);
    }

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        doMigrate(this.sourceJdbcTemplate, jdbcTemplate);
    }

    protected abstract void doMigrate(JdbcTemplate sourceNamedJdbcTemplate, JdbcTemplate targetJdbcTemplate) throws Exception;
}
