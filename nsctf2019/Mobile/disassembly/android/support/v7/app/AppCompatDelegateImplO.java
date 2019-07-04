// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.view.KeyEvent;
import android.view.Window;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
class AppCompatDelegateImplO extends AppCompatDelegateImplN
{
    AppCompatDelegateImplO(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }
    
    @Override
    public boolean checkActionBarFocusKey(final KeyEvent keyEvent) {
        return false;
    }
}
