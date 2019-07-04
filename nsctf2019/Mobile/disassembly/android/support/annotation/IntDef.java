// 
// Decompiled by Procyon v0.5.30
// 

package android.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface IntDef {
    boolean flag() default false;
    
    long[] value() default {};
}
