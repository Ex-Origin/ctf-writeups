// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.media.MediaDescription$Builder;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.media.MediaDescription;
import android.os.Parcel;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class MediaDescriptionCompatApi21
{
    public static Object fromParcel(final Parcel parcel) {
        return MediaDescription.CREATOR.createFromParcel(parcel);
    }
    
    public static CharSequence getDescription(final Object o) {
        return ((MediaDescription)o).getDescription();
    }
    
    public static Bundle getExtras(final Object o) {
        return ((MediaDescription)o).getExtras();
    }
    
    public static Bitmap getIconBitmap(final Object o) {
        return ((MediaDescription)o).getIconBitmap();
    }
    
    public static Uri getIconUri(final Object o) {
        return ((MediaDescription)o).getIconUri();
    }
    
    public static String getMediaId(final Object o) {
        return ((MediaDescription)o).getMediaId();
    }
    
    public static CharSequence getSubtitle(final Object o) {
        return ((MediaDescription)o).getSubtitle();
    }
    
    public static CharSequence getTitle(final Object o) {
        return ((MediaDescription)o).getTitle();
    }
    
    public static void writeToParcel(final Object o, final Parcel parcel, final int n) {
        ((MediaDescription)o).writeToParcel(parcel, n);
    }
    
    static class Builder
    {
        public static Object build(final Object o) {
            return ((MediaDescription$Builder)o).build();
        }
        
        public static Object newInstance() {
            return new MediaDescription$Builder();
        }
        
        public static void setDescription(final Object o, final CharSequence description) {
            ((MediaDescription$Builder)o).setDescription(description);
        }
        
        public static void setExtras(final Object o, final Bundle extras) {
            ((MediaDescription$Builder)o).setExtras(extras);
        }
        
        public static void setIconBitmap(final Object o, final Bitmap iconBitmap) {
            ((MediaDescription$Builder)o).setIconBitmap(iconBitmap);
        }
        
        public static void setIconUri(final Object o, final Uri iconUri) {
            ((MediaDescription$Builder)o).setIconUri(iconUri);
        }
        
        public static void setMediaId(final Object o, final String mediaId) {
            ((MediaDescription$Builder)o).setMediaId(mediaId);
        }
        
        public static void setSubtitle(final Object o, final CharSequence subtitle) {
            ((MediaDescription$Builder)o).setSubtitle(subtitle);
        }
        
        public static void setTitle(final Object o, final CharSequence title) {
            ((MediaDescription$Builder)o).setTitle(title);
        }
    }
}
