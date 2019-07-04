// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.support.v4.os.BuildCompat;
import android.graphics.drawable.Icon;
import android.support.annotation.RestrictTo;
import android.content.Intent$ShortcutIconResource;
import android.os.Parcelable;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;

public class IconCompat
{
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final int TYPE_ADAPTIVE_BITMAP = 5;
    private static final int TYPE_BITMAP = 1;
    private static final int TYPE_DATA = 3;
    private static final int TYPE_RESOURCE = 2;
    private static final int TYPE_URI = 4;
    private int mInt1;
    private int mInt2;
    private Object mObj1;
    private final int mType;
    
    private IconCompat(final int mType) {
        this.mType = mType;
    }
    
    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(final Bitmap bitmap) {
        final int n = (int)(0.6666667f * Math.min(bitmap.getWidth(), bitmap.getHeight()));
        final Bitmap bitmap2 = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint(3);
        final float n2 = n * 0.5f;
        final float n3 = n2 * 0.9166667f;
        final float n4 = 0.010416667f * n;
        paint.setColor(0);
        paint.setShadowLayer(n4, 0.0f, 0.020833334f * n, 1023410176);
        canvas.drawCircle(n2, n2, n3, paint);
        paint.setShadowLayer(n4, 0.0f, 0.0f, 503316480);
        canvas.drawCircle(n2, n2, n3, paint);
        paint.clearShadowLayer();
        paint.setColor(-16777216);
        final BitmapShader shader = new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        final Matrix localMatrix = new Matrix();
        localMatrix.setTranslate((float)(-(bitmap.getWidth() - n) / 2), (float)(-(bitmap.getHeight() - n) / 2));
        shader.setLocalMatrix(localMatrix);
        paint.setShader((Shader)shader);
        canvas.drawCircle(n2, n2, n3, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    public static IconCompat createWithAdaptiveBitmap(final Bitmap mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(5);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithBitmap(final Bitmap mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(1);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithContentUri(final Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        return createWithContentUri(uri.toString());
    }
    
    public static IconCompat createWithContentUri(final String mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(4);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithData(final byte[] mObj1, final int mInt1, final int mInt2) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(3);
        iconCompat.mObj1 = mObj1;
        iconCompat.mInt1 = mInt1;
        iconCompat.mInt2 = mInt2;
        return iconCompat;
    }
    
    public static IconCompat createWithResource(final Context mObj1, @DrawableRes final int mInt1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = mInt1;
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void addToShortcutIntent(final Intent intent) {
        switch (this.mType) {
            default: {
                throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
            }
            case 1: {
                intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)this.mObj1);
            }
            case 5: {
                intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1));
            }
            case 2: {
                intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent$ShortcutIconResource.fromContext((Context)this.mObj1, this.mInt1));
            }
        }
    }
    
    @TargetApi(10000)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @VisibleForTesting
    public Icon toIcon() {
        switch (this.mType) {
            default: {
                throw new IllegalArgumentException("Unknown type");
            }
            case 1: {
                return Icon.createWithBitmap((Bitmap)this.mObj1);
            }
            case 5: {
                if (BuildCompat.isAtLeastO()) {
                    return Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                }
                return Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1));
            }
            case 2: {
                return Icon.createWithResource((Context)this.mObj1, this.mInt1);
            }
            case 3: {
                return Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
            }
            case 4: {
                return Icon.createWithContentUri((String)this.mObj1);
            }
        }
    }
}
