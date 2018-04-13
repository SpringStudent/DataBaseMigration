package ning.zhou.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * 集合遍历器
 *
 * @param <T>
 * @author 周宁
 * @date 2017年10月19日 上午9:58:53
 */
public abstract class CollectionIterator<T> {
    /**
     * 对集合中每个元素的迭代处理
     * @param t
     * @throws Exception
     */
    protected abstract void each(T t) throws Exception;

    /**
     * 对集合中每个元素的迭代处理
     * @param c
     * @throws Exception
     */
    public final void iterator(Collection<T> c) throws Exception {
        if (EmptyUtils.isNotEmpty(c)) {
            Iterator<T> itr = c.iterator();
            while (itr.hasNext()) {
                T t = itr.next();
                each(t);
            }
        }
    }
}
