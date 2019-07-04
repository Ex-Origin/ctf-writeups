// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.ViewParent;
import android.os.SystemClock;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.MotionEvent;
import android.support.v4.view.ViewCompat;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;
import android.support.annotation.RequiresApi;
import android.view.View$OnAttachStateChangeListener;
import android.view.ViewConfiguration;
import android.os.Build$VERSION;
import android.view.View;
import android.support.annotation.RestrictTo;
import android.view.View$OnTouchListener;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public abstract class ForwardingListener implements View$OnTouchListener
{
    private int mActivePointerId;
    private Runnable mDisallowIntercept;
    private boolean mForwarding;
    private final int mLongPressTimeout;
    private final float mScaledTouchSlop;
    final View mSrc;
    private final int mTapTimeout;
    private final int[] mTmpLocation;
    private Runnable mTriggerLongPress;
    
    public ForwardingListener(final View mSrc) {
        this.mTmpLocation = new int[2];
        (this.mSrc = mSrc).setLongClickable(true);
        if (Build$VERSION.SDK_INT >= 12) {
            this.addDetachListenerApi12(mSrc);
        }
        else {
            this.addDetachListenerBase(mSrc);
        }
        this.mScaledTouchSlop = ViewConfiguration.get(mSrc.getContext()).getScaledTouchSlop();
        this.mTapTimeout = ViewConfiguration.getTapTimeout();
        this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }
    
    @RequiresApi(12)
    private void addDetachListenerApi12(final View view) {
        view.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)new View$OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(final View view) {
            }
            
            public void onViewDetachedFromWindow(final View view) {
                ForwardingListener.this.onDetachedFromWindow();
            }
        });
    }
    
    private void addDetachListenerBase(final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver$OnGlobalLayoutListener)new ViewTreeObserver$OnGlobalLayoutListener() {
            boolean mIsAttached = ViewCompat.isAttachedToWindow(ForwardingListener.this.mSrc);
            
            public void onGlobalLayout() {
                final boolean mIsAttached = this.mIsAttached;
                this.mIsAttached = ViewCompat.isAttachedToWindow(ForwardingListener.this.mSrc);
                if (mIsAttached && !this.mIsAttached) {
                    ForwardingListener.this.onDetachedFromWindow();
                }
            }
        });
    }
    
    private void clearCallbacks() {
        if (this.mTriggerLongPress != null) {
            this.mSrc.removeCallbacks(this.mTriggerLongPress);
        }
        if (this.mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(this.mDisallowIntercept);
        }
    }
    
    private void onDetachedFromWindow() {
        this.mForwarding = false;
        this.mActivePointerId = -1;
        if (this.mDisallowIntercept != null) {
            this.mSrc.removeCallbacks(this.mDisallowIntercept);
        }
    }
    
    private boolean onTouchForwarded(final MotionEvent motionEvent) {
        boolean b = true;
        final View mSrc = this.mSrc;
        final ShowableListMenu popup = this.getPopup();
        if (popup != null && popup.isShowing()) {
            final DropDownListView dropDownListView = (DropDownListView)popup.getListView();
            if (dropDownListView != null && dropDownListView.isShown()) {
                final MotionEvent obtainNoHistory = MotionEvent.obtainNoHistory(motionEvent);
                this.toGlobalMotionEvent(mSrc, obtainNoHistory);
                this.toLocalMotionEvent((View)dropDownListView, obtainNoHistory);
                final boolean onForwardedEvent = dropDownListView.onForwardedEvent(obtainNoHistory, this.mActivePointerId);
                obtainNoHistory.recycle();
                final int actionMasked = motionEvent.getActionMasked();
                boolean b2;
                if (actionMasked != 1 && actionMasked != 3) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                if (!onForwardedEvent || !b2) {
                    b = false;
                }
                return b;
            }
        }
        return false;
    }
    
    private boolean onTouchObserved(final MotionEvent motionEvent) {
        final View mSrc = this.mSrc;
        if (mSrc.isEnabled()) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    return false;
                }
                case 0: {
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    if (this.mDisallowIntercept == null) {
                        this.mDisallowIntercept = new DisallowIntercept();
                    }
                    mSrc.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
                    if (this.mTriggerLongPress == null) {
                        this.mTriggerLongPress = new TriggerLongPress();
                    }
                    mSrc.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
                    return false;
                }
                case 2: {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex >= 0 && !pointInView(mSrc, motionEvent.getX(pointerIndex), motionEvent.getY(pointerIndex), this.mScaledTouchSlop)) {
                        this.clearCallbacks();
                        mSrc.getParent().requestDisallowInterceptTouchEvent(true);
                        return true;
                    }
                    break;
                }
                case 1:
                case 3: {
                    this.clearCallbacks();
                    return false;
                }
            }
        }
        return false;
    }
    
    private static boolean pointInView(final View view, final float n, final float n2, final float n3) {
        return n >= -n3 && n2 >= -n3 && n < view.getRight() - view.getLeft() + n3 && n2 < view.getBottom() - view.getTop() + n3;
    }
    
    private boolean toGlobalMotionEvent(final View view, final MotionEvent motionEvent) {
        final int[] mTmpLocation = this.mTmpLocation;
        view.getLocationOnScreen(mTmpLocation);
        motionEvent.offsetLocation((float)mTmpLocation[0], (float)mTmpLocation[1]);
        return true;
    }
    
    private boolean toLocalMotionEvent(final View view, final MotionEvent motionEvent) {
        final int[] mTmpLocation = this.mTmpLocation;
        view.getLocationOnScreen(mTmpLocation);
        motionEvent.offsetLocation((float)(-mTmpLocation[0]), (float)(-mTmpLocation[1]));
        return true;
    }
    
    public abstract ShowableListMenu getPopup();
    
    protected boolean onForwardingStarted() {
        final ShowableListMenu popup = this.getPopup();
        if (popup != null && !popup.isShowing()) {
            popup.show();
        }
        return true;
    }
    
    protected boolean onForwardingStopped() {
        final ShowableListMenu popup = this.getPopup();
        if (popup != null && popup.isShowing()) {
            popup.dismiss();
        }
        return true;
    }
    
    void onLongPress() {
        this.clearCallbacks();
        final View mSrc = this.mSrc;
        if (mSrc.isEnabled() && !mSrc.isLongClickable() && this.onForwardingStarted()) {
            mSrc.getParent().requestDisallowInterceptTouchEvent(true);
            final long uptimeMillis = SystemClock.uptimeMillis();
            final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            mSrc.onTouchEvent(obtain);
            obtain.recycle();
            this.mForwarding = true;
        }
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final boolean b = false;
        final boolean mForwarding = this.mForwarding;
        boolean mForwarding2;
        if (mForwarding) {
            mForwarding2 = (this.onTouchForwarded(motionEvent) || !this.onForwardingStopped());
        }
        else {
            final boolean b2 = mForwarding2 = (this.onTouchObserved(motionEvent) && this.onForwardingStarted());
            if (b2) {
                final long uptimeMillis = SystemClock.uptimeMillis();
                final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                this.mSrc.onTouchEvent(obtain);
                obtain.recycle();
                mForwarding2 = b2;
            }
        }
        if (!(this.mForwarding = mForwarding2)) {
            final boolean b3 = b;
            if (!mForwarding) {
                return b3;
            }
        }
        return true;
    }
    
    private class DisallowIntercept implements Runnable
    {
        @Override
        public void run() {
            final ViewParent parent = ForwardingListener.this.mSrc.getParent();
            if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
        }
    }
    
    private class TriggerLongPress implements Runnable
    {
        @Override
        public void run() {
            ForwardingListener.this.onLongPress();
        }
    }
}
