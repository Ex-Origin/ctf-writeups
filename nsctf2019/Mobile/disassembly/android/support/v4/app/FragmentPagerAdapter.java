// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.PagerAdapter;

public abstract class FragmentPagerAdapter extends PagerAdapter
{
    private static final boolean DEBUG = false;
    private static final String TAG = "FragmentPagerAdapter";
    private FragmentTransaction mCurTransaction;
    private Fragment mCurrentPrimaryItem;
    private final FragmentManager mFragmentManager;
    
    public FragmentPagerAdapter(final FragmentManager mFragmentManager) {
        this.mCurTransaction = null;
        this.mCurrentPrimaryItem = null;
        this.mFragmentManager = mFragmentManager;
    }
    
    private static String makeFragmentName(final int n, final long n2) {
        return "android:switcher:" + n + ":" + n2;
    }
    
    @Override
    public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        this.mCurTransaction.detach((Fragment)o);
    }
    
    @Override
    public void finishUpdate(final ViewGroup viewGroup) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitNowAllowingStateLoss();
            this.mCurTransaction = null;
        }
    }
    
    public abstract Fragment getItem(final int p0);
    
    public long getItemId(final int n) {
        return n;
    }
    
    @Override
    public Object instantiateItem(final ViewGroup viewGroup, final int n) {
        if (this.mCurTransaction == null) {
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        }
        final long itemId = this.getItemId(n);
        final Fragment fragmentByTag = this.mFragmentManager.findFragmentByTag(makeFragmentName(viewGroup.getId(), itemId));
        Fragment fragment;
        if (fragmentByTag != null) {
            this.mCurTransaction.attach(fragmentByTag);
            fragment = fragmentByTag;
        }
        else {
            final Fragment item = this.getItem(n);
            this.mCurTransaction.add(viewGroup.getId(), item, makeFragmentName(viewGroup.getId(), itemId));
            fragment = item;
        }
        if (fragment != this.mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;
    }
    
    @Override
    public boolean isViewFromObject(final View view, final Object o) {
        return ((Fragment)o).getView() == view;
    }
    
    @Override
    public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
    }
    
    @Override
    public Parcelable saveState() {
        return null;
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
