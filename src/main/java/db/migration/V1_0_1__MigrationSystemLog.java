package db.migration;

import ning.zhou.bean.*;
import ning.zhou.domain.source.AdminOperationLog;
import ning.zhou.domain.tgt.SystemLog;
import ning.zhou.jdbc.CommonJdbcTemplate;
import ning.zhou.utils.CollectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * 数据库迁移；重置期数事件
 *
 * @Author 周宁
 * @Date 2018/1/17 9:39.
 */
public class V1_0_1__MigrationSystemLog extends BaseDataSourceCopyMigration {

    @Override
    protected void doMigrate(JdbcTemplate sourceJdbcTemplate, JdbcTemplate targetJdbcTemplate) throws Exception {
        CommonJdbcTemplate source = new CommonJdbcTemplate(AdminOperationLog.class,sourceJdbcTemplate);
        CommonJdbcTemplate target = new CommonJdbcTemplate(SystemLog.class,targetJdbcTemplate);
        List<AdminOperationLog> adminOperationLogs = source.query();
        List<SystemLog> systemLogs = CollectionHelper.map(adminOperationLogs, new ObjectMapper<SystemLog, AdminOperationLog>() {
            @Override
            public SystemLog map(AdminOperationLog target) {
                SystemLog systemLog = new SystemLog();
                systemLog.setOperationTime(target.getOperationTime());
                systemLog.setEnterpriseId(target.getEnterpriseId());
                systemLog.setId(target.getId() + "");
                systemLog.setOperationIP(target.getOperationIP());
                systemLog.setOperator(target.getAdmin());
                systemLog.setOperFunction(target.getOperFunction());
                systemLog.setOperObject(buildSystemLogOperObject(target.getOperFunction(), target.getOperObject()));
                return systemLog;
            }
        });
        target.batchSave(systemLogs);

        Criteria criteria = new Criteria().where("operator","13701966214").and("enterpriseId","in", Arrays.asList(new Integer[]{89}));
        List<SystemLog> result = target.queryWithCriteria(criteria);
        PageResult<SystemLog> result2 =target.pageQuery(new Page(1,12));
        PageResult<SystemLog> result3 = target.pageAndSortQuery(new Page(1,11),new Sort("operationTime","desc"));
        PageResult<SystemLog> result4 = target.pageQueryWithCriteria(new Page(1,12),criteria);
        PageResult<SystemLog> result5 = target.pageAndSortQueryWithCriteria(new Page(1,12),new Sort("operationTime","desc"),criteria);
        System.out.println(systemLogs);
        System.out.println(result);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(result4);
        System.out.println(result5);
    }

    private String buildSystemLogOperObject(String operFunction, String operObject) {
        return operFunction + "\"" + operObject + "\"";
    }
}
