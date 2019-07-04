// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.drawable;

import android.view.Gravity;
import android.graphics.Rect;
import android.graphics.Outline;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class RoundedBitmapDrawable21 extends RoundedBitmapDrawable
{
    protected RoundedBitmapDrawable21(final Resources resources, final Bitmap bitmap) {
        super(resources, bitmap);
    }
    
    public void getOutline(final Outline outline) {
        this.updateDstRect();
        outline.setRoundRect(this.mDstRect, this.getCornerRadius());
    }
    
    @Override
    void gravityCompatApply(final int n, final int n2, final int n3, final Rect rect, final Rect rect2) {
        Gravity.apply(n, n2, n3, rect, rect2, 0);
    }
    
    @Override
    public boolean hasMipMap() {
        return this.mBitmap != null && this.mBitmap.hasMipMap();
    }
    
    @Override
    public void setMipMap(final boolean hasMipMap) {
        if (this.mBitmap != null) {
            this.mBitmap.setHasMipMap(hasMipMap);
            this.invalidateSelf();
        }
    }
}
