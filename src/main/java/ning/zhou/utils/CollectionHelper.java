package ning.zhou.utils;

import ning.zhou.bean.*;

import java.util.*;

/**
 * @Author 周宁
 * @Date 2017/11/13 15:45.
 */
public class CollectionHelper {
    /**
     * 判断集合是否为空
     *
     * @param coll
     * @param <T>
     * @return
     */
    public static <T> boolean isEmpty(Iterable<T> coll) {
        return coll == null || !coll.iterator().hasNext();
    }

    /**
     * 合并结果并将结果放到容器工厂创建的容器
     *
     * @param first   第一个集合
     * @param second  第二个结合
     * @param factory 容器工厂
     * @param <T>
     * @param <E>
     * @param <F>
     * @param <C>
     * @return
     */
    public static <T, E extends T, F extends T, C extends Collection<T>> C merge(Collection<E> first, Collection<F> second, ObjectFactory<C> factory) {
        C result = factory.getObject();
        result.addAll(first);
        result.addAll(second);
        return result;
    }

    /**
     * 如果Value不存在则去创建，并返回Value，否则就直接返回Value
     *
     * @param map
     * @param key
     * @param factory
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V putIfAbsent(Map<K, V> map, K key, ValueFactory<V, K> factory) {
        if (map.containsKey(key)) return map.get(key);
        V value = factory.create(key);
        map.put(key, value);
        return value;
    }

    /**
     * 将迭代器中的对象属性映射为一个集合
     *
     * @param iter
     * @param mapper
     * @return
     */
    public static <R, T> List<R> map(Iterator<? extends T> iter, ObjectMapper<R, T> mapper) {
        List<R> result = new ArrayList<R>();
        while (iter.hasNext()) {
            R mapped = mapper.map(iter.next());
            if (mapped != null) {
                result.add(mapped);
            }
        }
        return result;
    }

    /**
     * 将集合中的对象属性映射为一个集合(条件过滤)
     *
     * @param coll
     * @param mapper
     * @param condition
     * @param <R>
     * @param <T>
     * @return
     */
    public static <R, T> List<R> mapWithCondition(Collection<T> coll, ObjectMapper<R, T> mapper, Condition<T> condition) {
        if (!isEmpty(coll)) {
            return mapWithCondition(coll.iterator(), mapper, condition);
        }
        return Collections.emptyList();
    }

    /**
     * 将迭代器中的对象属性映射为一个集合(条件过滤)
     *
     * @param iter
     * @param mapper
     * @return
     */
    public static <R, T> List<R> mapWithCondition(Iterator<T> iter, ObjectMapper<R, T> mapper, Condition<T> condition) {
        List<R> result = new ArrayList<R>();
        while (iter.hasNext()) {
            T target = iter.next();
            R mapped = mapper.map(target);
            if (mapped != null && condition.match(target)) {
                result.add(mapped);
            }
        }
        return result;
    }

    /**
     * 将集合中的对象属性映射为一个集合
     *
     * @param coll
     * @param mapper
     * @return
     */
    public static <R, T> List<R> map(Collection<? extends T> coll, ObjectMapper<R, T> mapper) {
        if (!isEmpty(coll)) {
            return map(coll.iterator(), mapper);
        }
        return Collections.emptyList();
    }


    /**
     * Collection集合转换为Map
     *
     * @param coll
     * @param kvMapper
     * @param <K>
     * @param <V>
     * @param <T>
     * @return
     */
    public static <K, V, T> Map<K, V> convert2Map(Collection<T> coll, ObjectMapper<Pair<K, V>, T> kvMapper) {
        HashMap<K, V> result = new HashMap<>();
        if (!isEmpty(coll)) {
            return convert2Map(coll.iterator(), kvMapper);
        }
        return result;
    }


    public static <K, V, T> Map<K, V> convert2Map(Iterator<T> iter, ObjectMapper<Pair<K, V>, T> mapper) {
        Map<K, V> result = new HashMap<>();
        while (iter.hasNext()) {
            Pair<K, V> mapped = mapper.map(iter.next());
            if (mapped != null) {
                result.put(mapped.getFirst(), mapped.getSecond());
            }
        }
        return result;
    }

    /**
     * 去除List集合中的重复元素
     *
     * @param param
     * @param <T>
     * @return
     */
    public static <T> List<T> distinctList(List<T> param) {
        if (isEmpty(param)) {
            return param;
        }
        Set<T> temp = new HashSet<T>(param);
        return new ArrayList<>(temp);
    }

    /**
     * 求俩个集合的交集
     *
     * @param list1
     * @param list2
     * @param <T>
     * @return
     */
    public static <T> List<T> intersection(List<T> list1, List<T> list2) {
        if (isEmpty(list1) || isEmpty(list2)) {
            return new ArrayList<>();
        } else {
            Set<T> set = new HashSet<>(list1);
            set.retainAll(list2);
            return new ArrayList<>(set);
        }
    }

    /**
     * 对集合中元素进行特定的处理
     *
     * @param collection
     * @param handler
     * @param <T>
     */
    public static <T> void handler(Collection<T> collection, ObjectHandler<T> handler) {
        if (!isEmpty(collection)) {
            handler(collection.iterator(), handler);
        }
    }

    /**
     * 对集合中元素进行特定的处理
     *
     * @param iter
     * @param handler
     * @param <T>
     */
    public static <T> void handler(Iterator<T> iter, ObjectHandler<T> handler) {
        while (iter.hasNext()) {
            handler.handler(iter.next());
        }
    }
}