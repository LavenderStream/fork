package org.fork.mvp;

import android.app.Activity;
import android.databinding.ViewDataBinding;

/**
 * Created by tiny on 5/8/2017.
 */
public class ForkActivity<B extends ViewDataBinding, P> extends Activity {
    protected B binding;
    protected P mvpPresenter;
}
