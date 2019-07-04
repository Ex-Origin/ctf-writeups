// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.os;

import android.os.Build$VERSION;

public final class CancellationSignal
{
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
    
    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                this.wait();
            }
            catch (InterruptedException ex) {}
        }
    }
    
    public void cancel() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: monitorenter   
        //     2: aload_0        
        //     3: getfield        android/support/v4/os/CancellationSignal.mIsCanceled:Z
        //     6: ifeq            12
        //     9: aload_0        
        //    10: monitorexit    
        //    11: return         
        //    12: aload_0        
        //    13: iconst_1       
        //    14: putfield        android/support/v4/os/CancellationSignal.mIsCanceled:Z
        //    17: aload_0        
        //    18: iconst_1       
        //    19: putfield        android/support/v4/os/CancellationSignal.mCancelInProgress:Z
        //    22: aload_0        
        //    23: getfield        android/support/v4/os/CancellationSignal.mOnCancelListener:Landroid/support/v4/os/CancellationSignal$OnCancelListener;
        //    26: astore_1       
        //    27: aload_0        
        //    28: getfield        android/support/v4/os/CancellationSignal.mCancellationSignalObj:Ljava/lang/Object;
        //    31: astore_2       
        //    32: aload_0        
        //    33: monitorexit    
        //    34: aload_1        
        //    35: ifnull          44
        //    38: aload_1        
        //    39: invokeinterface android/support/v4/os/CancellationSignal$OnCancelListener.onCancel:()V
        //    44: aload_2        
        //    45: ifnull          63
        //    48: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    51: bipush          16
        //    53: if_icmplt       63
        //    56: aload_2        
        //    57: checkcast       Landroid/os/CancellationSignal;
        //    60: invokevirtual   android/os/CancellationSignal.cancel:()V
        //    63: aload_0        
        //    64: monitorenter   
        //    65: aload_0        
        //    66: iconst_0       
        //    67: putfield        android/support/v4/os/CancellationSignal.mCancelInProgress:Z
        //    70: aload_0        
        //    71: invokevirtual   java/lang/Object.notifyAll:()V
        //    74: aload_0        
        //    75: monitorexit    
        //    76: return         
        //    77: astore_1       
        //    78: aload_0        
        //    79: monitorexit    
        //    80: aload_1        
        //    81: athrow         
        //    82: astore_1       
        //    83: aload_0        
        //    84: monitorexit    
        //    85: aload_1        
        //    86: athrow         
        //    87: astore_1       
        //    88: aload_0        
        //    89: monitorenter   
        //    90: aload_0        
        //    91: iconst_0       
        //    92: putfield        android/support/v4/os/CancellationSignal.mCancelInProgress:Z
        //    95: aload_0        
        //    96: invokevirtual   java/lang/Object.notifyAll:()V
        //    99: aload_0        
        //   100: monitorexit    
        //   101: aload_1        
        //   102: athrow         
        //   103: astore_1       
        //   104: aload_0        
        //   105: monitorexit    
        //   106: aload_1        
        //   107: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  2      11     82     87     Any
        //  12     34     82     87     Any
        //  38     44     87     108    Any
        //  48     63     87     108    Any
        //  65     76     77     82     Any
        //  78     80     77     82     Any
        //  83     85     82     87     Any
        //  90     101    103    108    Any
        //  104    106    103    108    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0044:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public Object getCancellationSignalObject() {
        if (Build$VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                this.mCancellationSignalObj = new android.os.CancellationSignal();
                if (this.mIsCanceled) {
                    ((android.os.CancellationSignal)this.mCancellationSignalObj).cancel();
                }
            }
            return this.mCancellationSignalObj;
        }
    }
    
    public boolean isCanceled() {
        synchronized (this) {
            return this.mIsCanceled;
        }
    }
    
    public void setOnCancelListener(final OnCancelListener mOnCancelListener) {
        synchronized (this) {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == mOnCancelListener) {
                return;
            }
            this.mOnCancelListener = mOnCancelListener;
            if (!this.mIsCanceled || mOnCancelListener == null) {
                return;
            }
        }
        // monitorexit(this)
        final OnCancelListener onCancelListener;
        onCancelListener.onCancel();
    }
    
    public void throwIfCanceled() {
        if (this.isCanceled()) {
            throw new OperationCanceledException();
        }
    }
    
    public interface OnCancelListener
    {
        void onCancel();
    }
}
