// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface TintableImageSourceView
{
    @Nullable
    ColorStateList getSupportImageTintList();
    
    @Nullable
    PorterDuff$Mode getSupportImageTintMode();
    
    void setSupportImageTintList(@Nullable final ColorStateList p0);
    
    void setSupportImageTintMode(@Nullable final PorterDuff$Mode p0);
}
