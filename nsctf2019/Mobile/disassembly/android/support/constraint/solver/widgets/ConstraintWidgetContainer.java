// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.SolverVariable;
import java.util.Arrays;
import android.support.constraint.solver.LinearSystem;

public class ConstraintWidgetContainer extends WidgetContainer
{
    static boolean ALLOW_ROOT_GROUP = false;
    private static final int CHAIN_FIRST = 0;
    private static final int CHAIN_FIRST_VISIBLE = 2;
    private static final int CHAIN_LAST = 1;
    private static final int CHAIN_LAST_VISIBLE = 3;
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_OPTIMIZE = false;
    private static final int FLAG_CHAIN_DANGLING = 1;
    private static final int FLAG_CHAIN_OPTIMIZE = 0;
    private static final int FLAG_RECOMPUTE_BOUNDS = 2;
    private static final int MAX_ITERATIONS = 8;
    public static final int OPTIMIZATION_ALL = 2;
    public static final int OPTIMIZATION_BASIC = 4;
    public static final int OPTIMIZATION_CHAIN = 8;
    public static final int OPTIMIZATION_NONE = 1;
    private static final boolean USE_SNAPSHOT = true;
    private static final boolean USE_THREAD = false;
    private boolean[] flags;
    protected LinearSystem mBackgroundSystem;
    private ConstraintWidget[] mChainEnds;
    private boolean mHeightMeasuredTooSmall;
    private ConstraintWidget[] mHorizontalChainsArray;
    private int mHorizontalChainsSize;
    private ConstraintWidget[] mMatchConstraintsChainedWidgets;
    private int mOptimizationLevel;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem;
    private ConstraintWidget[] mVerticalChainsArray;
    private int mVerticalChainsSize;
    private boolean mWidthMeasuredTooSmall;
    int mWrapHeight;
    int mWrapWidth;
    
    static {
        ConstraintWidgetContainer.ALLOW_ROOT_GROUP = true;
    }
    
    public ConstraintWidgetContainer() {
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    public ConstraintWidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
        this.mOptimizationLevel = 2;
        this.flags = new boolean[3];
        this.mChainEnds = new ConstraintWidget[4];
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
    }
    
    private void addHorizontalChain(final ConstraintWidget constraintWidget) {
        for (int i = 0; i < this.mHorizontalChainsSize; ++i) {
            if (this.mHorizontalChainsArray[i] == constraintWidget) {
                return;
            }
        }
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = constraintWidget;
        ++this.mHorizontalChainsSize;
    }
    
    private void addVerticalChain(final ConstraintWidget constraintWidget) {
        for (int i = 0; i < this.mVerticalChainsSize; ++i) {
            if (this.mVerticalChainsArray[i] == constraintWidget) {
                return;
            }
        }
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = constraintWidget;
        ++this.mVerticalChainsSize;
    }
    
