package ning.zhou.bean;

import java.io.Serializable;

/**
 * 树结构
 * @author 周宁
 * @date 2018/4/11 17:51
 */
public interface TreeNode<ID extends Serializable> {

    ID getId();

    ID getParentId();

    boolean isRoot();
}
