package com.charliesong.demo0327.bean;

import java.util.ArrayList;

public  class DataBean<T> {
    /**
     * curPage : 1112
     * datas : []
     * offset : 22220
     * over : true
     * pageCount : 70
     * size : 20
     * total : 1399
     */

    private int curPage;
    private int offset;
    private boolean over;
    private int pageCount;
    private int size;
    private int total;
    private ArrayList<T> datas;

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<T> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<T> datas) {
        this.datas = datas;
    }
}