package treehousecareerprojects.readie.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 2/18/2015.
 */
public final class SearchResponseParser {
    private static final Pattern snippetPattern = Pattern.compile("<p>(.*)</p>");
    private static final int SNIPPET_REGEX_ID = 1;

    public static List<SearchResult> extractSearchResults(JSONObject json) throws JSONException {
        List<SearchResult> results = new ArrayList<SearchResult>();
        JSONArray reviews = json.getJSONArray(SearchResult.REVIEW_ARRAY_JSON_ID);

        for(int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);

            if(!isBatchOrAudioReview(review) && hasReviewPath(review)) {
                SearchResult result = new SearchResult();
                result.setBookTitle(review.getString(SearchResult.BOOK_TITLE_JSON_ID));
                result.setReviewer(review.getString(SearchResult.REVIEWER_JSON_ID));
                result.setAuthor(review.getString(SearchResult.AUTHOR_JSON_ID));
                result.setReviewUrl(review.getString(SearchResult.REVIEW_URL_JSON_ID));
                result.setIsbn(review.getString(SearchResult.ISBN_JSON_ID));
                result.setReviewSnippet(cleanReviewSnippet(review.getString(SearchResult.REVIEW_SNIPPET_JSON_ID)));

                results.add(result);
            }
        }

        return results;
    }

    private static String cleanReviewSnippet(String snippet) {
        Matcher result = snippetPattern.matcher(snippet);
        result.find();

        String body = "";
        try {
            body = result.group(SNIPPET_REGEX_ID);
        }
        catch(IllegalStateException e) {
            body = snippet;
        }

        return body;
    }

    private static boolean isBatchOrAudioReview(JSONObject review) throws JSONException {
        final boolean isAudio = review.getBoolean(SearchResult.AUDIO_REVIEW_STATUS_JSON_ID);
        final boolean isBatch = review.getBoolean(SearchResult.BATCH_REVIEW_STATUS_JSON_ID);

        return isAudio || isBatch;
    }

    private static boolean hasReviewPath(JSONObject review) throws JSONException {
        final boolean hasPath = !review.getString(SearchResult.REVIEW_URL_JSON_ID).trim().equals("");

        return hasPath;
    }
}
