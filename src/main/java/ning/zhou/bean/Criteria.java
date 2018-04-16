package ning.zhou.bean;

import ning.zhou.utils.EmptyUtils;

import java.util.*;

/**
 * mysql查询条件封装
 *
 * @author 周宁
 * @date 2018/4/13 16:08
 */
public class Criteria {
    /**
     * 条件入参
     */
    private Set<WhereParam> whereParams;
    /**
     * 分组入参
     */
    private Set<String> groupByFeilds;
    /**
     * 排序入参
     */
    private Set<Sort> sorts;

    public Set<WhereParam> getWhereParams() {
        return whereParams;
    }

    public void setWhereParams(Set<WhereParam> whereParams) {
        this.whereParams = whereParams;
    }

    public Set<String> getGroupByFeilds() {
        return groupByFeilds;
    }

    public void setGroupByFeilds(Set<String> groupByFeilds) {
        this.groupByFeilds = groupByFeilds;
    }

    public Set<Sort> getSorts() {
        return sorts;
    }

    public void setSorts(Set<Sort> sorts) {
        this.sorts = sorts;
    }

    public Criteria() {
        whereParams = new LinkedHashSet<>();
        groupByFeilds = new HashSet<>();
        sorts = new LinkedHashSet<>();
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

    public Criteria groupBy(String... fields) {
        groupByFeilds.addAll(Arrays.asList(fields));
        return this;
    }

    public Criteria descOrderBy(String... fields) {
        for (String field : fields) {
            sorts.add(new Sort(field));
        }
        return this;
    }

    public Criteria ascOrderBy(String... fields) {
        for (String field : fields) {
            sorts.add(new Sort(field, "ASC"));
        }
        return this;
    }



}