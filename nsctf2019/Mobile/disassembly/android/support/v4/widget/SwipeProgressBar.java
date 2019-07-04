// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.support.v4.view.ViewCompat;
import android.view.animation.AnimationUtils;
import android.graphics.Canvas;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.view.animation.Interpolator;

final class SwipeProgressBar
{
    private static final int ANIMATION_DURATION_MS = 2000;
    private static final int COLOR1 = -1291845632;
    private static final int COLOR2 = Integer.MIN_VALUE;
    private static final int COLOR3 = 1291845632;
    private static final int COLOR4 = 436207616;
    private static final int FINISH_ANIMATION_DURATION_MS = 1000;
    private static final Interpolator INTERPOLATOR;
    private Rect mBounds;
    private final RectF mClipRect;
    private int mColor1;
    private int mColor2;
    private int mColor3;
    private int mColor4;
    private long mFinishTime;
    private final Paint mPaint;
    private View mParent;
    private boolean mRunning;
    private long mStartTime;
    private float mTriggerPercentage;
    
    static {
        INTERPOLATOR = (Interpolator)new FastOutSlowInInterpolator();
    }
    
    SwipeProgressBar(final View mParent) {
        this.mPaint = new Paint();
        this.mClipRect = new RectF();
        this.mBounds = new Rect();
        this.mParent = mParent;
        this.mColor1 = -1291845632;
        this.mColor2 = Integer.MIN_VALUE;
        this.mColor3 = 1291845632;
        this.mColor4 = 436207616;
    }
    
    private void drawCircle(final Canvas canvas, final float n, float interpolation, final int color, final float n2) {
        this.mPaint.setColor(color);
        canvas.save();
        canvas.translate(n, interpolation);
        interpolation = SwipeProgressBar.INTERPOLATOR.getInterpolation(n2);
        canvas.scale(interpolation, interpolation);
        canvas.drawCircle(0.0f, 0.0f, n, this.mPaint);
        canvas.restore();
    }
    
    private void drawTrigger(final Canvas canvas, final int n, final int n2) {
        this.mPaint.setColor(this.mColor1);
        canvas.drawCircle((float)n, (float)n2, n * this.mTriggerPercentage, this.mPaint);
    }
    
    void draw(final Canvas canvas) {
        final int width = this.mBounds.width();
        final int height = this.mBounds.height();
        final int n = width / 2;
        final int n2 = height / 2;
        boolean b = false;
        final int save = canvas.save();
        canvas.clipRect(this.mBounds);
        int save2;
        if (this.mRunning || this.mFinishTime > 0L) {
            final long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
            final long mStartTime = this.mStartTime;
            final long n3 = (currentAnimationTimeMillis - this.mStartTime) / 2000L;
            final float n4 = (currentAnimationTimeMillis - mStartTime) % 2000L / 20.0f;
            if (!this.mRunning) {
                if (currentAnimationTimeMillis - this.mFinishTime >= 1000L) {
                    this.mFinishTime = 0L;
                    return;
                }
                final float n5 = width / 2 * SwipeProgressBar.INTERPOLATOR.getInterpolation((currentAnimationTimeMillis - this.mFinishTime) % 1000L / 10.0f / 100.0f);
                this.mClipRect.set(n - n5, 0.0f, n + n5, (float)height);
                canvas.saveLayerAlpha(this.mClipRect, 0, 0);
                b = true;
            }
            if (n3 == 0L) {
                canvas.drawColor(this.mColor1);
            }
            else if (n4 >= 0.0f && n4 < 25.0f) {
                canvas.drawColor(this.mColor4);
            }
            else if (n4 >= 25.0f && n4 < 50.0f) {
                canvas.drawColor(this.mColor1);
            }
            else if (n4 >= 50.0f && n4 < 75.0f) {
                canvas.drawColor(this.mColor2);
            }
            else {
                canvas.drawColor(this.mColor3);
            }
            if (n4 >= 0.0f && n4 <= 25.0f) {
                this.drawCircle(canvas, n, n2, this.mColor1, (25.0f + n4) * 2.0f / 100.0f);
            }
            if (n4 >= 0.0f && n4 <= 50.0f) {
                this.drawCircle(canvas, n, n2, this.mColor2, 2.0f * n4 / 100.0f);
            }
            if (n4 >= 25.0f && n4 <= 75.0f) {
                this.drawCircle(canvas, n, n2, this.mColor3, (n4 - 25.0f) * 2.0f / 100.0f);
            }
            if (n4 >= 50.0f && n4 <= 100.0f) {
                this.drawCircle(canvas, n, n2, this.mColor4, (n4 - 50.0f) * 2.0f / 100.0f);
            }
            if (n4 >= 75.0f && n4 <= 100.0f) {
                this.drawCircle(canvas, n, n2, this.mColor1, (n4 - 75.0f) * 2.0f / 100.0f);
            }
            save2 = save;
            if (this.mTriggerPercentage > 0.0f) {
                save2 = save;
                if (b) {
                    canvas.restoreToCount(save);
                    save2 = canvas.save();
                    canvas.clipRect(this.mBounds);
                    this.drawTrigger(canvas, n, n2);
                }
            }
            ViewCompat.postInvalidateOnAnimation(this.mParent, this.mBounds.left, this.mBounds.top, this.mBounds.right, this.mBounds.bottom);
        }
        else {
            save2 = save;
            if (this.mTriggerPercentage > 0.0f) {
                save2 = save;
                if (this.mTriggerPercentage <= 1.0) {
                    this.drawTrigger(canvas, n, n2);
                    save2 = save;
                }
            }
        }
        canvas.restoreToCount(save2);
    }
    
    boolean isRunning() {
        return this.mRunning || this.mFinishTime > 0L;
    }
    
    void setBounds(final int left, final int top, final int right, final int bottom) {
        this.mBounds.left = left;
        this.mBounds.top = top;
        this.mBounds.right = right;
        this.mBounds.bottom = bottom;
    }
    
    void setColorScheme(final int mColor1, final int mColor2, final int mColor3, final int mColor4) {
        this.mColor1 = mColor1;
        this.mColor2 = mColor2;
        this.mColor3 = mColor3;
        this.mColor4 = mColor4;
    }
    
    void setTriggerPercentage(final float mTriggerPercentage) {
        this.mTriggerPercentage = mTriggerPercentage;
        this.mStartTime = 0L;
        ViewCompat.postInvalidateOnAnimation(this.mParent, this.mBounds.left, this.mBounds.top, this.mBounds.right, this.mBounds.bottom);
    }
    
    void start() {
        if (!this.mRunning) {
            this.mTriggerPercentage = 0.0f;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mRunning = true;
            this.mParent.postInvalidate();
        }
    }
    
    void stop() {
        if (this.mRunning) {
            this.mTriggerPercentage = 0.0f;
            this.mFinishTime = AnimationUtils.currentAnimationTimeMillis();
            this.mRunning = false;
            this.mParent.postInvalidate();
        }
    }
}
