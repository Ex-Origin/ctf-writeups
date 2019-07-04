// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.graphics.Canvas;
import android.view.View$MeasureSpec;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;

public class Space extends View
{
    public Space(final Context context) {
        this(context, null);
    }
    
    public Space(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public Space(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        if (this.getVisibility() == 0) {
            this.setVisibility(4);
        }
    }
    
    private static int getDefaultSize2(final int n, int size) {
        final int mode = View$MeasureSpec.getMode(size);
        size = View$MeasureSpec.getSize(size);
        switch (mode) {
            default: {
                return n;
            }
            case 0: {
                return n;
            }
            case Integer.MIN_VALUE: {
                return Math.min(n, size);
            }
            case 1073741824: {
                return size;
            }
        }
    }
    
    public void draw(final Canvas canvas) {
    }
    
    protected void onMeasure(final int n, final int n2) {
        this.setMeasuredDimension(getDefaultSize2(this.getSuggestedMinimumWidth(), n), getDefaultSize2(this.getSuggestedMinimumHeight(), n2));
    }
}
