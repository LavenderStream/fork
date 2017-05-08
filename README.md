```
@ForkLayoutId(R.layout.activity_main)
@ForkPresenter(MainPresenter.class)
public class MainActivity extends ForkActivity<ActivityMainBinding, MainPresenter> implements MainContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fork.bind(this);

        binding.rvText.setText("haha");
        mvpPresenter.run();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
```
