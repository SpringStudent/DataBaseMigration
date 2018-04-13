package ning.zhou.bean;

/**
 * 对象包装构建两个对象关系
 *
 * @author 周宁
 * @date 2017/11/13 11:22
 */
public class Pair<F, S> {

    private F first;
    
    private S second;

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
