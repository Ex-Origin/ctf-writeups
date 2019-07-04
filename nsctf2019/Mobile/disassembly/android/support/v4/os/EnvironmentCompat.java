// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import java.io.IOException;
import android.util.Log;
import android.os.Environment;
import android.os.Build$VERSION;
import java.io.File;

public final class EnvironmentCompat
{
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";
    
    public static String getStorageState(final File file) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Environment.getStorageState(file);
        }
        try {
            if (file.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                return Environment.getExternalStorageState();
            }
        }
        catch (IOException ex) {
            Log.w("EnvironmentCompat", "Failed to resolve canonical path: " + ex);
        }
        return "unknown";
    }
}
