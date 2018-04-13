package ning.zhou.bean;

/**
 * @Author 周宁
 * @Date 2017/11/17 19:30.
 */
public interface ValueFactory<V, K> {
    V create(K k);
}
