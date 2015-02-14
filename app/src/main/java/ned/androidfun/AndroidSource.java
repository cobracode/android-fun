package ned.androidfun;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Gets Android-private methods
class AndroidSource {
    private static final String TAG = "AndroidSource";

    public static boolean executeMethod(final String className, final String methodName, final Object object, final Class<?>... params) {
        Log.v(TAG, "excuteMethod() begin");
        boolean result = false;

        if (doesMethodExist(className, methodName, params)) {
            try {
                // Get Method
                final Method m = Class.forName(className).getDeclaredMethod(methodName, params);

                // Invoke class method with parameters
                m.invoke(object, params);
            }
            catch (final Exception e) {
                Log.e(TAG, "executeMethod(): error executing method: " + e);
            }
        }

        Log.v(TAG, "executeMethod() end; result = " + result);
        return result;
    }

    public static boolean doesMethodExist(final String className, final String methodName, final Class<?>... params) {
        Log.v(TAG, "doesMethodExist(): begin");
        boolean result = true;

        try {
            Class.forName(className).getDeclaredMethod(methodName, params);
        }
        catch (final Exception e) {
            Log.e(TAG, "doesMethodExist(): method " + className + "." + methodName + " doesn't exist.");
            result = false;
        }

        Log.v(TAG, "doesMethodExist(): end");
        return result;
    }

    public static boolean doesFieldExist(final String className, final String fieldName) {
        Log.v(TAG, "doesFieldExist(): begin");
        boolean result = true;

        try {
            Class.forName(className).getDeclaredField(fieldName);
        }
        catch (final Exception e) {
            Log.e(TAG, "doesFieldExist(): field " + className + "." + fieldName + " doesn't exist.");
            result = false;
        }

        Log.v(TAG, "doesFieldExist(): end");
        return result;
    }

    public static void printMethods(final String className) {
        Log.v(TAG, "printMethods() begin");
        try {
            for (final Method m : Class.forName(className).getDeclaredMethods()) {
                Log.v(TAG, m.toString());
            }
        }
        catch (final Exception e) {
            Log.e(TAG, "printMethods(): " + e);
        }
        Log.v(TAG, "printMethods() end");
    }

    public static void printFields(final String className) {
        Log.v(TAG, "printFields() begin");
        try {
            for (final Field m : Class.forName(className).getDeclaredFields()) {
                Log.v(TAG, "printFields(): " + m.toString());
            }
        }
        catch (final Exception e) {
            Log.e(TAG, "printFields(): " + e);
        }
        Log.v(TAG, "printFields() end");
    }
}