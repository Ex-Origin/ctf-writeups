// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.util.Log;
import java.lang.reflect.Field;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.support.annotation.StyleRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.widget.TextView;
import android.os.Build$VERSION;

public final class TextViewCompat
{
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    static final TextViewCompatBaseImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 26) {
            IMPL = (TextViewCompatBaseImpl)new TextViewCompatApi26Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (TextViewCompatBaseImpl)new TextViewCompatApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            IMPL = (TextViewCompatBaseImpl)new TextViewCompatApi18Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 17) {
            IMPL = (TextViewCompatBaseImpl)new TextViewCompatApi17Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (TextViewCompatBaseImpl)new TextViewCompatApi16Impl();
            return;
        }
        IMPL = new TextViewCompatBaseImpl();
    }
    
    public static int getAutoSizeMaxTextSize(final TextView textView) {
        return TextViewCompat.IMPL.getAutoSizeMaxTextSize(textView);
    }
    
    public static int getAutoSizeMinTextSize(final TextView textView) {
        return TextViewCompat.IMPL.getAutoSizeMinTextSize(textView);
    }
    
    public static int getAutoSizeStepGranularity(final TextView textView) {
        return TextViewCompat.IMPL.getAutoSizeStepGranularity(textView);
    }
    
    public static int[] getAutoSizeTextAvailableSizes(final TextView textView) {
        return TextViewCompat.IMPL.getAutoSizeTextAvailableSizes(textView);
    }
    
    public static int getAutoSizeTextType(final TextView textView) {
        return TextViewCompat.IMPL.getAutoSizeTextType(textView);
    }
    
    public static Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
        return TextViewCompat.IMPL.getCompoundDrawablesRelative(textView);
    }
    
    public static int getMaxLines(@NonNull final TextView textView) {
        return TextViewCompat.IMPL.getMaxLines(textView);
    }
    
    public static int getMinLines(@NonNull final TextView textView) {
        return TextViewCompat.IMPL.getMinLines(textView);
    }
    
    public static void setAutoSizeTextTypeUniformWithConfiguration(final TextView textView, final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        TextViewCompat.IMPL.setAutoSizeTextTypeUniformWithConfiguration(textView, n, n2, n3, n4);
    }
    
    public static void setAutoSizeTextTypeUniformWithPresetSizes(final TextView textView, @NonNull final int[] array, final int n) throws IllegalArgumentException {
        TextViewCompat.IMPL.setAutoSizeTextTypeUniformWithPresetSizes(textView, array, n);
    }
    
    public static void setAutoSizeTextTypeWithDefaults(final TextView textView, final int n) {
        TextViewCompat.IMPL.setAutoSizeTextTypeWithDefaults(textView, n);
    }
    
    public static void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        TextViewCompat.IMPL.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
        TextViewCompat.IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        TextViewCompat.IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable, drawable2, drawable3, drawable4);
    }
    
    public static void setTextAppearance(@NonNull final TextView textView, @StyleRes final int n) {
        TextViewCompat.IMPL.setTextAppearance(textView, n);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface AutoSizeTextType {
    }
    
    @RequiresApi(16)
    static class TextViewCompatApi16Impl extends TextViewCompatBaseImpl
    {
        @Override
        public int getMaxLines(final TextView textView) {
            return textView.getMaxLines();
        }
        
        @Override
        public int getMinLines(final TextView textView) {
            return textView.getMinLines();
        }
    }
    
    @RequiresApi(17)
    static class TextViewCompatApi17Impl extends TextViewCompatApi16Impl
    {
        @Override
        public Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
            int n = 1;
            if (textView.getLayoutDirection() != 1) {
                n = 0;
            }
            final Drawable[] compoundDrawables = textView.getCompoundDrawables();
            if (n != 0) {
                final Drawable drawable = compoundDrawables[2];
                final Drawable drawable2 = compoundDrawables[0];
                compoundDrawables[0] = drawable;
                compoundDrawables[2] = drawable2;
            }
            return compoundDrawables;
        }
        
        @Override
        public void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            boolean b = true;
            if (textView.getLayoutDirection() != 1) {
                b = false;
            }
            Drawable drawable5;
            if (b) {
                drawable5 = drawable3;
            }
            else {
                drawable5 = drawable;
            }
            if (!b) {
                drawable = drawable3;
            }
            textView.setCompoundDrawables(drawable5, drawable2, drawable, drawable4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
            boolean b = true;
            if (textView.getLayoutDirection() != 1) {
                b = false;
            }
            int n5;
            if (b) {
                n5 = n3;
            }
            else {
                n5 = n;
            }
            if (!b) {
                n = n3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            boolean b = true;
            if (textView.getLayoutDirection() != 1) {
                b = false;
            }
            Drawable drawable5;
            if (b) {
                drawable5 = drawable3;
            }
            else {
                drawable5 = drawable;
            }
            if (!b) {
                drawable = drawable3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable5, drawable2, drawable, drawable4);
        }
    }
    
    @RequiresApi(18)
    static class TextViewCompatApi18Impl extends TextViewCompatApi17Impl
    {
        @Override
        public Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
            return textView.getCompoundDrawablesRelative();
        }
        
        @Override
        public void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            textView.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(n, n2, n3, n4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
    }
    
    @RequiresApi(23)
    static class TextViewCompatApi23Impl extends TextViewCompatApi18Impl
    {
        @Override
        public void setTextAppearance(@NonNull final TextView textView, @StyleRes final int textAppearance) {
            textView.setTextAppearance(textAppearance);
        }
    }
    
    @RequiresApi(26)
    static class TextViewCompatApi26Impl extends TextViewCompatApi23Impl
    {
        @Override
        public int getAutoSizeMaxTextSize(final TextView textView) {
            return textView.getAutoSizeMaxTextSize();
        }
        
        @Override
        public int getAutoSizeMinTextSize(final TextView textView) {
            return textView.getAutoSizeMinTextSize();
        }
        
        @Override
        public int getAutoSizeStepGranularity(final TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }
        
        @Override
        public int[] getAutoSizeTextAvailableSizes(final TextView textView) {
            return textView.getAutoSizeTextAvailableSizes();
        }
        
        @Override
        public int getAutoSizeTextType(final TextView textView) {
            return textView.getAutoSizeTextType();
        }
        
        @Override
        public void setAutoSizeTextTypeUniformWithConfiguration(final TextView textView, final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
            textView.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
        
        @Override
        public void setAutoSizeTextTypeUniformWithPresetSizes(final TextView textView, @NonNull final int[] array, final int n) throws IllegalArgumentException {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
        
        @Override
        public void setAutoSizeTextTypeWithDefaults(final TextView textView, final int autoSizeTextTypeWithDefaults) {
            textView.setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
        }
    }
    
    static class TextViewCompatBaseImpl
    {
        private static final int LINES = 1;
        private static final String LOG_TAG = "TextViewCompatBase";
        private static Field sMaxModeField;
        private static boolean sMaxModeFieldFetched;
        private static Field sMaximumField;
        private static boolean sMaximumFieldFetched;
        private static Field sMinModeField;
        private static boolean sMinModeFieldFetched;
        private static Field sMinimumField;
        private static boolean sMinimumFieldFetched;
        
        private static Field retrieveField(final String s) {
            Field declaredField = null;
            try {
                final Field field = declaredField = TextView.class.getDeclaredField(s);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException ex) {
                Log.e("TextViewCompatBase", "Could not retrieve " + s + " field.");
                return declaredField;
            }
        }
        
        private static int retrieveIntFromField(final Field field, final TextView textView) {
            try {
                return field.getInt(textView);
            }
            catch (IllegalAccessException ex) {
                Log.d("TextViewCompatBase", "Could not retrieve value of " + field.getName() + " field.");
                return -1;
            }
        }
        
        public int getAutoSizeMaxTextSize(final TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeMaxTextSize();
            }
            return -1;
        }
        
        public int getAutoSizeMinTextSize(final TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeMinTextSize();
            }
            return -1;
        }
        
        public int getAutoSizeStepGranularity(final TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeStepGranularity();
            }
            return -1;
        }
        
        public int[] getAutoSizeTextAvailableSizes(final TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeTextAvailableSizes();
            }
            return new int[0];
        }
        
        public int getAutoSizeTextType(final TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeTextType();
            }
            return 0;
        }
        
        public Drawable[] getCompoundDrawablesRelative(@NonNull final TextView textView) {
            return textView.getCompoundDrawables();
        }
        
        public int getMaxLines(final TextView textView) {
            if (!TextViewCompatBaseImpl.sMaxModeFieldFetched) {
                TextViewCompatBaseImpl.sMaxModeField = retrieveField("mMaxMode");
                TextViewCompatBaseImpl.sMaxModeFieldFetched = true;
            }
            if (TextViewCompatBaseImpl.sMaxModeField != null && retrieveIntFromField(TextViewCompatBaseImpl.sMaxModeField, textView) == 1) {
                if (!TextViewCompatBaseImpl.sMaximumFieldFetched) {
                    TextViewCompatBaseImpl.sMaximumField = retrieveField("mMaximum");
                    TextViewCompatBaseImpl.sMaximumFieldFetched = true;
                }
                if (TextViewCompatBaseImpl.sMaximumField != null) {
                    return retrieveIntFromField(TextViewCompatBaseImpl.sMaximumField, textView);
                }
            }
            return -1;
        }
        
        public int getMinLines(final TextView textView) {
            if (!TextViewCompatBaseImpl.sMinModeFieldFetched) {
                TextViewCompatBaseImpl.sMinModeField = retrieveField("mMinMode");
                TextViewCompatBaseImpl.sMinModeFieldFetched = true;
            }
            if (TextViewCompatBaseImpl.sMinModeField != null && retrieveIntFromField(TextViewCompatBaseImpl.sMinModeField, textView) == 1) {
                if (!TextViewCompatBaseImpl.sMinimumFieldFetched) {
                    TextViewCompatBaseImpl.sMinimumField = retrieveField("mMinimum");
                    TextViewCompatBaseImpl.sMinimumFieldFetched = true;
                }
                if (TextViewCompatBaseImpl.sMinimumField != null) {
                    return retrieveIntFromField(TextViewCompatBaseImpl.sMinimumField, textView);
                }
            }
            return -1;
        }
        
        public void setAutoSizeTextTypeUniformWithConfiguration(final TextView textView, final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
            }
        }
        
        public void setAutoSizeTextTypeUniformWithPresetSizes(final TextView textView, @NonNull final int[] array, final int n) throws IllegalArgumentException {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithPresetSizes(array, n);
            }
        }
        
        public void setAutoSizeTextTypeWithDefaults(final TextView textView, final int autoSizeTextTypeWithDefaults) {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView)textView).setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
            }
        }
        
        public void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            textView.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        }
        
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
        }
        
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
        
        public void setTextAppearance(final TextView textView, @StyleRes final int n) {
            textView.setTextAppearance(textView.getContext(), n);
        }
    }
}
