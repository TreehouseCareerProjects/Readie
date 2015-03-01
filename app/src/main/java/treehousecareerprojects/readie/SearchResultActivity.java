package treehousecareerprojects.readie;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.dialog.ErrorDialogFragment;
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
                JSONObject json = new JSONObject(response.getResponseBody());
                List<SearchResult> searchResults = SearchResponseParser.extractSearchResults(json);

                setListAdapter(new SearchResultAdapter(SearchResultActivity.this, searchResults));
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
}
