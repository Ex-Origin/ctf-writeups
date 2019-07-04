// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.drawable;

import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface TintAwareDrawable
{
    void setTint(@ColorInt final int p0);
    
    void setTintList(final ColorStateList p0);
    
    void setTintMode(final PorterDuff$Mode p0);
}
