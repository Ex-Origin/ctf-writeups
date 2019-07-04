// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.view.MotionEvent;
import android.os.Parcelable;
import android.view.View$MeasureSpec;
import android.view.View;
import android.support.v7.widget.TooltipCompat;
import android.text.TextUtils;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.content.res.Resources;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ForwardingListener;
import android.support.annotation.RestrictTo;
import android.support.v7.widget.ActionMenuView;
import android.view.View$OnClickListener;
import android.support.v7.widget.AppCompatTextView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ActionMenuItemView extends AppCompatTextView implements ItemView, View$OnClickListener, ActionMenuChildView
{
    private static final int MAX_ICON_SIZE = 32;
    private static final String TAG = "ActionMenuItemView";
    private boolean mAllowTextWithIcon;
    private boolean mExpandedFormat;
    private ForwardingListener mForwardingListener;
    private Drawable mIcon;
    MenuItemImpl mItemData;
    ItemInvoker mItemInvoker;
    private int mMaxIconSize;
    private int mMinWidth;
    PopupCallback mPopupCallback;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;
    
    public ActionMenuItemView(final Context context) {
        this(context, null);
    }
    
    public ActionMenuItemView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public ActionMenuItemView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        final Resources resources = context.getResources();
        this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ActionMenuItemView, n, 0);
        this.mMinWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ActionMenuItemView_android_minWidth, 0);
        obtainStyledAttributes.recycle();
        this.mMaxIconSize = (int)(32.0f * resources.getDisplayMetrics().density + 0.5f);
        this.setOnClickListener((View$OnClickListener)this);
        this.mSavedPaddingLeft = -1;
        this.setSaveEnabled(false);
    }
    
    private boolean shouldAllowTextWithIcon() {
        final Configuration configuration = this.getContext().getResources().getConfiguration();
        final int screenWidthDp = configuration.screenWidthDp;
        final int screenHeightDp = configuration.screenHeightDp;
        return screenWidthDp >= 480 || (screenWidthDp >= 640 && screenHeightDp >= 480) || configuration.orientation == 2;
    }
    
    private void updateTextButtonVisibility() {
        final CharSequence charSequence = null;
        final boolean b = false;
        final boolean b2 = !TextUtils.isEmpty(this.mTitle) && true;
        boolean b3 = false;
        Label_0054: {
            if (this.mIcon != null) {
                b3 = b;
                if (!this.mItemData.showsTextAsAction()) {
                    break Label_0054;
                }
                if (!this.mAllowTextWithIcon) {
                    b3 = b;
                    if (!this.mExpandedFormat) {
                        break Label_0054;
                    }
                }
            }
            b3 = true;
        }
        final boolean b4 = b2 & b3;
        CharSequence mTitle;
        if (b4) {
            mTitle = this.mTitle;
        }
        else {
            mTitle = null;
        }
        this.setText(mTitle);
        final CharSequence contentDescription = this.mItemData.getContentDescription();
        if (TextUtils.isEmpty(contentDescription)) {
            this.setContentDescription(this.mItemData.getTitle());
        }
        else {
            this.setContentDescription(contentDescription);
        }
        final CharSequence tooltipText = this.mItemData.getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            CharSequence title;
            if (b4) {
                title = charSequence;
            }
            else {
                title = this.mItemData.getTitle();
            }
            TooltipCompat.setTooltipText((View)this, title);
            return;
        }
        TooltipCompat.setTooltipText((View)this, tooltipText);
    }
    
    @Override
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }
    
    public boolean hasText() {
        return !TextUtils.isEmpty(this.getText());
    }
    
    @Override
    public void initialize(final MenuItemImpl mItemData, int visibility) {
        this.mItemData = mItemData;
        this.setIcon(mItemData.getIcon());
        this.setTitle(mItemData.getTitleForItemView(this));
        this.setId(mItemData.getItemId());
        if (mItemData.isVisible()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        this.setVisibility(visibility);
        ((MenuView.ItemView)this).setEnabled(mItemData.isEnabled());
        if (mItemData.hasSubMenu() && this.mForwardingListener == null) {
            this.mForwardingListener = new ActionMenuItemForwardingListener();
        }
    }
    
    public boolean needsDividerAfter() {
        return this.hasText();
    }
    
    public boolean needsDividerBefore() {
        return this.hasText() && this.mItemData.getIcon() == null;
    }
    
    public void onClick(final View view) {
        if (this.mItemInvoker != null) {
            this.mItemInvoker.invokeItem(this.mItemData);
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mAllowTextWithIcon = this.shouldAllowTextWithIcon();
        this.updateTextButtonVisibility();
    }
    
    protected void onMeasure(int n, final int n2) {
        final boolean hasText = this.hasText();
        if (hasText && this.mSavedPaddingLeft >= 0) {
            super.setPadding(this.mSavedPaddingLeft, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
        super.onMeasure(n, n2);
        final int mode = View$MeasureSpec.getMode(n);
        n = View$MeasureSpec.getSize(n);
        final int measuredWidth = this.getMeasuredWidth();
        if (mode == Integer.MIN_VALUE) {
            n = Math.min(n, this.mMinWidth);
        }
        else {
            n = this.mMinWidth;
        }
        if (mode != 1073741824 && this.mMinWidth > 0 && measuredWidth < n) {
            super.onMeasure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), n2);
        }
        if (!hasText && this.mIcon != null) {
            super.setPadding((this.getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, this.getPaddingTop(), this.getPaddingRight(), this.getPaddingBottom());
        }
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        super.onRestoreInstanceState((Parcelable)null);
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return (this.mItemData.hasSubMenu() && this.mForwardingListener != null && this.mForwardingListener.onTouch((View)this, motionEvent)) || super.onTouchEvent(motionEvent);
    }
    
    @Override
    public boolean prefersCondensedTitle() {
        return true;
    }
    
    @Override
    public void setCheckable(final boolean b) {
    }
    
    @Override
    public void setChecked(final boolean b) {
    }
    
    public void setExpandedFormat(final boolean mExpandedFormat) {
        if (this.mExpandedFormat != mExpandedFormat) {
            this.mExpandedFormat = mExpandedFormat;
            if (this.mItemData != null) {
                this.mItemData.actionFormatChanged();
            }
        }
    }
    
    @Override
    public void setIcon(final Drawable mIcon) {
        this.mIcon = mIcon;
        if (mIcon != null) {
            final int intrinsicWidth = mIcon.getIntrinsicWidth();
            int intrinsicHeight;
            final int n = intrinsicHeight = mIcon.getIntrinsicHeight();
            int mMaxIconSize;
            if ((mMaxIconSize = intrinsicWidth) > this.mMaxIconSize) {
                final float n2 = this.mMaxIconSize / intrinsicWidth;
                mMaxIconSize = this.mMaxIconSize;
                intrinsicHeight = (int)(n * n2);
            }
            int mMaxIconSize2 = intrinsicHeight;
            int n3 = mMaxIconSize;
            if (intrinsicHeight > this.mMaxIconSize) {
                final float n4 = this.mMaxIconSize / intrinsicHeight;
                mMaxIconSize2 = this.mMaxIconSize;
                n3 = (int)(mMaxIconSize * n4);
            }
            mIcon.setBounds(0, 0, n3, mMaxIconSize2);
        }
        this.setCompoundDrawables(mIcon, (Drawable)null, (Drawable)null, (Drawable)null);
        this.updateTextButtonVisibility();
    }
    
    public void setItemInvoker(final ItemInvoker mItemInvoker) {
        this.mItemInvoker = mItemInvoker;
    }
    
    public void setPadding(final int mSavedPaddingLeft, final int n, final int n2, final int n3) {
        super.setPadding(this.mSavedPaddingLeft = mSavedPaddingLeft, n, n2, n3);
    }
    
    public void setPopupCallback(final PopupCallback mPopupCallback) {
        this.mPopupCallback = mPopupCallback;
    }
    
    @Override
    public void setShortcut(final boolean b, final char c) {
    }
    
    @Override
    public void setTitle(final CharSequence mTitle) {
        this.mTitle = mTitle;
        this.updateTextButtonVisibility();
    }
    
    @Override
    public boolean showsIcon() {
        return true;
    }
    
    private class ActionMenuItemForwardingListener extends ForwardingListener
    {
        public ActionMenuItemForwardingListener() {
            super((View)ActionMenuItemView.this);
        }
        
        @Override
        public ShowableListMenu getPopup() {
            if (ActionMenuItemView.this.mPopupCallback != null) {
                return ActionMenuItemView.this.mPopupCallback.getPopup();
            }
            return null;
        }
        
        @Override
        protected boolean onForwardingStarted() {
            boolean b2;
            final boolean b = b2 = false;
            if (ActionMenuItemView.this.mItemInvoker != null) {
                b2 = b;
                if (ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
                    final ShowableListMenu popup = this.getPopup();
                    b2 = b;
                    if (popup != null) {
                        b2 = b;
                        if (popup.isShowing()) {
                            b2 = true;
                        }
                    }
                }
            }
            return b2;
        }
    }
    
    public abstract static class PopupCallback
    {
        public abstract ShowableListMenu getPopup();
    }
}
