package ning.zhou.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Map遍历器
 *
 * @author 周宁
 * @date 2017年10月19日 上午9:59:30
 */
public abstract class MapIterator<K, V> {


    /**
     * 对Map中的每个元素的迭代操作
     * @param k
     * @param v
     * @throws Exception
     */
    protected abstract void each(K k, V v) throws Exception;

    /**
     * 遍历Map
     * @param map
     * @throws Exception
     */
    public final void iterator(Map<K, V> map) throws Exception {
        if (EmptyUtils.isNotEmpty(map)) {
            Iterator<K> itr = map.keySet().iterator();
            while (itr.hasNext()) {
                K k = itr.next();
                V v = map.get(k);
                each(k, v);
            }
        }
    }
}
