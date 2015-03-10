package treehousecareerprojects.readie.http.request;

import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;

/**
 * Created by Dan on 3/9/2015.
 */
public class BookCoverRequest extends HttpRequest {
    public BookCoverRequest(HttpMethod httpMethod, String url) {
        super(httpMethod, url);
    }

    @Override
    public void onSuccess(HttpResponse response) {

    }

    @Override
    public void onFailure(HttpResponse response) {

    }
}
