// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

import java.util.Arrays;

public class ArrayLinkedVariables
{
    private static final boolean DEBUG = false;
    private static final int NONE = -1;
    private int ROW_SIZE;
    private SolverVariable candidate;
    int currentSize;
    private int[] mArrayIndices;
    private int[] mArrayNextIndices;
    private float[] mArrayValues;
    private final Cache mCache;
    private boolean mDidFillOnce;
    private int mHead;
    private int mLast;
    private final ArrayRow mRow;
    
    ArrayLinkedVariables(final ArrayRow mRow, final Cache mCache) {
        this.currentSize = 0;
        this.ROW_SIZE = 8;
        this.candidate = null;
        this.mArrayIndices = new int[this.ROW_SIZE];
        this.mArrayNextIndices = new int[this.ROW_SIZE];
        this.mArrayValues = new float[this.ROW_SIZE];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = mRow;
        this.mCache = mCache;
    }
    
    public final void add(final SolverVariable solverVariable, final float n) {
        if (n != 0.0f) {
            if (this.mHead == -1) {
                this.mHead = 0;
                this.mArrayValues[this.mHead] = n;
                this.mArrayIndices[this.mHead] = solverVariable.id;
                this.mArrayNextIndices[this.mHead] = -1;
                ++this.currentSize;
                if (!this.mDidFillOnce) {
                    ++this.mLast;
                }
            }
            else {
                int mHead = this.mHead;
                int n2 = -1;
                int n3 = 0;
                while (mHead != -1 && n3 < this.currentSize) {
                    final int n4 = this.mArrayIndices[mHead];
                    if (n4 == solverVariable.id) {
                        final float[] mArrayValues = this.mArrayValues;
                        mArrayValues[mHead] += n;
                        if (this.mArrayValues[mHead] == 0.0f) {
                            if (mHead == this.mHead) {
                                this.mHead = this.mArrayNextIndices[mHead];
                            }
                            else {
                                this.mArrayNextIndices[n2] = this.mArrayNextIndices[mHead];
                            }
                            this.mCache.mIndexedVariables[n4].removeClientEquation(this.mRow);
                            if (this.mDidFillOnce) {
                                this.mLast = mHead;
                            }
                            --this.currentSize;
                        }
                        return;
                    }
                    else {
                        if (this.mArrayIndices[mHead] < solverVariable.id) {
                            n2 = mHead;
                        }
                        mHead = this.mArrayNextIndices[mHead];
                        ++n3;
                    }
                }
                int n5 = this.mLast + 1;
                if (this.mDidFillOnce) {
                    if (this.mArrayIndices[this.mLast] == -1) {
                        n5 = this.mLast;
                    }
                    else {
                        n5 = this.mArrayIndices.length;
                    }
                }
                int n6;
                if ((n6 = n5) >= this.mArrayIndices.length) {
                    n6 = n5;
                    if (this.currentSize < this.mArrayIndices.length) {
                        int n7 = 0;
                        while (true) {
                            n6 = n5;
                            if (n7 >= this.mArrayIndices.length) {
                                break;
                            }
                            if (this.mArrayIndices[n7] == -1) {
                                n6 = n7;
                                break;
                            }
                            ++n7;
                        }
                    }
                }
                int length;
                if ((length = n6) >= this.mArrayIndices.length) {
                    length = this.mArrayIndices.length;
                    this.ROW_SIZE *= 2;
                    this.mDidFillOnce = false;
                    this.mLast = length - 1;
                    this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
                    this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
                    this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
                }
                this.mArrayIndices[length] = solverVariable.id;
                this.mArrayValues[length] = n;
                if (n2 != -1) {
                    this.mArrayNextIndices[length] = this.mArrayNextIndices[n2];
                    this.mArrayNextIndices[n2] = length;
                }
                else {
                    this.mArrayNextIndices[length] = this.mHead;
                    this.mHead = length;
                }
                ++this.currentSize;
                if (!this.mDidFillOnce) {
                    ++this.mLast;
                }
                if (this.mLast >= this.mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = this.mArrayIndices.length - 1;
                }
            }
        }
    }
    
    public final void clear() {
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }
    
    final boolean containsKey(final SolverVariable solverVariable) {
        if (this.mHead != -1) {
            for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
                if (this.mArrayIndices[mHead] == solverVariable.id) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void display() {
        final int currentSize = this.currentSize;
        System.out.print("{ ");
        for (int i = 0; i < currentSize; ++i) {
            final SolverVariable variable = this.getVariable(i);
            if (variable != null) {
                System.out.print(variable + " = " + this.getVariableValue(i) + " ");
            }
        }
        System.out.println(" }");
    }
    
    void divideByAmount(final float n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] /= n;
        }
    }
    
