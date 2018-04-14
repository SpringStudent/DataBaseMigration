package db.migration;

import ning.zhou.bean.*;
import ning.zhou.domain.source.AdminOperationLog;
import ning.zhou.domain.tgt.SystemLog;
import ning.zhou.jdbc.CommonJdbcTemplate;
import ning.zhou.utils.CollectionHelper;
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

        /*Criteria criteria = new Criteria().where("operator","13701966214").and("enterpriseId", "in",Arrays.asList(89,921))).gt("id",235)
                .like("operObject","y").notEqual("id",256).or("id","in",18);*/
        Criteria criteria = new Criteria().or("id",123);

        List<SystemLog> result = target.queryWithCriteria(criteria);
        PageResult<SystemLog> result2 =target.pageQuery(new Page(1,12));
        PageResult<SystemLog> result3 = target.pageAndSortQuery(new Page(1,11),new Sort("operationTime","desc"));
        PageResult<SystemLog> result4 = target.pageQueryWithCriteria(new Page(1,12),criteria);
        PageResult<SystemLog> result5 = target.pageAndSortQueryWithCriteria(new Page(1,12),new Sort("operationTime","desc"),criteria);
        SystemLog systemLog = target.queryOne(1);

        System.out.print(systemLog);
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

    /*List<AdminOperationLog> logs = new ArrayList<>();
        String[] adminNames = new String[]{"17788888888","zhouning","xyj","1835607079","zhangyapo","13701966214"};
        String[] ips = new String[]{"127.0.0.1","117.21.32.44","172.16.21.42","192.152.21.3","182.1.2.23.2","117.232.22.8"};
        Integer[] enterpriseIds = new Integer[]{90001000,-1,89,921,1018,1042};
        String[] operFunctions = new String[]{"修改密码","修改授权","创建用户","删除用户","打开工程","重命名"};
        Random random = new Random();
        for(int i = 1;i<12340;i++){
            AdminOperationLog log = new AdminOperationLog();
            log.setAdmin(adminNames[random.nextInt(6)]);
            log.setEnterpriseId(enterpriseIds[random.nextInt(6)]);
            log.setOperationIP(ips[random.nextInt(6)]);
            log.setOperFunction(operFunctions[random.nextInt(6)]);
            log.setOperObject(adminNames[random.nextInt(6)]);
            log.setOperationTime(new Date());
            logs.add(log);
        }
        source.batchSave(logs);*/
}
