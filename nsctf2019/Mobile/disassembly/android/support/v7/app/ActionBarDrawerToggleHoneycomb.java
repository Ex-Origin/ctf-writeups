// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

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

@RequiresApi(11)
class ActionBarDrawerToggleHoneycomb
{
    private static final String TAG = "ActionBarDrawerToggleHoneycomb";
    private static final int[] THEME_ATTRS;
    
    static {
        THEME_ATTRS = new int[] { 16843531 };
    }
    
    public static Drawable getThemeUpIndicator(final Activity activity) {
        final TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(ActionBarDrawerToggleHoneycomb.THEME_ATTRS);
        final Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }
    
    public static SetIndicatorInfo setActionBarDescription(final SetIndicatorInfo setIndicatorInfo, final Activity activity, final int n) {
        SetIndicatorInfo setIndicatorInfo2 = setIndicatorInfo;
        if (setIndicatorInfo == null) {
            setIndicatorInfo2 = new SetIndicatorInfo(activity);
        }
        if (setIndicatorInfo2.setHomeAsUpIndicator == null) {
            return setIndicatorInfo2;
        }
        try {
            final ActionBar actionBar = activity.getActionBar();
            setIndicatorInfo2.setHomeActionContentDescription.invoke(actionBar, n);
            if (Build$VERSION.SDK_INT <= 19) {
                actionBar.setSubtitle(actionBar.getSubtitle());
            }
            return setIndicatorInfo2;
        }
        catch (Exception ex) {
            Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", (Throwable)ex);
            return setIndicatorInfo2;
        }
    }
    
    public static SetIndicatorInfo setActionBarUpIndicator(SetIndicatorInfo setIndicatorInfo, final Activity activity, final Drawable imageDrawable, final int n) {
        setIndicatorInfo = new SetIndicatorInfo(activity);
        if (setIndicatorInfo.setHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar = activity.getActionBar();
                setIndicatorInfo.setHomeAsUpIndicator.invoke(actionBar, imageDrawable);
                setIndicatorInfo.setHomeActionContentDescription.invoke(actionBar, n);
                return setIndicatorInfo;
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)ex);
                return setIndicatorInfo;
            }
        }
        if (setIndicatorInfo.upIndicatorView != null) {
            setIndicatorInfo.upIndicatorView.setImageDrawable(imageDrawable);
            return setIndicatorInfo;
        }
        Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
        return setIndicatorInfo;
    }
    
    static class SetIndicatorInfo
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
