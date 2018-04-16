package ning.zhou.jdbc;

import ning.zhou.bean.Criteria;
import ning.zhou.bean.Page;
import ning.zhou.bean.PageResult;
import ning.zhou.bean.Sort;

import java.io.Serializable;
import java.util.List;

/**
 * @author 周宁
 * @date 2018/4/13 10:17
 */
public interface CommonJdbcOperations {

    String SQL_INSERT = "INSERT";
    String SQL_UPDATE = "UPDATE";
    String SQL_DELETE = "DELETE";
    int BATCH_PAGE_SIZE = 1000;
    String SPACE = " ";
    String SQL_IN = "IN";
    char IN_START = '(';
    char IN_END = ')';
    String SQL_IS = "IS";
    String SQL_GROUP_BY = "GROUP BY";
    String SQL_ORDER_BY = "ORDER BY";
    /**
     * 根据主键查询一条记录
     *
     * @param id
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
    <E> void batchSave(List<E> list) throws Exception;

    /**
     * 根据主键删除一条记录
     *
     * @param id
     * @param <E>
     * @param <Id>
     * @throws Exception
     */
    <E, Id extends Serializable> void delete(Id id) throws Exception;

    /**
     * 根据主键批量删除
     *
     * @param ids
     * @param <E>
     * @param <Id>
     * @throws Exception
     */
    <E, Id extends Serializable> void batchDelete(List<Id> ids) throws Exception;

    /**
     * 更新一条记录
     *
     * @param e
     * @param <E>
     * @throws Exception
     */
    <E> void update(E e) throws Exception;

    /**
     * 批量更新
     *
     * @param <E>
     * @throws Exception
     */
    <E> void batchUpdate(List<E> list) throws Exception;

    /**
     * 分页查询
     *
     * @param page
     * @param <E>
     * @return
     * @throws Exception
     */
    <E> PageResult<E> pageQuery(Page page) throws Exception;

    <E> PageResult<E> pageQueryWithCriteria(Page page, Criteria criteria) throws Exception;

    /**
     * 分页并排序查询
     *
     * @param page
     * @param sort
     * @param <E>
     * @return
     * @throws Exception
     */
    <E> PageResult<E> pageAndSortQuery(Page page, Sort sort) throws Exception;

    <E> PageResult<E> pageAndSortQueryWithCriteria(Page page, Sort sort, Criteria criteria) throws Exception;

    /**
     * 条件查询
     *
     * @param criteria
     * @param <E>
     * @return
     * @throws Exception
     */
    <E> List<E> queryWithCriteria(Criteria criteria) throws Exception;
}
