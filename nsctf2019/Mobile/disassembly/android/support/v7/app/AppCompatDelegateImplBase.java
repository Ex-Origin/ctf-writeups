// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.view.View;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.appcompat.R;
import android.support.v7.view.ActionMode;
import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.support.v7.view.SupportMenuInflater;
import android.view.KeyEvent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.TintTypedArray;
import android.content.res.Resources$NotFoundException;
import android.os.Build$VERSION;
import android.view.Window;
import android.view.MenuInflater;
import android.content.Context;
import android.view.Window$Callback;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
abstract class AppCompatDelegateImplBase extends AppCompatDelegate
{
    static final boolean DEBUG = false;
    static final String EXCEPTION_HANDLER_MESSAGE_SUFFIX = ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.";
    private static final boolean SHOULD_INSTALL_EXCEPTION_HANDLER;
    private static boolean sInstalledExceptionHandler;
    private static final int[] sWindowBackgroundStyleable;
    ActionBar mActionBar;
    final AppCompatCallback mAppCompatCallback;
    final Window$Callback mAppCompatWindowCallback;
    final Context mContext;
    private boolean mEatKeyUpEvent;
    boolean mHasActionBar;
    private boolean mIsDestroyed;
    boolean mIsFloating;
    private boolean mIsStarted;
    MenuInflater mMenuInflater;
    final Window$Callback mOriginalWindowCallback;
    boolean mOverlayActionBar;
    boolean mOverlayActionMode;
    private CharSequence mTitle;
    final Window mWindow;
    boolean mWindowNoTitle;
    
