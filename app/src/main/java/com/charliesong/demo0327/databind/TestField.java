package com.charliesong.demo0327.databind;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.charliesong.demo0327.kt.Student;

public class TestField {

    ObservableField<String>  name=new ObservableField<>();

    ObservableField<Student>  student=new ObservableField<>();

    ObservableInt  age=new ObservableInt();
    ObservableBoolean  male=new ObservableBoolean();
}
