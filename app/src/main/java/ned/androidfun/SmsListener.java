package ned.androidfun;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

public class SmsListener extends BroadcastReceiver {
    private static final String TAG = "SmsListener";

    public SmsListener() {}

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.v(TAG, "onReceive() begin");

        final SmsMessage messages[] = getMessagesFromIntent(intent);
        final String from = Contacts.getContactName(context, messages[0].getOriginatingAddress());

        String fullText = from + " says";

        for (final SmsMessage message : messages) {
            fullText += " " + message.getMessageBody();
        }

        Logger.log(TAG, fullText);
        Parrot.say(fullText);

        Log.v(TAG, "onReceive() end");
    }
}
