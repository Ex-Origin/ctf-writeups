// 
// Decompiled by Procyon v0.5.30
// 

package edu.fudan.debugme;

import android.os.Build;
import android.content.Context;

public class b
{
    public static final String[] a;
    
    static {
        a = new String[] { "userdebug", "user-debug", "root", "virtualbox", "genymotion", "emulator" };
    }
    
    public static boolean b(final Context p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //     4: astore_2       
        //     5: new             Ljava/util/LinkedList;
        //     8: dup            
        //     9: invokespecial   java/util/LinkedList.<init>:()V
        //    12: astore_3       
        //    13: new             Ljava/util/ArrayList;
        //    16: dup            
        //    17: invokespecial   java/util/ArrayList.<init>:()V
        //    20: astore_0       
        //    21: aload_2        
        //    22: sipush          128
        //    25: invokevirtual   android/content/pm/PackageManager.getInstalledPackages:(I)Ljava/util/List;
        //    28: astore_2       
        //    29: aload_2        
        //    30: ifnonnull       35
        //    33: iconst_0       
        //    34: ireturn        
        //    35: aload_2        
        //    36: astore_0       
        //    37: aload_3        
        //    38: ldc             "xposed"
        //    40: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    45: pop            
        //    46: aload_2        
        //    47: astore_0       
        //    48: aload_3        
        //    49: ldc             "supersu"
        //    51: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    56: pop            
        //    57: aload_2        
        //    58: astore_0       
        //    59: aload_3        
        //    60: ldc             "genymotion.super"
        //    62: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    67: pop            
        //    68: aload_2        
        //    69: astore_0       
        //    70: aload_3        
        //    71: ldc             "superuser"
        //    73: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    78: pop            
        //    79: aload_2        
        //    80: astore_0       
        //    81: aload_0        
        //    82: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    87: astore_0       
        //    88: aload_0        
        //    89: invokeinterface java/util/Iterator.hasNext:()Z
        //    94: ifeq            166
        //    97: aload_0        
        //    98: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   103: checkcast       Landroid/content/pm/PackageInfo;
        //   106: astore_2       
        //   107: aload_3        
        //   108: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //   113: astore          4
        //   115: aload           4
        //   117: invokeinterface java/util/Iterator.hasNext:()Z
        //   122: ifeq            88
        //   125: aload           4
        //   127: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   132: checkcast       Ljava/lang/String;
        //   135: astore          5
        //   137: aload_2        
        //   138: getfield        android/content/pm/PackageInfo.packageName:Ljava/lang/String;
        //   141: aload           5
        //   143: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //   146: istore_1       
        //   147: iload_1        
        //   148: ifeq            115
        //   151: iconst_1       
        //   152: ireturn        
        //   153: astore_2       
        //   154: aload_2        
        //   155: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   158: goto            81
        //   161: astore_0       
        //   162: aload_0        
        //   163: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   166: iconst_0       
        //   167: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  21     29     153    161    Ljava/lang/Exception;
        //  37     46     153    161    Ljava/lang/Exception;
        //  48     57     153    161    Ljava/lang/Exception;
        //  59     68     153    161    Ljava/lang/Exception;
        //  70     79     153    161    Ljava/lang/Exception;
        //  81     88     161    166    Ljava/lang/Exception;
        //  88     115    161    166    Ljava/lang/Exception;
        //  115    147    161    166    Ljava/lang/Exception;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0081:
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
    
    public static boolean c() {
        try {
            throw new Exception("");
        }
        catch (Exception ex) {
            final StackTraceElement[] stackTrace = ex.getStackTrace();
            for (int length = stackTrace.length, i = 0; i < length; ++i) {
                if (stackTrace[i].getClassName().equals("de.robv.android.xposed.XposedBridge")) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public static boolean d() {
        final boolean b = false;
        final StringBuilder sb = new StringBuilder();
        sb.append(Build.MANUFACTURER).append(' ');
        sb.append(Build.MODEL).append(' ');
        sb.append(Build.FINGERPRINT).append(' ');
        sb.append(Build.BOARD).append(' ');
        sb.append(Build.BRAND).append(' ');
        sb.append(Build.DEVICE);
        final String lowerCase = sb.toString().toLowerCase();
        final String[] a = edu.fudan.debugme.b.a;
        final int length = a.length;
        int n = 0;
        boolean b2;
        while (true) {
            b2 = b;
            if (n >= length) {
                break;
            }
            if (lowerCase.contains(a[n])) {
                b2 = true;
                break;
            }
            ++n;
        }
        return b2;
    }
}
