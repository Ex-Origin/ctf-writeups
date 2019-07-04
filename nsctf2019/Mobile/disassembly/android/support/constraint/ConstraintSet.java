// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint;

import android.content.res.XmlResourceParser;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.Iterator;
import android.view.View;
import android.os.Build$VERSION;
import android.view.ViewGroup$LayoutParams;
import java.util.Collection;
import java.util.HashSet;
import android.util.Log;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import java.util.HashMap;
import android.util.SparseIntArray;

public class ConstraintSet
{
    private static final int ALPHA = 43;
    public static final int BASELINE = 5;
    private static final int BASELINE_TO_BASELINE = 1;
    public static final int BOTTOM = 4;
    private static final int BOTTOM_MARGIN = 2;
    private static final int BOTTOM_TO_BOTTOM = 3;
    private static final int BOTTOM_TO_TOP = 4;
    public static final int CHAIN_PACKED = 2;
    public static final int CHAIN_SPREAD = 0;
    public static final int CHAIN_SPREAD_INSIDE = 1;
    private static final boolean DEBUG = false;
    private static final int DIMENSION_RATIO = 5;
    private static final int EDITOR_ABSOLUTE_X = 6;
    private static final int EDITOR_ABSOLUTE_Y = 7;
    private static final int ELEVATION = 44;
    public static final int END = 7;
    private static final int END_MARGIN = 8;
    private static final int END_TO_END = 9;
    private static final int END_TO_START = 10;
    public static final int GONE = 8;
    private static final int GONE_BOTTOM_MARGIN = 11;
    private static final int GONE_END_MARGIN = 12;
    private static final int GONE_LEFT_MARGIN = 13;
    private static final int GONE_RIGHT_MARGIN = 14;
    private static final int GONE_START_MARGIN = 15;
    private static final int GONE_TOP_MARGIN = 16;
    private static final int GUIDE_BEGIN = 17;
    private static final int GUIDE_END = 18;
    private static final int GUIDE_PERCENT = 19;
    private static final int HEIGHT_DEFAULT = 55;
    private static final int HEIGHT_MAX = 57;
    private static final int HEIGHT_MIN = 59;
    public static final int HORIZONTAL = 0;
    private static final int HORIZONTAL_BIAS = 20;
    public static final int HORIZONTAL_GUIDELINE = 0;
    private static final int HORIZONTAL_STYLE = 41;
    private static final int HORIZONTAL_WEIGHT = 39;
    public static final int INVISIBLE = 4;
    private static final int LAYOUT_HEIGHT = 21;
    private static final int LAYOUT_VISIBILITY = 22;
    private static final int LAYOUT_WIDTH = 23;
    public static final int LEFT = 1;
    private static final int LEFT_MARGIN = 24;
    private static final int LEFT_TO_LEFT = 25;
    private static final int LEFT_TO_RIGHT = 26;
    public static final int MATCH_CONSTRAINT = 0;
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    private static final int ORIENTATION = 27;
    public static final int PARENT_ID = 0;
    public static final int RIGHT = 2;
    private static final int RIGHT_MARGIN = 28;
    private static final int RIGHT_TO_LEFT = 29;
    private static final int RIGHT_TO_RIGHT = 30;
    private static final int ROTATION_X = 45;
    private static final int ROTATION_Y = 46;
    private static final int SCALE_X = 47;
    private static final int SCALE_Y = 48;
    public static final int START = 6;
    private static final int START_MARGIN = 31;
    private static final int START_TO_END = 32;
    private static final int START_TO_START = 33;
    private static final String TAG = "ConstraintSet";
    public static final int TOP = 3;
    private static final int TOP_MARGIN = 34;
    private static final int TOP_TO_BOTTOM = 35;
    private static final int TOP_TO_TOP = 36;
    private static final int TRANSFORM_PIVOT_X = 49;
    private static final int TRANSFORM_PIVOT_Y = 50;
    private static final int TRANSLATION_X = 51;
    private static final int TRANSLATION_Y = 52;
    private static final int TRANSLATION_Z = 53;
    public static final int UNSET = -1;
    private static final int UNUSED = 60;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_BIAS = 37;
    public static final int VERTICAL_GUIDELINE = 1;
    private static final int VERTICAL_STYLE = 42;
    private static final int VERTICAL_WEIGHT = 40;
    private static final int VIEW_ID = 38;
    private static final int[] VISIBILITY_FLAGS;
    public static final int VISIBLE = 0;
    private static final int WIDTH_DEFAULT = 54;
    private static final int WIDTH_MAX = 56;
    private static final int WIDTH_MIN = 58;
    public static final int WRAP_CONTENT = -2;
    private static SparseIntArray mapToConstant;
    private HashMap<Integer, Constraint> mConstraints;
    
