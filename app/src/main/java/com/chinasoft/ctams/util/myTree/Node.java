package com.chinasoft.ctams.util.myTree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public class Node {
    private String id;
    private String pid="";
    private String name;
    private int level;
    private boolean isExpand=false;
    private int nodeIcon;
    private Node parent;
    private List<Node> children=new ArrayList<Node>();

    public Node() {
    }

    public Node(String id, String pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return parent==null?0:parent.getLevel()+1;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        this.isExpand = expand;
        if (!isExpand){
            for(Node node:children){
                node.setExpand(false);
            }
        }
    }

    public int getNodeIcon() {
        return nodeIcon;
    }

    public void setNodeIcon(int nodeIcon) {
        this.nodeIcon = nodeIcon;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public boolean isRoot(){
        return parent==null;
    }
    public boolean isParentExpand(){
        if (parent==null) return false;
        return parent.isExpand();
    }

    public boolean isLeaf(){
        return children.size()==0;
    }
}
