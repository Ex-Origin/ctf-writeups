// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.content.Context;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.support.annotation.RequiresApi;

@RequiresApi(17)
class AppCompatTextHelperV17 extends AppCompatTextHelper
{
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableStartTint;
    
    AppCompatTextHelperV17(final TextView textView) {
        super(textView);
    }
    
    @Override
    void applyCompoundDrawablesTints() {
        super.applyCompoundDrawablesTints();
        if (this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
            final Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
        }
    }
    
    @Override
    void loadFromAttributes(final AttributeSet set, final int n) {
        super.loadFromAttributes(set, n);
        final Context context = this.mView.getContext();
        final AppCompatDrawableManager value = AppCompatDrawableManager.get();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.AppCompatTextHelper, n, 0);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
            this.mDrawableStartTint = AppCompatTextHelper.createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
            this.mDrawableEndTint = AppCompatTextHelper.createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
        }
        obtainStyledAttributes.recycle();
    }
}
