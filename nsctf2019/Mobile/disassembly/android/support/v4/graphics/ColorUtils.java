// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics;

import android.support.annotation.VisibleForTesting;
import android.support.annotation.IntRange;
import android.support.annotation.FloatRange;
import android.support.annotation.ColorInt;
import android.graphics.Color;
import android.support.annotation.NonNull;

public final class ColorUtils
{
    private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
    private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
    private static final ThreadLocal<double[]> TEMP_ARRAY;
    private static final double XYZ_EPSILON = 0.008856;
    private static final double XYZ_KAPPA = 903.3;
    private static final double XYZ_WHITE_REFERENCE_X = 95.047;
    private static final double XYZ_WHITE_REFERENCE_Y = 100.0;
    private static final double XYZ_WHITE_REFERENCE_Z = 108.883;
    
    static {
        TEMP_ARRAY = new ThreadLocal<double[]>();
    }
    
    @ColorInt
    public static int HSLToColor(@NonNull final float[] array) {
        final float n = array[0];
        final float n2 = array[1];
        final float n3 = array[2];
        final float n4 = (1.0f - Math.abs(2.0f * n3 - 1.0f)) * n2;
        final float n5 = n3 - 0.5f * n4;
        final float n6 = n4 * (1.0f - Math.abs(n / 60.0f % 2.0f - 1.0f));
        final int n7 = (int)n / 60;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        switch (n7) {
            case 0: {
                n8 = Math.round(255.0f * (n4 + n5));
                n9 = Math.round(255.0f * (n6 + n5));
                n10 = Math.round(255.0f * n5);
                break;
            }
            case 1: {
                n8 = Math.round(255.0f * (n6 + n5));
                n9 = Math.round(255.0f * (n4 + n5));
                n10 = Math.round(255.0f * n5);
                break;
            }
            case 2: {
                n8 = Math.round(255.0f * n5);
                n9 = Math.round(255.0f * (n4 + n5));
                n10 = Math.round(255.0f * (n6 + n5));
                break;
            }
            case 3: {
                n8 = Math.round(255.0f * n5);
                n9 = Math.round(255.0f * (n6 + n5));
                n10 = Math.round(255.0f * (n4 + n5));
                break;
            }
            case 4: {
                n8 = Math.round(255.0f * (n6 + n5));
                n9 = Math.round(255.0f * n5);
                n10 = Math.round(255.0f * (n4 + n5));
                break;
            }
            case 5:
            case 6: {
                n8 = Math.round(255.0f * (n4 + n5));
                n9 = Math.round(255.0f * n5);
                n10 = Math.round(255.0f * (n6 + n5));
                break;
            }
        }
        return Color.rgb(constrain(n8, 0, 255), constrain(n9, 0, 255), constrain(n10, 0, 255));
    }
    
    @ColorInt
    public static int LABToColor(@FloatRange(from = 0.0, to = 100.0) final double n, @FloatRange(from = -128.0, to = 127.0) final double n2, @FloatRange(from = -128.0, to = 127.0) final double n3) {
        final double[] tempDouble3Array = getTempDouble3Array();
        LABToXYZ(n, n2, n3, tempDouble3Array);
        return XYZToColor(tempDouble3Array[0], tempDouble3Array[1], tempDouble3Array[2]);
    }
    
    public static void LABToXYZ(@FloatRange(from = 0.0, to = 100.0) double pow, @FloatRange(from = -128.0, to = 127.0) double pow2, @FloatRange(from = -128.0, to = 127.0) double pow3, @NonNull final double[] array) {
        final double n = (16.0 + pow) / 116.0;
        final double n2 = pow2 / 500.0 + n;
        final double n3 = n - pow3 / 200.0;
        pow2 = Math.pow(n2, 3.0);
        if (pow2 <= 0.008856) {
            pow2 = (116.0 * n2 - 16.0) / 903.3;
        }
        if (pow > 7.9996247999999985) {
            pow = Math.pow(n, 3.0);
        }
        else {
            pow /= 903.3;
        }
        pow3 = Math.pow(n3, 3.0);
        if (pow3 <= 0.008856) {
            pow3 = (116.0 * n3 - 16.0) / 903.3;
        }
        array[0] = 95.047 * pow2;
        array[1] = 100.0 * pow;
        array[2] = 108.883 * pow3;
    }
    
