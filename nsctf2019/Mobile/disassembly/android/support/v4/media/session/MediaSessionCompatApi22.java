// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import android.media.session.MediaSession;
import android.support.annotation.RequiresApi;

@RequiresApi(22)
class MediaSessionCompatApi22
{
    public static void setRatingType(final Object o, final int ratingType) {
        ((MediaSession)o).setRatingType(ratingType);
    }
}
