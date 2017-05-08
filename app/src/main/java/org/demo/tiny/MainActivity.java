package org.demo.tiny;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.ForkLayoutId;
import com.example.ForkPresenter;

import org.demo.tiny.databinding.ActivityMainBinding;


public class MainActivity extends Activity implements MainContract.View {

    private static final String TAG = "MainActivity";

    @ForkLayoutId(R.layout.activity_main)
    ActivityMainBinding binding;

    @ForkPresenter(MainPersenter.class)
    MainPersenter persenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fork.bind(this);

        persenter.run();
    }

    @Override
    public void showMessage(String message) {
        Log.d(TAG, "showMessage: " + message);
    }
}