    public final float get(final SolverVariable solverVariable) {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayIndices[mHead] == solverVariable.id) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    SolverVariable getPivotCandidate() {
        SolverVariable candidate;
        if (this.candidate == null) {
            int mHead = this.mHead;
            int n = 0;
            SolverVariable solverVariable = null;
            while (true) {
                candidate = solverVariable;
                if (mHead == -1) {
                    break;
                }
                candidate = solverVariable;
                if (n >= this.currentSize) {
                    break;
                }
                SolverVariable solverVariable2 = solverVariable;
                Label_0088: {
                    if (this.mArrayValues[mHead] < 0.0f) {
                        final SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                        if (solverVariable != null) {
                            solverVariable2 = solverVariable;
                            if (solverVariable.strength >= solverVariable3.strength) {
                                break Label_0088;
                            }
                        }
                        solverVariable2 = solverVariable3;
                    }
                }
                mHead = this.mArrayNextIndices[mHead];
                ++n;
                solverVariable = solverVariable2;
            }
        }
        else {
            candidate = this.candidate;
        }
        return candidate;
    }
    
    final SolverVariable getVariable(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
            }
        }
        return null;
    }
    
    final float getVariableValue(final int n) {
        for (int mHead = this.mHead, n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
            if (n2 == n) {
                return this.mArrayValues[mHead];
            }
        }
        return 0.0f;
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            if (this.mArrayValues[mHead] > 0.0f) {
                return true;
            }
        }
        return false;
    }
    
    void invert() {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            final float[] mArrayValues = this.mArrayValues;
            mArrayValues[mHead] *= -1.0f;
        }
    }
    
    SolverVariable pickPivotCandidate() {
        SolverVariable solverVariable = null;
        SolverVariable solverVariable2 = null;
        SolverVariable solverVariable3;
        SolverVariable solverVariable4;
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n, solverVariable = solverVariable3, solverVariable2 = solverVariable4) {
            final float n2 = this.mArrayValues[mHead];
            float n3;
            if (n2 < 0.0f) {
                n3 = n2;
                if (n2 > -0.001f) {
                    this.mArrayValues[mHead] = 0.0f;
                    n3 = 0.0f;
                }
            }
            else {
                n3 = n2;
                if (n2 < 0.001f) {
                    this.mArrayValues[mHead] = 0.0f;
                    n3 = 0.0f;
                }
            }
            solverVariable3 = solverVariable;
            solverVariable4 = solverVariable2;
            if (n3 != 0.0f) {
                final SolverVariable solverVariable5 = this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
                if (solverVariable5.mType == SolverVariable.Type.UNRESTRICTED) {
                    if (n3 < 0.0f) {
                        return solverVariable5;
                    }
                    solverVariable3 = solverVariable;
                    if ((solverVariable4 = solverVariable2) == null) {
                        solverVariable4 = solverVariable5;
                        solverVariable3 = solverVariable;
                    }
                }
                else {
                    solverVariable3 = solverVariable;
                    solverVariable4 = solverVariable2;
                    if (n3 < 0.0f) {
                        if (solverVariable != null) {
                            solverVariable3 = solverVariable;
                            solverVariable4 = solverVariable2;
                            if (solverVariable5.strength >= solverVariable.strength) {
                                continue;
                            }
                        }
                        solverVariable3 = solverVariable5;
                        solverVariable4 = solverVariable2;
                    }
                }
            }
        }
        if (solverVariable2 != null) {
            return solverVariable2;
        }
        return solverVariable;
    }
    
    public final void put(final SolverVariable solverVariable, final float n) {
        if (n == 0.0f) {
            this.remove(solverVariable);
        }
        else if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = n;
            this.mArrayIndices[this.mHead] = solverVariable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
            }
        }
        else {
            int mHead = this.mHead;
            int n2 = -1;
            for (int n3 = 0; mHead != -1 && n3 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n3) {
                if (this.mArrayIndices[mHead] == solverVariable.id) {
                    this.mArrayValues[mHead] = n;
                    return;
                }
                if (this.mArrayIndices[mHead] < solverVariable.id) {
                    n2 = mHead;
                }
            }
            int n4 = this.mLast + 1;
            if (this.mDidFillOnce) {
                if (this.mArrayIndices[this.mLast] == -1) {
                    n4 = this.mLast;
                }
                else {
                    n4 = this.mArrayIndices.length;
                }
            }
            int n5;
            if ((n5 = n4) >= this.mArrayIndices.length) {
                n5 = n4;
                if (this.currentSize < this.mArrayIndices.length) {
                    int n6 = 0;
                    while (true) {
                        n5 = n4;
                        if (n6 >= this.mArrayIndices.length) {
                            break;
                        }
                        if (this.mArrayIndices[n6] == -1) {
                            n5 = n6;
                            break;
                        }
                        ++n6;
                    }
                }
            }
            int length;
            if ((length = n5) >= this.mArrayIndices.length) {
                length = this.mArrayIndices.length;
                this.ROW_SIZE *= 2;
                this.mDidFillOnce = false;
                this.mLast = length - 1;
                this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
                this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
                this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
            }
            this.mArrayIndices[length] = solverVariable.id;
            this.mArrayValues[length] = n;
            if (n2 != -1) {
                this.mArrayNextIndices[length] = this.mArrayNextIndices[n2];
                this.mArrayNextIndices[n2] = length;
            }
            else {
                this.mArrayNextIndices[length] = this.mHead;
                this.mHead = length;
            }
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
            }
            if (this.currentSize >= this.mArrayIndices.length) {
                this.mDidFillOnce = true;
            }
        }
    }
    
    public final float remove(final SolverVariable solverVariable) {
        if (this.candidate == solverVariable) {
            this.candidate = null;
        }
        if (this.mHead != -1) {
            int mHead = this.mHead;
            int n = -1;
            for (int n2 = 0; mHead != -1 && n2 < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n2) {
                final int n3 = this.mArrayIndices[mHead];
                if (n3 == solverVariable.id) {
                    if (mHead == this.mHead) {
                        this.mHead = this.mArrayNextIndices[mHead];
                    }
                    else {
                        this.mArrayNextIndices[n] = this.mArrayNextIndices[mHead];
                    }
                    this.mCache.mIndexedVariables[n3].removeClientEquation(this.mRow);
                    --this.currentSize;
                    this.mArrayIndices[mHead] = -1;
                    if (this.mDidFillOnce) {
                        this.mLast = mHead;
                    }
                    return this.mArrayValues[mHead];
                }
                n = mHead;
            }
        }
        return 0.0f;
    }
    
    int sizeInBytes() {
        return 0 + this.mArrayIndices.length * 4 * 3 + 36;
    }
    
    @Override
    public String toString() {
        String string = "";
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            string = string + " -> " + this.mArrayValues[mHead] + " : " + this.mCache.mIndexedVariables[this.mArrayIndices[mHead]];
        }
        return string;
    }
    
    void updateClientEquations(final ArrayRow arrayRow) {
        for (int mHead = this.mHead, n = 0; mHead != -1 && n < this.currentSize; mHead = this.mArrayNextIndices[mHead], ++n) {
            this.mCache.mIndexedVariables[this.mArrayIndices[mHead]].addClientEquation(arrayRow);
        }
    }
    
    void updateFromRow(final ArrayRow arrayRow, final ArrayRow arrayRow2) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            if (this.mArrayIndices[n] == arrayRow2.variable.id) {
                final float n3 = this.mArrayValues[n];
                this.remove(arrayRow2.variable);
                final ArrayLinkedVariables variables = arrayRow2.variables;
                for (int mHead = variables.mHead, n4 = 0; mHead != -1 && n4 < variables.currentSize; mHead = variables.mArrayNextIndices[mHead], ++n4) {
                    this.add(this.mCache.mIndexedVariables[variables.mArrayIndices[mHead]], variables.mArrayValues[mHead] * n3);
                }
                arrayRow.constantValue += arrayRow2.constantValue * n3;
                arrayRow2.variable.removeClientEquation(arrayRow);
                n = this.mHead;
                n2 = 0;
            }
            else {
                n = this.mArrayNextIndices[n];
                ++n2;
            }
        }
    }
    
    void updateFromSystem(final ArrayRow arrayRow, final ArrayRow[] array) {
        int n = this.mHead;
        int n2 = 0;
        while (n != -1 && n2 < this.currentSize) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[n]];
            if (solverVariable.definitionId != -1) {
                final float n3 = this.mArrayValues[n];
                this.remove(solverVariable);
                final ArrayRow arrayRow2 = array[solverVariable.definitionId];
                if (!arrayRow2.isSimpleDefinition) {
                    final ArrayLinkedVariables variables = arrayRow2.variables;
                    for (int mHead = variables.mHead, n4 = 0; mHead != -1 && n4 < variables.currentSize; mHead = variables.mArrayNextIndices[mHead], ++n4) {
                        this.add(this.mCache.mIndexedVariables[variables.mArrayIndices[mHead]], variables.mArrayValues[mHead] * n3);
                    }
                }
                arrayRow.constantValue += arrayRow2.constantValue * n3;
                arrayRow2.variable.removeClientEquation(arrayRow);
                n = this.mHead;
                n2 = 0;
            }
            else {
                n = this.mArrayNextIndices[n];
                ++n2;
            }
        }
    }
}
