package ning.zhou.bean;

/**
 * @author 周宁
 * @date 2018/4/10 15:32
 */
public class Sort {

    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序类型(DESC降序，ASC升序)
     */
    private String sortType = "DESC";

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String buildSortSql(){
        return " order by "+this.getSortField()+" "+sortType;
    }
    public Sort(){

    }

    public Sort(String sortField){
        this.sortField = sortField;
    }

    public Sort(String sortField,String sortType){
        this.sortField = sortField;
    }
}
