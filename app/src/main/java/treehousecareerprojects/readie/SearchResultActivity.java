package treehousecareerprojects.readie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.request.SearchRequest;
import treehousecareerprojects.readie.model.SearchResult;
import treehousecareerprojects.readie.model.SearchResultComparator;
import treehousecareerprojects.readie.view.SearchResultActionBar;


public class SearchResultActivity extends ActionBarActivity {
    public static final String SEARCH_RESULT_ID = "result";

    private ProgressBar progressBar;
    private ListView listView;
    private List<SearchResult> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        searchResults = new ArrayList<>();

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        SearchResultActionBar actionBar = new SearchResultActionBar(this, getSupportActionBar());
        actionBar.assembleActionBar();
        actionBar.setOnNewQueryListener(new OnNewSearchQuery());
        actionBar.setOnSortListener(new OnSortOptionsNavigation());

        listView = (ListView)findViewById(R.id.list);
        listView.setOnItemClickListener(new SearchResultItemClickListener());
        listView.setAdapter(new SearchResultAdapter(this, searchResults));

        String searchQuery = getIntent().getStringExtra(MainActivity.SEARCH_QUERY_ID);
        HttpConnection.sendInBackground(
                new SearchRequest(this, searchQuery, getString(R.string.api_key), listView));
    }

    public void toggleProgressBar() {
        if(progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }

    public void updateSearchResults(List<SearchResult> searchResults) {
        this.searchResults.clear();
        this.searchResults.addAll(searchResults);

        listView.setAdapter(new SearchResultAdapter(this, this.searchResults));
    }

    private class SearchResultItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent reviewIntent = new Intent(SearchResultActivity.this, ReviewActivity.class);
            reviewIntent.putExtra(SEARCH_RESULT_ID, (SearchResult)(listView.getAdapter().getItem(position)));
            startActivity(reviewIntent);
        }
    }

    private class OnSortOptionsNavigation implements Spinner.OnItemSelectedListener {
        private String[] menuOptions = getResources().getStringArray(R.array.sort_menu_options);

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(searchResults != null) {
                if (menuOptions[position].equals("Title"))
                    Collections.sort(
                            searchResults,
                            new SearchResultComparator(SearchResultComparator.SortBy.TITLE));
                else
                    Collections.sort(
                            searchResults,
                            new SearchResultComparator(SearchResultComparator.SortBy.AUTHOR));

                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class OnNewSearchQuery implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            HttpConnection.sendInBackground(
                    new SearchRequest(SearchResultActivity.this, query, getString(R.string.api_key), listView));

            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    }
}
