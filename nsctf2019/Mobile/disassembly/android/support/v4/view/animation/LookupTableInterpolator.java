// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator implements Interpolator
{
    private final float mStepSize;
    private final float[] mValues;
    
    public LookupTableInterpolator(final float[] mValues) {
        this.mValues = mValues;
        this.mStepSize = 1.0f / (this.mValues.length - 1);
    }
    
    public float getInterpolation(float n) {
        if (n >= 1.0f) {
            return 1.0f;
        }
        if (n <= 0.0f) {
            return 0.0f;
        }
        final int min = Math.min((int)((this.mValues.length - 1) * n), this.mValues.length - 2);
        n = (n - min * this.mStepSize) / this.mStepSize;
        return this.mValues[min] + (this.mValues[min + 1] - this.mValues[min]) * n;
    }
}
