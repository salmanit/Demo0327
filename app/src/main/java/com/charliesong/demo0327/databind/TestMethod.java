package com.charliesong.demo0327.databind;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class TestMethod extends BaseObservable {

    private String name;

    private int age;
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
