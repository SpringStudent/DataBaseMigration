package ning.zhou;

import ning.zhou.bean.ObjectFactory;
import ning.zhou.bean.TreeNode;
import ning.zhou.utils.TreeNodeUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * 模拟测试树节点<br>
 * <p>
 * <p>root</p>
 * <p>level10 level11 level12</p>
 * <p>level20 level21 level22</p>
 * <p>level 30 level31 level 32</p>
 *
 * @Date 2017/11/20 9:26.
 */
public class TreeNodesTest {

    Map<String, TreeNode<String>> allNodes = new HashMap<>();
    private MockTreeNode root;

    private MockTreeNode level10;
    private MockTreeNode level11;
    private MockTreeNode level12;

    private MockTreeNode level20;
    private MockTreeNode level21;
    private MockTreeNode level22;

    private MockTreeNode level30;
    private MockTreeNode level31;
    private MockTreeNode level32;

    @Before
    public void setUp() {
        String rootId = UUID.randomUUID().toString().replace("-", "");
        root = new MockTreeNode(rootId, rootId);
        allNodes.put(root.getId(), root);

        level10 = root.createSubNode();
        level11 = root.createSubNode();
        level12 = root.createSubNode();

        allNodes.put(level10.getId(), level10);
        allNodes.put(level11.getId(), level11);
        allNodes.put(level12.getId(), level12);

        level20 = level10.createSubNode();
        level21 = level10.createSubNode();
        level22 = level10.createSubNode();

        allNodes.put(level20.getId(), level20);
        allNodes.put(level21.getId(), level21);
        allNodes.put(level22.getId(), level22);

        level30 = level20.createSubNode();
        level31 = level20.createSubNode();
        level32 = level20.createSubNode();

        allNodes.put(level30.getId(), level30);
        allNodes.put(level31.getId(), level31);
        allNodes.put(level32.getId(), level32);
    }

    @Test
    public void testSearchAncestors() {
        Set<TreeNode<String>> result = TreeNodeUtils.searchAncestors(allNodes, level20, new ObjectFactory<Set<TreeNode<String>>>() {
            @Override
            public Set<TreeNode<String>> getObject() {
                return new HashSet<>();
            }
        });

        Set<TreeNode<String>> expected = new HashSet<>();
        expected.add(level10);
        expected.add(root);
        assertEquals(expected, result);
    }

    @Test
    public void testSearchDescendants() {
        Set<TreeNode<String>> result = TreeNodeUtils.searchDescendants(allNodes, level10, new ObjectFactory<Set<TreeNode<String>>>() {
            @Override
            public Set<TreeNode<String>> getObject() {
                return new HashSet<>();
            }
        });

        Set<TreeNode<String>> expected = new HashSet<>();
        expected.add(level20);
        expected.add(level21);
        expected.add(level22);

        expected.add(level30);
        expected.add(level31);
        expected.add(level32);
        assertEquals(expected, result);
    }

    @Test
    public void testSearchAncestorsAndDescendants() {
        Set<TreeNode<String>> result = TreeNodeUtils.searchAncestorsAndDescendants(allNodes, level20, new ObjectFactory<Set<TreeNode<String>>>() {
            @Override
            public Set<TreeNode<String>> getObject() {
                return new HashSet<>();
            }
        });

        Set<TreeNode<String>> expected = new HashSet<>();
        expected.add(root);
        expected.add(level10);

        expected.add(level30);
        expected.add(level31);
        expected.add(level32);
        assertEquals(expected, result);
    }
}
