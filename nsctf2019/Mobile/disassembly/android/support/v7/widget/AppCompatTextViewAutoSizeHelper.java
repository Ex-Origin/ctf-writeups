// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.support.annotation.RestrictTo;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.annotation.TargetApi;
import android.text.StaticLayout$Builder;
import android.text.TextDirectionHeuristics;
import android.text.TextDirectionHeuristic;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.TextView;
import android.text.TextPaint;
import java.lang.reflect.Method;
import java.util.Hashtable;
import android.content.Context;
import android.graphics.RectF;

class AppCompatTextViewAutoSizeHelper
{
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final RectF TEMP_RECTF;
    static final int UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1;
    private static final int VERY_WIDE = 1048576;
    private int mAutoSizeMaxTextSizeInPx;
    private int mAutoSizeMinTextSizeInPx;
    private int mAutoSizeStepGranularityInPx;
    private int[] mAutoSizeTextSizesInPx;
    private int mAutoSizeTextType;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues;
    private Hashtable<String, Method> mMethodByNameCache;
    private boolean mNeedsAutoSizeText;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;
    
    static {
        TEMP_RECTF = new RectF();
    }
    
    AppCompatTextViewAutoSizeHelper(final TextView mTextView) {
        this.mAutoSizeTextType = 0;
        this.mNeedsAutoSizeText = false;
        this.mAutoSizeStepGranularityInPx = -1;
        this.mAutoSizeMinTextSizeInPx = -1;
        this.mAutoSizeMaxTextSizeInPx = -1;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mHasPresetAutoSizeValues = false;
        this.mMethodByNameCache = new Hashtable<String, Method>();
        this.mTextView = mTextView;
        this.mContext = this.mTextView.getContext();
    }
    
