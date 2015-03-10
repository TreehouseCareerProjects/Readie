package treehousecareerprojects.readie.http.request;

import android.widget.ListView;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import treehousecareerprojects.readie.SearchResultActivity;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.json.SearchResponseParser;
import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 3/9/2015.
 */
public class SearchRequest extends HttpRequest {
    public static final String SEARCH_REQUEST_FORMAT =
            "http://api.usatoday.com/open/reviews/books/book/%s" +
            "?audiobooks=n&batch=n&encoding=json&api_key=%s";

    private SearchResultActivity context;
    private ListView listView;

    public SearchRequest(SearchResultActivity context, String query, String apiKey, ListView listView) {
        super(HttpMethod.GET, encodeRequest(query, apiKey));

        this.context = context;
        this.listView = listView;
    }

    private static String encodeRequest(String query, String apiKey) {
        String url = "";

        try {
            url = String.format(SEARCH_REQUEST_FORMAT,
                    URLEncoder.encode(query, "UTF-8"),
                    URLEncoder.encode(apiKey, "UTF-8"));
        }
        catch(UnsupportedEncodingException e) {

        }

        return url;
    }

    @Override
    public void onSuccess(HttpResponse response) {
        context.toggleProgressBar();

        try {
            JSONObject json = new JSONObject(IOUtils.toString(response.getResponseBody(), "UTF-8"));
            List<SearchResult> searchResults = SearchResponseParser.extractSearchResults(json);

            context.updateSearchResults(searchResults);
        }
        catch(IOException e) {

        }
        catch (JSONException e) {

        }
    }

    @Override
    public void onFailure(HttpResponse response) {
        context.toggleProgressBar();
    }
}
