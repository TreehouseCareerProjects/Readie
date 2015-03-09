package treehousecareerprojects.readie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import treehousecareerprojects.readie.view.MainActionBar;


public class MainActivity extends ActionBarActivity {
    public static final String SEARCH_QUERY_ID = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActionBar actionBar = new MainActionBar(this, getSupportActionBar());
        actionBar.assembleActionBar();
    }
}
