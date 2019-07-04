// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.widget.ListView;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public interface ShowableListMenu
{
    void dismiss();
    
    ListView getListView();
    
    boolean isShowing();
    
    void show();
}
