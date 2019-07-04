// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.app;

import android.util.LongSparseArray;
import android.util.Log;
import android.support.annotation.RequiresApi;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.content.res.Resources;
import java.lang.reflect.Field;

class ResourcesFlusher
{
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;
    
    static boolean flush(@NonNull final Resources resources) {
        if (Build$VERSION.SDK_INT >= 24) {
            return flushNougats(resources);
        }
        if (Build$VERSION.SDK_INT >= 23) {
            return flushMarshmallows(resources);
        }
        return Build$VERSION.SDK_INT >= 21 && flushLollipops(resources);
    }
    
    @RequiresApi(21)
    private static boolean flushLollipops(@NonNull final Resources p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched:Z
        //     3: ifne            27
        //     6: ldc             Landroid/content/res/Resources;.class
        //     8: ldc             "mDrawableCache"
        //    10: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    13: putstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    16: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    19: iconst_1       
        //    20: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    23: iconst_1       
        //    24: putstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched:Z
        //    27: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    30: ifnull          86
        //    33: aconst_null    
        //    34: astore_1       
        //    35: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    38: aload_0        
        //    39: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    42: checkcast       Ljava/util/Map;
        //    45: astore_0       
        //    46: aload_0        
        //    47: ifnull          86
        //    50: aload_0        
        //    51: invokeinterface java/util/Map.clear:()V
        //    56: iconst_1       
        //    57: ireturn        
        //    58: astore_1       
        //    59: ldc             "ResourcesFlusher"
        //    61: ldc             "Could not retrieve Resources#mDrawableCache field"
        //    63: aload_1        
        //    64: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    67: pop            
        //    68: goto            23
        //    71: astore_0       
        //    72: ldc             "ResourcesFlusher"
        //    74: ldc             "Could not retrieve value from Resources#mDrawableCache"
        //    76: aload_0        
        //    77: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    80: pop            
        //    81: aload_1        
        //    82: astore_0       
        //    83: goto            46
        //    86: iconst_0       
        //    87: ireturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  6      23     58     71     Ljava/lang/NoSuchFieldException;
        //  35     46     71     86     Ljava/lang/IllegalAccessException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0046:
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
    
    @RequiresApi(23)
    private static boolean flushMarshmallows(@NonNull final Resources p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_1       
        //     1: istore_1       
        //     2: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched:Z
        //     5: ifne            29
        //     8: ldc             Landroid/content/res/Resources;.class
        //    10: ldc             "mDrawableCache"
        //    12: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    15: putstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    18: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    21: iconst_1       
        //    22: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    25: iconst_1       
        //    26: putstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched:Z
        //    29: aconst_null    
        //    30: astore_3       
        //    31: aload_3        
        //    32: astore_2       
        //    33: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    36: ifnull          47
        //    39: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    42: aload_0        
        //    43: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    46: astore_2       
        //    47: aload_2        
        //    48: ifnonnull       81
        //    51: iconst_0       
        //    52: ireturn        
        //    53: astore_2       
        //    54: ldc             "ResourcesFlusher"
        //    56: ldc             "Could not retrieve Resources#mDrawableCache field"
        //    58: aload_2        
        //    59: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    62: pop            
        //    63: goto            25
        //    66: astore_0       
        //    67: ldc             "ResourcesFlusher"
        //    69: ldc             "Could not retrieve value from Resources#mDrawableCache"
        //    71: aload_0        
        //    72: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    75: pop            
        //    76: aload_3        
        //    77: astore_2       
        //    78: goto            47
        //    81: aload_2        
        //    82: ifnull          94
        //    85: aload_2        
        //    86: invokestatic    android/support/v7/app/ResourcesFlusher.flushThemedResourcesCache:(Ljava/lang/Object;)Z
        //    89: ifeq            94
        //    92: iload_1        
        //    93: ireturn        
        //    94: iconst_0       
        //    95: istore_1       
        //    96: goto            92
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  8      25     53     66     Ljava/lang/NoSuchFieldException;
        //  39     47     66     81     Ljava/lang/IllegalAccessException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0047:
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
    
    @RequiresApi(24)
    private static boolean flushNougats(@NonNull final Resources p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_1       
        //     1: istore_1       
        //     2: getstatic       android/support/v7/app/ResourcesFlusher.sResourcesImplFieldFetched:Z
        //     5: ifne            29
        //     8: ldc             Landroid/content/res/Resources;.class
        //    10: ldc             "mResourcesImpl"
        //    12: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    15: putstatic       android/support/v7/app/ResourcesFlusher.sResourcesImplField:Ljava/lang/reflect/Field;
        //    18: getstatic       android/support/v7/app/ResourcesFlusher.sResourcesImplField:Ljava/lang/reflect/Field;
        //    21: iconst_1       
        //    22: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    25: iconst_1       
        //    26: putstatic       android/support/v7/app/ResourcesFlusher.sResourcesImplFieldFetched:Z
        //    29: getstatic       android/support/v7/app/ResourcesFlusher.sResourcesImplField:Ljava/lang/reflect/Field;
        //    32: ifnonnull       50
        //    35: iconst_0       
        //    36: ireturn        
        //    37: astore_2       
        //    38: ldc             "ResourcesFlusher"
        //    40: ldc             "Could not retrieve Resources#mResourcesImpl field"
        //    42: aload_2        
        //    43: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //    46: pop            
        //    47: goto            25
        //    50: aconst_null    
        //    51: astore_2       
        //    52: getstatic       android/support/v7/app/ResourcesFlusher.sResourcesImplField:Ljava/lang/reflect/Field;
        //    55: aload_0        
        //    56: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    59: astore_0       
        //    60: aload_0        
        //    61: ifnull          35
        //    64: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched:Z
        //    67: ifne            93
        //    70: aload_0        
        //    71: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    74: ldc             "mDrawableCache"
        //    76: invokevirtual   java/lang/Class.getDeclaredField:(Ljava/lang/String;)Ljava/lang/reflect/Field;
        //    79: putstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    82: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //    85: iconst_1       
        //    86: invokevirtual   java/lang/reflect/Field.setAccessible:(Z)V
        //    89: iconst_1       
        //    90: putstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheFieldFetched:Z
        //    93: aconst_null    
        //    94: astore_3       
        //    95: aload_3        
        //    96: astore_2       
        //    97: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //   100: ifnull          111
        //   103: getstatic       android/support/v7/app/ResourcesFlusher.sDrawableCacheField:Ljava/lang/reflect/Field;
        //   106: aload_0        
        //   107: invokevirtual   java/lang/reflect/Field.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   110: astore_2       
        //   111: aload_2        
        //   112: ifnull          167
        //   115: aload_2        
        //   116: invokestatic    android/support/v7/app/ResourcesFlusher.flushThemedResourcesCache:(Ljava/lang/Object;)Z
        //   119: ifeq            167
        //   122: iload_1        
        //   123: ireturn        
        //   124: astore_0       
        //   125: ldc             "ResourcesFlusher"
        //   127: ldc             "Could not retrieve value from Resources#mResourcesImpl"
        //   129: aload_0        
        //   130: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   133: pop            
        //   134: aload_2        
        //   135: astore_0       
        //   136: goto            60
        //   139: astore_2       
        //   140: ldc             "ResourcesFlusher"
        //   142: ldc             "Could not retrieve ResourcesImpl#mDrawableCache field"
        //   144: aload_2        
        //   145: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   148: pop            
        //   149: goto            89
        //   152: astore_0       
        //   153: ldc             "ResourcesFlusher"
        //   155: ldc             "Could not retrieve value from ResourcesImpl#mDrawableCache"
        //   157: aload_0        
        //   158: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   161: pop            
        //   162: aload_3        
        //   163: astore_2       
        //   164: goto            111
        //   167: iconst_0       
        //   168: istore_1       
        //   169: goto            122
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  8      25     37     50     Ljava/lang/NoSuchFieldException;
        //  52     60     124    139    Ljava/lang/IllegalAccessException;
        //  70     89     139    152    Ljava/lang/NoSuchFieldException;
        //  103    111    152    167    Ljava/lang/IllegalAccessException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 89, Size: 89
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
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
    
    @RequiresApi(16)
    private static boolean flushThemedResourcesCache(@NonNull final Object o) {
        Label_0018: {
            if (ResourcesFlusher.sThemedResourceCacheClazzFetched) {
                break Label_0018;
            }
            while (true) {
                try {
                    ResourcesFlusher.sThemedResourceCacheClazz = Class.forName("android.content.res.ThemedResourceCache");
                    ResourcesFlusher.sThemedResourceCacheClazzFetched = true;
                    if (ResourcesFlusher.sThemedResourceCacheClazz == null) {
                        return false;
                    }
                }
                catch (ClassNotFoundException ex) {
                    Log.e("ResourcesFlusher", "Could not find ThemedResourceCache class", (Throwable)ex);
                    continue;
                }
                break;
            }
        }
        while (true) {
            if (!ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched) {
                while (true) {
                    try {
                        (ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField = ResourcesFlusher.sThemedResourceCacheClazz.getDeclaredField("mUnthemedEntries")).setAccessible(true);
                        ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched = true;
                        if (ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField == null) {
                            return false;
                        }
                    }
                    catch (NoSuchFieldException ex2) {
                        Log.e("ResourcesFlusher", "Could not retrieve ThemedResourceCache#mUnthemedEntries field", (Throwable)ex2);
                        continue;
                    }
                    break;
                }
                final LongSparseArray longSparseArray = null;
                while (true) {
                    try {
                        final LongSparseArray longSparseArray2 = (LongSparseArray)ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField.get(o);
                        if (longSparseArray2 != null) {
                            longSparseArray2.clear();
                            return true;
                        }
                    }
                    catch (IllegalAccessException ex3) {
                        Log.e("ResourcesFlusher", "Could not retrieve value from ThemedResourceCache#mUnthemedEntries", (Throwable)ex3);
                        final LongSparseArray longSparseArray2 = longSparseArray;
                        continue;
                    }
                    break;
                }
                return false;
            }
            continue;
        }
    }
}
