// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.print;

import android.support.annotation.RequiresApi;
import java.io.FileNotFoundException;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Build$VERSION;
import android.content.Context;

public final class PrintHelper
{
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    PrintHelperVersionImpl mImpl;
    
    public PrintHelper(final Context context) {
        if (Build$VERSION.SDK_INT >= 24) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi24Impl(context);
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi23Impl(context);
            return;
        }
        if (Build$VERSION.SDK_INT >= 20) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperApi20Impl(context);
            return;
        }
        if (Build$VERSION.SDK_INT >= 19) {
            this.mImpl = (PrintHelperVersionImpl)new PrintHelperKitkatImpl(context);
            return;
        }
        this.mImpl = (PrintHelperVersionImpl)new PrintHelperStubImpl();
    }
    
    public static boolean systemSupportsPrint() {
        return Build$VERSION.SDK_INT >= 19;
    }
    
    public int getColorMode() {
        return this.mImpl.getColorMode();
    }
    
    public int getOrientation() {
        return this.mImpl.getOrientation();
    }
    
    public int getScaleMode() {
        return this.mImpl.getScaleMode();
    }
    
    public void printBitmap(final String s, final Bitmap bitmap) {
        this.mImpl.printBitmap(s, bitmap, null);
    }
    
    public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        this.mImpl.printBitmap(s, bitmap, onPrintFinishCallback);
    }
    
    public void printBitmap(final String s, final Uri uri) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri, null);
    }
    
    public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
        this.mImpl.printBitmap(s, uri, onPrintFinishCallback);
    }
    
    public void setColorMode(final int colorMode) {
        this.mImpl.setColorMode(colorMode);
    }
    
    public void setOrientation(final int orientation) {
        this.mImpl.setOrientation(orientation);
    }
    
    public void setScaleMode(final int scaleMode) {
        this.mImpl.setScaleMode(scaleMode);
    }
    
    public interface OnPrintFinishCallback
    {
        void onFinish();
    }
    
    @RequiresApi(20)
    private static final class PrintHelperApi20Impl extends PrintHelperImpl<PrintHelperApi20>
    {
        PrintHelperApi20Impl(final Context context) {
            super(new PrintHelperApi20(context));
        }
    }
    
    @RequiresApi(23)
    private static final class PrintHelperApi23Impl extends PrintHelperImpl<PrintHelperApi23>
    {
        PrintHelperApi23Impl(final Context context) {
            super(new PrintHelperApi23(context));
        }
    }
    
    @RequiresApi(24)
    private static final class PrintHelperApi24Impl extends PrintHelperImpl<PrintHelperApi24>
    {
        PrintHelperApi24Impl(final Context context) {
            super(new PrintHelperApi24(context));
        }
    }
    
    @RequiresApi(19)
    private static class PrintHelperImpl<RealHelper extends PrintHelperKitkat> implements PrintHelperVersionImpl
    {
        private final RealHelper mPrintHelper;
        
        protected PrintHelperImpl(final RealHelper mPrintHelper) {
            this.mPrintHelper = mPrintHelper;
        }
        
        @Override
        public int getColorMode() {
            return this.mPrintHelper.getColorMode();
        }
        
        @Override
        public int getOrientation() {
            return this.mPrintHelper.getOrientation();
        }
        
        @Override
        public int getScaleMode() {
            return this.mPrintHelper.getScaleMode();
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
            Object o = null;
            if (onPrintFinishCallback != null) {
                o = new PrintHelperKitkat.OnPrintFinishCallback() {
                    @Override
                    public void onFinish() {
                        onPrintFinishCallback.onFinish();
                    }
                };
            }
            this.mPrintHelper.printBitmap(s, bitmap, (PrintHelperKitkat.OnPrintFinishCallback)o);
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) throws FileNotFoundException {
            Object o = null;
            if (onPrintFinishCallback != null) {
                o = new PrintHelperKitkat.OnPrintFinishCallback() {
                    @Override
                    public void onFinish() {
                        onPrintFinishCallback.onFinish();
                    }
                };
            }
            this.mPrintHelper.printBitmap(s, uri, (PrintHelperKitkat.OnPrintFinishCallback)o);
        }
        
        @Override
        public void setColorMode(final int colorMode) {
            this.mPrintHelper.setColorMode(colorMode);
        }
        
        @Override
        public void setOrientation(final int orientation) {
            this.mPrintHelper.setOrientation(orientation);
        }
        
        @Override
        public void setScaleMode(final int scaleMode) {
            this.mPrintHelper.setScaleMode(scaleMode);
        }
    }
    
    @RequiresApi(19)
    private static final class PrintHelperKitkatImpl extends PrintHelperImpl<PrintHelperKitkat>
    {
        PrintHelperKitkatImpl(final Context context) {
            super(new PrintHelperKitkat(context));
        }
    }
    
    private static final class PrintHelperStubImpl implements PrintHelperVersionImpl
    {
        int mColorMode;
        int mOrientation;
        int mScaleMode;
        
        private PrintHelperStubImpl() {
            this.mScaleMode = 2;
            this.mColorMode = 2;
            this.mOrientation = 1;
        }
        
        @Override
        public int getColorMode() {
            return this.mColorMode;
        }
        
        @Override
        public int getOrientation() {
            return this.mOrientation;
        }
        
        @Override
        public int getScaleMode() {
            return this.mScaleMode;
        }
        
        @Override
        public void printBitmap(final String s, final Bitmap bitmap, final OnPrintFinishCallback onPrintFinishCallback) {
        }
        
        @Override
        public void printBitmap(final String s, final Uri uri, final OnPrintFinishCallback onPrintFinishCallback) {
        }
        
        @Override
        public void setColorMode(final int mColorMode) {
            this.mColorMode = mColorMode;
        }
        
        @Override
        public void setOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void setScaleMode(final int mScaleMode) {
            this.mScaleMode = mScaleMode;
        }
    }
    
    interface PrintHelperVersionImpl
    {
        int getColorMode();
        
        int getOrientation();
        
        int getScaleMode();
        
        void printBitmap(final String p0, final Bitmap p1, final OnPrintFinishCallback p2);
        
        void printBitmap(final String p0, final Uri p1, final OnPrintFinishCallback p2) throws FileNotFoundException;
        
        void setColorMode(final int p0);
        
        void setOrientation(final int p0);
        
        void setScaleMode(final int p0);
    }
}
