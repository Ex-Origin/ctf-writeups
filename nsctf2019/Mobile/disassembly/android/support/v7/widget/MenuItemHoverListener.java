// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuBuilder;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface MenuItemHoverListener
{
    void onItemHoverEnter(@NonNull final MenuBuilder p0, @NonNull final MenuItem p1);
    
    void onItemHoverExit(@NonNull final MenuBuilder p0, @NonNull final MenuItem p1);
}
