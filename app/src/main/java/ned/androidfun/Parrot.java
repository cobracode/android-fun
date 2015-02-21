package ned.androidfun;

import android.content.Context;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.util.Log;

class Parrot implements TextToSpeech.OnInitListener {
    private static final String TAG = "Parrot";
    private static TextToSpeech tts = null;

    public Parrot(final Context context) {
        Log.v(TAG, "Parrot() begin");
        tts = new TextToSpeech(context, this);
        Log.v(TAG, "Parrot() end");
    }

    public void close() {
        Log.v(TAG, "close() begin");
        say("Voice Deactivating");
        while (tts.isSpeaking()) {
            SystemClock.sleep(250);
        }
        tts.stop();
        tts.shutdown();
        tts = null;
        Log.v(TAG, "close() end");
    }

    @Override
    public void onInit(final int status) {
        Log.v(TAG, "onInit() begin");
        if (TextToSpeech.SUCCESS == status) {
            try {
                say("Voice activated");
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
        Log.v(TAG, "onInit() end");
    }

    public static void say(final String spokenText) {
        try {
            tts.speak(spokenText, TextToSpeech.QUEUE_ADD, null);
            Log.v(TAG, "say(): \"" + spokenText + "\"");
        } catch (final NullPointerException e) {
            final String error = "ERROR: Attempted to speak with uninitialized voice.";
            Log.e(TAG, error);
            Logger.log(TAG, error);
        } catch (final Exception e) {
            final String error = "Error while attempting to speak: " + e;
            Log.w(TAG, error);
            Logger.log(TAG, error);
        }
    }
}