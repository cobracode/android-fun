package ned.androidfun;


import android.os.Bundle;
import android.util.Log;

class Util {
    private static final String TAG = "Util";

    public static void printAllBundleExtras(final Bundle bundle) {
        for (final String key : bundle.keySet()) {
            Log.v(TAG, "printAllBundleExtras(): \"" + key + "\" = \"" + bundle.get(key).toString() + "\"");
        }
    }
}
