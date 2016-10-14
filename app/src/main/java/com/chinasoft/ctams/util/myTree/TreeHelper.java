package com.chinasoft.ctams.util.myTree;

import com.chinasoft.ctams.R;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodeId;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodeLabel;
import com.chinasoft.ctams.util.myTree.annotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class TreeHelper {
    public static <T> List<Node> convertDataToNodes(List<T> data) throws IllegalAccessException {
        List<Node> nodes=new ArrayList<>();
        Node node=null;
        for (T t:data){
            String id="";
            String pid="";
            String label=null;
            Class clazz=t.getClass();
            Field[] fields=clazz.getDeclaredFields();
            for (Field field:fields){
                if (field.getAnnotation(TreeNodeId.class)!=null){
                    field.setAccessible(true);
                    id=(String) field.get(t);
                }
                if (field.getAnnotation(TreeNodePid.class)!=null){
                    field.setAccessible(true);
                    pid=(String) field.get(t);
                }
                if (field.getAnnotation(TreeNodeLabel.class)!=null){
                    field.setAccessible(true);
                    label= (String) field.get(t);
                }
            }
            node=new Node(id,pid,label);
            nodes.add(node);
        }

        for (int i=0;i<nodes.size();i++){
            Node n=nodes.get(i);
            for (int j=i+1;j<nodes.size();j++){
                Node m=nodes.get(j);
                if (m.getPid().equals(n.getId())){
                    n.getChildren().add(m);
                    m.setParent(n);
                }else if (m.getId().equals(n.getPid())){
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }
        for (Node n:nodes){
            setNodeIcon(n);
        }
        return nodes;
    }

    public static <T> List<Node> getSortedNodes(List<T> data,int defaultExpandLevel) throws IllegalAccessException {
        List<Node> result=new ArrayList<>();
        List<Node> nodes=convertDataToNodes(data);
        List<Node> rootNodes=getRootNodes(nodes);
        for (Node node:rootNodes){
            addNode(result,node,defaultExpandLevel,1);
        }
        return result;
    }

    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);
        if (defaultExpandLevel>=currentLevel){
            node.setExpand(true);
        }
        if (node.isLeaf())
            return;
        for (int i=0;i<node.getChildren().size();i++){
            addNode(result,node.getChildren().get(i),defaultExpandLevel,currentLevel+1);

        }
    }

    public static List<Node> filterVisibleNodes(List<Node> nodes){
        List<Node> result=new ArrayList<>();
        for (Node node:nodes){
            if (node.isRoot() || node.isParentExpand()){
                setNodeIcon(node);
                result.add(node);
            }
        }
        return result;
    }


    private static List<Node> getRootNodes(List<Node> nodes){
        List<Node> root=new ArrayList<>();
        for (Node node:nodes){
            if (node.isRoot()){
                root.add(node);
            }
        }
        return root;
    }

    private static void setNodeIcon(Node n){
        if (n.getChildren().size()>0 && n.isExpand()){
            n.setNodeIcon(R.mipmap.tree_ex);
        }else if (n.getChildren().size()>0 && !n.isExpand()){
            n.setNodeIcon(R.mipmap.tree_ec);
        }else {
            n.setNodeIcon(-1);
        }
    }
}
