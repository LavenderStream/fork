package org.fork.annotation;

/**
 * Created by tiny on 5/8/2017.
 */

public interface Provider {
    Object getPresenter(Object obj);
    int getLayoutId();
}
