// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.content.Context;
import android.util.Log;
import android.os.Parcel;
import android.os.Bundle;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

final class FragmentState implements Parcelable
{
    public static final Parcelable$Creator<FragmentState> CREATOR;
    final Bundle mArguments;
    final String mClassName;
    final int mContainerId;
    final boolean mDetached;
    final int mFragmentId;
    final boolean mFromLayout;
    final boolean mHidden;
    final int mIndex;
    Fragment mInstance;
    final boolean mRetainInstance;
    Bundle mSavedFragmentState;
    final String mTag;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<FragmentState>() {
            public FragmentState createFromParcel(final Parcel parcel) {
                return new FragmentState(parcel);
            }
            
            public FragmentState[] newArray(final int n) {
                return new FragmentState[n];
            }
        };
    }
    
    public FragmentState(final Parcel parcel) {
        final boolean b = true;
        this.mClassName = parcel.readString();
        this.mIndex = parcel.readInt();
        this.mFromLayout = (parcel.readInt() != 0);
        this.mFragmentId = parcel.readInt();
        this.mContainerId = parcel.readInt();
        this.mTag = parcel.readString();
        this.mRetainInstance = (parcel.readInt() != 0);
        this.mDetached = (parcel.readInt() != 0);
        this.mArguments = parcel.readBundle();
        this.mHidden = (parcel.readInt() != 0 && b);
        this.mSavedFragmentState = parcel.readBundle();
    }
    
    public FragmentState(final Fragment fragment) {
        this.mClassName = fragment.getClass().getName();
        this.mIndex = fragment.mIndex;
        this.mFromLayout = fragment.mFromLayout;
        this.mFragmentId = fragment.mFragmentId;
        this.mContainerId = fragment.mContainerId;
        this.mTag = fragment.mTag;
        this.mRetainInstance = fragment.mRetainInstance;
        this.mDetached = fragment.mDetached;
        this.mArguments = fragment.mArguments;
        this.mHidden = fragment.mHidden;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public Fragment instantiate(final FragmentHostCallback fragmentHostCallback, final FragmentContainer fragmentContainer, final Fragment fragment, final FragmentManagerNonConfig mChildNonConfig) {
        if (this.mInstance == null) {
            final Context context = fragmentHostCallback.getContext();
            if (this.mArguments != null) {
                this.mArguments.setClassLoader(context.getClassLoader());
            }
            if (fragmentContainer != null) {
                this.mInstance = fragmentContainer.instantiate(context, this.mClassName, this.mArguments);
            }
            else {
                this.mInstance = Fragment.instantiate(context, this.mClassName, this.mArguments);
            }
            if (this.mSavedFragmentState != null) {
                this.mSavedFragmentState.setClassLoader(context.getClassLoader());
                this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
            }
            this.mInstance.setIndex(this.mIndex, fragment);
            this.mInstance.mFromLayout = this.mFromLayout;
            this.mInstance.mRestored = true;
            this.mInstance.mFragmentId = this.mFragmentId;
            this.mInstance.mContainerId = this.mContainerId;
            this.mInstance.mTag = this.mTag;
            this.mInstance.mRetainInstance = this.mRetainInstance;
            this.mInstance.mDetached = this.mDetached;
            this.mInstance.mHidden = this.mHidden;
            this.mInstance.mFragmentManager = fragmentHostCallback.mFragmentManager;
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Instantiated fragment " + this.mInstance);
            }
        }
        this.mInstance.mChildNonConfig = mChildNonConfig;
        return this.mInstance;
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        final int n2 = 1;
        parcel.writeString(this.mClassName);
        parcel.writeInt(this.mIndex);
        if (this.mFromLayout) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeInt(this.mFragmentId);
        parcel.writeInt(this.mContainerId);
        parcel.writeString(this.mTag);
        if (this.mRetainInstance) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        if (this.mDetached) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeBundle(this.mArguments);
        if (this.mHidden) {
            n = n2;
        }
        else {
            n = 0;
        }
        parcel.writeInt(n);
        parcel.writeBundle(this.mSavedFragmentState);
    }
}