    public static void RGBToHSL(@IntRange(from = 0L, to = 255L) final int n, @IntRange(from = 0L, to = 255L) final int n2, @IntRange(from = 0L, to = 255L) final int n3, @NonNull final float[] array) {
        final float n4 = n / 255.0f;
        final float n5 = n2 / 255.0f;
        final float n6 = n3 / 255.0f;
        final float max = Math.max(n4, Math.max(n5, n6));
        final float min = Math.min(n4, Math.min(n5, n6));
        final float n7 = max - min;
        final float n8 = (max + min) / 2.0f;
        float n9;
        float n10;
        if (max == min) {
            n9 = 0.0f;
            n10 = 0.0f;
        }
        else {
            float n11;
            if (max == n4) {
                n11 = (n5 - n6) / n7 % 6.0f;
            }
            else if (max == n5) {
                n11 = (n6 - n4) / n7 + 2.0f;
            }
            else {
                n11 = (n4 - n5) / n7 + 4.0f;
            }
            final float n12 = n7 / (1.0f - Math.abs(2.0f * n8 - 1.0f));
            n10 = n11;
            n9 = n12;
        }
        float n14;
        final float n13 = n14 = 60.0f * n10 % 360.0f;
        if (n13 < 0.0f) {
            n14 = n13 + 360.0f;
        }
        array[0] = constrain(n14, 0.0f, 360.0f);
        array[1] = constrain(n9, 0.0f, 1.0f);
        array[2] = constrain(n8, 0.0f, 1.0f);
    }
    
    public static void RGBToLAB(@IntRange(from = 0L, to = 255L) final int n, @IntRange(from = 0L, to = 255L) final int n2, @IntRange(from = 0L, to = 255L) final int n3, @NonNull final double[] array) {
        RGBToXYZ(n, n2, n3, array);
        XYZToLAB(array[0], array[1], array[2], array);
    }
    
    public static void RGBToXYZ(@IntRange(from = 0L, to = 255L) final int n, @IntRange(from = 0L, to = 255L) final int n2, @IntRange(from = 0L, to = 255L) final int n3, @NonNull final double[] array) {
        if (array.length != 3) {
            throw new IllegalArgumentException("outXyz must have a length of 3.");
        }
        final double n4 = n / 255.0;
        double pow;
        if (n4 < 0.04045) {
            pow = n4 / 12.92;
        }
        else {
            pow = Math.pow((0.055 + n4) / 1.055, 2.4);
        }
        final double n5 = n2 / 255.0;
        double pow2;
        if (n5 < 0.04045) {
            pow2 = n5 / 12.92;
        }
        else {
            pow2 = Math.pow((0.055 + n5) / 1.055, 2.4);
        }
        final double n6 = n3 / 255.0;
        double pow3;
        if (n6 < 0.04045) {
            pow3 = n6 / 12.92;
        }
        else {
            pow3 = Math.pow((0.055 + n6) / 1.055, 2.4);
        }
        array[0] = 100.0 * (0.4124 * pow + 0.3576 * pow2 + 0.1805 * pow3);
        array[1] = 100.0 * (0.2126 * pow + 0.7152 * pow2 + 0.0722 * pow3);
        array[2] = 100.0 * (0.0193 * pow + 0.1192 * pow2 + 0.9505 * pow3);
    }
    