    static {
        VISIBILITY_FLAGS = new int[] { 0, 4, 8 };
        (ConstraintSet.mapToConstant = new SparseIntArray()).append(R.styleable.ConstraintSet_layout_constraintLeft_toLeftOf, 25);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_toRightOf, 26);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toLeftOf, 29);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_toRightOf, 30);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toTopOf, 36);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_toBottomOf, 35);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toTopOf, 4);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_toBottomOf, 3);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_toBaselineOf, 1);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteX, 6);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_editor_absoluteY, 7);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_begin, 17);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_end, 18);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintGuide_percent, 19);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_orientation, 27);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toEndOf, 32);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintStart_toStartOf, 33);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toStartOf, 10);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintEnd_toEndOf, 9);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginLeft, 13);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginTop, 16);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginRight, 14);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginBottom, 11);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginStart, 15);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_goneMarginEnd, 12);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_weight, 40);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_weight, 39);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_chainStyle, 41);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_chainStyle, 42);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHorizontal_bias, 20);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintVertical_bias, 37);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintDimensionRatio, 5);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintLeft_creator, 60);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintTop_creator, 60);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintRight_creator, 60);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBottom_creator, 60);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintBaseline_creator, 60);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginLeft, 24);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginRight, 28);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginStart, 31);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginEnd, 8);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginTop, 34);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_marginBottom, 2);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_width, 23);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_layout_height, 21);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_visibility, 22);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_alpha, 43);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_elevation, 44);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_rotationX, 45);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_rotationY, 46);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_scaleX, 47);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_scaleY, 48);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotX, 49);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_transformPivotY, 50);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_translationX, 51);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_translationY, 52);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_translationZ, 53);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_default, 54);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_default, 55);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_max, 56);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_max, 57);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintWidth_min, 58);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_layout_constraintHeight_min, 59);
        ConstraintSet.mapToConstant.append(R.styleable.ConstraintSet_android_id, 38);
    }
    
    public ConstraintSet() {
        this.mConstraints = new HashMap<Integer, Constraint>();
    }
    
    private void createHorizontalChain(int i, int n, final int n2, final int n3, final int[] array, final float[] array2, final int horizontalChainStyle, final int n4, final int n5) {
        if (array.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null && array2.length != array.length) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null) {
            this.get(array[0]).verticalWeight = array2[0];
        }
        this.get(array[0]).horizontalChainStyle = horizontalChainStyle;
        this.connect(array[0], n4, i, n, -1);
        for (i = 1; i < array.length; ++i) {
            n = array[i];
            this.connect(array[i], n4, array[i - 1], n5, -1);
            this.connect(array[i - 1], n5, array[i], n4, -1);
            if (array2 != null) {
                this.get(array[i]).horizontalWeight = array2[i];
            }
        }
        this.connect(array[array.length - 1], n5, n2, n3, -1);
    }
    
    private Constraint fillFromAttributeList(final Context context, final AttributeSet set) {
        final Constraint constraint = new Constraint();
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ConstraintSet);
        this.populateConstraint(constraint, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        return constraint;
    }
    
    private Constraint get(final int n) {
        if (!this.mConstraints.containsKey(n)) {
            this.mConstraints.put(n, new Constraint());
        }
        return this.mConstraints.get(n);
    }
    
    private static int lookupID(final TypedArray typedArray, final int n, int n2) {
        if ((n2 = typedArray.getResourceId(n, n2)) == -1) {
            n2 = typedArray.getInt(n, -1);
        }
        return n2;
    }
    
    private void populateConstraint(final Constraint constraint, final TypedArray typedArray) {
        for (int indexCount = typedArray.getIndexCount(), i = 0; i < indexCount; ++i) {
            final int index = typedArray.getIndex(i);
            switch (ConstraintSet.mapToConstant.get(index)) {
                default: {
                    Log.w("ConstraintSet", "Unknown attribute 0x" + Integer.toHexString(index) + "   " + ConstraintSet.mapToConstant.get(index));
                    break;
                }
                case 25: {
                    constraint.leftToLeft = lookupID(typedArray, index, constraint.leftToLeft);
                    break;
                }
                case 26: {
                    constraint.leftToRight = lookupID(typedArray, index, constraint.leftToRight);
                    break;
                }
                case 29: {
                    constraint.rightToLeft = lookupID(typedArray, index, constraint.rightToLeft);
                    break;
                }
                case 30: {
                    constraint.rightToRight = lookupID(typedArray, index, constraint.rightToRight);
                    break;
                }
                case 36: {
                    constraint.topToTop = lookupID(typedArray, index, constraint.topToTop);
                    break;
                }
                case 35: {
                    constraint.topToBottom = lookupID(typedArray, index, constraint.topToBottom);
                    break;
                }
                case 4: {
                    constraint.bottomToTop = lookupID(typedArray, index, constraint.bottomToTop);
                    break;
                }
                case 3: {
                    constraint.bottomToBottom = lookupID(typedArray, index, constraint.bottomToBottom);
                    break;
                }
                case 1: {
                    constraint.baselineToBaseline = lookupID(typedArray, index, constraint.baselineToBaseline);
                    break;
                }
                case 6: {
                    constraint.editorAbsoluteX = typedArray.getDimensionPixelOffset(index, constraint.editorAbsoluteX);
                    break;
                }
                case 7: {
                    constraint.editorAbsoluteY = typedArray.getDimensionPixelOffset(index, constraint.editorAbsoluteY);
                    break;
                }
                case 17: {
                    constraint.guideBegin = typedArray.getDimensionPixelOffset(index, constraint.guideBegin);
                    break;
                }
                case 18: {
                    constraint.guideEnd = typedArray.getDimensionPixelOffset(index, constraint.guideEnd);
                    break;
                }
                case 19: {
                    constraint.guidePercent = typedArray.getFloat(index, constraint.guidePercent);
                    break;
                }
                case 27: {
                    constraint.orientation = typedArray.getInt(index, constraint.orientation);
                    break;
                }
                case 32: {
                    constraint.startToEnd = lookupID(typedArray, index, constraint.startToEnd);
                    break;
                }
                case 33: {
                    constraint.startToStart = lookupID(typedArray, index, constraint.startToStart);
                    break;
                }
                case 10: {
                    constraint.endToStart = lookupID(typedArray, index, constraint.endToStart);
                    break;
                }
                case 9: {
                    constraint.bottomToTop = lookupID(typedArray, index, constraint.endToEnd);
                    break;
                }
                case 13: {
                    constraint.goneLeftMargin = typedArray.getDimensionPixelSize(index, constraint.goneLeftMargin);
                    break;
                }
                case 16: {
                    constraint.goneTopMargin = typedArray.getDimensionPixelSize(index, constraint.goneTopMargin);
                    break;
                }
                case 14: {
                    constraint.goneRightMargin = typedArray.getDimensionPixelSize(index, constraint.goneRightMargin);
                    break;
                }
                case 11: {
                    constraint.goneBottomMargin = typedArray.getDimensionPixelSize(index, constraint.goneBottomMargin);
                    break;
                }
                case 15: {
                    constraint.goneStartMargin = typedArray.getDimensionPixelSize(index, constraint.goneStartMargin);
                    break;
                }
                case 12: {
                    constraint.goneEndMargin = typedArray.getDimensionPixelSize(index, constraint.goneEndMargin);
                    break;
                }
                case 20: {
                    constraint.horizontalBias = typedArray.getFloat(index, constraint.horizontalBias);
                    break;
                }
                case 37: {
                    constraint.verticalBias = typedArray.getFloat(index, constraint.verticalBias);
                    break;
                }
                case 24: {
                    constraint.leftMargin = typedArray.getDimensionPixelSize(index, constraint.leftMargin);
                    break;
                }
                case 28: {
                    constraint.rightMargin = typedArray.getDimensionPixelSize(index, constraint.rightMargin);
                    break;
                }
                case 31: {
                    constraint.startMargin = typedArray.getDimensionPixelSize(index, constraint.startMargin);
                    break;
                }
                case 8: {
                    constraint.endMargin = typedArray.getDimensionPixelSize(index, constraint.endMargin);
                    break;
                }
                case 34: {
                    constraint.topMargin = typedArray.getDimensionPixelSize(index, constraint.topMargin);
                    break;
                }
                case 2: {
                    constraint.bottomMargin = typedArray.getDimensionPixelSize(index, constraint.bottomMargin);
                    break;
                }
                case 23: {
                    constraint.mWidth = typedArray.getLayoutDimension(index, constraint.mWidth);
                    break;
                }
                case 21: {
                    constraint.mHeight = typedArray.getLayoutDimension(index, constraint.mHeight);
                    break;
                }
                case 22: {
                    constraint.visibility = typedArray.getInt(index, constraint.visibility);
                    constraint.visibility = ConstraintSet.VISIBILITY_FLAGS[constraint.visibility];
                    break;
                }
                case 43: {
                    constraint.alpha = typedArray.getFloat(index, constraint.alpha);
                    break;
                }
                case 44: {
                    constraint.applyElevation = true;
                    constraint.elevation = typedArray.getFloat(index, constraint.elevation);
                    break;
                }
                case 45: {
                    constraint.rotationX = typedArray.getFloat(index, constraint.rotationX);
                    break;
                }
                case 46: {
                    constraint.rotationY = typedArray.getFloat(index, constraint.rotationY);
                    break;
                }
                case 47: {
                    constraint.scaleX = typedArray.getFloat(index, constraint.scaleX);
                    break;
                }
                case 48: {
                    constraint.scaleY = typedArray.getFloat(index, constraint.scaleY);
                    break;
                }
                case 49: {
                    constraint.transformPivotX = typedArray.getFloat(index, constraint.transformPivotX);
                    break;
                }
                case 50: {
                    constraint.transformPivotY = typedArray.getFloat(index, constraint.transformPivotY);
                    break;
                }
                case 51: {
                    constraint.translationX = typedArray.getFloat(index, constraint.translationX);
                    break;
                }
                case 52: {
                    constraint.translationY = typedArray.getFloat(index, constraint.translationY);
                    break;
                }
                case 53: {
                    constraint.translationZ = typedArray.getFloat(index, constraint.translationZ);
                    break;
                }
                case 40: {
                    constraint.verticalWeight = typedArray.getFloat(index, constraint.verticalWeight);
                    break;
                }
                case 39: {
                    constraint.horizontalWeight = typedArray.getFloat(index, constraint.horizontalWeight);
                    break;
                }
                case 42: {
                    constraint.verticalChainStyle = typedArray.getInt(index, constraint.verticalChainStyle);
                    break;
                }
                case 41: {
                    constraint.horizontalChainStyle = typedArray.getInt(index, constraint.horizontalChainStyle);
                    break;
                }
                case 38: {
                    constraint.mViewId = typedArray.getResourceId(index, constraint.mViewId);
                    break;
                }
                case 5: {
                    constraint.dimensionRatio = typedArray.getString(index);
                    break;
                }
                case 60: {
                    Log.w("ConstraintSet", "unused attribute 0x" + Integer.toHexString(index) + "   " + ConstraintSet.mapToConstant.get(index));
                    break;
                }
            }
        }
    }
    
    private String sideToString(final int n) {
        switch (n) {
            default: {
                return "undefined";
            }
            case 1: {
                return "left";
            }
            case 2: {
                return "right";
            }
            case 3: {
                return "top";
            }
            case 4: {
                return "bottom";
            }
            case 5: {
                return "baseline";
            }
            case 6: {
                return "start";
            }
            case 7: {
                return "end";
            }
        }
    }
    
    public void addToHorizontalChain(final int n, final int n2, final int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 1;
        }
        else {
            n4 = 2;
        }
        this.connect(n, 1, n2, n4, 0);
        int n5;
        if (n3 == 0) {
            n5 = 2;
        }
        else {
            n5 = 1;
        }
        this.connect(n, 2, n3, n5, 0);
        if (n2 != 0) {
            this.connect(n2, 2, n, 1, 0);
        }
        if (n3 != 0) {
            this.connect(n3, 1, n, 2, 0);
        }
    }
    
    public void addToHorizontalChainRTL(final int n, final int n2, final int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 6;
        }
        else {
            n4 = 7;
        }
        this.connect(n, 6, n2, n4, 0);
        int n5;
        if (n3 == 0) {
            n5 = 7;
        }
        else {
            n5 = 6;
        }
        this.connect(n, 7, n3, n5, 0);
        if (n2 != 0) {
            this.connect(n2, 7, n, 6, 0);
        }
        if (n3 != 0) {
            this.connect(n3, 6, n, 7, 0);
        }
    }
    
    public void addToVerticalChain(final int n, final int n2, final int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 3;
        }
        else {
            n4 = 4;
        }
        this.connect(n, 3, n2, n4, 0);
        int n5;
        if (n3 == 0) {
            n5 = 4;
        }
        else {
            n5 = 3;
        }
        this.connect(n, 4, n3, n5, 0);
        if (n2 != 0) {
            this.connect(n2, 4, n, 3, 0);
        }
        if (n2 != 0) {
            this.connect(n3, 3, n, 4, 0);
        }
    }
    
    public void applyTo(final ConstraintLayout constraintLayout) {
        this.applyToInternal(constraintLayout);
        constraintLayout.setConstraintSet(null);
    }
    
    void applyToInternal(final ConstraintLayout constraintLayout) {
        final int childCount = constraintLayout.getChildCount();
        final HashSet<Integer> set = new HashSet<Integer>(this.mConstraints.keySet());
        for (int i = 0; i < childCount; ++i) {
            final View child = constraintLayout.getChildAt(i);
            final int id = child.getId();
            if (this.mConstraints.containsKey(id)) {
                set.remove(id);
                final Constraint constraint = this.mConstraints.get(id);
                final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)child.getLayoutParams();
                constraint.applyTo(layoutParams);
                child.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                child.setVisibility(constraint.visibility);
                if (Build$VERSION.SDK_INT >= 17) {
                    child.setAlpha(constraint.alpha);
                    child.setRotationX(constraint.rotationX);
                    child.setRotationY(constraint.rotationY);
                    child.setScaleX(constraint.scaleX);
                    child.setScaleY(constraint.scaleY);
                    child.setPivotX(constraint.transformPivotX);
                    child.setPivotY(constraint.transformPivotY);
                    child.setTranslationX(constraint.translationX);
                    child.setTranslationY(constraint.translationY);
                    if (Build$VERSION.SDK_INT >= 21) {
                        child.setTranslationZ(constraint.translationZ);
                        if (constraint.applyElevation) {
                            child.setElevation(constraint.elevation);
                        }
                    }
                }
            }
        }
        for (final Integer n : set) {
            final Constraint constraint2 = this.mConstraints.get(n);
            if (constraint2.mIsGuideline) {
                final Guideline guideline = new Guideline(constraintLayout.getContext());
                guideline.setId((int)n);
                final ConstraintLayout.LayoutParams generateDefaultLayoutParams = constraintLayout.generateDefaultLayoutParams();
                constraint2.applyTo(generateDefaultLayoutParams);
                constraintLayout.addView((View)guideline, (ViewGroup$LayoutParams)generateDefaultLayoutParams);
            }
        }
    }
    
    public void center(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final float verticalBias) {
        if (n4 < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (n7 < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        }
        if (verticalBias <= 0.0f || verticalBias > 1.0f) {
            throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
        }
        if (n3 == 1 || n3 == 2) {
            this.connect(n, 1, n2, n3, n4);
            this.connect(n, 2, n5, n6, n7);
            this.mConstraints.get(n).horizontalBias = verticalBias;
            return;
        }
        if (n3 == 6 || n3 == 7) {
            this.connect(n, 6, n2, n3, n4);
            this.connect(n, 7, n5, n6, n7);
            this.mConstraints.get(n).horizontalBias = verticalBias;
            return;
        }
        this.connect(n, 3, n2, n3, n4);
        this.connect(n, 4, n5, n6, n7);
        this.mConstraints.get(n).verticalBias = verticalBias;
    }
    
    public void centerHorizontally(final int n, final int n2) {
        if (n2 == 0) {
            this.center(n, 0, 1, 0, 0, 2, 0, 0.5f);
            return;
        }
        this.center(n, n2, 2, 0, n2, 1, 0, 0.5f);
    }
    
    public void centerHorizontally(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final float horizontalBias) {
        this.connect(n, 1, n2, n3, n4);
        this.connect(n, 2, n5, n6, n7);
        this.mConstraints.get(n).horizontalBias = horizontalBias;
    }
    
    public void centerHorizontallyRtl(final int n, final int n2) {
        if (n2 == 0) {
            this.center(n, 0, 6, 0, 0, 7, 0, 0.5f);
            return;
        }
        this.center(n, n2, 7, 0, n2, 6, 0, 0.5f);
    }
    
    public void centerHorizontallyRtl(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final float horizontalBias) {
        this.connect(n, 6, n2, n3, n4);
        this.connect(n, 7, n5, n6, n7);
        this.mConstraints.get(n).horizontalBias = horizontalBias;
    }
    
    public void centerVertically(final int n, final int n2) {
        if (n2 == 0) {
            this.center(n, 0, 3, 0, 0, 4, 0, 0.5f);
            return;
        }
        this.center(n, n2, 4, 0, n2, 3, 0, 0.5f);
    }
    
    public void centerVertically(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final float verticalBias) {
        this.connect(n, 3, n2, n3, n4);
        this.connect(n, 4, n5, n6, n7);
        this.mConstraints.get(n).verticalBias = verticalBias;
    }
    
    public void clear(final int n) {
        this.mConstraints.remove(n);
    }
    
    public void clear(final int n, final int n2) {
        if (this.mConstraints.containsKey(n)) {
            final Constraint constraint = this.mConstraints.get(n);
            switch (n2) {
                default: {
                    throw new IllegalArgumentException("unknown constraint");
                }
                case 1: {
                    constraint.leftToRight = -1;
                    constraint.leftToLeft = -1;
                    constraint.leftMargin = -1;
                    constraint.goneLeftMargin = -1;
                    break;
                }
                case 2: {
                    constraint.rightToRight = -1;
                    constraint.rightToLeft = -1;
                    constraint.rightMargin = -1;
                    constraint.goneRightMargin = -1;
                }
                case 3: {
                    constraint.topToBottom = -1;
                    constraint.topToTop = -1;
                    constraint.topMargin = -1;
                    constraint.goneTopMargin = -1;
                }
                case 4: {
                    constraint.bottomToTop = -1;
                    constraint.bottomToBottom = -1;
                    constraint.bottomMargin = -1;
                    constraint.goneBottomMargin = -1;
                }
                case 5: {
                    constraint.baselineToBaseline = -1;
                }
                case 6: {
                    constraint.startToEnd = -1;
                    constraint.startToStart = -1;
                    constraint.startMargin = -1;
                    constraint.goneStartMargin = -1;
                }
                case 7: {
                    constraint.endToStart = -1;
                    constraint.endToEnd = -1;
                    constraint.endMargin = -1;
                    constraint.goneEndMargin = -1;
                }
            }
        }
    }
    
    public void clone(final Context context, final int n) {
        this.clone((ConstraintLayout)LayoutInflater.from(context).inflate(n, (ViewGroup)null));
    }
    
    public void clone(final ConstraintLayout constraintLayout) {
        final int childCount = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < childCount; ++i) {
            final View child = constraintLayout.getChildAt(i);
            final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)child.getLayoutParams();
            final int id = child.getId();
            if (!this.mConstraints.containsKey(id)) {
                this.mConstraints.put(id, new Constraint());
            }
            final Constraint constraint = this.mConstraints.get(id);
            constraint.fillFrom(id, layoutParams);
            constraint.visibility = child.getVisibility();
            if (Build$VERSION.SDK_INT >= 17) {
                constraint.alpha = child.getAlpha();
                constraint.rotationX = child.getRotationX();
                constraint.rotationY = child.getRotationY();
                constraint.scaleX = child.getScaleX();
                constraint.scaleY = child.getScaleY();
                constraint.transformPivotX = child.getPivotX();
                constraint.transformPivotY = child.getPivotY();
                constraint.translationX = child.getTranslationX();
                constraint.translationY = child.getTranslationY();
                if (Build$VERSION.SDK_INT >= 21) {
                    constraint.translationZ = child.getTranslationZ();
                    if (constraint.applyElevation) {
                        constraint.elevation = child.getElevation();
                    }
                }
            }
        }
    }
    
    public void clone(final ConstraintSet set) {
        this.mConstraints.clear();
        for (final Integer n : set.mConstraints.keySet()) {
            this.mConstraints.put(n, set.mConstraints.get(n).clone());
        }
    }
    
    public void connect(final int n, final int n2, final int endToStart, final int n3) {
        if (!this.mConstraints.containsKey(n)) {
            this.mConstraints.put(n, new Constraint());
        }
        final Constraint constraint = this.mConstraints.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException(this.sideToString(n2) + " to " + this.sideToString(n3) + " unknown");
            }
            case 1: {
                if (n3 == 1) {
                    constraint.leftToLeft = endToStart;
                    constraint.leftToRight = -1;
                    return;
                }
                if (n3 == 2) {
                    constraint.leftToRight = endToStart;
                    constraint.leftToLeft = -1;
                    return;
                }
                throw new IllegalArgumentException("left to " + this.sideToString(n3) + " undefined");
            }
            case 2: {
                if (n3 == 1) {
                    constraint.rightToLeft = endToStart;
                    constraint.rightToRight = -1;
                    return;
                }
                if (n3 == 2) {
                    constraint.rightToRight = endToStart;
                    constraint.rightToLeft = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
            case 3: {
                if (n3 == 3) {
                    constraint.topToTop = endToStart;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                }
                if (n3 == 4) {
                    constraint.topToBottom = endToStart;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
            case 4: {
                if (n3 == 4) {
                    constraint.bottomToBottom = endToStart;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                }
                if (n3 == 3) {
                    constraint.bottomToTop = endToStart;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
            case 5: {
                if (n3 == 5) {
                    constraint.baselineToBaseline = endToStart;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
            case 6: {
                if (n3 == 6) {
                    constraint.startToStart = endToStart;
                    constraint.startToEnd = -1;
                    return;
                }
                if (n3 == 7) {
                    constraint.startToEnd = endToStart;
                    constraint.startToStart = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
            case 7: {
                if (n3 == 7) {
                    constraint.endToEnd = endToStart;
                    constraint.endToStart = -1;
                    return;
                }
                if (n3 == 6) {
                    constraint.endToStart = endToStart;
                    constraint.endToEnd = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
        }
    }
    
    public void connect(final int n, final int n2, final int endToStart, final int n3, final int n4) {
        if (!this.mConstraints.containsKey(n)) {
            this.mConstraints.put(n, new Constraint());
        }
        final Constraint constraint = this.mConstraints.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException(this.sideToString(n2) + " to " + this.sideToString(n3) + " unknown");
            }
            case 1: {
                if (n3 == 1) {
                    constraint.leftToLeft = endToStart;
                    constraint.leftToRight = -1;
                }
                else {
                    if (n3 != 2) {
                        throw new IllegalArgumentException("Left to " + this.sideToString(n3) + " undefined");
                    }
                    constraint.leftToRight = endToStart;
                    constraint.leftToLeft = -1;
                }
                constraint.leftMargin = n4;
            }
            case 2: {
                if (n3 == 1) {
                    constraint.rightToLeft = endToStart;
                    constraint.rightToRight = -1;
                }
                else {
                    if (n3 != 2) {
                        throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
                    }
                    constraint.rightToRight = endToStart;
                    constraint.rightToLeft = -1;
                }
                constraint.rightMargin = n4;
            }
            case 3: {
                if (n3 == 3) {
                    constraint.topToTop = endToStart;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                }
                else {
                    if (n3 != 4) {
                        throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
                    }
                    constraint.topToBottom = endToStart;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                }
                constraint.topMargin = n4;
            }
            case 4: {
                if (n3 == 4) {
                    constraint.bottomToBottom = endToStart;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                }
                else {
                    if (n3 != 3) {
                        throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
                    }
                    constraint.bottomToTop = endToStart;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                }
                constraint.bottomMargin = n4;
            }
            case 5: {
                if (n3 == 5) {
                    constraint.baselineToBaseline = endToStart;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
            }
            case 6: {
                if (n3 == 6) {
                    constraint.startToStart = endToStart;
                    constraint.startToEnd = -1;
                }
                else {
                    if (n3 != 7) {
                        throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
                    }
                    constraint.startToEnd = endToStart;
                    constraint.startToStart = -1;
                }
                constraint.startMargin = n4;
            }
            case 7: {
                if (n3 == 7) {
                    constraint.endToEnd = endToStart;
                    constraint.endToStart = -1;
                }
                else {
                    if (n3 != 6) {
                        throw new IllegalArgumentException("right to " + this.sideToString(n3) + " undefined");
                    }
                    constraint.endToStart = endToStart;
                    constraint.endToEnd = -1;
                }
                constraint.endMargin = n4;
            }
        }
    }
    
    public void constrainDefaultHeight(final int n, final int heightDefault) {
        this.get(n).heightDefault = heightDefault;
    }
    
    public void constrainDefaultWidth(final int n, final int widthDefault) {
        this.get(n).widthDefault = widthDefault;
    }
    
    public void constrainHeight(final int n, final int mHeight) {
        this.get(n).mHeight = mHeight;
    }
    
    public void constrainMaxHeight(final int n, final int heightMax) {
        this.get(n).heightMax = heightMax;
    }
    
    public void constrainMaxWidth(final int n, final int widthMax) {
        this.get(n).widthMax = widthMax;
    }
    
    public void constrainMinHeight(final int n, final int heightMin) {
        this.get(n).heightMin = heightMin;
    }
    
    public void constrainMinWidth(final int n, final int widthMin) {
        this.get(n).widthMin = widthMin;
    }
    
    public void constrainWidth(final int n, final int mWidth) {
        this.get(n).mWidth = mWidth;
    }
    
    public void create(final int n, final int orientation) {
        final Constraint value = this.get(n);
        value.mIsGuideline = true;
        value.orientation = orientation;
    }
    
    public void createHorizontalChain(final int n, final int n2, final int n3, final int n4, final int[] array, final float[] array2, final int n5) {
        this.createHorizontalChain(n, n2, n3, n4, array, array2, n5, 1, 2);
    }
    
    public void createHorizontalChainRtl(final int n, final int n2, final int n3, final int n4, final int[] array, final float[] array2, final int n5) {
        this.createHorizontalChain(n, n2, n3, n4, array, array2, n5, 6, 7);
    }
    
    public void createVerticalChain(int i, int n, final int n2, final int n3, final int[] array, final float[] array2, final int verticalChainStyle) {
        if (array.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null && array2.length != array.length) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
        if (array2 != null) {
            this.get(array[0]).verticalWeight = array2[0];
        }
        this.get(array[0]).verticalChainStyle = verticalChainStyle;
        this.connect(array[0], 3, i, n, 0);
        for (i = 1; i < array.length; ++i) {
            n = array[i];
            this.connect(array[i], 3, array[i - 1], 4, 0);
            this.connect(array[i - 1], 4, array[i], 3, 0);
            if (array2 != null) {
                this.get(array[i]).verticalWeight = array2[i];
            }
        }
        this.connect(array[array.length - 1], 4, n2, n3, 0);
    }
    
    public boolean getApplyElevation(final int n) {
        return this.get(n).applyElevation;
    }
    
    public void load(final Context context, int n) {
    Label_0029_Outer:
        while (true) {
            final XmlResourceParser xml = context.getResources().getXml(n);
        Label_0019_Outer:
            while (true) {
            Label_0019:
                while (true) {
                    while (true) {
                        Label_0111: {
                            try {
                                n = ((XmlPullParser)xml).getEventType();
                                break Label_0111;
                                // iftrue(Label_0075:, !name.equalsIgnoreCase("Guideline"))
                                while (true) {
                                    final Constraint fillFromAttributeList;
                                    fillFromAttributeList.mIsGuideline = true;
                                    Label_0075: {
                                        break Label_0075;
                                        ((XmlPullParser)xml).getName();
                                        break Label_0019;
                                    }
                                    this.mConstraints.put(fillFromAttributeList.mViewId, fillFromAttributeList);
                                    break Label_0019;
                                    final String name = ((XmlPullParser)xml).getName();
                                    fillFromAttributeList = this.fillFromAttributeList(context, Xml.asAttributeSet((XmlPullParser)xml));
                                    continue Label_0029_Outer;
                                }
                                n = ((XmlPullParser)xml).next();
                            }
                            catch (XmlPullParserException ex) {
                                ex.printStackTrace();
                            }
                            catch (IOException ex2) {
                                ex2.printStackTrace();
                                return;
                            }
                        }
                        if (n == 1) {
                            goto Label_0101;
                        }
                        switch (n) {
                            case 1: {
                                continue Label_0019;
                            }
                            case 0: {
                                continue Label_0019_Outer;
                            }
                            case 2: {
                                continue;
                            }
                            case 3: {
                                goto Label_0102;
                                goto Label_0102;
                            }
                            default: {
                                continue Label_0019;
                            }
                        }
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    public void removeFromHorizontalChain(final int n) {
        if (this.mConstraints.containsKey(n)) {
            final Constraint constraint = this.mConstraints.get(n);
            final int leftToRight = constraint.leftToRight;
            final int rightToLeft = constraint.rightToLeft;
            if (leftToRight == -1 && rightToLeft == -1) {
                final int startToEnd = constraint.startToEnd;
                final int endToStart = constraint.endToStart;
                if (startToEnd != -1 || endToStart != -1) {
                    if (startToEnd != -1 && endToStart != -1) {
                        this.connect(startToEnd, 7, endToStart, 6, 0);
                        this.connect(endToStart, 6, leftToRight, 7, 0);
                    }
                    else if (leftToRight != -1 || endToStart != -1) {
                        if (constraint.rightToRight != -1) {
                            this.connect(leftToRight, 7, constraint.rightToRight, 7, 0);
                        }
                        else if (constraint.leftToLeft != -1) {
                            this.connect(endToStart, 6, constraint.leftToLeft, 6, 0);
                        }
                    }
                }
                this.clear(n, 6);
                this.clear(n, 7);
                return;
            }
            if (leftToRight != -1 && rightToLeft != -1) {
                this.connect(leftToRight, 2, rightToLeft, 1, 0);
                this.connect(rightToLeft, 1, leftToRight, 2, 0);
            }
            else if (leftToRight != -1 || rightToLeft != -1) {
                if (constraint.rightToRight != -1) {
                    this.connect(leftToRight, 2, constraint.rightToRight, 2, 0);
                }
                else if (constraint.leftToLeft != -1) {
                    this.connect(rightToLeft, 1, constraint.leftToLeft, 1, 0);
                }
            }
            this.clear(n, 1);
            this.clear(n, 2);
        }
    }
    
    public void removeFromVerticalChain(final int n) {
        if (this.mConstraints.containsKey(n)) {
            final Constraint constraint = this.mConstraints.get(n);
            final int topToBottom = constraint.topToBottom;
            final int bottomToTop = constraint.bottomToTop;
            if (topToBottom != -1 || bottomToTop != -1) {
                if (topToBottom != -1 && bottomToTop != -1) {
                    this.connect(topToBottom, 4, bottomToTop, 3, 0);
                    this.connect(bottomToTop, 3, topToBottom, 4, 0);
                }
                else if (topToBottom != -1 || bottomToTop != -1) {
                    if (constraint.bottomToBottom != -1) {
                        this.connect(topToBottom, 4, constraint.bottomToBottom, 4, 0);
                    }
                    else if (constraint.topToTop != -1) {
                        this.connect(bottomToTop, 3, constraint.topToTop, 3, 0);
                    }
                }
            }
        }
        this.clear(n, 3);
        this.clear(n, 4);
    }
    
    public void setAlpha(final int n, final float alpha) {
        this.get(n).alpha = alpha;
    }
    
    public void setApplyElevation(final int n, final boolean applyElevation) {
        this.get(n).applyElevation = applyElevation;
    }
    
    public void setDimensionRatio(final int n, final String dimensionRatio) {
        this.get(n).dimensionRatio = dimensionRatio;
    }
    
    public void setElevation(final int n, final float elevation) {
        this.get(n).elevation = elevation;
        this.get(n).applyElevation = true;
    }
    
    public void setGoneMargin(final int n, final int n2, final int n3) {
        final Constraint value = this.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 1: {
                value.goneLeftMargin = n3;
            }
            case 2: {
                value.goneRightMargin = n3;
            }
            case 3: {
                value.goneTopMargin = n3;
            }
            case 4: {
                value.goneBottomMargin = n3;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 6: {
                value.goneStartMargin = n3;
            }
            case 7: {
                value.goneEndMargin = n3;
            }
        }
    }
    
    public void setGuidelineBegin(final int n, final int guideBegin) {
        this.get(n).guideBegin = guideBegin;
        this.get(n).guideEnd = -1;
        this.get(n).guidePercent = -1.0f;
    }
    
    public void setGuidelineEnd(final int n, final int guideEnd) {
        this.get(n).guideEnd = guideEnd;
        this.get(n).guideBegin = -1;
        this.get(n).guidePercent = -1.0f;
    }
    
    public void setGuidelinePercent(final int n, final float guidePercent) {
        this.get(n).guidePercent = guidePercent;
        this.get(n).guideEnd = -1;
        this.get(n).guideBegin = -1;
    }
    
    public void setHorizontalBias(final int n, final float horizontalBias) {
        this.get(n).horizontalBias = horizontalBias;
    }
    
    public void setHorizontalChainStyle(final int n, final int horizontalChainStyle) {
        this.get(n).horizontalChainStyle = horizontalChainStyle;
    }
    
    public void setHorizontalWeight(final int n, final float horizontalWeight) {
        this.get(n).horizontalWeight = horizontalWeight;
    }
    
    public void setMargin(final int n, final int n2, final int n3) {
        final Constraint value = this.get(n);
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown constraint");
            }
            case 1: {
                value.leftMargin = n3;
            }
            case 2: {
                value.rightMargin = n3;
            }
            case 3: {
                value.topMargin = n3;
            }
            case 4: {
                value.bottomMargin = n3;
            }
            case 5: {
                throw new IllegalArgumentException("baseline does not support margins");
            }
            case 6: {
                value.startMargin = n3;
            }
            case 7: {
                value.endMargin = n3;
            }
        }
    }
    
    public void setRotationX(final int n, final float rotationX) {
        this.get(n).rotationX = rotationX;
    }
    
    public void setRotationY(final int n, final float rotationY) {
        this.get(n).rotationY = rotationY;
    }
    
    public void setScaleX(final int n, final float scaleX) {
        this.get(n).scaleX = scaleX;
    }
    
    public void setScaleY(final int n, final float scaleY) {
        this.get(n).scaleY = scaleY;
    }
    
    public void setTransformPivot(final int n, final float transformPivotX, final float transformPivotY) {
        final Constraint value = this.get(n);
        value.transformPivotY = transformPivotY;
        value.transformPivotX = transformPivotX;
    }
    
    public void setTransformPivotX(final int n, final float transformPivotX) {
        this.get(n).transformPivotX = transformPivotX;
    }
    
    public void setTransformPivotY(final int n, final float transformPivotY) {
        this.get(n).transformPivotY = transformPivotY;
    }
    
    public void setTranslation(final int n, final float translationX, final float translationY) {
        final Constraint value = this.get(n);
        value.translationX = translationX;
        value.translationY = translationY;
    }
    
    public void setTranslationX(final int n, final float translationX) {
        this.get(n).translationX = translationX;
    }
    
    public void setTranslationY(final int n, final float translationY) {
        this.get(n).translationY = translationY;
    }
    
    public void setTranslationZ(final int n, final float translationZ) {
        this.get(n).translationZ = translationZ;
    }
    
    public void setVerticalBias(final int n, final float verticalBias) {
        this.get(n).verticalBias = verticalBias;
    }
    
    public void setVerticalChainStyle(final int n, final int verticalChainStyle) {
        this.get(n).verticalChainStyle = verticalChainStyle;
    }
    
    public void setVerticalWeight(final int n, final float verticalWeight) {
        this.get(n).verticalWeight = verticalWeight;
    }
    
    public void setVisibility(final int n, final int visibility) {
        this.get(n).visibility = visibility;
    }
    
    private static class Constraint
    {
        static final int UNSET = -1;
        public float alpha;
        public boolean applyElevation;
        public int baselineToBaseline;
        public int bottomMargin;
        public int bottomToBottom;
        public int bottomToTop;
        public String dimensionRatio;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public float elevation;
        public int endMargin;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public int heightDefault;
        public int heightMax;
        public int heightMin;
        public float horizontalBias;
        public int horizontalChainStyle;
        public float horizontalWeight;
        public int leftMargin;
        public int leftToLeft;
        public int leftToRight;
        public int mHeight;
        boolean mIsGuideline;
        int mViewId;
        public int mWidth;
        public int orientation;
        public int rightMargin;
        public int rightToLeft;
        public int rightToRight;
        public float rotationX;
        public float rotationY;
        public float scaleX;
        public float scaleY;
        public int startMargin;
        public int startToEnd;
        public int startToStart;
        public int topMargin;
        public int topToBottom;
        public int topToTop;
        public float transformPivotX;
        public float transformPivotY;
        public float translationX;
        public float translationY;
        public float translationZ;
        public float verticalBias;
        public int verticalChainStyle;
        public float verticalWeight;
        public int visibility;
        public int widthDefault;
        public int widthMax;
        public int widthMin;
        
        private Constraint() {
            this.mIsGuideline = false;
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = -1.0f;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = null;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
            this.leftMargin = -1;
            this.rightMargin = -1;
            this.topMargin = -1;
            this.bottomMargin = -1;
            this.endMargin = -1;
            this.startMargin = -1;
            this.visibility = 0;
            this.goneLeftMargin = -1;
            this.goneTopMargin = -1;
            this.goneRightMargin = -1;
            this.goneBottomMargin = -1;
            this.goneEndMargin = -1;
            this.goneStartMargin = -1;
            this.verticalWeight = 0.0f;
            this.horizontalWeight = 0.0f;
            this.horizontalChainStyle = 0;
            this.verticalChainStyle = 0;
            this.alpha = 1.0f;
            this.applyElevation = false;
            this.elevation = 0.0f;
            this.rotationX = 0.0f;
            this.rotationY = 0.0f;
            this.scaleX = 1.0f;
            this.scaleY = 1.0f;
            this.transformPivotX = 0.0f;
            this.transformPivotY = 0.0f;
            this.translationX = 0.0f;
            this.translationY = 0.0f;
            this.translationZ = 0.0f;
            this.widthDefault = -1;
            this.heightDefault = -1;
            this.widthMax = -1;
            this.heightMax = -1;
            this.widthMin = -1;
            this.heightMin = -1;
        }
        
        private void fillFrom(final int mViewId, final ConstraintLayout.LayoutParams layoutParams) {
            this.mViewId = mViewId;
            this.leftToLeft = layoutParams.leftToLeft;
            this.leftToRight = layoutParams.leftToRight;
            this.rightToLeft = layoutParams.rightToLeft;
            this.rightToRight = layoutParams.rightToRight;
            this.topToTop = layoutParams.topToTop;
            this.topToBottom = layoutParams.topToBottom;
            this.bottomToTop = layoutParams.bottomToTop;
            this.bottomToBottom = layoutParams.bottomToBottom;
            this.baselineToBaseline = layoutParams.baselineToBaseline;
            this.startToEnd = layoutParams.startToEnd;
            this.startToStart = layoutParams.startToStart;
            this.endToStart = layoutParams.endToStart;
            this.endToEnd = layoutParams.endToEnd;
            this.horizontalBias = layoutParams.horizontalBias;
            this.verticalBias = layoutParams.verticalBias;
            this.dimensionRatio = layoutParams.dimensionRatio;
            this.editorAbsoluteX = layoutParams.editorAbsoluteX;
            this.editorAbsoluteY = layoutParams.editorAbsoluteY;
            this.orientation = layoutParams.orientation;
            this.guidePercent = layoutParams.guidePercent;
            this.guideBegin = layoutParams.guideBegin;
            this.guideEnd = layoutParams.guideEnd;
            this.mWidth = layoutParams.width;
            this.mHeight = layoutParams.height;
            this.leftMargin = layoutParams.leftMargin;
            this.rightMargin = layoutParams.rightMargin;
            this.topMargin = layoutParams.topMargin;
            this.bottomMargin = layoutParams.bottomMargin;
            this.verticalWeight = layoutParams.verticalWeight;
            this.horizontalWeight = layoutParams.horizontalWeight;
            this.verticalChainStyle = layoutParams.verticalChainStyle;
            this.horizontalChainStyle = layoutParams.horizontalChainStyle;
            this.widthDefault = layoutParams.matchConstraintDefaultWidth;
            this.heightDefault = layoutParams.matchConstraintDefaultHeight;
            this.widthMax = layoutParams.matchConstraintMaxWidth;
            this.heightMax = layoutParams.matchConstraintMaxHeight;
            this.widthMin = layoutParams.matchConstraintMinWidth;
            this.heightMin = layoutParams.matchConstraintMinHeight;
            if (Build$VERSION.SDK_INT >= 17) {
                this.endMargin = layoutParams.getMarginEnd();
                this.startMargin = layoutParams.getMarginStart();
            }
        }
        
        public void applyTo(final ConstraintLayout.LayoutParams layoutParams) {
            layoutParams.leftToLeft = this.leftToLeft;
            layoutParams.leftToRight = this.leftToRight;
            layoutParams.rightToLeft = this.rightToLeft;
            layoutParams.rightToRight = this.rightToRight;
            layoutParams.topToTop = this.topToTop;
            layoutParams.topToBottom = this.topToBottom;
            layoutParams.bottomToTop = this.bottomToTop;
            layoutParams.bottomToBottom = this.bottomToBottom;
            layoutParams.baselineToBaseline = this.baselineToBaseline;
            layoutParams.startToEnd = this.startToEnd;
            layoutParams.startToStart = this.startToStart;
            layoutParams.endToStart = this.endToStart;
            layoutParams.endToEnd = this.endToEnd;
            layoutParams.leftMargin = this.leftMargin;
            layoutParams.rightMargin = this.rightMargin;
            layoutParams.topMargin = this.topMargin;
            layoutParams.bottomMargin = this.bottomMargin;
            layoutParams.goneStartMargin = this.goneStartMargin;
            layoutParams.goneEndMargin = this.goneEndMargin;
            layoutParams.horizontalBias = this.horizontalBias;
            layoutParams.verticalBias = this.verticalBias;
            layoutParams.dimensionRatio = this.dimensionRatio;
            layoutParams.editorAbsoluteX = this.editorAbsoluteX;
            layoutParams.editorAbsoluteY = this.editorAbsoluteY;
            layoutParams.verticalWeight = this.verticalWeight;
            layoutParams.horizontalWeight = this.horizontalWeight;
            layoutParams.verticalChainStyle = this.verticalChainStyle;
            layoutParams.horizontalChainStyle = this.horizontalChainStyle;
            layoutParams.matchConstraintDefaultWidth = this.widthDefault;
            layoutParams.matchConstraintDefaultHeight = this.heightDefault;
            layoutParams.matchConstraintMaxWidth = this.widthMax;
            layoutParams.matchConstraintMaxHeight = this.heightMax;
            layoutParams.matchConstraintMinWidth = this.widthMin;
            layoutParams.matchConstraintMinHeight = this.heightMin;
            layoutParams.orientation = this.orientation;
            layoutParams.guidePercent = this.guidePercent;
            layoutParams.guideBegin = this.guideBegin;
            layoutParams.guideEnd = this.guideEnd;
            layoutParams.width = this.mWidth;
            layoutParams.height = this.mHeight;
            if (Build$VERSION.SDK_INT >= 17) {
                layoutParams.setMarginStart(this.startMargin);
                layoutParams.setMarginEnd(this.endMargin);
            }
            layoutParams.validate();
        }
        
        public Constraint clone() {
            final Constraint constraint = new Constraint();
            constraint.mIsGuideline = this.mIsGuideline;
            constraint.mWidth = this.mWidth;
            constraint.mHeight = this.mHeight;
            constraint.guideBegin = this.guideBegin;
            constraint.guideEnd = this.guideEnd;
            constraint.guidePercent = this.guidePercent;
            constraint.leftToLeft = this.leftToLeft;
            constraint.leftToRight = this.leftToRight;
            constraint.rightToLeft = this.rightToLeft;
            constraint.rightToRight = this.rightToRight;
            constraint.topToTop = this.topToTop;
            constraint.topToBottom = this.topToBottom;
            constraint.bottomToTop = this.bottomToTop;
            constraint.bottomToBottom = this.bottomToBottom;
            constraint.baselineToBaseline = this.baselineToBaseline;
            constraint.startToEnd = this.startToEnd;
            constraint.startToStart = this.startToStart;
            constraint.endToStart = this.endToStart;
            constraint.endToEnd = this.endToEnd;
            constraint.horizontalBias = this.horizontalBias;
            constraint.verticalBias = this.verticalBias;
            constraint.dimensionRatio = this.dimensionRatio;
            constraint.editorAbsoluteX = this.editorAbsoluteX;
            constraint.editorAbsoluteY = this.editorAbsoluteY;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.horizontalBias = this.horizontalBias;
            constraint.orientation = this.orientation;
            constraint.leftMargin = this.leftMargin;
            constraint.rightMargin = this.rightMargin;
            constraint.topMargin = this.topMargin;
            constraint.bottomMargin = this.bottomMargin;
            constraint.endMargin = this.endMargin;
            constraint.startMargin = this.startMargin;
            constraint.visibility = this.visibility;
            constraint.goneLeftMargin = this.goneLeftMargin;
            constraint.goneTopMargin = this.goneTopMargin;
            constraint.goneRightMargin = this.goneRightMargin;
            constraint.goneBottomMargin = this.goneBottomMargin;
            constraint.goneEndMargin = this.goneEndMargin;
            constraint.goneStartMargin = this.goneStartMargin;
            constraint.verticalWeight = this.verticalWeight;
            constraint.horizontalWeight = this.horizontalWeight;
            constraint.horizontalChainStyle = this.horizontalChainStyle;
            constraint.verticalChainStyle = this.verticalChainStyle;
            constraint.alpha = this.alpha;
            constraint.applyElevation = this.applyElevation;
            constraint.elevation = this.elevation;
            constraint.rotationX = this.rotationX;
            constraint.rotationY = this.rotationY;
            constraint.scaleX = this.scaleX;
            constraint.scaleY = this.scaleY;
            constraint.transformPivotX = this.transformPivotX;
            constraint.transformPivotY = this.transformPivotY;
            constraint.translationX = this.translationX;
            constraint.translationY = this.translationY;
            constraint.translationZ = this.translationZ;
            constraint.widthDefault = this.widthDefault;
            constraint.heightDefault = this.heightDefault;
            constraint.widthMax = this.widthMax;
            constraint.heightMax = this.heightMax;
            constraint.widthMin = this.widthMin;
            constraint.heightMin = this.heightMin;
            return constraint;
        }
    }
}
