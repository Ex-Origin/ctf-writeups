// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.text;

import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import java.util.Locale;
import android.os.Build$VERSION;

public final class ICUCompat
{
    private static final ICUCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (ICUCompatBaseImpl)new ICUCompatApi21Impl();
            return;
        }
        IMPL = new ICUCompatBaseImpl();
    }
    
    @Nullable
    public static String maximizeAndGetScript(final Locale locale) {
        return ICUCompat.IMPL.maximizeAndGetScript(locale);
    }
    
    @RequiresApi(21)
    static class ICUCompatApi21Impl extends ICUCompatBaseImpl
    {
        @Override
        public String maximizeAndGetScript(final Locale locale) {
            return ICUCompatApi21.maximizeAndGetScript(locale);
        }
    }
    
    static class ICUCompatBaseImpl
    {
        public String maximizeAndGetScript(final Locale locale) {
            return ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }
}
