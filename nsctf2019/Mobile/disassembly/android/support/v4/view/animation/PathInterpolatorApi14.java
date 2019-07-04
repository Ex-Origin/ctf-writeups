// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.animation;

import android.graphics.PathMeasure;
import android.graphics.Path;
import android.view.animation.Interpolator;

class PathInterpolatorApi14 implements Interpolator
{
    private static final float PRECISION = 0.002f;
    private final float[] mX;
    private final float[] mY;
    
    PathInterpolatorApi14(final float n, final float n2) {
        this(createQuad(n, n2));
    }
    
    PathInterpolatorApi14(final float n, final float n2, final float n3, final float n4) {
        this(createCubic(n, n2, n3, n4));
    }
    
    PathInterpolatorApi14(final Path path) {
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        final float length = pathMeasure.getLength();
        final int n = (int)(length / 0.002f) + 1;
        this.mX = new float[n];
        this.mY = new float[n];
        final float[] array = new float[2];
        for (int i = 0; i < n; ++i) {
            pathMeasure.getPosTan(i * length / (n - 1), array, (float[])null);
            this.mX[i] = array[0];
            this.mY[i] = array[1];
        }
    }
    
    private static Path createCubic(final float n, final float n2, final float n3, final float n4) {
        final Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(n, n2, n3, n4, 1.0f, 1.0f);
        return path;
    }
    
    private static Path createQuad(final float n, final float n2) {
        final Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.quadTo(n, n2, 1.0f, 1.0f);
        return path;
    }
    
    public float getInterpolation(float n) {
        if (n <= 0.0f) {
            return 0.0f;
        }
        if (n >= 1.0f) {
            return 1.0f;
        }
        int n2 = 0;
        int n3 = this.mX.length - 1;
        while (n3 - n2 > 1) {
            final int n4 = (n2 + n3) / 2;
            if (n < this.mX[n4]) {
                n3 = n4;
            }
            else {
                n2 = n4;
            }
        }
        final float n5 = this.mX[n3] - this.mX[n2];
        if (n5 == 0.0f) {
            return this.mY[n2];
        }
        n = (n - this.mX[n2]) / n5;
        final float n6 = this.mY[n2];
        return (this.mY[n3] - n6) * n + n6;
    }
}
