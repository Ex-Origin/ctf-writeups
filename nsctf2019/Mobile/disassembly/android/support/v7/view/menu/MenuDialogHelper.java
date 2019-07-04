// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.view.WindowManager$LayoutParams;
import android.support.v7.appcompat.R;
import android.os.IBinder;
import android.view.KeyEvent$DispatcherState;
import android.view.View;
import android.view.Window;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnClickListener;
import android.content.DialogInterface$OnKeyListener;

class MenuDialogHelper implements DialogInterface$OnKeyListener, DialogInterface$OnClickListener, DialogInterface$OnDismissListener, Callback
{
    private AlertDialog mDialog;
    private MenuBuilder mMenu;
    ListMenuPresenter mPresenter;
    private Callback mPresenterCallback;
    
    public MenuDialogHelper(final MenuBuilder mMenu) {
        this.mMenu = mMenu;
    }
    
    public void dismiss() {
        if (this.mDialog != null) {
            this.mDialog.dismiss();
        }
    }
    
    public void onClick(final DialogInterface dialogInterface, final int n) {
        this.mMenu.performItemAction((MenuItem)this.mPresenter.getAdapter().getItem(n), 0);
    }
    
    public void onCloseMenu(final MenuBuilder menuBuilder, final boolean b) {
        if (b || menuBuilder == this.mMenu) {
            this.dismiss();
        }
        if (this.mPresenterCallback != null) {
            this.mPresenterCallback.onCloseMenu(menuBuilder, b);
        }
    }
    
    public void onDismiss(final DialogInterface dialogInterface) {
        this.mPresenter.onCloseMenu(this.mMenu, true);
    }
    
    public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
        if (n == 82 || n == 4) {
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                final Window window = this.mDialog.getWindow();
                if (window != null) {
                    final View decorView = window.getDecorView();
                    if (decorView != null) {
                        final KeyEvent$DispatcherState keyDispatcherState = decorView.getKeyDispatcherState();
                        if (keyDispatcherState != null) {
                            keyDispatcherState.startTracking(keyEvent, (Object)this);
                            return true;
                        }
                    }
                }
            }
            else if (keyEvent.getAction() == 1 && !keyEvent.isCanceled()) {
                final Window window2 = this.mDialog.getWindow();
                if (window2 != null) {
                    final View decorView2 = window2.getDecorView();
                    if (decorView2 != null) {
                        final KeyEvent$DispatcherState keyDispatcherState2 = decorView2.getKeyDispatcherState();
                        if (keyDispatcherState2 != null && keyDispatcherState2.isTracking(keyEvent)) {
                            this.mMenu.close(true);
                            dialogInterface.dismiss();
                            return true;
                        }
                    }
                }
            }
        }
        return this.mMenu.performShortcut(n, keyEvent, 0);
    }
    
    public boolean onOpenSubMenu(final MenuBuilder menuBuilder) {
        return this.mPresenterCallback != null && this.mPresenterCallback.onOpenSubMenu(menuBuilder);
    }
    
    public void setPresenterCallback(final Callback mPresenterCallback) {
        this.mPresenterCallback = mPresenterCallback;
    }
    
    public void show(final IBinder token) {
        final MenuBuilder mMenu = this.mMenu;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mMenu.getContext());
        (this.mPresenter = new ListMenuPresenter(builder.getContext(), R.layout.abc_list_menu_item_layout)).setCallback(this);
        this.mMenu.addMenuPresenter(this.mPresenter);
        builder.setAdapter(this.mPresenter.getAdapter(), (DialogInterface$OnClickListener)this);
        final View headerView = mMenu.getHeaderView();
        if (headerView != null) {
            builder.setCustomTitle(headerView);
        }
        else {
            builder.setIcon(mMenu.getHeaderIcon()).setTitle(mMenu.getHeaderTitle());
        }
        builder.setOnKeyListener((DialogInterface$OnKeyListener)this);
        (this.mDialog = builder.create()).setOnDismissListener((DialogInterface$OnDismissListener)this);
        final WindowManager$LayoutParams attributes = this.mDialog.getWindow().getAttributes();
        attributes.type = 1003;
        if (token != null) {
            attributes.token = token;
        }
        attributes.flags |= 0x20000;
        this.mDialog.show();
    }
}
