// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class Snapshot
{
    private ArrayList<Connection> mConnections;
    private int mHeight;
    private int mWidth;
    private int mX;
    private int mY;
    
    public Snapshot(final ConstraintWidget constraintWidget) {
        this.mConnections = new ArrayList<Connection>();
        this.mX = constraintWidget.getX();
        this.mY = constraintWidget.getY();
        this.mWidth = constraintWidget.getWidth();
        this.mHeight = constraintWidget.getHeight();
        final ArrayList<ConstraintAnchor> anchors = constraintWidget.getAnchors();
        for (int i = 0; i < anchors.size(); ++i) {
            this.mConnections.add(new Connection(anchors.get(i)));
        }
    }
    
    public void applyTo(final ConstraintWidget constraintWidget) {
        constraintWidget.setX(this.mX);
        constraintWidget.setY(this.mY);
        constraintWidget.setWidth(this.mWidth);
        constraintWidget.setHeight(this.mHeight);
        for (int i = 0; i < this.mConnections.size(); ++i) {
            this.mConnections.get(i).applyTo(constraintWidget);
        }
    }
    
    public void updateFrom(final ConstraintWidget constraintWidget) {
        this.mX = constraintWidget.getX();
        this.mY = constraintWidget.getY();
        this.mWidth = constraintWidget.getWidth();
        this.mHeight = constraintWidget.getHeight();
        for (int size = this.mConnections.size(), i = 0; i < size; ++i) {
            this.mConnections.get(i).updateFrom(constraintWidget);
        }
    }
    
    static class Connection
    {
        private ConstraintAnchor mAnchor;
        private int mCreator;
        private int mMargin;
        private ConstraintAnchor.Strength mStrengh;
        private ConstraintAnchor mTarget;
        
        public Connection(final ConstraintAnchor mAnchor) {
            this.mAnchor = mAnchor;
            this.mTarget = mAnchor.getTarget();
            this.mMargin = mAnchor.getMargin();
            this.mStrengh = mAnchor.getStrength();
            this.mCreator = mAnchor.getConnectionCreator();
        }
        
        public void applyTo(final ConstraintWidget constraintWidget) {
            constraintWidget.getAnchor(this.mAnchor.getType()).connect(this.mTarget, this.mMargin, this.mStrengh, this.mCreator);
        }
        
        public void updateFrom(final ConstraintWidget constraintWidget) {
            this.mAnchor = constraintWidget.getAnchor(this.mAnchor.getType());
            if (this.mAnchor != null) {
                this.mTarget = this.mAnchor.getTarget();
                this.mMargin = this.mAnchor.getMargin();
                this.mStrengh = this.mAnchor.getStrength();
                this.mCreator = this.mAnchor.getConnectionCreator();
                return;
            }
            this.mTarget = null;
            this.mMargin = 0;
            this.mStrengh = ConstraintAnchor.Strength.STRONG;
            this.mCreator = 0;
        }
    }
}
