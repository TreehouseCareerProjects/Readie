package treehousecareerprojects.readie.http.request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 3/9/2015.
 */
public class BookCoverRequest extends HttpRequest {
    public static final String BOOK_COVER_PATH_FORMAT = "http://covers.openlibrary.org/b/ISBN/%s-M.jpg";

    private SearchResultAdapter context;
    private SearchResult searchResult;

    public BookCoverRequest(SearchResultAdapter context, String isbn, SearchResult searchResult) {
        super(HttpMethod.GET, encodeRequest(isbn));

        this.context = context;
        this.searchResult = searchResult;
    }

    private static String encodeRequest(String isbn) {
        String url = "";

        try {
            url = String.format(BOOK_COVER_PATH_FORMAT,
                    URLEncoder.encode(isbn, "UTF-8"));
        }
        catch(UnsupportedEncodingException e) {
            //TODO: Add error dialog
        }

        return url;
    }

    @Override
    public void onSuccess(HttpResponse response) {
        searchResult.setBookCoverMedium(response.getResponseBody());

        context.notifyDataSetChanged();
    }

    @Override
    public void onFailure(HttpResponse response) {}
}
