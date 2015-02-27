package treehousecareerprojects.readie;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.json.SearchResponseParser;
import treehousecareerprojects.readie.model.SearchResult;


public class SearchResultActivity extends ListActivity {
    public static final String SEARCH_RESULT_ID = "result";

    private static final String SEARCH_REQUEST_KEY =
            "xhhcf4dwtx55rj8y5cmx2t5t";
    private static final String SEARCH_REQUEST_FORMAT =
            "http://api.usatoday.com/open/reviews/books/book/%s" +
            "?audiobooks=n&batch=n&encoding=json&api_key=%s";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        String searchQuery = getIntent().getStringExtra(MainActivity.SEARCH_QUERY_ID);
        progressBar.setVisibility(View.VISIBLE);
        HttpConnection.sendInBackground(new SearchHttpRequest(formatSearchRequest(searchQuery)));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent reviewIntent = new Intent(this, ReviewActivity.class);
        reviewIntent.putExtra(SEARCH_RESULT_ID, (SearchResult)(getListAdapter().getItem(position)));
        startActivity(reviewIntent);
    }

    private String formatSearchRequest(String searchQuery) {
        //TODO: Encode query
        return String.format(SEARCH_REQUEST_FORMAT, searchQuery, SEARCH_REQUEST_KEY);
    }

    private class SearchHttpRequest extends HttpRequest {
        public SearchHttpRequest(String url) {
            super(HttpMethod.GET, url);
        }

        @Override
        public void onSuccess(HttpResponse response) {
            List<SearchResult> searchResults = new ArrayList<>();

            progressBar.setVisibility(View.GONE);

            try {
                JSONObject json = new JSONObject(response.getResponseBody());
                searchResults = SearchResponseParser.extractSearchResults(json);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            setListAdapter(new SearchResultAdapter(SearchResultActivity.this, searchResults));
        }

        @Override
        public void onFailure(HttpResponse response) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SearchResultActivity.this);
            dialogBuilder
                    .setTitle(R.string.connection_error_title)
                    .setMessage(R.string.connection_error_message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            });

             dialogBuilder.create().show();
        }
    }
}
