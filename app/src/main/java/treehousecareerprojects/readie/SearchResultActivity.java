package treehousecareerprojects.readie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.dialog.ErrorDialogFragment;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.json.SearchResponseParser;
import treehousecareerprojects.readie.model.SearchResult;
import treehousecareerprojects.readie.model.SearchResultComparator;

//TODO: Extract repeated locals as properties
public class SearchResultActivity extends ActionBarActivity {
    public static final String SEARCH_RESULT_ID = "result";

    private static final String SEARCH_REQUEST_KEY =
            "xhhcf4dwtx55rj8y5cmx2t5t";
    private static final String SEARCH_REQUEST_FORMAT =
            "http://api.usatoday.com/open/reviews/books/book/%s" +
            "?audiobooks=n&batch=n&encoding=json&api_key=%s";

    private ProgressBar progressBar;
    private ListView listView;
    private List<SearchResult> searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        setupActionBarDropdown();

        listView = (ListView)findViewById(R.id.list);

        String searchQuery = getIntent().getStringExtra(MainActivity.SEARCH_QUERY_ID);
        HttpConnection.sendInBackground(new SearchHttpRequest(formatSearchRequest(searchQuery)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_result, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new OnNewQueryListener());
        searchView.setSubmitButtonEnabled(true);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBarDropdown() {
        Spinner spinner = (Spinner)findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.sort_menu_options, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new OnSortOptionsNavigation());
    }

    private String formatSearchRequest(String searchQuery) {
        String request = "";

        try {
            request = String.format(
                    SEARCH_REQUEST_FORMAT,
                    URLEncoder.encode(searchQuery, "UTF-8"),
                    SEARCH_REQUEST_KEY);
        }
        catch (UnsupportedEncodingException e) {
            displayTerminatingErrorDialog(
                    R.string.encoding_error_title,
                    R.string.encoding_error_message,
                    "encoding_error_dialog");
        }

        return request;
    }

    private void displayTerminatingErrorDialog(int title, int message, String dialogId) {
        Bundle dialogArgs = new Bundle();
        dialogArgs.putInt(ErrorDialogFragment.TITLE_ID, title);
        dialogArgs.putInt(ErrorDialogFragment.MESSAGE_ID, message);
        dialogArgs.putBoolean(ErrorDialogFragment.TERMINATE_ID, true);

        ErrorDialogFragment errorDialog = new ErrorDialogFragment();
        errorDialog.setArguments(dialogArgs);
        errorDialog.show(getFragmentManager(), dialogId);
    }

    private class SearchHttpRequest extends HttpRequest {
        public SearchHttpRequest(String url) {
            super(HttpMethod.GET, url);
        }

        @Override
        public void onSuccess(HttpResponse response) {
            progressBar.setVisibility(View.GONE);

            try {
                JSONObject json = new JSONObject(IOUtils.toString(response.getResponseBody(), "UTF-8"));
                searchResults = SearchResponseParser.extractSearchResults(json);

                listView.setAdapter(new SearchResultAdapter(SearchResultActivity.this, searchResults));
                listView.setOnItemClickListener(new SearchResultItemClickListener());
            }
            catch(IOException e) {
                displayTerminatingErrorDialog(
                        R.string.decoding_error_title,
                        R.string.decoding_error_message,
                        "decoding_error_dialog");
            }
            catch (JSONException e) {
                displayTerminatingErrorDialog(
                        R.string.json_error_title,
                        R.string.json_error_message,
                        "json_error_dialog");
            }
        }

        @Override
        public void onFailure(HttpResponse response) {
            progressBar.setVisibility(View.GONE);

            displayTerminatingErrorDialog(
                    R.string.connection_error_title,
                    R.string.connection_error_message,
                    "connection_error_dialog");
        }
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

                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {}
    }

    private class OnNewQueryListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String newQuery) {
            HttpConnection.sendInBackground(new SearchHttpRequest(formatSearchRequest(newQuery)));

            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            return false;
        }
    }
}
