package treehousecareerprojects.readie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    public static final String SEARCH_QUERY_ID = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.imageView).setOnClickListener(new OnSearchButtonClick());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search_main);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setSubmitButtonEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    private boolean isQueryValid(String query) {
        final boolean notEmpty = !query.trim().equals("");

        return notEmpty;
    }

    private class OnSearchButtonClick implements ImageView.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText searchField = (EditText)findViewById(R.id.searchField);
            String query = searchField.getText().toString();

            if(isQueryValid(query)) {
                Intent searchIntent = new Intent(MainActivity.this, SearchResultActivity.class);
                searchIntent.putExtra(SEARCH_QUERY_ID, query);
                startActivity(searchIntent);
            }
            else {
                Toast.makeText(MainActivity.this, R.string.query_empty_alert, Toast.LENGTH_LONG)
                     .show();
            }
        }
    }
}
