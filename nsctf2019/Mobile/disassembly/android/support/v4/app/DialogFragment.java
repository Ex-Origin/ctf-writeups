// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.annotation.RestrictTo;
import android.view.LayoutInflater;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.content.DialogInterface;
import android.content.Context;
import android.view.View;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.app.Dialog;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;

public class DialogFragment extends Fragment implements DialogInterface$OnCancelListener, DialogInterface$OnDismissListener
{
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    public static final int STYLE_NO_TITLE = 1;
    int mBackStackId;
    boolean mCancelable;
    Dialog mDialog;
    boolean mDismissed;
    boolean mShownByMe;
    boolean mShowsDialog;
    int mStyle;
    int mTheme;
    boolean mViewDestroyed;
    
    public DialogFragment() {
        this.mStyle = 0;
        this.mTheme = 0;
        this.mCancelable = true;
        this.mShowsDialog = true;
        this.mBackStackId = -1;
    }
    
    public void dismiss() {
        this.dismissInternal(false);
    }
    
    public void dismissAllowingStateLoss() {
        this.dismissInternal(true);
    }
    
    void dismissInternal(final boolean b) {
        if (this.mDismissed) {
            return;
        }
        this.mDismissed = true;
        this.mShownByMe = false;
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
        this.mViewDestroyed = true;
        if (this.mBackStackId >= 0) {
            this.getFragmentManager().popBackStack(this.mBackStackId, 1);
            this.mBackStackId = -1;
            return;
        }
        final FragmentTransaction beginTransaction = this.getFragmentManager().beginTransaction();
        beginTransaction.remove(this);
        if (b) {
            beginTransaction.commitAllowingStateLoss();
            return;
        }
        beginTransaction.commit();
    }
    
    public Dialog getDialog() {
        return this.mDialog;
    }
    
    public boolean getShowsDialog() {
        return this.mShowsDialog;
    }
    
    @StyleRes
    public int getTheme() {
        return this.mTheme;
    }
    
    public boolean isCancelable() {
        return this.mCancelable;
    }
    
    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mShowsDialog) {
            final View view = this.getView();
            if (view != null) {
                if (view.getParent() != null) {
                    throw new IllegalStateException("DialogFragment can not be attached to a container view");
                }
                this.mDialog.setContentView(view);
            }
            final FragmentActivity activity = this.getActivity();
            if (activity != null) {
                this.mDialog.setOwnerActivity((Activity)activity);
            }
            this.mDialog.setCancelable(this.mCancelable);
            this.mDialog.setOnCancelListener((DialogInterface$OnCancelListener)this);
            this.mDialog.setOnDismissListener((DialogInterface$OnDismissListener)this);
            if (bundle != null) {
                bundle = bundle.getBundle("android:savedDialogState");
                if (bundle != null) {
                    this.mDialog.onRestoreInstanceState(bundle);
                }
            }
        }
    }
    
    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (!this.mShownByMe) {
            this.mDismissed = false;
        }
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
    }
    
    @Override
    public void onCreate(@Nullable final Bundle bundle) {
        super.onCreate(bundle);
        this.mShowsDialog = (this.mContainerId == 0);
        if (bundle != null) {
            this.mStyle = bundle.getInt("android:style", 0);
            this.mTheme = bundle.getInt("android:theme", 0);
            this.mCancelable = bundle.getBoolean("android:cancelable", true);
            this.mShowsDialog = bundle.getBoolean("android:showsDialog", this.mShowsDialog);
            this.mBackStackId = bundle.getInt("android:backStackId", -1);
        }
    }
    
    @NonNull
    public Dialog onCreateDialog(final Bundle bundle) {
        return new Dialog((Context)this.getActivity(), this.getTheme());
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mDialog != null) {
            this.mViewDestroyed = true;
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
        }
    }
    
    public void onDismiss(final DialogInterface dialogInterface) {
        if (!this.mViewDestroyed) {
            this.dismissInternal(true);
        }
    }
    
    @Override
    public LayoutInflater onGetLayoutInflater(final Bundle bundle) {
        if (!this.mShowsDialog) {
            return super.onGetLayoutInflater(bundle);
        }
        this.mDialog = this.onCreateDialog(bundle);
        if (this.mDialog != null) {
            this.setupDialog(this.mDialog, this.mStyle);
            return (LayoutInflater)this.mDialog.getContext().getSystemService("layout_inflater");
        }
        return (LayoutInflater)this.mHost.getContext().getSystemService("layout_inflater");
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (this.mDialog != null) {
            final Bundle onSaveInstanceState = this.mDialog.onSaveInstanceState();
            if (onSaveInstanceState != null) {
                bundle.putBundle("android:savedDialogState", onSaveInstanceState);
            }
        }
        if (this.mStyle != 0) {
            bundle.putInt("android:style", this.mStyle);
        }
        if (this.mTheme != 0) {
            bundle.putInt("android:theme", this.mTheme);
        }
        if (!this.mCancelable) {
            bundle.putBoolean("android:cancelable", this.mCancelable);
        }
        if (!this.mShowsDialog) {
            bundle.putBoolean("android:showsDialog", this.mShowsDialog);
        }
        if (this.mBackStackId != -1) {
            bundle.putInt("android:backStackId", this.mBackStackId);
        }
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (this.mDialog != null) {
            this.mViewDestroyed = false;
            this.mDialog.show();
        }
    }
    
    @Override
    public void onStop() {
        super.onStop();
        if (this.mDialog != null) {
            this.mDialog.hide();
        }
    }
    
    public void setCancelable(final boolean b) {
        this.mCancelable = b;
        if (this.mDialog != null) {
            this.mDialog.setCancelable(b);
        }
    }
    
    public void setShowsDialog(final boolean mShowsDialog) {
        this.mShowsDialog = mShowsDialog;
    }
    
    public void setStyle(final int mStyle, @StyleRes final int mTheme) {
        this.mStyle = mStyle;
        if (this.mStyle == 2 || this.mStyle == 3) {
            this.mTheme = 16973913;
        }
        if (mTheme != 0) {
            this.mTheme = mTheme;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setupDialog(final Dialog dialog, final int n) {
        switch (n) {
            default: {}
            case 3: {
                dialog.getWindow().addFlags(24);
            }
            case 1:
            case 2: {
                dialog.requestWindowFeature(1);
            }
        }
    }
    
    public int show(final FragmentTransaction fragmentTransaction, final String s) {
        this.mDismissed = false;
        this.mShownByMe = true;
        fragmentTransaction.add(this, s);
        this.mViewDestroyed = false;
        return this.mBackStackId = fragmentTransaction.commit();
    }
    
    public void show(final FragmentManager fragmentManager, final String s) {
        this.mDismissed = false;
        this.mShownByMe = true;
        final FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.add(this, s);
        beginTransaction.commit();
    }
}