    private int[] cleanupAutoSizePresetSizes(int[] array) {
        final int length = array.length;
        if (length != 0) {
            Arrays.sort(array);
            final ArrayList<Comparable<? super Integer>> list = new ArrayList<Comparable<? super Integer>>();
            for (int i = 0; i < length; ++i) {
                final int n = array[i];
                if (n > 0 && Collections.binarySearch(list, n) < 0) {
                    list.add(n);
                }
            }
            if (length != list.size()) {
                final int size = list.size();
                array = new int[size];
                for (int j = 0; j < size; ++j) {
                    array[j] = list.get(j);
                }
                return array;
            }
        }
        return array;
    }
    
    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1;
        this.mAutoSizeMaxTextSizeInPx = -1;
        this.mAutoSizeStepGranularityInPx = -1;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }
    
    @TargetApi(23)
    private StaticLayout createStaticLayoutForMeasuring(final CharSequence charSequence, final Layout$Alignment alignment, int maxLines, final int n) {
        final TextDirectionHeuristic textDirection = this.invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
        final StaticLayout$Builder setHyphenationFrequency = StaticLayout$Builder.obtain(charSequence, 0, charSequence.length(), this.mTempTextPaint, maxLines).setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
        maxLines = n;
        if (n == -1) {
            maxLines = Integer.MAX_VALUE;
        }
        return setHyphenationFrequency.setMaxLines(maxLines).setTextDirection(textDirection).build();
    }
    
    @TargetApi(14)
    private StaticLayout createStaticLayoutForMeasuringPre23(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final int n) {
        float n2;
        float n3;
        boolean b;
        if (Build$VERSION.SDK_INT >= 16) {
            n2 = this.mTextView.getLineSpacingMultiplier();
            n3 = this.mTextView.getLineSpacingExtra();
            b = this.mTextView.getIncludeFontPadding();
        }
        else {
            n2 = this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", 1.0f);
            n3 = this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", 0.0f);
            b = this.invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", true);
        }
        return new StaticLayout(charSequence, this.mTempTextPaint, n, layout$Alignment, n2, n3, b);
    }
    
    private int findLargestTextSizeWhichFits(final RectF rectF) {
        final int length = this.mAutoSizeTextSizesInPx.length;
        if (length == 0) {
            throw new IllegalStateException("No available text sizes to choose from.");
        }
        int n = 0;
        int i = 0 + 1;
        int n2 = length - 1;
        while (i <= n2) {
            final int n3 = (i + n2) / 2;
            if (this.suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[n3], rectF)) {
                final int n4 = n3 + 1;
                n = i;
                i = n4;
            }
            else {
                n2 = (n = n3 - 1);
            }
        }
        return this.mAutoSizeTextSizesInPx[n];
    }
    
    private <T> T invokeAndReturnWithDefault(@NonNull Object o, @NonNull final String s, @NonNull final T t) {
        final Object o2 = null;
        try {
            Method method;
            if ((method = this.mMethodByNameCache.get(s)) == null) {
                final Method declaredMethod = TextView.class.getDeclaredMethod(s, (Class<?>[])new Class[0]);
                if ((method = declaredMethod) != null) {
                    declaredMethod.setAccessible(true);
                    this.mMethodByNameCache.put(s, declaredMethod);
                    method = declaredMethod;
                }
            }
            final Object invoke = method.invoke(o, new Object[0]);
            if ((o = invoke) == null) {
                o = invoke;
                if (false) {
                    o = t;
                }
            }
            return (T)o;
        }
        catch (Exception ex) {
            o = o2;
            if (false) {
                return (T)o;
            }
            o = o2;
            if (true) {
                return t;
            }
            return (T)o;
        }
        finally {
            if (false || false) {}
        }
    }
    
    private void setRawTextSize(final float textSize) {
        if (textSize == this.mTextView.getPaint().getTextSize()) {
            return;
        }
        this.mTextView.getPaint().setTextSize(textSize);
        if (this.mTextView.getLayout() == null) {
            return;
        }
        this.mNeedsAutoSizeText = false;
        while (true) {
            try {
                Method method;
                if ((method = this.mMethodByNameCache.get("nullLayouts")) == null) {
                    final Method declaredMethod = TextView.class.getDeclaredMethod("nullLayouts", (Class<?>[])new Class[0]);
                    if ((method = declaredMethod) != null) {
                        declaredMethod.setAccessible(true);
                        this.mMethodByNameCache.put("nullLayouts", declaredMethod);
                        method = declaredMethod;
                    }
                }
                if (method != null) {
                    method.invoke(this.mTextView, new Object[0]);
                }
                this.mTextView.requestLayout();
                this.mTextView.invalidate();
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    private void setupAutoSizeText() {
        if (this.supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int n2;
                final int n = n2 = (int)Math.ceil((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) / this.mAutoSizeStepGranularityInPx);
                if ((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) % this.mAutoSizeStepGranularityInPx == 0) {
                    n2 = n + 1;
                }
                this.mAutoSizeTextSizesInPx = new int[n2];
                int mAutoSizeMinTextSizeInPx = this.mAutoSizeMinTextSizeInPx;
                for (int i = 0; i < n2; ++i) {
                    this.mAutoSizeTextSizesInPx[i] = mAutoSizeMinTextSizeInPx;
                    mAutoSizeMinTextSizeInPx += this.mAutoSizeStepGranularityInPx;
                }
            }
            this.mNeedsAutoSizeText = true;
            this.autoSizeText();
        }
    }
    
    private void setupAutoSizeUniformPresetSizes(final TypedArray typedArray) {
        final int length = typedArray.length();
        final int[] array = new int[length];
        if (length > 0) {
            for (int i = 0; i < length; ++i) {
                array[i] = typedArray.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(array);
            this.setupAutoSizeUniformPresetSizesConfiguration();
        }
    }
    
    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        final int length = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = (length > 0);
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = this.mAutoSizeTextSizesInPx[0];
            this.mAutoSizeMaxTextSizeInPx = this.mAutoSizeTextSizesInPx[length - 1];
            this.mAutoSizeStepGranularityInPx = -1;
        }
        return this.mHasPresetAutoSizeValues;
    }
    
    private boolean suggestedSizeFitsInSpace(final int n, final RectF rectF) {
        final CharSequence text = this.mTextView.getText();
        int maxLines;
        if (Build$VERSION.SDK_INT >= 16) {
            maxLines = this.mTextView.getMaxLines();
        }
        else {
            maxLines = -1;
        }
        int n2;
        if (this.invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", false)) {
            n2 = 1048576;
        }
        else {
            n2 = this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
        }
        if (this.mTempTextPaint == null) {
            this.mTempTextPaint = new TextPaint();
        }
        else {
            this.mTempTextPaint.reset();
        }
        this.mTempTextPaint.set(this.mTextView.getPaint());
        this.mTempTextPaint.setTextSize((float)n);
        final Layout$Alignment layout$Alignment = this.invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Layout$Alignment.ALIGN_NORMAL);
        StaticLayout staticLayout;
        if (Build$VERSION.SDK_INT >= 23) {
            staticLayout = this.createStaticLayoutForMeasuring(text, layout$Alignment, n2, maxLines);
        }
        else {
            staticLayout = this.createStaticLayoutForMeasuringPre23(text, layout$Alignment, n2);
        }
        return (maxLines == -1 || staticLayout.getLineCount() <= maxLines) && staticLayout.getHeight() <= rectF.bottom;
    }
    
    private boolean supportsAutoSizeText() {
        return !(this.mTextView instanceof AppCompatEditText);
    }
    
    private void validateAndSetAutoSizeTextTypeUniformConfiguration(final int mAutoSizeMinTextSizeInPx, final int mAutoSizeMaxTextSizeInPx, final int mAutoSizeStepGranularityInPx) throws IllegalArgumentException {
        if (mAutoSizeMinTextSizeInPx <= 0) {
            throw new IllegalArgumentException("Minimum auto-size text size (" + mAutoSizeMinTextSizeInPx + "px) is less or equal to (0px)");
        }
        if (mAutoSizeMaxTextSizeInPx <= mAutoSizeMinTextSizeInPx) {
            throw new IllegalArgumentException("Maximum auto-size text size (" + mAutoSizeMaxTextSizeInPx + "px) is less or equal to minimum auto-size " + "text size (" + mAutoSizeMinTextSizeInPx + "px)");
        }
        if (mAutoSizeStepGranularityInPx <= 0) {
            throw new IllegalArgumentException("The auto-size step granularity (" + mAutoSizeStepGranularityInPx + "px) is less or equal to (0px)");
        }
        this.mAutoSizeTextType = 1;
        this.mAutoSizeMinTextSizeInPx = mAutoSizeMinTextSizeInPx;
        this.mAutoSizeMaxTextSizeInPx = mAutoSizeMaxTextSizeInPx;
        this.mAutoSizeStepGranularityInPx = mAutoSizeStepGranularityInPx;
        this.mHasPresetAutoSizeValues = false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void autoSizeText() {
        final int n = this.mTextView.getWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
        int n2;
        if (Build$VERSION.SDK_INT >= 21) {
            n2 = this.mTextView.getHeight() - this.mTextView.getExtendedPaddingBottom() - this.mTextView.getExtendedPaddingBottom();
        }
        else {
            n2 = this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom() - this.mTextView.getCompoundPaddingTop();
        }
        if (n <= 0 || n2 <= 0) {
            return;
        }
        synchronized (AppCompatTextViewAutoSizeHelper.TEMP_RECTF) {
            AppCompatTextViewAutoSizeHelper.TEMP_RECTF.setEmpty();
            AppCompatTextViewAutoSizeHelper.TEMP_RECTF.right = n;
            AppCompatTextViewAutoSizeHelper.TEMP_RECTF.bottom = n2;
            final float n3 = this.findLargestTextSizeWhichFits(AppCompatTextViewAutoSizeHelper.TEMP_RECTF);
            if (n3 != this.mTextView.getTextSize()) {
                this.setTextSizeInternal(0, n3);
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeMaxTextSizeInPx;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeMinTextSize() {
        return this.mAutoSizeMinTextSizeInPx;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeStepGranularity() {
        return this.mAutoSizeStepGranularityInPx;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean getNeedsAutoSizeText() {
        return this.mNeedsAutoSizeText;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean isAutoSizeEnabled() {
        return this.supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }
    
    void loadFromAttributes(final AttributeSet set, int dimensionPixelSize) {
        int dimensionPixelSize2 = -1;
        int dimensionPixelSize3 = -1;
        final int n = -1;
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(set, R.styleable.AppCompatTextView, dimensionPixelSize, 0);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = obtainStyledAttributes.getInt(R.styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        dimensionPixelSize = n;
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
            dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AppCompatTextView_autoSizeStepGranularity, -1);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
            dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AppCompatTextView_autoSizeMinTextSize, -1);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AppCompatTextView_autoSizeMaxTextSize, -1);
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizePresetSizes)) {
            final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextView_autoSizePresetSizes, 0);
            if (resourceId > 0) {
                final TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(resourceId);
                this.setupAutoSizeUniformPresetSizes(obtainTypedArray);
                obtainTypedArray.recycle();
            }
        }
        obtainStyledAttributes.recycle();
        if (this.supportsAutoSizeText()) {
            if (this.mAutoSizeTextType == 1) {
                if (!this.mHasPresetAutoSizeValues) {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    int n2;
                    if ((n2 = dimensionPixelSize2) == -1) {
                        n2 = (int)TypedValue.applyDimension(2, 12.0f, displayMetrics);
                    }
                    int n3;
                    if ((n3 = dimensionPixelSize3) == -1) {
                        n3 = (int)TypedValue.applyDimension(2, 112.0f, displayMetrics);
                    }
                    int n4;
                    if ((n4 = dimensionPixelSize) == -1) {
                        n4 = 1;
                    }
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration(n2, n3, n4);
                }
                this.setupAutoSizeText();
            }
            return;
        }
        this.mAutoSizeTextType = 0;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            this.validateAndSetAutoSizeTextTypeUniformConfiguration((int)TypedValue.applyDimension(n4, (float)n, displayMetrics), (int)TypedValue.applyDimension(n4, (float)n2, displayMetrics), (int)TypedValue.applyDimension(n4, (float)n3, displayMetrics));
            this.setupAutoSizeText();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            final int length = array.length;
            if (length > 0) {
                final int[] array2 = new int[length];
                int[] copy;
                if (n == 0) {
                    copy = Arrays.copyOf(array, length);
                }
                else {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    int n2 = 0;
                    while (true) {
                        copy = array2;
                        if (n2 >= length) {
                            break;
                        }
                        array2[n2] = (int)TypedValue.applyDimension(n, (float)array[n2], displayMetrics);
                        ++n2;
                    }
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(copy);
                if (!this.setupAutoSizeUniformPresetSizesConfiguration()) {
                    throw new IllegalArgumentException("None of the preset sizes is valid: " + Arrays.toString(array));
                }
            }
            else {
                this.mHasPresetAutoSizeValues = false;
            }
            this.setupAutoSizeText();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setAutoSizeTextTypeWithDefaults(final int n) {
        if (this.supportsAutoSizeText()) {
            switch (n) {
                default: {
                    throw new IllegalArgumentException("Unknown auto-size text type: " + n);
                }
                case 0: {
                    this.clearAutoSizeConfiguration();
                    break;
                }
                case 1: {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration((int)TypedValue.applyDimension(2, 12.0f, displayMetrics), (int)TypedValue.applyDimension(2, 112.0f, displayMetrics), 1);
                    this.setupAutoSizeText();
                }
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setNeedsAutoSizeText(final boolean mNeedsAutoSizeText) {
        this.mNeedsAutoSizeText = mNeedsAutoSizeText;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setTextSizeInternal(final int n, final float n2) {
        Resources resources;
        if (this.mContext == null) {
            resources = Resources.getSystem();
        }
        else {
            resources = this.mContext.getResources();
        }
        this.setRawTextSize(TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
    }
}