    private void applyHorizontalChain(final LinearSystem linearSystem) {
        for (int i = 0; i < this.mHorizontalChainsSize; ++i) {
            final ConstraintWidget constraintWidget = this.mHorizontalChainsArray[i];
            final int countMatchConstraintsChainedWidgets = this.countMatchConstraintsChainedWidgets(linearSystem, this.mChainEnds, this.mHorizontalChainsArray[i], 0, this.flags);
            ConstraintWidget mHorizontalNextWidget = this.mChainEnds[2];
            if (mHorizontalNextWidget != null) {
                if (this.flags[1]) {
                    int drawX = constraintWidget.getDrawX();
                    while (mHorizontalNextWidget != null) {
                        linearSystem.addEquality(mHorizontalNextWidget.mLeft.mSolverVariable, drawX);
                        final ConstraintWidget mHorizontalNextWidget2 = mHorizontalNextWidget.mHorizontalNextWidget;
                        drawX += mHorizontalNextWidget.mLeft.getMargin() + mHorizontalNextWidget.getWidth() + mHorizontalNextWidget.mRight.getMargin();
                        mHorizontalNextWidget = mHorizontalNextWidget2;
                    }
                }
                else {
                    final boolean b = constraintWidget.mHorizontalChainStyle == 0;
                    boolean b2;
                    if (constraintWidget.mHorizontalChainStyle == 2) {
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    boolean b3;
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        b3 = true;
                    }
                    else {
                        b3 = false;
                    }
                    if ((this.mOptimizationLevel == 2 || this.mOptimizationLevel == 8) && this.flags[0] && constraintWidget.mHorizontalChainFixedPosition && !b2 && !b3 && constraintWidget.mHorizontalChainStyle == 0) {
                        Optimizer.applyDirectResolutionHorizontalChain(this, linearSystem, countMatchConstraintsChainedWidgets, constraintWidget);
                    }
                    else if (countMatchConstraintsChainedWidgets == 0 || b2) {
                        ConstraintWidget constraintWidget2 = null;
                        ConstraintWidget constraintWidget3 = null;
                        boolean b4 = false;
                        ConstraintWidget constraintWidget4 = mHorizontalNextWidget;
                        while (true) {
                            final ConstraintWidget constraintWidget5 = constraintWidget4;
                            if (constraintWidget5 == null) {
                                break;
                            }
                            final ConstraintWidget mHorizontalNextWidget3 = constraintWidget5.mHorizontalNextWidget;
                            if (mHorizontalNextWidget3 == null) {
                                constraintWidget3 = this.mChainEnds[1];
                                b4 = true;
                            }
                            ConstraintWidget constraintWidget6;
                            if (b2) {
                                final ConstraintAnchor mLeft = constraintWidget5.mLeft;
                                int margin = mLeft.getMargin();
                                if (constraintWidget2 != null) {
                                    margin += constraintWidget2.mRight.getMargin();
                                }
                                int n = 1;
                                if (mHorizontalNextWidget != constraintWidget5) {
                                    n = 3;
                                }
                                linearSystem.addGreaterThan(mLeft.mSolverVariable, mLeft.mTarget.mSolverVariable, margin, n);
                                constraintWidget6 = mHorizontalNextWidget3;
                                if (constraintWidget5.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    final ConstraintAnchor mRight = constraintWidget5.mRight;
                                    if (constraintWidget5.mMatchConstraintDefaultWidth == 1) {
                                        linearSystem.addEquality(mRight.mSolverVariable, mLeft.mSolverVariable, Math.max(constraintWidget5.mMatchConstraintMinWidth, constraintWidget5.getWidth()), 3);
                                        constraintWidget6 = mHorizontalNextWidget3;
                                    }
                                    else {
                                        linearSystem.addGreaterThan(mLeft.mSolverVariable, mLeft.mTarget.mSolverVariable, mLeft.mMargin, 3);
                                        linearSystem.addLowerThan(mRight.mSolverVariable, mLeft.mSolverVariable, constraintWidget5.mMatchConstraintMinWidth, 3);
                                        constraintWidget6 = mHorizontalNextWidget3;
                                    }
                                }
                            }
                            else if (!b && b4 && constraintWidget2 != null) {
                                if (constraintWidget5.mRight.mTarget == null) {
                                    linearSystem.addEquality(constraintWidget5.mRight.mSolverVariable, constraintWidget5.getDrawRight());
                                    constraintWidget6 = mHorizontalNextWidget3;
                                }
                                else {
                                    linearSystem.addEquality(constraintWidget5.mRight.mSolverVariable, constraintWidget3.mRight.mTarget.mSolverVariable, -constraintWidget5.mRight.getMargin(), 5);
                                    constraintWidget6 = mHorizontalNextWidget3;
                                }
                            }
                            else if (!b && !b4 && constraintWidget2 == null) {
                                if (constraintWidget5.mLeft.mTarget == null) {
                                    linearSystem.addEquality(constraintWidget5.mLeft.mSolverVariable, constraintWidget5.getDrawX());
                                    constraintWidget6 = mHorizontalNextWidget3;
                                }
                                else {
                                    linearSystem.addEquality(constraintWidget5.mLeft.mSolverVariable, constraintWidget.mLeft.mTarget.mSolverVariable, constraintWidget5.mLeft.getMargin(), 5);
                                    constraintWidget6 = mHorizontalNextWidget3;
                                }
                            }
                            else {
                                final ConstraintAnchor mLeft2 = constraintWidget5.mLeft;
                                final ConstraintAnchor mRight2 = constraintWidget5.mRight;
                                final int margin2 = mLeft2.getMargin();
                                final int margin3 = mRight2.getMargin();
                                linearSystem.addGreaterThan(mLeft2.mSolverVariable, mLeft2.mTarget.mSolverVariable, margin2, 1);
                                linearSystem.addLowerThan(mRight2.mSolverVariable, mRight2.mTarget.mSolverVariable, -margin3, 1);
                                SolverVariable solverVariable;
                                if (mLeft2.mTarget != null) {
                                    solverVariable = mLeft2.mTarget.mSolverVariable;
                                }
                                else {
                                    solverVariable = null;
                                }
                                if (constraintWidget2 == null) {
                                    if (constraintWidget.mLeft.mTarget != null) {
                                        solverVariable = constraintWidget.mLeft.mTarget.mSolverVariable;
                                    }
                                    else {
                                        solverVariable = null;
                                    }
                                }
                                ConstraintWidget mOwner;
                                if ((mOwner = mHorizontalNextWidget3) == null) {
                                    if (constraintWidget3.mRight.mTarget != null) {
                                        mOwner = constraintWidget3.mRight.mTarget.mOwner;
                                    }
                                    else {
                                        mOwner = null;
                                    }
                                }
                                if ((constraintWidget6 = mOwner) != null) {
                                    SolverVariable solverVariable2 = mOwner.mLeft.mSolverVariable;
                                    if (b4) {
                                        if (constraintWidget3.mRight.mTarget != null) {
                                            solverVariable2 = constraintWidget3.mRight.mTarget.mSolverVariable;
                                        }
                                        else {
                                            solverVariable2 = null;
                                        }
                                    }
                                    constraintWidget6 = mOwner;
                                    if (solverVariable != null) {
                                        constraintWidget6 = mOwner;
                                        if (solverVariable2 != null) {
                                            linearSystem.addCentering(mLeft2.mSolverVariable, solverVariable, margin2, 0.5f, solverVariable2, mRight2.mSolverVariable, margin3, 4);
                                            constraintWidget6 = mOwner;
                                        }
                                    }
                                }
                            }
                            if (b4) {
                                constraintWidget4 = null;
                            }
                            else {
                                constraintWidget4 = constraintWidget6;
                            }
                            constraintWidget2 = constraintWidget5;
                        }
                        if (b2) {
                            final ConstraintAnchor mLeft3 = mHorizontalNextWidget.mLeft;
                            final ConstraintAnchor mRight3 = constraintWidget3.mRight;
                            final int margin4 = mLeft3.getMargin();
                            final int margin5 = mRight3.getMargin();
                            SolverVariable mSolverVariable;
                            if (constraintWidget.mLeft.mTarget != null) {
                                mSolverVariable = constraintWidget.mLeft.mTarget.mSolverVariable;
                            }
                            else {
                                mSolverVariable = null;
                            }
                            SolverVariable mSolverVariable2;
                            if (constraintWidget3.mRight.mTarget != null) {
                                mSolverVariable2 = constraintWidget3.mRight.mTarget.mSolverVariable;
                            }
                            else {
                                mSolverVariable2 = null;
                            }
                            if (mSolverVariable != null && mSolverVariable2 != null) {
                                linearSystem.addLowerThan(mRight3.mSolverVariable, mSolverVariable2, -margin5, 1);
                                linearSystem.addCentering(mLeft3.mSolverVariable, mSolverVariable, margin4, constraintWidget.mHorizontalBiasPercent, mSolverVariable2, mRight3.mSolverVariable, margin5, 4);
                            }
                        }
                    }
                    else {
                        ConstraintWidget constraintWidget7 = null;
                        float n2 = 0.0f;
                        while (mHorizontalNextWidget != null) {
                            if (mHorizontalNextWidget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                                int margin6 = mHorizontalNextWidget.mLeft.getMargin();
                                if (constraintWidget7 != null) {
                                    margin6 += constraintWidget7.mRight.getMargin();
                                }
                                int n3 = 3;
                                if (mHorizontalNextWidget.mLeft.mTarget.mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n3 = 2;
                                }
                                linearSystem.addGreaterThan(mHorizontalNextWidget.mLeft.mSolverVariable, mHorizontalNextWidget.mLeft.mTarget.mSolverVariable, margin6, n3);
                                int margin7;
                                final int n4 = margin7 = mHorizontalNextWidget.mRight.getMargin();
                                if (mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.mTarget != null) {
                                    margin7 = n4;
                                    if (mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == mHorizontalNextWidget) {
                                        margin7 = n4 + mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.getMargin();
                                    }
                                }
                                int n5 = 3;
                                if (mHorizontalNextWidget.mRight.mTarget.mOwner.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n5 = 2;
                                }
                                linearSystem.addLowerThan(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mRight.mTarget.mSolverVariable, -margin7, n5);
                            }
                            else {
                                n2 += mHorizontalNextWidget.mHorizontalWeight;
                                int margin8 = 0;
                                if (mHorizontalNextWidget.mRight.mTarget != null) {
                                    margin8 = mHorizontalNextWidget.mRight.getMargin();
                                    if (mHorizontalNextWidget != this.mChainEnds[3]) {
                                        margin8 += mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.getMargin();
                                    }
                                }
                                linearSystem.addGreaterThan(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mLeft.mSolverVariable, 0, 1);
                                linearSystem.addLowerThan(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mRight.mTarget.mSolverVariable, -margin8, 1);
                            }
                            constraintWidget7 = mHorizontalNextWidget;
                            mHorizontalNextWidget = mHorizontalNextWidget.mHorizontalNextWidget;
                        }
                        if (countMatchConstraintsChainedWidgets == 1) {
                            final ConstraintWidget constraintWidget8 = this.mMatchConstraintsChainedWidgets[0];
                            int margin9;
                            final int n6 = margin9 = constraintWidget8.mLeft.getMargin();
                            if (constraintWidget8.mLeft.mTarget != null) {
                                margin9 = n6 + constraintWidget8.mLeft.mTarget.getMargin();
                            }
                            int margin10;
                            final int n7 = margin10 = constraintWidget8.mRight.getMargin();
                            if (constraintWidget8.mRight.mTarget != null) {
                                margin10 = n7 + constraintWidget8.mRight.mTarget.getMargin();
                            }
                            SolverVariable solverVariable3 = constraintWidget.mRight.mTarget.mSolverVariable;
                            if (constraintWidget8 == this.mChainEnds[3]) {
                                solverVariable3 = this.mChainEnds[1].mRight.mTarget.mSolverVariable;
                            }
                            if (constraintWidget8.mMatchConstraintDefaultWidth == 1) {
                                linearSystem.addGreaterThan(constraintWidget.mLeft.mSolverVariable, constraintWidget.mLeft.mTarget.mSolverVariable, margin9, 1);
                                linearSystem.addLowerThan(constraintWidget.mRight.mSolverVariable, solverVariable3, -margin10, 1);
                                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, constraintWidget.mLeft.mSolverVariable, constraintWidget.getWidth(), 2);
                            }
                            else {
                                linearSystem.addEquality(constraintWidget8.mLeft.mSolverVariable, constraintWidget8.mLeft.mTarget.mSolverVariable, margin9, 1);
                                linearSystem.addEquality(constraintWidget8.mRight.mSolverVariable, solverVariable3, -margin10, 1);
                            }
                        }
                        else {
                            for (int j = 0; j < countMatchConstraintsChainedWidgets - 1; ++j) {
                                final ConstraintWidget constraintWidget9 = this.mMatchConstraintsChainedWidgets[j];
                                final ConstraintWidget constraintWidget10 = this.mMatchConstraintsChainedWidgets[j + 1];
                                final SolverVariable mSolverVariable3 = constraintWidget9.mLeft.mSolverVariable;
                                final SolverVariable mSolverVariable4 = constraintWidget9.mRight.mSolverVariable;
                                final SolverVariable mSolverVariable5 = constraintWidget10.mLeft.mSolverVariable;
                                SolverVariable solverVariable4 = constraintWidget10.mRight.mSolverVariable;
                                if (constraintWidget10 == this.mChainEnds[3]) {
                                    solverVariable4 = this.mChainEnds[1].mRight.mSolverVariable;
                                }
                                int margin11;
                                final int n8 = margin11 = constraintWidget9.mLeft.getMargin();
                                if (constraintWidget9.mLeft.mTarget != null) {
                                    margin11 = n8;
                                    if (constraintWidget9.mLeft.mTarget.mOwner.mRight.mTarget != null) {
                                        margin11 = n8;
                                        if (constraintWidget9.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == constraintWidget9) {
                                            margin11 = n8 + constraintWidget9.mLeft.mTarget.mOwner.mRight.getMargin();
                                        }
                                    }
                                }
                                linearSystem.addGreaterThan(mSolverVariable3, constraintWidget9.mLeft.mTarget.mSolverVariable, margin11, 2);
                                int margin12;
                                final int n9 = margin12 = constraintWidget9.mRight.getMargin();
                                if (constraintWidget9.mRight.mTarget != null) {
                                    margin12 = n9;
                                    if (constraintWidget9.mHorizontalNextWidget != null) {
                                        int margin13;
                                        if (constraintWidget9.mHorizontalNextWidget.mLeft.mTarget != null) {
                                            margin13 = constraintWidget9.mHorizontalNextWidget.mLeft.getMargin();
                                        }
                                        else {
                                            margin13 = 0;
                                        }
                                        margin12 = n9 + margin13;
                                    }
                                }
                                linearSystem.addLowerThan(mSolverVariable4, constraintWidget9.mRight.mTarget.mSolverVariable, -margin12, 2);
                                if (j + 1 == countMatchConstraintsChainedWidgets - 1) {
                                    int margin14;
                                    final int n10 = margin14 = constraintWidget10.mLeft.getMargin();
                                    if (constraintWidget10.mLeft.mTarget != null) {
                                        margin14 = n10;
                                        if (constraintWidget10.mLeft.mTarget.mOwner.mRight.mTarget != null) {
                                            margin14 = n10;
                                            if (constraintWidget10.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == constraintWidget10) {
                                                margin14 = n10 + constraintWidget10.mLeft.mTarget.mOwner.mRight.getMargin();
                                            }
                                        }
                                    }
                                    linearSystem.addGreaterThan(mSolverVariable5, constraintWidget10.mLeft.mTarget.mSolverVariable, margin14, 2);
                                    ConstraintAnchor constraintAnchor = constraintWidget10.mRight;
                                    if (constraintWidget10 == this.mChainEnds[3]) {
                                        constraintAnchor = this.mChainEnds[1].mRight;
                                    }
                                    int margin15;
                                    final int n11 = margin15 = constraintAnchor.getMargin();
                                    if (constraintAnchor.mTarget != null) {
                                        margin15 = n11;
                                        if (constraintAnchor.mTarget.mOwner.mLeft.mTarget != null) {
                                            margin15 = n11;
                                            if (constraintAnchor.mTarget.mOwner.mLeft.mTarget.mOwner == constraintWidget10) {
                                                margin15 = n11 + constraintAnchor.mTarget.mOwner.mLeft.getMargin();
                                            }
                                        }
                                    }
                                    linearSystem.addLowerThan(solverVariable4, constraintAnchor.mTarget.mSolverVariable, -margin15, 2);
                                }
                                if (constraintWidget.mMatchConstraintMaxWidth > 0) {
                                    linearSystem.addLowerThan(mSolverVariable4, mSolverVariable3, constraintWidget.mMatchConstraintMaxWidth, 2);
                                }
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualDimension(constraintWidget9.mHorizontalWeight, n2, constraintWidget10.mHorizontalWeight, mSolverVariable3, constraintWidget9.mLeft.getMargin(), mSolverVariable4, constraintWidget9.mRight.getMargin(), mSolverVariable5, constraintWidget10.mLeft.getMargin(), solverVariable4, constraintWidget10.mRight.getMargin());
                                linearSystem.addConstraint(row);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void applyVerticalChain(final LinearSystem linearSystem) {
        for (int i = 0; i < this.mVerticalChainsSize; ++i) {
            final ConstraintWidget constraintWidget = this.mVerticalChainsArray[i];
            final int countMatchConstraintsChainedWidgets = this.countMatchConstraintsChainedWidgets(linearSystem, this.mChainEnds, this.mVerticalChainsArray[i], 1, this.flags);
            ConstraintWidget mVerticalNextWidget = this.mChainEnds[2];
            if (mVerticalNextWidget != null) {
                if (this.flags[1]) {
                    int drawY = constraintWidget.getDrawY();
                    while (mVerticalNextWidget != null) {
                        linearSystem.addEquality(mVerticalNextWidget.mTop.mSolverVariable, drawY);
                        final ConstraintWidget mVerticalNextWidget2 = mVerticalNextWidget.mVerticalNextWidget;
                        drawY += mVerticalNextWidget.mTop.getMargin() + mVerticalNextWidget.getHeight() + mVerticalNextWidget.mBottom.getMargin();
                        mVerticalNextWidget = mVerticalNextWidget2;
                    }
                }
                else {
                    final boolean b = constraintWidget.mVerticalChainStyle == 0;
                    boolean b2;
                    if (constraintWidget.mVerticalChainStyle == 2) {
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    boolean b3;
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        b3 = true;
                    }
                    else {
                        b3 = false;
                    }
                    if ((this.mOptimizationLevel == 2 || this.mOptimizationLevel == 8) && this.flags[0] && constraintWidget.mVerticalChainFixedPosition && !b2 && !b3 && constraintWidget.mVerticalChainStyle == 0) {
                        Optimizer.applyDirectResolutionVerticalChain(this, linearSystem, countMatchConstraintsChainedWidgets, constraintWidget);
                    }
                    else if (countMatchConstraintsChainedWidgets == 0 || b2) {
                        ConstraintWidget constraintWidget2 = null;
                        ConstraintWidget constraintWidget3 = null;
                        boolean b4 = false;
                        ConstraintWidget constraintWidget4 = mVerticalNextWidget;
                        while (true) {
                            final ConstraintWidget constraintWidget5 = constraintWidget4;
                            if (constraintWidget5 == null) {
                                break;
                            }
                            final ConstraintWidget mVerticalNextWidget3 = constraintWidget5.mVerticalNextWidget;
                            if (mVerticalNextWidget3 == null) {
                                constraintWidget3 = this.mChainEnds[1];
                                b4 = true;
                            }
                            ConstraintWidget constraintWidget6;
                            if (b2) {
                                final ConstraintAnchor mTop = constraintWidget5.mTop;
                                int margin;
                                final int n = margin = mTop.getMargin();
                                if (constraintWidget2 != null) {
                                    margin = n + constraintWidget2.mBottom.getMargin();
                                }
                                int n2 = 1;
                                if (mVerticalNextWidget != constraintWidget5) {
                                    n2 = 3;
                                }
                                SolverVariable solverVariable = null;
                                SolverVariable solverVariable2 = null;
                                int n3;
                                if (mTop.mTarget != null) {
                                    solverVariable = mTop.mSolverVariable;
                                    solverVariable2 = mTop.mTarget.mSolverVariable;
                                    n3 = margin;
                                }
                                else {
                                    n3 = margin;
                                    if (constraintWidget5.mBaseline.mTarget != null) {
                                        solverVariable = constraintWidget5.mBaseline.mSolverVariable;
                                        solverVariable2 = constraintWidget5.mBaseline.mTarget.mSolverVariable;
                                        n3 = margin - mTop.getMargin();
                                    }
                                }
                                if (solverVariable != null && solverVariable2 != null) {
                                    linearSystem.addGreaterThan(solverVariable, solverVariable2, n3, n2);
                                }
                                constraintWidget6 = mVerticalNextWidget3;
                                if (constraintWidget5.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    final ConstraintAnchor mBottom = constraintWidget5.mBottom;
                                    if (constraintWidget5.mMatchConstraintDefaultHeight == 1) {
                                        linearSystem.addEquality(mBottom.mSolverVariable, mTop.mSolverVariable, Math.max(constraintWidget5.mMatchConstraintMinHeight, constraintWidget5.getHeight()), 3);
                                        constraintWidget6 = mVerticalNextWidget3;
                                    }
                                    else {
                                        linearSystem.addGreaterThan(mTop.mSolverVariable, mTop.mTarget.mSolverVariable, mTop.mMargin, 3);
                                        linearSystem.addLowerThan(mBottom.mSolverVariable, mTop.mSolverVariable, constraintWidget5.mMatchConstraintMinHeight, 3);
                                        constraintWidget6 = mVerticalNextWidget3;
                                    }
                                }
                            }
                            else if (!b && b4 && constraintWidget2 != null) {
                                if (constraintWidget5.mBottom.mTarget == null) {
                                    linearSystem.addEquality(constraintWidget5.mBottom.mSolverVariable, constraintWidget5.getDrawBottom());
                                    constraintWidget6 = mVerticalNextWidget3;
                                }
                                else {
                                    linearSystem.addEquality(constraintWidget5.mBottom.mSolverVariable, constraintWidget3.mBottom.mTarget.mSolverVariable, -constraintWidget5.mBottom.getMargin(), 5);
                                    constraintWidget6 = mVerticalNextWidget3;
                                }
                            }
                            else if (!b && !b4 && constraintWidget2 == null) {
                                if (constraintWidget5.mTop.mTarget == null) {
                                    linearSystem.addEquality(constraintWidget5.mTop.mSolverVariable, constraintWidget5.getDrawY());
                                    constraintWidget6 = mVerticalNextWidget3;
                                }
                                else {
                                    linearSystem.addEquality(constraintWidget5.mTop.mSolverVariable, constraintWidget.mTop.mTarget.mSolverVariable, constraintWidget5.mTop.getMargin(), 5);
                                    constraintWidget6 = mVerticalNextWidget3;
                                }
                            }
                            else {
                                final ConstraintAnchor mTop2 = constraintWidget5.mTop;
                                final ConstraintAnchor mBottom2 = constraintWidget5.mBottom;
                                final int margin2 = mTop2.getMargin();
                                final int margin3 = mBottom2.getMargin();
                                linearSystem.addGreaterThan(mTop2.mSolverVariable, mTop2.mTarget.mSolverVariable, margin2, 1);
                                linearSystem.addLowerThan(mBottom2.mSolverVariable, mBottom2.mTarget.mSolverVariable, -margin3, 1);
                                SolverVariable solverVariable3;
                                if (mTop2.mTarget != null) {
                                    solverVariable3 = mTop2.mTarget.mSolverVariable;
                                }
                                else {
                                    solverVariable3 = null;
                                }
                                if (constraintWidget2 == null) {
                                    if (constraintWidget.mTop.mTarget != null) {
                                        solverVariable3 = constraintWidget.mTop.mTarget.mSolverVariable;
                                    }
                                    else {
                                        solverVariable3 = null;
                                    }
                                }
                                ConstraintWidget mOwner;
                                if ((mOwner = mVerticalNextWidget3) == null) {
                                    if (constraintWidget3.mBottom.mTarget != null) {
                                        mOwner = constraintWidget3.mBottom.mTarget.mOwner;
                                    }
                                    else {
                                        mOwner = null;
                                    }
                                }
                                if ((constraintWidget6 = mOwner) != null) {
                                    SolverVariable solverVariable4 = mOwner.mTop.mSolverVariable;
                                    if (b4) {
                                        if (constraintWidget3.mBottom.mTarget != null) {
                                            solverVariable4 = constraintWidget3.mBottom.mTarget.mSolverVariable;
                                        }
                                        else {
                                            solverVariable4 = null;
                                        }
                                    }
                                    constraintWidget6 = mOwner;
                                    if (solverVariable3 != null) {
                                        constraintWidget6 = mOwner;
                                        if (solverVariable4 != null) {
                                            linearSystem.addCentering(mTop2.mSolverVariable, solverVariable3, margin2, 0.5f, solverVariable4, mBottom2.mSolverVariable, margin3, 4);
                                            constraintWidget6 = mOwner;
                                        }
                                    }
                                }
                            }
                            if (b4) {
                                constraintWidget4 = null;
                            }
                            else {
                                constraintWidget4 = constraintWidget6;
                            }
                            constraintWidget2 = constraintWidget5;
                        }
                        if (b2) {
                            final ConstraintAnchor mTop3 = mVerticalNextWidget.mTop;
                            final ConstraintAnchor mBottom3 = constraintWidget3.mBottom;
                            final int margin4 = mTop3.getMargin();
                            final int margin5 = mBottom3.getMargin();
                            SolverVariable mSolverVariable;
                            if (constraintWidget.mTop.mTarget != null) {
                                mSolverVariable = constraintWidget.mTop.mTarget.mSolverVariable;
                            }
                            else {
                                mSolverVariable = null;
                            }
                            SolverVariable mSolverVariable2;
                            if (constraintWidget3.mBottom.mTarget != null) {
                                mSolverVariable2 = constraintWidget3.mBottom.mTarget.mSolverVariable;
                            }
                            else {
                                mSolverVariable2 = null;
                            }
                            if (mSolverVariable != null && mSolverVariable2 != null) {
                                linearSystem.addLowerThan(mBottom3.mSolverVariable, mSolverVariable2, -margin5, 1);
                                linearSystem.addCentering(mTop3.mSolverVariable, mSolverVariable, margin4, constraintWidget.mVerticalBiasPercent, mSolverVariable2, mBottom3.mSolverVariable, margin5, 4);
                            }
                        }
                    }
                    else {
                        ConstraintWidget constraintWidget7 = null;
                        float n4 = 0.0f;
                        while (mVerticalNextWidget != null) {
                            if (mVerticalNextWidget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                                int margin6 = mVerticalNextWidget.mTop.getMargin();
                                if (constraintWidget7 != null) {
                                    margin6 += constraintWidget7.mBottom.getMargin();
                                }
                                int n5 = 3;
                                if (mVerticalNextWidget.mTop.mTarget.mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n5 = 2;
                                }
                                linearSystem.addGreaterThan(mVerticalNextWidget.mTop.mSolverVariable, mVerticalNextWidget.mTop.mTarget.mSolverVariable, margin6, n5);
                                int margin7;
                                final int n6 = margin7 = mVerticalNextWidget.mBottom.getMargin();
                                if (mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.mTarget != null) {
                                    margin7 = n6;
                                    if (mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == mVerticalNextWidget) {
                                        margin7 = n6 + mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.getMargin();
                                    }
                                }
                                int n7 = 3;
                                if (mVerticalNextWidget.mBottom.mTarget.mOwner.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    n7 = 2;
                                }
                                linearSystem.addLowerThan(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mBottom.mTarget.mSolverVariable, -margin7, n7);
                            }
                            else {
                                n4 += mVerticalNextWidget.mVerticalWeight;
                                int margin8 = 0;
                                if (mVerticalNextWidget.mBottom.mTarget != null) {
                                    margin8 = mVerticalNextWidget.mBottom.getMargin();
                                    if (mVerticalNextWidget != this.mChainEnds[3]) {
                                        margin8 += mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.getMargin();
                                    }
                                }
                                linearSystem.addGreaterThan(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mTop.mSolverVariable, 0, 1);
                                linearSystem.addLowerThan(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mBottom.mTarget.mSolverVariable, -margin8, 1);
                            }
                            constraintWidget7 = mVerticalNextWidget;
                            mVerticalNextWidget = mVerticalNextWidget.mVerticalNextWidget;
                        }
                        if (countMatchConstraintsChainedWidgets == 1) {
                            final ConstraintWidget constraintWidget8 = this.mMatchConstraintsChainedWidgets[0];
                            int margin9;
                            final int n8 = margin9 = constraintWidget8.mTop.getMargin();
                            if (constraintWidget8.mTop.mTarget != null) {
                                margin9 = n8 + constraintWidget8.mTop.mTarget.getMargin();
                            }
                            int margin10;
                            final int n9 = margin10 = constraintWidget8.mBottom.getMargin();
                            if (constraintWidget8.mBottom.mTarget != null) {
                                margin10 = n9 + constraintWidget8.mBottom.mTarget.getMargin();
                            }
                            SolverVariable solverVariable5 = constraintWidget.mBottom.mTarget.mSolverVariable;
                            if (constraintWidget8 == this.mChainEnds[3]) {
                                solverVariable5 = this.mChainEnds[1].mBottom.mTarget.mSolverVariable;
                            }
                            if (constraintWidget8.mMatchConstraintDefaultHeight == 1) {
                                linearSystem.addGreaterThan(constraintWidget.mTop.mSolverVariable, constraintWidget.mTop.mTarget.mSolverVariable, margin9, 1);
                                linearSystem.addLowerThan(constraintWidget.mBottom.mSolverVariable, solverVariable5, -margin10, 1);
                                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, constraintWidget.mTop.mSolverVariable, constraintWidget.getHeight(), 2);
                            }
                            else {
                                linearSystem.addEquality(constraintWidget8.mTop.mSolverVariable, constraintWidget8.mTop.mTarget.mSolverVariable, margin9, 1);
                                linearSystem.addEquality(constraintWidget8.mBottom.mSolverVariable, solverVariable5, -margin10, 1);
                            }
                        }
                        else {
                            for (int j = 0; j < countMatchConstraintsChainedWidgets - 1; ++j) {
                                final ConstraintWidget constraintWidget9 = this.mMatchConstraintsChainedWidgets[j];
                                final ConstraintWidget constraintWidget10 = this.mMatchConstraintsChainedWidgets[j + 1];
                                final SolverVariable mSolverVariable3 = constraintWidget9.mTop.mSolverVariable;
                                final SolverVariable mSolverVariable4 = constraintWidget9.mBottom.mSolverVariable;
                                final SolverVariable mSolverVariable5 = constraintWidget10.mTop.mSolverVariable;
                                SolverVariable solverVariable6 = constraintWidget10.mBottom.mSolverVariable;
                                if (constraintWidget10 == this.mChainEnds[3]) {
                                    solverVariable6 = this.mChainEnds[1].mBottom.mSolverVariable;
                                }
                                int margin11;
                                final int n10 = margin11 = constraintWidget9.mTop.getMargin();
                                if (constraintWidget9.mTop.mTarget != null) {
                                    margin11 = n10;
                                    if (constraintWidget9.mTop.mTarget.mOwner.mBottom.mTarget != null) {
                                        margin11 = n10;
                                        if (constraintWidget9.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == constraintWidget9) {
                                            margin11 = n10 + constraintWidget9.mTop.mTarget.mOwner.mBottom.getMargin();
                                        }
                                    }
                                }
                                linearSystem.addGreaterThan(mSolverVariable3, constraintWidget9.mTop.mTarget.mSolverVariable, margin11, 2);
                                int margin12;
                                final int n11 = margin12 = constraintWidget9.mBottom.getMargin();
                                if (constraintWidget9.mBottom.mTarget != null) {
                                    margin12 = n11;
                                    if (constraintWidget9.mVerticalNextWidget != null) {
                                        int margin13;
                                        if (constraintWidget9.mVerticalNextWidget.mTop.mTarget != null) {
                                            margin13 = constraintWidget9.mVerticalNextWidget.mTop.getMargin();
                                        }
                                        else {
                                            margin13 = 0;
                                        }
                                        margin12 = n11 + margin13;
                                    }
                                }
                                linearSystem.addLowerThan(mSolverVariable4, constraintWidget9.mBottom.mTarget.mSolverVariable, -margin12, 2);
                                if (j + 1 == countMatchConstraintsChainedWidgets - 1) {
                                    int margin14;
                                    final int n12 = margin14 = constraintWidget10.mTop.getMargin();
                                    if (constraintWidget10.mTop.mTarget != null) {
                                        margin14 = n12;
                                        if (constraintWidget10.mTop.mTarget.mOwner.mBottom.mTarget != null) {
                                            margin14 = n12;
                                            if (constraintWidget10.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == constraintWidget10) {
                                                margin14 = n12 + constraintWidget10.mTop.mTarget.mOwner.mBottom.getMargin();
                                            }
                                        }
                                    }
                                    linearSystem.addGreaterThan(mSolverVariable5, constraintWidget10.mTop.mTarget.mSolverVariable, margin14, 2);
                                    ConstraintAnchor constraintAnchor = constraintWidget10.mBottom;
                                    if (constraintWidget10 == this.mChainEnds[3]) {
                                        constraintAnchor = this.mChainEnds[1].mBottom;
                                    }
                                    int margin15;
                                    final int n13 = margin15 = constraintAnchor.getMargin();
                                    if (constraintAnchor.mTarget != null) {
                                        margin15 = n13;
                                        if (constraintAnchor.mTarget.mOwner.mTop.mTarget != null) {
                                            margin15 = n13;
                                            if (constraintAnchor.mTarget.mOwner.mTop.mTarget.mOwner == constraintWidget10) {
                                                margin15 = n13 + constraintAnchor.mTarget.mOwner.mTop.getMargin();
                                            }
                                        }
                                    }
                                    linearSystem.addLowerThan(solverVariable6, constraintAnchor.mTarget.mSolverVariable, -margin15, 2);
                                }
                                if (constraintWidget.mMatchConstraintMaxHeight > 0) {
                                    linearSystem.addLowerThan(mSolverVariable4, mSolverVariable3, constraintWidget.mMatchConstraintMaxHeight, 2);
                                }
                                final ArrayRow row = linearSystem.createRow();
                                row.createRowEqualDimension(constraintWidget9.mVerticalWeight, n4, constraintWidget10.mVerticalWeight, mSolverVariable3, constraintWidget9.mTop.getMargin(), mSolverVariable4, constraintWidget9.mBottom.getMargin(), mSolverVariable5, constraintWidget10.mTop.getMargin(), solverVariable6, constraintWidget10.mBottom.getMargin());
                                linearSystem.addConstraint(row);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private int countMatchConstraintsChainedWidgets(final LinearSystem linearSystem, final ConstraintWidget[] array, final ConstraintWidget constraintWidget, int n, final boolean[] array2) {
        final int n2 = 0;
        final int n3 = 0;
        array2[0] = true;
        array2[1] = false;
        array[2] = (array[0] = null);
        array[3] = (array[1] = null);
        if (n == 0) {
            final boolean b = true;
            ConstraintWidget mOwner = null;
            boolean b2 = b;
            if (constraintWidget.mLeft.mTarget != null) {
                b2 = b;
                if (constraintWidget.mLeft.mTarget.mOwner != this) {
                    b2 = false;
                }
            }
            constraintWidget.mHorizontalNextWidget = null;
            ConstraintWidget constraintWidget2 = null;
            if (constraintWidget.getVisibility() != 8) {
                constraintWidget2 = constraintWidget;
            }
            ConstraintWidget constraintWidget3 = constraintWidget2;
            ConstraintWidget mHorizontalNextWidget = constraintWidget;
            n = n3;
            int n4;
            ConstraintWidget constraintWidget4;
            ConstraintWidget constraintWidget5;
            while (true) {
                n4 = n;
                constraintWidget4 = constraintWidget2;
                constraintWidget5 = constraintWidget3;
                if (mHorizontalNextWidget.mRight.mTarget == null) {
                    break;
                }
                mHorizontalNextWidget.mHorizontalNextWidget = null;
                if (mHorizontalNextWidget.getVisibility() != 8) {
                    ConstraintWidget constraintWidget6;
                    if ((constraintWidget6 = constraintWidget2) == null) {
                        constraintWidget6 = mHorizontalNextWidget;
                    }
                    if (constraintWidget3 != null && constraintWidget3 != mHorizontalNextWidget) {
                        constraintWidget3.mHorizontalNextWidget = mHorizontalNextWidget;
                    }
                    constraintWidget3 = mHorizontalNextWidget;
                    constraintWidget2 = constraintWidget6;
                }
                else {
                    linearSystem.addEquality(mHorizontalNextWidget.mLeft.mSolverVariable, mHorizontalNextWidget.mLeft.mTarget.mSolverVariable, 0, 5);
                    linearSystem.addEquality(mHorizontalNextWidget.mRight.mSolverVariable, mHorizontalNextWidget.mLeft.mSolverVariable, 0, 5);
                }
                int n5 = n;
                if (mHorizontalNextWidget.getVisibility() != 8) {
                    n5 = n;
                    if (mHorizontalNextWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        if (mHorizontalNextWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                            array2[0] = false;
                        }
                        n5 = n;
                        if (mHorizontalNextWidget.mDimensionRatio <= 0.0f) {
                            array2[0] = false;
                            if (n + 1 >= this.mMatchConstraintsChainedWidgets.length) {
                                this.mMatchConstraintsChainedWidgets = Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                            }
                            this.mMatchConstraintsChainedWidgets[n] = mHorizontalNextWidget;
                            n5 = n + 1;
                        }
                    }
                }
                if (mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.mTarget == null) {
                    constraintWidget5 = constraintWidget3;
                    constraintWidget4 = constraintWidget2;
                    n4 = n5;
                    break;
                }
                n4 = n5;
                constraintWidget4 = constraintWidget2;
                constraintWidget5 = constraintWidget3;
                if (mHorizontalNextWidget.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != mHorizontalNextWidget) {
                    break;
                }
                n4 = n5;
                constraintWidget4 = constraintWidget2;
                constraintWidget5 = constraintWidget3;
                if (mHorizontalNextWidget.mRight.mTarget.mOwner == mHorizontalNextWidget) {
                    break;
                }
                mHorizontalNextWidget = (mOwner = mHorizontalNextWidget.mRight.mTarget.mOwner);
                n = n5;
            }
            boolean mHorizontalChainFixedPosition = b2;
            if (mHorizontalNextWidget.mRight.mTarget != null) {
                mHorizontalChainFixedPosition = b2;
                if (mHorizontalNextWidget.mRight.mTarget.mOwner != this) {
                    mHorizontalChainFixedPosition = false;
                }
            }
            if (constraintWidget.mLeft.mTarget == null || mOwner.mRight.mTarget == null) {
                array2[1] = true;
            }
            constraintWidget.mHorizontalChainFixedPosition = mHorizontalChainFixedPosition;
            mOwner.mHorizontalNextWidget = null;
            array[0] = constraintWidget;
            array[2] = constraintWidget4;
            array[1] = mOwner;
            array[3] = constraintWidget5;
            return n4;
        }
        final boolean b3 = true;
        ConstraintWidget mOwner2 = null;
        boolean b4 = b3;
        if (constraintWidget.mTop.mTarget != null) {
            b4 = b3;
            if (constraintWidget.mTop.mTarget.mOwner != this) {
                b4 = false;
            }
        }
        constraintWidget.mVerticalNextWidget = null;
        ConstraintWidget constraintWidget7 = null;
        if (constraintWidget.getVisibility() != 8) {
            constraintWidget7 = constraintWidget;
        }
        ConstraintWidget constraintWidget8 = constraintWidget7;
        ConstraintWidget mVerticalNextWidget = constraintWidget;
        n = n2;
        int n6;
        ConstraintWidget constraintWidget9;
        ConstraintWidget constraintWidget10;
        while (true) {
            n6 = n;
            constraintWidget9 = constraintWidget7;
            constraintWidget10 = constraintWidget8;
            if (mVerticalNextWidget.mBottom.mTarget == null) {
                break;
            }
            mVerticalNextWidget.mVerticalNextWidget = null;
            if (mVerticalNextWidget.getVisibility() != 8) {
                ConstraintWidget constraintWidget11;
                if ((constraintWidget11 = constraintWidget7) == null) {
                    constraintWidget11 = mVerticalNextWidget;
                }
                if (constraintWidget8 != null && constraintWidget8 != mVerticalNextWidget) {
                    constraintWidget8.mVerticalNextWidget = mVerticalNextWidget;
                }
                constraintWidget8 = mVerticalNextWidget;
                constraintWidget7 = constraintWidget11;
            }
            else {
                linearSystem.addEquality(mVerticalNextWidget.mTop.mSolverVariable, mVerticalNextWidget.mTop.mTarget.mSolverVariable, 0, 5);
                linearSystem.addEquality(mVerticalNextWidget.mBottom.mSolverVariable, mVerticalNextWidget.mTop.mSolverVariable, 0, 5);
            }
            int n7 = n;
            if (mVerticalNextWidget.getVisibility() != 8) {
                n7 = n;
                if (mVerticalNextWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (mVerticalNextWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                        array2[0] = false;
                    }
                    n7 = n;
                    if (mVerticalNextWidget.mDimensionRatio <= 0.0f) {
                        array2[0] = false;
                        if (n + 1 >= this.mMatchConstraintsChainedWidgets.length) {
                            this.mMatchConstraintsChainedWidgets = Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                        }
                        this.mMatchConstraintsChainedWidgets[n] = mVerticalNextWidget;
                        n7 = n + 1;
                    }
                }
            }
            if (mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.mTarget == null) {
                constraintWidget10 = constraintWidget8;
                constraintWidget9 = constraintWidget7;
                n6 = n7;
                break;
            }
            n6 = n7;
            constraintWidget9 = constraintWidget7;
            constraintWidget10 = constraintWidget8;
            if (mVerticalNextWidget.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != mVerticalNextWidget) {
                break;
            }
            n6 = n7;
            constraintWidget9 = constraintWidget7;
            constraintWidget10 = constraintWidget8;
            if (mVerticalNextWidget.mBottom.mTarget.mOwner == mVerticalNextWidget) {
                break;
            }
            mVerticalNextWidget = (mOwner2 = mVerticalNextWidget.mBottom.mTarget.mOwner);
            n = n7;
        }
        boolean mVerticalChainFixedPosition = b4;
        if (mVerticalNextWidget.mBottom.mTarget != null) {
            mVerticalChainFixedPosition = b4;
            if (mVerticalNextWidget.mBottom.mTarget.mOwner != this) {
                mVerticalChainFixedPosition = false;
            }
        }
        if (constraintWidget.mTop.mTarget == null || mOwner2.mBottom.mTarget == null) {
            array2[1] = true;
        }
        constraintWidget.mVerticalChainFixedPosition = mVerticalChainFixedPosition;
        mOwner2.mVerticalNextWidget = null;
        array[0] = constraintWidget;
        array[2] = constraintWidget9;
        array[1] = mOwner2;
        array[3] = constraintWidget10;
        return n6;
    }
    
    public static ConstraintWidgetContainer createContainer(final ConstraintWidgetContainer constraintWidgetContainer, final String debugName, final ArrayList<ConstraintWidget> list, int n) {
        final Rectangle bounds = WidgetContainer.getBounds(list);
        ConstraintWidgetContainer constraintWidgetContainer2;
        if (bounds.width == 0 || bounds.height == 0) {
            constraintWidgetContainer2 = null;
        }
        else {
            if (n > 0) {
                final int min = Math.min(bounds.x, bounds.y);
                int n2;
                if ((n2 = n) > min) {
                    n2 = min;
                }
                bounds.grow(n2, n2);
            }
            constraintWidgetContainer.setOrigin(bounds.x, bounds.y);
            constraintWidgetContainer.setDimension(bounds.width, bounds.height);
            constraintWidgetContainer.setDebugName(debugName);
            final ConstraintWidget parent = list.get(0).getParent();
            n = 0;
            final int size = list.size();
            while (true) {
                constraintWidgetContainer2 = constraintWidgetContainer;
                if (n >= size) {
                    break;
                }
                final ConstraintWidget constraintWidget = list.get(n);
                if (constraintWidget.getParent() == parent) {
                    constraintWidgetContainer.add(constraintWidget);
                    constraintWidget.setX(constraintWidget.getX() - bounds.x);
                    constraintWidget.setY(constraintWidget.getY() - bounds.y);
                }
                ++n;
            }
        }
        return constraintWidgetContainer2;
    }
    
    private boolean optimize(final LinearSystem linearSystem) {
        final int size = this.mChildren.size();
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        final int n = 0;
        int n2 = 0;
        int n3;
        int n4;
        int n5;
        int n6;
        while (true) {
            n3 = (b3 ? 1 : 0);
            n4 = (b ? 1 : 0);
            n5 = (b2 ? 1 : 0);
            n6 = n;
            if (n2 >= size) {
                break;
            }
            final ConstraintWidget constraintWidget = this.mChildren.get(n2);
            constraintWidget.mHorizontalResolution = -1;
            constraintWidget.mVerticalResolution = -1;
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                constraintWidget.mHorizontalResolution = 1;
                constraintWidget.mVerticalResolution = 1;
            }
            ++n2;
        }
        while (true) {
            final int n7 = n5;
            final int n8 = n3;
            if (n4 != 0) {
                break;
            }
            int n9 = 0;
            int n10 = 0;
            final int n11 = n6 + 1;
            int n12;
            int n13;
            for (int i = 0; i < size; ++i, n10 = n13, n9 = n12) {
                final ConstraintWidget constraintWidget2 = this.mChildren.get(i);
                if (constraintWidget2.mHorizontalResolution == -1) {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mHorizontalResolution = 1;
                    }
                    else {
                        Optimizer.checkHorizontalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                if (constraintWidget2.mVerticalResolution == -1) {
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        constraintWidget2.mVerticalResolution = 1;
                    }
                    else {
                        Optimizer.checkVerticalSimpleDependency(this, linearSystem, constraintWidget2);
                    }
                }
                n12 = n9;
                if (constraintWidget2.mVerticalResolution == -1) {
                    n12 = n9 + 1;
                }
                n13 = n10;
                if (constraintWidget2.mHorizontalResolution == -1) {
                    n13 = n10 + 1;
                }
            }
            if (n9 == 0 && n10 == 0) {
                n4 = 1;
                n6 = n11;
                n5 = n9;
                n3 = n10;
            }
            else {
                n3 = n10;
                n5 = n9;
                n6 = n11;
                if (n7 != n9) {
                    continue;
                }
                n3 = n10;
                n5 = n9;
                n6 = n11;
                if (n8 != n10) {
                    continue;
                }
                n4 = 1;
                n3 = n10;
                n5 = n9;
                n6 = n11;
            }
        }
        int n14 = 0;
        int n15 = 0;
        int n16 = 0;
        int n17;
        for (int j = 0; j < size; ++j, n14 = n16, n15 = n17) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(j);
            Label_0403: {
                if (constraintWidget3.mHorizontalResolution != 1) {
                    n16 = n14;
                    if (constraintWidget3.mHorizontalResolution != -1) {
                        break Label_0403;
                    }
                }
                n16 = n14 + 1;
            }
            if (constraintWidget3.mVerticalResolution != 1) {
                n17 = n15;
                if (constraintWidget3.mVerticalResolution != -1) {
                    continue;
                }
            }
            n17 = n15 + 1;
        }
        return n14 == 0 && n15 == 0;
    }
    
    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }
    
    static int setGroup(final ConstraintAnchor constraintAnchor, int setGroup) {
        final int mGroup = constraintAnchor.mGroup;
        int n;
        if (constraintAnchor.mOwner.getParent() == null) {
            n = setGroup;
        }
        else if ((n = mGroup) > setGroup) {
            constraintAnchor.mGroup = setGroup;
            final ConstraintAnchor opposite = constraintAnchor.getOpposite();
            final ConstraintAnchor mTarget = constraintAnchor.mTarget;
            int setGroup2 = setGroup;
            if (opposite != null) {
                setGroup2 = setGroup(opposite, setGroup);
            }
            setGroup = setGroup2;
            if (mTarget != null) {
                setGroup = setGroup(mTarget, setGroup2);
            }
            int setGroup3 = setGroup;
            if (opposite != null) {
                setGroup3 = setGroup(opposite, setGroup);
            }
            return constraintAnchor.mGroup = setGroup3;
        }
        return n;
    }
    
    void addChain(ConstraintWidget constraintWidget, final int n) {
        if (n == 0) {
            while (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mRight.mTarget == constraintWidget.mLeft && constraintWidget.mLeft.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mLeft.mTarget.mOwner;
            }
            this.addHorizontalChain(constraintWidget);
        }
        else if (n == 1) {
            while (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mBottom.mTarget == constraintWidget.mTop && constraintWidget.mTop.mTarget.mOwner != constraintWidget) {
                constraintWidget = constraintWidget.mTop.mTarget.mOwner;
            }
            this.addVerticalChain(constraintWidget);
        }
    }
    
    public boolean addChildrenToSolver(final LinearSystem linearSystem, final int n) {
        this.addToSolver(linearSystem, n);
        final int size = this.mChildren.size();
        boolean b = false;
        if (this.mOptimizationLevel == 2 || this.mOptimizationLevel == 4) {
            if (this.optimize(linearSystem)) {
                return false;
            }
        }
        else {
            b = true;
        }
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                final DimensionBehaviour mHorizontalDimensionBehaviour = constraintWidget.mHorizontalDimensionBehaviour;
                final DimensionBehaviour mVerticalDimensionBehaviour = constraintWidget.mVerticalDimensionBehaviour;
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem, n);
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(mHorizontalDimensionBehaviour);
                }
                if (mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(mVerticalDimensionBehaviour);
                }
            }
            else {
                if (b) {
                    Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                }
                constraintWidget.addToSolver(linearSystem, n);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            this.applyHorizontalChain(linearSystem);
        }
        if (this.mVerticalChainsSize > 0) {
            this.applyVerticalChain(linearSystem);
        }
        return true;
    }
    
    public void findHorizontalWrapRecursive(final ConstraintWidget constraintWidget, final boolean[] array) {
        final boolean b = false;
        if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        final int optimizerWrapWidth = constraintWidget.getOptimizerWrapWidth();
        if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        int relativeEnd = optimizerWrapWidth;
        ConstraintWidget constraintWidget2 = null;
        ConstraintWidget constraintWidget3 = null;
        constraintWidget.mHorizontalWrapVisited = true;
        int relativeBegin = 0;
        Label_0134: {
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                relativeBegin = optimizerWrapWidth;
                if (guideline.getOrientation() == 1) {
                    final int n = 0;
                    relativeEnd = 0;
                    if (guideline.getRelativeBegin() != -1) {
                        relativeBegin = guideline.getRelativeBegin();
                    }
                    else {
                        relativeBegin = n;
                        if (guideline.getRelativeEnd() != -1) {
                            relativeEnd = guideline.getRelativeEnd();
                            relativeBegin = n;
                        }
                    }
                }
            }
            else if (!constraintWidget.mRight.isConnected() && !constraintWidget.mLeft.isConnected()) {
                relativeBegin = optimizerWrapWidth + constraintWidget.getX();
            }
            else {
                if (constraintWidget.mRight.mTarget != null && constraintWidget.mLeft.mTarget != null && (constraintWidget.mRight.mTarget == constraintWidget.mLeft.mTarget || (constraintWidget.mRight.mTarget.mOwner == constraintWidget.mLeft.mTarget.mOwner && constraintWidget.mRight.mTarget.mOwner != constraintWidget.mParent))) {
                    array[0] = false;
                    return;
                }
                int n2 = relativeEnd;
                if (constraintWidget.mRight.mTarget != null) {
                    final ConstraintWidget mOwner = constraintWidget.mRight.mTarget.mOwner;
                    final int n3 = n2 = relativeEnd + constraintWidget.mRight.getMargin();
                    constraintWidget3 = mOwner;
                    if (!mOwner.isRoot()) {
                        n2 = n3;
                        constraintWidget3 = mOwner;
                        if (!mOwner.mHorizontalWrapVisited) {
                            this.findHorizontalWrapRecursive(mOwner, array);
                            constraintWidget3 = mOwner;
                            n2 = n3;
                        }
                    }
                }
                int n4 = optimizerWrapWidth;
                if (constraintWidget.mLeft.mTarget != null) {
                    final ConstraintWidget mOwner2 = constraintWidget.mLeft.mTarget.mOwner;
                    final int n5 = n4 = optimizerWrapWidth + constraintWidget.mLeft.getMargin();
                    constraintWidget2 = mOwner2;
                    if (!mOwner2.isRoot()) {
                        n4 = n5;
                        constraintWidget2 = mOwner2;
                        if (!mOwner2.mHorizontalWrapVisited) {
                            this.findHorizontalWrapRecursive(mOwner2, array);
                            constraintWidget2 = mOwner2;
                            n4 = n5;
                        }
                    }
                }
                int n6 = n2;
                Label_0622: {
                    if (constraintWidget.mRight.mTarget != null) {
                        n6 = n2;
                        if (!constraintWidget3.isRoot()) {
                            int n7;
                            if (constraintWidget.mRight.mTarget.mType == ConstraintAnchor.Type.RIGHT) {
                                n7 = n2 + (constraintWidget3.mDistToRight - constraintWidget3.getOptimizerWrapWidth());
                            }
                            else {
                                n7 = n2;
                                if (constraintWidget.mRight.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                                    n7 = n2 + constraintWidget3.mDistToRight;
                                }
                            }
                            constraintWidget.mRightHasCentered = (constraintWidget3.mRightHasCentered || (constraintWidget3.mLeft.mTarget != null && constraintWidget3.mRight.mTarget != null && constraintWidget3.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT));
                            n6 = n7;
                            if (constraintWidget.mRightHasCentered) {
                                if (constraintWidget3.mLeft.mTarget != null) {
                                    n6 = n7;
                                    if (constraintWidget3.mLeft.mTarget.mOwner == constraintWidget) {
                                        break Label_0622;
                                    }
                                }
                                n6 = n7 + (n7 - constraintWidget3.mDistToRight);
                            }
                        }
                    }
                }
                relativeBegin = n4;
                relativeEnd = n6;
                if (constraintWidget.mLeft.mTarget != null) {
                    relativeBegin = n4;
                    relativeEnd = n6;
                    if (!constraintWidget2.isRoot()) {
                        int n8;
                        if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.LEFT) {
                            n8 = n4 + (constraintWidget2.mDistToLeft - constraintWidget2.getOptimizerWrapWidth());
                        }
                        else {
                            n8 = n4;
                            if (constraintWidget.mLeft.mTarget.getType() == ConstraintAnchor.Type.RIGHT) {
                                n8 = n4 + constraintWidget2.mDistToLeft;
                            }
                        }
                        boolean mLeftHasCentered = false;
                        Label_0742: {
                            if (!constraintWidget2.mLeftHasCentered) {
                                mLeftHasCentered = b;
                                if (constraintWidget2.mLeft.mTarget == null) {
                                    break Label_0742;
                                }
                                mLeftHasCentered = b;
                                if (constraintWidget2.mRight.mTarget == null) {
                                    break Label_0742;
                                }
                                mLeftHasCentered = b;
                                if (constraintWidget2.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    break Label_0742;
                                }
                            }
                            mLeftHasCentered = true;
                        }
                        constraintWidget.mLeftHasCentered = mLeftHasCentered;
                        relativeBegin = n8;
                        relativeEnd = n6;
                        if (constraintWidget.mLeftHasCentered) {
                            if (constraintWidget2.mRight.mTarget != null) {
                                relativeBegin = n8;
                                relativeEnd = n6;
                                if (constraintWidget2.mRight.mTarget.mOwner == constraintWidget) {
                                    break Label_0134;
                                }
                            }
                            relativeBegin = n8 + (n8 - constraintWidget2.mDistToLeft);
                            relativeEnd = n6;
                        }
                    }
                }
            }
        }
        int mDistToLeft = relativeBegin;
        int mDistToRight = relativeEnd;
        if (constraintWidget.getVisibility() == 8) {
            mDistToLeft = relativeBegin - constraintWidget.mWidth;
            mDistToRight = relativeEnd - constraintWidget.mWidth;
        }
        constraintWidget.mDistToLeft = mDistToLeft;
        constraintWidget.mDistToRight = mDistToRight;
    }
    
    public void findVerticalWrapRecursive(final ConstraintWidget constraintWidget, final boolean[] array) {
        final boolean b = false;
        if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.mDimensionRatio > 0.0f) {
            array[0] = false;
            return;
        }
        final int optimizerWrapHeight;
        final int n2;
        final int n = n2 = (optimizerWrapHeight = constraintWidget.getOptimizerWrapHeight());
        ConstraintWidget constraintWidget2 = null;
        ConstraintWidget constraintWidget3 = null;
        constraintWidget.mVerticalWrapVisited = true;
        int relativeEnd = 0;
        int relativeBegin = 0;
        Label_0104: {
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                relativeEnd = n2;
                relativeBegin = optimizerWrapHeight;
                if (guideline.getOrientation() == 0) {
                    final int n3 = 0;
                    relativeEnd = 0;
                    if (guideline.getRelativeBegin() != -1) {
                        relativeBegin = guideline.getRelativeBegin();
                    }
                    else {
                        relativeBegin = n3;
                        if (guideline.getRelativeEnd() != -1) {
                            relativeEnd = guideline.getRelativeEnd();
                            relativeBegin = n3;
                        }
                    }
                }
            }
            else if (constraintWidget.mBaseline.mTarget == null && constraintWidget.mTop.mTarget == null && constraintWidget.mBottom.mTarget == null) {
                final int n4 = optimizerWrapHeight + constraintWidget.getY();
                relativeEnd = n2;
                relativeBegin = n4;
            }
            else {
                if (constraintWidget.mBottom.mTarget != null && constraintWidget.mTop.mTarget != null && (constraintWidget.mBottom.mTarget == constraintWidget.mTop.mTarget || (constraintWidget.mBottom.mTarget.mOwner == constraintWidget.mTop.mTarget.mOwner && constraintWidget.mBottom.mTarget.mOwner != constraintWidget.mParent))) {
                    array[0] = false;
                    return;
                }
                if (constraintWidget.mBaseline.isConnected()) {
                    final ConstraintWidget owner = constraintWidget.mBaseline.mTarget.getOwner();
                    if (!owner.mVerticalWrapVisited) {
                        this.findVerticalWrapRecursive(owner, array);
                    }
                    final int max = Math.max(owner.mDistToTop - owner.mHeight + n, n);
                    int max2 = Math.max(owner.mDistToBottom - owner.mHeight + n, n);
                    int mDistToTop = max;
                    if (constraintWidget.getVisibility() == 8) {
                        mDistToTop = max - constraintWidget.mHeight;
                        max2 -= constraintWidget.mHeight;
                    }
                    constraintWidget.mDistToTop = mDistToTop;
                    constraintWidget.mDistToBottom = max2;
                    return;
                }
                int n5 = optimizerWrapHeight;
                if (constraintWidget.mTop.isConnected()) {
                    final ConstraintWidget owner2 = constraintWidget.mTop.mTarget.getOwner();
                    final int n6 = n5 = optimizerWrapHeight + constraintWidget.mTop.getMargin();
                    constraintWidget2 = owner2;
                    if (!owner2.isRoot()) {
                        n5 = n6;
                        constraintWidget2 = owner2;
                        if (!owner2.mVerticalWrapVisited) {
                            this.findVerticalWrapRecursive(owner2, array);
                            constraintWidget2 = owner2;
                            n5 = n6;
                        }
                    }
                }
                int n7 = n2;
                if (constraintWidget.mBottom.isConnected()) {
                    final ConstraintWidget owner3 = constraintWidget.mBottom.mTarget.getOwner();
                    final int n8 = n2 + constraintWidget.mBottom.getMargin();
                    constraintWidget3 = owner3;
                    n7 = n8;
                    if (!owner3.isRoot()) {
                        constraintWidget3 = owner3;
                        n7 = n8;
                        if (!owner3.mVerticalWrapVisited) {
                            this.findVerticalWrapRecursive(owner3, array);
                            n7 = n8;
                            constraintWidget3 = owner3;
                        }
                    }
                }
                int n9 = n5;
                Label_0761: {
                    if (constraintWidget.mTop.mTarget != null) {
                        n9 = n5;
                        if (!constraintWidget2.isRoot()) {
                            int n10;
                            if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                                n10 = n5 + (constraintWidget2.mDistToTop - constraintWidget2.getOptimizerWrapHeight());
                            }
                            else {
                                n10 = n5;
                                if (constraintWidget.mTop.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                                    n10 = n5 + constraintWidget2.mDistToTop;
                                }
                            }
                            constraintWidget.mTopHasCentered = (constraintWidget2.mTopHasCentered || (constraintWidget2.mTop.mTarget != null && constraintWidget2.mTop.mTarget.mOwner != constraintWidget && constraintWidget2.mBottom.mTarget != null && constraintWidget2.mBottom.mTarget.mOwner != constraintWidget && constraintWidget2.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT));
                            n9 = n10;
                            if (constraintWidget.mTopHasCentered) {
                                if (constraintWidget2.mBottom.mTarget != null) {
                                    n9 = n10;
                                    if (constraintWidget2.mBottom.mTarget.mOwner == constraintWidget) {
                                        break Label_0761;
                                    }
                                }
                                n9 = n10 + (n10 - constraintWidget2.mDistToTop);
                            }
                        }
                    }
                }
                relativeEnd = n7;
                relativeBegin = n9;
                if (constraintWidget.mBottom.mTarget != null) {
                    relativeEnd = n7;
                    relativeBegin = n9;
                    if (!constraintWidget3.isRoot()) {
                        int n11;
                        if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.BOTTOM) {
                            n11 = n7 + (constraintWidget3.mDistToBottom - constraintWidget3.getOptimizerWrapHeight());
                        }
                        else {
                            n11 = n7;
                            if (constraintWidget.mBottom.mTarget.getType() == ConstraintAnchor.Type.TOP) {
                                n11 = n7 + constraintWidget3.mDistToBottom;
                            }
                        }
                        boolean mBottomHasCentered = false;
                        Label_0919: {
                            if (!constraintWidget3.mBottomHasCentered) {
                                mBottomHasCentered = b;
                                if (constraintWidget3.mTop.mTarget == null) {
                                    break Label_0919;
                                }
                                mBottomHasCentered = b;
                                if (constraintWidget3.mTop.mTarget.mOwner == constraintWidget) {
                                    break Label_0919;
                                }
                                mBottomHasCentered = b;
                                if (constraintWidget3.mBottom.mTarget == null) {
                                    break Label_0919;
                                }
                                mBottomHasCentered = b;
                                if (constraintWidget3.mBottom.mTarget.mOwner == constraintWidget) {
                                    break Label_0919;
                                }
                                mBottomHasCentered = b;
                                if (constraintWidget3.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                                    break Label_0919;
                                }
                            }
                            mBottomHasCentered = true;
                        }
                        constraintWidget.mBottomHasCentered = mBottomHasCentered;
                        relativeEnd = n11;
                        relativeBegin = n9;
                        if (constraintWidget.mBottomHasCentered) {
                            if (constraintWidget3.mTop.mTarget != null) {
                                relativeEnd = n11;
                                relativeBegin = n9;
                                if (constraintWidget3.mTop.mTarget.mOwner == constraintWidget) {
                                    break Label_0104;
                                }
                            }
                            relativeEnd = n11 + (n11 - constraintWidget3.mDistToBottom);
                            relativeBegin = n9;
                        }
                    }
                }
            }
        }
        int mDistToBottom = relativeEnd;
        int mDistToTop2 = relativeBegin;
        if (constraintWidget.getVisibility() == 8) {
            mDistToTop2 = relativeBegin - constraintWidget.mHeight;
            mDistToBottom = relativeEnd - constraintWidget.mHeight;
        }
        constraintWidget.mDistToTop = mDistToTop2;
        constraintWidget.mDistToBottom = mDistToBottom;
    }
    
