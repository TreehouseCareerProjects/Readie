package treehousecareerprojects.readie.http;

import java.util.List;
import java.util.Map;

/**
 * Created by Dan on 1/9/2015.
 */
public class HttpResponse {
    private final Map<String, List<String>> responseHeaders;
    private final String responseBody;
    private final int responseCode;

    public HttpResponse(Map<String, List<String>> responseHeaders,
                        String responseBody,
                        int responseCode) {
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