    @ColorInt
    public static int XYZToColor(@FloatRange(from = 0.0, to = 95.047) double n, @FloatRange(from = 0.0, to = 100.0) double n2, @FloatRange(from = 0.0, to = 108.883) double n3) {
        final double n4 = (3.2406 * n + -1.5372 * n2 + -0.4986 * n3) / 100.0;
        final double n5 = (-0.9689 * n + 1.8758 * n2 + 0.0415 * n3) / 100.0;
        n3 = (0.0557 * n + -0.204 * n2 + 1.057 * n3) / 100.0;
        if (n4 > 0.0031308) {
            n = 1.055 * Math.pow(n4, 0.4166666666666667) - 0.055;
        }
        else {
            n = n4 * 12.92;
        }
        if (n5 > 0.0031308) {
            n2 = 1.055 * Math.pow(n5, 0.4166666666666667) - 0.055;
        }
        else {
            n2 = n5 * 12.92;
        }
        if (n3 > 0.0031308) {
            n3 = 1.055 * Math.pow(n3, 0.4166666666666667) - 0.055;
        }
        else {
            n3 *= 12.92;
        }
        return Color.rgb(constrain((int)Math.round(255.0 * n), 0, 255), constrain((int)Math.round(255.0 * n2), 0, 255), constrain((int)Math.round(255.0 * n3), 0, 255));
    }
    
    public static void XYZToLAB(@FloatRange(from = 0.0, to = 95.047) double pivotXyzComponent, @FloatRange(from = 0.0, to = 100.0) double pivotXyzComponent2, @FloatRange(from = 0.0, to = 108.883) double pivotXyzComponent3, @NonNull final double[] array) {
        if (array.length != 3) {
            throw new IllegalArgumentException("outLab must have a length of 3.");
        }
        pivotXyzComponent = pivotXyzComponent(pivotXyzComponent / 95.047);
        pivotXyzComponent2 = pivotXyzComponent(pivotXyzComponent2 / 100.0);
        pivotXyzComponent3 = pivotXyzComponent(pivotXyzComponent3 / 108.883);
        array[0] = Math.max(0.0, 116.0 * pivotXyzComponent2 - 16.0);
        array[1] = 500.0 * (pivotXyzComponent - pivotXyzComponent2);
        array[2] = 200.0 * (pivotXyzComponent2 - pivotXyzComponent3);
    }
    
    @ColorInt
    public static int blendARGB(@ColorInt final int n, @ColorInt final int n2, @FloatRange(from = 0.0, to = 1.0) final float n3) {
        final float n4 = 1.0f - n3;
        return Color.argb((int)(Color.alpha(n) * n4 + Color.alpha(n2) * n3), (int)(Color.red(n) * n4 + Color.red(n2) * n3), (int)(Color.green(n) * n4 + Color.green(n2) * n3), (int)(Color.blue(n) * n4 + Color.blue(n2) * n3));
    }
    
    public static void blendHSL(@NonNull final float[] array, @NonNull final float[] array2, @FloatRange(from = 0.0, to = 1.0) final float n, @NonNull final float[] array3) {
        if (array3.length != 3) {
            throw new IllegalArgumentException("result must have a length of 3.");
        }
        final float n2 = 1.0f - n;
        array3[0] = circularInterpolate(array[0], array2[0], n);
        array3[1] = array[1] * n2 + array2[1] * n;
        array3[2] = array[2] * n2 + array2[2] * n;
    }
    
    public static void blendLAB(@NonNull final double[] array, @NonNull final double[] array2, @FloatRange(from = 0.0, to = 1.0) final double n, @NonNull final double[] array3) {
        if (array3.length != 3) {
            throw new IllegalArgumentException("outResult must have a length of 3.");
        }
        final double n2 = 1.0 - n;
        array3[0] = array[0] * n2 + array2[0] * n;
        array3[1] = array[1] * n2 + array2[1] * n;
        array3[2] = array[2] * n2 + array2[2] * n;
    }
    
