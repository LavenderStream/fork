package org.demo.tiny;

import android.os.Bundle;
import android.util.Log;

import org.demo.tiny.databinding.ActivityMainBinding;
import org.fork.annotation.ForkLayoutId;
import org.fork.annotation.ForkPresenter;
import org.fork.mvp.ForkActivity;
import org.fork.mvp.Fork;

@ForkLayoutId(R.layout.activity_main)
@ForkPresenter(MainPersenter.class)
public class MainActivity extends ForkActivity<ActivityMainBinding, MainPersenter> implements MainContract.View {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fork.bind(this);

        binding.text.setText("haha");

        mvpPresenter.run();
    }

    @Override
    public void showMessage(String message) {
        Log.d(TAG, "showMessage: " + message);
    }
}
