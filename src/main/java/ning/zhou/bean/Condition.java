package ning.zhou.bean;

/**
 * @author 周宁
 * @date 2018/2/11 9:29
 */
public interface Condition<T> {

    boolean match(T t);
}
