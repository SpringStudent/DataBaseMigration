package ning.zhou.bean;

import java.util.List;

/**
 * @author 周宁
 * @date 2018/4/10 15:29
 */
public class PageResult<T> {
    /**
     * 记录总条数
     */
    private int total;
    /**
     * 结果集
     */
    private List<T> result;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public static <T> PageResult<T> newPageResult(int total, List<T> paged) {
        PageResult<T> result = new PageResult<T>();
        result.setResult(paged);
        result.setTotal(total);
        return result;
    }
}
