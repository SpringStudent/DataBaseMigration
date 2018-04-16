package ning.zhou.bean;

import ning.zhou.utils.EmptyUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 周宁
 * @date 2018/4/13 16:08
 */
public class Criteria {

    private Set<WhereParam> whereParams;

    public Set<WhereParam> getWhereParams() {
        return whereParams;
    }

    public void setWhereParams(Set<WhereParam> whereParams) {
        this.whereParams = whereParams;
    }

    public Criteria() {
        whereParams = new LinkedHashSet<>();
    }

    public Criteria where(String key, Object value) {
        return this.where(key, "=", value);
    }

    public Criteria where(String key, String opt, Object value) {
        this.whereParams.add(WhereParam.builder().key(key).opt(opt).value(value).build());
        return this;
    }

    public Criteria like(String key, Object value) {
        return this.where(key, "LIKE", "%" + value + "%");
    }

    public Criteria gt(String key, Object value) {
        return this.where(key, ">", value);
    }

    public Criteria gte(String key, Object value) {
        return this.where(key, ">=", value);
    }

    public Criteria lt(String key, Object value) {
        return this.where(key, "<", value);
    }

    public Criteria let(String key, Object value) {
        return this.where(key, "<=", value);
    }

    public Criteria notEqual(String key, Object value) {
        return this.where(key, "<>", value);
    }

    public Criteria isNull(String key) {
        return this.where(key, "IS", "NULL");
    }

    public Criteria isNotNull(String key) {
        return this.where(key, "IS", "NOT NULL");
    }

    public Criteria and(String key, Object value) {
        return this.where(key, value);
    }

    public Criteria and(String key, String opt, Object value) {
        return this.where(key, opt, value);
    }

    public Criteria or(String key, Object value) {
        return this.or(key, "=", value);
    }

    public Criteria or(String key, String opt, Object value) {
        if (EmptyUtils.isEmpty(this.whereParams)) {
            throw new RuntimeException("sql error,condition \"or\" must be following after \"where\"!");
        }
        return this.where(" OR " + key, opt, value);
    }

    public Criteria in(String key, List<?> args) {
        return this.where(key, "IN", args);
    }

    public static void main(String[] args) {
        Criteria criteria = new Criteria();
        criteria.where("username", "zhouning01").and("epid", 921).or("isAdim", true).in("id", Arrays.asList(new Integer[]{1, 3, 4, 5}));
        System.out.println(criteria.whereParams);
    }
}