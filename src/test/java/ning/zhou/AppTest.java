package ning.zhou;

import ning.zhou.bean.ObjectFactory;
import ning.zhou.bean.ObjectMapper;
import ning.zhou.bean.Pair;
import ning.zhou.utils.CollectionHelper;
import ning.zhou.utils.TreeNodeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    public static void main(String[] args) {
        List<Org> treeNodeList = new ArrayList<>();
        Org treeNode = new Org("1","","treeNode");
        Org treeNode1 = new Org("11","1","treeNode1");
        Org treeNode2 = new Org("22","1","treeNode2");
        Org treeNode3 = new Org("333","22","treeNode3");
        Org treeNode4 = new Org("4444","333","treeNode4");
        treeNodeList.add(treeNode);treeNodeList.add(treeNode1);treeNodeList.add(treeNode2);
        treeNodeList.add(treeNode3);treeNodeList.add(treeNode4);
        Map<String,Org> map = CollectionHelper.convert2Map(treeNodeList, new ObjectMapper<Pair<String,Org>, Org>() {
            @Override
            public Pair<String,Org> map(Org target) {
                Pair<String,Org> pair = new Pair<>();
                pair.setFirst(target.getId());
                pair.setSecond(target);
                return pair;
            }
        });
        List<Org> anOrgs = TreeNodeUtils.searchAncestors(map, treeNode4, new ObjectFactory<List<Org>>() {
            @Override
            public List<Org> getObject() {
                return new ArrayList<>();
            }
        });
        System.out.print(anOrgs);
        List<Org> deOrgs = TreeNodeUtils.searchDescendants(map, treeNode2, new ObjectFactory<List<Org>>() {
            @Override
            public List<Org> getObject() {
                return new ArrayList<>();
            }
        });
        System.out.print(deOrgs);

        List<Org> orgs = TreeNodeUtils.searchAncestorsAndDescendants(map, treeNode2, new ObjectFactory<List<Org>>() {
            @Override
            public List<Org> getObject() {
                return new ArrayList<>();
            }
        });
        System.out.print(orgs);
    }
}
