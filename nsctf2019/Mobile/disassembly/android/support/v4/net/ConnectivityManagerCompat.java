// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.net;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.support.annotation.RequiresApi;
import android.net.NetworkInfo;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build$VERSION;

public final class ConnectivityManagerCompat
{
    private static final ConnectivityManagerCompatImpl IMPL;
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
    
    static {
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (ConnectivityManagerCompatImpl)new ConnectivityManagerCompatApi24Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (ConnectivityManagerCompatImpl)new ConnectivityManagerCompatApi16Impl();
            return;
        }
        IMPL = (ConnectivityManagerCompatImpl)new ConnectivityManagerCompatBaseImpl();
    }
    
    public static NetworkInfo getNetworkInfoFromBroadcast(final ConnectivityManager connectivityManager, final Intent intent) {
        final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
        if (networkInfo != null) {
            return connectivityManager.getNetworkInfo(networkInfo.getType());
        }
        return null;
    }
    
    public static int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
        return ConnectivityManagerCompat.IMPL.getRestrictBackgroundStatus(connectivityManager);
    }
    
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        return ConnectivityManagerCompat.IMPL.isActiveNetworkMetered(connectivityManager);
    }
    
    @RequiresApi(16)
    static class ConnectivityManagerCompatApi16Impl extends ConnectivityManagerCompatBaseImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            return connectivityManager.isActiveNetworkMetered();
        }
    }
    
    @RequiresApi(24)
    static class ConnectivityManagerCompatApi24Impl extends ConnectivityManagerCompatApi16Impl
    {
        @Override
        public int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
    }
    
    static class ConnectivityManagerCompatBaseImpl implements ConnectivityManagerCompatImpl
    {
        @Override
        public int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
            return 3;
        }
        
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                switch (activeNetworkInfo.getType()) {
                    case 0:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6: {
                        break;
                    }
                    default: {
                        return true;
                    }
                    case 1:
                    case 7:
                    case 9: {
                        return false;
                    }
                }
            }
            return true;
        }
    }
    
    interface ConnectivityManagerCompatImpl
    {
        int getRestrictBackgroundStatus(final ConnectivityManager p0);
        
        boolean isActiveNetworkMetered(final ConnectivityManager p0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface RestrictBackgroundStatus {
    }
}
