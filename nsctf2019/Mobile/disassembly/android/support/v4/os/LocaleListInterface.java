// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.support.annotation.NonNull;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import java.util.Locale;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
interface LocaleListInterface
{
    boolean equals(final Object p0);
    
    Locale get(final int p0);
    
    @Nullable
    Locale getFirstMatch(final String[] p0);
    
    Object getLocaleList();
    
    int hashCode();
    
    @IntRange(from = -1L)
    int indexOf(final Locale p0);
    
    boolean isEmpty();
    
    void setLocaleList(@NonNull final Locale... p0);
    
    @IntRange(from = 0L)
    int size();
    
    String toLanguageTags();
    
    String toString();
}
