package ning.zhou.domain.source;

import lombok.Data;
import ning.zhou.jdbc.bean.Table;

import java.util.Date;

/**
 * @author 周宁
 * @date 2018/4/12 14:10
 */
@Data
@Table(name="adminoperationlog")
public class AdminOperationLog {
    private Integer id;

    private String admin;

    private String operFunction;

    private String operObject;

    private String operationIP;

    private Integer enterpriseId;

    private Date operationTime;

}
