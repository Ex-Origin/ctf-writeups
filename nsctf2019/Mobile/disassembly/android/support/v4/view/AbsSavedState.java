// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public abstract class AbsSavedState implements Parcelable
{
    public static final Parcelable$Creator<AbsSavedState> CREATOR;
    public static final AbsSavedState EMPTY_STATE;
    private final Parcelable mSuperState;
    
    static {
        EMPTY_STATE = new AbsSavedState() {};
        CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<AbsSavedState>() {
            public AbsSavedState createFromParcel(final Parcel parcel) {
                return this.createFromParcel(parcel, null);
            }
            
            public AbsSavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                if (parcel.readParcelable(classLoader) != null) {
                    throw new IllegalStateException("superState must be null");
                }
                return AbsSavedState.EMPTY_STATE;
            }
            
            public AbsSavedState[] newArray(final int n) {
                return new AbsSavedState[n];
            }
        };
    }
    
    private AbsSavedState() {
        this.mSuperState = null;
    }
    
    protected AbsSavedState(final Parcel parcel) {
        this(parcel, null);
    }
    
    protected AbsSavedState(final Parcel parcel, final ClassLoader classLoader) {
        Object mSuperState = parcel.readParcelable(classLoader);
        if (mSuperState == null) {
            mSuperState = AbsSavedState.EMPTY_STATE;
        }
        this.mSuperState = (Parcelable)mSuperState;
    }
    
    protected AbsSavedState(Parcelable mSuperState) {
        if (mSuperState == null) {
            throw new IllegalArgumentException("superState must not be null");
        }
        if (mSuperState == AbsSavedState.EMPTY_STATE) {
            mSuperState = null;
        }
        this.mSuperState = mSuperState;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public final Parcelable getSuperState() {
        return this.mSuperState;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeParcelable(this.mSuperState, n);
    }
}
