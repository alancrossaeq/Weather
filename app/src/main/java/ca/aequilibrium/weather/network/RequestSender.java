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

import ca.aequilibrium.weather.BuildConfig;
import ca.aequilibrium.weather.models.Forecast;
import ca.aequilibrium.weather.models.Location;
import ca.aequilibrium.weather.utils.NetworkUtils;

public class RequestSender {

    public void getForecast(Location location, RequestSenderCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("lat", String.valueOf(location.getLatLng().latitude));
        params.put("lon", String.valueOf(location.getLatLng().longitude));
        params.put("units", "metric");
        params.put("appid", BuildConfig.WEATHER_API_ID);
        performRequestInBackground(NetworkConstants.openWeatherBaseUrl + NetworkConstants.openWeatherCurrentForecastPath,
                params, "GET", null, callback);
    }

    public void getFiveDayForecast(Location location, RequestSenderCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("lat", String.valueOf(location.getLatLng().latitude));
        params.put("lon", String.valueOf(location.getLatLng().longitude));
        params.put("units", "metric");
        params.put("appid", BuildConfig.WEATHER_API_ID);
        performRequestInBackground(NetworkConstants.openWeatherBaseUrl + NetworkConstants.openWeatherFiveDayForecastPath,
                params, "GET", null, callback);
    }

    public interface RequestSenderCallback {
        void onResponse(String result, String error);
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

    private class RequestTask extends AsyncTask<NetworkRequest, Void, Pair<String, String>> {

        private RequestSenderCallback mCallback;

        RequestTask(RequestSenderCallback callback) {
            this.mCallback = callback;
        }

        @Override
        protected Pair<String, String> doInBackground(NetworkRequest... params) {
            NetworkRequest request = params[0];

            if (request.getParameters() == null) {
                request.setParameters(new HashMap<String, String>());
            }

//            try {
//                URL url = new URL(request.getPath());
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod(request.getRequestMethod());
//
//            } catch (Exception e) {
//
//            }

            URL url;
            String response = "";
            String error = null;
            try {
                String queryParamString = NetworkUtils.urlEncodeUTF8(request.getParameters());
                url = new URL(request.getPath() + "?" + queryParamString);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod(request.getRequestMethod());
                conn.setDoInput(true);

                if (request.getBody() != null && (request.getRequestMethod().equals("POST") || request.getRequestMethod().equals("PUT") || request.getRequestMethod().equals("PATCH"))) {
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(request.getBody());

                    writer.flush();
                    writer.close();
                    os.close();
                }

                int responseCode = conn.getResponseCode();

                if (responseCode >= 200 && responseCode < 300) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response += line;
                    }
                }
                else {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                    while ((line=br.readLine()) != null) {
                        error += line;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new Pair(response, error);
        }

        @Override
        protected void onPostExecute(Pair<String, String> response) {
            super.onPostExecute(response);
//            if (this.mCallback != null) {
//                if (response != null) {
//                    this.mCallback.parseData(mGson, response.first, response.second);
//                } else {
//                    this.mCallback.onFailure(new Exception("Error occurred when trying to complete the request."));
//                }
//            }
            this.mCallback.onResponse(response.first, response.second);
        }
    }
}
