// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable
{
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 6;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueId;
    public float computedValue;
    int definitionId;
    public int id;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Type mType;
    public int strength;
    float[] strengthVector;
    
    static {
        SolverVariable.uniqueId = 1;
    }
    
    public SolverVariable(final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[6];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mType = mType;
    }
    
    public SolverVariable(final String mName, final Type mType) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[6];
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mName = mName;
        this.mType = mType;
    }
    
    private static String getUniqueName(final Type type) {
        ++SolverVariable.uniqueId;
        switch (type) {
            default: {
                return "V" + SolverVariable.uniqueId;
            }
            case UNRESTRICTED: {
                return "U" + SolverVariable.uniqueId;
            }
            case CONSTANT: {
                return "C" + SolverVariable.uniqueId;
            }
            case SLACK: {
                return "S" + SolverVariable.uniqueId;
            }
            case ERROR: {
                return "e" + SolverVariable.uniqueId;
            }
        }
    }
    
    void addClientEquation(final ArrayRow arrayRow) {
        for (int i = 0; i < this.mClientEquationsCount; ++i) {
            if (this.mClientEquations[i] == arrayRow) {
                return;
            }
        }
        if (this.mClientEquationsCount >= this.mClientEquations.length) {
            this.mClientEquations = Arrays.copyOf(this.mClientEquations, this.mClientEquations.length * 2);
        }
        this.mClientEquations[this.mClientEquationsCount] = arrayRow;
        ++this.mClientEquationsCount;
    }
    
    void clearStrengths() {
        for (int i = 0; i < 6; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }
    
    public String getName() {
        return this.mName;
    }
    
    void removeClientEquation(final ArrayRow arrayRow) {
        for (int i = 0; i < this.mClientEquationsCount; ++i) {
            if (this.mClientEquations[i] == arrayRow) {
                for (int j = 0; j < this.mClientEquationsCount - i - 1; ++j) {
                    this.mClientEquations[i + j] = this.mClientEquations[i + j + 1];
                }
                --this.mClientEquationsCount;
                break;
            }
        }
    }
    
    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
    }
    
    public void setName(final String mName) {
        this.mName = mName;
    }
    
    public void setType(final Type mType) {
        this.mType = mType;
    }
    
    String strengthsToString() {
        String s = this + "[";
        for (int i = 0; i < this.strengthVector.length; ++i) {
            final String string = s + this.strengthVector[i];
            if (i < this.strengthVector.length - 1) {
                s = string + ", ";
            }
            else {
                s = string + "] ";
            }
        }
        return s;
    }
    
    @Override
    public String toString() {
        return "" + this.mName;
    }
    
    public enum Type
    {
        CONSTANT, 
        ERROR, 
        SLACK, 
        UNKNOWN, 
        UNRESTRICTED;
    }
}
