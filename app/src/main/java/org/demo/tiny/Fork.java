package org.demo.tiny;

import android.app.Activity;
import android.databinding.DataBindingUtil;

import com.example.Provider;


/**
 * Created by tiny on 5/8/2017.
 */

public final class Fork {
    private static final String TAG = "Fork";

    public static void bind(Activity activity) {
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
            provider.inject(activity);
            DataBindingUtil.setContentView(activity, provider.getLayoutId());
        }

    }
}
