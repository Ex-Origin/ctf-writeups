// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.annotation.StyleRes;
import android.support.annotation.AnimRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.support.annotation.Nullable;
import android.support.annotation.IdRes;

public abstract class FragmentTransaction
{
    public static final int TRANSIT_ENTER_MASK = 4096;
    public static final int TRANSIT_EXIT_MASK = 8192;
    public static final int TRANSIT_FRAGMENT_CLOSE = 8194;
    public static final int TRANSIT_FRAGMENT_FADE = 4099;
    public static final int TRANSIT_FRAGMENT_OPEN = 4097;
    public static final int TRANSIT_NONE = 0;
    public static final int TRANSIT_UNSET = -1;
    
    public abstract FragmentTransaction add(@IdRes final int p0, final Fragment p1);
    
    public abstract FragmentTransaction add(@IdRes final int p0, final Fragment p1, @Nullable final String p2);
    
    public abstract FragmentTransaction add(final Fragment p0, final String p1);
    
    public abstract FragmentTransaction addSharedElement(final View p0, final String p1);
    
    public abstract FragmentTransaction addToBackStack(@Nullable final String p0);
    
    public abstract FragmentTransaction attach(final Fragment p0);
    
    public abstract int commit();
    
    public abstract int commitAllowingStateLoss();
    
    public abstract void commitNow();
    
    public abstract void commitNowAllowingStateLoss();
    
    public abstract FragmentTransaction detach(final Fragment p0);
    
    public abstract FragmentTransaction disallowAddToBackStack();
    
    public abstract FragmentTransaction hide(final Fragment p0);
    
    public abstract boolean isAddToBackStackAllowed();
    
    public abstract boolean isEmpty();
    
    public abstract FragmentTransaction postOnCommit(final Runnable p0);
    
    public abstract FragmentTransaction remove(final Fragment p0);
    
    public abstract FragmentTransaction replace(@IdRes final int p0, final Fragment p1);
    
    public abstract FragmentTransaction replace(@IdRes final int p0, final Fragment p1, @Nullable final String p2);
    
    public abstract FragmentTransaction setAllowOptimization(final boolean p0);
    
    public abstract FragmentTransaction setBreadCrumbShortTitle(@StringRes final int p0);
    
    public abstract FragmentTransaction setBreadCrumbShortTitle(final CharSequence p0);
    
    public abstract FragmentTransaction setBreadCrumbTitle(@StringRes final int p0);
    
    public abstract FragmentTransaction setBreadCrumbTitle(final CharSequence p0);
    
    public abstract FragmentTransaction setCustomAnimations(@AnimRes final int p0, @AnimRes final int p1);
    
    public abstract FragmentTransaction setCustomAnimations(@AnimRes final int p0, @AnimRes final int p1, @AnimRes final int p2, @AnimRes final int p3);
    
    public abstract FragmentTransaction setPrimaryNavigationFragment(final Fragment p0);
    
    public abstract FragmentTransaction setTransition(final int p0);
    
    public abstract FragmentTransaction setTransitionStyle(@StyleRes final int p0);
    
    public abstract FragmentTransaction show(final Fragment p0);
}
