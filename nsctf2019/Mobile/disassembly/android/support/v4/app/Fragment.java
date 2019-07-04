// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.support.v4.util.DebugUtils;
import android.os.Looper;
import android.content.IntentSender$SendIntentException;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.ContextMenu$ContextMenuInfo;
import android.view.ContextMenu;
import android.view.animation.Animation;
import android.view.MenuItem;
import android.content.res.Configuration;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;
import android.content.res.Resources;
import android.support.annotation.RestrictTo;
import android.support.v4.view.LayoutInflaterCompat;
import java.io.PrintWriter;
import java.io.FileDescriptor;
import android.support.annotation.Nullable;
import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.support.v4.util.SimpleArrayMap;
import android.view.View$OnCreateContextMenuListener;
import android.content.ComponentCallbacks;

public class Fragment implements ComponentCallbacks, View$OnCreateContextMenuListener
{
    static final int ACTIVITY_CREATED = 2;
    static final int CREATED = 1;
    static final int INITIALIZING = 0;
    static final int RESUMED = 5;
    static final int STARTED = 4;
    static final int STOPPED = 3;
    static final Object USE_DEFAULT_TRANSITION;
    private static final SimpleArrayMap<String, Class<?>> sClassMap;
    boolean mAdded;
    AnimationInfo mAnimationInfo;
    Bundle mArguments;
    int mBackStackNesting;
    boolean mCalled;
    boolean mCheckedForLoaderManager;
    FragmentManagerImpl mChildFragmentManager;
    FragmentManagerNonConfig mChildNonConfig;
    ViewGroup mContainer;
    int mContainerId;
    boolean mDeferStart;
    boolean mDetached;
    int mFragmentId;
    FragmentManagerImpl mFragmentManager;
    boolean mFromLayout;
    boolean mHasMenu;
    boolean mHidden;
    boolean mHiddenChanged;
    FragmentHostCallback mHost;
    boolean mInLayout;
    int mIndex;
    View mInnerView;
    boolean mIsNewlyAdded;
    LayoutInflater mLayoutInflater;
    LoaderManagerImpl mLoaderManager;
    boolean mLoadersStarted;
    boolean mMenuVisible;
    Fragment mParentFragment;
    boolean mPerformedCreateView;
    float mPostponedAlpha;
    boolean mRemoving;
    boolean mRestored;
    boolean mRetainInstance;
    boolean mRetaining;
    Bundle mSavedFragmentState;
    SparseArray<Parcelable> mSavedViewState;
    int mState;
    String mTag;
    Fragment mTarget;
    int mTargetIndex;
    int mTargetRequestCode;
    boolean mUserVisibleHint;
    View mView;
    String mWho;
    
    static {
        sClassMap = new SimpleArrayMap<String, Class<?>>();
        USE_DEFAULT_TRANSITION = new Object();
    }
    
    public Fragment() {
        this.mState = 0;
        this.mIndex = -1;
        this.mTargetIndex = -1;
        this.mMenuVisible = true;
        this.mUserVisibleHint = true;
    }
    
    private void callStartTransitionListener() {
        Object mStartEnterTransitionListener;
        if (this.mAnimationInfo == null) {
            mStartEnterTransitionListener = null;
        }
        else {
            this.mAnimationInfo.mEnterTransitionPostponed = false;
            mStartEnterTransitionListener = this.mAnimationInfo.mStartEnterTransitionListener;
            this.mAnimationInfo.mStartEnterTransitionListener = null;
        }
        if (mStartEnterTransitionListener != null) {
            ((OnStartEnterTransitionListener)mStartEnterTransitionListener).onStartEnterTransition();
        }
    }
    
    private AnimationInfo ensureAnimationInfo() {
        if (this.mAnimationInfo == null) {
            this.mAnimationInfo = new AnimationInfo();
        }
        return this.mAnimationInfo;
    }
    
    public static Fragment instantiate(final Context context, final String s) {
        return instantiate(context, s, null);
    }
    
    public static Fragment instantiate(final Context context, final String s, @Nullable final Bundle arguments) {
        try {
            Class<?> loadClass;
            if ((loadClass = Fragment.sClassMap.get(s)) == null) {
                loadClass = context.getClassLoader().loadClass(s);
                Fragment.sClassMap.put(s, loadClass);
            }
            final Fragment fragment = (Fragment)loadClass.newInstance();
            if (arguments != null) {
                arguments.setClassLoader(fragment.getClass().getClassLoader());
                fragment.setArguments(arguments);
            }
            return fragment;
        }
        catch (ClassNotFoundException ex) {
            throw new InstantiationException("Unable to instantiate fragment " + s + ": make sure class name exists, is public, and has an" + " empty constructor that is public", ex);
        }
        catch (java.lang.InstantiationException ex2) {
            throw new InstantiationException("Unable to instantiate fragment " + s + ": make sure class name exists, is public, and has an" + " empty constructor that is public", ex2);
        }
        catch (IllegalAccessException ex3) {
            throw new InstantiationException("Unable to instantiate fragment " + s + ": make sure class name exists, is public, and has an" + " empty constructor that is public", ex3);
        }
    }
    
