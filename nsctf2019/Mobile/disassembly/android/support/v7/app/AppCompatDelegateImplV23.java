// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.view.ActionMode;
import android.view.ActionMode$Callback;
import android.view.Window$Callback;
import android.view.Window;
import android.content.Context;
import android.app.UiModeManager;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class AppCompatDelegateImplV23 extends AppCompatDelegateImplV14
{
    private final UiModeManager mUiModeManager;
    
    AppCompatDelegateImplV23(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
        this.mUiModeManager = (UiModeManager)context.getSystemService("uimode");
    }
    
    @Override
    int mapNightMode(final int n) {
        if (n == 0 && this.mUiModeManager.getNightMode() == 0) {
            return -1;
        }
        return super.mapNightMode(n);
    }
    
    @Override
    Window$Callback wrapWindowCallback(final Window$Callback window$Callback) {
        return (Window$Callback)new AppCompatWindowCallbackV23(window$Callback);
    }
    
    class AppCompatWindowCallbackV23 extends AppCompatWindowCallbackV14
    {
        AppCompatWindowCallbackV23(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback) {
            return null;
        }
        
        @Override
        public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback, final int n) {
            if (AppCompatDelegateImplV23.this.isHandleNativeActionModesEnabled()) {
                switch (n) {
                    case 0: {
                        return ((AppCompatWindowCallbackV14)this).startAsSupportActionMode(actionMode$Callback);
                    }
                }
            }
            return super.onWindowStartingActionMode(actionMode$Callback, n);
        }
    }
}
