// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.util.Iterator;
import android.util.Log;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import android.support.v4.view.PagerAdapter;

public abstract class FragmentStatePagerAdapter extends PagerAdapter
{
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentStatePagerAdapter";
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem;
    private final FragmentManager mFragmentManager;
    private ArrayList<Fragment> mFragments;
    private ArrayList<Fragment.SavedState> mSavedState;
    
    public FragmentStatePagerAdapter(final FragmentManager mFragmentManager) {
        this.mCurTransaction = null;
        this.mSavedState = new ArrayList<Fragment.SavedState>();
        this.mFragments = new ArrayList<Fragment>();
        this.mCurrentPrimaryItem = null;
        this.mFragmentManager = mFragmentManager;
    }
    
    @Override
    public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
        final Fragment fragment = (Fragment)o;
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        while (this.mSavedState.size() <= n) {
            this.mSavedState.add(null);
        }
        final ArrayList<Fragment.SavedState> mSavedState = this.mSavedState;
        Fragment.SavedState saveFragmentInstanceState;
        if (fragment.isAdded()) {
            saveFragmentInstanceState = this.mFragmentManager.saveFragmentInstanceState(fragment);
        }
        else {
            saveFragmentInstanceState = null;
        }
        mSavedState.set(n, saveFragmentInstanceState);
        this.mFragments.set(n, null);
        this.mCurTransaction.remove(fragment);
    }
    
    @Override
    public void finishUpdate(final ViewGroup viewGroup) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }
    }
    
    public abstract Fragment getItem(final int p0);
    
    @Override
    public Object instantiateItem(final ViewGroup viewGroup, final int n) {
        if (this.mFragments.size() > n) {
            final Fragment fragment = this.mFragments.get(n);
            if (fragment != null) {
                return fragment;
            }
        }
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        final Fragment item = this.getItem(n);
        if (this.mSavedState.size() > n) {
            final Fragment.SavedState initialSavedState = this.mSavedState.get(n);
            if (initialSavedState != null) {
                item.setInitialSavedState(initialSavedState);
            }
        }
        while (this.mFragments.size() <= n) {
            this.mFragments.add(null);
        }
        item.setMenuVisibility(false);
        item.setUserVisibleHint(false);
        this.mFragments.set(n, item);
        this.mCurTransaction.add(viewGroup.getId(), item);
        return item;
    }
    
    @Override
    public boolean isViewFromObject(final View view, final Object o) {
        return ((Fragment)o).getView() == view;
    }
    
    @Override
    public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
        if (parcelable != null) {
            final Bundle bundle = (Bundle)parcelable;
            bundle.setClassLoader(classLoader);
            final Parcelable[] parcelableArray = bundle.getParcelableArray("states");
            this.mSavedState.clear();
            this.mFragments.clear();
            if (parcelableArray != null) {
                for (int i = 0; i < parcelableArray.length; ++i) {
                    this.mSavedState.add((Fragment.SavedState)parcelableArray[i]);
                }
            }
            for (final String s : bundle.keySet()) {
                if (s.startsWith("f")) {
                    final int int1 = Integer.parseInt(s.substring(1));
                    final Fragment fragment = this.mFragmentManager.getFragment(bundle, s);
                    if (fragment != null) {
                        while (this.mFragments.size() <= int1) {
                            this.mFragments.add(null);
                        }
                        fragment.setMenuVisibility(false);
                        this.mFragments.set(int1, fragment);
                    }
                    else {
                        Log.w("FragmentStatePagerAdapter", "Bad fragment at key " + s);
                    }
                }
            }
        }
    }
    
    @Override
    public Parcelable saveState() {
        Bundle bundle = null;
        if (this.mSavedState.size() > 0) {
            bundle = new Bundle();
            final Fragment.SavedState[] array = new Fragment.SavedState[this.mSavedState.size()];
            this.mSavedState.toArray(array);
            bundle.putParcelableArray("states", (Parcelable[])array);
        }
        Object o;
        for (int i = 0; i < this.mFragments.size(); ++i, bundle = (Bundle)o) {
            final Fragment fragment = this.mFragments.get(i);
            o = bundle;
            if (fragment != null) {
                o = bundle;
                if (fragment.isAdded()) {
                    if ((o = bundle) == null) {
                        o = new Bundle();
                    }
                    this.mFragmentManager.putFragment((Bundle)o, "f" + i, fragment);
                }
            }
        }
        return (Parcelable)bundle;
    }
    
    @Override
    public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
        final Fragment mCurrentPrimaryItem = (Fragment)o;
        if (mCurrentPrimaryItem != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(true);
                mCurrentPrimaryItem.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = mCurrentPrimaryItem;
        }
    }
    
    @Override
    public void startUpdate(final ViewGroup viewGroup) {
        if (viewGroup.getId() == -1) {
            throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
        }
    }
}
