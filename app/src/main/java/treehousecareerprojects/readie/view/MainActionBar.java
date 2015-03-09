package treehousecareerprojects.readie.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import treehousecareerprojects.readie.MainActivity;
import treehousecareerprojects.readie.R;
import treehousecareerprojects.readie.SearchResultActivity;

/**
 * Created by Dan on 3/8/2015.
 */
public class MainActionBar extends ReadieActionBar {
    public MainActionBar(Activity activity, ActionBar actionBar) {
        super(activity, actionBar);
    }

    @Override
    public void assembleActionBar() {
        init(R.layout.action_bar_main);
        rewriteSearchView(R.id.mainSearch);

        SearchView search = (SearchView)activity.findViewById(R.id.mainSearch);
        search.setOnQueryTextListener(new OnQueryListener());
    }

    private class OnQueryListener implements SearchView.OnQueryTextListener {
        private boolean isQueryValid(String query) {
            return !query.trim().equals("");
        }

        private void startSearchResultActivity(String query) {
            Intent searchIntent = new Intent(activity, SearchResultActivity.class);
            searchIntent.putExtra(MainActivity.SEARCH_QUERY_ID, query);
            activity.startActivity(searchIntent);
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            if(isQueryValid(query))
                startSearchResultActivity(query);
            else
                Toast.makeText(activity, R.string.query_empty_alert, Toast.LENGTH_LONG).show();

            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    }
}
