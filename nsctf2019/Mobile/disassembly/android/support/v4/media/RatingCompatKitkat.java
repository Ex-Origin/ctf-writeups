// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.media.Rating;
import android.support.annotation.RequiresApi;

@RequiresApi(19)
class RatingCompatKitkat
{
    public static float getPercentRating(final Object o) {
        return ((Rating)o).getPercentRating();
    }
    
    public static int getRatingStyle(final Object o) {
        return ((Rating)o).getRatingStyle();
    }
    
    public static float getStarRating(final Object o) {
        return ((Rating)o).getStarRating();
    }
    
    public static boolean hasHeart(final Object o) {
        return ((Rating)o).hasHeart();
    }
    
    public static boolean isRated(final Object o) {
        return ((Rating)o).isRated();
    }
    
    public static boolean isThumbUp(final Object o) {
        return ((Rating)o).isThumbUp();
    }
    
    public static Object newHeartRating(final boolean b) {
        return Rating.newHeartRating(b);
    }
    
    public static Object newPercentageRating(final float n) {
        return Rating.newPercentageRating(n);
    }
    
    public static Object newStarRating(final int n, final float n2) {
        return Rating.newStarRating(n, n2);
    }
    
    public static Object newThumbRating(final boolean b) {
        return Rating.newThumbRating(b);
    }
    
    public static Object newUnratedRating(final int n) {
        return Rating.newUnratedRating(n);
    }
}
