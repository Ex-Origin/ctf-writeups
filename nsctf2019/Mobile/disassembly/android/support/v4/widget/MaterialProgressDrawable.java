// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.support.annotation.NonNull;
import android.graphics.Path$FillType;
import android.graphics.Paint$Style;
import android.graphics.Paint$Cap;
import android.graphics.RectF;
import android.graphics.Paint;
import android.graphics.Path;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.Transformation;
import android.content.Context;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.animation.LinearInterpolator;
import android.content.res.Resources;
import android.view.View;
import android.graphics.drawable.Drawable$Callback;
import java.util.ArrayList;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

class MaterialProgressDrawable extends Drawable implements Animatable
{
    private static final int ANIMATION_DURATION = 1332;
    private static final int ARROW_HEIGHT = 5;
    private static final int ARROW_HEIGHT_LARGE = 6;
    private static final float ARROW_OFFSET_ANGLE = 5.0f;
    private static final int ARROW_WIDTH = 10;
    private static final int ARROW_WIDTH_LARGE = 12;
    private static final float CENTER_RADIUS = 8.75f;
    private static final float CENTER_RADIUS_LARGE = 12.5f;
    private static final int CIRCLE_DIAMETER = 40;
    private static final int CIRCLE_DIAMETER_LARGE = 56;
    private static final int[] COLORS;
    private static final float COLOR_START_DELAY_OFFSET = 0.75f;
    static final int DEFAULT = 1;
    private static final float END_TRIM_START_DELAY_OFFSET = 0.5f;
    private static final float FULL_ROTATION = 1080.0f;
    static final int LARGE = 0;
    private static final Interpolator LINEAR_INTERPOLATOR;
    static final Interpolator MATERIAL_INTERPOLATOR;
    private static final float MAX_PROGRESS_ARC = 0.8f;
    private static final float NUM_POINTS = 5.0f;
    private static final float START_TRIM_DURATION_OFFSET = 0.5f;
    private static final float STROKE_WIDTH = 2.5f;
    private static final float STROKE_WIDTH_LARGE = 3.0f;
    private Animation mAnimation;
    private final ArrayList<Animation> mAnimators;
    private final Drawable$Callback mCallback;
    boolean mFinishing;
    private double mHeight;
    private View mParent;
    private Resources mResources;
    private final Ring mRing;
    private float mRotation;
    float mRotationCount;
    private double mWidth;
    
    static {
        LINEAR_INTERPOLATOR = (Interpolator)new LinearInterpolator();
        MATERIAL_INTERPOLATOR = (Interpolator)new FastOutSlowInInterpolator();
        COLORS = new int[] { -16777216 };
    }
    
