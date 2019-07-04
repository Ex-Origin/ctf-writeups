// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.support.annotation.Nullable;
import android.graphics.Rect;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Display;
import android.os.Build$VERSION;
import android.graphics.Point;
import android.view.WindowManager;
import android.support.annotation.StyleRes;
import android.support.annotation.AttrRes;
import android.support.v7.appcompat.R;
import android.support.annotation.NonNull;
import android.widget.PopupWindow$OnDismissListener;
import android.content.Context;
import android.view.View;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MenuPopupHelper implements MenuHelper
{
    private static final int TOUCH_EPICENTER_SIZE_DP = 48;
    private View mAnchorView;
    private final Context mContext;
    private int mDropDownGravity;
    private boolean mForceShowIcon;
    private final PopupWindow$OnDismissListener mInternalOnDismissListener;
    private final MenuBuilder mMenu;
    private PopupWindow$OnDismissListener mOnDismissListener;
    private final boolean mOverflowOnly;
    private MenuPopup mPopup;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    private MenuPresenter.Callback mPresenterCallback;
    
    public MenuPopupHelper(@NonNull final Context context, @NonNull final MenuBuilder menuBuilder) {
        this(context, menuBuilder, null, false, R.attr.popupMenuStyle, 0);
    }
    
    public MenuPopupHelper(@NonNull final Context context, @NonNull final MenuBuilder menuBuilder, @NonNull final View view) {
        this(context, menuBuilder, view, false, R.attr.popupMenuStyle, 0);
    }
    
    public MenuPopupHelper(@NonNull final Context context, @NonNull final MenuBuilder menuBuilder, @NonNull final View view, final boolean b, @AttrRes final int n) {
        this(context, menuBuilder, view, b, n, 0);
    }
    
    public MenuPopupHelper(@NonNull final Context mContext, @NonNull final MenuBuilder mMenu, @NonNull final View mAnchorView, final boolean mOverflowOnly, @AttrRes final int mPopupStyleAttr, @StyleRes final int mPopupStyleRes) {
        this.mDropDownGravity = 8388611;
        this.mInternalOnDismissListener = (PopupWindow$OnDismissListener)new PopupWindow$OnDismissListener() {
            public void onDismiss() {
                MenuPopupHelper.this.onDismiss();
            }
        };
        this.mContext = mContext;
        this.mMenu = mMenu;
        this.mAnchorView = mAnchorView;
        this.mOverflowOnly = mOverflowOnly;
        this.mPopupStyleAttr = mPopupStyleAttr;
        this.mPopupStyleRes = mPopupStyleRes;
    }
    
    @NonNull
    private MenuPopup createPopup() {
        final Display defaultDisplay = ((WindowManager)this.mContext.getSystemService("window")).getDefaultDisplay();
        final Point point = new Point();
        if (Build$VERSION.SDK_INT >= 17) {
            defaultDisplay.getRealSize(point);
        }
        else if (Build$VERSION.SDK_INT >= 13) {
            defaultDisplay.getSize(point);
        }
        else {
            point.set(defaultDisplay.getWidth(), defaultDisplay.getHeight());
        }
        int n;
        if (Math.min(point.x, point.y) >= this.mContext.getResources().getDimensionPixelSize(R.dimen.abc_cascading_menus_min_smallest_width)) {
            n = 1;
        }
        else {
            n = 0;
        }
        MenuPresenter menuPresenter;
        if (n != 0) {
            menuPresenter = new CascadingMenuPopup(this.mContext, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
        }
        else {
            menuPresenter = new StandardMenuPopup(this.mContext, this.mMenu, this.mAnchorView, this.mPopupStyleAttr, this.mPopupStyleRes, this.mOverflowOnly);
        }
        ((MenuPopup)menuPresenter).addMenu(this.mMenu);
        ((MenuPopup)menuPresenter).setOnDismissListener(this.mInternalOnDismissListener);
        ((MenuPopup)menuPresenter).setAnchorView(this.mAnchorView);
        menuPresenter.setCallback(this.mPresenterCallback);
        ((MenuPopup)menuPresenter).setForceShowIcon(this.mForceShowIcon);
        ((MenuPopup)menuPresenter).setGravity(this.mDropDownGravity);
        return (MenuPopup)menuPresenter;
    }
    
    private void showPopup(int n, final int verticalOffset, final boolean b, final boolean showTitle) {
        final MenuPopup popup = this.getPopup();
        popup.setShowTitle(showTitle);
        if (b) {
            int horizontalOffset = n;
            if ((GravityCompat.getAbsoluteGravity(this.mDropDownGravity, ViewCompat.getLayoutDirection(this.mAnchorView)) & 0x7) == 0x5) {
                horizontalOffset = n - this.mAnchorView.getWidth();
            }
            popup.setHorizontalOffset(horizontalOffset);
            popup.setVerticalOffset(verticalOffset);
            n = (int)(48.0f * this.mContext.getResources().getDisplayMetrics().density / 2.0f);
            popup.setEpicenterBounds(new Rect(horizontalOffset - n, verticalOffset - n, horizontalOffset + n, verticalOffset + n));
        }
        popup.show();
    }
    
    @Override
    public void dismiss() {
        if (this.isShowing()) {
            this.mPopup.dismiss();
        }
    }
    
    public int getGravity() {
        return this.mDropDownGravity;
    }
    
    @NonNull
    public MenuPopup getPopup() {
        if (this.mPopup == null) {
            this.mPopup = this.createPopup();
        }
        return this.mPopup;
    }
    
    public boolean isShowing() {
        return this.mPopup != null && this.mPopup.isShowing();
    }
    
    protected void onDismiss() {
        this.mPopup = null;
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }
    }
    
    public void setAnchorView(@NonNull final View mAnchorView) {
        this.mAnchorView = mAnchorView;
    }
    
    public void setForceShowIcon(final boolean b) {
        this.mForceShowIcon = b;
        if (this.mPopup != null) {
            this.mPopup.setForceShowIcon(b);
        }
    }
    
    public void setGravity(final int mDropDownGravity) {
        this.mDropDownGravity = mDropDownGravity;
    }
    
    public void setOnDismissListener(@Nullable final PopupWindow$OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }
    
    @Override
    public void setPresenterCallback(@Nullable final MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
        if (this.mPopup != null) {
            this.mPopup.setCallback(callback);
        }
    }
    
    public void show() {
        if (!this.tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }
    
    public void show(final int n, final int n2) {
        if (!this.tryShow(n, n2)) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }
    
    public boolean tryShow() {
        if (this.isShowing()) {
            return true;
        }
        if (this.mAnchorView == null) {
            return false;
        }
        this.showPopup(0, 0, false, false);
        return true;
    }
    
    public boolean tryShow(final int n, final int n2) {
        if (this.isShowing()) {
            return true;
        }
        if (this.mAnchorView == null) {
            return false;
        }
        this.showPopup(n, n2, true, true);
        return true;
    }
}
