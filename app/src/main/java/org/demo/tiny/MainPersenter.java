package org.demo.tiny;

/**
 * Created by tiny on 5/8/2017.
 */

public class MainPersenter {
    private MainContract.View mvpView;

    public MainPersenter(MainContract.View view) {
        mvpView = view;
    }

    public void run(){
        mvpView.showMessage("haha");
    }
}
