// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.graphics.PathMeasure;
import android.graphics.Path;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.res.XmlResourceParser;
import android.content.res.Resources$NotFoundException;
import android.animation.AnimatorInflater;
import android.os.Build$VERSION;
import android.support.annotation.AnimatorRes;
import android.util.TypedValue;
import android.view.InflateException;
import android.animation.TypeEvaluator;
import android.support.v4.graphics.PathParser;
import android.util.Log;
import android.animation.Keyframe;
import android.animation.PropertyValuesHolder;
import android.content.res.TypedArray;
import java.util.Iterator;
import java.util.ArrayList;
import android.support.v4.content.res.TypedArrayUtils;
import android.animation.ValueAnimator;
import android.util.AttributeSet;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.animation.AnimatorSet;
import android.util.Xml;
import android.animation.Animator;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AnimatorInflaterCompat
{
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;
    
    private static Animator createAnimatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final float n) throws XmlPullParserException, IOException {
        return createAnimatorFromXml(context, resources, resources$Theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser), null, 0, n);
    }
    
    private static Animator createAnimatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final AttributeSet set, final AnimatorSet set2, final int n, final float n2) throws XmlPullParserException, IOException {
        Object o = null;
        ArrayList<AnimatorSet> list = null;
        final int depth = xmlPullParser.getDepth();
        while (true) {
            final int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                if (set2 != null && list != null) {
                    final Animator[] array = new Animator[list.size()];
                    int n3 = 0;
                    final Iterator<AnimatorSet> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        array[n3] = (Animator)iterator.next();
                        ++n3;
                    }
                    if (n != 0) {
                        set2.playSequentially(array);
                        return (Animator)o;
                    }
                    set2.playTogether(array);
                }
                return (Animator)o;
            }
            if (next != 2) {
                continue;
            }
            final String name = xmlPullParser.getName();
            int n4 = 0;
            Object o2;
            if (name.equals("objectAnimator")) {
                o2 = loadObjectAnimator(context, resources, resources$Theme, set, n2, xmlPullParser);
            }
            else if (name.equals("animator")) {
                o2 = loadAnimator(context, resources, resources$Theme, set, null, n2, xmlPullParser);
            }
            else if (name.equals("set")) {
                o2 = new AnimatorSet();
                final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATOR_SET);
                createAnimatorFromXml(context, resources, resources$Theme, xmlPullParser, set, (AnimatorSet)o2, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "ordering", 0, 0), n2);
                obtainAttributes.recycle();
            }
            else {
                if (!name.equals("propertyValuesHolder")) {
                    throw new RuntimeException("Unknown animator name: " + xmlPullParser.getName());
                }
                final PropertyValuesHolder[] loadValues = loadValues(context, resources, resources$Theme, xmlPullParser, Xml.asAttributeSet(xmlPullParser));
                if (loadValues != null && o != null && o instanceof ValueAnimator) {
                    ((ValueAnimator)o).setValues(loadValues);
                }
                n4 = 1;
                o2 = o;
            }
            o = o2;
            if (set2 == null) {
                continue;
            }
            o = o2;
            if (n4 != 0) {
                continue;
            }
            ArrayList<AnimatorSet> list2;
            if ((list2 = list) == null) {
                list2 = new ArrayList<AnimatorSet>();
            }
            list2.add((AnimatorSet)o2);
            o = o2;
            list = list2;
        }
    }
    
    private static Keyframe createNewKeyframe(final Keyframe keyframe, final float n) {
        if (keyframe.getType() == Float.TYPE) {
            return Keyframe.ofFloat(n);
        }
        if (keyframe.getType() == Integer.TYPE) {
            return Keyframe.ofInt(n);
        }
        return Keyframe.ofObject(n);
    }
    
    private static void distributeKeyframes(final Keyframe[] array, float n, int i, final int n2) {
        n /= n2 - i + 2;
        while (i <= n2) {
            array[i].setFraction(array[i - 1].getFraction() + n);
            ++i;
        }
    }
    
    private static void dumpKeyframes(final Object[] array, String s) {
        if (array != null && array.length != 0) {
            Log.d("AnimatorInflater", s);
            for (int length = array.length, i = 0; i < length; ++i) {
                final Keyframe keyframe = (Keyframe)array[i];
                final StringBuilder append = new StringBuilder().append("Keyframe ").append(i).append(": fraction ");
                if (keyframe.getFraction() < 0.0f) {
                    s = "null";
                }
                else {
                    s = (String)keyframe.getFraction();
                }
                final StringBuilder append2 = append.append((Object)s).append(", ").append(", value : ");
                if (keyframe.hasValue()) {
                    s = (String)keyframe.getValue();
                }
                else {
                    s = "null";
                }
                Log.d("AnimatorInflater", append2.append((Object)s).toString());
            }
        }
    }
    
    private static PropertyValuesHolder getPVH(final TypedArray typedArray, int n, int n2, final int n3, final String s) {
        final TypedValue peekValue = typedArray.peekValue(n2);
        boolean b;
        if (peekValue != null) {
            b = true;
        }
        else {
            b = false;
        }
        int type;
        if (b) {
            type = peekValue.type;
        }
        else {
            type = 0;
        }
        final TypedValue peekValue2 = typedArray.peekValue(n3);
        boolean b2;
        if (peekValue2 != null) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        int type2;
        if (b2) {
            type2 = peekValue2.type;
        }
        else {
            type2 = 0;
        }
        int n4 = n;
        if (n == 4) {
            if ((b && isColorType(type)) || (b2 && isColorType(type2))) {
                n4 = 3;
            }
            else {
                n4 = 0;
            }
        }
        if (n4 == 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        final PropertyValuesHolder propertyValuesHolder = null;
        final PropertyValuesHolder propertyValuesHolder2 = null;
        PropertyValuesHolder ofObject;
        if (n4 == 2) {
            final String string = typedArray.getString(n2);
            final String string2 = typedArray.getString(n3);
            final PathParser.PathDataNode[] nodesFromPathData = PathParser.createNodesFromPathData(string);
            final PathParser.PathDataNode[] nodesFromPathData2 = PathParser.createNodesFromPathData(string2);
            if (nodesFromPathData == null) {
                ofObject = propertyValuesHolder2;
                if (nodesFromPathData2 == null) {
                    return ofObject;
                }
            }
            if (nodesFromPathData != null) {
                final PathDataEvaluator pathDataEvaluator = new PathDataEvaluator();
                if (nodesFromPathData2 == null) {
                    return PropertyValuesHolder.ofObject(s, (TypeEvaluator)pathDataEvaluator, new Object[] { nodesFromPathData });
                }
                if (!PathParser.canMorph(nodesFromPathData, nodesFromPathData2)) {
                    throw new InflateException(" Can't morph from " + string + " to " + string2);
                }
                ofObject = PropertyValuesHolder.ofObject(s, (TypeEvaluator)pathDataEvaluator, new Object[] { nodesFromPathData, nodesFromPathData2 });
            }
            else {
                ofObject = propertyValuesHolder2;
                if (nodesFromPathData2 != null) {
                    return PropertyValuesHolder.ofObject(s, (TypeEvaluator)new PathDataEvaluator(), new Object[] { nodesFromPathData2 });
                }
            }
        }
        else {
            Object instance = null;
            if (n4 == 3) {
                instance = ArgbEvaluator.getInstance();
            }
            PropertyValuesHolder propertyValuesHolder3;
            if (n != 0) {
                if (b) {
                    float n5;
                    if (type == 5) {
                        n5 = typedArray.getDimension(n2, 0.0f);
                    }
                    else {
                        n5 = typedArray.getFloat(n2, 0.0f);
                    }
                    if (b2) {
                        float n6;
                        if (type2 == 5) {
                            n6 = typedArray.getDimension(n3, 0.0f);
                        }
                        else {
                            n6 = typedArray.getFloat(n3, 0.0f);
                        }
                        propertyValuesHolder3 = PropertyValuesHolder.ofFloat(s, new float[] { n5, n6 });
                    }
                    else {
                        propertyValuesHolder3 = PropertyValuesHolder.ofFloat(s, new float[] { n5 });
                    }
                }
                else {
                    float n7;
                    if (type2 == 5) {
                        n7 = typedArray.getDimension(n3, 0.0f);
                    }
                    else {
                        n7 = typedArray.getFloat(n3, 0.0f);
                    }
                    propertyValuesHolder3 = PropertyValuesHolder.ofFloat(s, new float[] { n7 });
                }
            }
            else if (b) {
                if (type == 5) {
                    n = (int)typedArray.getDimension(n2, 0.0f);
                }
                else if (isColorType(type)) {
                    n = typedArray.getColor(n2, 0);
                }
                else {
                    n = typedArray.getInt(n2, 0);
                }
                if (b2) {
                    if (type2 == 5) {
                        n2 = (int)typedArray.getDimension(n3, 0.0f);
                    }
                    else if (isColorType(type2)) {
                        n2 = typedArray.getColor(n3, 0);
                    }
                    else {
                        n2 = typedArray.getInt(n3, 0);
                    }
                    propertyValuesHolder3 = PropertyValuesHolder.ofInt(s, new int[] { n, n2 });
                }
                else {
                    propertyValuesHolder3 = PropertyValuesHolder.ofInt(s, new int[] { n });
                }
            }
            else {
                propertyValuesHolder3 = propertyValuesHolder;
                if (b2) {
                    if (type2 == 5) {
                        n = (int)typedArray.getDimension(n3, 0.0f);
                    }
                    else if (isColorType(type2)) {
                        n = typedArray.getColor(n3, 0);
                    }
                    else {
                        n = typedArray.getInt(n3, 0);
                    }
                    propertyValuesHolder3 = PropertyValuesHolder.ofInt(s, new int[] { n });
                }
            }
            if ((ofObject = propertyValuesHolder3) != null) {
                ofObject = propertyValuesHolder3;
                if (instance != null) {
                    propertyValuesHolder3.setEvaluator((TypeEvaluator)instance);
                    return propertyValuesHolder3;
                }
            }
        }
        return ofObject;
    }
    
    private static int inferValueTypeFromValues(final TypedArray typedArray, int n, int n2) {
        final int n3 = 1;
        final TypedValue peekValue = typedArray.peekValue(n);
        if (peekValue != null) {
            n = 1;
        }
        else {
            n = 0;
        }
        int type;
        if (n != 0) {
            type = peekValue.type;
        }
        else {
            type = 0;
        }
        final TypedValue peekValue2 = typedArray.peekValue(n2);
        if (peekValue2 != null) {
            n2 = n3;
        }
        else {
            n2 = 0;
        }
        int type2;
        if (n2 != 0) {
            type2 = peekValue2.type;
        }
        else {
            type2 = 0;
        }
        if ((n != 0 && isColorType(type)) || (n2 != 0 && isColorType(type2))) {
            return 3;
        }
        return 0;
    }
    
    private static int inferValueTypeOfKeyframe(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final XmlPullParser xmlPullParser) {
        boolean b = false;
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_KEYFRAME);
        final TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        if (peekNamedValue != null) {
            b = true;
        }
        int n;
        if (b && isColorType(peekNamedValue.type)) {
            n = 3;
        }
        else {
            n = 0;
        }
        obtainAttributes.recycle();
        return n;
    }
    
    private static boolean isColorType(final int n) {
        return n >= 28 && n <= 31;
    }
    
    public static Animator loadAnimator(final Context context, @AnimatorRes final int n) throws Resources$NotFoundException {
        if (Build$VERSION.SDK_INT >= 24) {
            return AnimatorInflater.loadAnimator(context, n);
        }
        return loadAnimator(context, context.getResources(), context.getTheme(), n);
    }
    
    public static Animator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, @AnimatorRes final int n) throws Resources$NotFoundException {
        return loadAnimator(context, resources, resources$Theme, n, 1.0f);
    }
    
    public static Animator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, @AnimatorRes final int n, final float n2) throws Resources$NotFoundException {
        XmlResourceParser xmlResourceParser = null;
        XmlResourceParser xmlResourceParser2 = null;
        XmlResourceParser animation = null;
        try {
            return createAnimatorFromXml(context, resources, resources$Theme, (XmlPullParser)(xmlResourceParser2 = (xmlResourceParser = (animation = resources.getAnimation(n)))), n2);
        }
        catch (XmlPullParserException ex2) {
            xmlResourceParser = animation;
            final Resources$NotFoundException ex = new Resources$NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(n));
            xmlResourceParser = animation;
            ex.initCause((Throwable)ex2);
            xmlResourceParser = animation;
            throw ex;
        }
        catch (IOException ex4) {
            xmlResourceParser = xmlResourceParser2;
            final Resources$NotFoundException ex3 = new Resources$NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(n));
            xmlResourceParser = xmlResourceParser2;
            ex3.initCause((Throwable)ex4);
            xmlResourceParser = xmlResourceParser2;
            throw ex3;
        }
        finally {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }
    
    private static ValueAnimator loadAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final ValueAnimator valueAnimator, final float n, final XmlPullParser xmlPullParser) throws Resources$NotFoundException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATOR);
        final TypedArray obtainAttributes2 = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        ValueAnimator valueAnimator2 = valueAnimator;
        if (valueAnimator == null) {
            valueAnimator2 = new ValueAnimator();
        }
        parseAnimatorFromTypeArray(valueAnimator2, obtainAttributes, obtainAttributes2, n, xmlPullParser);
        final int namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 0, 0);
        if (namedResourceId > 0) {
            valueAnimator2.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        if (obtainAttributes2 != null) {
            obtainAttributes2.recycle();
        }
        return valueAnimator2;
    }
    
    private static Keyframe loadKeyframe(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, int namedResourceId, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_KEYFRAME);
        final Keyframe keyframe = null;
        final float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "fraction", 3, -1.0f);
        final TypedValue peekNamedValue = TypedArrayUtils.peekNamedValue(obtainAttributes, xmlPullParser, "value", 0);
        boolean b;
        if (peekNamedValue != null) {
            b = true;
        }
        else {
            b = false;
        }
        int n = namedResourceId;
        if (namedResourceId == 4) {
            if (b && isColorType(peekNamedValue.type)) {
                n = 3;
            }
            else {
                n = 0;
            }
        }
        Keyframe keyframe2;
        if (b) {
            keyframe2 = keyframe;
            switch (n) {
                default: {
                    keyframe2 = keyframe;
                    break;
                }
                case 1:
                case 3: {
                    keyframe2 = Keyframe.ofInt(namedFloat, TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "value", 0, 0));
                    break;
                }
                case 0: {
                    keyframe2 = Keyframe.ofFloat(namedFloat, TypedArrayUtils.getNamedFloat(obtainAttributes, xmlPullParser, "value", 0, 0.0f));
                }
                case 2: {
                    break;
                }
            }
        }
        else if (n == 0) {
            keyframe2 = Keyframe.ofFloat(namedFloat);
        }
        else {
            keyframe2 = Keyframe.ofInt(namedFloat);
        }
        namedResourceId = TypedArrayUtils.getNamedResourceId(obtainAttributes, xmlPullParser, "interpolator", 1, 0);
        if (namedResourceId > 0) {
            keyframe2.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, namedResourceId));
        }
        obtainAttributes.recycle();
        return keyframe2;
    }
    
    private static ObjectAnimator loadObjectAnimator(final Context context, final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final float n, final XmlPullParser xmlPullParser) throws Resources$NotFoundException {
        final ObjectAnimator objectAnimator = new ObjectAnimator();
        loadAnimator(context, resources, resources$Theme, set, (ValueAnimator)objectAnimator, n, xmlPullParser);
        return objectAnimator;
    }
    
    private static PropertyValuesHolder loadPvh(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final String s, int i) throws XmlPullParserException, IOException {
        final PropertyValuesHolder propertyValuesHolder = null;
        ArrayList<Keyframe> list = null;
        int n = i;
        while (true) {
            i = xmlPullParser.next();
            if (i == 3 || i == 1) {
                break;
            }
            if (!xmlPullParser.getName().equals("keyframe")) {
                continue;
            }
            if ((i = n) == 4) {
                i = inferValueTypeOfKeyframe(resources, resources$Theme, Xml.asAttributeSet(xmlPullParser), xmlPullParser);
            }
            final Keyframe loadKeyframe = loadKeyframe(context, resources, resources$Theme, Xml.asAttributeSet(xmlPullParser), i, xmlPullParser);
            ArrayList<Keyframe> list2 = list;
            if (loadKeyframe != null) {
                if ((list2 = list) == null) {
                    list2 = new ArrayList<Keyframe>();
                }
                list2.add(loadKeyframe);
            }
            xmlPullParser.next();
            list = list2;
            n = i;
        }
        PropertyValuesHolder ofKeyframe = propertyValuesHolder;
        if (list != null) {
            final int size = list.size();
            ofKeyframe = propertyValuesHolder;
            if (size > 0) {
                final Keyframe keyframe = list.get(0);
                final Keyframe keyframe2 = list.get(size - 1);
                final float fraction = keyframe2.getFraction();
                i = size;
                if (fraction < 1.0f) {
                    if (fraction < 0.0f) {
                        keyframe2.setFraction(1.0f);
                        i = size;
                    }
                    else {
                        list.add(list.size(), createNewKeyframe(keyframe2, 1.0f));
                        i = size + 1;
                    }
                }
                final float fraction2 = keyframe.getFraction();
                int n2 = i;
                if (fraction2 != 0.0f) {
                    if (fraction2 < 0.0f) {
                        keyframe.setFraction(0.0f);
                        n2 = i;
                    }
                    else {
                        list.add(0, createNewKeyframe(keyframe, 0.0f));
                        n2 = i + 1;
                    }
                }
                final Keyframe[] array = new Keyframe[n2];
                list.toArray(array);
                Keyframe keyframe3;
                int n3;
                int n4;
                for (i = 0; i < n2; ++i) {
                    keyframe3 = array[i];
                    if (keyframe3.getFraction() < 0.0f) {
                        if (i == 0) {
                            keyframe3.setFraction(0.0f);
                        }
                        else if (i == n2 - 1) {
                            keyframe3.setFraction(1.0f);
                        }
                        else {
                            n3 = i;
                            for (n4 = i + 1; n4 < n2 - 1 && array[n4].getFraction() < 0.0f; ++n4) {
                                n3 = n4;
                            }
                            distributeKeyframes(array, array[n3 + 1].getFraction() - array[i - 1].getFraction(), i, n3);
                        }
                    }
                }
                final PropertyValuesHolder propertyValuesHolder2 = ofKeyframe = PropertyValuesHolder.ofKeyframe(s, array);
                if (n == 3) {
                    propertyValuesHolder2.setEvaluator((TypeEvaluator)ArgbEvaluator.getInstance());
                    ofKeyframe = propertyValuesHolder2;
                }
            }
        }
        return ofKeyframe;
    }
    
    private static PropertyValuesHolder[] loadValues(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser, final AttributeSet set) throws XmlPullParserException, IOException {
        ArrayList<PropertyValuesHolder> list = null;
        while (true) {
            final int eventType = xmlPullParser.getEventType();
            if (eventType == 3 || eventType == 1) {
                break;
            }
            if (eventType != 2) {
                xmlPullParser.next();
            }
            else {
                ArrayList<PropertyValuesHolder> list2 = list;
                if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                    final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                    final String namedString = TypedArrayUtils.getNamedString(obtainAttributes, xmlPullParser, "propertyName", 3);
                    final int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, xmlPullParser, "valueType", 2, 4);
                    PropertyValuesHolder propertyValuesHolder;
                    if ((propertyValuesHolder = loadPvh(context, resources, resources$Theme, xmlPullParser, namedString, namedInt)) == null) {
                        propertyValuesHolder = getPVH(obtainAttributes, namedInt, 0, 1, namedString);
                    }
                    list2 = list;
                    if (propertyValuesHolder != null) {
                        if ((list2 = list) == null) {
                            list2 = new ArrayList<PropertyValuesHolder>();
                        }
                        list2.add(propertyValuesHolder);
                    }
                    obtainAttributes.recycle();
                }
                xmlPullParser.next();
                list = list2;
            }
        }
        PropertyValuesHolder[] array = null;
        if (list != null) {
            final int size = list.size();
            final PropertyValuesHolder[] array2 = new PropertyValuesHolder[size];
            int n = 0;
            while (true) {
                array = array2;
                if (n >= size) {
                    break;
                }
                array2[n] = list.get(n);
                ++n;
            }
        }
        return array;
    }
    
    private static void parseAnimatorFromTypeArray(final ValueAnimator valueAnimator, final TypedArray typedArray, final TypedArray typedArray2, final float n, final XmlPullParser xmlPullParser) {
        final long duration = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        final long startDelay = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int namedInt;
        final int n2 = namedInt = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom")) {
            namedInt = n2;
            if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
                int inferValueTypeFromValues;
                if ((inferValueTypeFromValues = n2) == 4) {
                    inferValueTypeFromValues = inferValueTypeFromValues(typedArray, 5, 6);
                }
                final PropertyValuesHolder pvh = getPVH(typedArray, inferValueTypeFromValues, 5, 6, "");
                namedInt = inferValueTypeFromValues;
                if (pvh != null) {
                    valueAnimator.setValues(new PropertyValuesHolder[] { pvh });
                    namedInt = inferValueTypeFromValues;
                }
            }
        }
        valueAnimator.setDuration(duration);
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            setupObjectAnimator(valueAnimator, typedArray2, namedInt, n, xmlPullParser);
        }
    }
    
    private static void setupObjectAnimator(final ValueAnimator valueAnimator, final TypedArray typedArray, final int n, final float n2, final XmlPullParser xmlPullParser) {
        final ObjectAnimator objectAnimator = (ObjectAnimator)valueAnimator;
        final String namedString = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "pathData", 1);
        if (namedString == null) {
            objectAnimator.setPropertyName(TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 0));
            return;
        }
        final String namedString2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyXName", 2);
        final String namedString3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyYName", 3);
        if (n == 2 || n == 4) {}
        if (namedString2 == null && namedString3 == null) {
            throw new InflateException(typedArray.getPositionDescription() + " propertyXName or propertyYName is needed for PathData");
        }
        setupPathMotion(PathParser.createPathFromPathData(namedString), objectAnimator, 0.5f * n2, namedString2, namedString3);
    }
    
    private static void setupPathMotion(final Path path, final ObjectAnimator objectAnimator, float n, final String s, final String s2) {
        final PathMeasure pathMeasure = new PathMeasure(path, false);
        float n2 = 0.0f;
        final ArrayList<Float> list = new ArrayList<Float>();
        list.add(0.0f);
        float n3;
        do {
            n3 = n2 + pathMeasure.getLength();
            list.add(n3);
            n2 = n3;
        } while (pathMeasure.nextContour());
        final PathMeasure pathMeasure2 = new PathMeasure(path, false);
        final int min = Math.min(100, (int)(n3 / n) + 1);
        final float[] array = new float[min];
        final float[] array2 = new float[min];
        final float[] array3 = new float[2];
        int n4 = 0;
        final float n5 = n3 / (min - 1);
        n = 0.0f;
        int n7;
        for (int i = 0; i < min; ++i, n4 = n7) {
            pathMeasure2.getPosTan(n, array3, (float[])null);
            pathMeasure2.getPosTan(n, array3, (float[])null);
            array[i] = array3[0];
            array2[i] = array3[1];
            final float n6 = n + n5;
            n7 = n4;
            n = n6;
            if (n4 + 1 < list.size()) {
                n7 = n4;
                n = n6;
                if (n6 > list.get(n4 + 1)) {
                    n = n6 - list.get(n4 + 1);
                    n7 = n4 + 1;
                    pathMeasure2.nextContour();
                }
            }
        }
        PropertyValuesHolder ofFloat = null;
        final PropertyValuesHolder propertyValuesHolder = null;
        if (s != null) {
            ofFloat = PropertyValuesHolder.ofFloat(s, array);
        }
        PropertyValuesHolder ofFloat2 = propertyValuesHolder;
        if (s2 != null) {
            ofFloat2 = PropertyValuesHolder.ofFloat(s2, array2);
        }
        if (ofFloat == null) {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat2 });
            return;
        }
        if (ofFloat2 == null) {
            objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat });
            return;
        }
        objectAnimator.setValues(new PropertyValuesHolder[] { ofFloat, ofFloat2 });
    }
    
    private static class PathDataEvaluator implements TypeEvaluator<PathParser.PathDataNode[]>
    {
        private PathParser.PathDataNode[] mNodeArray;
        
        private PathDataEvaluator() {
        }
        
        PathDataEvaluator(final PathParser.PathDataNode[] mNodeArray) {
            this.mNodeArray = mNodeArray;
        }
        
        public PathParser.PathDataNode[] evaluate(final float n, final PathParser.PathDataNode[] array, final PathParser.PathDataNode[] array2) {
            if (!PathParser.canMorph(array, array2)) {
                throw new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            }
            if (this.mNodeArray == null || !PathParser.canMorph(this.mNodeArray, array)) {
                this.mNodeArray = PathParser.deepCopyNodes(array);
            }
            for (int i = 0; i < array.length; ++i) {
                this.mNodeArray[i].interpolatePathDataNode(array[i], array2[i], n);
            }
            return this.mNodeArray;
        }
    }
}
