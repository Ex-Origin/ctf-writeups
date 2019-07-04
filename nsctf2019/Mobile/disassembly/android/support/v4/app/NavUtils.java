// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.content.pm.ActivityInfo;
import android.support.annotation.Nullable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.Context;
import android.content.ComponentName;
import android.os.Build$VERSION;
import android.content.Intent;
import android.app.Activity;

public final class NavUtils
{
    public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
    private static final String TAG = "NavUtils";
    
    public static Intent getParentActivityIntent(final Activity activity) {
        if (Build$VERSION.SDK_INT >= 16) {
            final Intent parentActivityIntent = activity.getParentActivityIntent();
            if (parentActivityIntent != null) {
                return parentActivityIntent;
            }
        }
        final String parentActivityName = getParentActivityName(activity);
        if (parentActivityName == null) {
            return null;
        }
        final ComponentName component = new ComponentName((Context)activity, parentActivityName);
        Intent intent;
        try {
            if (getParentActivityName((Context)activity, component) == null) {
                intent = Intent.makeMainActivity(component);
            }
            else {
                intent = new Intent().setComponent(component);
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            Log.e("NavUtils", "getParentActivityIntent: bad parentActivityName '" + parentActivityName + "' in manifest");
            return null;
        }
        return intent;
    }
    
    public static Intent getParentActivityIntent(final Context context, ComponentName component) throws PackageManager$NameNotFoundException {
        final String parentActivityName = getParentActivityName(context, component);
        if (parentActivityName == null) {
            return null;
        }
        component = new ComponentName(component.getPackageName(), parentActivityName);
        if (getParentActivityName(context, component) == null) {
            return Intent.makeMainActivity(component);
        }
        return new Intent().setComponent(component);
    }
    
    public static Intent getParentActivityIntent(final Context context, final Class<?> clazz) throws PackageManager$NameNotFoundException {
        final String parentActivityName = getParentActivityName(context, new ComponentName(context, (Class)clazz));
        if (parentActivityName == null) {
            return null;
        }
        final ComponentName component = new ComponentName(context, parentActivityName);
        if (getParentActivityName(context, component) == null) {
            return Intent.makeMainActivity(component);
        }
        return new Intent().setComponent(component);
    }
    
    @Nullable
    public static String getParentActivityName(final Activity activity) {
        try {
            return getParentActivityName((Context)activity, activity.getComponentName());
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new IllegalArgumentException((Throwable)ex);
        }
    }
    
    @Nullable
    public static String getParentActivityName(final Context context, final ComponentName componentName) throws PackageManager$NameNotFoundException {
        final ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(componentName, 128);
        if (Build$VERSION.SDK_INT >= 16) {
            final String parentActivityName = activityInfo.parentActivityName;
            if (parentActivityName != null) {
                return parentActivityName;
            }
        }
        if (activityInfo.metaData == null) {
            return null;
        }
        final String string = activityInfo.metaData.getString("android.support.PARENT_ACTIVITY");
        if (string == null) {
            return null;
        }
        String string2 = string;
        if (string.charAt(0) == '.') {
            string2 = context.getPackageName() + string;
        }
        return string2;
    }
    
    public static void navigateUpFromSameTask(final Activity activity) {
        final Intent parentActivityIntent = getParentActivityIntent(activity);
        if (parentActivityIntent == null) {
            throw new IllegalArgumentException("Activity " + activity.getClass().getSimpleName() + " does not have a parent activity name specified." + " (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> " + " element in your manifest?)");
        }
        navigateUpTo(activity, parentActivityIntent);
    }
    
    public static void navigateUpTo(final Activity activity, final Intent intent) {
        if (Build$VERSION.SDK_INT >= 16) {
            activity.navigateUpTo(intent);
            return;
        }
        intent.addFlags(67108864);
        activity.startActivity(intent);
        activity.finish();
    }
    
    public static boolean shouldUpRecreateTask(final Activity activity, final Intent intent) {
        if (Build$VERSION.SDK_INT >= 16) {
            return activity.shouldUpRecreateTask(intent);
        }
        final String action = activity.getIntent().getAction();
        return action != null && !action.equals("android.intent.action.MAIN");
    }
}
