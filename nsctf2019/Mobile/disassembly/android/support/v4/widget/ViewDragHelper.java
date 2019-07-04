// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.view.MotionEvent;
import android.util.Log;
import android.support.v4.view.ViewCompat;
import java.util.Arrays;
import android.view.ViewConfiguration;
import android.content.Context;
import android.view.VelocityTracker;
import android.widget.OverScroller;
import android.view.ViewGroup;
import android.view.View;
import android.view.animation.Interpolator;

public class ViewDragHelper
{
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator;
    private int mActivePointerId;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private OverScroller mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;
    
    static {
        sInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
    }
    
    private ViewDragHelper(final Context context, final ViewGroup mParentView, final Callback mCallback) {
        this.mActivePointerId = -1;
        this.mSetIdleRunnable = new Runnable() {
            @Override
            public void run() {
                ViewDragHelper.this.setDragState(0);
            }
        };
        if (mParentView == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (mCallback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.mParentView = mParentView;
        this.mCallback = mCallback;
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mEdgeSize = (int)(20.0f * context.getResources().getDisplayMetrics().density + 0.5f);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mMaxVelocity = value.getScaledMaximumFlingVelocity();
        this.mMinVelocity = value.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(context, ViewDragHelper.sInterpolator);
    }
    
    private boolean checkNewEdgeDrag(float abs, float abs2, final int n, final int n2) {
        abs = Math.abs(abs);
        abs2 = Math.abs(abs2);
        if ((this.mInitialEdgesTouched[n] & n2) == n2 && (this.mTrackingEdges & n2) != 0x0 && (this.mEdgeDragsLocked[n] & n2) != n2 && (this.mEdgeDragsInProgress[n] & n2) != n2 && (abs > this.mTouchSlop || abs2 > this.mTouchSlop)) {
            if (abs < 0.5f * abs2 && this.mCallback.onEdgeLock(n2)) {
                final int[] mEdgeDragsLocked = this.mEdgeDragsLocked;
                mEdgeDragsLocked[n] |= n2;
                return false;
            }
            if ((this.mEdgeDragsInProgress[n] & n2) == 0x0 && abs > this.mTouchSlop) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkTouchSlop(final View view, final float n, final float n2) {
        boolean b = true;
        if (view == null) {
            b = false;
        }
        else {
            boolean b2;
            if (this.mCallback.getViewHorizontalDragRange(view) > 0) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            boolean b3;
            if (this.mCallback.getViewVerticalDragRange(view) > 0) {
                b3 = true;
            }
            else {
                b3 = false;
            }
            if (b2 && b3) {
                if (n * n + n2 * n2 <= this.mTouchSlop * this.mTouchSlop) {
                    return false;
                }
            }
            else if (b2) {
                if (Math.abs(n) <= this.mTouchSlop) {
                    return false;
                }
            }
            else {
                if (!b3) {
                    return false;
                }
                if (Math.abs(n2) <= this.mTouchSlop) {
                    return false;
                }
            }
        }
        return b;
    }
    
    private float clampMag(final float n, float n2, final float n3) {
        final float abs = Math.abs(n);
        if (abs < n2) {
            n2 = 0.0f;
        }
        else {
            if (abs <= n3) {
                return n;
            }
            n2 = n3;
            if (n <= 0.0f) {
                return -n3;
            }
        }
        return n2;
    }
    
    private int clampMag(final int n, int n2, final int n3) {
        final int abs = Math.abs(n);
        if (abs < n2) {
            n2 = 0;
        }
        else {
            if (abs <= n3) {
                return n;
            }
            n2 = n3;
            if (n <= 0) {
                return -n3;
            }
        }
        return n2;
    }
    
    private void clearMotionHistory() {
        if (this.mInitialMotionX == null) {
            return;
        }
        Arrays.fill(this.mInitialMotionX, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }
    
    private void clearMotionHistory(final int n) {
        if (this.mInitialMotionX == null || !this.isPointerDown(n)) {
            return;
        }
        this.mInitialMotionX[n] = 0.0f;
        this.mInitialMotionY[n] = 0.0f;
        this.mLastMotionX[n] = 0.0f;
        this.mLastMotionY[n] = 0.0f;
        this.mInitialEdgesTouched[n] = 0;
        this.mEdgeDragsInProgress[n] = 0;
        this.mEdgeDragsLocked[n] = 0;
        this.mPointersDown &= ~(1 << n);
    }
    
    private int computeAxisDuration(int n, int abs, final int n2) {
        if (n == 0) {
            return 0;
        }
        final int width = this.mParentView.getWidth();
        final int n3 = width / 2;
        final float min = Math.min(1.0f, Math.abs(n) / width);
        final float n4 = n3;
        final float n5 = n3;
        final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
        abs = Math.abs(abs);
        if (abs > 0) {
            n = Math.round(1000.0f * Math.abs((n4 + n5 * distanceInfluenceForSnapDuration) / abs)) * 4;
        }
        else {
            n = (int)((Math.abs(n) / n2 + 1.0f) * 256.0f);
        }
        return Math.min(n, 600);
    }
    
    private int computeSettleDuration(final View view, int computeAxisDuration, int computeAxisDuration2, int clampMag, int clampMag2) {
        clampMag = this.clampMag(clampMag, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        clampMag2 = this.clampMag(clampMag2, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        final int abs = Math.abs(computeAxisDuration);
        final int abs2 = Math.abs(computeAxisDuration2);
        final int abs3 = Math.abs(clampMag);
        final int abs4 = Math.abs(clampMag2);
        final int n = abs3 + abs4;
        final int n2 = abs + abs2;
        float n3;
        if (clampMag != 0) {
            n3 = abs3 / n;
        }
        else {
            n3 = abs / n2;
        }
        float n4;
        if (clampMag2 != 0) {
            n4 = abs4 / n;
        }
        else {
            n4 = abs2 / n2;
        }
        computeAxisDuration = this.computeAxisDuration(computeAxisDuration, clampMag, this.mCallback.getViewHorizontalDragRange(view));
        computeAxisDuration2 = this.computeAxisDuration(computeAxisDuration2, clampMag2, this.mCallback.getViewVerticalDragRange(view));
        return (int)(computeAxisDuration * n3 + computeAxisDuration2 * n4);
    }
    
    public static ViewDragHelper create(final ViewGroup viewGroup, final float n, final Callback callback) {
        final ViewDragHelper create = create(viewGroup, callback);
        create.mTouchSlop *= (int)(1.0f / n);
        return create;
    }
    
    public static ViewDragHelper create(final ViewGroup viewGroup, final Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, callback);
    }
    
    private void dispatchViewReleased(final float n, final float n2) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, n, n2);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            this.setDragState(0);
        }
    }
    
    private float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((n - 0.5f) * 0.47123894f);
    }
    
    private void dragTo(final int n, final int n2, final int n3, final int n4) {
        int clampViewPositionHorizontal = n;
        int clampViewPositionVertical = n2;
        final int left = this.mCapturedView.getLeft();
        final int top = this.mCapturedView.getTop();
        if (n3 != 0) {
            clampViewPositionHorizontal = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, n, n3);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, clampViewPositionHorizontal - left);
        }
        if (n4 != 0) {
            clampViewPositionVertical = this.mCallback.clampViewPositionVertical(this.mCapturedView, n2, n4);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, clampViewPositionVertical - top);
        }
        if (n3 != 0 || n4 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, clampViewPositionHorizontal, clampViewPositionVertical, clampViewPositionHorizontal - left, clampViewPositionVertical - top);
        }
    }
    
    private void ensureMotionHistorySizeForId(final int n) {
        if (this.mInitialMotionX == null || this.mInitialMotionX.length <= n) {
            final float[] mInitialMotionX = new float[n + 1];
            final float[] mInitialMotionY = new float[n + 1];
            final float[] mLastMotionX = new float[n + 1];
            final float[] mLastMotionY = new float[n + 1];
            final int[] mInitialEdgesTouched = new int[n + 1];
            final int[] mEdgeDragsInProgress = new int[n + 1];
            final int[] mEdgeDragsLocked = new int[n + 1];
            if (this.mInitialMotionX != null) {
                System.arraycopy(this.mInitialMotionX, 0, mInitialMotionX, 0, this.mInitialMotionX.length);
                System.arraycopy(this.mInitialMotionY, 0, mInitialMotionY, 0, this.mInitialMotionY.length);
                System.arraycopy(this.mLastMotionX, 0, mLastMotionX, 0, this.mLastMotionX.length);
                System.arraycopy(this.mLastMotionY, 0, mLastMotionY, 0, this.mLastMotionY.length);
                System.arraycopy(this.mInitialEdgesTouched, 0, mInitialEdgesTouched, 0, this.mInitialEdgesTouched.length);
                System.arraycopy(this.mEdgeDragsInProgress, 0, mEdgeDragsInProgress, 0, this.mEdgeDragsInProgress.length);
                System.arraycopy(this.mEdgeDragsLocked, 0, mEdgeDragsLocked, 0, this.mEdgeDragsLocked.length);
            }
            this.mInitialMotionX = mInitialMotionX;
            this.mInitialMotionY = mInitialMotionY;
            this.mLastMotionX = mLastMotionX;
            this.mLastMotionY = mLastMotionY;
            this.mInitialEdgesTouched = mInitialEdgesTouched;
            this.mEdgeDragsInProgress = mEdgeDragsInProgress;
            this.mEdgeDragsLocked = mEdgeDragsLocked;
        }
    }
    
    private boolean forceSettleCapturedViewAt(int n, int n2, int computeSettleDuration, final int n3) {
        final int left = this.mCapturedView.getLeft();
        final int top = this.mCapturedView.getTop();
        n -= left;
        n2 -= top;
        if (n == 0 && n2 == 0) {
            this.mScroller.abortAnimation();
            this.setDragState(0);
            return false;
        }
        computeSettleDuration = this.computeSettleDuration(this.mCapturedView, n, n2, computeSettleDuration, n3);
        this.mScroller.startScroll(left, top, n, n2, computeSettleDuration);
        this.setDragState(2);
        return true;
    }
    
    private int getEdgesTouched(int n, final int n2) {
        boolean b = false;
        if (n < this.mParentView.getLeft() + this.mEdgeSize) {
            b = (false | true);
        }
        int n3 = b ? 1 : 0;
        if (n2 < this.mParentView.getTop() + this.mEdgeSize) {
            n3 = ((b ? 1 : 0) | 0x4);
        }
        int n4 = n3;
        if (n > this.mParentView.getRight() - this.mEdgeSize) {
            n4 = (n3 | 0x2);
        }
        n = n4;
        if (n2 > this.mParentView.getBottom() - this.mEdgeSize) {
            n = (n4 | 0x8);
        }
        return n;
    }
    
    private boolean isValidPointerForActionMove(final int n) {
        if (!this.isPointerDown(n)) {
            Log.e("ViewDragHelper", "Ignoring pointerId=" + n + " because ACTION_DOWN was not received " + "for this pointer before ACTION_MOVE. It likely happened because " + " ViewDragHelper did not receive all the events in the event stream.");
            return false;
        }
        return true;
    }
    
    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        this.dispatchViewReleased(this.clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), this.clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }
    
    private void reportNewEdgeDrags(final float n, final float n2, final int n3) {
        boolean b = false;
        if (this.checkNewEdgeDrag(n, n2, n3, 1)) {
            b = (false | true);
        }
        int n4 = b ? 1 : 0;
        if (this.checkNewEdgeDrag(n2, n, n3, 4)) {
            n4 = ((b ? 1 : 0) | 0x4);
        }
        int n5 = n4;
        if (this.checkNewEdgeDrag(n, n2, n3, 2)) {
            n5 = (n4 | 0x2);
        }
        int n6 = n5;
        if (this.checkNewEdgeDrag(n2, n, n3, 8)) {
            n6 = (n5 | 0x8);
        }
        if (n6 != 0) {
            final int[] mEdgeDragsInProgress = this.mEdgeDragsInProgress;
            mEdgeDragsInProgress[n3] |= n6;
            this.mCallback.onEdgeDragStarted(n6, n3);
        }
    }
    
    private void saveInitialMotion(final float n, final float n2, final int n3) {
        this.ensureMotionHistorySizeForId(n3);
        this.mInitialMotionX[n3] = (this.mLastMotionX[n3] = n);
        this.mInitialMotionY[n3] = (this.mLastMotionY[n3] = n2);
        this.mInitialEdgesTouched[n3] = this.getEdgesTouched((int)n, (int)n2);
        this.mPointersDown |= 1 << n3;
    }
    
    private void saveLastMotion(final MotionEvent motionEvent) {
        for (int pointerCount = motionEvent.getPointerCount(), i = 0; i < pointerCount; ++i) {
            final int pointerId = motionEvent.getPointerId(i);
            if (this.isValidPointerForActionMove(pointerId)) {
                final float x = motionEvent.getX(i);
                final float y = motionEvent.getY(i);
                this.mLastMotionX[pointerId] = x;
                this.mLastMotionY[pointerId] = y;
            }
        }
    }
    
    public void abort() {
        this.cancel();
        if (this.mDragState == 2) {
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            final int currX2 = this.mScroller.getCurrX();
            final int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        this.setDragState(0);
    }
    
    protected boolean canScroll(final View view, final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            final int scrollX = view.getScrollX();
            final int scrollY = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (n3 + scrollX >= child.getLeft() && n3 + scrollX < child.getRight() && n4 + scrollY >= child.getTop() && n4 + scrollY < child.getBottom() && this.canScroll(child, true, n, n2, n3 + scrollX - child.getLeft(), n4 + scrollY - child.getTop())) {
                    return true;
                }
            }
        }
        return b && (view.canScrollHorizontally(-n) || view.canScrollVertically(-n2));
    }
    
    public void cancel() {
        this.mActivePointerId = -1;
        this.clearMotionHistory();
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    public void captureChildView(final View mCapturedView, final int mActivePointerId) {
        if (mCapturedView.getParent() != this.mParentView) {
            throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
        }
        this.mCapturedView = mCapturedView;
        this.mActivePointerId = mActivePointerId;
        this.mCallback.onViewCaptured(mCapturedView, mActivePointerId);
        this.setDragState(1);
    }
    
    public boolean checkTouchSlop(final int n) {
        for (int length = this.mInitialMotionX.length, i = 0; i < length; ++i) {
            if (this.checkTouchSlop(n, i)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkTouchSlop(int n, final int n2) {
        boolean b = true;
        if (!this.isPointerDown(n2)) {
            b = false;
        }
        else {
            boolean b2;
            if ((n & 0x1) == 0x1) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            if ((n & 0x2) == 0x2) {
                n = 1;
            }
            else {
                n = 0;
            }
            final float n3 = this.mLastMotionX[n2] - this.mInitialMotionX[n2];
            final float n4 = this.mLastMotionY[n2] - this.mInitialMotionY[n2];
            if (b2 && n != 0) {
                if (n3 * n3 + n4 * n4 <= this.mTouchSlop * this.mTouchSlop) {
                    return false;
                }
            }
            else if (b2) {
                if (Math.abs(n3) <= this.mTouchSlop) {
                    return false;
                }
            }
            else {
                if (n == 0) {
                    return false;
                }
                if (Math.abs(n4) <= this.mTouchSlop) {
                    return false;
                }
            }
        }
        return b;
    }
    
    public boolean continueSettling(final boolean b) {
        if (this.mDragState == 2) {
            final boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            final int n = currX - this.mCapturedView.getLeft();
            final int n2 = currY - this.mCapturedView.getTop();
            if (n != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, n);
            }
            if (n2 != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, n2);
            }
            if (n != 0 || n2 != 0) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, n, n2);
            }
            boolean b2 = computeScrollOffset;
            if (computeScrollOffset) {
                b2 = computeScrollOffset;
                if (currX == this.mScroller.getFinalX()) {
                    b2 = computeScrollOffset;
                    if (currY == this.mScroller.getFinalY()) {
                        this.mScroller.abortAnimation();
                        b2 = false;
                    }
                }
            }
            if (!b2) {
                if (b) {
                    this.mParentView.post(this.mSetIdleRunnable);
                }
                else {
                    this.setDragState(0);
                }
            }
        }
        return this.mDragState == 2;
    }
    
    public View findTopChildUnder(final int n, final int n2) {
        for (int i = this.mParentView.getChildCount() - 1; i >= 0; --i) {
            final View child = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
            if (n >= child.getLeft() && n < child.getRight() && n2 >= child.getTop() && n2 < child.getBottom()) {
                return child;
            }
        }
        return null;
    }
    
    public void flingCapturedView(final int n, final int n2, final int n3, final int n4) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
        }
        this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), n, n3, n2, n4);
        this.setDragState(2);
    }
    
    public int getActivePointerId() {
        return this.mActivePointerId;
    }
    
    public View getCapturedView() {
        return this.mCapturedView;
    }
    
    public int getEdgeSize() {
        return this.mEdgeSize;
    }
    
    public float getMinVelocity() {
        return this.mMinVelocity;
    }
    
    public int getTouchSlop() {
        return this.mTouchSlop;
    }
    
    public int getViewDragState() {
        return this.mDragState;
    }
    
    public boolean isCapturedViewUnder(final int n, final int n2) {
        return this.isViewUnder(this.mCapturedView, n, n2);
    }
    
    public boolean isEdgeTouched(final int n) {
        for (int length = this.mInitialEdgesTouched.length, i = 0; i < length; ++i) {
            if (this.isEdgeTouched(n, i)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isEdgeTouched(final int n, final int n2) {
        return this.isPointerDown(n2) && (this.mInitialEdgesTouched[n2] & n) != 0x0;
    }
    
    public boolean isPointerDown(final int n) {
        return (this.mPointersDown & 1 << n) != 0x0;
    }
    
    public boolean isViewUnder(final View view, final int n, final int n2) {
        return view != null && n >= view.getLeft() && n < view.getRight() && n2 >= view.getTop() && n2 < view.getBottom();
    }
    
    public void processTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        final int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (actionMasked) {
            case 0: {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                final int pointerId = motionEvent.getPointerId(0);
                final View topChildUnder = this.findTopChildUnder((int)x, (int)y);
                this.saveInitialMotion(x, y, pointerId);
                this.tryCaptureViewForDrag(topChildUnder, pointerId);
                final int n = this.mInitialEdgesTouched[pointerId];
                if ((this.mTrackingEdges & n) != 0x0) {
                    this.mCallback.onEdgeTouched(this.mTrackingEdges & n, pointerId);
                    return;
                }
                break;
            }
            case 5: {
                final int pointerId2 = motionEvent.getPointerId(actionIndex);
                final float x2 = motionEvent.getX(actionIndex);
                final float y2 = motionEvent.getY(actionIndex);
                this.saveInitialMotion(x2, y2, pointerId2);
                if (this.mDragState == 0) {
                    this.tryCaptureViewForDrag(this.findTopChildUnder((int)x2, (int)y2), pointerId2);
                    final int n2 = this.mInitialEdgesTouched[pointerId2];
                    if ((this.mTrackingEdges & n2) != 0x0) {
                        this.mCallback.onEdgeTouched(this.mTrackingEdges & n2, pointerId2);
                        return;
                    }
                    break;
                }
                else {
                    if (this.isCapturedViewUnder((int)x2, (int)y2)) {
                        this.tryCaptureViewForDrag(this.mCapturedView, pointerId2);
                        return;
                    }
                    break;
                }
                break;
            }
            case 2: {
                if (this.mDragState != 1) {
                    for (int pointerCount = motionEvent.getPointerCount(), i = 0; i < pointerCount; ++i) {
                        final int pointerId3 = motionEvent.getPointerId(i);
                        if (this.isValidPointerForActionMove(pointerId3)) {
                            final float x3 = motionEvent.getX(i);
                            final float y3 = motionEvent.getY(i);
                            final float n3 = x3 - this.mInitialMotionX[pointerId3];
                            final float n4 = y3 - this.mInitialMotionY[pointerId3];
                            this.reportNewEdgeDrags(n3, n4, pointerId3);
                            if (this.mDragState == 1) {
                                break;
                            }
                            final View topChildUnder2 = this.findTopChildUnder((int)x3, (int)y3);
                            if (this.checkTouchSlop(topChildUnder2, n3, n4) && this.tryCaptureViewForDrag(topChildUnder2, pointerId3)) {
                                break;
                            }
                        }
                    }
                    this.saveLastMotion(motionEvent);
                    return;
                }
                if (this.isValidPointerForActionMove(this.mActivePointerId)) {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    final float x4 = motionEvent.getX(pointerIndex);
                    final float y4 = motionEvent.getY(pointerIndex);
                    final int n5 = (int)(x4 - this.mLastMotionX[this.mActivePointerId]);
                    final int n6 = (int)(y4 - this.mLastMotionY[this.mActivePointerId]);
                    this.dragTo(this.mCapturedView.getLeft() + n5, this.mCapturedView.getTop() + n6, n5, n6);
                    this.saveLastMotion(motionEvent);
                    return;
                }
                break;
            }
            case 6: {
                final int pointerId4 = motionEvent.getPointerId(actionIndex);
                if (this.mDragState == 1 && pointerId4 == this.mActivePointerId) {
                    final int n7 = -1;
                    final int pointerCount2 = motionEvent.getPointerCount();
                    int n8 = 0;
                    int mActivePointerId;
                    while (true) {
                        mActivePointerId = n7;
                        if (n8 >= pointerCount2) {
                            break;
                        }
                        final int pointerId5 = motionEvent.getPointerId(n8);
                        if (pointerId5 != this.mActivePointerId && this.findTopChildUnder((int)motionEvent.getX(n8), (int)motionEvent.getY(n8)) == this.mCapturedView && this.tryCaptureViewForDrag(this.mCapturedView, pointerId5)) {
                            mActivePointerId = this.mActivePointerId;
                            break;
                        }
                        ++n8;
                    }
                    if (mActivePointerId == -1) {
                        this.releaseViewForPointerUp();
                    }
                }
                this.clearMotionHistory(pointerId4);
            }
            case 1: {
                if (this.mDragState == 1) {
                    this.releaseViewForPointerUp();
                }
                this.cancel();
            }
            case 3: {
                if (this.mDragState == 1) {
                    this.dispatchViewReleased(0.0f, 0.0f);
                }
                this.cancel();
            }
        }
    }
    
    void setDragState(final int mDragState) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != mDragState) {
            this.mDragState = mDragState;
            this.mCallback.onViewDragStateChanged(mDragState);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }
    
    public void setEdgeTrackingEnabled(final int mTrackingEdges) {
        this.mTrackingEdges = mTrackingEdges;
    }
    
    public void setMinVelocity(final float mMinVelocity) {
        this.mMinVelocity = mMinVelocity;
    }
    
    public boolean settleCapturedViewAt(final int n, final int n2) {
        if (!this.mReleaseInProgress) {
            throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
        }
        return this.forceSettleCapturedViewAt(n, n2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId));
    }
    
    public boolean shouldInterceptTouchEvent(final MotionEvent motionEvent) {
        final int actionMasked = motionEvent.getActionMasked();
        final int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (actionMasked) {
            case 0: {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();
                final int pointerId = motionEvent.getPointerId(0);
                this.saveInitialMotion(x, y, pointerId);
                final View topChildUnder = this.findTopChildUnder((int)x, (int)y);
                if (topChildUnder == this.mCapturedView && this.mDragState == 2) {
                    this.tryCaptureViewForDrag(topChildUnder, pointerId);
                }
                final int n = this.mInitialEdgesTouched[pointerId];
                if ((this.mTrackingEdges & n) != 0x0) {
                    this.mCallback.onEdgeTouched(this.mTrackingEdges & n, pointerId);
                    break;
                }
                break;
            }
            case 5: {
                final int pointerId2 = motionEvent.getPointerId(actionIndex);
                final float x2 = motionEvent.getX(actionIndex);
                final float y2 = motionEvent.getY(actionIndex);
                this.saveInitialMotion(x2, y2, pointerId2);
                if (this.mDragState == 0) {
                    final int n2 = this.mInitialEdgesTouched[pointerId2];
                    if ((this.mTrackingEdges & n2) != 0x0) {
                        this.mCallback.onEdgeTouched(this.mTrackingEdges & n2, pointerId2);
                        break;
                    }
                    break;
                }
                else {
                    if (this.mDragState != 2) {
                        break;
                    }
                    final View topChildUnder2 = this.findTopChildUnder((int)x2, (int)y2);
                    if (topChildUnder2 == this.mCapturedView) {
                        this.tryCaptureViewForDrag(topChildUnder2, pointerId2);
                        break;
                    }
                    break;
                }
                break;
            }
            case 2: {
                if (this.mInitialMotionX != null && this.mInitialMotionY != null) {
                    for (int pointerCount = motionEvent.getPointerCount(), i = 0; i < pointerCount; ++i) {
                        final int pointerId3 = motionEvent.getPointerId(i);
                        if (this.isValidPointerForActionMove(pointerId3)) {
                            final float x3 = motionEvent.getX(i);
                            final float y3 = motionEvent.getY(i);
                            final float n3 = x3 - this.mInitialMotionX[pointerId3];
                            final float n4 = y3 - this.mInitialMotionY[pointerId3];
                            final View topChildUnder3 = this.findTopChildUnder((int)x3, (int)y3);
                            boolean b;
                            if (topChildUnder3 != null && this.checkTouchSlop(topChildUnder3, n3, n4)) {
                                b = true;
                            }
                            else {
                                b = false;
                            }
                            if (b) {
                                final int left = topChildUnder3.getLeft();
                                final int clampViewPositionHorizontal = this.mCallback.clampViewPositionHorizontal(topChildUnder3, left + (int)n3, (int)n3);
                                final int top = topChildUnder3.getTop();
                                final int clampViewPositionVertical = this.mCallback.clampViewPositionVertical(topChildUnder3, top + (int)n4, (int)n4);
                                final int viewHorizontalDragRange = this.mCallback.getViewHorizontalDragRange(topChildUnder3);
                                final int viewVerticalDragRange = this.mCallback.getViewVerticalDragRange(topChildUnder3);
                                if ((viewHorizontalDragRange == 0 || (viewHorizontalDragRange > 0 && clampViewPositionHorizontal == left)) && (viewVerticalDragRange == 0 || (viewVerticalDragRange > 0 && clampViewPositionVertical == top))) {
                                    break;
                                }
                            }
                            this.reportNewEdgeDrags(n3, n4, pointerId3);
                            if (this.mDragState == 1) {
                                break;
                            }
                            if (b && this.tryCaptureViewForDrag(topChildUnder3, pointerId3)) {
                                break;
                            }
                        }
                    }
                    this.saveLastMotion(motionEvent);
                    break;
                }
                break;
            }
            case 6: {
                this.clearMotionHistory(motionEvent.getPointerId(actionIndex));
                break;
            }
            case 1:
            case 3: {
                this.cancel();
                break;
            }
        }
        return this.mDragState == 1;
    }
    
    public boolean smoothSlideViewTo(final View mCapturedView, final int n, final int n2) {
        this.mCapturedView = mCapturedView;
        this.mActivePointerId = -1;
        final boolean forceSettleCapturedView = this.forceSettleCapturedViewAt(n, n2, 0, 0);
        if (!forceSettleCapturedView && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return forceSettleCapturedView;
    }
    
    boolean tryCaptureViewForDrag(final View view, final int mActivePointerId) {
        if (view == this.mCapturedView && this.mActivePointerId == mActivePointerId) {
            return true;
        }
        if (view != null && this.mCallback.tryCaptureView(view, mActivePointerId)) {
            this.captureChildView(view, this.mActivePointerId = mActivePointerId);
            return true;
        }
        return false;
    }
    
    public abstract static class Callback
    {
        public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
            return 0;
        }
        
        public int clampViewPositionVertical(final View view, final int n, final int n2) {
            return 0;
        }
        
        public int getOrderedChildIndex(final int n) {
            return n;
        }
        
        public int getViewHorizontalDragRange(final View view) {
            return 0;
        }
        
        public int getViewVerticalDragRange(final View view) {
            return 0;
        }
        
        public void onEdgeDragStarted(final int n, final int n2) {
        }
        
        public boolean onEdgeLock(final int n) {
            return false;
        }
        
        public void onEdgeTouched(final int n, final int n2) {
        }
        
        public void onViewCaptured(final View view, final int n) {
        }
        
        public void onViewDragStateChanged(final int n) {
        }
        
        public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
        }
        
        public void onViewReleased(final View view, final float n, final float n2) {
        }
        
        public abstract boolean tryCaptureView(final View p0, final int p1);
    }
}
