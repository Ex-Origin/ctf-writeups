// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.app.Activity;
import android.content.Context;

public class AppLaunchChecker
{
    private static final String KEY_STARTED_FROM_LAUNCHER = "startedFromLauncher";
    private static final String SHARED_PREFS_NAME = "android.support.AppLaunchChecker";
    
    public static boolean hasStartedFromLauncher(final Context context) {
        return context.getSharedPreferences("android.support.AppLaunchChecker", 0).getBoolean("startedFromLauncher", false);
    }
    
    public static void onActivityCreate(final Activity activity) {
        final SharedPreferences sharedPreferences = activity.getSharedPreferences("android.support.AppLaunchChecker", 0);
        if (!sharedPreferences.getBoolean("startedFromLauncher", false)) {
            final Intent intent = activity.getIntent();
            if (intent != null && "android.intent.action.MAIN".equals(intent.getAction()) && (intent.hasCategory("android.intent.category.LAUNCHER") || intent.hasCategory("android.intent.category.LEANBACK_LAUNCHER"))) {
                SharedPreferencesCompat.EditorCompat.getInstance().apply(sharedPreferences.edit().putBoolean("startedFromLauncher", true));
            }
        }
    }
}
