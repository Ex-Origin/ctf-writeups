// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem
{
    private static final boolean DEBUG = false;
    private static int POOL_SIZE;
    private int TABLE_SIZE;
    private boolean[] mAlreadyTestedCandidates;
    final Cache mCache;
    private Goal mGoal;
    private int mMaxColumns;
    private int mMaxRows;
    int mNumColumns;
    private int mNumRows;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    private ArrayRow[] mRows;
    private HashMap<String, SolverVariable> mVariables;
    int mVariablesID;
    private ArrayRow[] tempClientsCopy;
    
    static {
        LinearSystem.POOL_SIZE = 1000;
    }
    
    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.mGoal = new Goal();
        this.TABLE_SIZE = 32;
        this.mMaxColumns = this.TABLE_SIZE;
        this.mRows = null;
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = this.TABLE_SIZE;
        this.mPoolVariables = new SolverVariable[LinearSystem.POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new ArrayRow[this.TABLE_SIZE];
        this.mRows = new ArrayRow[this.TABLE_SIZE];
        this.releaseRows();
        this.mCache = new Cache();
    }
    
    private SolverVariable acquireSolverVariable(final SolverVariable.Type type) {
        final SolverVariable solverVariable = this.mCache.solverVariablePool.acquire();
        SolverVariable solverVariable2;
        if (solverVariable == null) {
            solverVariable2 = new SolverVariable(type);
        }
        else {
            solverVariable.reset();
            solverVariable.setType(type);
            solverVariable2 = solverVariable;
        }
        if (this.mPoolVariablesCount >= LinearSystem.POOL_SIZE) {
            LinearSystem.POOL_SIZE *= 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, LinearSystem.POOL_SIZE);
        }
        return this.mPoolVariables[this.mPoolVariablesCount++] = solverVariable2;
    }
    
    private void addError(final ArrayRow arrayRow) {
        arrayRow.addError(this.createErrorVariable(), this.createErrorVariable());
    }
    
    private void addSingleError(final ArrayRow arrayRow, final int n) {
        arrayRow.addSingleError(this.createErrorVariable(), n);
    }
    
    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }
    
    public static ArrayRow createRowCentering(final LinearSystem linearSystem, SolverVariable errorVariable, final SolverVariable solverVariable, final int n, final float n2, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final int n3, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowCentering(errorVariable, solverVariable, n, n2, solverVariable2, solverVariable3, n3);
        if (b) {
            errorVariable = linearSystem.createErrorVariable();
            final SolverVariable errorVariable2 = linearSystem.createErrorVariable();
            errorVariable.strength = 4;
            errorVariable2.strength = 4;
            row.addError(errorVariable, errorVariable2);
        }
        return row;
    }
    
    public static ArrayRow createRowDimensionPercent(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final SolverVariable solverVariable3, final float n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        if (b) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(solverVariable, solverVariable2, solverVariable3, n);
    }
    
    public static ArrayRow createRowEquals(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final ArrayRow row = linearSystem.createRow();
        row.createRowEquals(solverVariable, solverVariable2, n);
        if (b) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }
    
    public static ArrayRow createRowGreaterThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
        }
        return row;
    }
    
    public static ArrayRow createRowLowerThan(final LinearSystem linearSystem, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final boolean b) {
        final SolverVariable slackVariable = linearSystem.createSlackVariable();
        final ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        if (b) {
            linearSystem.addSingleError(row, (int)(-1.0f * row.variables.get(slackVariable)));
        }
        return row;
    }
    
    private SolverVariable createVariable(final String name, final SolverVariable.Type type) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(type);
        acquireSolverVariable.setName(name);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        this.mVariables.put(name, acquireSolverVariable);
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    private void displayRows() {
        this.displaySolverVariables();
        String string = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            string = string + this.mRows[i] + "\n";
        }
        String string2 = string;
        if (this.mGoal.variables.size() != 0) {
            string2 = string + this.mGoal + "\n";
        }
        System.out.println(string2);
    }
    
    private void displaySolverVariables() {
        String s = "Display Rows (" + this.mNumRows + "x" + this.mNumColumns + ") :\n\t | C | ";
        for (int i = 1; i <= this.mNumColumns; ++i) {
            s = s + this.mCache.mIndexedVariables[i] + " | ";
        }
        System.out.println(s + "\n");
    }
    
    private int enforceBFS(final Goal goal) throws Exception {
        final boolean b = false;
        final boolean b2 = false;
        int n = 0;
        boolean b3;
        while (true) {
            b3 = b2;
            if (n >= this.mNumRows) {
                break;
            }
            if (this.mRows[n].variable.mType != SolverVariable.Type.UNRESTRICTED && this.mRows[n].constantValue < 0.0f) {
                b3 = true;
                break;
            }
            ++n;
        }
        int n2 = b ? 1 : 0;
        if (b3) {
            int n3 = 0;
            int n4 = 0;
            while (true) {
                n2 = n4;
                if (n3 != 0) {
                    break;
                }
                final int n5 = n4 + 1;
                float n6 = Float.MAX_VALUE;
                int n7 = 0;
                int definitionId = -1;
                int n8 = -1;
                int n9;
                int n10;
                int n11;
                float n12;
                for (int i = 0; i < this.mNumRows; ++i, n6 = n12, n8 = n11, definitionId = n10, n7 = n9) {
                    final ArrayRow arrayRow = this.mRows[i];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        n9 = n7;
                        n10 = definitionId;
                        n11 = n8;
                        n12 = n6;
                    }
                    else {
                        n12 = n6;
                        n11 = n8;
                        n10 = definitionId;
                        n9 = n7;
                        if (arrayRow.constantValue < 0.0f) {
                            int n13 = 1;
                            while (true) {
                                n12 = n6;
                                n11 = n8;
                                n10 = definitionId;
                                n9 = n7;
                                if (n13 >= this.mNumColumns) {
                                    break;
                                }
                                final SolverVariable solverVariable = this.mCache.mIndexedVariables[n13];
                                final float value = arrayRow.variables.get(solverVariable);
                                int n14;
                                int n15;
                                int n16;
                                float n17;
                                if (value <= 0.0f) {
                                    n14 = n7;
                                    n15 = definitionId;
                                    n16 = n8;
                                    n17 = n6;
                                }
                                else {
                                    final int n18 = 0;
                                    int n19 = n8;
                                    int n20 = n18;
                                    while (true) {
                                        n17 = n6;
                                        n16 = n19;
                                        n15 = definitionId;
                                        n14 = n7;
                                        if (n20 >= 6) {
                                            break;
                                        }
                                        final float n21 = solverVariable.strengthVector[n20] / value;
                                        int n22;
                                        if ((n21 < n6 && n20 == n7) || n20 > (n22 = n7)) {
                                            n6 = n21;
                                            definitionId = i;
                                            n19 = n13;
                                            n22 = n20;
                                        }
                                        ++n20;
                                        n7 = n22;
                                    }
                                }
                                ++n13;
                                n6 = n17;
                                n8 = n16;
                                definitionId = n15;
                                n7 = n14;
                            }
                        }
                    }
                }
                if (definitionId != -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(this.mCache.mIndexedVariables[n8]);
                    arrayRow2.variable.definitionId = definitionId;
                    for (int j = 0; j < this.mNumRows; ++j) {
                        this.mRows[j].updateRowWithEquation(arrayRow2);
                    }
                    goal.updateFromSystem(this);
                    n4 = n5;
                }
                else {
                    n3 = 1;
                    n4 = n5;
                }
            }
        }
        for (int n23 = 0; n23 < this.mNumRows && (this.mRows[n23].variable.mType == SolverVariable.Type.UNRESTRICTED || this.mRows[n23].constantValue >= 0.0f); ++n23) {}
        return n2;
    }
    
    private String getDisplaySize(final int n) {
        final int n2 = n * 4 / 1024 / 1024;
        if (n2 > 0) {
            return "" + n2 + " Mb";
        }
        final int n3 = n * 4 / 1024;
        if (n3 > 0) {
            return "" + n3 + " Kb";
        }
        return "" + n * 4 + " bytes";
    }
    
    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        this.mCache.mIndexedVariables = Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mMaxColumns = this.TABLE_SIZE;
        this.mMaxRows = this.TABLE_SIZE;
        this.mGoal.variables.clear();
    }
    
    private int optimize(final Goal goal) {
        final boolean b = false;
        int n = 0;
        for (int i = 0; i < this.mNumColumns; ++i) {
            this.mAlreadyTestedCandidates[i] = false;
        }
        int n2 = 0;
        int j = b ? 1 : 0;
        while (j == 0) {
            final int n3 = n + 1;
            final SolverVariable pivotCandidate = goal.getPivotCandidate();
            int n4 = j;
            SolverVariable solverVariable = pivotCandidate;
            n = n2;
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    solverVariable = null;
                    n = n2;
                    n4 = j;
                }
                else {
                    this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
                    final int n5 = n2 + 1;
                    n4 = j;
                    solverVariable = pivotCandidate;
                    if ((n = n5) >= this.mNumColumns) {
                        n4 = 1;
                        solverVariable = pivotCandidate;
                        n = n5;
                    }
                }
            }
            if (solverVariable != null) {
                float n6 = Float.MAX_VALUE;
                int definitionId = -1;
                int n7;
                float n8;
                for (int k = 0; k < this.mNumRows; ++k, n6 = n8, definitionId = n7) {
                    final ArrayRow arrayRow = this.mRows[k];
                    if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
                        n7 = definitionId;
                        n8 = n6;
                    }
                    else {
                        n8 = n6;
                        n7 = definitionId;
                        if (arrayRow.hasVariable(solverVariable)) {
                            final float value = arrayRow.variables.get(solverVariable);
                            n8 = n6;
                            n7 = definitionId;
                            if (value < 0.0f) {
                                final float n9 = -arrayRow.constantValue / value;
                                n8 = n6;
                                n7 = definitionId;
                                if (n9 < n6) {
                                    n8 = n9;
                                    n7 = k;
                                }
                            }
                        }
                    }
                }
                if (definitionId > -1) {
                    final ArrayRow arrayRow2 = this.mRows[definitionId];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(solverVariable);
                    arrayRow2.variable.definitionId = definitionId;
                    for (int l = 0; l < this.mNumRows; ++l) {
                        this.mRows[l].updateRowWithEquation(arrayRow2);
                    }
                    goal.updateFromSystem(this);
                    try {
                        this.enforceBFS(goal);
                        j = n4;
                        n2 = n;
                        n = n3;
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                        j = n4;
                        n2 = n;
                        n = n3;
                    }
                }
                else {
                    j = 1;
                    n2 = n;
                    n = n3;
                }
            }
            else {
                j = 1;
                n2 = n;
                n = n3;
            }
        }
        return n;
    }
    
    private void releaseRows() {
        for (int i = 0; i < this.mRows.length; ++i) {
            final ArrayRow arrayRow = this.mRows[i];
            if (arrayRow != null) {
                this.mCache.arrayRowPool.release(arrayRow);
            }
            this.mRows[i] = null;
        }
    }
    
    private void updateRowFromVariables(final ArrayRow arrayRow) {
        if (this.mNumRows > 0) {
            arrayRow.variables.updateFromSystem(arrayRow, this.mRows);
            if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
            }
        }
    }
    
    public void addCentering(SolverVariable errorVariable, SolverVariable errorVariable2, final int n, final float n2, final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n3, final int n4) {
        final ArrayRow row = this.createRow();
        row.createRowCentering(errorVariable, errorVariable2, n, n2, solverVariable, solverVariable2, n3);
        errorVariable = this.createErrorVariable();
        errorVariable2 = this.createErrorVariable();
        errorVariable.strength = n4;
        errorVariable2.strength = n4;
        row.addError(errorVariable, errorVariable2);
        this.addConstraint(row);
    }
    
    public void addConstraint(final ArrayRow arrayRow) {
        if (arrayRow != null) {
            if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
                this.increaseTableSize();
            }
            if (!arrayRow.isSimpleDefinition) {
                this.updateRowFromVariables(arrayRow);
                arrayRow.ensurePositiveConstant();
                arrayRow.pickRowVariable();
                if (!arrayRow.hasKeyVariable()) {
                    return;
                }
            }
            if (this.mRows[this.mNumRows] != null) {
                this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
            }
            if (!arrayRow.isSimpleDefinition) {
                arrayRow.updateClientEquations();
            }
            this.mRows[this.mNumRows] = arrayRow;
            arrayRow.variable.definitionId = this.mNumRows;
            ++this.mNumRows;
            final int mClientEquationsCount = arrayRow.variable.mClientEquationsCount;
            if (mClientEquationsCount > 0) {
                while (this.tempClientsCopy.length < mClientEquationsCount) {
                    this.tempClientsCopy = new ArrayRow[this.tempClientsCopy.length * 2];
                }
                final ArrayRow[] tempClientsCopy = this.tempClientsCopy;
                for (int i = 0; i < mClientEquationsCount; ++i) {
                    tempClientsCopy[i] = arrayRow.variable.mClientEquations[i];
                }
                for (int j = 0; j < mClientEquationsCount; ++j) {
                    final ArrayRow arrayRow2 = tempClientsCopy[j];
                    if (arrayRow2 != arrayRow) {
                        arrayRow2.variables.updateFromRow(arrayRow2, arrayRow);
                        arrayRow2.updateClientEquations();
                    }
                }
            }
        }
    }
    
    public ArrayRow addEquality(SolverVariable errorVariable, SolverVariable errorVariable2, final int n, final int n2) {
        final ArrayRow row = this.createRow();
        row.createRowEquals(errorVariable, errorVariable2, n);
        errorVariable = this.createErrorVariable();
        errorVariable2 = this.createErrorVariable();
        errorVariable.strength = n2;
        errorVariable2.strength = n2;
        row.addError(errorVariable, errorVariable2);
        this.addConstraint(row);
        return row;
    }
    
    public void addEquality(final SolverVariable solverVariable, final int n) {
        final int definitionId = solverVariable.definitionId;
        if (solverVariable.definitionId == -1) {
            final ArrayRow row = this.createRow();
            row.createRowDefinition(solverVariable, n);
            this.addConstraint(row);
            return;
        }
        final ArrayRow arrayRow = this.mRows[definitionId];
        if (arrayRow.isSimpleDefinition) {
            arrayRow.constantValue = n;
            return;
        }
        final ArrayRow row2 = this.createRow();
        row2.createRowEquals(solverVariable, n);
        this.addConstraint(row2);
    }
    
    public void addGreaterThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int strength) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = strength;
        row.createRowGreaterThan(solverVariable, solverVariable2, slackVariable, n);
        this.addConstraint(row);
    }
    
    public void addLowerThan(final SolverVariable solverVariable, final SolverVariable solverVariable2, final int n, final int strength) {
        final ArrayRow row = this.createRow();
        final SolverVariable slackVariable = this.createSlackVariable();
        slackVariable.strength = strength;
        row.createRowLowerThan(solverVariable, solverVariable2, slackVariable, n);
        this.addConstraint(row);
    }
    
    public SolverVariable createErrorVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.ERROR);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    public SolverVariable createObjectVariable(final Object o) {
        SolverVariable solverVariable;
        if (o == null) {
            solverVariable = null;
        }
        else {
            if (this.mNumColumns + 1 >= this.mMaxColumns) {
                this.increaseTableSize();
            }
            solverVariable = null;
            if (o instanceof ConstraintAnchor) {
                SolverVariable solverVariable2;
                if ((solverVariable2 = ((ConstraintAnchor)o).getSolverVariable()) == null) {
                    ((ConstraintAnchor)o).resetSolverVariable(this.mCache);
                    solverVariable2 = ((ConstraintAnchor)o).getSolverVariable();
                }
                if (solverVariable2.id != -1 && solverVariable2.id <= this.mVariablesID) {
                    solverVariable = solverVariable2;
                    if (this.mCache.mIndexedVariables[solverVariable2.id] != null) {
                        return solverVariable;
                    }
                }
                if (solverVariable2.id != -1) {
                    solverVariable2.reset();
                }
                ++this.mVariablesID;
                ++this.mNumColumns;
                solverVariable2.id = this.mVariablesID;
                solverVariable2.mType = SolverVariable.Type.UNRESTRICTED;
                return this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable2;
            }
        }
        return solverVariable;
    }
    
    public ArrayRow createRow() {
        final ArrayRow arrayRow = this.mCache.arrayRowPool.acquire();
        if (arrayRow == null) {
            return new ArrayRow(this.mCache);
        }
        arrayRow.reset();
        return arrayRow;
    }
    
    public SolverVariable createSlackVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable acquireSolverVariable = this.acquireSolverVariable(SolverVariable.Type.SLACK);
        ++this.mVariablesID;
        ++this.mNumColumns;
        acquireSolverVariable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = acquireSolverVariable;
    }
    
    void displayReadableRows() {
        this.displaySolverVariables();
        String string = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            string = string + this.mRows[i].toReadableString() + "\n";
        }
        String string2 = string;
        if (this.mGoal != null) {
            string2 = string + this.mGoal + "\n";
        }
        System.out.println(string2);
    }
    
    void displaySystemInformations() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.TABLE_SIZE; ++i, n = n2) {
            n2 = n;
            if (this.mRows[i] != null) {
                n2 = n + this.mRows[i].sizeInBytes();
            }
        }
        int n3 = 0;
        int n4;
        for (int j = 0; j < this.mNumRows; ++j, n3 = n4) {
            n4 = n3;
            if (this.mRows[j] != null) {
                n4 = n3 + this.mRows[j].sizeInBytes();
            }
        }
        System.out.println("Linear System -> Table size: " + this.TABLE_SIZE + " (" + this.getDisplaySize(this.TABLE_SIZE * this.TABLE_SIZE) + ") -- row sizes: " + this.getDisplaySize(n) + ", actual size: " + this.getDisplaySize(n3) + " rows: " + this.mNumRows + "/" + this.mMaxRows + " cols: " + this.mNumColumns + "/" + this.mMaxColumns + " " + 0 + " occupied cells, " + this.getDisplaySize(0));
    }
    
    public void displayVariablesReadableRows() {
        this.displaySolverVariables();
        String s = "";
        String string;
        for (int i = 0; i < this.mNumRows; ++i, s = string) {
            string = s;
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                string = s + this.mRows[i].toReadableString() + "\n";
            }
        }
        String string2 = s;
        if (this.mGoal.variables.size() != 0) {
            string2 = s + this.mGoal + "\n";
        }
        System.out.println(string2);
    }
    
    public Cache getCache() {
        return this.mCache;
    }
    
    Goal getGoal() {
        return this.mGoal;
    }
    
    public int getMemoryUsed() {
        int n = 0;
        int n2;
        for (int i = 0; i < this.mNumRows; ++i, n = n2) {
            n2 = n;
            if (this.mRows[i] != null) {
                n2 = n + this.mRows[i].sizeInBytes();
            }
        }
        return n;
    }
    
    public int getNumEquations() {
        return this.mNumRows;
    }
    
    public int getNumVariables() {
        return this.mVariablesID;
    }
    
    public int getObjectVariableValue(final Object o) {
        final SolverVariable solverVariable = ((ConstraintAnchor)o).getSolverVariable();
        if (solverVariable != null) {
            return (int)(solverVariable.computedValue + 0.5f);
        }
        return 0;
    }
    
    ArrayRow getRow(final int n) {
        return this.mRows[n];
    }
    
    float getValueFor(final String s) {
        final SolverVariable variable = this.getVariable(s, SolverVariable.Type.UNRESTRICTED);
        if (variable == null) {
            return 0.0f;
        }
        return variable.computedValue;
    }
    
    SolverVariable getVariable(final String s, final SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        SolverVariable variable;
        if ((variable = this.mVariables.get(s)) == null) {
            variable = this.createVariable(s, type);
        }
        return variable;
    }
    
    public void minimize() throws Exception {
        this.minimizeGoal(this.mGoal);
    }
    
    void minimizeGoal(final Goal goal) throws Exception {
        goal.updateFromSystem(this);
        this.enforceBFS(goal);
        this.optimize(goal);
        this.computeValues();
    }
    
    void rebuildGoalFromErrors() {
        this.mGoal.updateFromSystem(this);
    }
    
    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; ++i) {
            final SolverVariable solverVariable = this.mCache.mIndexedVariables[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        if (this.mVariables != null) {
            this.mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.variables.clear();
        this.mNumColumns = 1;
        for (int j = 0; j < this.mNumRows; ++j) {
            this.mRows[j].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
}
