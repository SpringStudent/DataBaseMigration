package ning.zhou.jdbc;

import java.io.Serializable;
import java.util.List;

/**
 * @author 周宁
 * @date 2018/4/13 10:17
 */
public interface CommonJdbcOperations {

    /**
     * 根据主键查询一条记录
     *
     * @param primaryKey
     * @param <E>
     * @param <Id>
     * @return
     * @throws Exception
     */
    <E, Id extends Serializable> E queryOne(Id id) throws Exception;

    /**
     * 查询所有记录
     *
     * @param <E>
     * @return
     * @throws Exception
     */
    <E> List<E> query() throws Exception;

    /**
     * 保存一条记录
     *
     * @param e
     * @param <E>
     * @throws Exception
     */
    <E> void save(E e) throws Exception;

    /**
     * 批量保存
     *
     * @param list
     * @param <E>
     * @throws Exception
     */
    <E> void save(List<E> list) throws Exception;

    /**
     * 根据主键删除一条记录
     *
     * @param primaryKey
     * @param <E>
     * @param <Id>
     * @throws Exception
     */
    <E, Id extends Serializable> void delete(Id id) throws Exception;

    /**
     * 根据主键批量删除
     *
     * @param primaryKeys
     * @param <E>
     * @param <Id>
     * @throws Exception
     */
    <E, Id extends Serializable> void deleteAll(List<Id> ids) throws Exception;

    /**
     * 更新一条记录
     *
     * @param e
     * @param <E>
     * @throws Exception
     */
    <E> void update(E e) throws Exception;
}
