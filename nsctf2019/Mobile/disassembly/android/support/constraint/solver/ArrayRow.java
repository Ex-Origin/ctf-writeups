// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

public class ArrayRow
{
    private static final boolean DEBUG = false;
    float constantValue;
    boolean isSimpleDefinition;
    boolean used;
    SolverVariable variable;
    final ArrayLinkedVariables variables;
    
    public ArrayRow(final Cache cache) {
        this.variable = null;
        this.constantValue = 0.0f;
        this.used = false;
        this.isSimpleDefinition = false;
        this.variables = new ArrayLinkedVariables(this, cache);
    }
    
    public ArrayRow addError(final SolverVariable solverVariable, final SolverVariable solverVariable2) {
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }
    
    ArrayRow addSingleError(final SolverVariable solverVariable, final int n) {
        this.variables.put(solverVariable, n);
        return this;
    }
    
    ArrayRow createRowCentering(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final float n2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final int n3) {
        if (solverVariable2 == solverVariable3) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable2, -2.0f);
        }
        else if (n2 == 0.5f) {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            if (n > 0 || n3 > 0) {
                this.constantValue = -n + n3;
                return this;
            }
        }
        else {
            if (n2 <= 0.0f) {
                this.variables.put(solverVariable, -1.0f);
                this.variables.put(solverVariable2, 1.0f);
                this.constantValue = n;
                return this;
            }
            if (n2 >= 1.0f) {
                this.variables.put(solverVariable3, -1.0f);
                this.variables.put(solverVariable4, 1.0f);
                this.constantValue = n3;
                return this;
            }
            this.variables.put(solverVariable, (1.0f - n2) * 1.0f);
            this.variables.put(solverVariable2, (1.0f - n2) * -1.0f);
            this.variables.put(solverVariable3, -1.0f * n2);
            this.variables.put(solverVariable4, 1.0f * n2);
            if (n > 0 || n3 > 0) {
                this.constantValue = -n * (1.0f - n2) + n3 * n2;
                return this;
            }
        }
        return this;
    }
    
    ArrayRow createRowDefinition(final SolverVariable variable, final int n) {
        this.variable = variable;
        variable.computedValue = n;
        this.constantValue = n;
        this.isSimpleDefinition = true;
        return this;
    }
    
    ArrayRow createRowDimensionPercent(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f - n);
        this.variables.put(solverVariable3, n);
        return this;
    }
    
    public ArrayRow createRowDimensionRatio(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final SolverVariable solverVariable4, final float n) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, n);
        this.variables.put(solverVariable4, -n);
        return this;
    }
    
    public ArrayRow createRowEqualDimension(float n, final float n2, final float n3, final SolverVariable solverVariable, final int n4, final SolverVariable solverVariable2, final int n5, final SolverVariable solverVariable3, final int n6, final SolverVariable solverVariable4, final int n7) {
        if (n2 == 0.0f || n == n3) {
            this.constantValue = -n4 - n5 + n6 + n7;
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable4, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
            return this;
        }
        n = n / n2 / (n3 / n2);
        this.constantValue = -n4 - n5 + n6 * n + n7 * n;
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable4, n);
        this.variables.put(solverVariable3, -n);
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final int n) {
        if (n < 0) {
            this.constantValue = n * -1;
            this.variables.put(solverVariable, 1.0f);
            return this;
        }
        this.constantValue = n;
        this.variables.put(solverVariable, -1.0f);
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable solverVariable, final SolverVariable solverVariable2, int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            final int n4 = n;
            n = n3;
            int n5;
            if ((n5 = n4) < 0) {
                n5 = n4 * -1;
                n = 1;
            }
            this.constantValue = n5;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        return this;
    }
    
    public ArrayRow createRowGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            final int n4 = n;
            n = n3;
            int n5;
            if ((n5 = n4) < 0) {
                n5 = n4 * -1;
                n = 1;
            }
            this.constantValue = n5;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable3, -1.0f);
        return this;
    }
    
    public ArrayRow createRowLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, int n) {
        int n2 = 0;
        final int n3 = 0;
        if (n != 0) {
            final int n4 = n;
            n = n3;
            int n5;
            if ((n5 = n4) < 0) {
                n5 = n4 * -1;
                n = 1;
            }
            this.constantValue = n5;
            n2 = n;
        }
        if (n2 == 0) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
            return this;
        }
        this.variables.put(solverVariable, 1.0f);
        this.variables.put(solverVariable2, -1.0f);
        this.variables.put(solverVariable3, 1.0f);
        return this;
    }
    
    void ensurePositiveConstant() {
        if (this.constantValue < 0.0f) {
            this.constantValue *= -1.0f;
            this.variables.invert();
        }
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        return this.variables.hasAtLeastOnePositiveVariable();
    }
    
    boolean hasKeyVariable() {
        return this.variable != null && (this.variable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }
    
    boolean hasVariable(final SolverVariable solverVariable) {
        return this.variables.containsKey(solverVariable);
    }
    
    void pickRowVariable() {
        final SolverVariable pickPivotCandidate = this.variables.pickPivotCandidate();
        if (pickPivotCandidate != null) {
            this.pivot(pickPivotCandidate);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
    }
    
    void pivot(final SolverVariable variable) {
        if (this.variable != null) {
            this.variables.put(this.variable, -1.0f);
            this.variable = null;
        }
        final float n = this.variables.remove(variable) * -1.0f;
        this.variable = variable;
        if (n == 1.0f) {
            return;
        }
        this.constantValue /= n;
        this.variables.divideByAmount(n);
    }
    
    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }
    
    int sizeInBytes() {
        int n = 0;
        if (this.variable != null) {
            n = 0 + 4;
        }
        return n + 4 + 4 + this.variables.sizeInBytes();
    }
    
    String toReadableString() {
        String s;
        if (this.variable == null) {
            s = "" + "0";
        }
        else {
            s = "" + this.variable;
        }
        final String string = s + " = ";
        int n = 0;
        String s2 = string;
        if (this.constantValue != 0.0f) {
            s2 = string + this.constantValue;
            n = 1;
        }
        for (int currentSize = this.variables.currentSize, i = 0; i < currentSize; ++i) {
            final SolverVariable variable = this.variables.getVariable(i);
            if (variable != null) {
                final float variableValue = this.variables.getVariableValue(i);
                final String string2 = variable.toString();
                float n2;
                String s3;
                if (n == 0) {
                    n2 = variableValue;
                    s3 = s2;
                    if (variableValue < 0.0f) {
                        s3 = s2 + "- ";
                        n2 = variableValue * -1.0f;
                    }
                }
                else if (variableValue > 0.0f) {
                    s3 = s2 + " + ";
                    n2 = variableValue;
                }
                else {
                    s3 = s2 + " - ";
                    n2 = variableValue * -1.0f;
                }
                if (n2 == 1.0f) {
                    s2 = s3 + string2;
                }
                else {
                    s2 = s3 + n2 + " " + string2;
                }
                n = 1;
            }
        }
        String string3 = s2;
        if (n == 0) {
            string3 = s2 + "0.0";
        }
        return string3;
    }
    
    @Override
    public String toString() {
        return this.toReadableString();
    }
    
    void updateClientEquations() {
        this.variables.updateClientEquations(this);
    }
    
    boolean updateRowWithEquation(final ArrayRow arrayRow) {
        this.variables.updateFromRow(this, arrayRow);
        return true;
    }
}
