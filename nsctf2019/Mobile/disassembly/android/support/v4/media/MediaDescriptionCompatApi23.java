// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.media.MediaDescription$Builder;
import android.media.MediaDescription;
import android.net.Uri;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
class MediaDescriptionCompatApi23 extends MediaDescriptionCompatApi21
{
    public static Uri getMediaUri(final Object o) {
        return ((MediaDescription)o).getMediaUri();
    }
    
    static class Builder extends MediaDescriptionCompatApi21.Builder
    {
        public static void setMediaUri(final Object o, final Uri mediaUri) {
            ((MediaDescription$Builder)o).setMediaUri(mediaUri);
        }
    }
}
