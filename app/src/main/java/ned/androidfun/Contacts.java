package ned.androidfun;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.HashMap;


final class Contacts {
    private static final String TAG = "Contacts";
    private static HashMap<String, String> contactNames = new HashMap<String, String>();

    static String getContactName(final Context context, final String number) {
        String name = "someone";

        if (!number.isEmpty()) {
            // Check if contact is already stored
            if (contactNames.containsKey(number)) {
                name = contactNames.get(number);
                Log.v(TAG, "getContactName(): name \"" + name + "\" found in contact map.");
            }
            else {
                // define the columns I want the query to return
                final String[] projection = new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID };

                // encode the phone number and build the filter URI
                final Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

                // query for the URI
                final Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                        Log.v(TAG, "getContactName(): " + number + " resolved to contact \"" + name + "\".");
                    } else {
                        Log.w(TAG, "getContactName(): contact not found for number " + number + ".");
                    }

                    cursor.close();
                }

                contactNames.put(number, name);
            }
        }

        return name;
    }
}