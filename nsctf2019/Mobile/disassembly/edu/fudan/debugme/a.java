// 
// Decompiled by Procyon v0.5.30
// 

package edu.fudan.debugme;

import java.security.MessageDigest;
import android.content.pm.PackageInfo;
import android.content.Context;
import android.app.Activity;

public class a
{
    public static boolean a(final Activity activity) {
        return !b((Context)activity).equals("74321e83dbd0e36c8be1e4fa20b024d9");
    }
    
    public static String b(final Context context) {
        return d(context, c(context));
    }
    
    public static String c(final Context context) {
        return context.getApplicationInfo().packageName;
    }
    
    public static String d(final Context context, final String s) {
        final byte[] array = null;
        try {
            final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(s, 64);
            int i = 0;
            byte[] byteArray = array;
            while (i < packageInfo.signatures.length) {
                byteArray = packageInfo.signatures[i].toByteArray();
                if (byteArray != null) {
                    break;
                }
                ++i;
            }
            return e(byteArray);
        }
        catch (Exception ex) {
            return "";
        }
    }
    
    public static String e(String e) {
        try {
            e = e(e.getBytes());
            return e;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static String e(byte[] digest) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("md5");
            instance.update(digest);
            digest = instance.digest();
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; ++i) {
                final String hexString = Integer.toHexString(digest[i] & 0xFF);
                if (hexString.length() < 2) {
                    sb.append(0);
                }
                sb.append(hexString);
            }
            return sb.toString();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
