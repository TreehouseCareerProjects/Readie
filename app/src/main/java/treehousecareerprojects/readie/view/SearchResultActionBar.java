package treehousecareerprojects.readie.view;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        Spinner spinner = (Spinner)activity.findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(activity, R.array.sort_menu_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public void setOnSortListener(Spinner.OnItemSelectedListener listener) {
        Spinner spinner = (Spinner)activity.findViewById(R.id.sortSpinner);
        spinner.setOnItemSelectedListener(listener);
    }

    public void setOnNewQueryListener(SearchView.OnQueryTextListener listener) {
        SearchView search = (SearchView)activity.findViewById(R.id.searchResultSearch);
        search.setOnQueryTextListener(listener);
    }

}
