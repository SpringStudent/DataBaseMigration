package db.migration;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 数据库迁移；重置期数事件
 *
 * @Author 周宁
 * @Date 2018/1/17 9:39.
 */
public class V1_0_1__MigrationSystemLog extends BaseDataSourceCopyMigration {

    public V1_0_1__MigrationSystemLog() {
        super("system_log", "adminoperationlog");
    }


    @Override
    protected void doMigrate(JdbcTemplate sourceNamedJdbcTemplate, JdbcTemplate targetJdbcTemplate) throws Exception {

    }

    private String buildSystemLogOperObject(String operFunction, String operObject) {
        return operFunction + "\"" + operObject + "\"";
    }
}
