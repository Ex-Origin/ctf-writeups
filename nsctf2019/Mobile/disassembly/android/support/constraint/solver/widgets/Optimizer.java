// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.LinearSystem;

public class Optimizer
{
    static void applyDirectResolutionHorizontalChain(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, ConstraintWidget constraintWidget) {
        int n2 = 0;
        ConstraintWidget constraintWidget2 = null;
        int n3 = 0;
        float n4 = 0.0f;
        ConstraintWidget constraintWidget3 = constraintWidget;
        while (constraintWidget3 != null) {
            int n5;
            if (constraintWidget3.getVisibility() == 8) {
                n5 = 1;
            }
            else {
                n5 = 0;
            }
            int n6 = n3;
            float n7 = n4;
            int n8 = n2;
            if (n5 == 0) {
                n6 = n3 + 1;
                if (constraintWidget3.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    final int width = constraintWidget3.getWidth();
                    int margin;
                    if (constraintWidget3.mLeft.mTarget != null) {
                        margin = constraintWidget3.mLeft.getMargin();
                    }
                    else {
                        margin = 0;
                    }
                    int margin2;
                    if (constraintWidget3.mRight.mTarget != null) {
                        margin2 = constraintWidget3.mRight.getMargin();
                    }
                    else {
                        margin2 = 0;
                    }
                    n8 = n2 + width + margin + margin2;
                    n7 = n4;
                }
                else {
                    n7 = n4 + constraintWidget3.mHorizontalWeight;
                    n8 = n2;
                }
            }
            final ConstraintWidget constraintWidget4 = constraintWidget3;
            ConstraintWidget mOwner;
            if (constraintWidget3.mRight.mTarget != null) {
                mOwner = constraintWidget3.mRight.mTarget.mOwner;
            }
            else {
                mOwner = null;
            }
            n3 = n6;
            constraintWidget2 = constraintWidget4;
            n4 = n7;
            n2 = n8;
            constraintWidget3 = mOwner;
            if (mOwner != null) {
                if (mOwner.mLeft.mTarget != null) {
                    n3 = n6;
                    constraintWidget2 = constraintWidget4;
                    n4 = n7;
                    n2 = n8;
                    constraintWidget3 = mOwner;
                    if (mOwner.mLeft.mTarget == null) {
                        continue;
                    }
                    n3 = n6;
                    constraintWidget2 = constraintWidget4;
                    n4 = n7;
                    n2 = n8;
                    constraintWidget3 = mOwner;
                    if (mOwner.mLeft.mTarget.mOwner == constraintWidget4) {
                        continue;
                    }
                }
                constraintWidget3 = null;
                n3 = n6;
                constraintWidget2 = constraintWidget4;
                n4 = n7;
                n2 = n8;
            }
        }
        int right = 0;
        if (constraintWidget2 != null) {
            int x;
            if (constraintWidget2.mRight.mTarget != null) {
                x = constraintWidget2.mRight.mTarget.mOwner.getX();
            }
            else {
                x = 0;
            }
            right = x;
            if (constraintWidget2.mRight.mTarget != null) {
                right = x;
                if (constraintWidget2.mRight.mTarget.mOwner == constraintWidgetContainer) {
                    right = constraintWidgetContainer.getRight();
                }
            }
        }
        final float n9 = right - 0 - n2;
        float n10 = n9 / (n3 + 1);
        float n11 = 0.0f;
        if (n == 0) {
            n11 = n10;
        }
        else {
            n10 = n9 / n;
        }
        while (constraintWidget != null) {
            int margin3;
            if (constraintWidget.mLeft.mTarget != null) {
                margin3 = constraintWidget.mLeft.getMargin();
            }
            else {
                margin3 = 0;
            }
            int margin4;
            if (constraintWidget.mRight.mTarget != null) {
                margin4 = constraintWidget.mRight.getMargin();
            }
            else {
                margin4 = 0;
            }
            float n15;
            if (constraintWidget.getVisibility() != 8) {
                final float n12 = n11 + margin3;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, (int)(0.5f + n12));
                float n13;
                if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (n4 == 0.0f) {
                        n13 = n12 + (n10 - margin3 - margin4);
                    }
                    else {
                        n13 = n12 + (constraintWidget.mHorizontalWeight * n9 / n4 - margin3 - margin4);
                    }
                }
                else {
                    n13 = n12 + constraintWidget.getWidth();
                }
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, (int)(0.5f + n13));
                float n14 = n13;
                if (n == 0) {
                    n14 = n13 + n10;
                }
                n15 = n14 + margin4;
            }
            else {
                final float n16 = n11 - n10 / 2.0f;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, (int)(0.5f + n16));
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, (int)(0.5f + n16));
                n15 = n11;
            }
            ConstraintWidget mOwner2;
            if (constraintWidget.mRight.mTarget != null) {
                mOwner2 = constraintWidget.mRight.mTarget.mOwner;
            }
            else {
                mOwner2 = null;
            }
            ConstraintWidget constraintWidget5 = mOwner2;
            if (mOwner2 != null) {
                constraintWidget5 = mOwner2;
                if (mOwner2.mLeft.mTarget != null) {
                    constraintWidget5 = mOwner2;
                    if (mOwner2.mLeft.mTarget.mOwner != constraintWidget) {
                        constraintWidget5 = null;
                    }
                }
            }
            n11 = n15;
            if ((constraintWidget = constraintWidget5) == constraintWidgetContainer) {
                constraintWidget = null;
                n11 = n15;
            }
        }
    }
    
    static void applyDirectResolutionVerticalChain(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final int n, ConstraintWidget constraintWidget) {
        int n2 = 0;
        ConstraintWidget constraintWidget2 = null;
        int n3 = 0;
        float n4 = 0.0f;
        ConstraintWidget constraintWidget3 = constraintWidget;
        while (constraintWidget3 != null) {
            int n5;
            if (constraintWidget3.getVisibility() == 8) {
                n5 = 1;
            }
            else {
                n5 = 0;
            }
            int n6 = n3;
            float n7 = n4;
            int n8 = n2;
            if (n5 == 0) {
                n6 = n3 + 1;
                if (constraintWidget3.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    final int height = constraintWidget3.getHeight();
                    int margin;
                    if (constraintWidget3.mTop.mTarget != null) {
                        margin = constraintWidget3.mTop.getMargin();
                    }
                    else {
                        margin = 0;
                    }
                    int margin2;
                    if (constraintWidget3.mBottom.mTarget != null) {
                        margin2 = constraintWidget3.mBottom.getMargin();
                    }
                    else {
                        margin2 = 0;
                    }
                    n8 = n2 + height + margin + margin2;
                    n7 = n4;
                }
                else {
                    n7 = n4 + constraintWidget3.mVerticalWeight;
                    n8 = n2;
                }
            }
            final ConstraintWidget constraintWidget4 = constraintWidget3;
            ConstraintWidget mOwner;
            if (constraintWidget3.mBottom.mTarget != null) {
                mOwner = constraintWidget3.mBottom.mTarget.mOwner;
            }
            else {
                mOwner = null;
            }
            n3 = n6;
            constraintWidget2 = constraintWidget4;
            n4 = n7;
            n2 = n8;
            constraintWidget3 = mOwner;
            if (mOwner != null) {
                if (mOwner.mTop.mTarget != null) {
                    n3 = n6;
                    constraintWidget2 = constraintWidget4;
                    n4 = n7;
                    n2 = n8;
                    constraintWidget3 = mOwner;
                    if (mOwner.mTop.mTarget == null) {
                        continue;
                    }
                    n3 = n6;
                    constraintWidget2 = constraintWidget4;
                    n4 = n7;
                    n2 = n8;
                    constraintWidget3 = mOwner;
                    if (mOwner.mTop.mTarget.mOwner == constraintWidget4) {
                        continue;
                    }
                }
                constraintWidget3 = null;
                n3 = n6;
                constraintWidget2 = constraintWidget4;
                n4 = n7;
                n2 = n8;
            }
        }
        int bottom = 0;
        if (constraintWidget2 != null) {
            int x;
            if (constraintWidget2.mBottom.mTarget != null) {
                x = constraintWidget2.mBottom.mTarget.mOwner.getX();
            }
            else {
                x = 0;
            }
            bottom = x;
            if (constraintWidget2.mBottom.mTarget != null) {
                bottom = x;
                if (constraintWidget2.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                    bottom = constraintWidgetContainer.getBottom();
                }
            }
        }
        final float n9 = bottom - 0 - n2;
        float n10 = n9 / (n3 + 1);
        float n11 = 0.0f;
        if (n == 0) {
            n11 = n10;
        }
        else {
            n10 = n9 / n;
        }
        while (constraintWidget != null) {
            int margin3;
            if (constraintWidget.mTop.mTarget != null) {
                margin3 = constraintWidget.mTop.getMargin();
            }
            else {
                margin3 = 0;
            }
            int margin4;
            if (constraintWidget.mBottom.mTarget != null) {
                margin4 = constraintWidget.mBottom.getMargin();
            }
            else {
                margin4 = 0;
            }
            float n15;
            if (constraintWidget.getVisibility() != 8) {
                final float n12 = n11 + margin3;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, (int)(0.5f + n12));
                float n13;
                if (constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (n4 == 0.0f) {
                        n13 = n12 + (n10 - margin3 - margin4);
                    }
                    else {
                        n13 = n12 + (constraintWidget.mVerticalWeight * n9 / n4 - margin3 - margin4);
                    }
                }
                else {
                    n13 = n12 + constraintWidget.getHeight();
                }
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, (int)(0.5f + n13));
                float n14 = n13;
                if (n == 0) {
                    n14 = n13 + n10;
                }
                n15 = n14 + margin4;
            }
            else {
                final float n16 = n11 - n10 / 2.0f;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, (int)(0.5f + n16));
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, (int)(0.5f + n16));
                n15 = n11;
            }
            ConstraintWidget mOwner2;
            if (constraintWidget.mBottom.mTarget != null) {
                mOwner2 = constraintWidget.mBottom.mTarget.mOwner;
            }
            else {
                mOwner2 = null;
            }
            ConstraintWidget constraintWidget5 = mOwner2;
            if (mOwner2 != null) {
                constraintWidget5 = mOwner2;
                if (mOwner2.mTop.mTarget != null) {
                    constraintWidget5 = mOwner2;
                    if (mOwner2.mTop.mTarget.mOwner != constraintWidget) {
                        constraintWidget5 = null;
                    }
                }
            }
            n11 = n15;
            if ((constraintWidget = constraintWidget5) == constraintWidgetContainer) {
                constraintWidget = null;
                n11 = n15;
            }
        }
    }
    
    static void checkHorizontalSimpleDependency(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mHorizontalResolution = 1;
        }
        else {
            if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                final int mMargin = constraintWidget.mLeft.mMargin;
                final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
                linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
                linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
                constraintWidget.setHorizontalDimension(mMargin, n);
                constraintWidget.mHorizontalResolution = 2;
                return;
            }
            if (constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) {
                if (constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
                    int margin = constraintWidget.mLeft.getMargin();
                    final int margin2 = constraintWidget.mRight.getMargin();
                    int n2;
                    if (constraintWidgetContainer.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        n2 = constraintWidgetContainer.getWidth() - margin2;
                    }
                    else {
                        margin += (int)((constraintWidgetContainer.getWidth() - margin - margin2 - constraintWidget.getWidth()) * constraintWidget.mHorizontalBiasPercent + 0.5f);
                        n2 = margin + constraintWidget.getWidth();
                    }
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, margin);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n2);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setHorizontalDimension(margin, n2);
                    return;
                }
                constraintWidget.mHorizontalResolution = 1;
            }
            else {
                if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner == constraintWidgetContainer) {
                    final int margin3 = constraintWidget.mLeft.getMargin();
                    final int n3 = margin3 + constraintWidget.getWidth();
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, margin3);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n3);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setHorizontalDimension(margin3, n3);
                    return;
                }
                if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner == constraintWidgetContainer) {
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    final int n4 = constraintWidgetContainer.getWidth() - constraintWidget.mRight.getMargin();
                    final int n5 = n4 - constraintWidget.getWidth();
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n5);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n4);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setHorizontalDimension(n5, n4);
                    return;
                }
                if (constraintWidget.mLeft.mTarget != null && constraintWidget.mLeft.mTarget.mOwner.mHorizontalResolution == 2) {
                    final SolverVariable mSolverVariable = constraintWidget.mLeft.mTarget.mSolverVariable;
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    final int n6 = (int)(mSolverVariable.computedValue + constraintWidget.mLeft.getMargin() + 0.5f);
                    final int n7 = n6 + constraintWidget.getWidth();
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n6);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n7);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setHorizontalDimension(n6, n7);
                    return;
                }
                if (constraintWidget.mRight.mTarget != null && constraintWidget.mRight.mTarget.mOwner.mHorizontalResolution == 2) {
                    final SolverVariable mSolverVariable2 = constraintWidget.mRight.mTarget.mSolverVariable;
                    constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                    constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                    final int n8 = (int)(mSolverVariable2.computedValue - constraintWidget.mRight.getMargin() + 0.5f);
                    final int n9 = n8 - constraintWidget.getWidth();
                    linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n9);
                    linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n8);
                    constraintWidget.mHorizontalResolution = 2;
                    constraintWidget.setHorizontalDimension(n9, n8);
                    return;
                }
                boolean b;
                if (constraintWidget.mLeft.mTarget != null) {
                    b = true;
                }
                else {
                    b = false;
                }
                boolean b2;
                if (constraintWidget.mRight.mTarget != null) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                if (!b && !b2) {
                    if (!(constraintWidget instanceof Guideline)) {
                        constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                        constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                        final int x = constraintWidget.getX();
                        final int width = constraintWidget.getWidth();
                        linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, x);
                        linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, x + width);
                        constraintWidget.mHorizontalResolution = 2;
                        return;
                    }
                    final Guideline guideline = (Guideline)constraintWidget;
                    if (guideline.getOrientation() == 1) {
                        constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
                        constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
                        float n10;
                        if (guideline.getRelativeBegin() != -1) {
                            n10 = guideline.getRelativeBegin();
                        }
                        else if (guideline.getRelativeEnd() != -1) {
                            n10 = constraintWidgetContainer.getWidth() - guideline.getRelativeEnd();
                        }
                        else {
                            n10 = constraintWidgetContainer.getWidth() * guideline.getRelativePercent();
                        }
                        final int n11 = (int)(0.5f + n10);
                        linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, n11);
                        linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n11);
                        constraintWidget.mHorizontalResolution = 2;
                        constraintWidget.mVerticalResolution = 2;
                        constraintWidget.setHorizontalDimension(n11, n11);
                        constraintWidget.setVerticalDimension(0, constraintWidgetContainer.getHeight());
                    }
                }
            }
        }
    }
    
    static void checkMatchParent(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidgetContainer.mHorizontalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mHorizontalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mLeft.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mLeft);
            constraintWidget.mRight.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mRight);
            final int mMargin = constraintWidget.mLeft.mMargin;
            final int n = constraintWidgetContainer.getWidth() - constraintWidget.mRight.mMargin;
            linearSystem.addEquality(constraintWidget.mLeft.mSolverVariable, mMargin);
            linearSystem.addEquality(constraintWidget.mRight.mSolverVariable, n);
            constraintWidget.setHorizontalDimension(mMargin, n);
            constraintWidget.mHorizontalResolution = 2;
        }
        if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
            constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
            constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
            final int mMargin2 = constraintWidget.mTop.mMargin;
            final int n2 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
            linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin2);
            linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
            if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin2);
            }
            constraintWidget.setVerticalDimension(mMargin2, n2);
            constraintWidget.mVerticalResolution = 2;
        }
    }
    
    static void checkVerticalSimpleDependency(final ConstraintWidgetContainer constraintWidgetContainer, final LinearSystem linearSystem, final ConstraintWidget constraintWidget) {
        if (constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
            constraintWidget.mVerticalResolution = 1;
        }
        else {
            if (constraintWidgetContainer.mVerticalDimensionBehaviour != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && constraintWidget.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
                constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                final int mMargin = constraintWidget.mTop.mMargin;
                final int n = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.mMargin;
                linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, mMargin);
                linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n);
                if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + mMargin);
                }
                constraintWidget.setVerticalDimension(mMargin, n);
                constraintWidget.mVerticalResolution = 2;
                return;
            }
            if (constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null) {
                if (constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                    int margin = constraintWidget.mTop.getMargin();
                    final int margin2 = constraintWidget.mBottom.getMargin();
                    int n2;
                    if (constraintWidgetContainer.mVerticalDimensionBehaviour == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        n2 = margin + constraintWidget.getHeight();
                    }
                    else {
                        margin = (int)(margin + (constraintWidgetContainer.getHeight() - margin - margin2 - constraintWidget.getHeight()) * constraintWidget.mVerticalBiasPercent + 0.5f);
                        n2 = margin + constraintWidget.getHeight();
                    }
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, margin);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n2);
                    if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                        linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + margin);
                    }
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setVerticalDimension(margin, n2);
                    return;
                }
                constraintWidget.mVerticalResolution = 1;
            }
            else {
                if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner == constraintWidgetContainer) {
                    final int margin3 = constraintWidget.mTop.getMargin();
                    final int n3 = margin3 + constraintWidget.getHeight();
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, margin3);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n3);
                    if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                        linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + margin3);
                    }
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setVerticalDimension(margin3, n3);
                    return;
                }
                if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner == constraintWidgetContainer) {
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    final int n4 = constraintWidgetContainer.getHeight() - constraintWidget.mBottom.getMargin();
                    final int n5 = n4 - constraintWidget.getHeight();
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n5);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n4);
                    if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                        linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n5);
                    }
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setVerticalDimension(n5, n4);
                    return;
                }
                if (constraintWidget.mTop.mTarget != null && constraintWidget.mTop.mTarget.mOwner.mVerticalResolution == 2) {
                    final SolverVariable mSolverVariable = constraintWidget.mTop.mTarget.mSolverVariable;
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    final int n6 = (int)(mSolverVariable.computedValue + constraintWidget.mTop.getMargin() + 0.5f);
                    final int n7 = n6 + constraintWidget.getHeight();
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n6);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n7);
                    if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                        linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n6);
                    }
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setVerticalDimension(n6, n7);
                    return;
                }
                if (constraintWidget.mBottom.mTarget != null && constraintWidget.mBottom.mTarget.mOwner.mVerticalResolution == 2) {
                    final SolverVariable mSolverVariable2 = constraintWidget.mBottom.mTarget.mSolverVariable;
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    final int n8 = (int)(mSolverVariable2.computedValue - constraintWidget.mBottom.getMargin() + 0.5f);
                    final int n9 = n8 - constraintWidget.getHeight();
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n9);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n8);
                    if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                        linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n9);
                    }
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setVerticalDimension(n9, n8);
                    return;
                }
                if (constraintWidget.mBaseline.mTarget != null && constraintWidget.mBaseline.mTarget.mOwner.mVerticalResolution == 2) {
                    final SolverVariable mSolverVariable3 = constraintWidget.mBaseline.mTarget.mSolverVariable;
                    constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                    constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                    final int n10 = (int)(mSolverVariable3.computedValue - constraintWidget.mBaselineDistance + 0.5f);
                    final int n11 = n10 + constraintWidget.getHeight();
                    linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n10);
                    linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n11);
                    linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + n10);
                    constraintWidget.mVerticalResolution = 2;
                    constraintWidget.setVerticalDimension(n10, n11);
                    return;
                }
                boolean b;
                if (constraintWidget.mBaseline.mTarget != null) {
                    b = true;
                }
                else {
                    b = false;
                }
                boolean b2;
                if (constraintWidget.mTop.mTarget != null) {
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                boolean b3;
                if (constraintWidget.mBottom.mTarget != null) {
                    b3 = true;
                }
                else {
                    b3 = false;
                }
                if (!b && !b2 && !b3) {
                    if (!(constraintWidget instanceof Guideline)) {
                        constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                        constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                        final int y = constraintWidget.getY();
                        final int height = constraintWidget.getHeight();
                        linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, y);
                        linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, y + height);
                        if (constraintWidget.mBaselineDistance > 0 || constraintWidget.getVisibility() == 8) {
                            linearSystem.addEquality(constraintWidget.mBaseline.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBaseline), constraintWidget.mBaselineDistance + y);
                        }
                        constraintWidget.mVerticalResolution = 2;
                        return;
                    }
                    final Guideline guideline = (Guideline)constraintWidget;
                    if (guideline.getOrientation() == 0) {
                        constraintWidget.mTop.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mTop);
                        constraintWidget.mBottom.mSolverVariable = linearSystem.createObjectVariable(constraintWidget.mBottom);
                        float n12;
                        if (guideline.getRelativeBegin() != -1) {
                            n12 = guideline.getRelativeBegin();
                        }
                        else if (guideline.getRelativeEnd() != -1) {
                            n12 = constraintWidgetContainer.getHeight() - guideline.getRelativeEnd();
                        }
                        else {
                            n12 = constraintWidgetContainer.getHeight() * guideline.getRelativePercent();
                        }
                        final int n13 = (int)(0.5f + n12);
                        linearSystem.addEquality(constraintWidget.mTop.mSolverVariable, n13);
                        linearSystem.addEquality(constraintWidget.mBottom.mSolverVariable, n13);
                        constraintWidget.mVerticalResolution = 2;
                        constraintWidget.mHorizontalResolution = 2;
                        constraintWidget.setVerticalDimension(n13, n13);
                        constraintWidget.setHorizontalDimension(0, constraintWidgetContainer.getWidth());
                    }
                }
            }
        }
    }
}
