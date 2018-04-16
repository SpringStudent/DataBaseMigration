package ning.zhou;

import ning.zhou.bean.TreeNode;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

/**
 * @Date 2017/11/20 9:26.
 */
public class MockTreeNode implements TreeNode<String> {

    private String id;
    private String parentId;

    public MockTreeNode(String id, String parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean isRoot() {
        return ObjectUtils.nullSafeEquals(id, parentId);
    }

    public MockTreeNode createSubNode() {
        return new MockTreeNode(UUID.randomUUID().toString().replace("-", ""), this.id);
    }
}
