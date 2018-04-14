package ning.zhou;

import ning.zhou.bean.TreeNode;
import ning.zhou.utils.EmptyUtils;

public class Org implements TreeNode<String>{

    private String id;

    private String parentId;

    private String name;

    public void setId(String id) {
        this.id = id;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public boolean isRoot() {
        if(EmptyUtils.isEmpty(this.parentId)){
            return true;
        }
        return false;
    }

    public Org() {
    }

    public Org(String id, String parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }
}
