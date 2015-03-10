package treehousecareerprojects.readie.http.request;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

import treehousecareerprojects.readie.MainActivity;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.json.SearchResponseParser;

/**
 * Created by Dan on 3/9/2015.
 */
public class RecommendationsRequest extends HttpRequest {
    public static final String BOOK_REVIEW_PATH_FORMAT =
            "http://api.usatoday.com/open/reviews/books/book/%s" +
            "?audiobooks=n&batch=n&encoding=json&api_key=%s";

    private MainActivity context;

    public RecommendationsRequest(MainActivity context, String apiKey) {
        super(HttpMethod.GET, encodeRequest(String.valueOf(getRandomLetter()), apiKey));

        this.context = context;
    }

    private static String encodeRequest(String query, String apiKey) {
        String url = "";

        try {
            url = String.format(
                    BOOK_REVIEW_PATH_FORMAT,
                    URLEncoder.encode(query, "UTF-8"),
                    URLEncoder.encode(apiKey, "UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            //TODO: Add error dialog
            e.printStackTrace();
        }

        return url;
    }

    private static char getRandomLetter() {    //Pick a random character to search for to get reviews.
        Random random = new Random();
        String letterChoices = "abcdefghijklmn";

        return letterChoices.charAt(random.nextInt(letterChoices.length()));
    }

    @Override
    public void onSuccess(HttpResponse response) {
        try {
            JSONObject json = new JSONObject(IOUtils.toString(response.getResponseBody(), "UTF-8"));
            context.updateRecommendations(SearchResponseParser.extractSearchResults(json).subList(0, 5));
        }
        catch (JSONException | IOException e) {
            context.hideRecommendations();
        }
    }

    @Override
    public void onFailure(HttpResponse response) {
        context.hideRecommendations();
    }
}
