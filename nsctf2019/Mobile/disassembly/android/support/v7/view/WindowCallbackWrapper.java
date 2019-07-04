// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view;

import android.view.ActionMode$Callback;
import android.view.WindowManager$LayoutParams;
import android.view.SearchEvent;
import android.view.KeyboardShortcutGroup;
import java.util.List;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ActionMode;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import android.support.annotation.RequiresApi;
import android.view.MotionEvent;
import android.support.annotation.RestrictTo;
import android.view.Window$Callback;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class WindowCallbackWrapper implements Window$Callback
{
    final Window$Callback mWrapped;
    
    public WindowCallbackWrapper(final Window$Callback mWrapped) {
        if (mWrapped == null) {
            throw new IllegalArgumentException("Window callback may not be null");
        }
        this.mWrapped = mWrapped;
    }
    
    @RequiresApi(12)
    public boolean dispatchGenericMotionEvent(final MotionEvent motionEvent) {
        return this.mWrapped.dispatchGenericMotionEvent(motionEvent);
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return this.mWrapped.dispatchKeyEvent(keyEvent);
    }
    
    @RequiresApi(11)
    public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
        return this.mWrapped.dispatchKeyShortcutEvent(keyEvent);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        return this.mWrapped.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }
    
    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        return this.mWrapped.dispatchTouchEvent(motionEvent);
    }
    
    public boolean dispatchTrackballEvent(final MotionEvent motionEvent) {
        return this.mWrapped.dispatchTrackballEvent(motionEvent);
    }
    
    @RequiresApi(11)
    public void onActionModeFinished(final ActionMode actionMode) {
        this.mWrapped.onActionModeFinished(actionMode);
    }
    
    @RequiresApi(11)
    public void onActionModeStarted(final ActionMode actionMode) {
        this.mWrapped.onActionModeStarted(actionMode);
    }
    
    public void onAttachedToWindow() {
        this.mWrapped.onAttachedToWindow();
    }
    
    public void onContentChanged() {
        this.mWrapped.onContentChanged();
    }
    
    public boolean onCreatePanelMenu(final int n, final Menu menu) {
        return this.mWrapped.onCreatePanelMenu(n, menu);
    }
    
    public View onCreatePanelView(final int n) {
        return this.mWrapped.onCreatePanelView(n);
    }
    
    public void onDetachedFromWindow() {
        this.mWrapped.onDetachedFromWindow();
    }
    
    public boolean onMenuItemSelected(final int n, final MenuItem menuItem) {
        return this.mWrapped.onMenuItemSelected(n, menuItem);
    }
    
    public boolean onMenuOpened(final int n, final Menu menu) {
        return this.mWrapped.onMenuOpened(n, menu);
    }
    
    public void onPanelClosed(final int n, final Menu menu) {
        this.mWrapped.onPanelClosed(n, menu);
    }
    
    public boolean onPreparePanel(final int n, final View view, final Menu menu) {
        return this.mWrapped.onPreparePanel(n, view, menu);
    }
    
    @RequiresApi(24)
    public void onProvideKeyboardShortcuts(final List<KeyboardShortcutGroup> list, final Menu menu, final int n) {
        this.mWrapped.onProvideKeyboardShortcuts((List)list, menu, n);
    }
    
    public boolean onSearchRequested() {
        return this.mWrapped.onSearchRequested();
    }
    
    @RequiresApi(23)
    public boolean onSearchRequested(final SearchEvent searchEvent) {
        return this.mWrapped.onSearchRequested(searchEvent);
    }
    
    public void onWindowAttributesChanged(final WindowManager$LayoutParams windowManager$LayoutParams) {
        this.mWrapped.onWindowAttributesChanged(windowManager$LayoutParams);
    }
    
    public void onWindowFocusChanged(final boolean b) {
        this.mWrapped.onWindowFocusChanged(b);
    }
    
    @RequiresApi(11)
    public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback) {
        return this.mWrapped.onWindowStartingActionMode(actionMode$Callback);
    }
    
    @RequiresApi(23)
    public ActionMode onWindowStartingActionMode(final ActionMode$Callback actionMode$Callback, final int n) {
        return this.mWrapped.onWindowStartingActionMode(actionMode$Callback, n);
    }
}
