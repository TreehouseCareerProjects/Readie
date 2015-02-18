package treehousecareerprojects.readie.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 2/18/2015.
 */
public final class SearchResponseParser {
    public static List<SearchResult> extractSearchResults(JSONObject json) throws JSONException {
        List<SearchResult> results = new ArrayList<>();
        JSONArray reviews = json.getJSONArray(SearchResult.REVIEW_ARRAY_JSON_ID);

        for(int i = 0; i < reviews.length(); i++) {
            SearchResult result = new SearchResult();
            JSONObject review = reviews.getJSONObject(i);

            result.setBookTitle(review.getString(SearchResult.BOOK_TITLE_JSON_ID));
            result.setReviewer(review.getString(SearchResult.REVIEWER_JSON_ID));
            result.setReviewSnippet(review.getString(SearchResult.REVIEW_SNIPPET_JSON_ID));
            result.setReviewUrl(review.getString(SearchResult.REVIEW_URL_JSON_ID));

            results.add(result);
        }

        return results;
    }
}
