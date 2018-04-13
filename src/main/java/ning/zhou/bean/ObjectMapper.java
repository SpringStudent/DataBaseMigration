package ning.zhou.bean;

/**
 * @author 周宁
 * @date 2018/2/11 9:29
 */
public interface ObjectMapper<R, T> {
    R map(T target);
}