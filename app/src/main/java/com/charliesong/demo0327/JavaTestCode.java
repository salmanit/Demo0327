package com.charliesong.demo0327;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by charlie.song on 2018/5/14.
 */

public class JavaTestCode {

    public class TreeNode{
        public TreeNode left;
        public TreeNode right;
        public int value;
        public TreeNode next;
    }

    public TreeNode revertNode(TreeNode treeNode){
        if(treeNode==null){
            return null;
        }
        TreeNode left=revertNode(treeNode.left);
        TreeNode right=revertNode(treeNode.right);

        treeNode.left=right;
        treeNode.right=left;
        return  treeNode;
    }

    public TreeNode linkedTreeNode(TreeNode treeNode){
        if(treeNode==null){
            return null;
        }
        if(treeNode.left!=null){
            treeNode.next=treeNode.left;

        }else{
            return treeNode;
        }
        linkedTreeNode(treeNode.next);

            treeNode.next=treeNode.right;

        return treeNode;
    }

    public static View find(ViewGroup vg,int id){
        int childCount=vg.getChildCount();
        for(int i=0;i<childCount;i++){
            View view=vg.getChildAt(i);
            if(view.getId()==id){
                return view;
            }
            if(view instanceof ViewGroup){
              View result=  find((ViewGroup) view,id);
              if(result!=null){
                  return result;
              }
            }
        }
        return null;
    }
}
