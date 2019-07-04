// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

import java.util.ArrayList;

public class Goal
{
    ArrayList<SolverVariable> variables;
    
    public Goal() {
        this.variables = new ArrayList<SolverVariable>();
    }
    
    private void initFromSystemErrors(final LinearSystem linearSystem) {
        this.variables.clear();
        for (int i = 1; i < linearSystem.mNumColumns; ++i) {
            final SolverVariable solverVariable = linearSystem.mCache.mIndexedVariables[i];
            for (int j = 0; j < 6; ++j) {
                solverVariable.strengthVector[j] = 0.0f;
            }
            solverVariable.strengthVector[solverVariable.strength] = 1.0f;
            if (solverVariable.mType == SolverVariable.Type.ERROR) {
                this.variables.add(solverVariable);
            }
        }
    }
    
    SolverVariable getPivotCandidate() {
        final int size = this.variables.size();
        SolverVariable solverVariable = null;
        int n = 0;
        for (int i = 0; i < size; ++i) {
            final SolverVariable solverVariable2 = this.variables.get(i);
            for (int j = 5; j >= 0; --j) {
                final float n2 = solverVariable2.strengthVector[j];
                SolverVariable solverVariable3 = solverVariable;
                int n3 = n;
                if (solverVariable == null) {
                    solverVariable3 = solverVariable;
                    n3 = n;
                    if (n2 < 0.0f) {
                        solverVariable3 = solverVariable;
                        if (j >= (n3 = n)) {
                            n3 = j;
                            solverVariable3 = solverVariable2;
                        }
                    }
                }
                solverVariable = solverVariable3;
                n = n3;
                if (n2 > 0.0f) {
                    solverVariable = solverVariable3;
                    if (j > (n = n3)) {
                        n = j;
                        solverVariable = null;
                    }
                }
            }
        }
        return solverVariable;
    }
    
    @Override
    public String toString() {
        String string = "Goal: ";
        for (int size = this.variables.size(), i = 0; i < size; ++i) {
            string += this.variables.get(i).strengthsToString();
        }
        return string;
    }
    
    void updateFromSystem(final LinearSystem linearSystem) {
        this.initFromSystemErrors(linearSystem);
        for (int size = this.variables.size(), i = 0; i < size; ++i) {
            final SolverVariable solverVariable = this.variables.get(i);
            if (solverVariable.definitionId != -1) {
                final ArrayLinkedVariables variables = linearSystem.getRow(solverVariable.definitionId).variables;
                for (int currentSize = variables.currentSize, j = 0; j < currentSize; ++j) {
                    final SolverVariable variable = variables.getVariable(j);
                    if (variable != null) {
                        final float variableValue = variables.getVariableValue(j);
                        for (int k = 0; k < 6; ++k) {
                            final float[] strengthVector = variable.strengthVector;
                            strengthVector[k] += solverVariable.strengthVector[k] * variableValue;
                        }
                        if (!this.variables.contains(variable)) {
                            this.variables.add(variable);
                        }
                    }
                }
                solverVariable.clearStrengths();
            }
        }
    }
}
