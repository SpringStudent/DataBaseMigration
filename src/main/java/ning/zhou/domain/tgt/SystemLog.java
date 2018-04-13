package ning.zhou.domain.tgt;

import lombok.Data;

import java.util.Date;

/**
 * @author 周宁
 * @date 2018/4/12 14:12
 */
@Data
public class SystemLog {

    private String id;

    private String operator;

    private String operFunction;

    private String operObject;

    private String operationIP;

    private Integer enterpriseId;

    private Date operationTime;

}
