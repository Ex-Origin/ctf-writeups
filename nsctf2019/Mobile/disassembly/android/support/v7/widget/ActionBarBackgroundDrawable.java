// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.graphics.drawable.Drawable;

@RequiresApi(9)
class ActionBarBackgroundDrawable extends Drawable
{
    final ActionBarContainer mContainer;
    
    public ActionBarBackgroundDrawable(final ActionBarContainer mContainer) {
        this.mContainer = mContainer;
    }
    
    public void draw(final Canvas canvas) {
        if (this.mContainer.mIsSplit) {
            if (this.mContainer.mSplitBackground != null) {
                this.mContainer.mSplitBackground.draw(canvas);
            }
        }
        else {
            if (this.mContainer.mBackground != null) {
                this.mContainer.mBackground.draw(canvas);
            }
            if (this.mContainer.mStackedBackground != null && this.mContainer.mIsStacked) {
                this.mContainer.mStackedBackground.draw(canvas);
            }
        }
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
