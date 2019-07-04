// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

public class ConstraintHorizontalLayout extends ConstraintWidgetContainer
{
    private ContentAlignment mAlignment;
    
    public ConstraintHorizontalLayout() {
        this.mAlignment = ContentAlignment.MIDDLE;
    }
    
    public ConstraintHorizontalLayout(final int n, final int n2) {
        super(n, n2);
        this.mAlignment = ContentAlignment.MIDDLE;
    }
    
    public ConstraintHorizontalLayout(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mAlignment = ContentAlignment.MIDDLE;
    }
    
    @Override
    public void addToSolver(final LinearSystem linearSystem, final int n) {
        if (this.mChildren.size() != 0) {
            ConstraintWidget constraintWidget = this;
            for (int i = 0; i < this.mChildren.size(); ++i) {
                final ConstraintWidget constraintWidget2 = this.mChildren.get(i);
                if (constraintWidget != this) {
                    constraintWidget2.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.RIGHT);
                    constraintWidget.connect(ConstraintAnchor.Type.RIGHT, constraintWidget2, ConstraintAnchor.Type.LEFT);
                }
                else {
                    ConstraintAnchor.Strength strength = ConstraintAnchor.Strength.STRONG;
                    if (this.mAlignment == ContentAlignment.END) {
                        strength = ConstraintAnchor.Strength.WEAK;
                    }
                    constraintWidget2.connect(ConstraintAnchor.Type.LEFT, constraintWidget, ConstraintAnchor.Type.LEFT, 0, strength);
                }
                constraintWidget2.connect(ConstraintAnchor.Type.TOP, this, ConstraintAnchor.Type.TOP);
                constraintWidget2.connect(ConstraintAnchor.Type.BOTTOM, this, ConstraintAnchor.Type.BOTTOM);
                constraintWidget = constraintWidget2;
            }
            if (constraintWidget != this) {
                ConstraintAnchor.Strength strength2 = ConstraintAnchor.Strength.STRONG;
                if (this.mAlignment == ContentAlignment.BEGIN) {
                    strength2 = ConstraintAnchor.Strength.WEAK;
                }
                constraintWidget.connect(ConstraintAnchor.Type.RIGHT, this, ConstraintAnchor.Type.RIGHT, 0, strength2);
            }
        }
        super.addToSolver(linearSystem, n);
    }
    
    public enum ContentAlignment
    {
        BEGIN, 
        BOTTOM, 
        END, 
        LEFT, 
        MIDDLE, 
        RIGHT, 
        TOP, 
        VERTICAL_MIDDLE;
    }
}
