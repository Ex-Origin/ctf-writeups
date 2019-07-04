// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;

public interface TintableCompoundButton
{
    @Nullable
    ColorStateList getSupportButtonTintList();
    
    @Nullable
    PorterDuff$Mode getSupportButtonTintMode();
    
    void setSupportButtonTintList(@Nullable final ColorStateList p0);
    
    void setSupportButtonTintMode(@Nullable final PorterDuff$Mode p0);
}
