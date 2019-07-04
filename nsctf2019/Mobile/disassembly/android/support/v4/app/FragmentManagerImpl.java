// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.support.annotation.CallSuper;
import android.support.v4.util.DebugUtils;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.res.Resources$NotFoundException;
import android.view.animation.AnimationUtils;
import java.util.Collections;
import java.util.Arrays;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.res.Configuration;
import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import android.support.v4.util.LogWriter;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import java.util.Iterator;
import android.util.Log;
import android.graphics.Paint;
import android.view.animation.Animation$AnimationListener;
import java.util.List;
import android.view.animation.ScaleAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.AlphaAnimation;
import android.content.Context;
import android.view.ViewGroup;
import java.util.Collection;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.View;
import android.support.v4.util.ArraySet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.util.Pair;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.ArrayList;
import android.util.SparseArray;
import java.lang.reflect.Field;
import android.view.animation.Interpolator;
import android.view.LayoutInflater$Factory2;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflater$Factory2
{
    static final Interpolator ACCELERATE_CUBIC;
    static final Interpolator ACCELERATE_QUINT;
    static final int ANIM_DUR = 220;
    public static final int ANIM_STYLE_CLOSE_ENTER = 3;
    public static final int ANIM_STYLE_CLOSE_EXIT = 4;
    public static final int ANIM_STYLE_FADE_ENTER = 5;
    public static final int ANIM_STYLE_FADE_EXIT = 6;
    public static final int ANIM_STYLE_OPEN_ENTER = 1;
    public static final int ANIM_STYLE_OPEN_EXIT = 2;
    static boolean DEBUG = false;
    static final Interpolator DECELERATE_CUBIC;
    static final Interpolator DECELERATE_QUINT;
    static final String TAG = "FragmentManager";
    static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
    static final String TARGET_STATE_TAG = "android:target_state";
    static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
    static final String VIEW_STATE_TAG = "android:view_state";
    static Field sAnimationListenerField;
    SparseArray<Fragment> mActive;
    ArrayList<Fragment> mAdded;
    ArrayList<Integer> mAvailBackStackIndices;
    ArrayList<BackStackRecord> mBackStack;
    ArrayList<OnBackStackChangedListener> mBackStackChangeListeners;
    ArrayList<BackStackRecord> mBackStackIndices;
    FragmentContainer mContainer;
    ArrayList<Fragment> mCreatedMenus;
    int mCurState;
    boolean mDestroyed;
    Runnable mExecCommit;
    boolean mExecutingActions;
    boolean mHavePendingDeferredStart;
    FragmentHostCallback mHost;
    private CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks;
    boolean mNeedMenuInvalidate;
    int mNextFragmentIndex;
    String mNoTransactionsBecause;
    Fragment mParent;
    ArrayList<OpGenerator> mPendingActions;
    ArrayList<StartEnterTransitionListener> mPostponedTransactions;
    Fragment mPrimaryNav;
    FragmentManagerNonConfig mSavedNonConfig;
    SparseArray<Parcelable> mStateArray;
    Bundle mStateBundle;
    boolean mStateSaved;
    ArrayList<Fragment> mTmpAddedFragments;
    ArrayList<Boolean> mTmpIsPop;
    ArrayList<BackStackRecord> mTmpRecords;
    
    static {
        FragmentManagerImpl.DEBUG = false;
        FragmentManagerImpl.sAnimationListenerField = null;
        DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5f);
        DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5f);
        ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5f);
        ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5f);
    }
    
    FragmentManagerImpl() {
        this.mNextFragmentIndex = 0;
        this.mCurState = 0;
        this.mStateBundle = null;
        this.mStateArray = null;
        this.mExecCommit = new Runnable() {
            @Override
            public void run() {
                FragmentManagerImpl.this.execPendingActions();
            }
        };
    }
    
    private void addAddedFragments(final ArraySet<Fragment> set) {
        if (this.mCurState >= 1) {
            final int min = Math.min(this.mCurState, 4);
            int size;
            if (this.mAdded == null) {
                size = 0;
            }
            else {
                size = this.mAdded.size();
            }
            for (int i = 0; i < size; ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment.mState < min) {
                    this.moveToState(fragment, min, fragment.getNextAnim(), fragment.getNextTransition(), false);
                    if (fragment.mView != null && !fragment.mHidden && fragment.mIsNewlyAdded) {
                        set.add(fragment);
                    }
                }
            }
        }
    }
    
    private void burpActive() {
        if (this.mActive != null) {
            for (int i = this.mActive.size() - 1; i >= 0; --i) {
                if (this.mActive.valueAt(i) == null) {
                    this.mActive.delete(this.mActive.keyAt(i));
                }
            }
        }
    }
    
    private void checkStateLoss() {
        if (this.mStateSaved) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
        if (this.mNoTransactionsBecause != null) {
            throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause);
        }
    }
    
    private void cleanupExec() {
        this.mExecutingActions = false;
        this.mTmpIsPop.clear();
        this.mTmpRecords.clear();
    }
    
    private void completeExecute(final BackStackRecord backStackRecord, final boolean b, final boolean b2, final boolean b3) {
        final ArrayList<BackStackRecord> list = new ArrayList<BackStackRecord>(1);
        final ArrayList<Boolean> list2 = new ArrayList<Boolean>(1);
        list.add(backStackRecord);
        list2.add(b);
        executeOps(list, list2, 0, 1);
        if (b2) {
            FragmentTransition.startTransitions(this, list, list2, 0, 1, true);
        }
        if (b3) {
            this.moveToState(this.mCurState, true);
        }
        if (this.mActive != null) {
            for (int size = this.mActive.size(), i = 0; i < size; ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null && fragment.mView != null && fragment.mIsNewlyAdded && backStackRecord.interactsWith(fragment.mContainerId)) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    if (b3) {
                        fragment.mPostponedAlpha = 0.0f;
                    }
                    else {
                        fragment.mPostponedAlpha = -1.0f;
                        fragment.mIsNewlyAdded = false;
                    }
                }
            }
        }
    }
    
    private void dispatchStateChange(final int n) {
        try {
            this.mExecutingActions = true;
            this.moveToState(n, false);
            this.mExecutingActions = false;
            this.execPendingActions();
        }
        finally {
            this.mExecutingActions = false;
        }
    }
    
    private void endAnimatingAwayFragments() {
        int size;
        if (this.mActive == null) {
            size = 0;
        }
        else {
            size = this.mActive.size();
        }
        for (int i = 0; i < size; ++i) {
            final Fragment fragment = (Fragment)this.mActive.valueAt(i);
            if (fragment != null && fragment.getAnimatingAway() != null) {
                final int stateAfterAnimating = fragment.getStateAfterAnimating();
                final View animatingAway = fragment.getAnimatingAway();
                fragment.setAnimatingAway(null);
                final Animation animation = animatingAway.getAnimation();
                if (animation != null) {
                    animation.cancel();
                    animatingAway.clearAnimation();
                }
                this.moveToState(fragment, stateAfterAnimating, 0, 0, false);
            }
        }
    }
    
    private void ensureExecReady(final boolean b) {
        if (this.mExecutingActions) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (Looper.myLooper() != this.mHost.getHandler().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        }
        if (!b) {
            this.checkStateLoss();
        }
        if (this.mTmpRecords == null) {
            this.mTmpRecords = new ArrayList<BackStackRecord>();
            this.mTmpIsPop = new ArrayList<Boolean>();
        }
        this.mExecutingActions = true;
        try {
            this.executePostponedTransaction(null, null);
        }
        finally {
            this.mExecutingActions = false;
        }
    }
    
    private static void executeOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        while (i < n) {
            final BackStackRecord backStackRecord = list.get(i);
            if (list2.get(i)) {
                backStackRecord.bumpBackStackNesting(-1);
                backStackRecord.executePopOps(i == n - 1);
            }
            else {
                backStackRecord.bumpBackStackNesting(1);
                backStackRecord.executeOps();
            }
            ++i;
        }
    }
    
    private void executeOpsTogether(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, int i, final int n) {
        final boolean mAllowOptimization = list.get(i).mAllowOptimization;
        int n2 = 0;
        if (this.mTmpAddedFragments == null) {
            this.mTmpAddedFragments = new ArrayList<Fragment>();
        }
        else {
            this.mTmpAddedFragments.clear();
        }
        if (this.mAdded != null) {
            this.mTmpAddedFragments.addAll(this.mAdded);
        }
        Fragment fragment = this.getPrimaryNavigationFragment();
        for (int j = i; j < n; ++j) {
            final BackStackRecord backStackRecord = list.get(j);
            if (!list2.get(j)) {
                fragment = backStackRecord.expandOps(this.mTmpAddedFragments, fragment);
            }
            else {
                fragment = backStackRecord.trackAddedFragmentsInPop(this.mTmpAddedFragments, fragment);
            }
            if (n2 != 0 || backStackRecord.mAddToBackStack) {
                n2 = 1;
            }
            else {
                n2 = 0;
            }
        }
        this.mTmpAddedFragments.clear();
        if (!mAllowOptimization) {
            FragmentTransition.startTransitions(this, list, list2, i, n, false);
        }
        executeOps(list, list2, i, n);
        int postponePostponableTransactions = n;
        if (mAllowOptimization) {
            final ArraySet<Fragment> set = new ArraySet<Fragment>();
            this.addAddedFragments(set);
            postponePostponableTransactions = this.postponePostponableTransactions(list, list2, i, n, set);
            this.makeRemovedFragmentsInvisible(set);
        }
        if (postponePostponableTransactions != i && mAllowOptimization) {
            FragmentTransition.startTransitions(this, list, list2, i, postponePostponableTransactions, true);
            this.moveToState(this.mCurState, true);
        }
        while (i < n) {
            final BackStackRecord backStackRecord2 = list.get(i);
            if (list2.get(i) && backStackRecord2.mIndex >= 0) {
                this.freeBackStackIndex(backStackRecord2.mIndex);
                backStackRecord2.mIndex = -1;
            }
            backStackRecord2.runOnCommitRunnables();
            ++i;
        }
        if (n2 != 0) {
            this.reportBackStackChanged();
        }
    }
    
    private void executePostponedTransaction(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        int size;
        if (this.mPostponedTransactions == null) {
            size = 0;
        }
        else {
            size = this.mPostponedTransactions.size();
        }
        final int n = 0;
        int n2 = size;
        int i = n;
    Label_0093_Outer:
        while (i < n2) {
            final StartEnterTransitionListener startEnterTransitionListener = this.mPostponedTransactions.get(i);
            while (true) {
                Label_0116: {
                    if (list == null || startEnterTransitionListener.mIsBack) {
                        break Label_0116;
                    }
                    final int index = list.indexOf(startEnterTransitionListener.mRecord);
                    if (index == -1 || !list2.get(index)) {
                        break Label_0116;
                    }
                    startEnterTransitionListener.cancelTransaction();
                    final int n3 = n2;
                    final int n4 = i;
                    i = n4 + 1;
                    n2 = n3;
                    continue Label_0093_Outer;
                }
                if (!startEnterTransitionListener.isReady()) {
                    int n4 = i;
                    int n3 = n2;
                    if (list == null) {
                        continue;
                    }
                    n4 = i;
                    n3 = n2;
                    if (!startEnterTransitionListener.mRecord.interactsWith(list, 0, list.size())) {
                        continue;
                    }
                }
                this.mPostponedTransactions.remove(i);
                int n4 = i - 1;
                int n3 = n2 - 1;
                if (list != null && !startEnterTransitionListener.mIsBack) {
                    final int index2 = list.indexOf(startEnterTransitionListener.mRecord);
                    if (index2 != -1 && list2.get(index2)) {
                        startEnterTransitionListener.cancelTransaction();
                        continue;
                    }
                }
                startEnterTransitionListener.completeTransaction();
                continue;
            }
        }
    }
    
    private Fragment findFragmentUnder(Fragment fragment) {
        final ViewGroup mContainer = fragment.mContainer;
        final View mView = fragment.mView;
        if (mContainer != null && mView != null) {
            for (int i = this.mAdded.indexOf(fragment) - 1; i >= 0; --i) {
                final Fragment fragment2 = this.mAdded.get(i);
                if (fragment2.mContainer == mContainer) {
                    fragment = fragment2;
                    if (fragment2.mView != null) {
                        return fragment;
                    }
                }
            }
            return null;
        }
        fragment = null;
        return fragment;
    }
    
    private void forcePostponedTransactions() {
        if (this.mPostponedTransactions != null) {
            while (!this.mPostponedTransactions.isEmpty()) {
                this.mPostponedTransactions.remove(0).completeTransaction();
            }
        }
    }
    
    private boolean generateOpsForPendingActions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        boolean b = false;
        synchronized (this) {
            if (this.mPendingActions == null || this.mPendingActions.size() == 0) {
                return false;
            }
            for (int size = this.mPendingActions.size(), i = 0; i < size; ++i) {
                b |= this.mPendingActions.get(i).generateOps(list, list2);
            }
            this.mPendingActions.clear();
            this.mHost.getHandler().removeCallbacks(this.mExecCommit);
            return b;
        }
    }
    
    static Animation makeFadeAnimation(final Context context, final float n, final float n2) {
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n, n2);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        return (Animation)alphaAnimation;
    }
    
    static Animation makeOpenCloseAnimation(final Context context, final float n, final float n2, final float n3, final float n4) {
        final AnimationSet set = new AnimationSet(false);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(n, n2, n, n2, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_QUINT);
        scaleAnimation.setDuration(220L);
        set.addAnimation((Animation)scaleAnimation);
        final AlphaAnimation alphaAnimation = new AlphaAnimation(n3, n4);
        alphaAnimation.setInterpolator(FragmentManagerImpl.DECELERATE_CUBIC);
        alphaAnimation.setDuration(220L);
        set.addAnimation((Animation)alphaAnimation);
        return (Animation)set;
    }
    
    private void makeRemovedFragmentsInvisible(final ArraySet<Fragment> set) {
        for (int size = set.size(), i = 0; i < size; ++i) {
            final Fragment fragment = set.valueAt(i);
            if (!fragment.mAdded) {
                final View view = fragment.getView();
                fragment.mPostponedAlpha = view.getAlpha();
                view.setAlpha(0.0f);
            }
        }
    }
    
    static boolean modifiesAlpha(final Animation animation) {
        if (!(animation instanceof AlphaAnimation)) {
            if (animation instanceof AnimationSet) {
                final List animations = ((AnimationSet)animation).getAnimations();
                for (int i = 0; i < animations.size(); ++i) {
                    if (animations.get(i) instanceof AlphaAnimation) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }
    
    private void optimizeAndExecuteOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (list != null && !list.isEmpty()) {
            if (list2 == null || list.size() != list2.size()) {
                throw new IllegalStateException("Internal error with the back stack records");
            }
            this.executePostponedTransaction(list, list2);
            final int size = list.size();
            int n = 0;
            int n2;
            int n3;
            for (int i = 0; i < size; i = n2 + 1, n = n3) {
                n2 = i;
                n3 = n;
                if (!list.get(i).mAllowOptimization) {
                    if (n != i) {
                        this.executeOpsTogether(list, list2, n, i);
                    }
                    int n5;
                    int n4 = n5 = i + 1;
                    if (list2.get(i)) {
                        while ((n5 = n4) < size) {
                            n5 = n4;
                            if (!list2.get(n4)) {
                                break;
                            }
                            n5 = n4;
                            if (list.get(n4).mAllowOptimization) {
                                break;
                            }
                            ++n4;
                        }
                    }
                    this.executeOpsTogether(list, list2, i, n5);
                    final int n6 = n5;
                    n2 = n5 - 1;
                    n3 = n6;
                }
            }
            if (n != size) {
                this.executeOpsTogether(list, list2, n, size);
            }
        }
    }
    
    private boolean popBackStackImmediate(final String s, final int n, final int n2) {
        this.execPendingActions();
        this.ensureExecReady(true);
        if (this.mPrimaryNav != null && n < 0 && s == null) {
            final FragmentManager peekChildFragmentManager = this.mPrimaryNav.peekChildFragmentManager();
            if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                return true;
            }
        }
        final boolean popBackStackState = this.popBackStackState(this.mTmpRecords, this.mTmpIsPop, s, n, n2);
        Label_0092: {
            if (!popBackStackState) {
                break Label_0092;
            }
            this.mExecutingActions = true;
            try {
                this.optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
                this.cleanupExec();
                this.doPendingDeferredStart();
                this.burpActive();
                return popBackStackState;
            }
            finally {
                this.cleanupExec();
            }
        }
    }
    
    private int postponePostponableTransactions(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final int n, final int n2, final ArraySet<Fragment> set) {
        int n3 = n2;
        int n5;
        for (int i = n2 - 1; i >= n; --i, n3 = n5) {
            final BackStackRecord backStackRecord = list.get(i);
            final boolean booleanValue = list2.get(i);
            int n4;
            if (backStackRecord.isPostponed() && !backStackRecord.interactsWith(list, i + 1, n2)) {
                n4 = 1;
            }
            else {
                n4 = 0;
            }
            n5 = n3;
            if (n4 != 0) {
                if (this.mPostponedTransactions == null) {
                    this.mPostponedTransactions = new ArrayList<StartEnterTransitionListener>();
                }
                final StartEnterTransitionListener onStartPostponedListener = new StartEnterTransitionListener(backStackRecord, booleanValue);
                this.mPostponedTransactions.add(onStartPostponedListener);
                backStackRecord.setOnStartPostponedListener(onStartPostponedListener);
                if (booleanValue) {
                    backStackRecord.executeOps();
                }
                else {
                    backStackRecord.executePopOps(false);
                }
                n5 = n3 - 1;
                if (i != n5) {
                    list.remove(i);
                    list.add(n5, backStackRecord);
                }
                this.addAddedFragments(set);
            }
        }
        return n3;
    }
    
    public static int reverseTransit(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 4097: {
                return 8194;
            }
            case 8194: {
                return 4097;
            }
            case 4099: {
                return 4099;
            }
        }
    }
    
    private void scheduleCommit() {
    Label_0081_Outer:
        while (true) {
            int n = 1;
        Label_0081:
            while (true) {
            Label_0097:
                while (true) {
                    Label_0092: {
                        while (true) {
                            final int n2;
                            synchronized (this) {
                                if (this.mPostponedTransactions == null || this.mPostponedTransactions.isEmpty()) {
                                    break Label_0092;
                                }
                                n2 = 1;
                                if (this.mPendingActions != null && this.mPendingActions.size() == 1) {
                                    break Label_0081;
                                }
                                break Label_0097;
                                this.mHost.getHandler().removeCallbacks(this.mExecCommit);
                                this.mHost.getHandler().post(this.mExecCommit);
                                return;
                            }
                            if (n2 == 0 && n == 0) {
                                return;
                            }
                            continue;
                        }
                    }
                    int n2 = 0;
                    continue Label_0081_Outer;
                }
                n = 0;
                continue Label_0081;
            }
        }
    }
    
    private void setHWLayerAnimListenerIfAlpha(final View view, final Animation animation) {
        if (view == null || animation == null || !shouldRunOnHWLayer(view, animation)) {
            return;
        }
        Animation$AnimationListener animation$AnimationListener = null;
        while (true) {
            try {
                if (FragmentManagerImpl.sAnimationListenerField == null) {
                    (FragmentManagerImpl.sAnimationListenerField = Animation.class.getDeclaredField("mListener")).setAccessible(true);
                }
                animation$AnimationListener = (Animation$AnimationListener)FragmentManagerImpl.sAnimationListenerField.get(animation);
                view.setLayerType(2, (Paint)null);
                animation.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(view, animation, animation$AnimationListener));
            }
            catch (NoSuchFieldException ex) {
                Log.e("FragmentManager", "No field with the name mListener is found in Animation class", (Throwable)ex);
                continue;
            }
            catch (IllegalAccessException ex2) {
                Log.e("FragmentManager", "Cannot access Animation's mListener field", (Throwable)ex2);
                continue;
            }
            break;
        }
    }
    
    private static void setRetaining(final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (fragmentManagerNonConfig != null) {
            final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
            if (fragments != null) {
                final Iterator<Fragment> iterator = fragments.iterator();
                while (iterator.hasNext()) {
                    iterator.next().mRetaining = true;
                }
            }
            final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
            if (childNonConfigs != null) {
                final Iterator<FragmentManagerNonConfig> iterator2 = childNonConfigs.iterator();
                while (iterator2.hasNext()) {
                    setRetaining(iterator2.next());
                }
            }
        }
    }
    
    static boolean shouldRunOnHWLayer(final View view, final Animation animation) {
        return Build$VERSION.SDK_INT >= 19 && view.getLayerType() == 0 && ViewCompat.hasOverlappingRendering(view) && modifiesAlpha(animation);
    }
    
    private void throwException(final RuntimeException ex) {
        Log.e("FragmentManager", ex.getMessage());
        Log.e("FragmentManager", "Activity state:");
        final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
        while (true) {
            Label_0075: {
                if (this.mHost == null) {
                    break Label_0075;
                }
                try {
                    this.mHost.onDump("  ", null, printWriter, new String[0]);
                    throw ex;
                }
                catch (Exception ex2) {
                    Log.e("FragmentManager", "Failed dumping state", (Throwable)ex2);
                    throw ex;
                }
                try {
                    this.dump("  ", null, printWriter, new String[0]);
                    continue;
                }
                catch (Exception ex3) {
                    Log.e("FragmentManager", "Failed dumping state", (Throwable)ex3);
                    continue;
                }
            }
            continue;
        }
    }
    
    public static int transitToStyleIndex(final int n, final boolean b) {
        switch (n) {
            default: {
                return -1;
            }
            case 4097: {
                if (b) {
                    return 1;
                }
                return 2;
            }
            case 8194: {
                if (b) {
                    return 3;
                }
                return 4;
            }
            case 4099: {
                if (b) {
                    return 5;
                }
                return 6;
            }
        }
    }
    
    void addBackStackState(final BackStackRecord backStackRecord) {
        if (this.mBackStack == null) {
            this.mBackStack = new ArrayList<BackStackRecord>();
        }
        this.mBackStack.add(backStackRecord);
        this.reportBackStackChanged();
    }
    
    public void addFragment(final Fragment fragment, final boolean b) {
        if (this.mAdded == null) {
            this.mAdded = new ArrayList<Fragment>();
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "add: " + fragment);
        }
        this.makeActive(fragment);
        if (fragment.mDetached) {
            return;
        }
        if (this.mAdded.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        synchronized (this.mAdded) {
            this.mAdded.add(fragment);
            // monitorexit(this.mAdded)
            fragment.mAdded = true;
            fragment.mRemoving = false;
            if (fragment.mView == null) {
                fragment.mHiddenChanged = false;
            }
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
            if (b) {
                this.moveToState(fragment);
            }
        }
    }
    
    @Override
    public void addOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners == null) {
            this.mBackStackChangeListeners = new ArrayList<OnBackStackChangedListener>();
        }
        this.mBackStackChangeListeners.add(onBackStackChangedListener);
    }
    
    public int allocBackStackIndex(final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mAvailBackStackIndices == null || this.mAvailBackStackIndices.size() <= 0) {
                if (this.mBackStackIndices == null) {
                    this.mBackStackIndices = new ArrayList<BackStackRecord>();
                }
                final int size = this.mBackStackIndices.size();
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Setting back stack index " + size + " to " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
                return size;
            }
            final int intValue = this.mAvailBackStackIndices.remove(this.mAvailBackStackIndices.size() - 1);
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Adding back stack index " + intValue + " with " + backStackRecord);
            }
            this.mBackStackIndices.set(intValue, backStackRecord);
            return intValue;
        }
    }
    
    public void attachController(final FragmentHostCallback mHost, final FragmentContainer mContainer, final Fragment mParent) {
        if (this.mHost != null) {
            throw new IllegalStateException("Already attached");
        }
        this.mHost = mHost;
        this.mContainer = mContainer;
        this.mParent = mParent;
    }
    
    public void attachFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "attach: " + fragment);
        }
        if (!fragment.mDetached) {
            return;
        }
        fragment.mDetached = false;
        if (fragment.mAdded) {
            return;
        }
        if (this.mAdded == null) {
            this.mAdded = new ArrayList<Fragment>();
        }
        if (this.mAdded.contains(fragment)) {
            throw new IllegalStateException("Fragment already added: " + fragment);
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "add from attach: " + fragment);
        }
        synchronized (this.mAdded) {
            this.mAdded.add(fragment);
            // monitorexit(this.mAdded)
            fragment.mAdded = true;
            if (fragment.mHasMenu && fragment.mMenuVisible) {
                this.mNeedMenuInvalidate = true;
            }
        }
    }
    
    @Override
    public FragmentTransaction beginTransaction() {
        return new BackStackRecord(this);
    }
    
    void completeShowHideFragment(final Fragment fragment) {
        if (fragment.mView != null) {
            final Animation loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), !fragment.mHidden, fragment.getNextTransitionStyle());
            if (loadAnimation != null) {
                this.setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                fragment.mView.startAnimation(loadAnimation);
                this.setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                loadAnimation.start();
            }
            int visibility;
            if (fragment.mHidden && !fragment.isHideReplaced()) {
                visibility = 8;
            }
            else {
                visibility = 0;
            }
            fragment.mView.setVisibility(visibility);
            if (fragment.isHideReplaced()) {
                fragment.setHideReplaced(false);
            }
        }
        if (fragment.mAdded && fragment.mHasMenu && fragment.mMenuVisible) {
            this.mNeedMenuInvalidate = true;
        }
        fragment.mHiddenChanged = false;
        fragment.onHiddenChanged(fragment.mHidden);
    }
    
    public void detachFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "detach: " + fragment);
        }
        if (fragment.mDetached) {
            return;
        }
        fragment.mDetached = true;
        if (!fragment.mAdded) {
            return;
        }
        Label_0108: {
            if (this.mAdded == null) {
                break Label_0108;
            }
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "remove from detach: " + fragment);
            }
            synchronized (this.mAdded) {
                this.mAdded.remove(fragment);
                // monitorexit(this.mAdded)
                if (fragment.mHasMenu && fragment.mMenuVisible) {
                    this.mNeedMenuInvalidate = true;
                }
                fragment.mAdded = false;
            }
        }
    }
    
    public void dispatchActivityCreated() {
        this.mStateSaved = false;
        this.dispatchStateChange(2);
    }
    
    public void dispatchConfigurationChanged(final Configuration configuration) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performConfigurationChanged(configuration);
                }
            }
        }
    }
    
    public boolean dispatchContextItemSelected(final MenuItem menuItem) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performContextItemSelected(menuItem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void dispatchCreate() {
        this.mStateSaved = false;
        this.dispatchStateChange(1);
    }
    
    public boolean dispatchCreateOptionsMenu(final Menu menu, final MenuInflater menuInflater) {
        boolean b = false;
        boolean b2 = false;
        ArrayList<Fragment> mCreatedMenus = null;
        ArrayList<Fragment> list = null;
        if (this.mAdded != null) {
            int n = 0;
            while (true) {
                mCreatedMenus = list;
                b = b2;
                if (n >= this.mAdded.size()) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(n);
                ArrayList<Fragment> list2 = list;
                boolean b3 = b2;
                if (fragment != null) {
                    list2 = list;
                    b3 = b2;
                    if (fragment.performCreateOptionsMenu(menu, menuInflater)) {
                        b3 = true;
                        if ((list2 = list) == null) {
                            list2 = new ArrayList<Fragment>();
                        }
                        list2.add(fragment);
                    }
                }
                ++n;
                list = list2;
                b2 = b3;
            }
        }
        if (this.mCreatedMenus != null) {
            for (int i = 0; i < this.mCreatedMenus.size(); ++i) {
                final Fragment fragment2 = this.mCreatedMenus.get(i);
                if (mCreatedMenus == null || !mCreatedMenus.contains(fragment2)) {
                    fragment2.onDestroyOptionsMenu();
                }
            }
        }
        this.mCreatedMenus = mCreatedMenus;
        return b;
    }
    
    public void dispatchDestroy() {
        this.mDestroyed = true;
        this.execPendingActions();
        this.dispatchStateChange(0);
        this.mHost = null;
        this.mContainer = null;
        this.mParent = null;
    }
    
    public void dispatchDestroyView() {
        this.dispatchStateChange(1);
    }
    
    public void dispatchLowMemory() {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performLowMemory();
                }
            }
        }
    }
    
    public void dispatchMultiWindowModeChanged(final boolean b) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performMultiWindowModeChanged(b);
                }
            }
        }
    }
    
    void dispatchOnFragmentActivityCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentActivityCreated(fragment, bundle, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentActivityCreated(this, fragment, bundle);
                }
            }
        }
    }
    
    void dispatchOnFragmentAttached(final Fragment fragment, final Context context, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentAttached(fragment, context, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentAttached(this, fragment, context);
                }
            }
        }
    }
    
    void dispatchOnFragmentCreated(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentCreated(fragment, bundle, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentCreated(this, fragment, bundle);
                }
            }
        }
    }
    
    void dispatchOnFragmentDestroyed(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDestroyed(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentDestroyed(this, fragment);
                }
            }
        }
    }
    
    void dispatchOnFragmentDetached(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentDetached(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentDetached(this, fragment);
                }
            }
        }
    }
    
    void dispatchOnFragmentPaused(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPaused(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentPaused(this, fragment);
                }
            }
        }
    }
    
    void dispatchOnFragmentPreAttached(final Fragment fragment, final Context context, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentPreAttached(fragment, context, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentPreAttached(this, fragment, context);
                }
            }
        }
    }
    
    void dispatchOnFragmentResumed(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentResumed(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentResumed(this, fragment);
                }
            }
        }
    }
    
    void dispatchOnFragmentSaveInstanceState(final Fragment fragment, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentSaveInstanceState(this, fragment, bundle);
                }
            }
        }
    }
    
    void dispatchOnFragmentStarted(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStarted(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentStarted(this, fragment);
                }
            }
        }
    }
    
    void dispatchOnFragmentStopped(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentStopped(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentStopped(this, fragment);
                }
            }
        }
    }
    
    void dispatchOnFragmentViewCreated(final Fragment fragment, final View view, final Bundle bundle, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewCreated(fragment, view, bundle, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentViewCreated(this, fragment, view, bundle);
                }
            }
        }
    }
    
    void dispatchOnFragmentViewDestroyed(final Fragment fragment, final boolean b) {
        if (this.mParent != null) {
            final FragmentManager fragmentManager = this.mParent.getFragmentManager();
            if (fragmentManager instanceof FragmentManagerImpl) {
                ((FragmentManagerImpl)fragmentManager).dispatchOnFragmentViewDestroyed(fragment, true);
            }
        }
        if (this.mLifecycleCallbacks != null) {
            for (final Pair<FragmentLifecycleCallbacks, Boolean> pair : this.mLifecycleCallbacks) {
                if (!b || pair.second) {
                    pair.first.onFragmentViewDestroyed(this, fragment);
                }
            }
        }
    }
    
    public boolean dispatchOptionsItemSelected(final MenuItem menuItem) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.performOptionsItemSelected(menuItem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void dispatchOptionsMenuClosed(final Menu menu) {
        if (this.mAdded != null) {
            for (int i = 0; i < this.mAdded.size(); ++i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performOptionsMenuClosed(menu);
                }
            }
        }
    }
    
    public void dispatchPause() {
        this.dispatchStateChange(4);
    }
    
    public void dispatchPictureInPictureModeChanged(final boolean b) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null) {
                    fragment.performPictureInPictureModeChanged(b);
                }
            }
        }
    }
    
    public boolean dispatchPrepareOptionsMenu(final Menu menu) {
        boolean b = false;
        boolean b2 = false;
        if (this.mAdded != null) {
            int n = 0;
            while (true) {
                b = b2;
                if (n >= this.mAdded.size()) {
                    break;
                }
                final Fragment fragment = this.mAdded.get(n);
                boolean b3 = b2;
                if (fragment != null) {
                    b3 = b2;
                    if (fragment.performPrepareOptionsMenu(menu)) {
                        b3 = true;
                    }
                }
                ++n;
                b2 = b3;
            }
        }
        return b;
    }
    
    public void dispatchReallyStop() {
        this.dispatchStateChange(2);
    }
    
    public void dispatchResume() {
        this.mStateSaved = false;
        this.dispatchStateChange(5);
    }
    
    public void dispatchStart() {
        this.mStateSaved = false;
        this.dispatchStateChange(4);
    }
    
    public void dispatchStop() {
        this.mStateSaved = true;
        this.dispatchStateChange(3);
    }
    
    void doPendingDeferredStart() {
        if (this.mHavePendingDeferredStart) {
            boolean b = false;
            boolean b2;
            for (int i = 0; i < this.mActive.size(); ++i, b = b2) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                b2 = b;
                if (fragment != null) {
                    b2 = b;
                    if (fragment.mLoaderManager != null) {
                        b2 = (b | fragment.mLoaderManager.hasRunningLoaders());
                    }
                }
            }
            if (!b) {
                this.mHavePendingDeferredStart = false;
                this.startPendingDeferredFragments();
            }
        }
    }
    
    @Override
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        final String string = s + "    ";
        if (this.mActive != null) {
            final int size = this.mActive.size();
            if (size > 0) {
                printWriter.print(s);
                printWriter.print("Active Fragments in ");
                printWriter.print(Integer.toHexString(System.identityHashCode(this)));
                printWriter.println(":");
                for (int i = 0; i < size; ++i) {
                    final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(i);
                    printWriter.print(": ");
                    printWriter.println(fragment);
                    if (fragment != null) {
                        fragment.dump(string, fileDescriptor, printWriter, array);
                    }
                }
            }
        }
        if (this.mAdded != null) {
            final int size2 = this.mAdded.size();
            if (size2 > 0) {
                printWriter.print(s);
                printWriter.println("Added Fragments:");
                for (int j = 0; j < size2; ++j) {
                    final Fragment fragment2 = this.mAdded.get(j);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(j);
                    printWriter.print(": ");
                    printWriter.println(fragment2.toString());
                }
            }
        }
        if (this.mCreatedMenus != null) {
            final int size3 = this.mCreatedMenus.size();
            if (size3 > 0) {
                printWriter.print(s);
                printWriter.println("Fragments Created Menus:");
                for (int k = 0; k < size3; ++k) {
                    final Fragment fragment3 = this.mCreatedMenus.get(k);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(k);
                    printWriter.print(": ");
                    printWriter.println(fragment3.toString());
                }
            }
        }
        if (this.mBackStack != null) {
            final int size4 = this.mBackStack.size();
            if (size4 > 0) {
                printWriter.print(s);
                printWriter.println("Back Stack:");
                for (int l = 0; l < size4; ++l) {
                    final BackStackRecord backStackRecord = this.mBackStack.get(l);
                    printWriter.print(s);
                    printWriter.print("  #");
                    printWriter.print(l);
                    printWriter.print(": ");
                    printWriter.println(backStackRecord.toString());
                    backStackRecord.dump(string, fileDescriptor, printWriter, array);
                }
            }
        }
        synchronized (this) {
            if (this.mBackStackIndices != null) {
                final int size5 = this.mBackStackIndices.size();
                if (size5 > 0) {
                    printWriter.print(s);
                    printWriter.println("Back Stack Indices:");
                    for (int n = 0; n < size5; ++n) {
                        final BackStackRecord backStackRecord2 = this.mBackStackIndices.get(n);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n);
                        printWriter.print(": ");
                        printWriter.println(backStackRecord2);
                    }
                }
            }
            if (this.mAvailBackStackIndices != null && this.mAvailBackStackIndices.size() > 0) {
                printWriter.print(s);
                printWriter.print("mAvailBackStackIndices: ");
                printWriter.println(Arrays.toString(this.mAvailBackStackIndices.toArray()));
            }
            // monitorexit(this)
            if (this.mPendingActions != null) {
                final int size6 = this.mPendingActions.size();
                if (size6 > 0) {
                    printWriter.print(s);
                    printWriter.println("Pending Actions:");
                    for (int n2 = 0; n2 < size6; ++n2) {
                        final OpGenerator opGenerator = this.mPendingActions.get(n2);
                        printWriter.print(s);
                        printWriter.print("  #");
                        printWriter.print(n2);
                        printWriter.print(": ");
                        printWriter.println(opGenerator);
                    }
                }
            }
        }
        final String s2;
        printWriter.print(s2);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(s2);
        printWriter.print("  mHost=");
        printWriter.println(this.mHost);
        printWriter.print(s2);
        printWriter.print("  mContainer=");
        printWriter.println(this.mContainer);
        if (this.mParent != null) {
            printWriter.print(s2);
            printWriter.print("  mParent=");
            printWriter.println(this.mParent);
        }
        printWriter.print(s2);
        printWriter.print("  mCurState=");
        printWriter.print(this.mCurState);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.mStateSaved);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.mDestroyed);
        if (this.mNeedMenuInvalidate) {
            printWriter.print(s2);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.mNeedMenuInvalidate);
        }
        if (this.mNoTransactionsBecause != null) {
            printWriter.print(s2);
            printWriter.print("  mNoTransactionsBecause=");
            printWriter.println(this.mNoTransactionsBecause);
        }
    }
    
    public void enqueueAction(final OpGenerator opGenerator, final boolean b) {
        if (!b) {
            this.checkStateLoss();
        }
        synchronized (this) {
            if (this.mDestroyed || this.mHost == null) {
                if (b) {
                    return;
                }
                throw new IllegalStateException("Activity has been destroyed");
            }
        }
        if (this.mPendingActions == null) {
            this.mPendingActions = new ArrayList<OpGenerator>();
        }
        final OpGenerator opGenerator2;
        this.mPendingActions.add(opGenerator2);
        this.scheduleCommit();
    }
    // monitorexit(this)
    
    void ensureInflatedFragmentView(final Fragment fragment) {
        if (fragment.mFromLayout && !fragment.mPerformedCreateView) {
            fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), null, fragment.mSavedFragmentState);
            if (fragment.mView == null) {
                fragment.mInnerView = null;
                return;
            }
            fragment.mInnerView = fragment.mView;
            fragment.mView.setSaveFromParentEnabled(false);
            if (fragment.mHidden) {
                fragment.mView.setVisibility(8);
            }
            fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
            this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
        }
    }
    
    public boolean execPendingActions() {
        this.ensureExecReady(true);
        boolean b = false;
        while (this.generateOpsForPendingActions(this.mTmpRecords, this.mTmpIsPop)) {
            this.mExecutingActions = true;
            try {
                this.optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
                this.cleanupExec();
                b = true;
                continue;
            }
            finally {
                this.cleanupExec();
            }
            break;
        }
        this.doPendingDeferredStart();
        this.burpActive();
        return b;
    }
    
    public void execSingleAction(final OpGenerator opGenerator, final boolean b) {
        if (b && (this.mHost == null || this.mDestroyed)) {
            return;
        }
        this.ensureExecReady(b);
        Label_0062: {
            if (!opGenerator.generateOps(this.mTmpRecords, this.mTmpIsPop)) {
                break Label_0062;
            }
            this.mExecutingActions = true;
            try {
                this.optimizeAndExecuteOps(this.mTmpRecords, this.mTmpIsPop);
                this.cleanupExec();
                this.doPendingDeferredStart();
                this.burpActive();
            }
            finally {
                this.cleanupExec();
            }
        }
    }
    
    @Override
    public boolean executePendingTransactions() {
        final boolean execPendingActions = this.execPendingActions();
        this.forcePostponedTransactions();
        return execPendingActions;
    }
    
    @Override
    public Fragment findFragmentById(final int n) {
        if (this.mAdded != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && fragment.mFragmentId == n) {
                    return fragment;
                }
            }
        }
        Label_0054: {
            break Label_0054;
        }
        if (this.mActive != null) {
            for (int j = this.mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null) {
                    final Fragment fragment = fragment2;
                    if (fragment2.mFragmentId == n) {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Fragment findFragmentByTag(final String s) {
        if (this.mAdded != null && s != null) {
            for (int i = this.mAdded.size() - 1; i >= 0; --i) {
                final Fragment fragment = this.mAdded.get(i);
                if (fragment != null && s.equals(fragment.mTag)) {
                    return fragment;
                }
            }
        }
        Label_0061: {
            break Label_0061;
        }
        if (this.mActive != null && s != null) {
            for (int j = this.mActive.size() - 1; j >= 0; --j) {
                final Fragment fragment2 = (Fragment)this.mActive.valueAt(j);
                if (fragment2 != null) {
                    final Fragment fragment = fragment2;
                    if (s.equals(fragment2.mTag)) {
                        return fragment;
                    }
                }
            }
        }
        return null;
    }
    
    public Fragment findFragmentByWho(final String s) {
        if (this.mActive != null && s != null) {
            for (int i = this.mActive.size() - 1; i >= 0; --i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    final Fragment fragmentByWho = fragment.findFragmentByWho(s);
                    if (fragmentByWho != null) {
                        return fragmentByWho;
                    }
                }
            }
        }
        return null;
    }
    
    public void freeBackStackIndex(final int n) {
        synchronized (this) {
            this.mBackStackIndices.set(n, null);
            if (this.mAvailBackStackIndices == null) {
                this.mAvailBackStackIndices = new ArrayList<Integer>();
            }
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Freeing back stack index " + n);
            }
            this.mAvailBackStackIndices.add(n);
        }
    }
    
    int getActiveFragmentCount() {
        if (this.mActive == null) {
            return 0;
        }
        return this.mActive.size();
    }
    
    List<Fragment> getActiveFragments() {
        List<Fragment> list;
        if (this.mActive == null) {
            list = null;
        }
        else {
            final int size = this.mActive.size();
            final ArrayList list2 = new ArrayList<Fragment>(size);
            int n = 0;
            while (true) {
                list = (List<Fragment>)list2;
                if (n >= size) {
                    break;
                }
                list2.add((Fragment)this.mActive.valueAt(n));
                ++n;
            }
        }
        return list;
    }
    
    @Override
    public BackStackEntry getBackStackEntryAt(final int n) {
        return this.mBackStack.get(n);
    }
    
    @Override
    public int getBackStackEntryCount() {
        if (this.mBackStack != null) {
            return this.mBackStack.size();
        }
        return 0;
    }
    
    @Override
    public Fragment getFragment(final Bundle bundle, final String s) {
        final int int1 = bundle.getInt(s, -1);
        Fragment fragment;
        if (int1 == -1) {
            fragment = null;
        }
        else {
            final Fragment fragment2 = (Fragment)this.mActive.get(int1);
            if ((fragment = fragment2) == null) {
                this.throwException(new IllegalStateException("Fragment no longer exists for key " + s + ": index " + int1));
                return fragment2;
            }
        }
        return fragment;
    }
    
    @Override
    public List<Fragment> getFragments() {
        if (this.mAdded == null) {
            return (List<Fragment>)Collections.EMPTY_LIST;
        }
        synchronized (this.mAdded) {
            return (List<Fragment>)this.mAdded.clone();
        }
    }
    
    LayoutInflater$Factory2 getLayoutInflaterFactory() {
        return (LayoutInflater$Factory2)this;
    }
    
    @Override
    public Fragment getPrimaryNavigationFragment() {
        return this.mPrimaryNav;
    }
    
    public void hideFragment(final Fragment fragment) {
        boolean mHiddenChanged = true;
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "hide: " + fragment);
        }
        if (!fragment.mHidden) {
            fragment.mHidden = true;
            if (fragment.mHiddenChanged) {
                mHiddenChanged = false;
            }
            fragment.mHiddenChanged = mHiddenChanged;
        }
    }
    
    @Override
    public boolean isDestroyed() {
        return this.mDestroyed;
    }
    
    boolean isStateAtLeast(final int n) {
        return this.mCurState >= n;
    }
    
    @Override
    public boolean isStateSaved() {
        return this.mStateSaved;
    }
    
    Animation loadAnimation(final Fragment fragment, int n, final boolean b, final int n2) {
        final Animation onCreateAnimation = fragment.onCreateAnimation(n, b, fragment.getNextAnim());
        if (onCreateAnimation != null) {
            return onCreateAnimation;
        }
        if (fragment.getNextAnim() != 0) {
            final Animation loadAnimation = AnimationUtils.loadAnimation(this.mHost.getContext(), fragment.getNextAnim());
            if (loadAnimation != null) {
                return loadAnimation;
            }
        }
        if (n == 0) {
            return null;
        }
        n = transitToStyleIndex(n, b);
        if (n < 0) {
            return null;
        }
        switch (n) {
            default: {
                n = n2;
                if (n2 == 0) {
                    n = n2;
                    if (this.mHost.onHasWindowAnimations()) {
                        n = this.mHost.onGetWindowAnimations();
                    }
                }
                if (n == 0) {
                    return null;
                }
                return null;
            }
            case 1: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.125f, 1.0f, 0.0f, 1.0f);
            }
            case 2: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 0.975f, 1.0f, 0.0f);
            }
            case 3: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 0.975f, 1.0f, 0.0f, 1.0f);
            }
            case 4: {
                return makeOpenCloseAnimation(this.mHost.getContext(), 1.0f, 1.075f, 1.0f, 0.0f);
            }
            case 5: {
                return makeFadeAnimation(this.mHost.getContext(), 0.0f, 1.0f);
            }
            case 6: {
                return makeFadeAnimation(this.mHost.getContext(), 1.0f, 0.0f);
            }
        }
    }
    
    void makeActive(final Fragment fragment) {
        if (fragment.mIndex < 0) {
            fragment.setIndex(this.mNextFragmentIndex++, this.mParent);
            if (this.mActive == null) {
                this.mActive = (SparseArray<Fragment>)new SparseArray();
            }
            this.mActive.put(fragment.mIndex, (Object)fragment);
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Allocated fragment index " + fragment);
            }
        }
    }
    
    void makeInactive(final Fragment fragment) {
        if (fragment.mIndex < 0) {
            return;
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "Freeing fragment index " + fragment);
        }
        this.mActive.put(fragment.mIndex, (Object)null);
        this.mHost.inactivateFragment(fragment.mWho);
        fragment.initState();
    }
    
    void moveFragmentToExpectedState(final Fragment fragment) {
        if (fragment != null) {
            int n2;
            final int n = n2 = this.mCurState;
            if (fragment.mRemoving) {
                if (fragment.isInBackStack()) {
                    n2 = Math.min(n, 1);
                }
                else {
                    n2 = Math.min(n, 0);
                }
            }
            this.moveToState(fragment, n2, fragment.getNextTransition(), fragment.getNextTransitionStyle(), false);
            if (fragment.mView != null) {
                final Fragment fragmentUnder = this.findFragmentUnder(fragment);
                if (fragmentUnder != null) {
                    final View mView = fragmentUnder.mView;
                    final ViewGroup mContainer = fragment.mContainer;
                    final int indexOfChild = mContainer.indexOfChild(mView);
                    final int indexOfChild2 = mContainer.indexOfChild(fragment.mView);
                    if (indexOfChild2 < indexOfChild) {
                        mContainer.removeViewAt(indexOfChild2);
                        mContainer.addView(fragment.mView, indexOfChild);
                    }
                }
                if (fragment.mIsNewlyAdded && fragment.mContainer != null) {
                    if (fragment.mPostponedAlpha > 0.0f) {
                        fragment.mView.setAlpha(fragment.mPostponedAlpha);
                    }
                    fragment.mPostponedAlpha = 0.0f;
                    fragment.mIsNewlyAdded = false;
                    final Animation loadAnimation = this.loadAnimation(fragment, fragment.getNextTransition(), true, fragment.getNextTransitionStyle());
                    if (loadAnimation != null) {
                        this.setHWLayerAnimListenerIfAlpha(fragment.mView, loadAnimation);
                        fragment.mView.startAnimation(loadAnimation);
                    }
                }
            }
            if (fragment.mHiddenChanged) {
                this.completeShowHideFragment(fragment);
            }
        }
    }
    
    void moveToState(int i, final boolean b) {
        if (this.mHost == null && i != 0) {
            throw new IllegalStateException("No activity");
        }
        if (b || i != this.mCurState) {
            this.mCurState = i;
            if (this.mActive != null) {
                i = 0;
                boolean b2 = false;
                if (this.mAdded != null) {
                    final int size = this.mAdded.size();
                    int n = 0;
                    while (true) {
                        i = (b2 ? 1 : 0);
                        if (n >= size) {
                            break;
                        }
                        final Fragment fragment = this.mAdded.get(n);
                        this.moveFragmentToExpectedState(fragment);
                        i = (b2 ? 1 : 0);
                        if (fragment.mLoaderManager != null) {
                            i = ((b2 | fragment.mLoaderManager.hasRunningLoaders()) ? 1 : 0);
                        }
                        ++n;
                        b2 = (i != 0);
                    }
                }
                final int size2 = this.mActive.size();
                final int n2 = 0;
                boolean b3 = i != 0;
                Fragment fragment2;
                boolean b4;
                for (i = n2; i < size2; ++i, b3 = b4) {
                    fragment2 = (Fragment)this.mActive.valueAt(i);
                    b4 = b3;
                    if (fragment2 != null) {
                        if (!fragment2.mRemoving) {
                            b4 = b3;
                            if (!fragment2.mDetached) {
                                continue;
                            }
                        }
                        b4 = b3;
                        if (!fragment2.mIsNewlyAdded) {
                            this.moveFragmentToExpectedState(fragment2);
                            b4 = b3;
                            if (fragment2.mLoaderManager != null) {
                                b4 = (b3 | fragment2.mLoaderManager.hasRunningLoaders());
                            }
                        }
                    }
                }
                if (!b3) {
                    this.startPendingDeferredFragments();
                }
                if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
                    this.mHost.onSupportInvalidateOptionsMenu();
                    this.mNeedMenuInvalidate = false;
                }
            }
        }
    }
    
    void moveToState(final Fragment fragment) {
        this.moveToState(fragment, this.mCurState, 0, 0, false);
    }
    
    void moveToState(final Fragment fragment, int n, int n2, int n3, final boolean b) {
        int n4 = 0;
        Label_0028: {
            if (fragment.mAdded) {
                n4 = n;
                if (!fragment.mDetached) {
                    break Label_0028;
                }
            }
            if ((n4 = n) > 1) {
                n4 = 1;
            }
        }
        int mState = n4;
        if (fragment.mRemoving && (mState = n4) > fragment.mState) {
            if (fragment.mState == 0 && fragment.isInBackStack()) {
                mState = 1;
            }
            else {
                mState = fragment.mState;
            }
        }
        n = mState;
        if (fragment.mDeferStart) {
            n = mState;
            if (fragment.mState < 4 && (n = mState) > 3) {
                n = 3;
            }
        }
        int mState2 = 0;
        Label_0211: {
            if (fragment.mState <= n) {
                if (!fragment.mFromLayout || fragment.mInLayout) {
                    if (fragment.getAnimatingAway() != null) {
                        fragment.setAnimatingAway(null);
                        this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, true);
                    }
                    n3 = n;
                    int n5 = n;
                    int n6 = n;
                    n2 = n;
                Label_1258_Outer:
                    while (true) {
                        Label_0708: {
                            while (true) {
                                FragmentManagerImpl mFragmentManager;
                                ViewGroup mContainer;
                                ViewGroup viewGroup;
                                String resourceName;
                                boolean mIsNewlyAdded;
                                Label_1066:Label_1127_Outer:
                                while (true) {
                                Label_1181_Outer:
                                    while (true) {
                                        while (true) {
                                            switch (fragment.mState) {
                                                default: {
                                                    mState2 = n;
                                                    break Label_0211;
                                                }
                                                case 0: {
                                                    n3 = n;
                                                    if (n <= 0) {
                                                        break Label_0713;
                                                    }
                                                    if (FragmentManagerImpl.DEBUG) {
                                                        Log.v("FragmentManager", "moveto CREATED: " + fragment);
                                                    }
                                                    n3 = n;
                                                    if (fragment.mSavedFragmentState != null) {
                                                        fragment.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                                                        fragment.mSavedViewState = (SparseArray<Parcelable>)fragment.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                                                        fragment.mTarget = this.getFragment(fragment.mSavedFragmentState, "android:target_state");
                                                        if (fragment.mTarget != null) {
                                                            fragment.mTargetRequestCode = fragment.mSavedFragmentState.getInt("android:target_req_state", 0);
                                                        }
                                                        fragment.mUserVisibleHint = fragment.mSavedFragmentState.getBoolean("android:user_visible_hint", true);
                                                        n3 = n;
                                                        if (!fragment.mUserVisibleHint) {
                                                            fragment.mDeferStart = true;
                                                            if ((n3 = n) > 3) {
                                                                n3 = 3;
                                                            }
                                                        }
                                                    }
                                                    fragment.mHost = this.mHost;
                                                    fragment.mParentFragment = this.mParent;
                                                    if (this.mParent != null) {
                                                        mFragmentManager = this.mParent.mChildFragmentManager;
                                                    }
                                                    else {
                                                        mFragmentManager = this.mHost.getFragmentManagerImpl();
                                                    }
                                                    fragment.mFragmentManager = mFragmentManager;
                                                    if (fragment.mTarget != null) {
                                                        if (this.mActive.get(fragment.mTarget.mIndex) != fragment.mTarget) {
                                                            throw new IllegalStateException("Fragment " + fragment + " declared target fragment " + fragment.mTarget + " that does not belong to this FragmentManager!");
                                                        }
                                                        if (fragment.mTarget.mState < 1) {
                                                            this.moveToState(fragment.mTarget, 1, 0, 0, true);
                                                        }
                                                    }
                                                    this.dispatchOnFragmentPreAttached(fragment, this.mHost.getContext(), false);
                                                    fragment.mCalled = false;
                                                    fragment.onAttach(this.mHost.getContext());
                                                    if (!fragment.mCalled) {
                                                        throw new SuperNotCalledException("Fragment " + fragment + " did not call through to super.onAttach()");
                                                    }
                                                    if (fragment.mParentFragment == null) {
                                                        this.mHost.onAttachFragment(fragment);
                                                        break;
                                                    }
                                                    break Label_1066;
                                                }
                                                case 1: {
                                                    this.ensureInflatedFragmentView(fragment);
                                                    if ((n5 = n3) <= 1) {
                                                        break Label_1066;
                                                    }
                                                    if (FragmentManagerImpl.DEBUG) {
                                                        Log.v("FragmentManager", "moveto ACTIVITY_CREATED: " + fragment);
                                                    }
                                                    if (fragment.mFromLayout) {
                                                        break Label_1066;
                                                    }
                                                    mContainer = null;
                                                    if (fragment.mContainerId == 0) {
                                                        break Label_1181_Outer;
                                                    }
                                                    if (fragment.mContainerId == -1) {
                                                        this.throwException(new IllegalArgumentException("Cannot create fragment " + fragment + " for a container view with no id"));
                                                    }
                                                    viewGroup = (ViewGroup)this.mContainer.onFindViewById(fragment.mContainerId);
                                                    if ((mContainer = viewGroup) != null) {
                                                        break Label_1181_Outer;
                                                    }
                                                    mContainer = viewGroup;
                                                    if (!fragment.mRestored) {
                                                        break Label_1181_Outer;
                                                    }
                                                    break Label_1181_Outer;
                                                }
                                                case 2: {
                                                    break Label_1066;
                                                Label_1060_Outer:
                                                    while (true) {
                                                        Label_1290: {
                                                            while (true) {
                                                            Label_1284:
                                                                while (true) {
                                                                    try {
                                                                        resourceName = fragment.getResources().getResourceName(fragment.mContainerId);
                                                                        this.throwException(new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(fragment.mContainerId) + " (" + resourceName + ") for fragment " + fragment));
                                                                        mContainer = viewGroup;
                                                                        fragment.mContainer = mContainer;
                                                                        fragment.mView = fragment.performCreateView(fragment.performGetLayoutInflater(fragment.mSavedFragmentState), mContainer, fragment.mSavedFragmentState);
                                                                        if (fragment.mView == null) {
                                                                            break Label_1290;
                                                                        }
                                                                        fragment.mInnerView = fragment.mView;
                                                                        fragment.mView.setSaveFromParentEnabled(false);
                                                                        if (mContainer != null) {
                                                                            mContainer.addView(fragment.mView);
                                                                        }
                                                                        if (fragment.mHidden) {
                                                                            fragment.mView.setVisibility(8);
                                                                        }
                                                                        fragment.onViewCreated(fragment.mView, fragment.mSavedFragmentState);
                                                                        this.dispatchOnFragmentViewCreated(fragment, fragment.mView, fragment.mSavedFragmentState, false);
                                                                        if (fragment.mView.getVisibility() != 0 || fragment.mContainer == null) {
                                                                            break Label_1284;
                                                                        }
                                                                        mIsNewlyAdded = true;
                                                                        fragment.mIsNewlyAdded = mIsNewlyAdded;
                                                                        fragment.performActivityCreated(fragment.mSavedFragmentState);
                                                                        this.dispatchOnFragmentActivityCreated(fragment, fragment.mSavedFragmentState, false);
                                                                        if (fragment.mView != null) {
                                                                            fragment.restoreViewState(fragment.mSavedFragmentState);
                                                                        }
                                                                        fragment.mSavedFragmentState = null;
                                                                        n5 = n3;
                                                                        if ((n6 = n5) > 2) {
                                                                            fragment.mState = 3;
                                                                            n6 = n5;
                                                                        }
                                                                        if ((n2 = n6) > 3) {
                                                                            if (FragmentManagerImpl.DEBUG) {
                                                                                Log.v("FragmentManager", "moveto STARTED: " + fragment);
                                                                            }
                                                                            fragment.performStart();
                                                                            this.dispatchOnFragmentStarted(fragment, false);
                                                                            n2 = n6;
                                                                        }
                                                                        if ((mState2 = n2) > 4) {
                                                                            if (FragmentManagerImpl.DEBUG) {
                                                                                Log.v("FragmentManager", "moveto RESUMED: " + fragment);
                                                                            }
                                                                            fragment.performResume();
                                                                            this.dispatchOnFragmentResumed(fragment, false);
                                                                            fragment.mSavedFragmentState = null;
                                                                            fragment.mSavedViewState = null;
                                                                            mState2 = n2;
                                                                        }
                                                                        break Label_0211;
                                                                        fragment.restoreChildFragmentState(fragment.mSavedFragmentState);
                                                                        fragment.mState = 1;
                                                                        break Label_0708;
                                                                        fragment.mParentFragment.onAttachFragment(fragment);
                                                                        break Label_1066;
                                                                    }
                                                                    catch (Resources$NotFoundException ex) {
                                                                        resourceName = "unknown";
                                                                        continue Label_1060_Outer;
                                                                    }
                                                                    break;
                                                                }
                                                                mIsNewlyAdded = false;
                                                                continue Label_1127_Outer;
                                                            }
                                                        }
                                                        fragment.mInnerView = null;
                                                        continue Label_1066;
                                                    }
                                                    break;
                                                }
                                                case 3: {
                                                    continue Label_1181_Outer;
                                                }
                                                case 4: {
                                                    continue Label_1258_Outer;
                                                }
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    break;
                                }
                                this.dispatchOnFragmentAttached(fragment, this.mHost.getContext(), false);
                                if (fragment.mRetaining) {
                                    continue;
                                }
                                break;
                            }
                            fragment.performCreate(fragment.mSavedFragmentState);
                            this.dispatchOnFragmentCreated(fragment, fragment.mSavedFragmentState, false);
                        }
                        fragment.mRetaining = false;
                        continue Label_1258_Outer;
                    }
                }
            }
            else {
                if (fragment.mState <= (mState2 = n)) {
                    break Label_0211;
                }
                switch (fragment.mState) {
                    default: {
                        mState2 = n;
                        break Label_0211;
                    }
                    case 3: {
                        if (n < 3) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom STOPPED: " + fragment);
                            }
                            fragment.performReallyStop();
                        }
                    }
                    case 2: {
                        if (n < 2) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom ACTIVITY_CREATED: " + fragment);
                            }
                            if (fragment.mView != null && this.mHost.onShouldSaveFragmentState(fragment) && fragment.mSavedViewState == null) {
                                this.saveFragmentViewState(fragment);
                            }
                            fragment.performDestroyView();
                            this.dispatchOnFragmentViewDestroyed(fragment, false);
                            if (fragment.mView != null && fragment.mContainer != null) {
                                Animation loadAnimation;
                                final Animation animation = loadAnimation = null;
                                if (this.mCurState > 0) {
                                    loadAnimation = animation;
                                    if (!this.mDestroyed) {
                                        loadAnimation = animation;
                                        if (fragment.mView.getVisibility() == 0) {
                                            loadAnimation = animation;
                                            if (fragment.mPostponedAlpha >= 0.0f) {
                                                loadAnimation = this.loadAnimation(fragment, n2, false, n3);
                                            }
                                        }
                                    }
                                }
                                fragment.mPostponedAlpha = 0.0f;
                                if (loadAnimation != null) {
                                    fragment.setAnimatingAway(fragment.mView);
                                    fragment.setStateAfterAnimating(n);
                                    loadAnimation.setAnimationListener((Animation$AnimationListener)new AnimateOnHWLayerIfNeededListener(fragment.mView, loadAnimation) {
                                        @Override
                                        public void onAnimationEnd(final Animation animation) {
                                            super.onAnimationEnd(animation);
                                            if (fragment.getAnimatingAway() != null) {
                                                fragment.setAnimatingAway(null);
                                                FragmentManagerImpl.this.moveToState(fragment, fragment.getStateAfterAnimating(), 0, 0, false);
                                            }
                                        }
                                    });
                                    fragment.mView.startAnimation(loadAnimation);
                                }
                                fragment.mContainer.removeView(fragment.mView);
                            }
                            fragment.mContainer = null;
                            fragment.mView = null;
                            fragment.mInnerView = null;
                            fragment.mInLayout = false;
                        }
                    }
                    case 1: {
                        if ((mState2 = n) >= 1) {
                            break Label_0211;
                        }
                        if (this.mDestroyed && fragment.getAnimatingAway() != null) {
                            final View animatingAway = fragment.getAnimatingAway();
                            fragment.setAnimatingAway(null);
                            animatingAway.clearAnimation();
                        }
                        if (fragment.getAnimatingAway() != null) {
                            fragment.setStateAfterAnimating(n);
                            mState2 = 1;
                            break Label_0211;
                        }
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "movefrom CREATED: " + fragment);
                        }
                        if (!fragment.mRetaining) {
                            fragment.performDestroy();
                            this.dispatchOnFragmentDestroyed(fragment, false);
                        }
                        else {
                            fragment.mState = 0;
                        }
                        fragment.performDetach();
                        this.dispatchOnFragmentDetached(fragment, false);
                        mState2 = n;
                        if (b) {
                            break Label_0211;
                        }
                        if (!fragment.mRetaining) {
                            this.makeInactive(fragment);
                            mState2 = n;
                            break Label_0211;
                        }
                        fragment.mHost = null;
                        fragment.mParentFragment = null;
                        fragment.mFragmentManager = null;
                        mState2 = n;
                        break Label_0211;
                    }
                    case 5: {
                        if (n < 5) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom RESUMED: " + fragment);
                            }
                            fragment.performPause();
                            this.dispatchOnFragmentPaused(fragment, false);
                        }
                    }
                    case 4: {
                        if (n < 4) {
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "movefrom STARTED: " + fragment);
                            }
                            fragment.performStop();
                            this.dispatchOnFragmentStopped(fragment, false);
                        }
                    }
                }
            }
            return;
        }
        if (fragment.mState != mState2) {
            Log.w("FragmentManager", "moveToState: Fragment state for " + fragment + " not updated inline; " + "expected state " + mState2 + " found " + fragment.mState);
            fragment.mState = mState2;
        }
    }
    
    public void noteStateNotSaved() {
        int size = 0;
        this.mSavedNonConfig = null;
        this.mStateSaved = false;
        if (this.mAdded != null) {
            size = this.mAdded.size();
        }
        for (int i = 0; i < size; ++i) {
            final Fragment fragment = this.mAdded.get(i);
            if (fragment != null) {
                fragment.noteStateNotSaved();
            }
        }
    }
    
    public View onCreateView(final View view, String attributeValue, final Context context, final AttributeSet set) {
        if (!"fragment".equals(attributeValue)) {
            return null;
        }
        attributeValue = set.getAttributeValue((String)null, "class");
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, FragmentTag.Fragment);
        String string;
        if ((string = attributeValue) == null) {
            string = obtainStyledAttributes.getString(0);
        }
        final int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        final String string2 = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (!Fragment.isSupportFragmentClass(this.mHost.getContext(), string)) {
            return null;
        }
        int id;
        if (view != null) {
            id = view.getId();
        }
        else {
            id = 0;
        }
        if (id == -1 && resourceId == -1 && string2 == null) {
            throw new IllegalArgumentException(set.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + string);
        }
        Fragment fragmentById;
        if (resourceId != -1) {
            fragmentById = this.findFragmentById(resourceId);
        }
        else {
            fragmentById = null;
        }
        Fragment fragmentByTag = fragmentById;
        if (fragmentById == null) {
            fragmentByTag = fragmentById;
            if (string2 != null) {
                fragmentByTag = this.findFragmentByTag(string2);
            }
        }
        Fragment fragmentById2;
        if ((fragmentById2 = fragmentByTag) == null) {
            fragmentById2 = fragmentByTag;
            if (id != -1) {
                fragmentById2 = this.findFragmentById(id);
            }
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(resourceId) + " fname=" + string + " existing=" + fragmentById2);
        }
        Fragment instantiate;
        if (fragmentById2 == null) {
            instantiate = this.mContainer.instantiate(context, string, null);
            instantiate.mFromLayout = true;
            int mFragmentId;
            if (resourceId != 0) {
                mFragmentId = resourceId;
            }
            else {
                mFragmentId = id;
            }
            instantiate.mFragmentId = mFragmentId;
            instantiate.mContainerId = id;
            instantiate.mTag = string2;
            instantiate.mInLayout = true;
            instantiate.mFragmentManager = this;
            instantiate.mHost = this.mHost;
            instantiate.onInflate(this.mHost.getContext(), set, instantiate.mSavedFragmentState);
            this.addFragment(instantiate, true);
        }
        else {
            if (fragmentById2.mInLayout) {
                throw new IllegalArgumentException(set.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string2 + ", or parent id 0x" + Integer.toHexString(id) + " with another fragment for " + string);
            }
            fragmentById2.mInLayout = true;
            fragmentById2.mHost = this.mHost;
            instantiate = fragmentById2;
            if (!fragmentById2.mRetaining) {
                fragmentById2.onInflate(this.mHost.getContext(), set, fragmentById2.mSavedFragmentState);
                instantiate = fragmentById2;
            }
        }
        if (this.mCurState < 1 && instantiate.mFromLayout) {
            this.moveToState(instantiate, 1, 0, 0, false);
        }
        else {
            this.moveToState(instantiate);
        }
        if (instantiate.mView == null) {
            throw new IllegalStateException("Fragment " + string + " did not create a view.");
        }
        if (resourceId != 0) {
            instantiate.mView.setId(resourceId);
        }
        if (instantiate.mView.getTag() == null) {
            instantiate.mView.setTag((Object)string2);
        }
        return instantiate.mView;
    }
    
    public View onCreateView(final String s, final Context context, final AttributeSet set) {
        return this.onCreateView(null, s, context, set);
    }
    
    public void performPendingDeferredStart(final Fragment fragment) {
        if (fragment.mDeferStart) {
            if (!this.mExecutingActions) {
                fragment.mDeferStart = false;
                this.moveToState(fragment, this.mCurState, 0, 0, false);
                return;
            }
            this.mHavePendingDeferredStart = true;
        }
    }
    
    @Override
    public void popBackStack() {
        this.enqueueAction((OpGenerator)new PopBackStackState(null, -1, 0), false);
    }
    
    @Override
    public void popBackStack(final int n, final int n2) {
        if (n < 0) {
            throw new IllegalArgumentException("Bad id: " + n);
        }
        this.enqueueAction((OpGenerator)new PopBackStackState(null, n, n2), false);
    }
    
    @Override
    public void popBackStack(final String s, final int n) {
        this.enqueueAction((OpGenerator)new PopBackStackState(s, -1, n), false);
    }
    
    @Override
    public boolean popBackStackImmediate() {
        this.checkStateLoss();
        return this.popBackStackImmediate(null, -1, 0);
    }
    
    @Override
    public boolean popBackStackImmediate(final int n, final int n2) {
        this.checkStateLoss();
        this.execPendingActions();
        if (n < 0) {
            throw new IllegalArgumentException("Bad id: " + n);
        }
        return this.popBackStackImmediate(null, n, n2);
    }
    
    @Override
    public boolean popBackStackImmediate(final String s, final int n) {
        this.checkStateLoss();
        return this.popBackStackImmediate(s, -1, n);
    }
    
    boolean popBackStackState(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2, final String s, int i, int n) {
        if (this.mBackStack != null) {
            if (s == null && i < 0 && (n & 0x1) == 0x0) {
                i = this.mBackStack.size() - 1;
                if (i < 0) {
                    return false;
                }
                list.add(this.mBackStack.remove(i));
                list2.add(true);
            }
            else {
                int n2 = -1;
                if (s != null || i >= 0) {
                    int j;
                    for (j = this.mBackStack.size() - 1; j >= 0; --j) {
                        final BackStackRecord backStackRecord = this.mBackStack.get(j);
                        if ((s != null && s.equals(backStackRecord.getName())) || (i >= 0 && i == backStackRecord.mIndex)) {
                            break;
                        }
                    }
                    if (j < 0) {
                        return false;
                    }
                    n2 = j;
                    if ((n & 0x1) != 0x0) {
                        n = j - 1;
                        while (true) {
                            n2 = n;
                            if (n < 0) {
                                break;
                            }
                            final BackStackRecord backStackRecord2 = this.mBackStack.get(n);
                            if (s == null || !s.equals(backStackRecord2.getName())) {
                                n2 = n;
                                if (i < 0) {
                                    break;
                                }
                                n2 = n;
                                if (i != backStackRecord2.mIndex) {
                                    break;
                                }
                            }
                            --n;
                        }
                    }
                }
                if (n2 == this.mBackStack.size() - 1) {
                    return false;
                }
                for (i = this.mBackStack.size() - 1; i > n2; --i) {
                    list.add(this.mBackStack.remove(i));
                    list2.add(true);
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void putFragment(final Bundle bundle, final String s, final Fragment fragment) {
        if (fragment.mIndex < 0) {
            this.throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        bundle.putInt(s, fragment.mIndex);
    }
    
    @Override
    public void registerFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks, final boolean b) {
        if (this.mLifecycleCallbacks == null) {
            this.mLifecycleCallbacks = new CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>>();
        }
        this.mLifecycleCallbacks.add(new Pair<FragmentLifecycleCallbacks, Boolean>(fragmentLifecycleCallbacks, b));
    }
    
    public void removeFragment(final Fragment fragment) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "remove: " + fragment + " nesting=" + fragment.mBackStackNesting);
        }
        Label_0120: {
            if (fragment.isInBackStack()) {
                break Label_0120;
            }
            int n = 1;
            while (true) {
                if (fragment.mDetached && n == 0) {
                    return;
                }
                Label_0090: {
                    if (this.mAdded == null) {
                        break Label_0090;
                    }
                    synchronized (this.mAdded) {
                        this.mAdded.remove(fragment);
                        // monitorexit(this.mAdded)
                        if (fragment.mHasMenu && fragment.mMenuVisible) {
                            this.mNeedMenuInvalidate = true;
                        }
                        fragment.mAdded = false;
                        fragment.mRemoving = true;
                        return;
                        n = 0;
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    @Override
    public void removeOnBackStackChangedListener(final OnBackStackChangedListener onBackStackChangedListener) {
        if (this.mBackStackChangeListeners != null) {
            this.mBackStackChangeListeners.remove(onBackStackChangedListener);
        }
    }
    
    void reportBackStackChanged() {
        if (this.mBackStackChangeListeners != null) {
            for (int i = 0; i < this.mBackStackChangeListeners.size(); ++i) {
                this.mBackStackChangeListeners.get(i).onBackStackChanged();
            }
        }
    }
    
    void restoreAllState(final Parcelable parcelable, final FragmentManagerNonConfig fragmentManagerNonConfig) {
        if (parcelable != null) {
            final FragmentManagerState fragmentManagerState = (FragmentManagerState)parcelable;
            if (fragmentManagerState.mActive != null) {
                List<FragmentManagerNonConfig> list = null;
                if (fragmentManagerNonConfig != null) {
                    final List<Fragment> fragments = fragmentManagerNonConfig.getFragments();
                    final List<FragmentManagerNonConfig> childNonConfigs = fragmentManagerNonConfig.getChildNonConfigs();
                    int size;
                    if (fragments != null) {
                        size = fragments.size();
                    }
                    else {
                        size = 0;
                    }
                    int n = 0;
                    while (true) {
                        list = childNonConfigs;
                        if (n >= size) {
                            break;
                        }
                        final Fragment mInstance = fragments.get(n);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: re-attaching retained " + mInstance);
                        }
                        int n2;
                        for (n2 = 0; n2 < fragmentManagerState.mActive.length && fragmentManagerState.mActive[n2].mIndex != mInstance.mIndex; ++n2) {}
                        if (n2 == fragmentManagerState.mActive.length) {
                            this.throwException(new IllegalStateException("Could not find active fragment with index " + mInstance.mIndex));
                        }
                        final FragmentState fragmentState = fragmentManagerState.mActive[n2];
                        fragmentState.mInstance = mInstance;
                        mInstance.mSavedViewState = null;
                        mInstance.mBackStackNesting = 0;
                        mInstance.mInLayout = false;
                        mInstance.mAdded = false;
                        mInstance.mTarget = null;
                        if (fragmentState.mSavedFragmentState != null) {
                            fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
                            mInstance.mSavedViewState = (SparseArray<Parcelable>)fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
                            mInstance.mSavedFragmentState = fragmentState.mSavedFragmentState;
                        }
                        ++n;
                    }
                }
                this.mActive = (SparseArray<Fragment>)new SparseArray(fragmentManagerState.mActive.length);
                for (int i = 0; i < fragmentManagerState.mActive.length; ++i) {
                    final FragmentState fragmentState2 = fragmentManagerState.mActive[i];
                    if (fragmentState2 != null) {
                        FragmentManagerNonConfig fragmentManagerNonConfig2 = null;
                        if (list != null) {
                            fragmentManagerNonConfig2 = fragmentManagerNonConfig2;
                            if (i < list.size()) {
                                fragmentManagerNonConfig2 = list.get(i);
                            }
                        }
                        final Fragment instantiate = fragmentState2.instantiate(this.mHost, this.mContainer, this.mParent, fragmentManagerNonConfig2);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: active #" + i + ": " + instantiate);
                        }
                        this.mActive.put(instantiate.mIndex, (Object)instantiate);
                        fragmentState2.mInstance = null;
                    }
                }
                if (fragmentManagerNonConfig != null) {
                    final List<Fragment> fragments2 = fragmentManagerNonConfig.getFragments();
                    int size2;
                    if (fragments2 != null) {
                        size2 = fragments2.size();
                    }
                    else {
                        size2 = 0;
                    }
                    for (int j = 0; j < size2; ++j) {
                        final Fragment fragment = fragments2.get(j);
                        if (fragment.mTargetIndex >= 0) {
                            fragment.mTarget = (Fragment)this.mActive.get(fragment.mTargetIndex);
                            if (fragment.mTarget == null) {
                                Log.w("FragmentManager", "Re-attaching retained fragment " + fragment + " target no longer exists: " + fragment.mTargetIndex);
                            }
                        }
                    }
                }
                Label_0794: {
                    Label_0789: {
                        if (fragmentManagerState.mAdded != null) {
                            this.mAdded = new ArrayList<Fragment>(fragmentManagerState.mAdded.length);
                            int k = 0;
                            while (k < fragmentManagerState.mAdded.length) {
                                final Fragment fragment2 = (Fragment)this.mActive.get(fragmentManagerState.mAdded[k]);
                                if (fragment2 == null) {
                                    this.throwException(new IllegalStateException("No instantiated fragment for index #" + fragmentManagerState.mAdded[k]));
                                }
                                fragment2.mAdded = true;
                                if (FragmentManagerImpl.DEBUG) {
                                    Log.v("FragmentManager", "restoreAllState: added #" + k + ": " + fragment2);
                                }
                                if (this.mAdded.contains(fragment2)) {
                                    throw new IllegalStateException("Already added!");
                                }
                                synchronized (this.mAdded) {
                                    this.mAdded.add(fragment2);
                                    // monitorexit(this.mAdded)
                                    ++k;
                                    continue;
                                }
                                break Label_0789;
                            }
                            break Label_0794;
                        }
                    }
                    this.mAdded = null;
                }
                if (fragmentManagerState.mBackStack != null) {
                    this.mBackStack = new ArrayList<BackStackRecord>(fragmentManagerState.mBackStack.length);
                    for (int l = 0; l < fragmentManagerState.mBackStack.length; ++l) {
                        final BackStackRecord instantiate2 = fragmentManagerState.mBackStack[l].instantiate(this);
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "restoreAllState: back stack #" + l + " (index " + instantiate2.mIndex + "): " + instantiate2);
                            final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
                            instantiate2.dump("  ", printWriter, false);
                            printWriter.close();
                        }
                        this.mBackStack.add(instantiate2);
                        if (instantiate2.mIndex >= 0) {
                            this.setBackStackIndex(instantiate2.mIndex, instantiate2);
                        }
                    }
                }
                else {
                    this.mBackStack = null;
                }
                if (fragmentManagerState.mPrimaryNavActiveIndex >= 0) {
                    this.mPrimaryNav = (Fragment)this.mActive.get(fragmentManagerState.mPrimaryNavActiveIndex);
                }
                this.mNextFragmentIndex = fragmentManagerState.mNextFragmentIndex;
            }
        }
    }
    
    FragmentManagerNonConfig retainNonConfig() {
        setRetaining(this.mSavedNonConfig);
        return this.mSavedNonConfig;
    }
    
    Parcelable saveAllState() {
        this.forcePostponedTransactions();
        this.endAnimatingAwayFragments();
        this.execPendingActions();
        this.mStateSaved = true;
        this.mSavedNonConfig = null;
        if (this.mActive != null && this.mActive.size() > 0) {
            final int size = this.mActive.size();
            final FragmentState[] mActive = new FragmentState[size];
            boolean b = false;
            for (int i = 0; i < size; ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    if (fragment.mIndex < 0) {
                        this.throwException(new IllegalStateException("Failure saving state: active " + fragment + " has cleared index: " + fragment.mIndex));
                    }
                    final boolean b2 = true;
                    final FragmentState fragmentState = new FragmentState(fragment);
                    mActive[i] = fragmentState;
                    if (fragment.mState > 0 && fragmentState.mSavedFragmentState == null) {
                        fragmentState.mSavedFragmentState = this.saveFragmentBasicState(fragment);
                        if (fragment.mTarget != null) {
                            if (fragment.mTarget.mIndex < 0) {
                                this.throwException(new IllegalStateException("Failure saving state: " + fragment + " has target not in fragment manager: " + fragment.mTarget));
                            }
                            if (fragmentState.mSavedFragmentState == null) {
                                fragmentState.mSavedFragmentState = new Bundle();
                            }
                            this.putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment.mTarget);
                            if (fragment.mTargetRequestCode != 0) {
                                fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment.mTargetRequestCode);
                            }
                        }
                    }
                    else {
                        fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
                    }
                    b = b2;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "Saved state of " + fragment + ": " + fragmentState.mSavedFragmentState);
                        b = b2;
                    }
                }
            }
            if (b) {
                final int[] array = null;
                final BackStackState[] array2 = null;
                int[] mAdded = array;
                if (this.mAdded != null) {
                    final int size2 = this.mAdded.size();
                    mAdded = array;
                    if (size2 > 0) {
                        final int[] array3 = new int[size2];
                        int n = 0;
                        while (true) {
                            mAdded = array3;
                            if (n >= size2) {
                                break;
                            }
                            array3[n] = this.mAdded.get(n).mIndex;
                            if (array3[n] < 0) {
                                this.throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(n) + " has cleared index: " + array3[n]));
                            }
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "saveAllState: adding fragment #" + n + ": " + this.mAdded.get(n));
                            }
                            ++n;
                        }
                    }
                }
                BackStackState[] mBackStack = array2;
                if (this.mBackStack != null) {
                    final int size3 = this.mBackStack.size();
                    mBackStack = array2;
                    if (size3 > 0) {
                        final BackStackState[] array4 = new BackStackState[size3];
                        int n2 = 0;
                        while (true) {
                            mBackStack = array4;
                            if (n2 >= size3) {
                                break;
                            }
                            array4[n2] = new BackStackState(this.mBackStack.get(n2));
                            if (FragmentManagerImpl.DEBUG) {
                                Log.v("FragmentManager", "saveAllState: adding back stack #" + n2 + ": " + this.mBackStack.get(n2));
                            }
                            ++n2;
                        }
                    }
                }
                final FragmentManagerState fragmentManagerState = new FragmentManagerState();
                fragmentManagerState.mActive = mActive;
                fragmentManagerState.mAdded = mAdded;
                fragmentManagerState.mBackStack = mBackStack;
                if (this.mPrimaryNav != null) {
                    fragmentManagerState.mPrimaryNavActiveIndex = this.mPrimaryNav.mIndex;
                }
                fragmentManagerState.mNextFragmentIndex = this.mNextFragmentIndex;
                this.saveNonConfig();
                return (Parcelable)fragmentManagerState;
            }
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
                return null;
            }
        }
        return null;
    }
    
    Bundle saveFragmentBasicState(final Fragment fragment) {
        Bundle mStateBundle = null;
        if (this.mStateBundle == null) {
            this.mStateBundle = new Bundle();
        }
        fragment.performSaveInstanceState(this.mStateBundle);
        this.dispatchOnFragmentSaveInstanceState(fragment, this.mStateBundle, false);
        if (!this.mStateBundle.isEmpty()) {
            mStateBundle = this.mStateBundle;
            this.mStateBundle = null;
        }
        if (fragment.mView != null) {
            this.saveFragmentViewState(fragment);
        }
        Bundle bundle = mStateBundle;
        if (fragment.mSavedViewState != null) {
            if ((bundle = mStateBundle) == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", (SparseArray)fragment.mSavedViewState);
        }
        Bundle bundle2 = bundle;
        if (!fragment.mUserVisibleHint) {
            if ((bundle2 = bundle) == null) {
                bundle2 = new Bundle();
            }
            bundle2.putBoolean("android:user_visible_hint", fragment.mUserVisibleHint);
        }
        return bundle2;
    }
    
    @Override
    public Fragment.SavedState saveFragmentInstanceState(final Fragment fragment) {
        final Fragment.SavedState savedState = null;
        if (fragment.mIndex < 0) {
            this.throwException(new IllegalStateException("Fragment " + fragment + " is not currently in the FragmentManager"));
        }
        Fragment.SavedState savedState2 = savedState;
        if (fragment.mState > 0) {
            final Bundle saveFragmentBasicState = this.saveFragmentBasicState(fragment);
            savedState2 = savedState;
            if (saveFragmentBasicState != null) {
                savedState2 = new Fragment.SavedState(saveFragmentBasicState);
            }
        }
        return savedState2;
    }
    
    void saveFragmentViewState(final Fragment fragment) {
        if (fragment.mInnerView != null) {
            if (this.mStateArray == null) {
                this.mStateArray = (SparseArray<Parcelable>)new SparseArray();
            }
            else {
                this.mStateArray.clear();
            }
            fragment.mInnerView.saveHierarchyState((SparseArray)this.mStateArray);
            if (this.mStateArray.size() > 0) {
                fragment.mSavedViewState = this.mStateArray;
                this.mStateArray = null;
            }
        }
    }
    
    void saveNonConfig() {
        List<Fragment> list = null;
        ArrayList<Fragment> list2 = null;
        List<FragmentManagerNonConfig> list3 = null;
        ArrayList<FragmentManagerNonConfig> list4 = null;
        if (this.mActive != null) {
            int n = 0;
            while (true) {
                list3 = list4;
                list = list2;
                if (n >= this.mActive.size()) {
                    break;
                }
                final Fragment fragment = (Fragment)this.mActive.valueAt(n);
                ArrayList<FragmentManagerNonConfig> list5 = list4;
                ArrayList<Fragment> list6 = list2;
                if (fragment != null) {
                    ArrayList<Fragment> list7 = list2;
                    if (fragment.mRetainInstance) {
                        ArrayList<Fragment> list8;
                        if ((list8 = list2) == null) {
                            list8 = new ArrayList<Fragment>();
                        }
                        list8.add(fragment);
                        int mIndex;
                        if (fragment.mTarget != null) {
                            mIndex = fragment.mTarget.mIndex;
                        }
                        else {
                            mIndex = -1;
                        }
                        fragment.mTargetIndex = mIndex;
                        list7 = list8;
                        if (FragmentManagerImpl.DEBUG) {
                            Log.v("FragmentManager", "retainNonConfig: keeping retained " + fragment);
                            list7 = list8;
                        }
                    }
                    boolean b = false;
                    ArrayList<FragmentManagerNonConfig> list9 = list4;
                    if (fragment.mChildFragmentManager != null) {
                        fragment.mChildFragmentManager.saveNonConfig();
                        final FragmentManagerNonConfig mSavedNonConfig = fragment.mChildFragmentManager.mSavedNonConfig;
                        b = b;
                        list9 = list4;
                        if (mSavedNonConfig != null) {
                            if ((list9 = list4) == null) {
                                final ArrayList<FragmentManagerNonConfig> list10 = new ArrayList<FragmentManagerNonConfig>();
                                int n2 = 0;
                                while (true) {
                                    list9 = list10;
                                    if (n2 >= n) {
                                        break;
                                    }
                                    list10.add(null);
                                    ++n2;
                                }
                            }
                            list9.add(mSavedNonConfig);
                            b = true;
                        }
                    }
                    list5 = list9;
                    list6 = list7;
                    if (list9 != null) {
                        list5 = list9;
                        list6 = list7;
                        if (!b) {
                            list9.add(null);
                            list6 = list7;
                            list5 = list9;
                        }
                    }
                }
                ++n;
                list4 = list5;
                list2 = list6;
            }
        }
        if (list == null && list3 == null) {
            this.mSavedNonConfig = null;
            return;
        }
        this.mSavedNonConfig = new FragmentManagerNonConfig(list, list3);
    }
    
    public void setBackStackIndex(final int n, final BackStackRecord backStackRecord) {
        synchronized (this) {
            if (this.mBackStackIndices == null) {
                this.mBackStackIndices = new ArrayList<BackStackRecord>();
            }
            int i;
            if (n < (i = this.mBackStackIndices.size())) {
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Setting back stack index " + n + " to " + backStackRecord);
                }
                this.mBackStackIndices.set(n, backStackRecord);
            }
            else {
                while (i < n) {
                    this.mBackStackIndices.add(null);
                    if (this.mAvailBackStackIndices == null) {
                        this.mAvailBackStackIndices = new ArrayList<Integer>();
                    }
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "Adding available back stack index " + i);
                    }
                    this.mAvailBackStackIndices.add(i);
                    ++i;
                }
                if (FragmentManagerImpl.DEBUG) {
                    Log.v("FragmentManager", "Adding back stack index " + n + " with " + backStackRecord);
                }
                this.mBackStackIndices.add(backStackRecord);
            }
        }
    }
    
    public void setPrimaryNavigationFragment(final Fragment mPrimaryNav) {
        if (mPrimaryNav != null && (this.mActive.get(mPrimaryNav.mIndex) != mPrimaryNav || (mPrimaryNav.mHost != null && mPrimaryNav.getFragmentManager() != this))) {
            throw new IllegalArgumentException("Fragment " + mPrimaryNav + " is not an active fragment of FragmentManager " + this);
        }
        this.mPrimaryNav = mPrimaryNav;
    }
    
    public void showFragment(final Fragment fragment) {
        boolean mHiddenChanged = false;
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "show: " + fragment);
        }
        if (fragment.mHidden) {
            fragment.mHidden = false;
            if (!fragment.mHiddenChanged) {
                mHiddenChanged = true;
            }
            fragment.mHiddenChanged = mHiddenChanged;
        }
    }
    
    void startPendingDeferredFragments() {
        if (this.mActive != null) {
            for (int i = 0; i < this.mActive.size(); ++i) {
                final Fragment fragment = (Fragment)this.mActive.valueAt(i);
                if (fragment != null) {
                    this.performPendingDeferredStart(fragment);
                }
            }
        }
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        if (this.mParent != null) {
            DebugUtils.buildShortClassTag(this.mParent, sb);
        }
        else {
            DebugUtils.buildShortClassTag(this.mHost, sb);
        }
        sb.append("}}");
        return sb.toString();
    }
    
    @Override
    public void unregisterFragmentLifecycleCallbacks(final FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        if (this.mLifecycleCallbacks == null) {
            return;
        }
        while (true) {
            final CopyOnWriteArrayList<Pair<FragmentLifecycleCallbacks, Boolean>> mLifecycleCallbacks = this.mLifecycleCallbacks;
            // monitorenter(mLifecycleCallbacks)
            int n = 0;
            while (true) {
                Label_0069: {
                    try {
                        final int size = this.mLifecycleCallbacks.size();
                        if (n < size) {
                            if (this.mLifecycleCallbacks.get(n).first != fragmentLifecycleCallbacks) {
                                break Label_0069;
                            }
                            this.mLifecycleCallbacks.remove(n);
                        }
                        return;
                    }
                    finally {
                    }
                    // monitorexit(mLifecycleCallbacks)
                }
                ++n;
                continue;
            }
        }
    }
    
    static class AnimateOnHWLayerIfNeededListener implements Animation$AnimationListener
    {
        private Animation$AnimationListener mOriginalListener;
        private boolean mShouldRunOnHWLayer;
        View mView;
        
        public AnimateOnHWLayerIfNeededListener(final View mView, final Animation animation) {
            if (mView == null || animation == null) {
                return;
            }
            this.mView = mView;
        }
        
        public AnimateOnHWLayerIfNeededListener(final View mView, final Animation animation, final Animation$AnimationListener mOriginalListener) {
            if (mView == null || animation == null) {
                return;
            }
            this.mOriginalListener = mOriginalListener;
            this.mView = mView;
            this.mShouldRunOnHWLayer = true;
        }
        
        @CallSuper
        public void onAnimationEnd(final Animation animation) {
            if (this.mView != null && this.mShouldRunOnHWLayer) {
                if (ViewCompat.isAttachedToWindow(this.mView) || Build$VERSION.SDK_INT >= 24) {
                    this.mView.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            AnimateOnHWLayerIfNeededListener.this.mView.setLayerType(0, (Paint)null);
                        }
                    });
                }
                else {
                    this.mView.setLayerType(0, (Paint)null);
                }
            }
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationEnd(animation);
            }
        }
        
        public void onAnimationRepeat(final Animation animation) {
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationRepeat(animation);
            }
        }
        
        @CallSuper
        public void onAnimationStart(final Animation animation) {
            if (this.mOriginalListener != null) {
                this.mOriginalListener.onAnimationStart(animation);
            }
        }
    }
    
    static class FragmentTag
    {
        public static final int[] Fragment;
        public static final int Fragment_id = 1;
        public static final int Fragment_name = 0;
        public static final int Fragment_tag = 2;
        
        static {
            Fragment = new int[] { 16842755, 16842960, 16842961 };
        }
    }
    
    interface OpGenerator
    {
        boolean generateOps(final ArrayList<BackStackRecord> p0, final ArrayList<Boolean> p1);
    }
    
    private class PopBackStackState implements OpGenerator
    {
        final int mFlags;
        final int mId;
        final String mName;
        
        PopBackStackState(final String mName, final int mId, final int mFlags) {
            this.mName = mName;
            this.mId = mId;
            this.mFlags = mFlags;
        }
        
        @Override
        public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
            if (FragmentManagerImpl.this.mPrimaryNav != null && this.mId < 0 && this.mName == null) {
                final FragmentManager peekChildFragmentManager = FragmentManagerImpl.this.mPrimaryNav.peekChildFragmentManager();
                if (peekChildFragmentManager != null && peekChildFragmentManager.popBackStackImmediate()) {
                    return false;
                }
            }
            return FragmentManagerImpl.this.popBackStackState(list, list2, this.mName, this.mId, this.mFlags);
        }
    }
    
    static class StartEnterTransitionListener implements OnStartEnterTransitionListener
    {
        private final boolean mIsBack;
        private int mNumPostponed;
        private final BackStackRecord mRecord;
        
        StartEnterTransitionListener(final BackStackRecord mRecord, final boolean mIsBack) {
            this.mIsBack = mIsBack;
            this.mRecord = mRecord;
        }
        
        public void cancelTransaction() {
            this.mRecord.mManager.completeExecute(this.mRecord, this.mIsBack, false, false);
        }
        
        public void completeTransaction() {
            boolean b = false;
            boolean b2;
            if (this.mNumPostponed > 0) {
                b2 = true;
            }
            else {
                b2 = false;
            }
            final FragmentManagerImpl mManager = this.mRecord.mManager;
            for (int size = mManager.mAdded.size(), i = 0; i < size; ++i) {
                final Fragment fragment = mManager.mAdded.get(i);
                fragment.setOnStartEnterTransitionListener(null);
                if (b2 && fragment.isPostponed()) {
                    fragment.startPostponedEnterTransition();
                }
            }
            final FragmentManagerImpl mManager2 = this.mRecord.mManager;
            final BackStackRecord mRecord = this.mRecord;
            final boolean mIsBack = this.mIsBack;
            if (!b2) {
                b = true;
            }
            mManager2.completeExecute(mRecord, mIsBack, b, true);
        }
        
        public boolean isReady() {
            return this.mNumPostponed == 0;
        }
        
        @Override
        public void onStartEnterTransition() {
            --this.mNumPostponed;
            if (this.mNumPostponed != 0) {
                return;
            }
            this.mRecord.mManager.scheduleCommit();
        }
        
        @Override
        public void startListening() {
            ++this.mNumPostponed;
        }
    }
}
