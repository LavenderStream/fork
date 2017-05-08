package com.example;

/**
 * Created by tiny on 5/8/2017.
 */

public interface Provider {
    void inject(Object obj);

    int getLayoutId();

    Object getViewBinding();
}
