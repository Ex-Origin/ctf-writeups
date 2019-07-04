// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.text;

import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import android.util.Log;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class ICUCompatIcs
{
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;
    
    static {
        try {
            final Class<?> forName = Class.forName("libcore.icu.ICU");
            if (forName != null) {
                ICUCompatIcs.sGetScriptMethod = forName.getMethod("getScript", String.class);
                ICUCompatIcs.sAddLikelySubtagsMethod = forName.getMethod("addLikelySubtags", String.class);
            }
        }
        catch (Exception ex) {
            ICUCompatIcs.sGetScriptMethod = null;
            ICUCompatIcs.sAddLikelySubtagsMethod = null;
            Log.w("ICUCompatIcs", (Throwable)ex);
        }
    }
    
    private static String addLikelySubtags(Locale string) {
        string = (Locale)string.toString();
        try {
            if (ICUCompatIcs.sAddLikelySubtagsMethod != null) {
                return (String)ICUCompatIcs.sAddLikelySubtagsMethod.invoke(null, string);
            }
            goto Label_0040;
        }
        catch (IllegalAccessException ex) {
            Log.w("ICUCompatIcs", (Throwable)ex);
        }
        catch (InvocationTargetException ex2) {
            Log.w("ICUCompatIcs", (Throwable)ex2);
            goto Label_0040;
        }
    }
    
    private static String getScript(String s) {
        try {
            if (ICUCompatIcs.sGetScriptMethod != null) {
                s = (String)ICUCompatIcs.sGetScriptMethod.invoke(null, s);
                return s;
            }
            goto Label_0035;
        }
        catch (IllegalAccessException ex) {
            Log.w("ICUCompatIcs", (Throwable)ex);
        }
        catch (InvocationTargetException ex2) {
            Log.w("ICUCompatIcs", (Throwable)ex2);
            goto Label_0035;
        }
    }
    
    public static String maximizeAndGetScript(final Locale locale) {
        final String addLikelySubtags = addLikelySubtags(locale);
        if (addLikelySubtags != null) {
            return getScript(addLikelySubtags);
        }
        return null;
    }
}
