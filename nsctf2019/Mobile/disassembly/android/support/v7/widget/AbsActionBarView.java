// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.content.res.TypedArray;
import android.content.res.Configuration;
import android.view.View$MeasureSpec;
import android.view.View;
import android.view.ContextThemeWrapper;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.util.AttributeSet;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.content.Context;
import android.view.ViewGroup;

abstract class AbsActionBarView extends ViewGroup
{
    private static final int FADE_DURATION = 200;
    protected ActionMenuPresenter mActionMenuPresenter;
    protected int mContentHeight;
    private boolean mEatingHover;
    private boolean mEatingTouch;
    protected ActionMenuView mMenuView;
    protected final Context mPopupContext;
    protected final VisibilityAnimListener mVisAnimListener;
    protected ViewPropertyAnimatorCompat mVisibilityAnim;
    
    AbsActionBarView(final Context context) {
        this(context, null);
    }
    
    AbsActionBarView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    AbsActionBarView(final Context mPopupContext, final AttributeSet set, final int n) {
        super(mPopupContext, set, n);
        this.mVisAnimListener = new VisibilityAnimListener();
        final TypedValue typedValue = new TypedValue();
        if (mPopupContext.getTheme().resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true) && typedValue.resourceId != 0) {
            this.mPopupContext = (Context)new ContextThemeWrapper(mPopupContext, typedValue.resourceId);
            return;
        }
        this.mPopupContext = mPopupContext;
    }
    
    static /* synthetic */ void access$001(final AbsActionBarView absActionBarView, final int visibility) {
        absActionBarView.setVisibility(visibility);
    }
    
    static /* synthetic */ void access$101(final AbsActionBarView absActionBarView, final int visibility) {
        absActionBarView.setVisibility(visibility);
    }
    
    protected static int next(final int n, final int n2, final boolean b) {
        if (b) {
            return n - n2;
        }
        return n + n2;
    }
    
    public void animateToVisibility(final int n) {
        this.setupAnimatorToVisibility(n, 200L).start();
    }
    
    public boolean canShowOverflowMenu() {
        return this.isOverflowReserved() && this.getVisibility() == 0;
    }
    
    public void dismissPopupMenus() {
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
    }
    
    public int getAnimatedVisibility() {
        if (this.mVisibilityAnim != null) {
            return this.mVisAnimListener.mFinalVisibility;
        }
        return this.getVisibility();
    }
    
    public int getContentHeight() {
        return this.mContentHeight;
    }
    
    public boolean hideOverflowMenu() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.hideOverflowMenu();
    }
    
    public boolean isOverflowMenuShowPending() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowMenuShowPending();
    }
    
    public boolean isOverflowMenuShowing() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowMenuShowing();
    }
    
    public boolean isOverflowReserved() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.isOverflowReserved();
    }
    
    protected int measureChildView(final View view, final int n, final int n2, final int n3) {
        view.measure(View$MeasureSpec.makeMeasureSpec(n, Integer.MIN_VALUE), n2);
        return Math.max(0, n - view.getMeasuredWidth() - n3);
    }
    
    protected void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes((AttributeSet)null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        this.setContentHeight(obtainStyledAttributes.getLayoutDimension(R.styleable.ActionBar_height, 0));
        obtainStyledAttributes.recycle();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.onConfigurationChanged(configuration);
        }
    }
    
    public boolean onHoverEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 9) {
            this.mEatingHover = false;
        }
        if (!this.mEatingHover) {
            final boolean onHoverEvent = super.onHoverEvent(motionEvent);
            if (actionMasked == 9 && !onHoverEvent) {
                this.mEatingHover = true;
            }
        }
        if (actionMasked == 10 || actionMasked == 3) {
            this.mEatingHover = false;
        }
        return true;
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            final boolean onTouchEvent = super.onTouchEvent(motionEvent);
            if (actionMasked == 0 && !onTouchEvent) {
                this.mEatingTouch = true;
            }
        }
        if (actionMasked == 1 || actionMasked == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }
    
    protected int positionChild(final View view, int n, int n2, final int n3, final boolean b) {
        final int measuredWidth = view.getMeasuredWidth();
        final int measuredHeight = view.getMeasuredHeight();
        n2 += (n3 - measuredHeight) / 2;
        if (b) {
            view.layout(n - measuredWidth, n2, n, n2 + measuredHeight);
        }
        else {
            view.layout(n, n2, n + measuredWidth, n2 + measuredHeight);
        }
        n = measuredWidth;
        if (b) {
            n = -measuredWidth;
        }
        return n;
    }
    
    public void postShowOverflowMenu() {
        this.post((Runnable)new Runnable() {
            @Override
            public void run() {
                AbsActionBarView.this.showOverflowMenu();
            }
        });
    }
    
    public void setContentHeight(final int mContentHeight) {
        this.mContentHeight = mContentHeight;
        this.requestLayout();
    }
    
    public void setVisibility(final int visibility) {
        if (visibility != this.getVisibility()) {
            if (this.mVisibilityAnim != null) {
                this.mVisibilityAnim.cancel();
            }
            super.setVisibility(visibility);
        }
    }
    
    public ViewPropertyAnimatorCompat setupAnimatorToVisibility(final int n, final long n2) {
        if (this.mVisibilityAnim != null) {
            this.mVisibilityAnim.cancel();
        }
        if (n == 0) {
            if (this.getVisibility() != 0) {
                this.setAlpha(0.0f);
            }
            final ViewPropertyAnimatorCompat alpha = ViewCompat.animate((View)this).alpha(1.0f);
            alpha.setDuration(n2);
            alpha.setListener(this.mVisAnimListener.withFinalVisibility(alpha, n));
            return alpha;
        }
        final ViewPropertyAnimatorCompat alpha2 = ViewCompat.animate((View)this).alpha(0.0f);
        alpha2.setDuration(n2);
        alpha2.setListener(this.mVisAnimListener.withFinalVisibility(alpha2, n));
        return alpha2;
    }
    
    public boolean showOverflowMenu() {
        return this.mActionMenuPresenter != null && this.mActionMenuPresenter.showOverflowMenu();
    }
    
    protected class VisibilityAnimListener implements ViewPropertyAnimatorListener
    {
        private boolean mCanceled;
        int mFinalVisibility;
        
        protected VisibilityAnimListener() {
            this.mCanceled = false;
        }
        
        @Override
        public void onAnimationCancel(final View view) {
            this.mCanceled = true;
        }
        
        @Override
        public void onAnimationEnd(final View view) {
            if (this.mCanceled) {
                return;
            }
            AbsActionBarView.this.mVisibilityAnim = null;
            AbsActionBarView.access$101(AbsActionBarView.this, this.mFinalVisibility);
        }
        
        @Override
        public void onAnimationStart(final View view) {
            AbsActionBarView.access$001(AbsActionBarView.this, 0);
            this.mCanceled = false;
        }
        
        public VisibilityAnimListener withFinalVisibility(final ViewPropertyAnimatorCompat mVisibilityAnim, final int mFinalVisibility) {
            AbsActionBarView.this.mVisibilityAnim = mVisibilityAnim;
            this.mFinalVisibility = mFinalVisibility;
            return this;
        }
    }
}
