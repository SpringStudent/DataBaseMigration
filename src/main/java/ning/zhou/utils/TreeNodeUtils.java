package ning.zhou.utils;

import ning.zhou.bean.ObjectFactory;
import ning.zhou.bean.TreeNode;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 周宁
 * @date 2018/4/11 17:56
 */
public class TreeNodeUtils {

    /**
     * 获取祖先和孩子节点
     * @param allNodes
     * @param node
     * @param factory
     * @param <E>
     * @param <ID>
     * @param <T>
     * @return
     */
    public static <E extends TreeNode,ID extends Serializable,T extends Collection<E>> T  searchAncestorsAndDescendants(Map<ID,E> allNodes, E node, ObjectFactory<T> factory){
        T result = factory.getObject();
        if(EmptyUtils.isEmpty(allNodes)||null==node){
            return result;
        }
        putAncestors(allNodes,node,result);
        putDescendants(allNodes,node,result);
        return result;
    }
    /**
     * 获取祖先节点
     * @param allNodes
     * @param node
     * @param factory
     * @param <E>
     * @param <ID>
     * @param <T>
     * @return
     */
    public static <E extends TreeNode,ID extends Serializable,T extends Collection<E>> T searchAncestors(Map<ID,E> allNodes, E node, ObjectFactory<T> factory){
        T result = factory.getObject();
        if(EmptyUtils.isEmpty(allNodes)||null==node){
            return result;
        }
        putAncestors(allNodes,node,result);
        return result;
    }

    /**
     * 获取孩子节点
     * @param allNodes
     * @param node
     * @param factory
     * @param <E>
     * @param <ID>
     * @param <T>
     * @return
     */
    public static <E extends TreeNode,ID extends Serializable,T extends Collection<E>> T searchDescendants(Map<ID,E> allNodes, E node, ObjectFactory<T> factory) {
        T result = factory.getObject();
        if(EmptyUtils.isEmpty(allNodes)||null==node){
            return result;
        }
        putDescendants(allNodes,node,result);
        return result;
    }
    /**
     * 递归当前节点的父节点
     * @param allNodes
     * @param node
     * @param result
     * @param <E>
     * @param <ID>
     * @return
     */
    private static  <E extends TreeNode,ID extends Serializable> Collection<E> putAncestors(Map<ID,E> allNodes, E node, Collection<E> result){
        if(node.isRoot()){
            return result;
        }
        node = allNodes.get(node.getParentId());
        result.add(node);
        return putAncestors(allNodes,node,result);
    }

    /**
     * 递归当前节点的子节点
     * @param allNodes
     * @param node
     * @param result
     * @param <E>
     * @param <ID>
     * @return
     */
    private static  <E extends TreeNode,ID extends Serializable> Collection<E> putDescendants(Map<ID,E> allNodes, E node, Collection<E> result){
        Iterator<Map.Entry<ID, E>> iterator=allNodes.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<ID, E> entry =iterator.next();
            E e = entry.getValue();
            if(e.getParentId().equals(node.getId())){
                putDescendants(allNodes,e,result);
                result.add(e);
            }
        }
        return result;
    }


}
