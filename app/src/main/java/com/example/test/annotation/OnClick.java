package com.example.test.annotation;


import android.view.View;

import androidx.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@EventType(listenerType = View.OnClickListener.class, listenerSetter = "setOnClickListener")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClick {
    @IdRes int[] myValue();
}
