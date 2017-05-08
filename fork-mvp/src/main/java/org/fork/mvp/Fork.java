package org.fork.mvp;

import android.databinding.DataBindingUtil;

import org.fork.annotation.Provider;


/**
 * Created by tiny on 5/8/2017.
 */

public final class Fork {
    private static final String TAG = "Fork";

    public static void bind(ForkActivity activity) {
        Provider provider = null;
        try {
            try {
                provider = (Provider) Class.forName(activity.getClass().getName() + "$$Provider").newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (provider != null) {
            activity.binding = DataBindingUtil.setContentView(activity, provider.getLayoutId());
            activity.mvpPresenter = provider.getPresenter(activity);
        }

    }
}
