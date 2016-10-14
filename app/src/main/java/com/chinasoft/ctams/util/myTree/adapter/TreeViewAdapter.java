package com.chinasoft.ctams.util.myTree.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.chinasoft.ctams.util.myTree.Node;
import com.chinasoft.ctams.util.myTree.TreeHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
public abstract class TreeViewAdapter<T> extends BaseAdapter {
    protected Context context;
    protected List<Node> allNodes;
    protected List<Node> visibleNodes;
    protected LayoutInflater layoutInflater;
    protected ListView mTree;

    public interface OnTreeNodeClickListener{
        void onClick(Node node, int position);
    }

    private OnTreeNodeClickListener onTreeNodeClickListener;

    public void setOnTreeNodeClickListener(OnTreeNodeClickListener onTreeNodeClickListener) {
        this.onTreeNodeClickListener = onTreeNodeClickListener;
    }

    public TreeViewAdapter(Context context, ListView tree, List<T> data, int defalutExpandLevel) throws IllegalAccessException {
        this.context=context;
        allNodes= TreeHelper.getSortedNodes(data,defalutExpandLevel);
        visibleNodes=TreeHelper.filterVisibleNodes(allNodes);
        layoutInflater=LayoutInflater.from(context);
        mTree=tree;
        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                expandOrCollapse(position);
                if (onTreeNodeClickListener!=null){
                    onTreeNodeClickListener.onClick(visibleNodes.get(position),position);
                }
            }
        });
    }

    private void expandOrCollapse(int position) {
        Node n=visibleNodes.get(position);
        if (n!=null){
            if (n.isLeaf())
                return;
            n.setExpand(!n.isExpand());
            visibleNodes=TreeHelper.filterVisibleNodes(allNodes);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return visibleNodes.size();
    }

    @Override
    public Object getItem(int i) {
        return visibleNodes.get(i);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Node node=visibleNodes.get(i);
        view=getConvertView(node,i,view,viewGroup);
        view.setPadding(node.getLevel()*30,3,3,3);
        return view;
    }

    public abstract View getConvertView(Node node,int position,View convertView,ViewGroup parent);
}
