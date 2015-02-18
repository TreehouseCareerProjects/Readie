package treehousecareerprojects.readie;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.model.SearchResult;


public class SearchResultActivity extends ListActivity {
    private static final String SEARCH_REQUEST_KEY =
            "68ehz8kqmje9uyw96abqp7p2";
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
    }

    private String formatSearchRequest(String searchQuery) {
        return String.format(SEARCH_REQUEST_FORMAT, searchQuery, SEARCH_REQUEST_KEY);
    }

    private class SearchHttpRequest extends HttpRequest {
        public SearchHttpRequest(String url) {
            super(HttpMethod.GET, url);
        }

        @Override
        public void onSuccess(HttpResponse response) {
            progressBar.setVisibility(View.GONE);

            List<SearchResult> searchResults = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(response.getResponseBody());
                JSONArray bookReviews = jsonObject.getJSONArray(SearchResult.REVIEW_ARRAY_JSON_ID);

                for(int i = 0; i < bookReviews.length(); i++) {
                    SearchResult result = new SearchResult();
                    JSONObject review = bookReviews.getJSONObject(i);

                    result.setBookTitle(review.getString(SearchResult.BOOK_TITLE_JSON_ID));
                    result.setReviewer(review.getString(SearchResult.REVIEWER_JSON_ID));
                    result.setReviewSnippet(review.getString(SearchResult.REVIEW_SNIPPET_JSON_ID));

                    searchResults.add(result);
                }

                setListAdapter(new SearchResultAdapter(SearchResultActivity.this, searchResults));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(HttpResponse response) {
            //Add Dialog
        }
    }
}
