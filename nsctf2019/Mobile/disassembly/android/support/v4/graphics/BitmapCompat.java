// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics;

import android.support.annotation.RequiresApi;
import android.graphics.Bitmap;
import android.os.Build$VERSION;

public final class BitmapCompat
{
    static final BitmapCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 19) {
            IMPL = (BitmapCompatBaseImpl)new BitmapCompatApi19Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = (BitmapCompatBaseImpl)new BitmapCompatApi18Impl();
            return;
        }
        IMPL = new BitmapCompatBaseImpl();
    }
    
    public static int getAllocationByteCount(final Bitmap bitmap) {
        return BitmapCompat.IMPL.getAllocationByteCount(bitmap);
    }
    
    public static boolean hasMipMap(final Bitmap bitmap) {
        return BitmapCompat.IMPL.hasMipMap(bitmap);
    }
    
    public static void setHasMipMap(final Bitmap bitmap, final boolean b) {
        BitmapCompat.IMPL.setHasMipMap(bitmap, b);
    }
    
    @RequiresApi(18)
    static class BitmapCompatApi18Impl extends BitmapCompatBaseImpl
    {
        @Override
        public boolean hasMipMap(final Bitmap bitmap) {
            return bitmap.hasMipMap();
        }
        
        @Override
        public void setHasMipMap(final Bitmap bitmap, final boolean hasMipMap) {
            bitmap.setHasMipMap(hasMipMap);
        }
    }
    
    @RequiresApi(19)
    static class BitmapCompatApi19Impl extends BitmapCompatApi18Impl
    {
        @Override
        public int getAllocationByteCount(final Bitmap bitmap) {
            return bitmap.getAllocationByteCount();
        }
    }
    
    static class BitmapCompatBaseImpl
    {
        public int getAllocationByteCount(final Bitmap bitmap) {
            return bitmap.getByteCount();
        }
        
        public boolean hasMipMap(final Bitmap bitmap) {
            return false;
        }
        
        public void setHasMipMap(final Bitmap bitmap, final boolean b) {
        }
    }
}
