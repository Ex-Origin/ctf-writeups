// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.print;

import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(20)
class PrintHelperApi20 extends PrintHelperKitkat
{
    PrintHelperApi20(final Context context) {
        super(context);
        this.mPrintActivityRespectsOrientation = false;
    }
}
