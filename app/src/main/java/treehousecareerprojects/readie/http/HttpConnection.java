package treehousecareerprojects.readie.http;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Dan on 1/9/2015.
 */
public class HttpConnection {
    private static int clientTimeout = 10000;

    public static void setClientTimeout(int milliseconds) {
        if(milliseconds < 0)
            throw new IllegalArgumentException("The client timeout cannot be negative.");

        clientTimeout = milliseconds;
    }

    public static void sendInBackground(HttpRequest request) {
        (new BackgroundRequest()).execute(request);
    }

    private static HttpResponse send(HttpRequest request) {
        HttpResponse response;

        try {
            HttpURLConnection connection = (HttpURLConnection)request.getUrl().openConnection();
            connection.setRequestMethod(request.getHttpMethod().name());
            connection.setDoInput(true);
            connection.setReadTimeout(clientTimeout);
            connection.connect();

            response = new HttpResponse(
                    connection.getHeaderFields(),
                    IOUtils.toString(connection.getInputStream(), connection.getContentEncoding()),
                    connection.getResponseCode());

            connection.disconnect();
        }
        catch(java.net.SocketTimeoutException exception) {
            Log.d(HttpConnection.class.getSimpleName(), exception.getMessage());

            response = new HttpResponse(null, null, HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        }
        catch(IOException exception) {
            Log.d(HttpConnection.class.getSimpleName(), exception.getMessage());

            response = new HttpResponse(null, null, HttpURLConnection.HTTP_BAD_REQUEST);
        }

        return response;
    }

    private static void notifyRequest(HttpRequest request, HttpResponse response) {
        switch(response.getResponseCode()) {
            case HttpURLConnection.HTTP_OK:
            case HttpURLConnection.HTTP_ACCEPTED:
            case HttpURLConnection.HTTP_CREATED:
            case HttpURLConnection.HTTP_NO_CONTENT:
            case HttpURLConnection.HTTP_NOT_AUTHORITATIVE:
            case HttpURLConnection.HTTP_NOT_MODIFIED:		//Not modified treated as success
            case HttpURLConnection.HTTP_PARTIAL:
            case HttpURLConnection.HTTP_RESET:
                request.onSuccess(response);
                break;
            default:
                request.onFailure(response);
                break;
        }
    }

    private static class BackgroundRequest extends AsyncTask<HttpRequest, Void, HttpResponse> {
        private HttpRequest request;

        @Override
        protected HttpResponse doInBackground(HttpRequest... request) {
            this.request = request[0];

            return send(this.request);
        }

        @Override
        protected void onPostExecute(HttpResponse response) {
            super.onPostExecute(response);

            notifyRequest(request, response);
        }
    }
}
