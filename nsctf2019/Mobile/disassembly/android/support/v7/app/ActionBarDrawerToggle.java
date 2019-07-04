// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.app.ActionBar;
import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.support.annotation.StringRes;
import android.app.Activity;
import android.view.View$OnClickListener;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.DrawerLayout;

public class ActionBarDrawerToggle implements DrawerListener
{
    private final Delegate mActivityImpl;
    private final int mCloseDrawerContentDescRes;
    boolean mDrawerIndicatorEnabled;
    private final DrawerLayout mDrawerLayout;
    private boolean mDrawerSlideAnimationEnabled;
    private boolean mHasCustomUpIndicator;
    private Drawable mHomeAsUpIndicator;
    private final int mOpenDrawerContentDescRes;
    private DrawerArrowDrawable mSlider;
    View$OnClickListener mToolbarNavigationClickListener;
    private boolean mWarnedForDisplayHomeAsUp;
    
    public ActionBarDrawerToggle(final Activity activity, final DrawerLayout drawerLayout, @StringRes final int n, @StringRes final int n2) {
        this(activity, null, drawerLayout, null, n, n2);
    }
    
    public ActionBarDrawerToggle(final Activity activity, final DrawerLayout drawerLayout, final Toolbar toolbar, @StringRes final int n, @StringRes final int n2) {
        this(activity, toolbar, drawerLayout, null, n, n2);
    }
    