    static {
        SHOULD_INSTALL_EXCEPTION_HANDLER = (Build$VERSION.SDK_INT < 21);
        if (AppCompatDelegateImplBase.SHOULD_INSTALL_EXCEPTION_HANDLER && !AppCompatDelegateImplBase.sInstalledExceptionHandler) {
            Thread.setDefaultUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new Thread.UncaughtExceptionHandler() {
                final /* synthetic */ UncaughtExceptionHandler val$defHandler = Thread.getDefaultUncaughtExceptionHandler();
                
                private boolean shouldWrapException(final Throwable t) {
                    boolean b2;
                    final boolean b = b2 = false;
                    if (t instanceof Resources$NotFoundException) {
                        final String message = t.getMessage();
                        b2 = b;
                        if (message != null) {
                            if (!message.contains("drawable")) {
                                b2 = b;
                                if (!message.contains("Drawable")) {
                                    return b2;
                                }
                            }
                            b2 = true;
                        }
                    }
                    return b2;
                }
                
                @Override
                public void uncaughtException(final Thread thread, final Throwable t) {
                    if (this.shouldWrapException(t)) {
                        final Resources$NotFoundException ex = new Resources$NotFoundException(t.getMessage() + ". If the resource you are trying to use is a vector resource, you may be referencing it in an unsupported way. See AppCompatDelegate.setCompatVectorFromResourcesEnabled() for more info.");
                        ((Throwable)ex).initCause(t.getCause());
                        ((Throwable)ex).setStackTrace(t.getStackTrace());
                        this.val$defHandler.uncaughtException(thread, (Throwable)ex);
                        return;
                    }
                    this.val$defHandler.uncaughtException(thread, t);
                }
            });
            AppCompatDelegateImplBase.sInstalledExceptionHandler = true;
        }
        sWindowBackgroundStyleable = new int[] { 16842836 };
    }
    
    AppCompatDelegateImplBase(final Context mContext, final Window mWindow, final AppCompatCallback mAppCompatCallback) {
        this.mContext = mContext;
        this.mWindow = mWindow;
        this.mAppCompatCallback = mAppCompatCallback;
        this.mOriginalWindowCallback = this.mWindow.getCallback();
        if (this.mOriginalWindowCallback instanceof AppCompatWindowCallbackBase) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        this.mAppCompatWindowCallback = this.wrapWindowCallback(this.mOriginalWindowCallback);
        this.mWindow.setCallback(this.mAppCompatWindowCallback);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(mContext, null, AppCompatDelegateImplBase.sWindowBackgroundStyleable);
        final Drawable drawableIfKnown = obtainStyledAttributes.getDrawableIfKnown(0);
        if (drawableIfKnown != null) {
            this.mWindow.setBackgroundDrawable(drawableIfKnown);
        }
        obtainStyledAttributes.recycle();
    }
    
    @Override
    public boolean applyDayNight() {
        return false;
    }
    
    @Override
    public boolean checkActionBarFocusKey(final KeyEvent keyEvent) {
        if (keyEvent.isCtrlPressed() && keyEvent.getUnicodeChar(keyEvent.getMetaState() & 0xFFFF8FFF) == 60) {
            final int action = keyEvent.getAction();
            if (action == 0) {
                final ActionBar supportActionBar = this.getSupportActionBar();
                if (supportActionBar != null && supportActionBar.isShowing() && supportActionBar.requestFocus()) {
                    return this.mEatKeyUpEvent = true;
                }
            }
            else if (action == 1 && this.mEatKeyUpEvent) {
                this.mEatKeyUpEvent = false;
                return true;
            }
        }
        return false;
    }
    
    abstract boolean dispatchKeyEvent(final KeyEvent p0);
    
    final Context getActionBarThemedContext() {
        Context themedContext = null;
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            themedContext = supportActionBar.getThemedContext();
        }
        Context mContext;
        if ((mContext = themedContext) == null) {
            mContext = this.mContext;
        }
        return mContext;
    }
    
    @Override
    public final ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return new ActionBarDrawableToggleImpl();
    }
    
    @Override
    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            this.initWindowDecorActionBar();
            Context context;
            if (this.mActionBar != null) {
                context = this.mActionBar.getThemedContext();
            }
            else {
                context = this.mContext;
            }
            this.mMenuInflater = new SupportMenuInflater(context);
        }
        return this.mMenuInflater;
    }
    
    @Override
    public ActionBar getSupportActionBar() {
        this.initWindowDecorActionBar();
        return this.mActionBar;
    }
    
    final CharSequence getTitle() {
        if (this.mOriginalWindowCallback instanceof Activity) {
            return ((Activity)this.mOriginalWindowCallback).getTitle();
        }
        return this.mTitle;
    }
    
    final Window$Callback getWindowCallback() {
        return this.mWindow.getCallback();
    }
    
    abstract void initWindowDecorActionBar();
    
    final boolean isDestroyed() {
        return this.mIsDestroyed;
    }
    
    @Override
    public boolean isHandleNativeActionModesEnabled() {
        return false;
    }
    
    final boolean isStarted() {
        return this.mIsStarted;
    }
    
    @Override
    public void onDestroy() {
        this.mIsDestroyed = true;
    }
    
    abstract boolean onKeyShortcut(final int p0, final KeyEvent p1);
    
    abstract boolean onMenuOpened(final int p0, final Menu p1);
    
    abstract void onPanelClosed(final int p0, final Menu p1);
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
    }
    
    @Override
    public void onStart() {
        this.mIsStarted = true;
    }
    
    @Override
    public void onStop() {
        this.mIsStarted = false;
    }
    
    abstract void onTitleChanged(final CharSequence p0);
    
    final ActionBar peekSupportActionBar() {
        return this.mActionBar;
    }
    
    @Override
    public void setHandleNativeActionModesEnabled(final boolean b) {
    }
    
    @Override
    public void setLocalNightMode(final int n) {
    }
    
    @Override
    public final void setTitle(final CharSequence mTitle) {
        this.onTitleChanged(this.mTitle = mTitle);
    }
    
    abstract ActionMode startSupportActionModeFromWindow(final ActionMode.Callback p0);
    
    Window$Callback wrapWindowCallback(final Window$Callback window$Callback) {
        return (Window$Callback)new AppCompatWindowCallbackBase(window$Callback);
    }
    
    private class ActionBarDrawableToggleImpl implements Delegate
    {
        @Override
        public Context getActionBarThemedContext() {
            return AppCompatDelegateImplBase.this.getActionBarThemedContext();
        }
        
        @Override
        public Drawable getThemeUpIndicator() {
            final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getActionBarThemedContext(), null, new int[] { R.attr.homeAsUpIndicator });
            final Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }
        
        @Override
        public boolean isNavigationVisible() {
            final ActionBar supportActionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            return supportActionBar != null && (supportActionBar.getDisplayOptions() & 0x4) != 0x0;
        }
        
        @Override
        public void setActionBarDescription(final int homeActionContentDescription) {
            final ActionBar supportActionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
        
        @Override
        public void setActionBarUpIndicator(final Drawable homeAsUpIndicator, final int homeActionContentDescription) {
            final ActionBar supportActionBar = AppCompatDelegateImplBase.this.getSupportActionBar();
            if (supportActionBar != null) {
                supportActionBar.setHomeAsUpIndicator(homeAsUpIndicator);
                supportActionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
        }
    }
    
    class AppCompatWindowCallbackBase extends WindowCallbackWrapper
    {
        AppCompatWindowCallbackBase(final Window$Callback window$Callback) {
            super(window$Callback);
        }
        
        @Override
        public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
            return AppCompatDelegateImplBase.this.dispatchKeyEvent(keyEvent) || super.dispatchKeyEvent(keyEvent);
        }
        
        @Override
        public boolean dispatchKeyShortcutEvent(final KeyEvent keyEvent) {
            return super.dispatchKeyShortcutEvent(keyEvent) || AppCompatDelegateImplBase.this.onKeyShortcut(keyEvent.getKeyCode(), keyEvent);
        }
        
        @Override
        public void onContentChanged() {
        }
        
        @Override
        public boolean onCreatePanelMenu(final int n, final Menu menu) {
            return (n != 0 || menu instanceof MenuBuilder) && super.onCreatePanelMenu(n, menu);
        }
        
        @Override
        public boolean onMenuOpened(final int n, final Menu menu) {
            super.onMenuOpened(n, menu);
            AppCompatDelegateImplBase.this.onMenuOpened(n, menu);
            return true;
        }
        
        @Override
        public void onPanelClosed(final int n, final Menu menu) {
            super.onPanelClosed(n, menu);
            AppCompatDelegateImplBase.this.onPanelClosed(n, menu);
        }
        
        @Override
        public boolean onPreparePanel(final int n, final View view, final Menu menu) {
            MenuBuilder menuBuilder;
            if (menu instanceof MenuBuilder) {
                menuBuilder = (MenuBuilder)menu;
            }
            else {
                menuBuilder = null;
            }
            boolean onPreparePanel;
            if (n == 0 && menuBuilder == null) {
                onPreparePanel = false;
            }
            else {
                if (menuBuilder != null) {
                    menuBuilder.setOverrideVisibleItems(true);
                }
                onPreparePanel = super.onPreparePanel(n, view, menu);
                if (menuBuilder != null) {
                    menuBuilder.setOverrideVisibleItems(false);
                    return onPreparePanel;
                }
            }
            return onPreparePanel;
        }
    }
}
