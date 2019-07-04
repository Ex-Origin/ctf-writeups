// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver;

public class Cache
{
    Pools.Pool<ArrayRow> arrayRowPool;
    SolverVariable[] mIndexedVariables;
    Pools.Pool<SolverVariable> solverVariablePool;
    
    public Cache() {
        this.arrayRowPool = new Pools.SimplePool<ArrayRow>(256);
        this.solverVariablePool = new Pools.SimplePool<SolverVariable>(256);
        this.mIndexedVariables = new SolverVariable[32];
    }
}
