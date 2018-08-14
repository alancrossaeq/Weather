package ca.aequilibrium.weather.network;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RequestSender {

    public interface RequestSenderCallback {
        void onResponse(Object result, String x);
    }

    public void performRequestInBackground(String path, Map<String, String> parameters, String requestMethod, String body, RequestSenderCallback callback) {
//        Request request = new DefaultRequest(SERVICE_NAME);
////        request.setResourcePath(path);
////        if (parameters != null) {
////            request.setParameters(parameters);
////        }
////        request.setEndpoint(mEndpointURI);
////        request.setHttpMethod(requestType);
////
////        if (body != null) {
////            byte[] content = body.getBytes(UTF8);
////            try {
////                request.setContent(new StringInputStream(body));
////            } catch (Exception e) {
////                Log.e(TAG, e.getMessage());
////            }
////            request.addHeader("Content-Length", Integer.toString(content.length));
////        }
////
////        request.addHeader("Content-Type", "application/json; charset=UTF-8");
////        request.addHeader("Accept", "application/json");

        NetworkRequest networkRequest = new NetworkRequest();
        networkRequest.setParameters(parameters);
        networkRequest.setPath(path);
        networkRequest.setRequestMethod(requestMethod);
        networkRequest.setBody(body);

        new RequestTask(callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, networkRequest);
    }

    private class RequestTask extends AsyncTask<NetworkRequest, Void, Pair<String, Exception>> {

        private RequestSenderCallback mCallback;

        RequestTask(RequestSenderCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected Pair<String, Exception> doInBackground(NetworkRequest... params) {
            NetworkRequest request = params[0];

            if (request.getParameters() == null) {
                request.setParameters(new HashMap<String, String>());
            }

            try {
                URL url = new URL(request.getPath());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod(request.getRequestMethod());

            } catch (Exception e) {

            }

            URL url;
            String response = "";
            try {
                url = new URL(request.getPath());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod(request.getRequestMethod());
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(request.getBody());

                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new Pair(response, null);
        }

        @Override
        protected void onPostExecute(Pair<String, Exception> response) {
            super.onPostExecute(response);
//            if (this.mCallback != null) {
//                if (response != null) {
//                    this.mCallback.parseData(mGson, response.first, response.second);
//                } else {
//                    this.mCallback.onFailure(new Exception("Error occurred when trying to complete the request."));
//                }
//            }
            this.mCallback.onResponse(response, null);
        }
    }
}
