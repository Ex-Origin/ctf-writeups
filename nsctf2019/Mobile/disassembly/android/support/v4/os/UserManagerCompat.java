// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.os.UserManager;
import android.os.Build$VERSION;
import android.content.Context;

public class UserManagerCompat
{
    public static boolean isUserUnlocked(final Context context) {
        return Build$VERSION.SDK_INT < 24 || ((UserManager)context.getSystemService((Class)UserManager.class)).isUserUnlocked();
    }
}
