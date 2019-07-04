// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;
import java.util.HashSet;
import android.support.constraint.solver.SolverVariable;

public class ConstraintAnchor
{
    private static final boolean ALLOW_BINARY = false;
    public static final int ANY_GROUP = Integer.MAX_VALUE;
    public static final int APPLY_GROUP_RESULTS = -2;
    public static final int AUTO_CONSTRAINT_CREATOR = 2;
    public static final int SCOUT_CREATOR = 1;
    private static final int UNSET_GONE_MARGIN = -1;
    public static final int USER_CREATOR = 0;
    public static final boolean USE_CENTER_ANCHOR = false;
    private int mConnectionCreator;
    private ConnectionType mConnectionType;
    int mGoneMargin;
    int mGroup;
    public int mMargin;
    final ConstraintWidget mOwner;
    SolverVariable mSolverVariable;
    private Strength mStrength;
    ConstraintAnchor mTarget;
    final Type mType;
    
    public ConstraintAnchor(final ConstraintWidget mOwner, final Type mType) {
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.NONE;
        this.mConnectionType = ConnectionType.RELAXED;
        this.mConnectionCreator = 0;
        this.mGroup = Integer.MAX_VALUE;
        this.mOwner = mOwner;
        this.mType = mType;
    }
    
    private boolean isConnectionToMe(final ConstraintWidget constraintWidget, final HashSet<ConstraintWidget> set) {
        if (!set.contains(constraintWidget)) {
            set.add(constraintWidget);
            if (constraintWidget == this.getOwner()) {
                return true;
            }
            final ArrayList<ConstraintAnchor> anchors = constraintWidget.getAnchors();
            for (int i = 0; i < anchors.size(); ++i) {
                final ConstraintAnchor constraintAnchor = anchors.get(i);
                if (constraintAnchor.isSimilarDimensionConnection(this) && constraintAnchor.isConnected() && this.isConnectionToMe(constraintAnchor.getTarget().getOwner(), set)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private String toString(final HashSet<ConstraintAnchor> set) {
        if (set.add(this)) {
            final StringBuilder append = new StringBuilder().append(this.mOwner.getDebugName()).append(":").append(this.mType.toString());
            String string;
            if (this.mTarget != null) {
                string = " connected to " + this.mTarget.toString(set);
            }
            else {
                string = "";
            }
            return append.append(string).toString();
        }
        return "<-";
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, 0, false);
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n, final int n2) {
        return this.connect(constraintAnchor, n, -1, Strength.STRONG, n2, false);
    }
    
    public boolean connect(final ConstraintAnchor mTarget, final int mMargin, final int mGoneMargin, final Strength mStrength, final int mConnectionCreator, final boolean b) {
        if (mTarget == null) {
            this.mTarget = null;
            this.mMargin = 0;
            this.mGoneMargin = -1;
            this.mStrength = Strength.NONE;
            this.mConnectionCreator = 2;
            return true;
        }
        if (!b && !this.isValidConnection(mTarget)) {
            return false;
        }
        this.mTarget = mTarget;
        if (mMargin > 0) {
            this.mMargin = mMargin;
        }
        else {
            this.mMargin = 0;
        }
        this.mGoneMargin = mGoneMargin;
        this.mStrength = mStrength;
        this.mConnectionCreator = mConnectionCreator;
        return true;
    }
    
    public boolean connect(final ConstraintAnchor constraintAnchor, final int n, final Strength strength, final int n2) {
        return this.connect(constraintAnchor, n, -1, strength, n2, false);
    }
    
    public int getConnectionCreator() {
        return this.mConnectionCreator;
    }
    
    public ConnectionType getConnectionType() {
        return this.mConnectionType;
    }
    
    public int getGroup() {
        return this.mGroup;
    }
    
    public int getMargin() {
        if (this.mOwner.getVisibility() == 8) {
            return 0;
        }
        if (this.mGoneMargin > -1 && this.mTarget != null && this.mTarget.mOwner.getVisibility() == 8) {
            return this.mGoneMargin;
        }
        return this.mMargin;
    }
    
    public final ConstraintAnchor getOpposite() {
        switch (this.mType) {
            default: {
                return null;
            }
            case LEFT: {
                return this.mOwner.mRight;
            }
            case RIGHT: {
                return this.mOwner.mLeft;
            }
            case TOP: {
                return this.mOwner.mBottom;
            }
            case BOTTOM: {
                return this.mOwner.mTop;
            }
        }
    }
    
    public ConstraintWidget getOwner() {
        return this.mOwner;
    }
    
    public int getPriorityLevel() {
        switch (this.mType) {
            default: {
                return 0;
            }
            case BASELINE: {
                return 1;
            }
            case LEFT: {
                return 2;
            }
            case RIGHT: {
                return 2;
            }
            case TOP: {
                return 2;
            }
            case BOTTOM: {
                return 2;
            }
            case CENTER: {
                return 2;
            }
        }
    }
    
    public int getSnapPriorityLevel() {
        boolean b = true;
        switch (this.mType) {
            default: {
                b = false;
                return b ? 1 : 0;
            }
            case LEFT:
            case RIGHT:
            case CENTER_Y: {
                return b ? 1 : 0;
            }
            case CENTER_X: {
                return 0;
            }
            case TOP: {
                return 0;
            }
            case BOTTOM: {
                return 0;
            }
            case BASELINE: {
                return 2;
            }
            case CENTER: {
                return 3;
            }
        }
    }
    
    public SolverVariable getSolverVariable() {
        return this.mSolverVariable;
    }
    
    public Strength getStrength() {
        return this.mStrength;
    }
    
    public ConstraintAnchor getTarget() {
        return this.mTarget;
    }
    
    public Type getType() {
        return this.mType;
    }
    
    public boolean isConnected() {
        return this.mTarget != null;
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget constraintWidget) {
        if (!this.isConnectionToMe(constraintWidget, new HashSet<ConstraintWidget>())) {
            final ConstraintWidget parent = this.getOwner().getParent();
            if (parent == constraintWidget) {
                return true;
            }
            if (constraintWidget.getParent() == parent) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isConnectionAllowed(final ConstraintWidget constraintWidget, final ConstraintAnchor constraintAnchor) {
        return this.isConnectionAllowed(constraintWidget);
    }
    
    public boolean isSideAnchor() {
        switch (this.mType) {
            default: {
                return false;
            }
            case LEFT:
            case RIGHT:
            case TOP:
            case BOTTOM: {
                return true;
            }
        }
    }
    
    public boolean isSimilarDimensionConnection(final ConstraintAnchor constraintAnchor) {
        boolean b = true;
        final boolean b2 = false;
        final Type type = constraintAnchor.getType();
        boolean b3 = false;
        if (type == this.mType) {
            b3 = true;
        }
        else {
            switch (this.mType) {
                default: {
                    return false;
                }
                case CENTER: {
                    if (type == Type.BASELINE) {
                        b = false;
                    }
                    return b;
                }
                case LEFT:
                case RIGHT:
                case CENTER_X: {
                    if (type != Type.LEFT && type != Type.RIGHT) {
                        b3 = b2;
                        if (type != Type.CENTER_X) {
                            break;
                        }
                    }
                    return true;
                }
                case TOP:
                case BOTTOM:
                case CENTER_Y:
                case BASELINE: {
                    if (type != Type.TOP && type != Type.BOTTOM && type != Type.CENTER_Y) {
                        b3 = b2;
                        if (type != Type.BASELINE) {
                            break;
                        }
                    }
                    return true;
                }
            }
        }
        return b3;
    }
    
    public boolean isSnapCompatibleWith(final ConstraintAnchor constraintAnchor) {
        if (this.mType == Type.CENTER) {
            return false;
        }
        if (this.mType == constraintAnchor.getType()) {
            return true;
        }
        switch (this.mType) {
            default: {
                return false;
            }
            case LEFT: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case RIGHT: {
                        return true;
                    }
                    case CENTER_X: {
                        return true;
                    }
                }
                break;
            }
            case RIGHT: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case LEFT: {
                        return true;
                    }
                    case CENTER_X: {
                        return true;
                    }
                }
                break;
            }
            case CENTER_X: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case LEFT: {
                        return true;
                    }
                    case RIGHT: {
                        return true;
                    }
                }
                break;
            }
            case TOP: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case BOTTOM: {
                        return true;
                    }
                    case CENTER_Y: {
                        return true;
                    }
                }
                break;
            }
            case BOTTOM: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case TOP: {
                        return true;
                    }
                    case CENTER_Y: {
                        return true;
                    }
                }
                break;
            }
            case CENTER_Y: {
                switch (constraintAnchor.getType()) {
                    default: {
                        return false;
                    }
                    case TOP: {
                        return true;
                    }
                    case BOTTOM: {
                        return true;
                    }
                }
                break;
            }
        }
    }
    
    public boolean isValidConnection(final ConstraintAnchor constraintAnchor) {
        boolean b = true;
        if (constraintAnchor != null) {
            final Type type = constraintAnchor.getType();
            if (type == this.mType) {
                if (this.mType != Type.CENTER && (this.mType != Type.BASELINE || (constraintAnchor.getOwner().hasBaseline() && this.getOwner().hasBaseline()))) {
                    return true;
                }
            }
            else {
                switch (this.mType) {
                    default: {
                        return false;
                    }
                    case CENTER: {
                        if (type == Type.BASELINE || type == Type.CENTER_X || type == Type.CENTER_Y) {
                            b = false;
                        }
                        return b;
                    }
                    case LEFT:
                    case RIGHT: {
                        boolean b2 = type == Type.LEFT || type == Type.RIGHT;
                        if (constraintAnchor.getOwner() instanceof Guideline) {
                            b2 = (b2 || type == Type.CENTER_X);
                        }
                        return b2;
                    }
                    case TOP:
                    case BOTTOM: {
                        boolean b3 = type == Type.TOP || type == Type.BOTTOM;
                        if (constraintAnchor.getOwner() instanceof Guideline) {
                            b3 = (b3 || type == Type.CENTER_Y);
                        }
                        return b3;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isVerticalAnchor() {
        switch (this.mType) {
            default: {
                return true;
            }
            case CENTER:
            case LEFT:
            case RIGHT:
            case CENTER_X: {
                return false;
            }
        }
    }
    
    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
        this.mStrength = Strength.STRONG;
        this.mConnectionCreator = 0;
        this.mConnectionType = ConnectionType.RELAXED;
    }
    
    public void resetSolverVariable(final Cache cache) {
        if (this.mSolverVariable == null) {
            this.mSolverVariable = new SolverVariable(SolverVariable.Type.UNRESTRICTED);
            return;
        }
        this.mSolverVariable.reset();
    }
    
    public void setConnectionCreator(final int mConnectionCreator) {
        this.mConnectionCreator = mConnectionCreator;
    }
    
    public void setConnectionType(final ConnectionType mConnectionType) {
        this.mConnectionType = mConnectionType;
    }
    
    public void setGoneMargin(final int mGoneMargin) {
        if (this.isConnected()) {
            this.mGoneMargin = mGoneMargin;
        }
    }
    
    public void setGroup(final int mGroup) {
        this.mGroup = mGroup;
    }
    
    public void setMargin(final int mMargin) {
        if (this.isConnected()) {
            this.mMargin = mMargin;
        }
    }
    
    public void setStrength(final Strength mStrength) {
        if (this.isConnected()) {
            this.mStrength = mStrength;
        }
    }
    
    @Override
    public String toString() {
        final HashSet<ConstraintAnchor> set = new HashSet<ConstraintAnchor>();
        final StringBuilder append = new StringBuilder().append(this.mOwner.getDebugName()).append(":").append(this.mType.toString());
        String string;
        if (this.mTarget != null) {
            string = " connected to " + this.mTarget.toString(set);
        }
        else {
            string = "";
        }
        return append.append(string).toString();
    }
    
    public enum ConnectionType
    {
        RELAXED, 
        STRICT;
    }
    
    public enum Strength
    {
        NONE, 
        STRONG, 
        WEAK;
    }
    
    public enum Type
    {
        BASELINE, 
        BOTTOM, 
        CENTER, 
        CENTER_X, 
        CENTER_Y, 
        LEFT, 
        NONE, 
        RIGHT, 
        TOP;
    }
}
