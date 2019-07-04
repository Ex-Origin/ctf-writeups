// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface AutoSizeableTextView
{
    int getAutoSizeMaxTextSize();
    
    int getAutoSizeMinTextSize();
    
    int getAutoSizeStepGranularity();
    
    int[] getAutoSizeTextAvailableSizes();
    
    int getAutoSizeTextType();
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int p0, final int p1, final int p2, final int p3) throws IllegalArgumentException;
    
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] p0, final int p1) throws IllegalArgumentException;
    
    void setAutoSizeTextTypeWithDefaults(final int p0);
}
