// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.content.DialogInterface$OnKeyListener;
import android.widget.AdapterView$OnItemSelectedListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnMultiChoiceClickListener;
import android.support.annotation.StringRes;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.database.Cursor;
import android.widget.ListAdapter;
import android.view.ContextThemeWrapper;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.support.annotation.RestrictTo;
import android.os.Message;
import android.content.DialogInterface$OnClickListener;
import android.view.KeyEvent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Button;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.support.annotation.Nullable;
import android.content.DialogInterface$OnCancelListener;
import android.support.annotation.StyleRes;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog extends AppCompatDialog implements DialogInterface
{
    static final int LAYOUT_HINT_NONE = 0;
    static final int LAYOUT_HINT_SIDE = 1;
    final AlertController mAlert;
    
    protected AlertDialog(@NonNull final Context context) {
        this(context, 0);
    }
    
    protected AlertDialog(@NonNull final Context context, @StyleRes final int n) {
        super(context, resolveDialogTheme(context, n));
        this.mAlert = new AlertController(this.getContext(), this, this.getWindow());
    }
    
    protected AlertDialog(@NonNull final Context context, final boolean cancelable, @Nullable final DialogInterface$OnCancelListener onCancelListener) {
        this(context, 0);
        this.setCancelable(cancelable);
        this.setOnCancelListener(onCancelListener);
    }
    
    static int resolveDialogTheme(@NonNull final Context context, @StyleRes final int n) {
        if (n >= 16777216) {
            return n;
        }
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
        return typedValue.resourceId;
    }
    
    public Button getButton(final int n) {
        return this.mAlert.getButton(n);
    }
    
    public ListView getListView() {
        return this.mAlert.getListView();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mAlert.installContent();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.mAlert.onKeyDown(n, keyEvent) || super.onKeyDown(n, keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        return this.mAlert.onKeyUp(n, keyEvent) || super.onKeyUp(n, keyEvent);
    }
    
    public void setButton(final int n, final CharSequence charSequence, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        this.mAlert.setButton(n, charSequence, dialogInterface$OnClickListener, null);
    }
    
    public void setButton(final int n, final CharSequence charSequence, final Message message) {
        this.mAlert.setButton(n, charSequence, null, message);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setButtonPanelLayoutHint(final int buttonPanelLayoutHint) {
        this.mAlert.setButtonPanelLayoutHint(buttonPanelLayoutHint);
    }
    
    public void setCustomTitle(final View customTitle) {
        this.mAlert.setCustomTitle(customTitle);
    }
    
    public void setIcon(final int icon) {
        this.mAlert.setIcon(icon);
    }
    
    public void setIcon(final Drawable icon) {
        this.mAlert.setIcon(icon);
    }
    
    public void setIconAttribute(final int n) {
        final TypedValue typedValue = new TypedValue();
        this.getContext().getTheme().resolveAttribute(n, typedValue, true);
        this.mAlert.setIcon(typedValue.resourceId);
    }
    
    public void setMessage(final CharSequence message) {
        this.mAlert.setMessage(message);
    }
    
    @Override
    public void setTitle(final CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mAlert.setTitle(charSequence);
    }
    
    public void setView(final View view) {
        this.mAlert.setView(view);
    }
    
    public void setView(final View view, final int n, final int n2, final int n3, final int n4) {
        this.mAlert.setView(view, n, n2, n3, n4);
    }
    
    public static class Builder
    {
        private final AlertController.AlertParams P;
        private final int mTheme;
        
        public Builder(@NonNull final Context context) {
            this(context, AlertDialog.resolveDialogTheme(context, 0));
        }
        
        public Builder(@NonNull final Context context, @StyleRes final int mTheme) {
            this.P = new AlertController.AlertParams((Context)new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, mTheme)));
            this.mTheme = mTheme;
        }
        
        public AlertDialog create() {
            final AlertDialog alertDialog = new AlertDialog(this.P.mContext, this.mTheme);
            this.P.apply(alertDialog.mAlert);
            alertDialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                alertDialog.setCanceledOnTouchOutside(true);
            }
            alertDialog.setOnCancelListener(this.P.mOnCancelListener);
            alertDialog.setOnDismissListener(this.P.mOnDismissListener);
            if (this.P.mOnKeyListener != null) {
                alertDialog.setOnKeyListener(this.P.mOnKeyListener);
            }
            return alertDialog;
        }
        
        @NonNull
        public Context getContext() {
            return this.P.mContext;
        }
        
        public Builder setAdapter(final ListAdapter mAdapter, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mAdapter = mAdapter;
            this.P.mOnClickListener = mOnClickListener;
            return this;
        }
        
        public Builder setCancelable(final boolean mCancelable) {
            this.P.mCancelable = mCancelable;
            return this;
        }
        
        public Builder setCursor(final Cursor mCursor, final DialogInterface$OnClickListener mOnClickListener, final String mLabelColumn) {
            this.P.mCursor = mCursor;
            this.P.mLabelColumn = mLabelColumn;
            this.P.mOnClickListener = mOnClickListener;
            return this;
        }
        
        public Builder setCustomTitle(@Nullable final View mCustomTitleView) {
            this.P.mCustomTitleView = mCustomTitleView;
            return this;
        }
        
        public Builder setIcon(@DrawableRes final int mIconId) {
            this.P.mIconId = mIconId;
            return this;
        }
        
        public Builder setIcon(@Nullable final Drawable mIcon) {
            this.P.mIcon = mIcon;
            return this;
        }
        
        public Builder setIconAttribute(@AttrRes final int n) {
            final TypedValue typedValue = new TypedValue();
            this.P.mContext.getTheme().resolveAttribute(n, typedValue, true);
            this.P.mIconId = typedValue.resourceId;
            return this;
        }
        
        @Deprecated
        public Builder setInverseBackgroundForced(final boolean mForceInverseBackground) {
            this.P.mForceInverseBackground = mForceInverseBackground;
            return this;
        }
        
        public Builder setItems(@ArrayRes final int n, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(n);
            this.P.mOnClickListener = mOnClickListener;
            return this;
        }
        
        public Builder setItems(final CharSequence[] mItems, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mItems = mItems;
            this.P.mOnClickListener = mOnClickListener;
            return this;
        }
        
        public Builder setMessage(@StringRes final int n) {
            this.P.mMessage = this.P.mContext.getText(n);
            return this;
        }
        
        public Builder setMessage(@Nullable final CharSequence mMessage) {
            this.P.mMessage = mMessage;
            return this;
        }
        
        public Builder setMultiChoiceItems(@ArrayRes final int n, final boolean[] mCheckedItems, final DialogInterface$OnMultiChoiceClickListener mOnCheckboxClickListener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(n);
            this.P.mOnCheckboxClickListener = mOnCheckboxClickListener;
            this.P.mCheckedItems = mCheckedItems;
            this.P.mIsMultiChoice = true;
            return this;
        }
        
        public Builder setMultiChoiceItems(final Cursor mCursor, final String mIsCheckedColumn, final String mLabelColumn, final DialogInterface$OnMultiChoiceClickListener mOnCheckboxClickListener) {
            this.P.mCursor = mCursor;
            this.P.mOnCheckboxClickListener = mOnCheckboxClickListener;
            this.P.mIsCheckedColumn = mIsCheckedColumn;
            this.P.mLabelColumn = mLabelColumn;
            this.P.mIsMultiChoice = true;
            return this;
        }
        
        public Builder setMultiChoiceItems(final CharSequence[] mItems, final boolean[] mCheckedItems, final DialogInterface$OnMultiChoiceClickListener mOnCheckboxClickListener) {
            this.P.mItems = mItems;
            this.P.mOnCheckboxClickListener = mOnCheckboxClickListener;
            this.P.mCheckedItems = mCheckedItems;
            this.P.mIsMultiChoice = true;
            return this;
        }
        
        public Builder setNegativeButton(@StringRes final int n, final DialogInterface$OnClickListener mNegativeButtonListener) {
            this.P.mNegativeButtonText = this.P.mContext.getText(n);
            this.P.mNegativeButtonListener = mNegativeButtonListener;
            return this;
        }
        
        public Builder setNegativeButton(final CharSequence mNegativeButtonText, final DialogInterface$OnClickListener mNegativeButtonListener) {
            this.P.mNegativeButtonText = mNegativeButtonText;
            this.P.mNegativeButtonListener = mNegativeButtonListener;
            return this;
        }
        
        public Builder setNeutralButton(@StringRes final int n, final DialogInterface$OnClickListener mNeutralButtonListener) {
            this.P.mNeutralButtonText = this.P.mContext.getText(n);
            this.P.mNeutralButtonListener = mNeutralButtonListener;
            return this;
        }
        
        public Builder setNeutralButton(final CharSequence mNeutralButtonText, final DialogInterface$OnClickListener mNeutralButtonListener) {
            this.P.mNeutralButtonText = mNeutralButtonText;
            this.P.mNeutralButtonListener = mNeutralButtonListener;
            return this;
        }
        
        public Builder setOnCancelListener(final DialogInterface$OnCancelListener mOnCancelListener) {
            this.P.mOnCancelListener = mOnCancelListener;
            return this;
        }
        
        public Builder setOnDismissListener(final DialogInterface$OnDismissListener mOnDismissListener) {
            this.P.mOnDismissListener = mOnDismissListener;
            return this;
        }
        
        public Builder setOnItemSelectedListener(final AdapterView$OnItemSelectedListener mOnItemSelectedListener) {
            this.P.mOnItemSelectedListener = mOnItemSelectedListener;
            return this;
        }
        
        public Builder setOnKeyListener(final DialogInterface$OnKeyListener mOnKeyListener) {
            this.P.mOnKeyListener = mOnKeyListener;
            return this;
        }
        
        public Builder setPositiveButton(@StringRes final int n, final DialogInterface$OnClickListener mPositiveButtonListener) {
            this.P.mPositiveButtonText = this.P.mContext.getText(n);
            this.P.mPositiveButtonListener = mPositiveButtonListener;
            return this;
        }
        
        public Builder setPositiveButton(final CharSequence mPositiveButtonText, final DialogInterface$OnClickListener mPositiveButtonListener) {
            this.P.mPositiveButtonText = mPositiveButtonText;
            this.P.mPositiveButtonListener = mPositiveButtonListener;
            return this;
        }
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Builder setRecycleOnMeasureEnabled(final boolean mRecycleOnMeasure) {
            this.P.mRecycleOnMeasure = mRecycleOnMeasure;
            return this;
        }
        
        public Builder setSingleChoiceItems(@ArrayRes final int n, final int mCheckedItem, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mItems = this.P.mContext.getResources().getTextArray(n);
            this.P.mOnClickListener = mOnClickListener;
            this.P.mCheckedItem = mCheckedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }
        
        public Builder setSingleChoiceItems(final Cursor mCursor, final int mCheckedItem, final String mLabelColumn, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mCursor = mCursor;
            this.P.mOnClickListener = mOnClickListener;
            this.P.mCheckedItem = mCheckedItem;
            this.P.mLabelColumn = mLabelColumn;
            this.P.mIsSingleChoice = true;
            return this;
        }
        
        public Builder setSingleChoiceItems(final ListAdapter mAdapter, final int mCheckedItem, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mAdapter = mAdapter;
            this.P.mOnClickListener = mOnClickListener;
            this.P.mCheckedItem = mCheckedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }
        
        public Builder setSingleChoiceItems(final CharSequence[] mItems, final int mCheckedItem, final DialogInterface$OnClickListener mOnClickListener) {
            this.P.mItems = mItems;
            this.P.mOnClickListener = mOnClickListener;
            this.P.mCheckedItem = mCheckedItem;
            this.P.mIsSingleChoice = true;
            return this;
        }
        
        public Builder setTitle(@StringRes final int n) {
            this.P.mTitle = this.P.mContext.getText(n);
            return this;
        }
        
        public Builder setTitle(@Nullable final CharSequence mTitle) {
            this.P.mTitle = mTitle;
            return this;
        }
        
        public Builder setView(final int mViewLayoutResId) {
            this.P.mView = null;
            this.P.mViewLayoutResId = mViewLayoutResId;
            this.P.mViewSpacingSpecified = false;
            return this;
        }
        
        public Builder setView(final View mView) {
            this.P.mView = mView;
            this.P.mViewLayoutResId = 0;
            this.P.mViewSpacingSpecified = false;
            return this;
        }
        
        @Deprecated
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public Builder setView(final View mView, final int mViewSpacingLeft, final int mViewSpacingTop, final int mViewSpacingRight, final int mViewSpacingBottom) {
            this.P.mView = mView;
            this.P.mViewLayoutResId = 0;
            this.P.mViewSpacingSpecified = true;
            this.P.mViewSpacingLeft = mViewSpacingLeft;
            this.P.mViewSpacingTop = mViewSpacingTop;
            this.P.mViewSpacingRight = mViewSpacingRight;
            this.P.mViewSpacingBottom = mViewSpacingBottom;
            return this;
        }
        
        public AlertDialog show() {
            final AlertDialog create = this.create();
            create.show();
            return create;
        }
    }
}
