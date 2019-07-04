// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.support.annotation.NonNull;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.support.v7.view.ActionMode;
import android.widget.SpinnerAdapter;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.content.res.Configuration;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.annotation.RestrictTo;

public abstract class ActionBar
{
    public static final int DISPLAY_HOME_AS_UP = 4;
    public static final int DISPLAY_SHOW_CUSTOM = 16;
    public static final int DISPLAY_SHOW_HOME = 2;
    public static final int DISPLAY_SHOW_TITLE = 8;
    public static final int DISPLAY_USE_LOGO = 1;
    @Deprecated
    public static final int NAVIGATION_MODE_LIST = 1;
    @Deprecated
    public static final int NAVIGATION_MODE_STANDARD = 0;
    @Deprecated
    public static final int NAVIGATION_MODE_TABS = 2;
    
    public abstract void addOnMenuVisibilityListener(final OnMenuVisibilityListener p0);
    
    @Deprecated
    public abstract void addTab(final Tab p0);
    
    @Deprecated
    public abstract void addTab(final Tab p0, final int p1);
    
    @Deprecated
    public abstract void addTab(final Tab p0, final int p1, final boolean p2);
    
    @Deprecated
    public abstract void addTab(final Tab p0, final boolean p1);
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean closeOptionsMenu() {
        return false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean collapseActionView() {
        return false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void dispatchMenuVisibilityChanged(final boolean b) {
    }
    
    public abstract View getCustomView();
    
    public abstract int getDisplayOptions();
    
    public float getElevation() {
        return 0.0f;
    }
    
    public abstract int getHeight();
    
    public int getHideOffset() {
        return 0;
    }
    
    @Deprecated
    public abstract int getNavigationItemCount();
    
    @Deprecated
    public abstract int getNavigationMode();
    
    @Deprecated
    public abstract int getSelectedNavigationIndex();
    
    @Deprecated
    @Nullable
    public abstract Tab getSelectedTab();
    
    @Nullable
    public abstract CharSequence getSubtitle();
    
    @Deprecated
    public abstract Tab getTabAt(final int p0);
    
    @Deprecated
    public abstract int getTabCount();
    
    public Context getThemedContext() {
        return null;
    }
    
    @Nullable
    public abstract CharSequence getTitle();
    
    public abstract void hide();
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean invalidateOptionsMenu() {
        return false;
    }
    
    public boolean isHideOnContentScrollEnabled() {
        return false;
    }
    
    public abstract boolean isShowing();
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean isTitleTruncated() {
        return false;
    }
    
    @Deprecated
    public abstract Tab newTab();
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void onConfigurationChanged(final Configuration configuration) {
    }
    
    void onDestroy() {
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean onKeyShortcut(final int n, final KeyEvent keyEvent) {
        return false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean onMenuKeyEvent(final KeyEvent keyEvent) {
        return false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean openOptionsMenu() {
        return false;
    }
    
    @Deprecated
    public abstract void removeAllTabs();
    
    public abstract void removeOnMenuVisibilityListener(final OnMenuVisibilityListener p0);
    
    @Deprecated
    public abstract void removeTab(final Tab p0);
    
    @Deprecated
    public abstract void removeTabAt(final int p0);
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean requestFocus() {
        return false;
    }
    
    @Deprecated
    public abstract void selectTab(final Tab p0);
    
    public abstract void setBackgroundDrawable(@Nullable final Drawable p0);
    
    public abstract void setCustomView(final int p0);
    
    public abstract void setCustomView(final View p0);
    
    public abstract void setCustomView(final View p0, final LayoutParams p1);
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setDefaultDisplayHomeAsUpEnabled(final boolean b) {
    }
    
    public abstract void setDisplayHomeAsUpEnabled(final boolean p0);
    
    public abstract void setDisplayOptions(final int p0);
    
    public abstract void setDisplayOptions(final int p0, final int p1);
    
    public abstract void setDisplayShowCustomEnabled(final boolean p0);
    
    public abstract void setDisplayShowHomeEnabled(final boolean p0);
    
    public abstract void setDisplayShowTitleEnabled(final boolean p0);
    
    public abstract void setDisplayUseLogoEnabled(final boolean p0);
    
    public void setElevation(final float n) {
        if (n != 0.0f) {
            throw new UnsupportedOperationException("Setting a non-zero elevation is not supported in this action bar configuration.");
        }
    }
    
    public void setHideOffset(final int n) {
        if (n != 0) {
            throw new UnsupportedOperationException("Setting an explicit action bar hide offset is not supported in this action bar configuration.");
        }
    }
    
    public void setHideOnContentScrollEnabled(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException("Hide on content scroll is not supported in this action bar configuration.");
        }
    }
    
    public void setHomeActionContentDescription(@StringRes final int n) {
    }
    
    public void setHomeActionContentDescription(@Nullable final CharSequence charSequence) {
    }
    
    public void setHomeAsUpIndicator(@DrawableRes final int n) {
    }
    
    public void setHomeAsUpIndicator(@Nullable final Drawable drawable) {
    }
    
    public void setHomeButtonEnabled(final boolean b) {
    }
    
    public abstract void setIcon(@DrawableRes final int p0);
    
    public abstract void setIcon(final Drawable p0);
    
    @Deprecated
    public abstract void setListNavigationCallbacks(final SpinnerAdapter p0, final OnNavigationListener p1);
    
    public abstract void setLogo(@DrawableRes final int p0);
    
    public abstract void setLogo(final Drawable p0);
    
    @Deprecated
    public abstract void setNavigationMode(final int p0);
    
    @Deprecated
    public abstract void setSelectedNavigationItem(final int p0);
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setShowHideAnimationEnabled(final boolean b) {
    }
    
    public void setSplitBackgroundDrawable(final Drawable drawable) {
    }
    
    public void setStackedBackgroundDrawable(final Drawable drawable) {
    }
    
    public abstract void setSubtitle(final int p0);
    
    public abstract void setSubtitle(final CharSequence p0);
    
    public abstract void setTitle(@StringRes final int p0);
    
    public abstract void setTitle(final CharSequence p0);
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setWindowTitle(final CharSequence charSequence) {
    }
    
    public abstract void show();
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ActionMode startActionMode(final ActionMode.Callback callback) {
        return null;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface DisplayOptions {
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public int gravity;
        
        public LayoutParams(final int n) {
            this(-2, -1, n);
        }
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.gravity = 0;
            this.gravity = 8388627;
        }
        
        public LayoutParams(final int n, final int n2, final int gravity) {
            super(n, n2);
            this.gravity = 0;
            this.gravity = gravity;
        }
        
        public LayoutParams(@NonNull final Context context, final AttributeSet set) {
            super(context, set);
            this.gravity = 0;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActionBarLayout);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.ActionBarLayout_android_layout_gravity, 0);
            obtainStyledAttributes.recycle();
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$MarginLayoutParams)layoutParams);
            this.gravity = 0;
            this.gravity = layoutParams.gravity;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.gravity = 0;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface NavigationMode {
    }
    
    public interface OnMenuVisibilityListener
    {
        void onMenuVisibilityChanged(final boolean p0);
    }
    
    @Deprecated
    public interface OnNavigationListener
    {
        boolean onNavigationItemSelected(final int p0, final long p1);
    }
    
    @Deprecated
    public abstract static class Tab
    {
        public static final int INVALID_POSITION = -1;
        
        public abstract CharSequence getContentDescription();
        
        public abstract View getCustomView();
        
        public abstract Drawable getIcon();
        
        public abstract int getPosition();
        
        public abstract Object getTag();
        
        public abstract CharSequence getText();
        
        public abstract void select();
        
        public abstract Tab setContentDescription(@StringRes final int p0);
        
        public abstract Tab setContentDescription(final CharSequence p0);
        
        public abstract Tab setCustomView(final int p0);
        
        public abstract Tab setCustomView(final View p0);
        
        public abstract Tab setIcon(@DrawableRes final int p0);
        
        public abstract Tab setIcon(final Drawable p0);
        
        public abstract Tab setTabListener(final TabListener p0);
        
        public abstract Tab setTag(final Object p0);
        
        public abstract Tab setText(final int p0);
        
        public abstract Tab setText(final CharSequence p0);
    }
    
    @Deprecated
    public interface TabListener
    {
        void onTabReselected(final Tab p0, final FragmentTransaction p1);
        
        void onTabSelected(final Tab p0, final FragmentTransaction p1);
        
        void onTabUnselected(final Tab p0, final FragmentTransaction p1);
    }
}