    public void findWrapSize(final ArrayList<ConstraintWidget> list, final boolean[] array) {
        int max = 0;
        int max2 = 0;
        int max3 = 0;
        int max4 = 0;
        int max5 = 0;
        int max6 = 0;
        final int size = list.size();
        array[0] = true;
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = list.get(i);
            if (!constraintWidget.isRoot()) {
                if (!constraintWidget.mHorizontalWrapVisited) {
                    this.findHorizontalWrapRecursive(constraintWidget, array);
                }
                if (!constraintWidget.mVerticalWrapVisited) {
                    this.findVerticalWrapRecursive(constraintWidget, array);
                }
                if (!array[0]) {
                    return;
                }
                int n = constraintWidget.mDistToLeft + constraintWidget.mDistToRight - constraintWidget.getWidth();
                int n2 = constraintWidget.mDistToTop + constraintWidget.mDistToBottom - constraintWidget.getHeight();
                if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
                    n = constraintWidget.getWidth() + constraintWidget.mLeft.mMargin + constraintWidget.mRight.mMargin;
                }
                if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_PARENT) {
                    n2 = constraintWidget.getHeight() + constraintWidget.mTop.mMargin + constraintWidget.mBottom.mMargin;
                }
                if (constraintWidget.getVisibility() == 8) {
                    n = 0;
                    n2 = 0;
                }
                max2 = Math.max(max2, constraintWidget.mDistToLeft);
                max3 = Math.max(max3, constraintWidget.mDistToRight);
                max4 = Math.max(max4, constraintWidget.mDistToBottom);
                max = Math.max(max, constraintWidget.mDistToTop);
                max5 = Math.max(max5, n);
                max6 = Math.max(max6, n2);
            }
        }
        this.mWrapWidth = Math.max(this.mMinWidth, Math.max(Math.max(max2, max3), max5));
        this.mWrapHeight = Math.max(this.mMinHeight, Math.max(Math.max(max, max4), max6));
        for (int j = 0; j < size; ++j) {
            final ConstraintWidget constraintWidget2 = list.get(j);
            constraintWidget2.mHorizontalWrapVisited = false;
            constraintWidget2.mVerticalWrapVisited = false;
            constraintWidget2.mLeftHasCentered = false;
            constraintWidget2.mRightHasCentered = false;
            constraintWidget2.mTopHasCentered = false;
            constraintWidget2.mBottomHasCentered = false;
        }
    }
    
    public ArrayList<Guideline> getHorizontalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 0) {
                    list.add(guideline);
                }
            }
        }
        return list;
    }
    
    public LinearSystem getSystem() {
        return this.mSystem;
    }
    
    @Override
    public String getType() {
        return "ConstraintLayout";
    }
    
    public ArrayList<Guideline> getVerticalGuidelines() {
        final ArrayList<Guideline> list = new ArrayList<Guideline>();
        for (int i = 0; i < this.mChildren.size(); ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                final Guideline guideline = (Guideline)constraintWidget;
                if (guideline.getOrientation() == 1) {
                    list.add(guideline);
                }
            }
        }
        return list;
    }
    
    public boolean handlesInternalConstraints() {
        return false;
    }
    
    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }
    
    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }
    
    @Override
    public void layout() {
        final int mx = this.mX;
        final int my = this.mY;
        final int max = Math.max(0, this.getWidth());
        final int max2 = Math.max(0, this.getHeight());
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.setX(this.mPaddingLeft);
            this.setY(this.mPaddingTop);
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        final boolean b = false;
        final DimensionBehaviour mVerticalDimensionBehaviour = this.mVerticalDimensionBehaviour;
        final DimensionBehaviour mHorizontalDimensionBehaviour = this.mHorizontalDimensionBehaviour;
        int n = b ? 1 : 0;
        Label_0322: {
            if (this.mOptimizationLevel == 2) {
                if (this.mVerticalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT) {
                    n = (b ? 1 : 0);
                    if (this.mHorizontalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT) {
                        break Label_0322;
                    }
                }
                this.findWrapSize(this.mChildren, this.flags);
                int n2;
                final boolean b2 = (n2 = (this.flags[0] ? 1 : 0)) != 0;
                Label_0221: {
                    if (max > 0) {
                        n2 = (b2 ? 1 : 0);
                        if (max2 > 0) {
                            if (this.mWrapWidth <= max) {
                                n2 = (b2 ? 1 : 0);
                                if (this.mWrapHeight <= max2) {
                                    break Label_0221;
                                }
                            }
                            n2 = 0;
                        }
                    }
                }
                if ((n = n2) != 0) {
                    if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                        if (max > 0 && max < this.mWrapWidth) {
                            this.mWidthMeasuredTooSmall = true;
                            this.setWidth(max);
                        }
                        else {
                            this.setWidth(Math.max(this.mMinWidth, this.mWrapWidth));
                        }
                    }
                    n = n2;
                    if (this.mVerticalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                        this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                        if (max2 > 0 && max2 < this.mWrapHeight) {
                            this.mHeightMeasuredTooSmall = true;
                            this.setHeight(max2);
                            n = n2;
                        }
                        else {
                            this.setHeight(Math.max(this.mMinHeight, this.mWrapHeight));
                            n = n2;
                        }
                    }
                }
            }
        }
        this.resetChains();
        final int size = this.mChildren.size();
        for (int i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
        int j = 1;
        int n3 = 0;
        while (j != 0) {
            int n4 = 0;
            int addChildrenToSolver = 0;
            int n5 = 0;
        Label_0519_Outer:
            while (true) {
                n4 = n3 + 1;
                addChildrenToSolver = j;
                boolean b5 = false;
                int max3 = 0;
                int max4 = 0;
            Label_0519:
                while (true) {
                    while (true) {
                        try {
                            this.mSystem.reset();
                            addChildrenToSolver = j;
                            final boolean b3 = (addChildrenToSolver = (this.addChildrenToSolver(this.mSystem, Integer.MAX_VALUE) ? 1 : 0)) != 0;
                            if (b3) {
                                addChildrenToSolver = (b3 ? 1 : 0);
                                this.mSystem.minimize();
                                addChildrenToSolver = (b3 ? 1 : 0);
                            }
                            if (addChildrenToSolver != 0) {
                                this.updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE, this.flags);
                                final boolean b4 = false;
                                b5 = false;
                                addChildrenToSolver = (b4 ? 1 : 0);
                                n5 = n;
                                if (n4 >= 8) {
                                    break;
                                }
                                addChildrenToSolver = (b4 ? 1 : 0);
                                n5 = n;
                                if (this.flags[2]) {
                                    max3 = 0;
                                    max4 = 0;
                                    for (int k = 0; k < size; ++k) {
                                        final ConstraintWidget constraintWidget2 = this.mChildren.get(k);
                                        max3 = Math.max(max3, constraintWidget2.mX + constraintWidget2.getWidth());
                                        max4 = Math.max(max4, constraintWidget2.mY + constraintWidget2.getHeight());
                                    }
                                    break Label_0519;
                                }
                                break;
                            }
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            continue Label_0519_Outer;
                        }
                        break;
                    }
                    this.updateFromSolver(this.mSystem, Integer.MAX_VALUE);
                    for (int l = 0; l < size; ++l) {
                        final ConstraintWidget constraintWidget3 = this.mChildren.get(l);
                        if (constraintWidget3.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget3.getWidth() < constraintWidget3.getWrapWidth()) {
                            this.flags[2] = true;
                            continue Label_0519;
                        }
                        if (constraintWidget3.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget3.getHeight() < constraintWidget3.getWrapHeight()) {
                            this.flags[2] = true;
                            continue Label_0519;
                        }
                    }
                    continue Label_0519;
                }
                final int max5 = Math.max(this.mMinWidth, max3);
                final int max6 = Math.max(this.mMinHeight, max4);
                boolean b6 = b5;
                int n6 = n;
                if (mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    b6 = b5;
                    n6 = n;
                    if (this.getWidth() < max5) {
                        this.setWidth(max5);
                        this.mHorizontalDimensionBehaviour = DimensionBehaviour.WRAP_CONTENT;
                        n6 = 1;
                        b6 = true;
                    }
                }
                addChildrenToSolver = (b6 ? 1 : 0);
                n5 = n6;
                if (mVerticalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT) {
                    break;
                }
                addChildrenToSolver = (b6 ? 1 : 0);
                n5 = n6;
                if (this.getHeight() < max6) {
                    this.setHeight(max6);
                    this.mVerticalDimensionBehaviour = DimensionBehaviour.WRAP_CONTENT;
                    n5 = 1;
                    addChildrenToSolver = 1;
                    break;
                }
                break;
            }
            final int max7 = Math.max(this.mMinWidth, this.getWidth());
            if (max7 > this.getWidth()) {
                this.setWidth(max7);
                this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                n5 = 1;
                addChildrenToSolver = 1;
            }
            final int max8 = Math.max(this.mMinHeight, this.getHeight());
            int n7 = n5;
            if (max8 > this.getHeight()) {
                this.setHeight(max8);
                this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                n7 = 1;
                addChildrenToSolver = 1;
            }
            n3 = n4;
            j = addChildrenToSolver;
            if ((n = n7) == 0) {
                int n8 = addChildrenToSolver;
                int n9 = n7;
                if (this.mHorizontalDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    n8 = addChildrenToSolver;
                    n9 = n7;
                    if (max > 0) {
                        n8 = addChildrenToSolver;
                        n9 = n7;
                        if (this.getWidth() > max) {
                            this.mWidthMeasuredTooSmall = true;
                            n9 = 1;
                            this.mHorizontalDimensionBehaviour = DimensionBehaviour.FIXED;
                            this.setWidth(max);
                            n8 = 1;
                        }
                    }
                }
                n3 = n4;
                j = n8;
                n = n9;
                if (this.mVerticalDimensionBehaviour != DimensionBehaviour.WRAP_CONTENT) {
                    continue;
                }
                n3 = n4;
                j = n8;
                n = n9;
                if (max2 <= 0) {
                    continue;
                }
                n3 = n4;
                j = n8;
                n = n9;
                if (this.getHeight() <= max2) {
                    continue;
                }
                this.mHeightMeasuredTooSmall = true;
                n = 1;
                this.mVerticalDimensionBehaviour = DimensionBehaviour.FIXED;
                this.setHeight(max2);
                j = 1;
                n3 = n4;
            }
        }
        if (this.mParent != null) {
            final int max9 = Math.max(this.mMinWidth, this.getWidth());
            final int max10 = Math.max(this.mMinHeight, this.getHeight());
            this.mSnapshot.applyTo(this);
            this.setWidth(this.mPaddingLeft + max9 + this.mPaddingRight);
            this.setHeight(this.mPaddingTop + max10 + this.mPaddingBottom);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (n != 0) {
            this.mHorizontalDimensionBehaviour = mHorizontalDimensionBehaviour;
            this.mVerticalDimensionBehaviour = mVerticalDimensionBehaviour;
        }
        this.resetSolverVariables(this.mSystem.getCache());
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    public int layoutFindGroups() {
        final ConstraintAnchor.Type[] array = { ConstraintAnchor.Type.LEFT, ConstraintAnchor.Type.RIGHT, ConstraintAnchor.Type.TOP, ConstraintAnchor.Type.BASELINE, ConstraintAnchor.Type.BOTTOM };
        int n = 1;
        final int size = this.mChildren.size();
        int n6;
        for (int i = 0; i < size; ++i, n = n6) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            final ConstraintAnchor mLeft = constraintWidget.mLeft;
            int n2;
            if (mLeft.mTarget != null) {
                if (setGroup(mLeft, n) == (n2 = n)) {
                    n2 = n + 1;
                }
            }
            else {
                mLeft.mGroup = Integer.MAX_VALUE;
                n2 = n;
            }
            final ConstraintAnchor mTop = constraintWidget.mTop;
            int n3;
            if (mTop.mTarget != null) {
                if (setGroup(mTop, n2) == (n3 = n2)) {
                    n3 = n2 + 1;
                }
            }
            else {
                mTop.mGroup = Integer.MAX_VALUE;
                n3 = n2;
            }
            final ConstraintAnchor mRight = constraintWidget.mRight;
            int n4;
            if (mRight.mTarget != null) {
                if (setGroup(mRight, n3) == (n4 = n3)) {
                    n4 = n3 + 1;
                }
            }
            else {
                mRight.mGroup = Integer.MAX_VALUE;
                n4 = n3;
            }
            final ConstraintAnchor mBottom = constraintWidget.mBottom;
            int n5;
            if (mBottom.mTarget != null) {
                if (setGroup(mBottom, n4) == (n5 = n4)) {
                    n5 = n4 + 1;
                }
            }
            else {
                mBottom.mGroup = Integer.MAX_VALUE;
                n5 = n4;
            }
            final ConstraintAnchor mBaseline = constraintWidget.mBaseline;
            if (mBaseline.mTarget != null) {
                if (setGroup(mBaseline, n5) == (n6 = n5)) {
                    n6 = n5 + 1;
                }
            }
            else {
                mBaseline.mGroup = Integer.MAX_VALUE;
                n6 = n5;
            }
        }
        int j = 1;
        int n7 = 0;
        int n8 = 0;
        while (j != 0) {
            int n9 = 0;
            final int n10 = n7 + 1;
            int n11 = 0;
            int n12 = n8;
            while (true) {
                n7 = n10;
                n8 = n12;
                j = n9;
                if (n11 >= size) {
                    break;
                }
                final ConstraintWidget constraintWidget2 = this.mChildren.get(n11);
                for (int k = 0; k < array.length; ++k) {
                    final ConstraintAnchor.Type type = array[k];
                    ConstraintAnchor constraintAnchor = null;
                    switch (type) {
                        case LEFT: {
                            constraintAnchor = constraintWidget2.mLeft;
                            break;
                        }
                        case TOP: {
                            constraintAnchor = constraintWidget2.mTop;
                            break;
                        }
                        case RIGHT: {
                            constraintAnchor = constraintWidget2.mRight;
                            break;
                        }
                        case BOTTOM: {
                            constraintAnchor = constraintWidget2.mBottom;
                            break;
                        }
                        case BASELINE: {
                            constraintAnchor = constraintWidget2.mBaseline;
                            break;
                        }
                    }
                    final ConstraintAnchor mTarget = constraintAnchor.mTarget;
                    if (mTarget != null) {
                        int n13 = n12;
                        int n14 = n9;
                        if (mTarget.mOwner.getParent() != null) {
                            n13 = n12;
                            n14 = n9;
                            if (mTarget.mGroup != constraintAnchor.mGroup) {
                                int n15;
                                if (constraintAnchor.mGroup > mTarget.mGroup) {
                                    n15 = mTarget.mGroup;
                                }
                                else {
                                    n15 = constraintAnchor.mGroup;
                                }
                                constraintAnchor.mGroup = n15;
                                mTarget.mGroup = n15;
                                n13 = n12 + 1;
                                n14 = 1;
                            }
                        }
                        final ConstraintAnchor opposite = mTarget.getOpposite();
                        n12 = n13;
                        n9 = n14;
                        if (opposite != null) {
                            n12 = n13;
                            n9 = n14;
                            if (opposite.mGroup != constraintAnchor.mGroup) {
                                int n16;
                                if (constraintAnchor.mGroup > opposite.mGroup) {
                                    n16 = opposite.mGroup;
                                }
                                else {
                                    n16 = constraintAnchor.mGroup;
                                }
                                constraintAnchor.mGroup = n16;
                                opposite.mGroup = n16;
                                n12 = n13 + 1;
                                n9 = 1;
                            }
                        }
                    }
                }
                ++n11;
            }
        }
        final int[] array2 = new int[this.mChildren.size() * array.length + 1];
        Arrays.fill(array2, -1);
        int l = 0;
        int n17 = 0;
        while (l < size) {
            final ConstraintWidget constraintWidget3 = this.mChildren.get(l);
            final ConstraintAnchor mLeft2 = constraintWidget3.mLeft;
            int n18;
            if (mLeft2.mGroup != Integer.MAX_VALUE) {
                final int mGroup = mLeft2.mGroup;
                if (array2[mGroup] == -1) {
                    n18 = n17 + 1;
                    array2[mGroup] = n17;
                }
                else {
                    n18 = n17;
                }
                mLeft2.mGroup = array2[mGroup];
            }
            else {
                n18 = n17;
            }
            final ConstraintAnchor mTop2 = constraintWidget3.mTop;
            int n19 = n18;
            if (mTop2.mGroup != Integer.MAX_VALUE) {
                final int mGroup2 = mTop2.mGroup;
                n19 = n18;
                if (array2[mGroup2] == -1) {
                    array2[mGroup2] = n18;
                    n19 = n18 + 1;
                }
                mTop2.mGroup = array2[mGroup2];
            }
            final ConstraintAnchor mRight2 = constraintWidget3.mRight;
            int n20 = n19;
            if (mRight2.mGroup != Integer.MAX_VALUE) {
                final int mGroup3 = mRight2.mGroup;
                n20 = n19;
                if (array2[mGroup3] == -1) {
                    array2[mGroup3] = n19;
                    n20 = n19 + 1;
                }
                mRight2.mGroup = array2[mGroup3];
            }
            final ConstraintAnchor mBottom2 = constraintWidget3.mBottom;
            int n21 = n20;
            if (mBottom2.mGroup != Integer.MAX_VALUE) {
                final int mGroup4 = mBottom2.mGroup;
                n21 = n20;
                if (array2[mGroup4] == -1) {
                    array2[mGroup4] = n20;
                    n21 = n20 + 1;
                }
                mBottom2.mGroup = array2[mGroup4];
            }
            final ConstraintAnchor mBaseline2 = constraintWidget3.mBaseline;
            int n22 = n21;
            if (mBaseline2.mGroup != Integer.MAX_VALUE) {
                final int mGroup5 = mBaseline2.mGroup;
                n22 = n21;
                if (array2[mGroup5] == -1) {
                    array2[mGroup5] = n21;
                    n22 = n21 + 1;
                }
                mBaseline2.mGroup = array2[mGroup5];
            }
            ++l;
            n17 = n22;
        }
        return n17;
    }
    
    public int layoutFindGroupsSimple() {
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.mLeft.mGroup = 0;
            constraintWidget.mRight.mGroup = 0;
            constraintWidget.mTop.mGroup = 1;
            constraintWidget.mBottom.mGroup = 1;
            constraintWidget.mBaseline.mGroup = 1;
        }
        return 2;
    }
    
    public void layoutWithGroup(int width) {
        final int mx = this.mX;
        final int my = this.mY;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.mX = 0;
            this.mY = 0;
            this.resetAnchors();
            this.resetSolverVariables(this.mSystem.getCache());
        }
        else {
            this.mX = 0;
            this.mY = 0;
        }
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            if (constraintWidget instanceof WidgetContainer) {
                ((WidgetContainer)constraintWidget).layout();
            }
        }
        this.mLeft.mGroup = 0;
        this.mRight.mGroup = 0;
        this.mTop.mGroup = 1;
        this.mBottom.mGroup = 1;
        this.mSystem.reset();
        int j = 0;
    Label_0207_Outer:
        while (j < width) {
            while (true) {
                try {
                    this.addToSolver(this.mSystem, j);
                    this.mSystem.minimize();
                    this.updateFromSolver(this.mSystem, j);
                    this.updateFromSolver(this.mSystem, -2);
                    ++j;
                    continue Label_0207_Outer;
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
            break;
        }
        if (this.mParent != null) {
            width = this.getWidth();
            final int height = this.getHeight();
            this.mSnapshot.applyTo(this);
            this.setWidth(width);
            this.setHeight(height);
        }
        else {
            this.mX = mx;
            this.mY = my;
        }
        if (this == this.getRootConstraintContainer()) {
            this.updateDrawPosition();
        }
    }
    
    @Override
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        super.reset();
    }
    
    public void setOptimizationLevel(final int mOptimizationLevel) {
        this.mOptimizationLevel = mOptimizationLevel;
    }
    
    public void setPadding(final int mPaddingLeft, final int mPaddingTop, final int mPaddingRight, final int mPaddingBottom) {
        this.mPaddingLeft = mPaddingLeft;
        this.mPaddingTop = mPaddingTop;
        this.mPaddingRight = mPaddingRight;
        this.mPaddingBottom = mPaddingBottom;
    }
    
    public void updateChildrenFromSolver(final LinearSystem linearSystem, final int n, final boolean[] array) {
        array[2] = false;
        this.updateFromSolver(linearSystem, n);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem, n);
            if (constraintWidget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                array[2] = true;
            }
            if (constraintWidget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                array[2] = true;
            }
        }
    }
}