    static boolean isSupportFragmentClass(final Context context, final String s) {
        try {
            Class<?> loadClass;
            if ((loadClass = Fragment.sClassMap.get(s)) == null) {
                loadClass = context.getClassLoader().loadClass(s);
                Fragment.sClassMap.put(s, loadClass);
            }
            return Fragment.class.isAssignableFrom(loadClass);
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        printWriter.print(s);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.mFragmentId));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.mContainerId));
        printWriter.print(" mTag=");
        printWriter.println(this.mTag);
        printWriter.print(s);
        printWriter.print("mState=");
        printWriter.print(this.mState);
        printWriter.print(" mIndex=");
        printWriter.print(this.mIndex);
        printWriter.print(" mWho=");
        printWriter.print(this.mWho);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.mBackStackNesting);
        printWriter.print(s);
        printWriter.print("mAdded=");
        printWriter.print(this.mAdded);
        printWriter.print(" mRemoving=");
        printWriter.print(this.mRemoving);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.mFromLayout);
        printWriter.print(" mInLayout=");
        printWriter.println(this.mInLayout);
        printWriter.print(s);
        printWriter.print("mHidden=");
        printWriter.print(this.mHidden);
        printWriter.print(" mDetached=");
        printWriter.print(this.mDetached);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.mMenuVisible);
        printWriter.print(" mHasMenu=");
        printWriter.println(this.mHasMenu);
        printWriter.print(s);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.mRetainInstance);
        printWriter.print(" mRetaining=");
        printWriter.print(this.mRetaining);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.mUserVisibleHint);
        if (this.mFragmentManager != null) {
            printWriter.print(s);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.mFragmentManager);
        }
        if (this.mHost != null) {
            printWriter.print(s);
            printWriter.print("mHost=");
            printWriter.println(this.mHost);
        }
        if (this.mParentFragment != null) {
            printWriter.print(s);
            printWriter.print("mParentFragment=");
            printWriter.println(this.mParentFragment);
        }
        if (this.mArguments != null) {
            printWriter.print(s);
            printWriter.print("mArguments=");
            printWriter.println(this.mArguments);
        }
        if (this.mSavedFragmentState != null) {
            printWriter.print(s);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.mSavedFragmentState);
        }
        if (this.mSavedViewState != null) {
            printWriter.print(s);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.mSavedViewState);
        }
        if (this.mTarget != null) {
            printWriter.print(s);
            printWriter.print("mTarget=");
            printWriter.print(this.mTarget);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.mTargetRequestCode);
        }
        if (this.getNextAnim() != 0) {
            printWriter.print(s);
            printWriter.print("mNextAnim=");
            printWriter.println(this.getNextAnim());
        }
        if (this.mContainer != null) {
            printWriter.print(s);
            printWriter.print("mContainer=");
            printWriter.println(this.mContainer);
        }
        if (this.mView != null) {
            printWriter.print(s);
            printWriter.print("mView=");
            printWriter.println(this.mView);
        }
        if (this.mInnerView != null) {
            printWriter.print(s);
            printWriter.print("mInnerView=");
            printWriter.println(this.mView);
        }
        if (this.getAnimatingAway() != null) {
            printWriter.print(s);
            printWriter.print("mAnimatingAway=");
            printWriter.println(this.getAnimatingAway());
            printWriter.print(s);
            printWriter.print("mStateAfterAnimating=");
            printWriter.println(this.getStateAfterAnimating());
        }
        if (this.mLoaderManager != null) {
            printWriter.print(s);
            printWriter.println("Loader Manager:");
            this.mLoaderManager.dump(s + "  ", fileDescriptor, printWriter, array);
        }
        if (this.mChildFragmentManager != null) {
            printWriter.print(s);
            printWriter.println("Child " + this.mChildFragmentManager + ":");
            this.mChildFragmentManager.dump(s + "  ", fileDescriptor, printWriter, array);
        }
    }
    
    @Override
    public final boolean equals(final Object o) {
        return super.equals(o);
    }
    
    Fragment findFragmentByWho(final String s) {
        if (s.equals(this.mWho)) {
            return this;
        }
        if (this.mChildFragmentManager != null) {
            return this.mChildFragmentManager.findFragmentByWho(s);
        }
        return null;
    }
    
    public final FragmentActivity getActivity() {
        if (this.mHost == null) {
            return null;
        }
        return (FragmentActivity)this.mHost.getActivity();
    }
    
    public boolean getAllowEnterTransitionOverlap() {
        return this.mAnimationInfo == null || this.mAnimationInfo.mAllowEnterTransitionOverlap == null || this.mAnimationInfo.mAllowEnterTransitionOverlap;
    }
    
    public boolean getAllowReturnTransitionOverlap() {
        return this.mAnimationInfo == null || this.mAnimationInfo.mAllowReturnTransitionOverlap == null || this.mAnimationInfo.mAllowReturnTransitionOverlap;
    }
    
    View getAnimatingAway() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mAnimatingAway;
    }
    
    public final Bundle getArguments() {
        return this.mArguments;
    }
    
    public final FragmentManager getChildFragmentManager() {
        if (this.mChildFragmentManager == null) {
            this.instantiateChildFragmentManager();
            if (this.mState >= 5) {
                this.mChildFragmentManager.dispatchResume();
            }
            else if (this.mState >= 4) {
                this.mChildFragmentManager.dispatchStart();
            }
            else if (this.mState >= 2) {
                this.mChildFragmentManager.dispatchActivityCreated();
            }
            else if (this.mState >= 1) {
                this.mChildFragmentManager.dispatchCreate();
            }
        }
        return this.mChildFragmentManager;
    }
    
    public Context getContext() {
        if (this.mHost == null) {
            return null;
        }
        return this.mHost.getContext();
    }
    
    public Object getEnterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mEnterTransition;
    }
    
    SharedElementCallback getEnterTransitionCallback() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mEnterTransitionCallback;
    }
    
    public Object getExitTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mExitTransition;
    }
    
    SharedElementCallback getExitTransitionCallback() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mExitTransitionCallback;
    }
    
    public final FragmentManager getFragmentManager() {
        return this.mFragmentManager;
    }
    
    public final Object getHost() {
        if (this.mHost == null) {
            return null;
        }
        return this.mHost.onGetHost();
    }
    
    public final int getId() {
        return this.mFragmentId;
    }
    
    public final LayoutInflater getLayoutInflater() {
        if (this.mLayoutInflater == null) {
            return this.performGetLayoutInflater(null);
        }
        return this.mLayoutInflater;
    }
    
    @Deprecated
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public LayoutInflater getLayoutInflater(final Bundle bundle) {
        if (this.mHost == null) {
            throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
        }
        final LayoutInflater onGetLayoutInflater = this.mHost.onGetLayoutInflater();
        this.getChildFragmentManager();
        LayoutInflaterCompat.setFactory2(onGetLayoutInflater, this.mChildFragmentManager.getLayoutInflaterFactory());
        return onGetLayoutInflater;
    }
    
    public LoaderManager getLoaderManager() {
        if (this.mLoaderManager != null) {
            return this.mLoaderManager;
        }
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        this.mCheckedForLoaderManager = true;
        return this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, true);
    }
    
    int getNextAnim() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mNextAnim;
    }
    
    int getNextTransition() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mNextTransition;
    }
    
    int getNextTransitionStyle() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mNextTransitionStyle;
    }
    
    public final Fragment getParentFragment() {
        return this.mParentFragment;
    }
    
    public Object getReenterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        if (this.mAnimationInfo.mReenterTransition == Fragment.USE_DEFAULT_TRANSITION) {
            return this.getExitTransition();
        }
        return this.mAnimationInfo.mReenterTransition;
    }
    
    public final Resources getResources() {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        return this.mHost.getContext().getResources();
    }
    
    public final boolean getRetainInstance() {
        return this.mRetainInstance;
    }
    
    public Object getReturnTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        if (this.mAnimationInfo.mReturnTransition == Fragment.USE_DEFAULT_TRANSITION) {
            return this.getEnterTransition();
        }
        return this.mAnimationInfo.mReturnTransition;
    }
    
    public Object getSharedElementEnterTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        return this.mAnimationInfo.mSharedElementEnterTransition;
    }
    
    public Object getSharedElementReturnTransition() {
        if (this.mAnimationInfo == null) {
            return null;
        }
        if (this.mAnimationInfo.mSharedElementReturnTransition == Fragment.USE_DEFAULT_TRANSITION) {
            return this.getSharedElementEnterTransition();
        }
        return this.mAnimationInfo.mSharedElementReturnTransition;
    }
    
    int getStateAfterAnimating() {
        if (this.mAnimationInfo == null) {
            return 0;
        }
        return this.mAnimationInfo.mStateAfterAnimating;
    }
    
    public final String getString(@StringRes final int n) {
        return this.getResources().getString(n);
    }
    
    public final String getString(@StringRes final int n, final Object... array) {
        return this.getResources().getString(n, array);
    }
    
    public final String getTag() {
        return this.mTag;
    }
    
    public final Fragment getTargetFragment() {
        return this.mTarget;
    }
    
    public final int getTargetRequestCode() {
        return this.mTargetRequestCode;
    }
    
    public final CharSequence getText(@StringRes final int n) {
        return this.getResources().getText(n);
    }
    
    public boolean getUserVisibleHint() {
        return this.mUserVisibleHint;
    }
    
    @Nullable
    public View getView() {
        return this.mView;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public final boolean hasOptionsMenu() {
        return this.mHasMenu;
    }
    
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    
    void initState() {
        this.mIndex = -1;
        this.mWho = null;
        this.mAdded = false;
        this.mRemoving = false;
        this.mFromLayout = false;
        this.mInLayout = false;
        this.mRestored = false;
        this.mBackStackNesting = 0;
        this.mFragmentManager = null;
        this.mChildFragmentManager = null;
        this.mHost = null;
        this.mFragmentId = 0;
        this.mContainerId = 0;
        this.mTag = null;
        this.mHidden = false;
        this.mDetached = false;
        this.mRetaining = false;
        this.mLoaderManager = null;
        this.mLoadersStarted = false;
        this.mCheckedForLoaderManager = false;
    }
    
    void instantiateChildFragmentManager() {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment has not been attached yet.");
        }
        (this.mChildFragmentManager = new FragmentManagerImpl()).attachController(this.mHost, new FragmentContainer() {
            @Override
            public Fragment instantiate(final Context context, final String s, final Bundle bundle) {
                return Fragment.this.mHost.instantiate(context, s, bundle);
            }
            
            @Nullable
            @Override
            public View onFindViewById(final int n) {
                if (Fragment.this.mView == null) {
                    throw new IllegalStateException("Fragment does not have a view");
                }
                return Fragment.this.mView.findViewById(n);
            }
            
            @Override
            public boolean onHasView() {
                return Fragment.this.mView != null;
            }
        }, this);
    }
    
    public final boolean isAdded() {
        return this.mHost != null && this.mAdded;
    }
    
    public final boolean isDetached() {
        return this.mDetached;
    }
    
    public final boolean isHidden() {
        return this.mHidden;
    }
    
    boolean isHideReplaced() {
        return this.mAnimationInfo != null && this.mAnimationInfo.mIsHideReplaced;
    }
    
    final boolean isInBackStack() {
        return this.mBackStackNesting > 0;
    }
    
    public final boolean isInLayout() {
        return this.mInLayout;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public final boolean isMenuVisible() {
        return this.mMenuVisible;
    }
    
    boolean isPostponed() {
        return this.mAnimationInfo != null && this.mAnimationInfo.mEnterTransitionPostponed;
    }
    
    public final boolean isRemoving() {
        return this.mRemoving;
    }
    
    public final boolean isResumed() {
        return this.mState >= 5;
    }
    
    public final boolean isStateSaved() {
        return this.mFragmentManager != null && this.mFragmentManager.isStateSaved();
    }
    
    public final boolean isVisible() {
        return this.isAdded() && !this.isHidden() && this.mView != null && this.mView.getWindowToken() != null && this.mView.getVisibility() == 0;
    }
    
    void noteStateNotSaved() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
    }
    
    @CallSuper
    public void onActivityCreated(@Nullable final Bundle bundle) {
        this.mCalled = true;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
    }
    
    @Deprecated
    @CallSuper
    public void onAttach(final Activity activity) {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onAttach(final Context context) {
        this.mCalled = true;
        Activity activity;
        if (this.mHost == null) {
            activity = null;
        }
        else {
            activity = this.mHost.getActivity();
        }
        if (activity != null) {
            this.mCalled = false;
            this.onAttach(activity);
        }
    }
    
    public void onAttachFragment(final Fragment fragment) {
    }
    
    @CallSuper
    public void onConfigurationChanged(final Configuration configuration) {
        this.mCalled = true;
    }
    
    public boolean onContextItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    @CallSuper
    public void onCreate(@Nullable final Bundle bundle) {
        this.mCalled = true;
        this.restoreChildFragmentState(bundle);
        if (this.mChildFragmentManager != null && !this.mChildFragmentManager.isStateAtLeast(1)) {
            this.mChildFragmentManager.dispatchCreate();
        }
    }
    
    public Animation onCreateAnimation(final int n, final boolean b, final int n2) {
        return null;
    }
    
    public void onCreateContextMenu(final ContextMenu contextMenu, final View view, final ContextMenu$ContextMenuInfo contextMenu$ContextMenuInfo) {
        this.getActivity().onCreateContextMenu(contextMenu, view, contextMenu$ContextMenuInfo);
    }
    
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
    }
    
    @Nullable
    public View onCreateView(final LayoutInflater layoutInflater, @Nullable final ViewGroup viewGroup, @Nullable final Bundle bundle) {
        return null;
    }
    
    @CallSuper
    public void onDestroy() {
        this.mCalled = true;
        if (!this.mCheckedForLoaderManager) {
            this.mCheckedForLoaderManager = true;
            this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
        }
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doDestroy();
        }
    }
    
    public void onDestroyOptionsMenu() {
    }
    
    @CallSuper
    public void onDestroyView() {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onDetach() {
        this.mCalled = true;
    }
    
    public LayoutInflater onGetLayoutInflater(final Bundle bundle) {
        return this.getLayoutInflater(bundle);
    }
    
    public void onHiddenChanged(final boolean b) {
    }
    
    @Deprecated
    @CallSuper
    public void onInflate(final Activity activity, final AttributeSet set, final Bundle bundle) {
        this.mCalled = true;
    }
    
    @CallSuper
    public void onInflate(final Context context, final AttributeSet set, final Bundle bundle) {
        this.mCalled = true;
        Activity activity;
        if (this.mHost == null) {
            activity = null;
        }
        else {
            activity = this.mHost.getActivity();
        }
        if (activity != null) {
            this.mCalled = false;
            this.onInflate(activity, set, bundle);
        }
    }
    
    @CallSuper
    public void onLowMemory() {
        this.mCalled = true;
    }
    
    public void onMultiWindowModeChanged(final boolean b) {
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        return false;
    }
    
    public void onOptionsMenuClosed(final Menu menu) {
    }
    
    @CallSuper
    public void onPause() {
        this.mCalled = true;
    }
    
    public void onPictureInPictureModeChanged(final boolean b) {
    }
    
    public void onPrepareOptionsMenu(final Menu menu) {
    }
    
    public void onRequestPermissionsResult(final int n, @NonNull final String[] array, @NonNull final int[] array2) {
    }
    
    @CallSuper
    public void onResume() {
        this.mCalled = true;
    }
    
    public void onSaveInstanceState(final Bundle bundle) {
    }
    
    @CallSuper
    public void onStart() {
        this.mCalled = true;
        if (!this.mLoadersStarted) {
            this.mLoadersStarted = true;
            if (!this.mCheckedForLoaderManager) {
                this.mCheckedForLoaderManager = true;
                this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
            }
            else if (this.mLoaderManager != null) {
                this.mLoaderManager.doStart();
            }
        }
    }
    
    @CallSuper
    public void onStop() {
        this.mCalled = true;
    }
    
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
    }
    
    @CallSuper
    public void onViewStateRestored(@Nullable final Bundle bundle) {
        this.mCalled = true;
    }
    
    FragmentManager peekChildFragmentManager() {
        return this.mChildFragmentManager;
    }
    
    void performActivityCreated(final Bundle bundle) {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mState = 2;
        this.mCalled = false;
        this.onActivityCreated(bundle);
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onActivityCreated()");
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchActivityCreated();
        }
    }
    
    void performConfigurationChanged(final Configuration configuration) {
        this.onConfigurationChanged(configuration);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchConfigurationChanged(configuration);
        }
    }
    
    boolean performContextItemSelected(final MenuItem menuItem) {
        return !this.mHidden && (this.onContextItemSelected(menuItem) || (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchContextItemSelected(menuItem)));
    }
    
    void performCreate(final Bundle bundle) {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mState = 1;
        this.mCalled = false;
        this.onCreate(bundle);
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onCreate()");
        }
    }
    
    boolean performCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        boolean b = false;
        final boolean b2 = false;
        if (!this.mHidden) {
            boolean b3 = b2;
            if (this.mHasMenu) {
                b3 = b2;
                if (this.mMenuVisible) {
                    b3 = true;
                    this.onCreateOptionsMenu(menu, menuInflater);
                }
            }
            b = b3;
            if (this.mChildFragmentManager != null) {
                b = (b3 | this.mChildFragmentManager.dispatchCreateOptionsMenu(menu, menuInflater));
            }
        }
        return b;
    }
    
    View performCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
        }
        this.mPerformedCreateView = true;
        return this.onCreateView(layoutInflater, viewGroup, bundle);
    }
    
    void performDestroy() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchDestroy();
        }
        this.mState = 0;
        this.mCalled = false;
        this.onDestroy();
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroy()");
        }
        this.mChildFragmentManager = null;
    }
    
    void performDestroyView() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchDestroyView();
        }
        this.mState = 1;
        this.mCalled = false;
        this.onDestroyView();
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroyView()");
        }
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doReportNextStart();
        }
        this.mPerformedCreateView = false;
    }
    
    void performDetach() {
        this.mCalled = false;
        this.onDetach();
        this.mLayoutInflater = null;
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDetach()");
        }
        if (this.mChildFragmentManager != null) {
            if (!this.mRetaining) {
                throw new IllegalStateException("Child FragmentManager of " + this + " was not " + " destroyed and this fragment is not retaining instance");
            }
            this.mChildFragmentManager.dispatchDestroy();
            this.mChildFragmentManager = null;
        }
    }
    
    LayoutInflater performGetLayoutInflater(final Bundle bundle) {
        return this.mLayoutInflater = this.onGetLayoutInflater(bundle);
    }
    
    void performLowMemory() {
        this.onLowMemory();
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchLowMemory();
        }
    }
    
    void performMultiWindowModeChanged(final boolean b) {
        this.onMultiWindowModeChanged(b);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchMultiWindowModeChanged(b);
        }
    }
    
    boolean performOptionsItemSelected(final MenuItem menuItem) {
        return !this.mHidden && ((this.mHasMenu && this.mMenuVisible && this.onOptionsItemSelected(menuItem)) || (this.mChildFragmentManager != null && this.mChildFragmentManager.dispatchOptionsItemSelected(menuItem)));
    }
    
    void performOptionsMenuClosed(final Menu menu) {
        if (!this.mHidden) {
            if (this.mHasMenu && this.mMenuVisible) {
                this.onOptionsMenuClosed(menu);
            }
            if (this.mChildFragmentManager != null) {
                this.mChildFragmentManager.dispatchOptionsMenuClosed(menu);
            }
        }
    }
    
    void performPause() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchPause();
        }
        this.mState = 4;
        this.mCalled = false;
        this.onPause();
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onPause()");
        }
    }
    
    void performPictureInPictureModeChanged(final boolean b) {
        this.onPictureInPictureModeChanged(b);
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchPictureInPictureModeChanged(b);
        }
    }
    
    boolean performPrepareOptionsMenu(final Menu menu) {
        boolean b = false;
        final boolean b2 = false;
        if (!this.mHidden) {
            boolean b3 = b2;
            if (this.mHasMenu) {
                b3 = b2;
                if (this.mMenuVisible) {
                    b3 = true;
                    this.onPrepareOptionsMenu(menu);
                }
            }
            b = b3;
            if (this.mChildFragmentManager != null) {
                b = (b3 | this.mChildFragmentManager.dispatchPrepareOptionsMenu(menu));
            }
        }
        return b;
    }
    
    void performReallyStop() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchReallyStop();
        }
        this.mState = 2;
        if (this.mLoadersStarted) {
            this.mLoadersStarted = false;
            if (!this.mCheckedForLoaderManager) {
                this.mCheckedForLoaderManager = true;
                this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
            }
            if (this.mLoaderManager != null) {
                if (!this.mHost.getRetainLoaders()) {
                    this.mLoaderManager.doStop();
                    return;
                }
                this.mLoaderManager.doRetain();
            }
        }
    }
    
    void performResume() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 5;
        this.mCalled = false;
        this.onResume();
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onResume()");
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchResume();
            this.mChildFragmentManager.execPendingActions();
        }
    }
    
    void performSaveInstanceState(final Bundle bundle) {
        this.onSaveInstanceState(bundle);
        if (this.mChildFragmentManager != null) {
            final Parcelable saveAllState = this.mChildFragmentManager.saveAllState();
            if (saveAllState != null) {
                bundle.putParcelable("android:support:fragments", saveAllState);
            }
        }
    }
    
    void performStart() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.noteStateNotSaved();
            this.mChildFragmentManager.execPendingActions();
        }
        this.mState = 4;
        this.mCalled = false;
        this.onStart();
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStart()");
        }
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStart();
        }
        if (this.mLoaderManager != null) {
            this.mLoaderManager.doReportStart();
        }
    }
    
    void performStop() {
        if (this.mChildFragmentManager != null) {
            this.mChildFragmentManager.dispatchStop();
        }
        this.mState = 3;
        this.mCalled = false;
        this.onStop();
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStop()");
        }
    }
    
    public void postponeEnterTransition() {
        this.ensureAnimationInfo().mEnterTransitionPostponed = true;
    }
    
    public void registerForContextMenu(final View view) {
        view.setOnCreateContextMenuListener((View$OnCreateContextMenuListener)this);
    }
    
    public final void requestPermissions(@NonNull final String[] array, final int n) {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        this.mHost.onRequestPermissionsFromFragment(this, array, n);
    }
    
    void restoreChildFragmentState(@Nullable final Bundle bundle) {
        if (bundle != null) {
            final Parcelable parcelable = bundle.getParcelable("android:support:fragments");
            if (parcelable != null) {
                if (this.mChildFragmentManager == null) {
                    this.instantiateChildFragmentManager();
                }
                this.mChildFragmentManager.restoreAllState(parcelable, this.mChildNonConfig);
                this.mChildNonConfig = null;
                this.mChildFragmentManager.dispatchCreate();
            }
        }
    }
    
    final void restoreViewState(final Bundle bundle) {
        if (this.mSavedViewState != null) {
            this.mInnerView.restoreHierarchyState((SparseArray)this.mSavedViewState);
            this.mSavedViewState = null;
        }
        this.mCalled = false;
        this.onViewStateRestored(bundle);
        if (!this.mCalled) {
            throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onViewStateRestored()");
        }
    }
    
    public void setAllowEnterTransitionOverlap(final boolean b) {
        this.ensureAnimationInfo().mAllowEnterTransitionOverlap = b;
    }
    
    public void setAllowReturnTransitionOverlap(final boolean b) {
        this.ensureAnimationInfo().mAllowReturnTransitionOverlap = b;
    }
    
    void setAnimatingAway(final View mAnimatingAway) {
        this.ensureAnimationInfo().mAnimatingAway = mAnimatingAway;
    }
    
    public void setArguments(final Bundle mArguments) {
        if (this.mIndex >= 0 && this.isStateSaved()) {
            throw new IllegalStateException("Fragment already active and state has been saved");
        }
        this.mArguments = mArguments;
    }
    
    public void setEnterSharedElementCallback(final SharedElementCallback mEnterTransitionCallback) {
        this.ensureAnimationInfo().mEnterTransitionCallback = mEnterTransitionCallback;
    }
    
    public void setEnterTransition(final Object o) {
        this.ensureAnimationInfo().mEnterTransition = o;
    }
    
    public void setExitSharedElementCallback(final SharedElementCallback mExitTransitionCallback) {
        this.ensureAnimationInfo().mExitTransitionCallback = mExitTransitionCallback;
    }
    
    public void setExitTransition(final Object o) {
        this.ensureAnimationInfo().mExitTransition = o;
    }
    
    public void setHasOptionsMenu(final boolean mHasMenu) {
        if (this.mHasMenu != mHasMenu) {
            this.mHasMenu = mHasMenu;
            if (this.isAdded() && !this.isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }
    
    void setHideReplaced(final boolean mIsHideReplaced) {
        this.ensureAnimationInfo().mIsHideReplaced = mIsHideReplaced;
    }
    
    final void setIndex(final int mIndex, final Fragment fragment) {
        this.mIndex = mIndex;
        if (fragment != null) {
            this.mWho = fragment.mWho + ":" + this.mIndex;
            return;
        }
        this.mWho = "android:fragment:" + this.mIndex;
    }
    
    public void setInitialSavedState(final SavedState savedState) {
        if (this.mIndex >= 0) {
            throw new IllegalStateException("Fragment already active");
        }
        Bundle mState;
        if (savedState != null && savedState.mState != null) {
            mState = savedState.mState;
        }
        else {
            mState = null;
        }
        this.mSavedFragmentState = mState;
    }
    
    public void setMenuVisibility(final boolean mMenuVisible) {
        if (this.mMenuVisible != mMenuVisible) {
            this.mMenuVisible = mMenuVisible;
            if (this.mHasMenu && this.isAdded() && !this.isHidden()) {
                this.mHost.onSupportInvalidateOptionsMenu();
            }
        }
    }
    
    void setNextAnim(final int mNextAnim) {
        if (this.mAnimationInfo == null && mNextAnim == 0) {
            return;
        }
        this.ensureAnimationInfo().mNextAnim = mNextAnim;
    }
    
    void setNextTransition(final int mNextTransition, final int mNextTransitionStyle) {
        if (this.mAnimationInfo == null && mNextTransition == 0 && mNextTransitionStyle == 0) {
            return;
        }
        this.ensureAnimationInfo();
        this.mAnimationInfo.mNextTransition = mNextTransition;
        this.mAnimationInfo.mNextTransitionStyle = mNextTransitionStyle;
    }
    
    void setOnStartEnterTransitionListener(final OnStartEnterTransitionListener mStartEnterTransitionListener) {
        this.ensureAnimationInfo();
        if (mStartEnterTransitionListener != this.mAnimationInfo.mStartEnterTransitionListener) {
            if (mStartEnterTransitionListener != null && this.mAnimationInfo.mStartEnterTransitionListener != null) {
                throw new IllegalStateException("Trying to set a replacement startPostponedEnterTransition on " + this);
            }
            if (this.mAnimationInfo.mEnterTransitionPostponed) {
                this.mAnimationInfo.mStartEnterTransitionListener = mStartEnterTransitionListener;
            }
            if (mStartEnterTransitionListener != null) {
                mStartEnterTransitionListener.startListening();
            }
        }
    }
    
    public void setReenterTransition(final Object o) {
        this.ensureAnimationInfo().mReenterTransition = o;
    }
    
    public void setRetainInstance(final boolean mRetainInstance) {
        this.mRetainInstance = mRetainInstance;
    }
    
    public void setReturnTransition(final Object o) {
        this.ensureAnimationInfo().mReturnTransition = o;
    }
    
    public void setSharedElementEnterTransition(final Object o) {
        this.ensureAnimationInfo().mSharedElementEnterTransition = o;
    }
    
    public void setSharedElementReturnTransition(final Object o) {
        this.ensureAnimationInfo().mSharedElementReturnTransition = o;
    }
    
    void setStateAfterAnimating(final int mStateAfterAnimating) {
        this.ensureAnimationInfo().mStateAfterAnimating = mStateAfterAnimating;
    }
    
    public void setTargetFragment(final Fragment mTarget, final int mTargetRequestCode) {
        final FragmentManager fragmentManager = this.getFragmentManager();
        FragmentManager fragmentManager2;
        if (mTarget != null) {
            fragmentManager2 = mTarget.getFragmentManager();
        }
        else {
            fragmentManager2 = null;
        }
        if (fragmentManager != null && fragmentManager2 != null && fragmentManager != fragmentManager2) {
            throw new IllegalArgumentException("Fragment " + mTarget + " must share the same FragmentManager to be set as a target fragment");
        }
        for (Fragment targetFragment = mTarget; targetFragment != null; targetFragment = targetFragment.getTargetFragment()) {
            if (targetFragment == this) {
                throw new IllegalArgumentException("Setting " + mTarget + " as the target of " + this + " would create a target cycle");
            }
        }
        this.mTarget = mTarget;
        this.mTargetRequestCode = mTargetRequestCode;
    }
    
    public void setUserVisibleHint(final boolean mUserVisibleHint) {
        if (!this.mUserVisibleHint && mUserVisibleHint && this.mState < 4 && this.mFragmentManager != null && this.isAdded()) {
            this.mFragmentManager.performPendingDeferredStart(this);
        }
        this.mUserVisibleHint = mUserVisibleHint;
        this.mDeferStart = (this.mState < 4 && !mUserVisibleHint);
    }
    
    public boolean shouldShowRequestPermissionRationale(@NonNull final String s) {
        return this.mHost != null && this.mHost.onShouldShowRequestPermissionRationale(s);
    }
    
    public void startActivity(final Intent intent) {
        this.startActivity(intent, null);
    }
    
    public void startActivity(final Intent intent, @Nullable final Bundle bundle) {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        this.mHost.onStartActivityFromFragment(this, intent, -1, bundle);
    }
    
    public void startActivityForResult(final Intent intent, final int n) {
        this.startActivityForResult(intent, n, null);
    }
    
    public void startActivityForResult(final Intent intent, final int n, @Nullable final Bundle bundle) {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        this.mHost.onStartActivityFromFragment(this, intent, n, bundle);
    }
    
    public void startIntentSenderForResult(final IntentSender intentSender, final int n, @Nullable final Intent intent, final int n2, final int n3, final int n4, final Bundle bundle) throws IntentSender$SendIntentException {
        if (this.mHost == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        this.mHost.onStartIntentSenderFromFragment(this, intentSender, n, intent, n2, n3, n4, bundle);
    }
    
    public void startPostponedEnterTransition() {
        if (this.mFragmentManager == null || this.mFragmentManager.mHost == null) {
            this.ensureAnimationInfo().mEnterTransitionPostponed = false;
            return;
        }
        if (Looper.myLooper() != this.mFragmentManager.mHost.getHandler().getLooper()) {
            this.mFragmentManager.mHost.getHandler().postAtFrontOfQueue((Runnable)new Runnable() {
                @Override
                public void run() {
                    Fragment.this.callStartTransitionListener();
                }
            });
            return;
        }
        this.callStartTransitionListener();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        DebugUtils.buildShortClassTag(this, sb);
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mFragmentId != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.mFragmentId));
        }
        if (this.mTag != null) {
            sb.append(" ");
            sb.append(this.mTag);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public void unregisterForContextMenu(final View view) {
        view.setOnCreateContextMenuListener((View$OnCreateContextMenuListener)null);
    }
    
    static class AnimationInfo
    {
        private Boolean mAllowEnterTransitionOverlap;
        private Boolean mAllowReturnTransitionOverlap;
        View mAnimatingAway;
        private Object mEnterTransition;
        SharedElementCallback mEnterTransitionCallback;
        boolean mEnterTransitionPostponed;
        private Object mExitTransition;
        SharedElementCallback mExitTransitionCallback;
        boolean mIsHideReplaced;
        int mNextAnim;
        int mNextTransition;
        int mNextTransitionStyle;
        private Object mReenterTransition;
        private Object mReturnTransition;
        private Object mSharedElementEnterTransition;
        private Object mSharedElementReturnTransition;
        OnStartEnterTransitionListener mStartEnterTransitionListener;
        int mStateAfterAnimating;
        
        AnimationInfo() {
            this.mEnterTransition = null;
            this.mReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mExitTransition = null;
            this.mReenterTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mSharedElementEnterTransition = null;
            this.mSharedElementReturnTransition = Fragment.USE_DEFAULT_TRANSITION;
            this.mEnterTransitionCallback = null;
            this.mExitTransitionCallback = null;
        }
    }
    
    public static class InstantiationException extends RuntimeException
    {
        public InstantiationException(final String s, final Exception ex) {
            super(s, ex);
        }
    }
    
    interface OnStartEnterTransitionListener
    {
        void onStartEnterTransition();
        
        void startListening();
    }
    
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        final Bundle mState;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Bundle mState) {
            this.mState = mState;
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            this.mState = parcel.readBundle();
            if (classLoader != null && this.mState != null) {
                this.mState.setClassLoader(classLoader);
            }
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeBundle(this.mState);
        }
    }
}
