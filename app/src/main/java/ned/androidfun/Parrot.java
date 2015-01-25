package ned.androidfun;

import android.content.Context;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;

class Parrot implements TextToSpeech.OnInitListener {
    private static final String TAG = "Parrot";
    private static TextToSpeech tts = null;

    public Parrot(final Context context) {
        Logger.log(TAG, "Parrot() begin");
        tts = new TextToSpeech(context, this);
        Logger.log(TAG, "Parrot() end");
    }

    public void close() {
        Logger.log(TAG, "close() begin");
        say("Voice Deactivating.");
        while (tts.isSpeaking()) {
            SystemClock.sleep(500);
        }
        tts.stop();
        tts.shutdown();
        tts = null;
        Logger.log(TAG, "close() end");
    }

    @Override
    public void onInit(final int status) {
        Logger.log(TAG, "onInit() begin");
        if (TextToSpeech.SUCCESS == status) {
            try {
                say("Hello! Voice activated.");
            }
            catch (final Exception e) {
                Log.e(TAG, "onInit(): " + e);
                close();
            }
        }
        else {
            Log.e(TAG, "onInit(): initialization failure");
            close();
        }
        Logger.log(TAG, "onInit() end");
    }

    public static void say(final String spokenText) {
        if (null != tts) {
            tts.speak(spokenText, TextToSpeech.QUEUE_ADD, null);
            Log.v(TAG, "say(): \"" + spokenText + "\"");
        }
    }
}