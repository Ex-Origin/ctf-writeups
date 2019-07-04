// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.View;
import android.view.KeyEvent$DispatcherState;
import android.view.KeyEvent$Callback;
import android.view.KeyEvent;

@Deprecated
public final class KeyEventCompat
{
    @Deprecated
    public static boolean dispatch(final KeyEvent keyEvent, final KeyEvent$Callback keyEvent$Callback, final Object o, final Object o2) {
        return keyEvent.dispatch(keyEvent$Callback, (KeyEvent$DispatcherState)o, o2);
    }
    
    @Deprecated
    public static Object getKeyDispatcherState(final View view) {
        return view.getKeyDispatcherState();
    }
    
    @Deprecated
    public static boolean hasModifiers(final KeyEvent keyEvent, final int n) {
        return keyEvent.hasModifiers(n);
    }
    
    @Deprecated
    public static boolean hasNoModifiers(final KeyEvent keyEvent) {
        return keyEvent.hasNoModifiers();
    }
    
    @Deprecated
    public static boolean isCtrlPressed(final KeyEvent keyEvent) {
        return keyEvent.isCtrlPressed();
    }
    
    @Deprecated
    public static boolean isTracking(final KeyEvent keyEvent) {
        return keyEvent.isTracking();
    }
    
    @Deprecated
    public static boolean metaStateHasModifiers(final int n, final int n2) {
        return KeyEvent.metaStateHasModifiers(n, n2);
    }
    
    @Deprecated
    public static boolean metaStateHasNoModifiers(final int n) {
        return KeyEvent.metaStateHasNoModifiers(n);
    }
    
    @Deprecated
    public static int normalizeMetaState(final int n) {
        return KeyEvent.normalizeMetaState(n);
    }
    
    @Deprecated
    public static void startTracking(final KeyEvent keyEvent) {
        keyEvent.startTracking();
    }
}
