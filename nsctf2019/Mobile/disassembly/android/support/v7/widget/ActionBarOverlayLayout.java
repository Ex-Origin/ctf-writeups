// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.ViewGroup$MarginLayoutParams;
import android.view.Window$Callback;
import android.support.v7.view.menu.MenuPresenter;
import android.view.Menu;
import android.os.Parcelable;
import android.util.SparseArray;
import android.os.Build$VERSION;
import android.content.res.Configuration;
import android.support.v4.view.ViewCompat;
import android.graphics.Canvas;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.view.View;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v7.appcompat.R;
import android.graphics.drawable.Drawable;
import android.animation.AnimatorListenerAdapter;
import android.support.v4.view.NestedScrollingParentHelper;
import android.widget.OverScroller;
import android.view.ViewPropertyAnimator;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v4.view.NestedScrollingParent;
import android.view.ViewGroup;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ActionBarOverlayLayout extends ViewGroup implements DecorContentParent, NestedScrollingParent
{
    static final int[] ATTRS;
    private static final String TAG = "ActionBarOverlayLayout";
    private final int ACTION_BAR_ANIMATE_DELAY;
    private int mActionBarHeight;
    ActionBarContainer mActionBarTop;
    private ActionBarVisibilityCallback mActionBarVisibilityCallback;
    private final Runnable mAddActionBarHideOffset;
    boolean mAnimatingForFling;
    private final Rect mBaseContentInsets;
    private final Rect mBaseInnerInsets;
    private ContentFrameLayout mContent;
    private final Rect mContentInsets;
    ViewPropertyAnimator mCurrentActionBarTopAnimator;
    private DecorToolbar mDecorToolbar;
    private OverScroller mFlingEstimator;
    private boolean mHasNonEmbeddedTabs;
    private boolean mHideOnContentScroll;
    private int mHideOnContentScrollReference;
    private boolean mIgnoreWindowContentOverlay;
    private final Rect mInnerInsets;
    private final Rect mLastBaseContentInsets;
    private final Rect mLastInnerInsets;
    private int mLastSystemUiVisibility;
    private boolean mOverlayMode;
    private final NestedScrollingParentHelper mParentHelper;
    private final Runnable mRemoveActionBarHideOffset;
    final AnimatorListenerAdapter mTopAnimatorListener;
    private Drawable mWindowContentOverlay;
    private int mWindowVisibility;
    
    static {
        ATTRS = new int[] { R.attr.actionBarSize, 16842841 };
    }
    
    public ActionBarOverlayLayout(final Context context) {
        this(context, null);
    }
    
    public ActionBarOverlayLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mWindowVisibility = 0;
        this.mBaseContentInsets = new Rect();
        this.mLastBaseContentInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mBaseInnerInsets = new Rect();
        this.mInnerInsets = new Rect();
        this.mLastInnerInsets = new Rect();
        this.ACTION_BAR_ANIMATE_DELAY = 600;
        this.mTopAnimatorListener = new AnimatorListenerAdapter() {
            public void onAnimationCancel(final Animator animator) {
                ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = null;
                ActionBarOverlayLayout.this.mAnimatingForFling = false;
            }
            
            public void onAnimationEnd(final Animator animator) {
                ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = null;
                ActionBarOverlayLayout.this.mAnimatingForFling = false;
            }
        };
        this.mRemoveActionBarHideOffset = new Runnable() {
            @Override
            public void run() {
                ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
                ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = ActionBarOverlayLayout.this.mActionBarTop.animate().translationY(0.0f).setListener((Animator$AnimatorListener)ActionBarOverlayLayout.this.mTopAnimatorListener);
            }
        };
        this.mAddActionBarHideOffset = new Runnable() {
            @Override
            public void run() {
                ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
                ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = ActionBarOverlayLayout.this.mActionBarTop.animate().translationY((float)(-ActionBarOverlayLayout.this.mActionBarTop.getHeight())).setListener((Animator$AnimatorListener)ActionBarOverlayLayout.this.mTopAnimatorListener);
            }
        };
        this.init(context);
        this.mParentHelper = new NestedScrollingParentHelper(this);
    }
    
    private void addActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.mAddActionBarHideOffset.run();
    }
    
    private boolean applyInsets(final View view, final Rect rect, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        final boolean b5 = false;
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        boolean b6 = b5;
        if (b) {
            b6 = b5;
            if (layoutParams.leftMargin != rect.left) {
                b6 = true;
                layoutParams.leftMargin = rect.left;
            }
        }
        boolean b7 = b6;
        if (b2) {
            b7 = b6;
            if (layoutParams.topMargin != rect.top) {
                b7 = true;
                layoutParams.topMargin = rect.top;
            }
        }
        boolean b8 = b7;
        if (b4) {
            b8 = b7;
            if (layoutParams.rightMargin != rect.right) {
                b8 = true;
                layoutParams.rightMargin = rect.right;
            }
        }
        boolean b9 = b8;
        if (b3) {
            b9 = b8;
            if (layoutParams.bottomMargin != rect.bottom) {
                b9 = true;
                layoutParams.bottomMargin = rect.bottom;
            }
        }
        return b9;
    }
    
    private DecorToolbar getDecorToolbar(final View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar)view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar)view).getWrapper();
        }
        throw new IllegalStateException("Can't make a decor toolbar out of " + view.getClass().getSimpleName());
    }
    
    private void init(final Context context) {
        final boolean b = true;
        final TypedArray obtainStyledAttributes = this.getContext().getTheme().obtainStyledAttributes(ActionBarOverlayLayout.ATTRS);
        this.mActionBarHeight = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        this.mWindowContentOverlay = obtainStyledAttributes.getDrawable(1);
        this.setWillNotDraw(this.mWindowContentOverlay == null);
        obtainStyledAttributes.recycle();
        this.mIgnoreWindowContentOverlay = (context.getApplicationInfo().targetSdkVersion < 19 && b);
        this.mFlingEstimator = new OverScroller(context);
    }
    
    private void postAddActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.postDelayed(this.mAddActionBarHideOffset, 600L);
    }
    
    private void postRemoveActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.postDelayed(this.mRemoveActionBarHideOffset, 600L);
    }
    
    private void removeActionBarHideOffset() {
        this.haltActionBarHideOffsetAnimations();
        this.mRemoveActionBarHideOffset.run();
    }
    
    private boolean shouldHideActionBarOnFling(final float n, final float n2) {
        boolean b = false;
        this.mFlingEstimator.fling(0, 0, 0, (int)n2, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        if (this.mFlingEstimator.getFinalY() > this.mActionBarTop.getHeight()) {
            b = true;
        }
        return b;
    }
    
    public boolean canShowOverflowMenu() {
        this.pullChildren();
        return this.mDecorToolbar.canShowOverflowMenu();
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams;
    }
    
    public void dismissPopups() {
        this.pullChildren();
        this.mDecorToolbar.dismissPopupMenus();
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        if (this.mWindowContentOverlay != null && !this.mIgnoreWindowContentOverlay) {
            int n;
            if (this.mActionBarTop.getVisibility() == 0) {
                n = (int)(this.mActionBarTop.getBottom() + this.mActionBarTop.getTranslationY() + 0.5f);
            }
            else {
                n = 0;
            }
            this.mWindowContentOverlay.setBounds(0, n, this.getWidth(), this.mWindowContentOverlay.getIntrinsicHeight() + n);
            this.mWindowContentOverlay.draw(canvas);
        }
    }
    
    protected boolean fitSystemWindows(final Rect rect) {
        this.pullChildren();
        if ((ViewCompat.getWindowSystemUiVisibility((View)this) & 0x100) != 0x0) {}
        boolean applyInsets = this.applyInsets((View)this.mActionBarTop, rect, true, true, false, true);
        this.mBaseInnerInsets.set(rect);
        ViewUtils.computeFitSystemWindows((View)this, this.mBaseInnerInsets, this.mBaseContentInsets);
        if (!this.mLastBaseContentInsets.equals((Object)this.mBaseContentInsets)) {
            applyInsets = true;
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        }
        if (applyInsets) {
            this.requestLayout();
        }
        return true;
    }
    
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }
    
    public LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return (ViewGroup$LayoutParams)new LayoutParams(viewGroup$LayoutParams);
    }
    
    public int getActionBarHideOffset() {
        if (this.mActionBarTop != null) {
            return -(int)this.mActionBarTop.getTranslationY();
        }
        return 0;
    }
    
    public int getNestedScrollAxes() {
        return this.mParentHelper.getNestedScrollAxes();
    }
    
    public CharSequence getTitle() {
        this.pullChildren();
        return this.mDecorToolbar.getTitle();
    }
    
    void haltActionBarHideOffsetAnimations() {
        this.removeCallbacks(this.mRemoveActionBarHideOffset);
        this.removeCallbacks(this.mAddActionBarHideOffset);
        if (this.mCurrentActionBarTopAnimator != null) {
            this.mCurrentActionBarTopAnimator.cancel();
        }
    }
    
    public boolean hasIcon() {
        this.pullChildren();
        return this.mDecorToolbar.hasIcon();
    }
    
    public boolean hasLogo() {
        this.pullChildren();
        return this.mDecorToolbar.hasLogo();
    }
    
    public boolean hideOverflowMenu() {
        this.pullChildren();
        return this.mDecorToolbar.hideOverflowMenu();
    }
    
    public void initFeature(final int n) {
        this.pullChildren();
        switch (n) {
            default: {}
            case 2: {
                this.mDecorToolbar.initProgress();
            }
            case 5: {
                this.mDecorToolbar.initIndeterminateProgress();
            }
            case 109: {
                this.setOverlayMode(true);
            }
        }
    }
    
    public boolean isHideOnContentScrollEnabled() {
        return this.mHideOnContentScroll;
    }
    
    public boolean isInOverlayMode() {
        return this.mOverlayMode;
    }
    
    public boolean isOverflowMenuShowPending() {
        this.pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        this.pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowing();
    }
    
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.init(this.getContext());
        ViewCompat.requestApplyInsets((View)this);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.haltActionBarHideOffsetAnimations();
    }
    
    protected void onLayout(final boolean b, int i, int childCount, int paddingLeft, int paddingTop) {
        childCount = this.getChildCount();
        paddingLeft = this.getPaddingLeft();
        this.getPaddingRight();
        paddingTop = this.getPaddingTop();
        this.getPaddingBottom();
        View child;
        LayoutParams layoutParams;
        int measuredWidth;
        int measuredHeight;
        int n;
        int n2;
        for (i = 0; i < childCount; ++i) {
            child = this.getChildAt(i);
            if (child.getVisibility() != 8) {
                layoutParams = (LayoutParams)child.getLayoutParams();
                measuredWidth = child.getMeasuredWidth();
                measuredHeight = child.getMeasuredHeight();
                n = paddingLeft + layoutParams.leftMargin;
                n2 = paddingTop + layoutParams.topMargin;
                child.layout(n, n2, n + measuredWidth, n2 + measuredHeight);
            }
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.pullChildren();
        int n3 = 0;
        this.measureChildWithMargins((View)this.mActionBarTop, n, 0, n2, 0);
        final LayoutParams layoutParams = (LayoutParams)this.mActionBarTop.getLayoutParams();
        final int max = Math.max(0, this.mActionBarTop.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
        final int max2 = Math.max(0, this.mActionBarTop.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
        final int combineMeasuredStates = View.combineMeasuredStates(0, this.mActionBarTop.getMeasuredState());
        boolean b;
        if ((ViewCompat.getWindowSystemUiVisibility((View)this) & 0x100) != 0x0) {
            b = true;
        }
        else {
            b = false;
        }
        if (b) {
            final int n4 = n3 = this.mActionBarHeight;
            if (this.mHasNonEmbeddedTabs) {
                n3 = n4;
                if (this.mActionBarTop.getTabContainer() != null) {
                    n3 = n4 + this.mActionBarHeight;
                }
            }
        }
        else if (this.mActionBarTop.getVisibility() != 8) {
            n3 = this.mActionBarTop.getMeasuredHeight();
        }
        this.mContentInsets.set(this.mBaseContentInsets);
        this.mInnerInsets.set(this.mBaseInnerInsets);
        if (!this.mOverlayMode && !b) {
            final Rect mContentInsets = this.mContentInsets;
            mContentInsets.top += n3;
            final Rect mContentInsets2 = this.mContentInsets;
            mContentInsets2.bottom += 0;
        }
        else {
            final Rect mInnerInsets = this.mInnerInsets;
            mInnerInsets.top += n3;
            final Rect mInnerInsets2 = this.mInnerInsets;
            mInnerInsets2.bottom += 0;
        }
        this.applyInsets((View)this.mContent, this.mContentInsets, true, true, true, true);
        if (!this.mLastInnerInsets.equals((Object)this.mInnerInsets)) {
            this.mLastInnerInsets.set(this.mInnerInsets);
            this.mContent.dispatchFitSystemWindows(this.mInnerInsets);
        }
        this.measureChildWithMargins((View)this.mContent, n, 0, n2, 0);
        final LayoutParams layoutParams2 = (LayoutParams)this.mContent.getLayoutParams();
        final int max3 = Math.max(max, this.mContent.getMeasuredWidth() + layoutParams2.leftMargin + layoutParams2.rightMargin);
        final int max4 = Math.max(max2, this.mContent.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin);
        final int combineMeasuredStates2 = View.combineMeasuredStates(combineMeasuredStates, this.mContent.getMeasuredState());
        this.setMeasuredDimension(View.resolveSizeAndState(Math.max(max3 + (this.getPaddingLeft() + this.getPaddingRight()), this.getSuggestedMinimumWidth()), n, combineMeasuredStates2), View.resolveSizeAndState(Math.max(max4 + (this.getPaddingTop() + this.getPaddingBottom()), this.getSuggestedMinimumHeight()), n2, combineMeasuredStates2 << 16));
    }
    
    public boolean onNestedFling(final View view, final float n, final float n2, final boolean b) {
        if (!this.mHideOnContentScroll || !b) {
            return false;
        }
        if (this.shouldHideActionBarOnFling(n, n2)) {
            this.addActionBarHideOffset();
        }
        else {
            this.removeActionBarHideOffset();
        }
        return this.mAnimatingForFling = true;
    }
    
    public boolean onNestedPreFling(final View view, final float n, final float n2) {
        return false;
    }
    
    public void onNestedPreScroll(final View view, final int n, final int n2, final int[] array) {
    }
    
    public void onNestedScroll(final View view, final int n, final int n2, final int n3, final int n4) {
        this.setActionBarHideOffset(this.mHideOnContentScrollReference += n2);
    }
    
    public void onNestedScrollAccepted(final View view, final View view2, final int n) {
        this.mParentHelper.onNestedScrollAccepted(view, view2, n);
        this.mHideOnContentScrollReference = this.getActionBarHideOffset();
        this.haltActionBarHideOffsetAnimations();
        if (this.mActionBarVisibilityCallback != null) {
            this.mActionBarVisibilityCallback.onContentScrollStarted();
        }
    }
    
    public boolean onStartNestedScroll(final View view, final View view2, final int n) {
        return (n & 0x2) != 0x0 && this.mActionBarTop.getVisibility() == 0 && this.mHideOnContentScroll;
    }
    
    public void onStopNestedScroll(final View view) {
        if (this.mHideOnContentScroll && !this.mAnimatingForFling) {
            if (this.mHideOnContentScrollReference <= this.mActionBarTop.getHeight()) {
                this.postRemoveActionBarHideOffset();
            }
            else {
                this.postAddActionBarHideOffset();
            }
        }
        if (this.mActionBarVisibilityCallback != null) {
            this.mActionBarVisibilityCallback.onContentScrollStopped();
        }
    }
    
    public void onWindowSystemUiVisibilityChanged(final int mLastSystemUiVisibility) {
        boolean b = true;
        if (Build$VERSION.SDK_INT >= 16) {
            super.onWindowSystemUiVisibilityChanged(mLastSystemUiVisibility);
        }
        this.pullChildren();
        final int mLastSystemUiVisibility2 = this.mLastSystemUiVisibility;
        this.mLastSystemUiVisibility = mLastSystemUiVisibility;
        boolean b2;
        if ((mLastSystemUiVisibility & 0x4) == 0x0) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        boolean b3;
        if ((mLastSystemUiVisibility & 0x100) != 0x0) {
            b3 = true;
        }
        else {
            b3 = false;
        }
        if (this.mActionBarVisibilityCallback != null) {
            final ActionBarVisibilityCallback mActionBarVisibilityCallback = this.mActionBarVisibilityCallback;
            if (b3) {
                b = false;
            }
            mActionBarVisibilityCallback.enableContentAnimations(b);
            if (b2 || !b3) {
                this.mActionBarVisibilityCallback.showForSystem();
            }
            else {
                this.mActionBarVisibilityCallback.hideForSystem();
            }
        }
        if (((mLastSystemUiVisibility2 ^ mLastSystemUiVisibility) & 0x100) != 0x0 && this.mActionBarVisibilityCallback != null) {
            ViewCompat.requestApplyInsets((View)this);
        }
    }
    
    protected void onWindowVisibilityChanged(final int mWindowVisibility) {
        super.onWindowVisibilityChanged(mWindowVisibility);
        this.mWindowVisibility = mWindowVisibility;
        if (this.mActionBarVisibilityCallback != null) {
            this.mActionBarVisibilityCallback.onWindowVisibilityChanged(mWindowVisibility);
        }
    }
    
    void pullChildren() {
        if (this.mContent == null) {
            this.mContent = (ContentFrameLayout)this.findViewById(R.id.action_bar_activity_content);
            this.mActionBarTop = (ActionBarContainer)this.findViewById(R.id.action_bar_container);
            this.mDecorToolbar = this.getDecorToolbar(this.findViewById(R.id.action_bar));
        }
    }
    
    public void restoreToolbarHierarchyState(final SparseArray<Parcelable> sparseArray) {
        this.pullChildren();
        this.mDecorToolbar.restoreHierarchyState(sparseArray);
    }
    
    public void saveToolbarHierarchyState(final SparseArray<Parcelable> sparseArray) {
        this.pullChildren();
        this.mDecorToolbar.saveHierarchyState(sparseArray);
    }
    
    public void setActionBarHideOffset(int max) {
        this.haltActionBarHideOffsetAnimations();
        max = Math.max(0, Math.min(max, this.mActionBarTop.getHeight()));
        this.mActionBarTop.setTranslationY((float)(-max));
    }
    
    public void setActionBarVisibilityCallback(final ActionBarVisibilityCallback mActionBarVisibilityCallback) {
        this.mActionBarVisibilityCallback = mActionBarVisibilityCallback;
        if (this.getWindowToken() != null) {
            this.mActionBarVisibilityCallback.onWindowVisibilityChanged(this.mWindowVisibility);
            if (this.mLastSystemUiVisibility != 0) {
                this.onWindowSystemUiVisibilityChanged(this.mLastSystemUiVisibility);
                ViewCompat.requestApplyInsets((View)this);
            }
        }
    }
    
    public void setHasNonEmbeddedTabs(final boolean mHasNonEmbeddedTabs) {
        this.mHasNonEmbeddedTabs = mHasNonEmbeddedTabs;
    }
    
    public void setHideOnContentScrollEnabled(final boolean mHideOnContentScroll) {
        if (mHideOnContentScroll != this.mHideOnContentScroll && !(this.mHideOnContentScroll = mHideOnContentScroll)) {
            this.haltActionBarHideOffsetAnimations();
            this.setActionBarHideOffset(0);
        }
    }
    
    public void setIcon(final int icon) {
        this.pullChildren();
        this.mDecorToolbar.setIcon(icon);
    }
    
    public void setIcon(final Drawable icon) {
        this.pullChildren();
        this.mDecorToolbar.setIcon(icon);
    }
    
    public void setLogo(final int logo) {
        this.pullChildren();
        this.mDecorToolbar.setLogo(logo);
    }
    
    public void setMenu(final Menu menu, final MenuPresenter.Callback callback) {
        this.pullChildren();
        this.mDecorToolbar.setMenu(menu, callback);
    }
    
    public void setMenuPrepared() {
        this.pullChildren();
        this.mDecorToolbar.setMenuPrepared();
    }
    
    public void setOverlayMode(final boolean mOverlayMode) {
        this.mOverlayMode = mOverlayMode;
        this.mIgnoreWindowContentOverlay = (mOverlayMode && this.getContext().getApplicationInfo().targetSdkVersion < 19);
    }
    
    public void setShowingForActionMode(final boolean b) {
    }
    
    public void setUiOptions(final int n) {
    }
    
    public void setWindowCallback(final Window$Callback windowCallback) {
        this.pullChildren();
        this.mDecorToolbar.setWindowCallback(windowCallback);
    }
    
    public void setWindowTitle(final CharSequence windowTitle) {
        this.pullChildren();
        this.mDecorToolbar.setWindowTitle(windowTitle);
    }
    
    public boolean shouldDelayChildPressedState() {
        return false;
    }
    
    public boolean showOverflowMenu() {
        this.pullChildren();
        return this.mDecorToolbar.showOverflowMenu();
    }
    
    public interface ActionBarVisibilityCallback
    {
        void enableContentAnimations(final boolean p0);
        
        void hideForSystem();
        
        void onContentScrollStarted();
        
        void onContentScrollStopped();
        
        void onWindowVisibilityChanged(final int p0);
        
        void showForSystem();
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
        }
    }
}