    ActionBarDrawerToggle(final Activity activity, final Toolbar toolbar, final DrawerLayout mDrawerLayout, final DrawerArrowDrawable mSlider, @StringRes final int mOpenDrawerContentDescRes, @StringRes final int mCloseDrawerContentDescRes) {
        this.mDrawerSlideAnimationEnabled = true;
        this.mDrawerIndicatorEnabled = true;
        this.mWarnedForDisplayHomeAsUp = false;
        if (toolbar != null) {
            this.mActivityImpl = (Delegate)new ToolbarCompatDelegate(toolbar);
            toolbar.setNavigationOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (ActionBarDrawerToggle.this.mDrawerIndicatorEnabled) {
                        ActionBarDrawerToggle.this.toggle();
                    }
                    else if (ActionBarDrawerToggle.this.mToolbarNavigationClickListener != null) {
                        ActionBarDrawerToggle.this.mToolbarNavigationClickListener.onClick(view);
                    }
                }
            });
        }
        else if (activity instanceof DelegateProvider) {
            this.mActivityImpl = ((DelegateProvider)activity).getDrawerToggleDelegate();
        }
        else if (Build$VERSION.SDK_INT >= 18) {
            this.mActivityImpl = (Delegate)new JellybeanMr2Delegate(activity);
        }
        else if (Build$VERSION.SDK_INT >= 14) {
            this.mActivityImpl = (Delegate)new IcsDelegate(activity);
        }
        else if (Build$VERSION.SDK_INT >= 11) {
            this.mActivityImpl = (Delegate)new HoneycombDelegate(activity);
        }
        else {
            this.mActivityImpl = (Delegate)new DummyDelegate(activity);
        }
        this.mDrawerLayout = mDrawerLayout;
        this.mOpenDrawerContentDescRes = mOpenDrawerContentDescRes;
        this.mCloseDrawerContentDescRes = mCloseDrawerContentDescRes;
        if (mSlider == null) {
            this.mSlider = new DrawerArrowDrawable(this.mActivityImpl.getActionBarThemedContext());
        }
        else {
            this.mSlider = mSlider;
        }
        this.mHomeAsUpIndicator = this.getThemeUpIndicator();
    }
    
    private void setPosition(final float progress) {
        if (progress == 1.0f) {
            this.mSlider.setVerticalMirror(true);
        }
        else if (progress == 0.0f) {
            this.mSlider.setVerticalMirror(false);
        }
        this.mSlider.setProgress(progress);
    }
    
    @NonNull
    public DrawerArrowDrawable getDrawerArrowDrawable() {
        return this.mSlider;
    }
    
    Drawable getThemeUpIndicator() {
        return this.mActivityImpl.getThemeUpIndicator();
    }
    
    public View$OnClickListener getToolbarNavigationClickListener() {
        return this.mToolbarNavigationClickListener;
    }
    
    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }
    
    public boolean isDrawerSlideAnimationEnabled() {
        return this.mDrawerSlideAnimationEnabled;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (!this.mHasCustomUpIndicator) {
            this.mHomeAsUpIndicator = this.getThemeUpIndicator();
        }
        this.syncState();
    }
    
    @Override
    public void onDrawerClosed(final View view) {
        this.setPosition(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.setActionBarDescription(this.mOpenDrawerContentDescRes);
        }
    }
    
    @Override
    public void onDrawerOpened(final View view) {
        this.setPosition(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.setActionBarDescription(this.mCloseDrawerContentDescRes);
        }
    }
    
    @Override
    public void onDrawerSlide(final View view, final float n) {
        if (this.mDrawerSlideAnimationEnabled) {
            this.setPosition(Math.min(1.0f, Math.max(0.0f, n)));
            return;
        }
        this.setPosition(0.0f);
    }
    
    @Override
    public void onDrawerStateChanged(final int n) {
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem != null && menuItem.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
            this.toggle();
            return true;
        }
        return false;
    }
    
    void setActionBarDescription(final int actionBarDescription) {
        this.mActivityImpl.setActionBarDescription(actionBarDescription);
    }
    
    void setActionBarUpIndicator(final Drawable drawable, final int n) {
        if (!this.mWarnedForDisplayHomeAsUp && !this.mActivityImpl.isNavigationVisible()) {
            Log.w("ActionBarDrawerToggle", "DrawerToggle may not show up because NavigationIcon is not visible. You may need to call actionbar.setDisplayHomeAsUpEnabled(true);");
            this.mWarnedForDisplayHomeAsUp = true;
        }
        this.mActivityImpl.setActionBarUpIndicator(drawable, n);
    }
    
    public void setDrawerArrowDrawable(@NonNull final DrawerArrowDrawable mSlider) {
        this.mSlider = mSlider;
        this.syncState();
    }
    
    public void setDrawerIndicatorEnabled(final boolean mDrawerIndicatorEnabled) {
        if (mDrawerIndicatorEnabled != this.mDrawerIndicatorEnabled) {
            if (mDrawerIndicatorEnabled) {
                final DrawerArrowDrawable mSlider = this.mSlider;
                int n;
                if (this.mDrawerLayout.isDrawerOpen(8388611)) {
                    n = this.mCloseDrawerContentDescRes;
                }
                else {
                    n = this.mOpenDrawerContentDescRes;
                }
                this.setActionBarUpIndicator(mSlider, n);
            }
            else {
                this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
            }
            this.mDrawerIndicatorEnabled = mDrawerIndicatorEnabled;
        }
    }
    
    public void setDrawerSlideAnimationEnabled(final boolean mDrawerSlideAnimationEnabled) {
        if (!(this.mDrawerSlideAnimationEnabled = mDrawerSlideAnimationEnabled)) {
            this.setPosition(0.0f);
        }
    }
    
    public void setHomeAsUpIndicator(final int n) {
        Drawable drawable = null;
        if (n != 0) {
            drawable = this.mDrawerLayout.getResources().getDrawable(n);
        }
        this.setHomeAsUpIndicator(drawable);
    }
    
    public void setHomeAsUpIndicator(final Drawable mHomeAsUpIndicator) {
        if (mHomeAsUpIndicator == null) {
            this.mHomeAsUpIndicator = this.getThemeUpIndicator();
            this.mHasCustomUpIndicator = false;
        }
        else {
            this.mHomeAsUpIndicator = mHomeAsUpIndicator;
            this.mHasCustomUpIndicator = true;
        }
        if (!this.mDrawerIndicatorEnabled) {
            this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
        }
    }
    
    public void setToolbarNavigationClickListener(final View$OnClickListener mToolbarNavigationClickListener) {
        this.mToolbarNavigationClickListener = mToolbarNavigationClickListener;
    }
    
    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
            this.setPosition(1.0f);
        }
        else {
            this.setPosition(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            final DrawerArrowDrawable mSlider = this.mSlider;
            int n;
            if (this.mDrawerLayout.isDrawerOpen(8388611)) {
                n = this.mCloseDrawerContentDescRes;
            }
            else {
                n = this.mOpenDrawerContentDescRes;
            }
            this.setActionBarUpIndicator(mSlider, n);
        }
    }
    
    void toggle() {
        final int drawerLockMode = this.mDrawerLayout.getDrawerLockMode(8388611);
        if (this.mDrawerLayout.isDrawerVisible(8388611) && drawerLockMode != 2) {
            this.mDrawerLayout.closeDrawer(8388611);
        }
        else if (drawerLockMode != 1) {
            this.mDrawerLayout.openDrawer(8388611);
        }
    }
    
    public interface Delegate
    {
        Context getActionBarThemedContext();
        
        Drawable getThemeUpIndicator();
        
        boolean isNavigationVisible();
        
        void setActionBarDescription(@StringRes final int p0);
        
        void setActionBarUpIndicator(final Drawable p0, @StringRes final int p1);
    }
    
    public interface DelegateProvider
    {
        @Nullable
        Delegate getDrawerToggleDelegate();
    }
    
    static class DummyDelegate implements Delegate
    {
        final Activity mActivity;
        
        DummyDelegate(final Activity mActivity) {
            this.mActivity = mActivity;
        }
        
        @Override
        public Context getActionBarThemedContext() {
            return (Context)this.mActivity;
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            return null;
        }
        
        @Override
        public boolean isNavigationVisible() {
            return true;
        }
        
        @Override
        public void setActionBarDescription(@StringRes final int n) {
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable drawable, @StringRes final int n) {
        }
    }
    
    @RequiresApi(11)
    private static class HoneycombDelegate implements Delegate
    {
        final Activity mActivity;
        ActionBarDrawerToggleHoneycomb.SetIndicatorInfo mSetIndicatorInfo;
        
        HoneycombDelegate(final Activity mActivity) {
            this.mActivity = mActivity;
        }
        
        @Override
        public Context getActionBarThemedContext() {
            return (Context)this.mActivity;
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(this.mActivity);
        }
        
        @Override
        public boolean isNavigationVisible() {
            final ActionBar actionBar = this.mActivity.getActionBar();
            return actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0x0;
        }
        
        @Override
        public void setActionBarDescription(final int n) {
            this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, n);
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable drawable, final int n) {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowHomeEnabled(true);
                this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, drawable, n);
                actionBar.setDisplayShowHomeEnabled(false);
            }
        }
    }
    
    @RequiresApi(14)
    private static class IcsDelegate extends HoneycombDelegate
    {
        IcsDelegate(final Activity activity) {
            super(activity);
        }
        
        @Override
        public Context getActionBarThemedContext() {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                return actionBar.getThemedContext();
            }
            return (Context)this.mActivity;
        }
    }
    
    @RequiresApi(18)
    private static class JellybeanMr2Delegate implements Delegate
    {
        final Activity mActivity;
        
        JellybeanMr2Delegate(final Activity mActivity) {
            this.mActivity = mActivity;
        }
        
        @Override
        public Context getActionBarThemedContext() {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                return actionBar.getThemedContext();
            }
            return (Context)this.mActivity;
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            final TypedArray obtainStyledAttributes = this.getActionBarThemedContext().obtainStyledAttributes((AttributeSet)null, new int[] { 16843531 }, 16843470, 0);
            final Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }
        
        @Override
        public boolean isNavigationVisible() {
            final ActionBar actionBar = this.mActivity.getActionBar();
            return actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0x0;
        }
        
        @Override
        public void setActionBarDescription(final int homeActionContentDescription) {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable homeAsUpIndicator, final int homeActionContentDescription) {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(homeAsUpIndicator);
                actionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
    }
    
    static class ToolbarCompatDelegate implements Delegate
    {
        final CharSequence mDefaultContentDescription;
        final Drawable mDefaultUpIndicator;
        final Toolbar mToolbar;
        
        ToolbarCompatDelegate(final Toolbar mToolbar) {
            this.mToolbar = mToolbar;
            this.mDefaultUpIndicator = mToolbar.getNavigationIcon();
            this.mDefaultContentDescription = mToolbar.getNavigationContentDescription();
        }
        
        @Override
        public Context getActionBarThemedContext() {
            return this.mToolbar.getContext();
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            return this.mDefaultUpIndicator;
        }
        
        @Override
        public boolean isNavigationVisible() {
            return true;
        }
        
        @Override
        public void setActionBarDescription(@StringRes final int navigationContentDescription) {
            if (navigationContentDescription == 0) {
                this.mToolbar.setNavigationContentDescription(this.mDefaultContentDescription);
                return;
            }
            this.mToolbar.setNavigationContentDescription(navigationContentDescription);
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable navigationIcon, @StringRes final int actionBarDescription) {
            this.mToolbar.setNavigationIcon(navigationIcon);
            this.setActionBarDescription(actionBarDescription);
        }
    }
}
