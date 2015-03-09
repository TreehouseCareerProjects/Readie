package treehousecareerprojects.readie.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import treehousecareerprojects.readie.MainActivity;
import treehousecareerprojects.readie.R;
import treehousecareerprojects.readie.SearchResultActivity;

/**
 * Created by Dan on 3/8/2015.
 */
public class ReviewActionBar extends ReadieActionBar {
    public ReviewActionBar(Activity activity, ActionBar actionBar) {
        super(activity, actionBar);
    }

    @Override
    public void assembleActionBar() {
        init(R.layout.action_bar_review);
        rewriteSearchView(R.id.reviewSearch);
        attachCallbacks();
    }

    private void attachCallbacks() {
        ImageView backIcon = (ImageView)activity.findViewById(R.id.reviewBackIcon);
        backIcon.setOnClickListener(new OnBackPressed());

        SearchView searchView = (SearchView)activity.findViewById(R.id.reviewSearch);
        searchView.setOnQueryTextListener(new OnNewSearchQuery());
    }


    private class OnBackPressed implements ImageView.OnClickListener {
        @Override
        public void onClick(View v) {
            activity.finish();
        }
    }

    private class OnNewSearchQuery implements SearchView.OnQueryTextListener {
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

            activity.finish();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    }
}
