package ned.androidfun;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

class Network {
    private static final String TAG = "Network";
    private static Context context = null;
    private static RequestQueue requests = null;
    private static int requestCount = 0;

    public static void initialize(final Context newContext) {
        context = newContext;

        try {
            requests = Volley.newRequestQueue(context);
        } catch (final NullPointerException e) {
            final String error = "Error initializing Volley request queue: empty context";
            Log.e(TAG, error);
            Logger.log(TAG, error);
        } catch (final Exception e) {
            final String error = "Error initializing Volley request queue: " + e;
            Log.e(TAG, error);
            Logger.log(TAG, error);
        }
    }

    public static void getHello() {
        final StringRequest request = new StringRequest(
                "http://www.scienceofspirituality.info/files/hello",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (response.equals("hi there!\n\n")) {
                            Logger.printSay("Correct response");
                        } else {
                            Logger.printSay("Invalid response");
                        }
                        Logger.log(TAG, "sendHello()::onResponse():: response = " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError volleyError) {
                        Logger.printSay("Error in request: " + volleyError);
                    }
                });

        addRequest(request);
    }

    public static void sendLog(final String URL, final String tag, final String log) {
        final String safeTag = tag.isEmpty() ? "generic" : tag;

        final StringRequest request = new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.v(TAG, "sendLog(): Post response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError volleyError) {
                        Log.e(TAG, "sendLog()::onErrorResponse(): Error in post: " + volleyError);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(safeTag, log);
                return params;
            };
        };

        addRequest(request);
    }

    public static void sendPoem() {
        final StringRequest request = new StringRequest(
                Request.Method.POST,
                "http://www.scienceofspirituality.info/files/receiver.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.v(TAG, "sendPoem(): Post response: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError volleyError) {
                        Logger.printSay("Error in post: " + volleyError);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("poem", "my love was fair like the wind was pale; my world flew");
                params.put("fishster", "bearcat");
                return params;
            };
        };

        addRequest(request);
    }

    public static void cancelRequests() {
        Log.v(TAG, "cancelRequests(): Cancelling all requests with tag \"" + TAG + "\"");
        requests.cancelAll(TAG);
    }

    private static void addRequest(final Request request) {
        Log.v(TAG, "Adding request " + requestCount);
        request.setTag(TAG);
        request.setSequence(requestCount++);

        try {
            requests.add(request);
        } catch (final NullPointerException e) {
            final String error = "ERROR: Attempted to add network request to uninitialized container";
            Log.w(TAG, error);
            Logger.printSay(error);
        } catch (final Exception e) {
            final String error = "Error while adding request to queue: " + e;
            Log.w(TAG, error);
            Logger.printSay(error);
        }
    }
}