    MaterialProgressDrawable(final Context context, final View mParent) {
        this.mAnimators = new ArrayList<Animation>();
        this.mCallback = (Drawable$Callback)new Drawable$Callback() {
            public void invalidateDrawable(final Drawable drawable) {
                MaterialProgressDrawable.this.invalidateSelf();
            }
            
            public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
                MaterialProgressDrawable.this.scheduleSelf(runnable, n);
            }
            
            public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
                MaterialProgressDrawable.this.unscheduleSelf(runnable);
            }
        };
        this.mParent = mParent;
        this.mResources = context.getResources();
        (this.mRing = new Ring(this.mCallback)).setColors(MaterialProgressDrawable.COLORS);
        this.updateSizes(1);
        this.setupAnimators();
    }
    
    private int evaluateColorChange(final float n, int n2, int intValue) {
        final int intValue2 = Integer.valueOf(n2);
        n2 = (intValue2 >> 24 & 0xFF);
        final int n3 = intValue2 >> 16 & 0xFF;
        final int n4 = intValue2 >> 8 & 0xFF;
        final int n5 = intValue2 & 0xFF;
        intValue = Integer.valueOf(intValue);
        return (int)(((intValue >> 24 & 0xFF) - n2) * n) + n2 << 24 | (int)(((intValue >> 16 & 0xFF) - n3) * n) + n3 << 16 | (int)(((intValue >> 8 & 0xFF) - n4) * n) + n4 << 8 | (int)(((intValue & 0xFF) - n5) * n) + n5;
    }
    
    private float getRotation() {
        return this.mRotation;
    }
    
    private void setSizeParameters(final double n, final double n2, final double n3, final double n4, final float n5, final float n6) {
        final Ring mRing = this.mRing;
        final float density = this.mResources.getDisplayMetrics().density;
        this.mWidth = density * n;
        this.mHeight = density * n2;
        mRing.setStrokeWidth((float)n4 * density);
        mRing.setCenterRadius(density * n3);
        mRing.setColorIndex(0);
        mRing.setArrowDimensions(n5 * density, n6 * density);
        mRing.setInsets((int)this.mWidth, (int)this.mHeight);
    }
    
    private void setupAnimators() {
        final Ring mRing = this.mRing;
        final Animation mAnimation = new Animation() {
            public void applyTransformation(final float n, final Transformation transformation) {
                if (MaterialProgressDrawable.this.mFinishing) {
                    MaterialProgressDrawable.this.applyFinishTranslation(n, mRing);
                    return;
                }
                final float minProgressArc = MaterialProgressDrawable.this.getMinProgressArc(mRing);
                final float startingEndTrim = mRing.getStartingEndTrim();
                final float startingStartTrim = mRing.getStartingStartTrim();
                final float startingRotation = mRing.getStartingRotation();
                MaterialProgressDrawable.this.updateRingColor(n, mRing);
                if (n <= 0.5f) {
                    mRing.setStartTrim(startingStartTrim + (0.8f - minProgressArc) * MaterialProgressDrawable.MATERIAL_INTERPOLATOR.getInterpolation(n / 0.5f));
                }
                if (n > 0.5f) {
                    mRing.setEndTrim(startingEndTrim + MaterialProgressDrawable.MATERIAL_INTERPOLATOR.getInterpolation((n - 0.5f) / 0.5f) * (0.8f - minProgressArc));
                }
                mRing.setRotation(startingRotation + 0.25f * n);
                MaterialProgressDrawable.this.setRotation(216.0f * n + 1080.0f * (MaterialProgressDrawable.this.mRotationCount / 5.0f));
            }
        };
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(1);
        mAnimation.setInterpolator(MaterialProgressDrawable.LINEAR_INTERPOLATOR);
        mAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
            }
            
            public void onAnimationRepeat(final Animation animation) {
                mRing.storeOriginals();
                mRing.goToNextColor();
                mRing.setStartTrim(mRing.getEndTrim());
                if (MaterialProgressDrawable.this.mFinishing) {
                    MaterialProgressDrawable.this.mFinishing = false;
                    animation.setDuration(1332L);
                    mRing.setShowArrow(false);
                    return;
                }
                MaterialProgressDrawable.this.mRotationCount = (MaterialProgressDrawable.this.mRotationCount + 1.0f) % 5.0f;
            }
            
            public void onAnimationStart(final Animation animation) {
                MaterialProgressDrawable.this.mRotationCount = 0.0f;
            }
        });
        this.mAnimation = mAnimation;
    }
    
    void applyFinishTranslation(final float n, final Ring ring) {
        this.updateRingColor(n, ring);
        final float n2 = (float)(Math.floor(ring.getStartingRotation() / 0.8f) + 1.0);
        ring.setStartTrim(ring.getStartingStartTrim() + (ring.getStartingEndTrim() - this.getMinProgressArc(ring) - ring.getStartingStartTrim()) * n);
        ring.setEndTrim(ring.getStartingEndTrim());
        ring.setRotation(ring.getStartingRotation() + (n2 - ring.getStartingRotation()) * n);
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final int save = canvas.save();
        canvas.rotate(this.mRotation, bounds.exactCenterX(), bounds.exactCenterY());
        this.mRing.draw(canvas, bounds);
        canvas.restoreToCount(save);
    }
    
    public int getAlpha() {
        return this.mRing.getAlpha();
    }
    
    public int getIntrinsicHeight() {
        return (int)this.mHeight;
    }
    
    public int getIntrinsicWidth() {
        return (int)this.mWidth;
    }
    
    float getMinProgressArc(final Ring ring) {
        return (float)Math.toRadians(ring.getStrokeWidth() / (6.283185307179586 * ring.getCenterRadius()));
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public boolean isRunning() {
        final ArrayList<Animation> mAnimators = this.mAnimators;
        for (int size = mAnimators.size(), i = 0; i < size; ++i) {
            final Animation animation = mAnimators.get(i);
            if (animation.hasStarted() && !animation.hasEnded()) {
                return true;
            }
        }
        return false;
    }
    
    public void setAlpha(final int alpha) {
        this.mRing.setAlpha(alpha);
    }
    
    public void setArrowScale(final float arrowScale) {
        this.mRing.setArrowScale(arrowScale);
    }
    
    public void setBackgroundColor(final int backgroundColor) {
        this.mRing.setBackgroundColor(backgroundColor);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mRing.setColorFilter(colorFilter);
    }
    
    public void setColorSchemeColors(final int... colors) {
        this.mRing.setColors(colors);
        this.mRing.setColorIndex(0);
    }
    
    public void setProgressRotation(final float rotation) {
        this.mRing.setRotation(rotation);
    }
    
    void setRotation(final float mRotation) {
        this.mRotation = mRotation;
        this.invalidateSelf();
    }
    
    public void setStartEndTrim(final float startTrim, final float endTrim) {
        this.mRing.setStartTrim(startTrim);
        this.mRing.setEndTrim(endTrim);
    }
    
    public void showArrow(final boolean showArrow) {
        this.mRing.setShowArrow(showArrow);
    }
    
    public void start() {
        this.mAnimation.reset();
        this.mRing.storeOriginals();
        if (this.mRing.getEndTrim() != this.mRing.getStartTrim()) {
            this.mFinishing = true;
            this.mAnimation.setDuration(666L);
            this.mParent.startAnimation(this.mAnimation);
            return;
        }
        this.mRing.setColorIndex(0);
        this.mRing.resetOriginals();
        this.mAnimation.setDuration(1332L);
        this.mParent.startAnimation(this.mAnimation);
    }
    
    public void stop() {
        this.mParent.clearAnimation();
        this.setRotation(0.0f);
        this.mRing.setShowArrow(false);
        this.mRing.setColorIndex(0);
        this.mRing.resetOriginals();
    }
    
    void updateRingColor(final float n, final Ring ring) {
        if (n > 0.75f) {
            ring.setColor(this.evaluateColorChange((n - 0.75f) / 0.25f, ring.getStartingColor(), ring.getNextColor()));
        }
    }
    
    public void updateSizes(final int n) {
        if (n == 0) {
            this.setSizeParameters(56.0, 56.0, 12.5, 3.0, 12.0f, 6.0f);
            return;
        }
        this.setSizeParameters(40.0, 40.0, 8.75, 2.5, 10.0f, 5.0f);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ProgressDrawableSize {
    }
    
    private static class Ring
    {
        private int mAlpha;
        private Path mArrow;
        private int mArrowHeight;
        private final Paint mArrowPaint;
        private float mArrowScale;
        private int mArrowWidth;
        private int mBackgroundColor;
        private final Drawable$Callback mCallback;
        private final Paint mCirclePaint;
        private int mColorIndex;
        private int[] mColors;
        private int mCurrentColor;
        private float mEndTrim;
        private final Paint mPaint;
        private double mRingCenterRadius;
        private float mRotation;
        private boolean mShowArrow;
        private float mStartTrim;
        private float mStartingEndTrim;
        private float mStartingRotation;
        private float mStartingStartTrim;
        private float mStrokeInset;
        private float mStrokeWidth;
        private final RectF mTempBounds;
        
        Ring(final Drawable$Callback mCallback) {
            this.mTempBounds = new RectF();
            this.mPaint = new Paint();
            this.mArrowPaint = new Paint();
            this.mStartTrim = 0.0f;
            this.mEndTrim = 0.0f;
            this.mRotation = 0.0f;
            this.mStrokeWidth = 5.0f;
            this.mStrokeInset = 2.5f;
            this.mCirclePaint = new Paint(1);
            this.mCallback = mCallback;
            this.mPaint.setStrokeCap(Paint$Cap.SQUARE);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint$Style.STROKE);
            this.mArrowPaint.setStyle(Paint$Style.FILL);
            this.mArrowPaint.setAntiAlias(true);
        }
        
        private void drawTriangle(final Canvas canvas, final float n, final float n2, final Rect rect) {
            if (this.mShowArrow) {
                if (this.mArrow == null) {
                    (this.mArrow = new Path()).setFillType(Path$FillType.EVEN_ODD);
                }
                else {
                    this.mArrow.reset();
                }
                final float n3 = (int)this.mStrokeInset / 2;
                final float mArrowScale = this.mArrowScale;
                final float n4 = (float)(this.mRingCenterRadius * Math.cos(0.0) + rect.exactCenterX());
                final float n5 = (float)(this.mRingCenterRadius * Math.sin(0.0) + rect.exactCenterY());
                this.mArrow.moveTo(0.0f, 0.0f);
                this.mArrow.lineTo(this.mArrowWidth * this.mArrowScale, 0.0f);
                this.mArrow.lineTo(this.mArrowWidth * this.mArrowScale / 2.0f, this.mArrowHeight * this.mArrowScale);
                this.mArrow.offset(n4 - n3 * mArrowScale, n5);
                this.mArrow.close();
                this.mArrowPaint.setColor(this.mCurrentColor);
                canvas.rotate(n + n2 - 5.0f, rect.exactCenterX(), rect.exactCenterY());
                canvas.drawPath(this.mArrow, this.mArrowPaint);
            }
        }
        
        private int getNextColorIndex() {
            return (this.mColorIndex + 1) % this.mColors.length;
        }
        
        private void invalidateSelf() {
            this.mCallback.invalidateDrawable((Drawable)null);
        }
        
        public void draw(final Canvas canvas, final Rect rect) {
            final RectF mTempBounds = this.mTempBounds;
            mTempBounds.set(rect);
            mTempBounds.inset(this.mStrokeInset, this.mStrokeInset);
            final float n = (this.mStartTrim + this.mRotation) * 360.0f;
            final float n2 = (this.mEndTrim + this.mRotation) * 360.0f - n;
            this.mPaint.setColor(this.mCurrentColor);
            canvas.drawArc(mTempBounds, n, n2, false, this.mPaint);
            this.drawTriangle(canvas, n, n2, rect);
            if (this.mAlpha < 255) {
                this.mCirclePaint.setColor(this.mBackgroundColor);
                this.mCirclePaint.setAlpha(255 - this.mAlpha);
                canvas.drawCircle(rect.exactCenterX(), rect.exactCenterY(), (float)(rect.width() / 2), this.mCirclePaint);
            }
        }
        
        public int getAlpha() {
            return this.mAlpha;
        }
        
        public double getCenterRadius() {
            return this.mRingCenterRadius;
        }
        
        public float getEndTrim() {
            return this.mEndTrim;
        }
        
        public float getInsets() {
            return this.mStrokeInset;
        }
        
        public int getNextColor() {
            return this.mColors[this.getNextColorIndex()];
        }
        
        public float getRotation() {
            return this.mRotation;
        }
        
        public float getStartTrim() {
            return this.mStartTrim;
        }
        
        public int getStartingColor() {
            return this.mColors[this.mColorIndex];
        }
        
        public float getStartingEndTrim() {
            return this.mStartingEndTrim;
        }
        
        public float getStartingRotation() {
            return this.mStartingRotation;
        }
        
        public float getStartingStartTrim() {
            return this.mStartingStartTrim;
        }
        
        public float getStrokeWidth() {
            return this.mStrokeWidth;
        }
        
        public void goToNextColor() {
            this.setColorIndex(this.getNextColorIndex());
        }
        
        public void resetOriginals() {
            this.mStartingStartTrim = 0.0f;
            this.mStartingEndTrim = 0.0f;
            this.setStartTrim(this.mStartingRotation = 0.0f);
            this.setEndTrim(0.0f);
            this.setRotation(0.0f);
        }
        
        public void setAlpha(final int mAlpha) {
            this.mAlpha = mAlpha;
        }
        
        public void setArrowDimensions(final float n, final float n2) {
            this.mArrowWidth = (int)n;
            this.mArrowHeight = (int)n2;
        }
        
        public void setArrowScale(final float mArrowScale) {
            if (mArrowScale != this.mArrowScale) {
                this.mArrowScale = mArrowScale;
                this.invalidateSelf();
            }
        }
        
        public void setBackgroundColor(final int mBackgroundColor) {
            this.mBackgroundColor = mBackgroundColor;
        }
        
        public void setCenterRadius(final double mRingCenterRadius) {
            this.mRingCenterRadius = mRingCenterRadius;
        }
        
        public void setColor(final int mCurrentColor) {
            this.mCurrentColor = mCurrentColor;
        }
        
        public void setColorFilter(final ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
            this.invalidateSelf();
        }
        
        public void setColorIndex(final int mColorIndex) {
            this.mColorIndex = mColorIndex;
            this.mCurrentColor = this.mColors[this.mColorIndex];
        }
        
        public void setColors(@NonNull final int[] mColors) {
            this.mColors = mColors;
            this.setColorIndex(0);
        }
        
        public void setEndTrim(final float mEndTrim) {
            this.mEndTrim = mEndTrim;
            this.invalidateSelf();
        }
        
        public void setInsets(final int n, final int n2) {
            final float n3 = Math.min(n, n2);
            float mStrokeInset;
            if (this.mRingCenterRadius <= 0.0 || n3 < 0.0f) {
                mStrokeInset = (float)Math.ceil(this.mStrokeWidth / 2.0f);
            }
            else {
                mStrokeInset = (float)(n3 / 2.0f - this.mRingCenterRadius);
            }
            this.mStrokeInset = mStrokeInset;
        }
        
        public void setRotation(final float mRotation) {
            this.mRotation = mRotation;
            this.invalidateSelf();
        }
        
        public void setShowArrow(final boolean mShowArrow) {
            if (this.mShowArrow != mShowArrow) {
                this.mShowArrow = mShowArrow;
                this.invalidateSelf();
            }
        }
        
        public void setStartTrim(final float mStartTrim) {
            this.mStartTrim = mStartTrim;
            this.invalidateSelf();
        }
        
        public void setStrokeWidth(final float n) {
            this.mStrokeWidth = n;
            this.mPaint.setStrokeWidth(n);
            this.invalidateSelf();
        }
        
        public void storeOriginals() {
            this.mStartingStartTrim = this.mStartTrim;
            this.mStartingEndTrim = this.mEndTrim;
            this.mStartingRotation = this.mRotation;
        }
    }
}
