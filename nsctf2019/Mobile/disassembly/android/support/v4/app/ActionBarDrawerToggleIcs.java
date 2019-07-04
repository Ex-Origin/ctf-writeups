// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;
import android.app.ActionBar;
import android.util.Log;
import android.os.Build$VERSION;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class ActionBarDrawerToggleIcs
{
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int[] THEME_ATTRS;
    
    static {
        THEME_ATTRS = new int[] { 16843531 };
    }
    
    public static Drawable getThemeUpIndicator(final Activity activity) {
        final TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(ActionBarDrawerToggleIcs.THEME_ATTRS);
        final Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }
    
    public static Object setActionBarDescription(final Object o, final Activity activity, final int n) {
        Object o2 = o;
        if (o == null) {
            o2 = new SetIndicatorInfo(activity);
        }
        final SetIndicatorInfo setIndicatorInfo = (SetIndicatorInfo)o2;
        if (setIndicatorInfo.setHomeAsUpIndicator == null) {
            return o2;
        }
        try {
            final ActionBar actionBar = activity.getActionBar();
            setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, n);
            if (Build$VERSION.SDK_INT <= 19) {
                actionBar.setSubtitle(actionBar.getSubtitle());
            }
            return o2;
        }
        catch (Exception ex) {
            Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", (Throwable)ex);
            return o2;
        }
    }
    
    public static Object setActionBarUpIndicator(final Object o, final Activity activity, final Drawable imageDrawable, final int n) {
        Object o2 = o;
        if (o == null) {
            o2 = new SetIndicatorInfo(activity);
        }
        final SetIndicatorInfo setIndicatorInfo = (SetIndicatorInfo)o2;
        if (setIndicatorInfo.setHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo.setHomeAsUpIndicator.invoke(actionBar, imageDrawable);
                setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, n);
                return o2;
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)ex);
                return o2;
            }
        }
        if (setIndicatorInfo.upIndicatorView != null) {
            setIndicatorInfo.upIndicatorView.setImageDrawable(imageDrawable);
            return o2;
        }
        Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
        return o2;
    }
    
    private static class SetIndicatorInfo
    {
        public Method setHomeActionContentDescription;
        public Method setHomeAsUpIndicator;
        public ImageView upIndicatorView;
        
        SetIndicatorInfo(Activity activity) {
            try {
                this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
            }
            catch (NoSuchMethodException ex) {
                activity = (Activity)activity.findViewById(16908332);
                if (activity == null) {
                    return;
                }
                final ViewGroup viewGroup = (ViewGroup)((View)activity).getParent();
                if (viewGroup.getChildCount() != 2) {
                    return;
                }
                activity = (Activity)viewGroup.getChildAt(0);
                final Object child = viewGroup.getChildAt(1);
                if (((View)activity).getId() == 16908332) {
                    activity = (Activity)child;
                }
                if (activity instanceof ImageView) {
                    this.upIndicatorView = (ImageView)activity;
                }
            }
        }
    }
}
