package treehousecareerprojects.readie.view;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;

import treehousecareerprojects.readie.R;

/**
 * Created by Dan on 3/8/2015.
 */
public class SearchResultActionBar extends ReadieActionBar {
    public SearchResultActionBar(Activity activity, ActionBar actionBar) {
        super(activity, actionBar);
    }

    @Override
    public void assembleActionBar() {
        init(R.layout.action_bar_search_result);
        rewriteSearchView(R.id.searchResultSearch);
    }

    public void setOnNewQueryListener(SearchView.OnQueryTextListener listener) {
        SearchView search = (SearchView)activity.findViewById(R.id.searchResultSearch);
        search.setOnQueryTextListener(listener);
    }

}
