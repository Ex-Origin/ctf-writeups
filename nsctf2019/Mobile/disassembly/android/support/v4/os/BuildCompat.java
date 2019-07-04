// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.os.Build$VERSION;

public class BuildCompat
{
    @Deprecated
    public static boolean isAtLeastN() {
        return Build$VERSION.SDK_INT >= 24;
    }
    
    @Deprecated
    public static boolean isAtLeastNMR1() {
        return Build$VERSION.SDK_INT >= 25;
    }
    
    public static boolean isAtLeastO() {
        return Build$VERSION.CODENAME.equals("O") || Build$VERSION.CODENAME.startsWith("ODR") || isAtLeastOMR1();
    }
    
    public static boolean isAtLeastOMR1() {
        return Build$VERSION.CODENAME.startsWith("OMR") || isAtLeastP();
    }
    
    public static boolean isAtLeastP() {
        return Build$VERSION.CODENAME.equals("P");
    }
}