    public static double calculateContrast(@ColorInt final int n, @ColorInt final int n2) {
        if (Color.alpha(n2) != 255) {
            throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(n2));
        }
        int compositeColors = n;
        if (Color.alpha(n) < 255) {
            compositeColors = compositeColors(n, n2);
        }
        final double n3 = calculateLuminance(compositeColors) + 0.05;
        final double n4 = calculateLuminance(n2) + 0.05;
        return Math.max(n3, n4) / Math.min(n3, n4);
    }
    
    @FloatRange(from = 0.0, to = 1.0)
    public static double calculateLuminance(@ColorInt final int n) {
        final double[] tempDouble3Array = getTempDouble3Array();
        colorToXYZ(n, tempDouble3Array);
        return tempDouble3Array[1] / 100.0;
    }
    
    public static int calculateMinimumAlpha(@ColorInt final int n, @ColorInt final int n2, final float n3) {
        if (Color.alpha(n2) != 255) {
            throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(n2));
        }
        int n4;
        if (calculateContrast(setAlphaComponent(n, 255), n2) < n3) {
            n4 = -1;
        }
        else {
            int n5 = 0;
            int n6 = 0;
            int n7 = 255;
            while (true) {
                n4 = n7;
                if (n5 > 10) {
                    break;
                }
                n4 = n7;
                if (n7 - n6 <= 1) {
                    break;
                }
                final int n8 = (n6 + n7) / 2;
                if (calculateContrast(setAlphaComponent(n, n8), n2) < n3) {
                    n6 = n8;
                }
                else {
                    n7 = n8;
                }
                ++n5;
            }
        }
        return n4;
    }
    
    @VisibleForTesting
    static float circularInterpolate(final float n, final float n2, final float n3) {
        float n4 = n;
        float n5 = n2;
        if (Math.abs(n2 - n) > 180.0f) {
            if (n2 > n) {
                n4 = n + 360.0f;
                n5 = n2;
            }
            else {
                n5 = n2 + 360.0f;
                n4 = n;
            }
        }
        return ((n5 - n4) * n3 + n4) % 360.0f;
    }
    
    public static void colorToHSL(@ColorInt final int n, @NonNull final float[] array) {
        RGBToHSL(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    public static void colorToLAB(@ColorInt final int n, @NonNull final double[] array) {
        RGBToLAB(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    public static void colorToXYZ(@ColorInt final int n, @NonNull final double[] array) {
        RGBToXYZ(Color.red(n), Color.green(n), Color.blue(n), array);
    }
    
    private static int compositeAlpha(final int n, final int n2) {
        return 255 - (255 - n2) * (255 - n) / 255;
    }
    
    public static int compositeColors(@ColorInt final int n, @ColorInt final int n2) {
        final int alpha = Color.alpha(n2);
        final int alpha2 = Color.alpha(n);
        final int compositeAlpha = compositeAlpha(alpha2, alpha);
        return Color.argb(compositeAlpha, compositeComponent(Color.red(n), alpha2, Color.red(n2), alpha, compositeAlpha), compositeComponent(Color.green(n), alpha2, Color.green(n2), alpha, compositeAlpha), compositeComponent(Color.blue(n), alpha2, Color.blue(n2), alpha, compositeAlpha));
    }
    
    private static int compositeComponent(final int n, final int n2, final int n3, final int n4, final int n5) {
        if (n5 == 0) {
            return 0;
        }
        return (n * 255 * n2 + n3 * n4 * (255 - n2)) / (n5 * 255);
    }
    
    private static float constrain(final float n, final float n2, final float n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    private static int constrain(final int n, final int n2, final int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }
    
    public static double distanceEuclidean(@NonNull final double[] array, @NonNull final double[] array2) {
        return Math.sqrt(Math.pow(array[0] - array2[0], 2.0) + Math.pow(array[1] - array2[1], 2.0) + Math.pow(array[2] - array2[2], 2.0));
    }
    
    private static double[] getTempDouble3Array() {
        double[] array;
        if ((array = ColorUtils.TEMP_ARRAY.get()) == null) {
            array = new double[3];
            ColorUtils.TEMP_ARRAY.set(array);
        }
        return array;
    }
    
    private static double pivotXyzComponent(final double n) {
        if (n > 0.008856) {
            return Math.pow(n, 0.3333333333333333);
        }
        return (903.3 * n + 16.0) / 116.0;
    }
    
    @ColorInt
    public static int setAlphaComponent(@ColorInt final int n, @IntRange(from = 0L, to = 255L) final int n2) {
        if (n2 < 0 || n2 > 255) {
            throw new IllegalArgumentException("alpha must be between 0 and 255.");
        }
        return (0xFFFFFF & n) | n2 << 24;
    }
}
