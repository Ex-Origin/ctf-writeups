// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.view.Menu;
import android.view.KeyboardShortcutGroup;
import java.util.List;
import android.view.Window$Callback;
import android.view.Window;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(24)
class AppCompatDelegateImplN extends AppCompatDelegateImplV23
{
    AppCompatDelegateImplN(final Context context, final Window window, final AppCompatCallback appCompatCallback) {
        super(context, window, appCompatCallback);
    }
    
    @Override
    Window$Callback wrapWindowCallback(final Window$Callback window$Callback) {
        return (Window$Callback)new AppCompatWindowCallbackN(window$Callback);
    }
    
    class AppCompatWindowCallbackN extends AppCompatWindowCallbackV23
    {
        AppCompatWindowCallbackN(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public void onProvideKeyboardShortcuts(final List<KeyboardShortcutGroup> list, final Menu menu, final int n) {
            final PanelFeatureState panelState = AppCompatDelegateImplN.this.getPanelState(0, true);
            if (panelState != null && panelState.menu != null) {
                super.onProvideKeyboardShortcuts(list, (Menu)panelState.menu, n);
                return;
            }
            super.onProvideKeyboardShortcuts(list, menu, n);
        }
    }
}
