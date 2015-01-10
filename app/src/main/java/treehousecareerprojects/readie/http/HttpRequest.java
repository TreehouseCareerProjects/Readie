package treehousecareerprojects.readie.http;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dan on 1/9/2015.
 */
public abstract class HttpRequest {
    protected HttpMethod httpMethod;
    protected URL url;

    public HttpRequest(HttpMethod httpMethod, URL url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public HttpRequest(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        try {
            this.url = new URL(url);
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public URL getUrl() {
        return url;
    }

    public abstract void onSuccess(HttpResponse response);
    public abstract void onFailure(HttpResponse response);
}
