// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.support.annotation.RestrictTo;
import android.animation.TypeEvaluator;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ArgbEvaluator implements TypeEvaluator
{
    private static final ArgbEvaluator sInstance;
    
    static {
        sInstance = new ArgbEvaluator();
    }
    
    public static ArgbEvaluator getInstance() {
        return ArgbEvaluator.sInstance;
    }
    
    public Object evaluate(final float n, final Object o, final Object o2) {
        final int intValue = (int)o;
        final float n2 = (intValue >> 24 & 0xFF) / 255.0f;
        final float n3 = (intValue >> 16 & 0xFF) / 255.0f;
        final float n4 = (intValue >> 8 & 0xFF) / 255.0f;
        final float n5 = (intValue & 0xFF) / 255.0f;
        final int intValue2 = (int)o2;
        final float n6 = (intValue2 >> 24 & 0xFF) / 255.0f;
        final float n7 = (intValue2 >> 16 & 0xFF) / 255.0f;
        final float n8 = (intValue2 >> 8 & 0xFF) / 255.0f;
        final float n9 = (intValue2 & 0xFF) / 255.0f;
        final float n10 = (float)Math.pow(n3, 2.2);
        final float n11 = (float)Math.pow(n4, 2.2);
        final float n12 = (float)Math.pow(n5, 2.2);
        return Math.round((n2 + (n6 - n2) * n) * 255.0f) << 24 | Math.round((float)Math.pow(n10 + ((float)Math.pow(n7, 2.2) - n10) * n, 0.45454545454545453) * 255.0f) << 16 | Math.round((float)Math.pow(n11 + ((float)Math.pow(n8, 2.2) - n11) * n, 0.45454545454545453) * 255.0f) << 8 | Math.round((float)Math.pow(n12 + ((float)Math.pow(n9, 2.2) - n12) * n, 0.45454545454545453) * 255.0f);
    }
}
