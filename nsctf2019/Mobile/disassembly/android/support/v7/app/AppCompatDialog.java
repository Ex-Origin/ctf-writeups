// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.support.annotation.LayoutRes;
import android.support.v7.view.ActionMode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.support.annotation.IdRes;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.content.DialogInterface$OnCancelListener;
import android.os.Bundle;
import android.content.Context;
import android.app.Dialog;

public class AppCompatDialog extends Dialog implements AppCompatCallback
{
    private AppCompatDelegate mDelegate;
    
    public AppCompatDialog(final Context context) {
        this(context, 0);
    }
    
    public AppCompatDialog(final Context context, final int n) {
        super(context, getThemeResId(context, n));
        this.getDelegate().onCreate(null);
        this.getDelegate().applyDayNight();
    }
    
    protected AppCompatDialog(final Context context, final boolean b, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        super(context, b, dialogInterface$OnCancelListener);
    }
    
    private static int getThemeResId(final Context context, final int n) {
        int resourceId = n;
        if (n == 0) {
            final TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.dialogTheme, typedValue, true);
            resourceId = typedValue.resourceId;
        }
        return resourceId;
    }
    
    public void addContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.getDelegate().addContentView(view, viewGroup$LayoutParams);
    }
    
    @Nullable
    public <T extends View> T findViewById(@IdRes final int n) {
        return this.getDelegate().findViewById(n);
    }
    
    public AppCompatDelegate getDelegate() {
        if (this.mDelegate == null) {
            this.mDelegate = AppCompatDelegate.create(this, this);
        }
        return this.mDelegate;
    }
    
    public ActionBar getSupportActionBar() {
        return this.getDelegate().getSupportActionBar();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void invalidateOptionsMenu() {
        this.getDelegate().invalidateOptionsMenu();
    }
    
    protected void onCreate(final Bundle bundle) {
        this.getDelegate().installViewFactory();
        super.onCreate(bundle);
        this.getDelegate().onCreate(bundle);
    }
    
    protected void onStop() {
        super.onStop();
        this.getDelegate().onStop();
    }
    
    public void onSupportActionModeFinished(final ActionMode actionMode) {
    }
    
    public void onSupportActionModeStarted(final ActionMode actionMode) {
    }
    
    @Nullable
    public ActionMode onWindowStartingSupportActionMode(final ActionMode.Callback callback) {
        return null;
    }
    
    public void setContentView(@LayoutRes final int contentView) {
        this.getDelegate().setContentView(contentView);
    }
    
    public void setContentView(final View contentView) {
        this.getDelegate().setContentView(contentView);
    }
    
    public void setContentView(final View view, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        this.getDelegate().setContentView(view, viewGroup$LayoutParams);
    }
    
    public void setTitle(final int title) {
        super.setTitle(title);
        this.getDelegate().setTitle(this.getContext().getString(title));
    }
    
    public void setTitle(final CharSequence charSequence) {
        super.setTitle(charSequence);
        this.getDelegate().setTitle(charSequence);
    }
    
    public boolean supportRequestWindowFeature(final int n) {
        return this.getDelegate().requestWindowFeature(n);
    }
}
