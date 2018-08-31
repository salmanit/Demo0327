package com.charliesong.demo0327.bean;

import java.util.List;

public class ThirdNavigation {

    /**
     * articles : [{"apkLink":"","author":"小编","chapterId":365,"chapterName":"反馈平台","collect":false,"courseId":13,"desc":"","envelopePic":"","fresh":false,"id":2976,"link":"https://tucao.qq.com","niceDate":"2018-06-03","origin":"","projectLink":"","publishTime":1527991466000,"superChapterId":0,"superChapterName":"","tags":[],"title":"一站式解决用户反馈问题","type":0,"userId":-1,"visible":1,"zan":0}]
     * cid : 365
     * name : 反馈平台
     */

    private int cid;
    private String name;
    private List<ArticleBean> articles;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleBean> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleBean> articles) {
        this.articles = articles;
    }

}
