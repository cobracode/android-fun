package ned.androidfun;

import android.util.Log;
import android.widget.TextView;

class Logger {
    private static TextView text = null;
    private static int count = 0;

    public static void setText(final TextView inText) {
        text = inText;
    }

    public static void log(final String tag, final String msg) {
        if (null != text) {
            Log.i(tag, msg);
            final String existingText = text.getText().toString();
            text.setText(Util.getTimestamp() + "|" + ++count + "|" + msg + "\n" + existingText);
        }
    }

    public static void printSay(final String msg) {
        // Print to TextView and say out loud
        Logger.log("", msg);
        Parrot.say(msg);
    }

    public static void resetCount() {
        count = 0;
    }
}