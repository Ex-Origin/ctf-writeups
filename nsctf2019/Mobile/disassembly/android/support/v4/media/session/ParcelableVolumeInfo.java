// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class ParcelableVolumeInfo implements Parcelable
{
    public static final Parcelable$Creator<ParcelableVolumeInfo> CREATOR;
    public int audioStream;
    public int controlType;
    public int currentVolume;
    public int maxVolume;
    public int volumeType;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<ParcelableVolumeInfo>() {
            public ParcelableVolumeInfo createFromParcel(final Parcel parcel) {
                return new ParcelableVolumeInfo(parcel);
            }
            
            public ParcelableVolumeInfo[] newArray(final int n) {
                return new ParcelableVolumeInfo[n];
            }
        };
    }
    
    public ParcelableVolumeInfo(final int volumeType, final int audioStream, final int controlType, final int maxVolume, final int currentVolume) {
        this.volumeType = volumeType;
        this.audioStream = audioStream;
        this.controlType = controlType;
        this.maxVolume = maxVolume;
        this.currentVolume = currentVolume;
    }
    
    public ParcelableVolumeInfo(final Parcel parcel) {
        this.volumeType = parcel.readInt();
        this.controlType = parcel.readInt();
        this.maxVolume = parcel.readInt();
        this.currentVolume = parcel.readInt();
        this.audioStream = parcel.readInt();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.volumeType);
        parcel.writeInt(this.controlType);
        parcel.writeInt(this.maxVolume);
        parcel.writeInt(this.currentVolume);
        parcel.writeInt(this.audioStream);
    }
}
