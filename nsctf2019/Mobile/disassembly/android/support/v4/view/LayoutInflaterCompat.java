// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.LayoutInflater$Factory;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.support.annotation.NonNull;
import android.view.LayoutInflater$Factory2;
import android.view.LayoutInflater;
import android.os.Build$VERSION;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat
{
    static final LayoutInflaterCompatBaseImpl IMPL;
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;
    
    static {
        if (Build$VERSION.SDK_INT >= 21) {
            IMPL = (LayoutInflaterCompatBaseImpl)new LayoutInflaterCompatApi21Impl();
            return;
        }
        IMPL = new LayoutInflaterCompatBaseImpl();
    }
    
    static void forceSetFactory2(final LayoutInflater p0, final LayoutInflater$Factory2 p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: getstatic       android/support/v4/view/LayoutInflaterCompat.sCheckedField:Z
        //     3: ifne            27
        //     6: ldc             Landroid/view/LayoutInflater;.class
        //     8: ldc             "mFactory2"
        //    10: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    13: putstatic       android/support/v4/view/LayoutInflaterCompat.sLayoutInflaterFactory2Field:Ljava/lang/reflect/Field;
        //    16: getstatic       android/support/v4/view/LayoutInflaterCompat.sLayoutInflaterFactory2Field:Ljava/lang/reflect/Field;
        //    19: iconst_1       
        //    20: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    23: iconst_1       
        //    24: putstatic       android/support/v4/view/LayoutInflaterCompat.sCheckedField:Z
        //    27: getstatic       android/support/v4/view/LayoutInflaterCompat.sLayoutInflaterFactory2Field:Ljava/lang/reflect/Field;
        //    30: ifnull          41
        //    33: getstatic       android/support/v4/view/LayoutInflaterCompat.sLayoutInflaterFactory2Field:Ljava/lang/reflect/Field;
        //    36: aload_0        
        //    37: aload_1        
        //    38: invokevirtual   java/lang/reflect/Field.set:(Ljava/lang/Object;Ljava/lang/Object;)V
        //    41: return         
        //    42: astore_2       
        //    43: ldc             "LayoutInflaterCompatHC"
        //    45: new             Ljava/lang/StringBuilder;
        //    48: dup            
        //    49: invokespecial   java/lang/StringBuilder.<init>:()V
        //    52: ldc             "forceSetFactory2 Could not find field 'mFactory2' on class "
        //    54: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    57: ldc             Landroid/view/LayoutInflater;.class
        //    59: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //    62: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    65: ldc             "; inflation may have unexpected results."
        //    67: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    70: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    73: aload_2        
        //    74: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    77: pop            
        //    78: goto            23
        //    81: astore_1       
        //    82: ldc             "LayoutInflaterCompatHC"
        //    84: new             Ljava/lang/StringBuilder;
        //    87: dup            
        //    88: invokespecial   java/lang/StringBuilder.<init>:()V
        //    91: ldc             "forceSetFactory2 could not set the Factory2 on LayoutInflater "
        //    93: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    96: aload_0        
        //    97: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   100: ldc             "; inflation may have unexpected results."
        //   102: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   105: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   108: aload_1        
        //   109: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   112: pop            
        //   113: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  6      23     42     81     Ljava/lang/NoSuchFieldException;
        //  33     41     81     114    Ljava/lang/IllegalAccessException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0041:
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
    
    @Deprecated
    public static LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
        return LayoutInflaterCompat.IMPL.getFactory(layoutInflater);
    }
    
    @Deprecated
    public static void setFactory(@NonNull final LayoutInflater layoutInflater, @NonNull final LayoutInflaterFactory layoutInflaterFactory) {
        LayoutInflaterCompat.IMPL.setFactory(layoutInflater, layoutInflaterFactory);
    }
    
    public static void setFactory2(@NonNull final LayoutInflater layoutInflater, @NonNull final LayoutInflater$Factory2 layoutInflater$Factory2) {
        LayoutInflaterCompat.IMPL.setFactory2(layoutInflater, layoutInflater$Factory2);
    }
    
    static class Factory2Wrapper implements LayoutInflater$Factory2
    {
        final LayoutInflaterFactory mDelegateFactory;
        
        Factory2Wrapper(final LayoutInflaterFactory mDelegateFactory) {
            this.mDelegateFactory = mDelegateFactory;
        }
        
        public View onCreateView(final View view, final String s, final Context context, final AttributeSet set) {
            return this.mDelegateFactory.onCreateView(view, s, context, set);
        }
        
        public View onCreateView(final String s, final Context context, final AttributeSet set) {
            return this.mDelegateFactory.onCreateView(null, s, context, set);
        }
        
        @Override
        public String toString() {
            return this.getClass().getName() + "{" + this.mDelegateFactory + "}";
        }
    }
    
    @RequiresApi(21)
    static class LayoutInflaterCompatApi21Impl extends LayoutInflaterCompatBaseImpl
    {
        @Override
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            Object factory2;
            if (layoutInflaterFactory != null) {
                factory2 = new Factory2Wrapper(layoutInflaterFactory);
            }
            else {
                factory2 = null;
            }
            layoutInflater.setFactory2((LayoutInflater$Factory2)factory2);
        }
        
        @Override
        public void setFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 factory2) {
            layoutInflater.setFactory2(factory2);
        }
    }
    
    static class LayoutInflaterCompatBaseImpl
    {
        public LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
            final LayoutInflater$Factory factory = layoutInflater.getFactory();
            if (factory instanceof Factory2Wrapper) {
                return ((Factory2Wrapper)factory).mDelegateFactory;
            }
            return null;
        }
        
        public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
            Object o;
            if (layoutInflaterFactory != null) {
                o = new Factory2Wrapper(layoutInflaterFactory);
            }
            else {
                o = null;
            }
            this.setFactory2(layoutInflater, (LayoutInflater$Factory2)o);
        }
        
        public void setFactory2(final LayoutInflater layoutInflater, final LayoutInflater$Factory2 factory2) {
            layoutInflater.setFactory2(factory2);
            final LayoutInflater$Factory factory3 = layoutInflater.getFactory();
            if (factory3 instanceof LayoutInflater$Factory2) {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater$Factory2)factory3);
                return;
            }
            LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2);
        }
    }
}
