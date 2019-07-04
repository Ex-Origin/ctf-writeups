// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.content.res.TypedArray;
import android.text.Layout;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.widget.TextView;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class DialogTitle extends TextView
{
    public DialogTitle(final Context context) {
        super(context);
    }
    
    public DialogTitle(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public DialogTitle(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        final Layout layout = this.getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0 && layout.getEllipsisCount(lineCount - 1) > 0) {
                this.setSingleLine(false);
                this.setMaxLines(2);
                final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes((AttributeSet)null, R.styleable.TextAppearance, 16842817, 16973892);
                final int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
                if (dimensionPixelSize != 0) {
                    this.setTextSize(0, (float)dimensionPixelSize);
                }
                obtainStyledAttributes.recycle();
                super.onMeasure(n, n2);
            }
        }
    }
}
