// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.graphics.fonts;

import android.support.v4.util.Preconditions;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable$Creator;
import android.support.annotation.RestrictTo;
import android.os.Parcelable;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class FontResult implements Parcelable
{
    public static final Parcelable$Creator<FontResult> CREATOR;
    private final ParcelFileDescriptor mFileDescriptor;
    private final String mFontVariationSettings;
    private final boolean mItalic;
    private final int mTtcIndex;
    private final int mWeight;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<FontResult>() {
            public FontResult createFromParcel(final Parcel parcel) {
                return new FontResult(parcel, null);
            }
            
            public FontResult[] newArray(final int n) {
                return new FontResult[n];
            }
        };
    }
    
    private FontResult(final Parcel parcel) {
        this.mFileDescriptor = (ParcelFileDescriptor)parcel.readParcelable((ClassLoader)null);
        this.mTtcIndex = parcel.readInt();
        if (parcel.readInt() == 1) {
            this.mFontVariationSettings = parcel.readString();
        }
        else {
            this.mFontVariationSettings = null;
        }
        this.mWeight = parcel.readInt();
        this.mItalic = (parcel.readInt() == 1);
    }
    
    public FontResult(@NonNull final ParcelFileDescriptor parcelFileDescriptor, final int mTtcIndex, @Nullable final String mFontVariationSettings, final int mWeight, final boolean mItalic) {
        this.mFileDescriptor = Preconditions.checkNotNull(parcelFileDescriptor);
        this.mTtcIndex = mTtcIndex;
        this.mFontVariationSettings = mFontVariationSettings;
        this.mWeight = mWeight;
        this.mItalic = mItalic;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public ParcelFileDescriptor getFileDescriptor() {
        return this.mFileDescriptor;
    }
    
    public String getFontVariationSettings() {
        return this.mFontVariationSettings;
    }
    
    public boolean getItalic() {
        return this.mItalic;
    }
    
    public int getTtcIndex() {
        return this.mTtcIndex;
    }
    
    public int getWeight() {
        return this.mWeight;
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        final int n2 = 1;
        parcel.writeParcelable((Parcelable)this.mFileDescriptor, n);
        parcel.writeInt(this.mTtcIndex);
        if (this.mFontVariationSettings != null) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        if (this.mFontVariationSettings != null) {
            parcel.writeString(this.mFontVariationSettings);
        }
        parcel.writeInt(this.mWeight);
        if (this.mItalic) {
            n = n2;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
    }
}
