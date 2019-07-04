// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.drawable;

import android.util.DisplayMetrics;
import android.graphics.Shader;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.Shader$TileMode;
import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.BitmapShader;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.graphics.drawable.Drawable;

@RequiresApi(9)
public abstract class RoundedBitmapDrawable extends Drawable
{
    private static final int DEFAULT_PAINT_FLAGS = 3;
    private boolean mApplyGravity;
    final Bitmap mBitmap;
    private int mBitmapHeight;
    private final BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private float mCornerRadius;
    final Rect mDstRect;
    private final RectF mDstRectF;
    private int mGravity;
    private boolean mIsCircular;
    private final Paint mPaint;
    private final Matrix mShaderMatrix;
    private int mTargetDensity;
    
    RoundedBitmapDrawable(final Resources resources, final Bitmap mBitmap) {
        this.mTargetDensity = 160;
        this.mGravity = 119;
        this.mPaint = new Paint(3);
        this.mShaderMatrix = new Matrix();
        this.mDstRect = new Rect();
        this.mDstRectF = new RectF();
        this.mApplyGravity = true;
        if (resources != null) {
            this.mTargetDensity = resources.getDisplayMetrics().densityDpi;
        }
        this.mBitmap = mBitmap;
        if (this.mBitmap != null) {
            this.computeBitmapSize();
            this.mBitmapShader = new BitmapShader(this.mBitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
            return;
        }
        this.mBitmapHeight = -1;
        this.mBitmapWidth = -1;
        this.mBitmapShader = null;
    }
    
    private void computeBitmapSize() {
        this.mBitmapWidth = this.mBitmap.getScaledWidth(this.mTargetDensity);
        this.mBitmapHeight = this.mBitmap.getScaledHeight(this.mTargetDensity);
    }
    
    private static boolean isGreaterThanZero(final float n) {
        return n > 0.05f;
    }
    
    private void updateCircularCornerRadius() {
        this.mCornerRadius = Math.min(this.mBitmapHeight, this.mBitmapWidth) / 2;
    }
    
    public void draw(final Canvas canvas) {
        final Bitmap mBitmap = this.mBitmap;
        if (mBitmap == null) {
            return;
        }
        this.updateDstRect();
        if (this.mPaint.getShader() == null) {
            canvas.drawBitmap(mBitmap, (Rect)null, this.mDstRect, this.mPaint);
            return;
        }
        canvas.drawRoundRect(this.mDstRectF, this.mCornerRadius, this.mCornerRadius, this.mPaint);
    }
    
    public int getAlpha() {
        return this.mPaint.getAlpha();
    }
    
    public final Bitmap getBitmap() {
        return this.mBitmap;
    }
    
    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }
    
    public float getCornerRadius() {
        return this.mCornerRadius;
    }
    
    public int getGravity() {
        return this.mGravity;
    }
    
    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }
    
    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }
    
    public int getOpacity() {
        if (this.mGravity == 119 && !this.mIsCircular) {
            final Bitmap mBitmap = this.mBitmap;
            if (mBitmap != null && !mBitmap.hasAlpha() && this.mPaint.getAlpha() >= 255 && !isGreaterThanZero(this.mCornerRadius)) {
                return -1;
            }
        }
        return -3;
    }
    
    public final Paint getPaint() {
        return this.mPaint;
    }
    
    void gravityCompatApply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2) {
        throw new UnsupportedOperationException();
    }
    
    public boolean hasAntiAlias() {
        return this.mPaint.isAntiAlias();
    }
    
    public boolean hasMipMap() {
        throw new UnsupportedOperationException();
    }
    
    public boolean isCircular() {
        return this.mIsCircular;
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        if (this.mIsCircular) {
            this.updateCircularCornerRadius();
        }
        this.mApplyGravity = true;
    }
    
    public void setAlpha(final int alpha) {
        if (alpha != this.mPaint.getAlpha()) {
            this.mPaint.setAlpha(alpha);
            this.invalidateSelf();
        }
    }
    
    public void setAntiAlias(final boolean antiAlias) {
        this.mPaint.setAntiAlias(antiAlias);
        this.invalidateSelf();
    }
    
    public void setCircular(final boolean mIsCircular) {
        this.mIsCircular = mIsCircular;
        this.mApplyGravity = true;
        if (mIsCircular) {
            this.updateCircularCornerRadius();
            this.mPaint.setShader((Shader)this.mBitmapShader);
            this.invalidateSelf();
            return;
        }
        this.setCornerRadius(0.0f);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }
    
    public void setCornerRadius(final float mCornerRadius) {
        if (this.mCornerRadius == mCornerRadius) {
            return;
        }
        this.mIsCircular = false;
        if (isGreaterThanZero(mCornerRadius)) {
            this.mPaint.setShader((Shader)this.mBitmapShader);
        }
        else {
            this.mPaint.setShader((Shader)null);
        }
        this.mCornerRadius = mCornerRadius;
        this.invalidateSelf();
    }
    
    public void setDither(final boolean dither) {
        this.mPaint.setDither(dither);
        this.invalidateSelf();
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mPaint.setFilterBitmap(filterBitmap);
        this.invalidateSelf();
    }
    
    public void setGravity(final int mGravity) {
        if (this.mGravity != mGravity) {
            this.mGravity = mGravity;
            this.mApplyGravity = true;
            this.invalidateSelf();
        }
    }
    
    public void setMipMap(final boolean b) {
        throw new UnsupportedOperationException();
    }
    
    public void setTargetDensity(final int n) {
        if (this.mTargetDensity != n) {
            int mTargetDensity;
            if ((mTargetDensity = n) == 0) {
                mTargetDensity = 160;
            }
            this.mTargetDensity = mTargetDensity;
            if (this.mBitmap != null) {
                this.computeBitmapSize();
            }
            this.invalidateSelf();
        }
    }
    
    public void setTargetDensity(final Canvas canvas) {
        this.setTargetDensity(canvas.getDensity());
    }
    
    public void setTargetDensity(final DisplayMetrics displayMetrics) {
        this.setTargetDensity(displayMetrics.densityDpi);
    }
    
    void updateDstRect() {
        if (this.mApplyGravity) {
            if (this.mIsCircular) {
                final int min = Math.min(this.mBitmapWidth, this.mBitmapHeight);
                this.gravityCompatApply(this.mGravity, min, min, this.getBounds(), this.mDstRect);
                final int min2 = Math.min(this.mDstRect.width(), this.mDstRect.height());
                this.mDstRect.inset(Math.max(0, (this.mDstRect.width() - min2) / 2), Math.max(0, (this.mDstRect.height() - min2) / 2));
                this.mCornerRadius = 0.5f * min2;
            }
            else {
                this.gravityCompatApply(this.mGravity, this.mBitmapWidth, this.mBitmapHeight, this.getBounds(), this.mDstRect);
            }
            this.mDstRectF.set(this.mDstRect);
            if (this.mBitmapShader != null) {
                this.mShaderMatrix.setTranslate(this.mDstRectF.left, this.mDstRectF.top);
                this.mShaderMatrix.preScale(this.mDstRectF.width() / this.mBitmap.getWidth(), this.mDstRectF.height() / this.mBitmap.getHeight());
                this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
                this.mPaint.setShader((Shader)this.mBitmapShader);
            }
            this.mApplyGravity = false;
        }
    }
}
