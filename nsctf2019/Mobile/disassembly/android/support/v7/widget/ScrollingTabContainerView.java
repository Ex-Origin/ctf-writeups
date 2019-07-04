// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.ViewParent;
import android.text.TextUtils$TruncateAt;
import android.text.TextUtils;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.View$MeasureSpec;
import android.widget.AdapterView;
import android.content.res.Configuration;
import android.view.View$OnClickListener;
import android.widget.AbsListView$LayoutParams;
import android.graphics.drawable.Drawable;
import android.animation.Animator$AnimatorListener;
import android.animation.TimeInterpolator;
import android.support.v7.app.ActionBar;
import android.widget.SpinnerAdapter;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.support.v7.view.ActionBarPolicy;
import android.content.Context;
import android.view.animation.DecelerateInterpolator;
import android.view.ViewPropertyAnimator;
import android.widget.Spinner;
import android.view.animation.Interpolator;
import android.support.annotation.RestrictTo;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.HorizontalScrollView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ScrollingTabContainerView extends HorizontalScrollView implements AdapterView$OnItemSelectedListener
{
    private static final int FADE_DURATION = 200;
    private static final String TAG = "ScrollingTabContainerView";
    private static final Interpolator sAlphaInterpolator;
    private boolean mAllowCollapse;
    private int mContentHeight;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    LinearLayoutCompat mTabLayout;
    Runnable mTabSelector;
    private Spinner mTabSpinner;
    protected final VisibilityAnimListener mVisAnimListener;
    protected ViewPropertyAnimator mVisibilityAnim;
    
    static {
        sAlphaInterpolator = (Interpolator)new DecelerateInterpolator();
    }
    
    public ScrollingTabContainerView(final Context context) {
        super(context);
        this.mVisAnimListener = new VisibilityAnimListener();
        this.setHorizontalScrollBarEnabled(false);
        final ActionBarPolicy value = ActionBarPolicy.get(context);
        this.setContentHeight(value.getTabContainerHeight());
        this.mStackedTabMaxWidth = value.getStackedTabMaxWidth();
        this.addView((View)(this.mTabLayout = this.createTabLayout()), new ViewGroup$LayoutParams(-2, -1));
    }
    
    private Spinner createSpinner() {
        final AppCompatSpinner appCompatSpinner = new AppCompatSpinner(this.getContext(), null, R.attr.actionDropDownStyle);
        appCompatSpinner.setLayoutParams((ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)this);
        return appCompatSpinner;
    }
    
    private LinearLayoutCompat createTabLayout() {
        final LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this.getContext(), null, R.attr.actionBarTabBarStyle);
        linearLayoutCompat.setMeasureWithLargestChildEnabled(true);
        linearLayoutCompat.setGravity(17);
        linearLayoutCompat.setLayoutParams((ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        return linearLayoutCompat;
    }
    
    private boolean isCollapsed() {
        return this.mTabSpinner != null && this.mTabSpinner.getParent() == this;
    }
    
    private void performCollapse() {
        if (this.isCollapsed()) {
            return;
        }
        if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
        }
        this.removeView((View)this.mTabLayout);
        this.addView((View)this.mTabSpinner, new ViewGroup$LayoutParams(-2, -1));
        if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter((SpinnerAdapter)new TabAdapter());
        }
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
            this.mTabSelector = null;
        }
        this.mTabSpinner.setSelection(this.mSelectedTabIndex);
    }
    
    private boolean performExpand() {
        if (!this.isCollapsed()) {
            return false;
        }
        this.removeView((View)this.mTabSpinner);
        this.addView((View)this.mTabLayout, new ViewGroup$LayoutParams(-2, -1));
        this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }
    
    public void addTab(final ActionBar.Tab tab, final int n, final boolean b) {
        final TabView tabView = this.createTabView(tab, false);
        this.mTabLayout.addView((View)tabView, n, (ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (b) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void addTab(final ActionBar.Tab tab, final boolean b) {
        final TabView tabView = this.createTabView(tab, false);
        this.mTabLayout.addView((View)tabView, (ViewGroup$LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (b) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void animateToTab(final int n) {
        final View child = this.mTabLayout.getChildAt(n);
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }
        this.post(this.mTabSelector = new Runnable() {
            @Override
            public void run() {
                ScrollingTabContainerView.this.smoothScrollTo(child.getLeft() - (ScrollingTabContainerView.this.getWidth() - child.getWidth()) / 2, 0);
                ScrollingTabContainerView.this.mTabSelector = null;
            }
        });
    }
    
    public void animateToVisibility(final int n) {
        if (this.mVisibilityAnim != null) {
            this.mVisibilityAnim.cancel();
        }
        if (n == 0) {
            if (this.getVisibility() != 0) {
                this.setAlpha(0.0f);
            }
            final ViewPropertyAnimator alpha = this.animate().alpha(1.0f);
            alpha.setDuration(200L);
            alpha.setInterpolator((TimeInterpolator)ScrollingTabContainerView.sAlphaInterpolator);
            alpha.setListener((Animator$AnimatorListener)this.mVisAnimListener.withFinalVisibility(alpha, n));
            alpha.start();
            return;
        }
        final ViewPropertyAnimator alpha2 = this.animate().alpha(0.0f);
        alpha2.setDuration(200L);
        alpha2.setInterpolator((TimeInterpolator)ScrollingTabContainerView.sAlphaInterpolator);
        alpha2.setListener((Animator$AnimatorListener)this.mVisAnimListener.withFinalVisibility(alpha2, n));
        alpha2.start();
    }
    
    TabView createTabView(final ActionBar.Tab tab, final boolean b) {
        final TabView tabView = new TabView(this.getContext(), tab, b);
        if (b) {
            tabView.setBackgroundDrawable((Drawable)null);
            tabView.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, this.mContentHeight));
            return tabView;
        }
        tabView.setFocusable(true);
        if (this.mTabClickListener == null) {
            this.mTabClickListener = new TabClickListener();
        }
        tabView.setOnClickListener((View$OnClickListener)this.mTabClickListener);
        return tabView;
    }
    
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null) {
            this.post(this.mTabSelector);
        }
    }
    
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final ActionBarPolicy value = ActionBarPolicy.get(this.getContext());
        this.setContentHeight(value.getTabContainerHeight());
        this.mStackedTabMaxWidth = value.getStackedTabMaxWidth();
    }
    
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null) {
            this.removeCallbacks(this.mTabSelector);
        }
    }
    
    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        ((TabView)view).getTab().select();
    }
    
    public void onMeasure(int measuredWidth, int n) {
        n = View$MeasureSpec.getMode(measuredWidth);
        final boolean fillViewport = n == 1073741824;
        this.setFillViewport(fillViewport);
        final int childCount = this.mTabLayout.getChildCount();
        if (childCount > 1 && (n == 1073741824 || n == Integer.MIN_VALUE)) {
            if (childCount > 2) {
                this.mMaxTabWidth = (int)(View$MeasureSpec.getSize(measuredWidth) * 0.4f);
            }
            else {
                this.mMaxTabWidth = View$MeasureSpec.getSize(measuredWidth) / 2;
            }
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        }
        else {
            this.mMaxTabWidth = -1;
        }
        final int measureSpec = View$MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
        if (!fillViewport && this.mAllowCollapse) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            this.mTabLayout.measure(0, measureSpec);
            if (this.mTabLayout.getMeasuredWidth() > View$MeasureSpec.getSize(measuredWidth)) {
                this.performCollapse();
            }
            else {
                this.performExpand();
            }
        }
        else {
            this.performExpand();
        }
        n = this.getMeasuredWidth();
        super.onMeasure(measuredWidth, measureSpec);
        measuredWidth = this.getMeasuredWidth();
        if (fillViewport && n != measuredWidth) {
            this.setTabSelected(this.mSelectedTabIndex);
        }
    }
    
    public void onNothingSelected(final AdapterView<?> adapterView) {
    }
    
    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void removeTabAt(final int n) {
        this.mTabLayout.removeViewAt(n);
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    public void setAllowCollapse(final boolean mAllowCollapse) {
        this.mAllowCollapse = mAllowCollapse;
    }
    
    public void setContentHeight(final int mContentHeight) {
        this.mContentHeight = mContentHeight;
        this.requestLayout();
    }
    
    public void setTabSelected(final int n) {
        this.mSelectedTabIndex = n;
        for (int childCount = this.mTabLayout.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.mTabLayout.getChildAt(i);
            final boolean selected = i == n;
            child.setSelected(selected);
            if (selected) {
                this.animateToTab(n);
            }
        }
        if (this.mTabSpinner != null && n >= 0) {
            this.mTabSpinner.setSelection(n);
        }
    }
    
    public void updateTab(final int n) {
        ((TabView)this.mTabLayout.getChildAt(n)).update();
        if (this.mTabSpinner != null) {
            ((TabAdapter)this.mTabSpinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }
    
    private class TabAdapter extends BaseAdapter
    {
        public int getCount() {
            return ScrollingTabContainerView.this.mTabLayout.getChildCount();
        }
        
        public Object getItem(final int n) {
            return ((TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(n)).getTab();
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            if (view == null) {
                return (View)ScrollingTabContainerView.this.createTabView((ActionBar.Tab)this.getItem(n), true);
            }
            ((TabView)view).bindTab((ActionBar.Tab)this.getItem(n));
            return view;
        }
    }
    
    private class TabClickListener implements View$OnClickListener
    {
        public void onClick(final View view) {
            ((TabView)view).getTab().select();
            for (int childCount = ScrollingTabContainerView.this.mTabLayout.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = ScrollingTabContainerView.this.mTabLayout.getChildAt(i);
                child.setSelected(child == view);
            }
        }
    }
    
    private class TabView extends LinearLayoutCompat
    {
        private final int[] BG_ATTRS;
        private View mCustomView;
        private ImageView mIconView;
        private ActionBar.Tab mTab;
        private TextView mTextView;
        
        public TabView(final Context context, final ActionBar.Tab mTab, final boolean b) {
            super(context, null, R.attr.actionBarTabStyle);
            this.BG_ATTRS = new int[] { 16842964 };
            this.mTab = mTab;
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, null, this.BG_ATTRS, R.attr.actionBarTabStyle, 0);
            if (obtainStyledAttributes.hasValue(0)) {
                this.setBackgroundDrawable(obtainStyledAttributes.getDrawable(0));
            }
            obtainStyledAttributes.recycle();
            if (b) {
                this.setGravity(8388627);
            }
            this.update();
        }
        
        public void bindTab(final ActionBar.Tab mTab) {
            this.mTab = mTab;
            this.update();
        }
        
        public ActionBar.Tab getTab() {
            return this.mTab;
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            if (Build$VERSION.SDK_INT >= 14) {
                accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
            }
        }
        
        public void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
            if (ScrollingTabContainerView.this.mMaxTabWidth > 0 && this.getMeasuredWidth() > ScrollingTabContainerView.this.mMaxTabWidth) {
                super.onMeasure(View$MeasureSpec.makeMeasureSpec(ScrollingTabContainerView.this.mMaxTabWidth, 1073741824), n2);
            }
        }
        
        public void setSelected(final boolean selected) {
            boolean b;
            if (this.isSelected() != selected) {
                b = true;
            }
            else {
                b = false;
            }
            super.setSelected(selected);
            if (b && selected) {
                this.sendAccessibilityEvent(4);
            }
        }
        
        public void update() {
            CharSequence contentDescription = null;
            final ActionBar.Tab mTab = this.mTab;
            final View customView = mTab.getCustomView();
            if (customView != null) {
                final ViewParent parent = customView.getParent();
                if (parent != this) {
                    if (parent != null) {
                        ((TabView)parent).removeView(customView);
                    }
                    this.addView(customView);
                }
                this.mCustomView = customView;
                if (this.mTextView != null) {
                    this.mTextView.setVisibility(8);
                }
                if (this.mIconView != null) {
                    this.mIconView.setVisibility(8);
                    this.mIconView.setImageDrawable((Drawable)null);
                }
                return;
            }
            if (this.mCustomView != null) {
                this.removeView(this.mCustomView);
                this.mCustomView = null;
            }
            final Drawable icon = mTab.getIcon();
            final CharSequence text = mTab.getText();
            if (icon != null) {
                if (this.mIconView == null) {
                    final AppCompatImageView mIconView = new AppCompatImageView(this.getContext());
                    final LayoutParams layoutParams = new LayoutParams(-2, -2);
                    layoutParams.gravity = 16;
                    mIconView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                    this.addView((View)mIconView, 0);
                    this.mIconView = mIconView;
                }
                this.mIconView.setImageDrawable(icon);
                this.mIconView.setVisibility(0);
            }
            else if (this.mIconView != null) {
                this.mIconView.setVisibility(8);
                this.mIconView.setImageDrawable((Drawable)null);
            }
            boolean b;
            if (!TextUtils.isEmpty(text)) {
                b = true;
            }
            else {
                b = false;
            }
            if (b) {
                if (this.mTextView == null) {
                    final AppCompatTextView mTextView = new AppCompatTextView(this.getContext(), null, R.attr.actionBarTabTextStyle);
                    mTextView.setEllipsize(TextUtils$TruncateAt.END);
                    final LayoutParams layoutParams2 = new LayoutParams(-2, -2);
                    layoutParams2.gravity = 16;
                    mTextView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                    this.addView((View)mTextView);
                    this.mTextView = mTextView;
                }
                this.mTextView.setText(text);
                this.mTextView.setVisibility(0);
            }
            else if (this.mTextView != null) {
                this.mTextView.setVisibility(8);
                this.mTextView.setText((CharSequence)null);
            }
            if (this.mIconView != null) {
                this.mIconView.setContentDescription(mTab.getContentDescription());
            }
            if (!b) {
                contentDescription = mTab.getContentDescription();
            }
            TooltipCompat.setTooltipText((View)this, contentDescription);
        }
    }
    
    protected class VisibilityAnimListener extends AnimatorListenerAdapter
    {
        private boolean mCanceled;
        private int mFinalVisibility;
        
        protected VisibilityAnimListener() {
            this.mCanceled = false;
        }
        
        public void onAnimationCancel(final Animator animator) {
            this.mCanceled = true;
        }
        
        public void onAnimationEnd(final Animator animator) {
            if (this.mCanceled) {
                return;
            }
            ScrollingTabContainerView.this.mVisibilityAnim = null;
            ScrollingTabContainerView.this.setVisibility(this.mFinalVisibility);
        }
        
        public void onAnimationStart(final Animator animator) {
            ScrollingTabContainerView.this.setVisibility(0);
            this.mCanceled = false;
        }
        
        public VisibilityAnimListener withFinalVisibility(final ViewPropertyAnimator mVisibilityAnim, final int mFinalVisibility) {
            this.mFinalVisibility = mFinalVisibility;
            ScrollingTabContainerView.this.mVisibilityAnim = mVisibilityAnim;
            return this;
        }
    }
}
