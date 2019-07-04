// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import java.io.FileDescriptor;
import java.io.Writer;
import java.io.PrintWriter;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.support.v4.view.ViewCompat;
import android.view.View;
import java.lang.reflect.Modifier;
import android.os.Build$VERSION;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction implements BackStackEntry, OpGenerator
{
    static final int OP_ADD = 1;
    static final int OP_ATTACH = 7;
    static final int OP_DETACH = 6;
    static final int OP_HIDE = 4;
    static final int OP_NULL = 0;
    static final int OP_REMOVE = 3;
    static final int OP_REPLACE = 2;
    static final int OP_SET_PRIMARY_NAV = 8;
    static final int OP_SHOW = 5;
    static final int OP_UNSET_PRIMARY_NAV = 9;
    static final boolean SUPPORTS_TRANSITIONS;
    static final String TAG = "FragmentManager";
    boolean mAddToBackStack;
    boolean mAllowAddToBackStack;
    boolean mAllowOptimization;
    int mBreadCrumbShortTitleRes;
    CharSequence mBreadCrumbShortTitleText;
    int mBreadCrumbTitleRes;
    CharSequence mBreadCrumbTitleText;
    ArrayList<Runnable> mCommitRunnables;
    boolean mCommitted;
    int mEnterAnim;
    int mExitAnim;
    int mIndex;
    final FragmentManagerImpl mManager;
    String mName;
    ArrayList<Op> mOps;
    int mPopEnterAnim;
    int mPopExitAnim;
    ArrayList<String> mSharedElementSourceNames;
    ArrayList<String> mSharedElementTargetNames;
    int mTransition;
    int mTransitionStyle;
    
    static {
        SUPPORTS_TRANSITIONS = (Build$VERSION.SDK_INT >= 21);
    }
    
    public BackStackRecord(final FragmentManagerImpl mManager) {
        this.mOps = new ArrayList<Op>();
        this.mAllowAddToBackStack = true;
        this.mIndex = -1;
        this.mAllowOptimization = false;
        this.mManager = mManager;
    }
    
    private void doAddOp(final int n, final Fragment fragment, final String mTag, final int n2) {
        final Class<? extends Fragment> class1 = fragment.getClass();
        final int modifiers = class1.getModifiers();
        if (class1.isAnonymousClass() || !Modifier.isPublic(modifiers) || (class1.isMemberClass() && !Modifier.isStatic(modifiers))) {
            throw new IllegalStateException("Fragment " + class1.getCanonicalName() + " must be a public static class to be  properly recreated from" + " instance state.");
        }
        fragment.mFragmentManager = this.mManager;
        if (mTag != null) {
            if (fragment.mTag != null && !mTag.equals(fragment.mTag)) {
                throw new IllegalStateException("Can't change tag of fragment " + fragment + ": was " + fragment.mTag + " now " + mTag);
            }
            fragment.mTag = mTag;
        }
        if (n != 0) {
            if (n == -1) {
                throw new IllegalArgumentException("Can't add fragment " + fragment + " with tag " + mTag + " to container view with no id");
            }
            if (fragment.mFragmentId != 0 && fragment.mFragmentId != n) {
                throw new IllegalStateException("Can't change container ID of fragment " + fragment + ": was " + fragment.mFragmentId + " now " + n);
            }
            fragment.mFragmentId = n;
            fragment.mContainerId = n;
        }
        this.addOp(new Op(n2, fragment));
    }
    
    private static boolean isFragmentPostponed(final Op op) {
        final Fragment fragment = op.fragment;
        return fragment != null && fragment.mAdded && fragment.mView != null && !fragment.mDetached && !fragment.mHidden && fragment.isPostponed();
    }
    
    @Override
    public FragmentTransaction add(final int n, final Fragment fragment) {
        this.doAddOp(n, fragment, null, 1);
        return this;
    }
    
    @Override
    public FragmentTransaction add(final int n, final Fragment fragment, final String s) {
        this.doAddOp(n, fragment, s, 1);
        return this;
    }
    
    @Override
    public FragmentTransaction add(final Fragment fragment, final String s) {
        this.doAddOp(0, fragment, s, 1);
        return this;
    }
    
    void addOp(final Op op) {
        this.mOps.add(op);
        op.enterAnim = this.mEnterAnim;
        op.exitAnim = this.mExitAnim;
        op.popEnterAnim = this.mPopEnterAnim;
        op.popExitAnim = this.mPopExitAnim;
    }
    
    @Override
    public FragmentTransaction addSharedElement(final View view, final String s) {
        if (BackStackRecord.SUPPORTS_TRANSITIONS) {
            final String transitionName = ViewCompat.getTransitionName(view);
            if (transitionName == null) {
                throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements");
            }
            if (this.mSharedElementSourceNames == null) {
                this.mSharedElementSourceNames = new ArrayList<String>();
                this.mSharedElementTargetNames = new ArrayList<String>();
            }
            else {
                if (this.mSharedElementTargetNames.contains(s)) {
                    throw new IllegalArgumentException("A shared element with the target name '" + s + "' has already been added to the transaction.");
                }
                if (this.mSharedElementSourceNames.contains(transitionName)) {
                    throw new IllegalArgumentException("A shared element with the source name '" + transitionName + " has already been added to the transaction.");
                }
            }
            this.mSharedElementSourceNames.add(transitionName);
            this.mSharedElementTargetNames.add(s);
        }
        return this;
    }
    
    @Override
    public FragmentTransaction addToBackStack(final String mName) {
        if (!this.mAllowAddToBackStack) {
            throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
        }
        this.mAddToBackStack = true;
        this.mName = mName;
        return this;
    }
    
    @Override
    public FragmentTransaction attach(final Fragment fragment) {
        this.addOp(new Op(7, fragment));
        return this;
    }
    
    void bumpBackStackNesting(final int n) {
        if (this.mAddToBackStack) {
            if (FragmentManagerImpl.DEBUG) {
                Log.v("FragmentManager", "Bump nesting in " + this + " by " + n);
            }
            for (int size = this.mOps.size(), i = 0; i < size; ++i) {
                final Op op = this.mOps.get(i);
                if (op.fragment != null) {
                    final Fragment fragment = op.fragment;
                    fragment.mBackStackNesting += n;
                    if (FragmentManagerImpl.DEBUG) {
                        Log.v("FragmentManager", "Bump nesting of " + op.fragment + " to " + op.fragment.mBackStackNesting);
                    }
                }
            }
        }
    }
    
    @Override
    public int commit() {
        return this.commitInternal(false);
    }
    
    @Override
    public int commitAllowingStateLoss() {
        return this.commitInternal(true);
    }
    
    int commitInternal(final boolean b) {
        if (this.mCommitted) {
            throw new IllegalStateException("commit already called");
        }
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "Commit: " + this);
            final PrintWriter printWriter = new PrintWriter(new LogWriter("FragmentManager"));
            this.dump("  ", null, printWriter, null);
            printWriter.close();
        }
        this.mCommitted = true;
        if (this.mAddToBackStack) {
            this.mIndex = this.mManager.allocBackStackIndex(this);
        }
        else {
            this.mIndex = -1;
        }
        this.mManager.enqueueAction((FragmentManagerImpl.OpGenerator)this, b);
        return this.mIndex;
    }
    
    @Override
    public void commitNow() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction((FragmentManagerImpl.OpGenerator)this, false);
    }
    
    @Override
    public void commitNowAllowingStateLoss() {
        this.disallowAddToBackStack();
        this.mManager.execSingleAction((FragmentManagerImpl.OpGenerator)this, true);
    }
    
    @Override
    public FragmentTransaction detach(final Fragment fragment) {
        this.addOp(new Op(6, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction disallowAddToBackStack() {
        if (this.mAddToBackStack) {
            throw new IllegalStateException("This transaction is already being added to the back stack");
        }
        this.mAllowAddToBackStack = false;
        return this;
    }
    
    public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        this.dump(s, printWriter, true);
    }
    
    public void dump(final String s, final PrintWriter printWriter, final boolean b) {
        if (b) {
            printWriter.print(s);
            printWriter.print("mName=");
            printWriter.print(this.mName);
            printWriter.print(" mIndex=");
            printWriter.print(this.mIndex);
            printWriter.print(" mCommitted=");
            printWriter.println(this.mCommitted);
            if (this.mTransition != 0) {
                printWriter.print(s);
                printWriter.print("mTransition=#");
                printWriter.print(Integer.toHexString(this.mTransition));
                printWriter.print(" mTransitionStyle=#");
                printWriter.println(Integer.toHexString(this.mTransitionStyle));
            }
            if (this.mEnterAnim != 0 || this.mExitAnim != 0) {
                printWriter.print(s);
                printWriter.print("mEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mEnterAnim));
                printWriter.print(" mExitAnim=#");
                printWriter.println(Integer.toHexString(this.mExitAnim));
            }
            if (this.mPopEnterAnim != 0 || this.mPopExitAnim != 0) {
                printWriter.print(s);
                printWriter.print("mPopEnterAnim=#");
                printWriter.print(Integer.toHexString(this.mPopEnterAnim));
                printWriter.print(" mPopExitAnim=#");
                printWriter.println(Integer.toHexString(this.mPopExitAnim));
            }
            if (this.mBreadCrumbTitleRes != 0 || this.mBreadCrumbTitleText != null) {
                printWriter.print(s);
                printWriter.print("mBreadCrumbTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbTitleRes));
                printWriter.print(" mBreadCrumbTitleText=");
                printWriter.println(this.mBreadCrumbTitleText);
            }
            if (this.mBreadCrumbShortTitleRes != 0 || this.mBreadCrumbShortTitleText != null) {
                printWriter.print(s);
                printWriter.print("mBreadCrumbShortTitleRes=#");
                printWriter.print(Integer.toHexString(this.mBreadCrumbShortTitleRes));
                printWriter.print(" mBreadCrumbShortTitleText=");
                printWriter.println(this.mBreadCrumbShortTitleText);
            }
        }
        if (!this.mOps.isEmpty()) {
            printWriter.print(s);
            printWriter.println("Operations:");
            new StringBuilder().append(s).append("    ").toString();
            for (int size = this.mOps.size(), i = 0; i < size; ++i) {
                final Op op = this.mOps.get(i);
                String string = null;
                switch (op.cmd) {
                    default: {
                        string = "cmd=" + op.cmd;
                        break;
                    }
                    case 0: {
                        string = "NULL";
                        break;
                    }
                    case 1: {
                        string = "ADD";
                        break;
                    }
                    case 2: {
                        string = "REPLACE";
                        break;
                    }
                    case 3: {
                        string = "REMOVE";
                        break;
                    }
                    case 4: {
                        string = "HIDE";
                        break;
                    }
                    case 5: {
                        string = "SHOW";
                        break;
                    }
                    case 6: {
                        string = "DETACH";
                        break;
                    }
                    case 7: {
                        string = "ATTACH";
                        break;
                    }
                    case 8: {
                        string = "SET_PRIMARY_NAV";
                        break;
                    }
                    case 9: {
                        string = "UNSET_PRIMARY_NAV";
                        break;
                    }
                }
                printWriter.print(s);
                printWriter.print("  Op #");
                printWriter.print(i);
                printWriter.print(": ");
                printWriter.print(string);
                printWriter.print(" ");
                printWriter.println(op.fragment);
                if (b) {
                    if (op.enterAnim != 0 || op.exitAnim != 0) {
                        printWriter.print(s);
                        printWriter.print("enterAnim=#");
                        printWriter.print(Integer.toHexString(op.enterAnim));
                        printWriter.print(" exitAnim=#");
                        printWriter.println(Integer.toHexString(op.exitAnim));
                    }
                    if (op.popEnterAnim != 0 || op.popExitAnim != 0) {
                        printWriter.print(s);
                        printWriter.print("popEnterAnim=#");
                        printWriter.print(Integer.toHexString(op.popEnterAnim));
                        printWriter.print(" popExitAnim=#");
                        printWriter.println(Integer.toHexString(op.popExitAnim));
                    }
                }
            }
        }
    }
    
    void executeOps() {
        for (int size = this.mOps.size(), i = 0; i < size; ++i) {
            final Op op = this.mOps.get(i);
            final Fragment fragment = op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(this.mTransition, this.mTransitionStyle);
            }
            switch (op.cmd) {
                default: {
                    throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
                }
                case 1: {
                    fragment.setNextAnim(op.enterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                }
                case 3: {
                    fragment.setNextAnim(op.exitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                }
                case 4: {
                    fragment.setNextAnim(op.exitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                }
                case 5: {
                    fragment.setNextAnim(op.enterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                }
                case 6: {
                    fragment.setNextAnim(op.exitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                }
                case 7: {
                    fragment.setNextAnim(op.enterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                }
                case 8: {
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                }
                case 9: {
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                }
            }
            if (!this.mAllowOptimization && op.cmd != 1 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mAllowOptimization) {
            this.mManager.moveToState(this.mManager.mCurState, true);
        }
    }
    
    void executePopOps(final boolean b) {
        for (int i = this.mOps.size() - 1; i >= 0; --i) {
            final Op op = this.mOps.get(i);
            final Fragment fragment = op.fragment;
            if (fragment != null) {
                fragment.setNextTransition(FragmentManagerImpl.reverseTransit(this.mTransition), this.mTransitionStyle);
            }
            switch (op.cmd) {
                default: {
                    throw new IllegalArgumentException("Unknown cmd: " + op.cmd);
                }
                case 1: {
                    fragment.setNextAnim(op.popExitAnim);
                    this.mManager.removeFragment(fragment);
                    break;
                }
                case 3: {
                    fragment.setNextAnim(op.popEnterAnim);
                    this.mManager.addFragment(fragment, false);
                    break;
                }
                case 4: {
                    fragment.setNextAnim(op.popEnterAnim);
                    this.mManager.showFragment(fragment);
                    break;
                }
                case 5: {
                    fragment.setNextAnim(op.popExitAnim);
                    this.mManager.hideFragment(fragment);
                    break;
                }
                case 6: {
                    fragment.setNextAnim(op.popEnterAnim);
                    this.mManager.attachFragment(fragment);
                    break;
                }
                case 7: {
                    fragment.setNextAnim(op.popExitAnim);
                    this.mManager.detachFragment(fragment);
                    break;
                }
                case 8: {
                    this.mManager.setPrimaryNavigationFragment(null);
                    break;
                }
                case 9: {
                    this.mManager.setPrimaryNavigationFragment(fragment);
                    break;
                }
            }
            if (!this.mAllowOptimization && op.cmd != 3 && fragment != null) {
                this.mManager.moveFragmentToExpectedState(fragment);
            }
        }
        if (!this.mAllowOptimization && b) {
            this.mManager.moveToState(this.mManager.mCurState, true);
        }
    }
    
    Fragment expandOps(final ArrayList<Fragment> list, Fragment fragment) {
        int i = 0;
        Fragment fragment2 = fragment;
    Label_0094_Outer:
        while (i < this.mOps.size()) {
            final Op op = this.mOps.get(i);
            int n = i;
            fragment = fragment2;
            while (true) {
                switch (op.cmd) {
                    default: {
                        fragment = fragment2;
                        n = i;
                        break Label_0094;
                    }
                    case 8: {
                        this.mOps.add(i, new Op(9, fragment2));
                        n = i + 1;
                        fragment = op.fragment;
                        break Label_0094;
                    }
                    case 1:
                    case 7: {
                        list.add(op.fragment);
                        n = i;
                        fragment = fragment2;
                    }
                    case 4:
                    case 5: {
                        i = n + 1;
                        fragment2 = fragment;
                        continue Label_0094_Outer;
                    }
                    case 3:
                    case 6: {
                        list.remove(op.fragment);
                        n = i;
                        fragment = fragment2;
                        if (op.fragment == fragment2) {
                            this.mOps.add(i, new Op(9, op.fragment));
                            n = i + 1;
                            fragment = null;
                        }
                        continue;
                    }
                    case 2: {
                        final Fragment fragment3 = op.fragment;
                        final int mContainerId = fragment3.mContainerId;
                        int n2 = 0;
                        final int n3 = list.size() - 1;
                        fragment = fragment2;
                        n = i;
                        int n4;
                        int n5;
                        Fragment fragment5;
                        for (int j = n3; j >= 0; --j, n2 = n4, n = n5, fragment = fragment5) {
                            final Fragment fragment4 = list.get(j);
                            n4 = n2;
                            n5 = n;
                            fragment5 = fragment;
                            if (fragment4.mContainerId == mContainerId) {
                                if (fragment4 == fragment3) {
                                    n4 = 1;
                                    fragment5 = fragment;
                                    n5 = n;
                                }
                                else {
                                    int n6 = n;
                                    if (fragment4 == (fragment5 = fragment)) {
                                        this.mOps.add(n, new Op(9, fragment4));
                                        n6 = n + 1;
                                        fragment5 = null;
                                    }
                                    final Op op2 = new Op(3, fragment4);
                                    op2.enterAnim = op.enterAnim;
                                    op2.popEnterAnim = op.popEnterAnim;
                                    op2.exitAnim = op.exitAnim;
                                    op2.popExitAnim = op.popExitAnim;
                                    this.mOps.add(n6, op2);
                                    list.remove(fragment4);
                                    n5 = n6 + 1;
                                    n4 = n2;
                                }
                            }
                        }
                        if (n2 != 0) {
                            this.mOps.remove(n);
                            --n;
                            continue;
                        }
                        op.cmd = 1;
                        list.add(fragment3);
                        continue;
                    }
                }
                break;
            }
        }
        return fragment2;
    }
    
    @Override
    public boolean generateOps(final ArrayList<BackStackRecord> list, final ArrayList<Boolean> list2) {
        if (FragmentManagerImpl.DEBUG) {
            Log.v("FragmentManager", "Run: " + this);
        }
        list.add(this);
        list2.add(false);
        if (this.mAddToBackStack) {
            this.mManager.addBackStackState(this);
        }
        return true;
    }
    
    @Override
    public CharSequence getBreadCrumbShortTitle() {
        if (this.mBreadCrumbShortTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes);
        }
        return this.mBreadCrumbShortTitleText;
    }
    
    @Override
    public int getBreadCrumbShortTitleRes() {
        return this.mBreadCrumbShortTitleRes;
    }
    
    @Override
    public CharSequence getBreadCrumbTitle() {
        if (this.mBreadCrumbTitleRes != 0) {
            return this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes);
        }
        return this.mBreadCrumbTitleText;
    }
    
    @Override
    public int getBreadCrumbTitleRes() {
        return this.mBreadCrumbTitleRes;
    }
    
    @Override
    public int getId() {
        return this.mIndex;
    }
    
    @Override
    public String getName() {
        return this.mName;
    }
    
    public int getTransition() {
        return this.mTransition;
    }
    
    public int getTransitionStyle() {
        return this.mTransitionStyle;
    }
    
    @Override
    public FragmentTransaction hide(final Fragment fragment) {
        this.addOp(new Op(4, fragment));
        return this;
    }
    
    boolean interactsWith(final int n) {
        final boolean b = false;
        final int size = this.mOps.size();
        int n2 = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n2 >= size) {
                break;
            }
            final Op op = this.mOps.get(n2);
            int mContainerId;
            if (op.fragment != null) {
                mContainerId = op.fragment.mContainerId;
            }
            else {
                mContainerId = 0;
            }
            if (mContainerId != 0 && mContainerId == n) {
                b2 = true;
                break;
            }
            ++n2;
        }
        return b2;
    }
    
    boolean interactsWith(final ArrayList<BackStackRecord> list, final int n, final int n2) {
        if (n2 == n) {
            return false;
        }
        final int size = this.mOps.size();
        int n3 = -1;
        int n4;
        for (int i = 0; i < size; ++i, n3 = n4) {
            final Op op = this.mOps.get(i);
            int mContainerId;
            if (op.fragment != null) {
                mContainerId = op.fragment.mContainerId;
            }
            else {
                mContainerId = 0;
            }
            n4 = n3;
            if (mContainerId != 0 && mContainerId != (n4 = n3)) {
                final int n5 = mContainerId;
                int n6 = n;
                while (true) {
                    n4 = n5;
                    if (n6 >= n2) {
                        break;
                    }
                    final BackStackRecord backStackRecord = list.get(n6);
                    for (int size2 = backStackRecord.mOps.size(), j = 0; j < size2; ++j) {
                        final Op op2 = backStackRecord.mOps.get(j);
                        int mContainerId2;
                        if (op2.fragment != null) {
                            mContainerId2 = op2.fragment.mContainerId;
                        }
                        else {
                            mContainerId2 = 0;
                        }
                        if (mContainerId2 == mContainerId) {
                            return true;
                        }
                    }
                    ++n6;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean isAddToBackStackAllowed() {
        return this.mAllowAddToBackStack;
    }
    
    @Override
    public boolean isEmpty() {
        return this.mOps.isEmpty();
    }
    
    boolean isPostponed() {
        for (int i = 0; i < this.mOps.size(); ++i) {
            if (isFragmentPostponed(this.mOps.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public FragmentTransaction postOnCommit(final Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable cannot be null");
        }
        this.disallowAddToBackStack();
        if (this.mCommitRunnables == null) {
            this.mCommitRunnables = new ArrayList<Runnable>();
        }
        this.mCommitRunnables.add(runnable);
        return this;
    }
    
    @Override
    public FragmentTransaction remove(final Fragment fragment) {
        this.addOp(new Op(3, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction replace(final int n, final Fragment fragment) {
        return this.replace(n, fragment, null);
    }
    
    @Override
    public FragmentTransaction replace(final int n, final Fragment fragment, final String s) {
        if (n == 0) {
            throw new IllegalArgumentException("Must use non-zero containerViewId");
        }
        this.doAddOp(n, fragment, s, 2);
        return this;
    }
    
    public void runOnCommitRunnables() {
        if (this.mCommitRunnables != null) {
            for (int i = 0; i < this.mCommitRunnables.size(); ++i) {
                this.mCommitRunnables.get(i).run();
            }
            this.mCommitRunnables = null;
        }
    }
    
    @Override
    public FragmentTransaction setAllowOptimization(final boolean mAllowOptimization) {
        this.mAllowOptimization = mAllowOptimization;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbShortTitle(final int mBreadCrumbShortTitleRes) {
        this.mBreadCrumbShortTitleRes = mBreadCrumbShortTitleRes;
        this.mBreadCrumbShortTitleText = null;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbShortTitle(final CharSequence mBreadCrumbShortTitleText) {
        this.mBreadCrumbShortTitleRes = 0;
        this.mBreadCrumbShortTitleText = mBreadCrumbShortTitleText;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbTitle(final int mBreadCrumbTitleRes) {
        this.mBreadCrumbTitleRes = mBreadCrumbTitleRes;
        this.mBreadCrumbTitleText = null;
        return this;
    }
    
    @Override
    public FragmentTransaction setBreadCrumbTitle(final CharSequence mBreadCrumbTitleText) {
        this.mBreadCrumbTitleRes = 0;
        this.mBreadCrumbTitleText = mBreadCrumbTitleText;
        return this;
    }
    
    @Override
    public FragmentTransaction setCustomAnimations(final int n, final int n2) {
        return this.setCustomAnimations(n, n2, 0, 0);
    }
    
    @Override
    public FragmentTransaction setCustomAnimations(final int mEnterAnim, final int mExitAnim, final int mPopEnterAnim, final int mPopExitAnim) {
        this.mEnterAnim = mEnterAnim;
        this.mExitAnim = mExitAnim;
        this.mPopEnterAnim = mPopEnterAnim;
        this.mPopExitAnim = mPopExitAnim;
        return this;
    }
    
    void setOnStartPostponedListener(final Fragment.OnStartEnterTransitionListener onStartEnterTransitionListener) {
        for (int i = 0; i < this.mOps.size(); ++i) {
            final Op op = this.mOps.get(i);
            if (isFragmentPostponed(op)) {
                op.fragment.setOnStartEnterTransitionListener(onStartEnterTransitionListener);
            }
        }
    }
    
    @Override
    public FragmentTransaction setPrimaryNavigationFragment(final Fragment fragment) {
        this.addOp(new Op(8, fragment));
        return this;
    }
    
    @Override
    public FragmentTransaction setTransition(final int mTransition) {
        this.mTransition = mTransition;
        return this;
    }
    
    @Override
    public FragmentTransaction setTransitionStyle(final int mTransitionStyle) {
        this.mTransitionStyle = mTransitionStyle;
        return this;
    }
    
    @Override
    public FragmentTransaction show(final Fragment fragment) {
        this.addOp(new Op(5, fragment));
        return this;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        sb.append("BackStackEntry{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        if (this.mIndex >= 0) {
            sb.append(" #");
            sb.append(this.mIndex);
        }
        if (this.mName != null) {
            sb.append(" ");
            sb.append(this.mName);
        }
        sb.append("}");
        return sb.toString();
    }
    
    Fragment trackAddedFragmentsInPop(final ArrayList<Fragment> list, Fragment fragment) {
        int i = 0;
        Fragment fragment2 = fragment;
        while (i < this.mOps.size()) {
            final Op op = this.mOps.get(i);
            fragment = fragment2;
            Label_0091: {
                switch (op.cmd) {
                    default: {
                        fragment = fragment2;
                        break Label_0091;
                    }
                    case 8: {
                        fragment = null;
                        break Label_0091;
                    }
                    case 9: {
                        fragment = op.fragment;
                        break Label_0091;
                    }
                    case 3:
                    case 6: {
                        list.add(op.fragment);
                        fragment = fragment2;
                        break Label_0091;
                    }
                    case 1:
                    case 7: {
                        list.remove(op.fragment);
                        fragment = fragment2;
                    }
                    case 2:
                    case 4:
                    case 5: {
                        ++i;
                        fragment2 = fragment;
                        continue;
                    }
                }
            }
        }
        return fragment2;
    }
    
    static final class Op
    {
        int cmd;
        int enterAnim;
        int exitAnim;
        Fragment fragment;
        int popEnterAnim;
        int popExitAnim;
        
        Op() {
        }
        
        Op(final int cmd, final Fragment fragment) {
            this.cmd = cmd;
            this.fragment = fragment;
        }
    }
}
