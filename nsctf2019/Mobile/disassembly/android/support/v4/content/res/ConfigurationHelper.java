// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.content.res;

import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.res.Resources;

public final class ConfigurationHelper
{
    public static int getDensityDpi(@NonNull final Resources resources) {
        if (Build$VERSION.SDK_INT >= 17) {
            return resources.getConfiguration().densityDpi;
        }
        return resources.getDisplayMetrics().densityDpi;
    }
    
    @Deprecated
    public static int getScreenHeightDp(@NonNull final Resources resources) {
        return resources.getConfiguration().screenHeightDp;
    }
    
    @Deprecated
    public static int getScreenWidthDp(@NonNull final Resources resources) {
        return resources.getConfiguration().screenWidthDp;
    }
    
    @Deprecated
    public static int getSmallestScreenWidthDp(@NonNull final Resources resources) {
        return resources.getConfiguration().smallestScreenWidthDp;
    }
}
