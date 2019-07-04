// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.VelocityTracker;

@Deprecated
public final class VelocityTrackerCompat
{
    @Deprecated
    public static float getXVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getXVelocity(n);
    }
    
    @Deprecated
    public static float getYVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getYVelocity(n);
    }
}
