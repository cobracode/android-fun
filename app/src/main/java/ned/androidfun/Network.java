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
import java.util.LinkedList;
import java.util.Map;

class Network implements InternetListener {
    // Core
    private static final String TAG = "Network";
    private static Context context = null;

    // State
    private static int requestCount = 0;
    private static RequestQueue requests = null;
    private static LinkedList<Request> storedRequests = new LinkedList<Request>();
    private static String previousMsg;

    // Constants
    private static final String SITE = "http://www.scienceofspirituality.info";
    public static final String SITE_HELLO = SITE + "/files/hello";
    private static final String SITE_RECEIVER = SITE + "/files/receiver.php";
    private static final String SITE_RESPONSE_SUCCESS = "Success";

    public Network(final Context context) {
        initialize(context);
    }

    public static void initialize(final Context newContext) {
        Log.v(TAG, "initialize() begin");
        context = newContext;
        requestCount = 0;
        storedRequests.clear();

        try {
            requests = Volley.newRequestQueue(context);
            Log.i(TAG, "Initialized Volley request queue with new context");
        } catch (final NullPointerException e) {
            final String error = "Error initializing Volley request queue: empty context";
            Log.e(TAG, error);
            Logger.log(TAG, error);
        } catch (final Exception e) {
            final String error = "Error initializing Volley request queue: " + e;
            Log.e(TAG, error);
            Logger.log(TAG, error);
        }

        Log.v(TAG, "initialize() end");
    }

    public static void getHello() {
        final StringRequest request = new StringRequest(
                SITE_HELLO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        if (response.equals("hi there!\n\n")) {
                            Logger.printSay("Correct response");
                        } else {
                            Logger.printSay("Invalid response");
                            Logger.log(TAG, "onResponse(): " + response);
                        }
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

    public static void sendToSite(final String text) {
        // Sometimes the same string is sent multiple times. Prevent that.
        if (!text.equals(previousMsg)) {
            // Add timestamp to text
            final String timedMsg = Util.getTimestamp() + "|" + text;

            final StringRequest request = new StringRequest(
                    Request.Method.POST,
                    SITE_RECEIVER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String response) {
                            if (!response.equals(SITE_RESPONSE_SUCCESS)) {
                                Logger.printSay("Site sent unexpected response");
                                Logger.log(TAG, "onResponse(): " + response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError volleyError) {
                            Log.w(TAG, "sendToSite(): " + volleyError);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sendToSite", timedMsg);
                    return params;
                }
            };

            addRequest(request);
        } else {
            Log.d(TAG, "sendToSite(): Discarding duplicate message: " + text);
        }

        previousMsg = text;
    }

    public static void cancelRequests() {
        Log.i(TAG, "cancelRequests(): Cancelling all Volley requests with tag \"" + TAG + "\"");
        requests.cancelAll(TAG);
    }

    private static void addRequest(final Request request) {
        // Assign request stats
        request.setSequence(requestCount++);
        request.setTag(TAG);

        // Only add request if internet available;
        // if not, add to internal queue
        if (IConnectivityManager.internetAvailable()) {
            sendRequest(request);
        } else {
            Log.w(TAG, "addRequest(): no internet; storing request " + request.getSequence());
            storedRequests.add(request);
        }
    }

    private static void sendRequest(final Request request) {
        try {
            requests.add(request);
            Log.d(TAG, "sendRequest(): Sent request " + request.getSequence());
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

    @Override
    public void internetOn() {
        Log.d(TAG, "internetOn(): flushing internal requests queue");

        // Send all requests in queue
        for (int i = 0; i < storedRequests.size(); i++) {
            sendRequest(storedRequests.remove());
        }
    }

    @Override
    public void internetOff() {
        Log.d(TAG, "internetOff(): -");
    }
}
