// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.content.res.AppCompatResources;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.support.v4.view.ViewCompat;
import android.os.Parcelable;
import android.util.SparseArray;
import android.util.Log;
import android.view.Menu;
import android.content.Context;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.view.ViewGroup$LayoutParams;
import android.view.MenuItem;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.view.Window$Callback;
import android.widget.Spinner;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ToolbarWidgetWrapper implements DecorToolbar
{
    private static final int AFFECTS_LOGO_MASK = 3;
    private static final long DEFAULT_FADE_DURATION_MS = 200L;
    private static final String TAG = "ToolbarWidgetWrapper";
    private ActionMenuPresenter mActionMenuPresenter;
    private View mCustomView;
    private int mDefaultNavigationContentDescription;
    private Drawable mDefaultNavigationIcon;
    private int mDisplayOpts;
    private CharSequence mHomeDescription;
    private Drawable mIcon;
    private Drawable mLogo;
    boolean mMenuPrepared;
    private Drawable mNavIcon;
    private int mNavigationMode;
    private Spinner mSpinner;
    private CharSequence mSubtitle;
    private View mTabView;
    CharSequence mTitle;
    private boolean mTitleSet;
    Toolbar mToolbar;
    Window$Callback mWindowCallback;
    
    public ToolbarWidgetWrapper(final Toolbar toolbar, final boolean b) {
        this(toolbar, b, R.string.abc_action_bar_up_description, R.drawable.abc_ic_ab_back_material);
    }
    
    public ToolbarWidgetWrapper(final Toolbar mToolbar, final boolean b, final int defaultNavigationContentDescription, int n) {
        this.mNavigationMode = 0;
        this.mDefaultNavigationContentDescription = 0;
        this.mToolbar = mToolbar;
        this.mTitle = mToolbar.getTitle();
        this.mSubtitle = mToolbar.getSubtitle();
        this.mTitleSet = (this.mTitle != null);
        this.mNavIcon = mToolbar.getNavigationIcon();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(mToolbar.getContext(), null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        this.mDefaultNavigationIcon = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_homeAsUpIndicator);
        if (b) {
            final CharSequence text = obtainStyledAttributes.getText(R.styleable.ActionBar_title);
            if (!TextUtils.isEmpty(text)) {
                this.setTitle(text);
            }
            final CharSequence text2 = obtainStyledAttributes.getText(R.styleable.ActionBar_subtitle);
            if (!TextUtils.isEmpty(text2)) {
                this.setSubtitle(text2);
            }
            final Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_logo);
            if (drawable != null) {
                this.setLogo(drawable);
            }
            final Drawable drawable2 = obtainStyledAttributes.getDrawable(R.styleable.ActionBar_icon);
            if (drawable2 != null) {
                this.setIcon(drawable2);
            }
            if (this.mNavIcon == null && this.mDefaultNavigationIcon != null) {
                this.setNavigationIcon(this.mDefaultNavigationIcon);
            }
            this.setDisplayOptions(obtainStyledAttributes.getInt(R.styleable.ActionBar_displayOptions, 0));
            n = obtainStyledAttributes.getResourceId(R.styleable.ActionBar_customNavigationLayout, 0);
            if (n != 0) {
                this.setCustomView(LayoutInflater.from(this.mToolbar.getContext()).inflate(n, (ViewGroup)this.mToolbar, false));
                this.setDisplayOptions(this.mDisplayOpts | 0x10);
            }
            n = obtainStyledAttributes.getLayoutDimension(R.styleable.ActionBar_height, 0);
            if (n > 0) {
                final ViewGroup$LayoutParams layoutParams = this.mToolbar.getLayoutParams();
                layoutParams.height = n;
                this.mToolbar.setLayoutParams(layoutParams);
            }
            n = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.ActionBar_contentInsetStart, -1);
            final int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.ActionBar_contentInsetEnd, -1);
            if (n >= 0 || dimensionPixelOffset >= 0) {
                this.mToolbar.setContentInsetsRelative(Math.max(n, 0), Math.max(dimensionPixelOffset, 0));
            }
            n = obtainStyledAttributes.getResourceId(R.styleable.ActionBar_titleTextStyle, 0);
            if (n != 0) {
                this.mToolbar.setTitleTextAppearance(this.mToolbar.getContext(), n);
            }
            n = obtainStyledAttributes.getResourceId(R.styleable.ActionBar_subtitleTextStyle, 0);
            if (n != 0) {
                this.mToolbar.setSubtitleTextAppearance(this.mToolbar.getContext(), n);
            }
            n = obtainStyledAttributes.getResourceId(R.styleable.ActionBar_popupTheme, 0);
            if (n != 0) {
                this.mToolbar.setPopupTheme(n);
            }
        }
        else {
            this.mDisplayOpts = this.detectDisplayOptions();
        }
        obtainStyledAttributes.recycle();
        this.setDefaultNavigationContentDescription(defaultNavigationContentDescription);
        this.mHomeDescription = this.mToolbar.getNavigationContentDescription();
        this.mToolbar.setNavigationOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final ActionMenuItem mNavItem = new ActionMenuItem(ToolbarWidgetWrapper.this.mToolbar.getContext(), 0, 16908332, 0, 0, ToolbarWidgetWrapper.this.mTitle);
            
            public void onClick(final View view) {
                if (ToolbarWidgetWrapper.this.mWindowCallback != null && ToolbarWidgetWrapper.this.mMenuPrepared) {
                    ToolbarWidgetWrapper.this.mWindowCallback.onMenuItemSelected(0, (MenuItem)this.mNavItem);
                }
            }
        });
    }
    
    private int detectDisplayOptions() {
        int n = 11;
        if (this.mToolbar.getNavigationIcon() != null) {
            n = (0xB | 0x4);
            this.mDefaultNavigationIcon = this.mToolbar.getNavigationIcon();
        }
        return n;
    }
    
    private void ensureSpinner() {
        if (this.mSpinner == null) {
            (this.mSpinner = new AppCompatSpinner(this.getContext(), null, R.attr.actionDropDownStyle)).setLayoutParams((ViewGroup$LayoutParams)new Toolbar.LayoutParams(-2, -2, 8388627));
        }
    }
    
    private void setTitleInt(final CharSequence charSequence) {
        this.mTitle = charSequence;
        if ((this.mDisplayOpts & 0x8) != 0x0) {
            this.mToolbar.setTitle(charSequence);
        }
    }
    
    private void updateHomeAccessibility() {
        if ((this.mDisplayOpts & 0x4) != 0x0) {
            if (!TextUtils.isEmpty(this.mHomeDescription)) {
                this.mToolbar.setNavigationContentDescription(this.mHomeDescription);
                return;
            }
            this.mToolbar.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
        }
    }
    
    private void updateNavigationIcon() {
        if ((this.mDisplayOpts & 0x4) != 0x0) {
            final Toolbar mToolbar = this.mToolbar;
            Drawable navigationIcon;
            if (this.mNavIcon != null) {
                navigationIcon = this.mNavIcon;
            }
            else {
                navigationIcon = this.mDefaultNavigationIcon;
            }
            mToolbar.setNavigationIcon(navigationIcon);
            return;
        }
        this.mToolbar.setNavigationIcon(null);
    }
    
    private void updateToolbarLogo() {
        Drawable logo = null;
        if ((this.mDisplayOpts & 0x2) != 0x0) {
            if ((this.mDisplayOpts & 0x1) != 0x0) {
                if (this.mLogo != null) {
                    logo = this.mLogo;
                }
                else {
                    logo = this.mIcon;
                }
            }
            else {
                logo = this.mIcon;
            }
        }
        this.mToolbar.setLogo(logo);
    }
    
    @Override
    public void animateToVisibility(final int n) {
        final ViewPropertyAnimatorCompat setupAnimatorToVisibility = this.setupAnimatorToVisibility(n, 200L);
        if (setupAnimatorToVisibility != null) {
            setupAnimatorToVisibility.start();
        }
    }
    
    @Override
    public boolean canShowOverflowMenu() {
        return this.mToolbar.canShowOverflowMenu();
    }
    
    @Override
    public void collapseActionView() {
        this.mToolbar.collapseActionView();
    }
    
    @Override
    public void dismissPopupMenus() {
        this.mToolbar.dismissPopupMenus();
    }
    
    @Override
    public Context getContext() {
        return this.mToolbar.getContext();
    }
    
    @Override
    public View getCustomView() {
        return this.mCustomView;
    }
    
    @Override
    public int getDisplayOptions() {
        return this.mDisplayOpts;
    }
    
    @Override
    public int getDropdownItemCount() {
        if (this.mSpinner != null) {
            return this.mSpinner.getCount();
        }
        return 0;
    }
    
    @Override
    public int getDropdownSelectedPosition() {
        if (this.mSpinner != null) {
            return this.mSpinner.getSelectedItemPosition();
        }
        return 0;
    }
    
    @Override
    public int getHeight() {
        return this.mToolbar.getHeight();
    }
    
    @Override
    public Menu getMenu() {
        return this.mToolbar.getMenu();
    }
    
    @Override
    public int getNavigationMode() {
        return this.mNavigationMode;
    }
    
    @Override
    public CharSequence getSubtitle() {
        return this.mToolbar.getSubtitle();
    }
    
    @Override
    public CharSequence getTitle() {
        return this.mToolbar.getTitle();
    }
    
    @Override
    public ViewGroup getViewGroup() {
        return this.mToolbar;
    }
    
    @Override
    public int getVisibility() {
        return this.mToolbar.getVisibility();
    }
    
    @Override
    public boolean hasEmbeddedTabs() {
        return this.mTabView != null;
    }
    
    @Override
    public boolean hasExpandedActionView() {
        return this.mToolbar.hasExpandedActionView();
    }
    
    @Override
    public boolean hasIcon() {
        return this.mIcon != null;
    }
    
    @Override
    public boolean hasLogo() {
        return this.mLogo != null;
    }
    
    @Override
    public boolean hideOverflowMenu() {
        return this.mToolbar.hideOverflowMenu();
    }
    
    @Override
    public void initIndeterminateProgress() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }
    
    @Override
    public void initProgress() {
        Log.i("ToolbarWidgetWrapper", "Progress display unsupported");
    }
    
    @Override
    public boolean isOverflowMenuShowPending() {
        return this.mToolbar.isOverflowMenuShowPending();
    }
    
    @Override
    public boolean isOverflowMenuShowing() {
        return this.mToolbar.isOverflowMenuShowing();
    }
    
    @Override
    public boolean isTitleTruncated() {
        return this.mToolbar.isTitleTruncated();
    }
    
    @Override
    public void restoreHierarchyState(final SparseArray<Parcelable> sparseArray) {
        this.mToolbar.restoreHierarchyState((SparseArray)sparseArray);
    }
    
    @Override
    public void saveHierarchyState(final SparseArray<Parcelable> sparseArray) {
        this.mToolbar.saveHierarchyState((SparseArray)sparseArray);
    }
    
    @Override
    public void setBackgroundDrawable(final Drawable drawable) {
        ViewCompat.setBackground((View)this.mToolbar, drawable);
    }
    
    @Override
    public void setCollapsible(final boolean collapsible) {
        this.mToolbar.setCollapsible(collapsible);
    }
    
    @Override
    public void setCustomView(final View mCustomView) {
        if (this.mCustomView != null && (this.mDisplayOpts & 0x10) != 0x0) {
            this.mToolbar.removeView(this.mCustomView);
        }
        if ((this.mCustomView = mCustomView) != null && (this.mDisplayOpts & 0x10) != 0x0) {
            this.mToolbar.addView(this.mCustomView);
        }
    }
    
    @Override
    public void setDefaultNavigationContentDescription(final int mDefaultNavigationContentDescription) {
        if (mDefaultNavigationContentDescription != this.mDefaultNavigationContentDescription) {
            this.mDefaultNavigationContentDescription = mDefaultNavigationContentDescription;
            if (TextUtils.isEmpty(this.mToolbar.getNavigationContentDescription())) {
                this.setNavigationContentDescription(this.mDefaultNavigationContentDescription);
            }
        }
    }
    
    @Override
    public void setDefaultNavigationIcon(final Drawable mDefaultNavigationIcon) {
        if (this.mDefaultNavigationIcon != mDefaultNavigationIcon) {
            this.mDefaultNavigationIcon = mDefaultNavigationIcon;
            this.updateNavigationIcon();
        }
    }
    
    @Override
    public void setDisplayOptions(final int mDisplayOpts) {
        final int n = this.mDisplayOpts ^ mDisplayOpts;
        this.mDisplayOpts = mDisplayOpts;
        if (n != 0) {
            if ((n & 0x4) != 0x0) {
                if ((mDisplayOpts & 0x4) != 0x0) {
                    this.updateHomeAccessibility();
                }
                this.updateNavigationIcon();
            }
            if ((n & 0x3) != 0x0) {
                this.updateToolbarLogo();
            }
            if ((n & 0x8) != 0x0) {
                if ((mDisplayOpts & 0x8) != 0x0) {
                    this.mToolbar.setTitle(this.mTitle);
                    this.mToolbar.setSubtitle(this.mSubtitle);
                }
                else {
                    this.mToolbar.setTitle(null);
                    this.mToolbar.setSubtitle(null);
                }
            }
            if ((n & 0x10) != 0x0 && this.mCustomView != null) {
                if ((mDisplayOpts & 0x10) == 0x0) {
                    this.mToolbar.removeView(this.mCustomView);
                    return;
                }
                this.mToolbar.addView(this.mCustomView);
            }
        }
    }
    
    @Override
    public void setDropdownParams(final SpinnerAdapter adapter, final AdapterView$OnItemSelectedListener onItemSelectedListener) {
        this.ensureSpinner();
        this.mSpinner.setAdapter(adapter);
        this.mSpinner.setOnItemSelectedListener(onItemSelectedListener);
    }
    
    @Override
    public void setDropdownSelectedPosition(final int selection) {
        if (this.mSpinner == null) {
            throw new IllegalStateException("Can't set dropdown selected position without an adapter");
        }
        this.mSpinner.setSelection(selection);
    }
    
    @Override
    public void setEmbeddedTabView(final ScrollingTabContainerView mTabView) {
        if (this.mTabView != null && this.mTabView.getParent() == this.mToolbar) {
            this.mToolbar.removeView(this.mTabView);
        }
        if ((this.mTabView = (View)mTabView) != null && this.mNavigationMode == 2) {
            this.mToolbar.addView(this.mTabView, 0);
            final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams)this.mTabView.getLayoutParams();
            layoutParams.width = -2;
            layoutParams.height = -2;
            layoutParams.gravity = 8388691;
            mTabView.setAllowCollapse(true);
        }
    }
    
    @Override
    public void setHomeButtonEnabled(final boolean b) {
    }
    
    @Override
    public void setIcon(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setIcon(drawable);
    }
    
    @Override
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
        this.updateToolbarLogo();
    }
    
    @Override
    public void setLogo(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setLogo(drawable);
    }
    
    @Override
    public void setLogo(final Drawable mLogo) {
        this.mLogo = mLogo;
        this.updateToolbarLogo();
    }
    
    @Override
    public void setMenu(final Menu menu, final MenuPresenter.Callback callback) {
        if (this.mActionMenuPresenter == null) {
            (this.mActionMenuPresenter = new ActionMenuPresenter(this.mToolbar.getContext())).setId(R.id.action_menu_presenter);
        }
        this.mActionMenuPresenter.setCallback(callback);
        this.mToolbar.setMenu((MenuBuilder)menu, this.mActionMenuPresenter);
    }
    
    @Override
    public void setMenuCallbacks(final MenuPresenter.Callback callback, final MenuBuilder.Callback callback2) {
        this.mToolbar.setMenuCallbacks(callback, callback2);
    }
    
    @Override
    public void setMenuPrepared() {
        this.mMenuPrepared = true;
    }
    
    @Override
    public void setNavigationContentDescription(final int n) {
        CharSequence string;
        if (n == 0) {
            string = null;
        }
        else {
            string = this.getContext().getString(n);
        }
        this.setNavigationContentDescription(string);
    }
    
    @Override
    public void setNavigationContentDescription(final CharSequence mHomeDescription) {
        this.mHomeDescription = mHomeDescription;
        this.updateHomeAccessibility();
    }
    
    @Override
    public void setNavigationIcon(final int n) {
        Drawable drawable;
        if (n != 0) {
            drawable = AppCompatResources.getDrawable(this.getContext(), n);
        }
        else {
            drawable = null;
        }
        this.setNavigationIcon(drawable);
    }
    
    @Override
    public void setNavigationIcon(final Drawable mNavIcon) {
        this.mNavIcon = mNavIcon;
        this.updateNavigationIcon();
    }
    
    @Override
    public void setNavigationMode(final int mNavigationMode) {
        final int mNavigationMode2 = this.mNavigationMode;
        if (mNavigationMode != mNavigationMode2) {
            switch (mNavigationMode2) {
                case 1: {
                    if (this.mSpinner != null && this.mSpinner.getParent() == this.mToolbar) {
                        this.mToolbar.removeView((View)this.mSpinner);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (this.mTabView != null && this.mTabView.getParent() == this.mToolbar) {
                        this.mToolbar.removeView(this.mTabView);
                        break;
                    }
                    break;
                }
            }
            switch (this.mNavigationMode = mNavigationMode) {
                default: {
                    throw new IllegalArgumentException("Invalid navigation mode " + mNavigationMode);
                }
                case 1: {
                    this.ensureSpinner();
                    this.mToolbar.addView((View)this.mSpinner, 0);
                }
                case 0: {
                    break;
                }
                case 2: {
                    if (this.mTabView != null) {
                        this.mToolbar.addView(this.mTabView, 0);
                        final Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams)this.mTabView.getLayoutParams();
                        layoutParams.width = -2;
                        layoutParams.height = -2;
                        layoutParams.gravity = 8388691;
                        return;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void setSubtitle(final CharSequence charSequence) {
        this.mSubtitle = charSequence;
        if ((this.mDisplayOpts & 0x8) != 0x0) {
            this.mToolbar.setSubtitle(charSequence);
        }
    }
    
    @Override
    public void setTitle(final CharSequence titleInt) {
        this.mTitleSet = true;
        this.setTitleInt(titleInt);
    }
    
    @Override
    public void setVisibility(final int visibility) {
        this.mToolbar.setVisibility(visibility);
    }
    
    @Override
    public void setWindowCallback(final Window$Callback mWindowCallback) {
        this.mWindowCallback = mWindowCallback;
    }
    
    @Override
    public void setWindowTitle(final CharSequence titleInt) {
        if (!this.mTitleSet) {
            this.setTitleInt(titleInt);
        }
    }
    
    @Override
    public ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int n, final long duration) {
        final ViewPropertyAnimatorCompat animate = ViewCompat.animate((View)this.mToolbar);
        float n2;
        if (n == 0) {
            n2 = 1.0f;
        }
        else {
            n2 = 0.0f;
        }
        return animate.alpha(n2).setDuration(duration).setListener(new ViewPropertyAnimatorListenerAdapter() {
            private boolean mCanceled = false;
            
            @Override
            public void onAnimationCancel(final View view) {
                this.mCanceled = true;
            }
            
            @Override
            public void onAnimationEnd(final View view) {
                if (!this.mCanceled) {
                    ToolbarWidgetWrapper.this.mToolbar.setVisibility(n);
                }
            }
            
            @Override
            public void onAnimationStart(final View view) {
                ToolbarWidgetWrapper.this.mToolbar.setVisibility(0);
            }
        });
    }
    
    @Override
    public boolean showOverflowMenu() {
        return this.mToolbar.showOverflowMenu();
    }
}
