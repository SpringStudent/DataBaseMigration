package ning.zhou.bean;

/**
 * @author 周宁
 * @date 2018/4/10 15:29
 */
public class Page {
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 每页记录数
     */
    private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset(){
        return (this.currentPage-1)*this.pageSize;
    }

    public Page(){

    }

    public Page(int currentPage,int pageSize){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }
}
