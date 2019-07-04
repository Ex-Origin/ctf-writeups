// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media;

import android.media.VolumeProvider;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class VolumeProviderCompatApi21
{
    public static Object createVolumeProvider(final int n, final int n2, final int n3, final Delegate delegate) {
        return new VolumeProvider(n, n2, n3) {
            public void onAdjustVolume(final int n) {
                delegate.onAdjustVolume(n);
            }
            
            public void onSetVolumeTo(final int n) {
                delegate.onSetVolumeTo(n);
            }
        };
    }
    
    public static void setCurrentVolume(final Object o, final int currentVolume) {
        ((VolumeProvider)o).setCurrentVolume(currentVolume);
    }
    
    public interface Delegate
    {
        void onAdjustVolume(final int p0);
        
        void onSetVolumeTo(final int p0);
    }
}
