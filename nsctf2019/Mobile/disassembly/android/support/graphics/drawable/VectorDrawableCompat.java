// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.graphics.drawable.VectorDrawable;
import android.support.annotation.RequiresApi;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.graphics.Path$FillType;
import android.graphics.Paint$Style;
import android.graphics.PathMeasure;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.util.ArrayMap;
import java.util.ArrayList;
import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import android.support.v4.graphics.PathParser;
import android.graphics.Region;
import android.support.annotation.RestrictTo;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.content.res.ColorStateList;
import android.support.v4.content.res.TypedArrayUtils;
import android.content.res.TypedArray;
import java.util.Stack;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import java.io.IOException;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.support.v4.content.res.ResourcesCompat;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.annotation.DrawableRes;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.PorterDuffColorFilter;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.PorterDuff$Mode;

public class VectorDrawableCompat extends VectorDrawableCommon
{
    private static final boolean DBG_VECTOR_DRAWABLE = false;
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private static final int LINECAP_BUTT = 0;
    private static final int LINECAP_ROUND = 1;
    private static final int LINECAP_SQUARE = 2;
    private static final int LINEJOIN_BEVEL = 2;
    private static final int LINEJOIN_MITER = 0;
    private static final int LINEJOIN_ROUND = 1;
    static final String LOGTAG = "VectorDrawableCompat";
    private static final int MAX_CACHED_BITMAP_SIZE = 2048;
    private static final String SHAPE_CLIP_PATH = "clip-path";
    private static final String SHAPE_GROUP = "group";
    private static final String SHAPE_PATH = "path";
    private static final String SHAPE_VECTOR = "vector";
    private boolean mAllowCaching;
    private Drawable$ConstantState mCachedConstantStateDelegate;
    private ColorFilter mColorFilter;
    private boolean mMutated;
    private PorterDuffColorFilter mTintFilter;
    private final Rect mTmpBounds;
    private final float[] mTmpFloats;
    private final Matrix mTmpMatrix;
    private VectorDrawableCompatState mVectorState;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    VectorDrawableCompat() {
        this.mAllowCaching = true;
        this.mTmpFloats = new float[9];
        this.mTmpMatrix = new Matrix();
        this.mTmpBounds = new Rect();
        this.mVectorState = new VectorDrawableCompatState();
    }
    
    VectorDrawableCompat(@NonNull final VectorDrawableCompatState mVectorState) {
        this.mAllowCaching = true;
        this.mTmpFloats = new float[9];
        this.mTmpMatrix = new Matrix();
        this.mTmpBounds = new Rect();
        this.mVectorState = mVectorState;
        this.mTintFilter = this.updateTintFilter(this.mTintFilter, mVectorState.mTint, mVectorState.mTintMode);
    }
    
    static int applyAlpha(final int n, final float n2) {
        return (n & 0xFFFFFF) | (int)(Color.alpha(n) * n2) << 24;
    }
    
    @Nullable
    public static VectorDrawableCompat create(@NonNull final Resources resources, @DrawableRes int next, @Nullable final Resources$Theme resources$Theme) {
        if (Build$VERSION.SDK_INT >= 24) {
            final VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(resources, next, resources$Theme);
            vectorDrawableCompat.mCachedConstantStateDelegate = new VectorDrawableDelegateState(vectorDrawableCompat.mDelegateDrawable.getConstantState());
            return vectorDrawableCompat;
        }
        try {
            final XmlResourceParser xml = resources.getXml(next);
            Xml.asAttributeSet((XmlPullParser)xml);
            do {
                next = ((XmlPullParser)xml).next();
            } while (next != 2 && next != 1);
            if (next != 2) {
                throw new XmlPullParserException("No start tag found");
            }
            goto Label_0102;
        }
        catch (XmlPullParserException ex) {
            Log.e("VectorDrawableCompat", "parser error", (Throwable)ex);
        }
        catch (IOException ex2) {
            Log.e("VectorDrawableCompat", "parser error", (Throwable)ex2);
            goto Label_0100;
        }
    }
    
    public static VectorDrawableCompat createFromXmlInner(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
        vectorDrawableCompat.inflate(resources, xmlPullParser, set, resources$Theme);
        return vectorDrawableCompat;
    }
    
    private void inflateInternal(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final VectorDrawableCompatState mVectorState = this.mVectorState;
        final VPathRenderer mvPathRenderer = mVectorState.mVPathRenderer;
        int n = 1;
        final Stack<VGroup> stack = new Stack<VGroup>();
        stack.push(mvPathRenderer.mRootGroup);
        int n3;
        for (int n2 = xmlPullParser.getEventType(), depth = xmlPullParser.getDepth(); n2 != 1 && (xmlPullParser.getDepth() >= depth + 1 || n2 != 3); n2 = xmlPullParser.next(), n = n3) {
            if (n2 == 2) {
                final String name = xmlPullParser.getName();
                final VGroup vGroup = stack.peek();
                if ("path".equals(name)) {
                    final VFullPath vFullPath = new VFullPath();
                    vFullPath.inflate(resources, set, resources$Theme, xmlPullParser);
                    vGroup.mChildren.add(vFullPath);
                    if (((VPath)vFullPath).getPathName() != null) {
                        mvPathRenderer.mVGTargetsMap.put(((VPath)vFullPath).getPathName(), vFullPath);
                    }
                    n3 = 0;
                    mVectorState.mChangingConfigurations |= vFullPath.mChangingConfigurations;
                }
                else if ("clip-path".equals(name)) {
                    final VClipPath vClipPath = new VClipPath();
                    vClipPath.inflate(resources, set, resources$Theme, xmlPullParser);
                    vGroup.mChildren.add(vClipPath);
                    if (((VPath)vClipPath).getPathName() != null) {
                        mvPathRenderer.mVGTargetsMap.put(((VPath)vClipPath).getPathName(), vClipPath);
                    }
                    mVectorState.mChangingConfigurations |= vClipPath.mChangingConfigurations;
                    n3 = n;
                }
                else {
                    n3 = n;
                    if ("group".equals(name)) {
                        final VGroup vGroup2 = new VGroup();
                        vGroup2.inflate(resources, set, resources$Theme, xmlPullParser);
                        vGroup.mChildren.add(vGroup2);
                        stack.push(vGroup2);
                        if (vGroup2.getGroupName() != null) {
                            mvPathRenderer.mVGTargetsMap.put(vGroup2.getGroupName(), vGroup2);
                        }
                        mVectorState.mChangingConfigurations |= vGroup2.mChangingConfigurations;
                        n3 = n;
                    }
                }
            }
            else {
                n3 = n;
                if (n2 == 3) {
                    n3 = n;
                    if ("group".equals(xmlPullParser.getName())) {
                        stack.pop();
                        n3 = n;
                    }
                }
            }
        }
        if (n != 0) {
            final StringBuffer sb = new StringBuffer();
            if (sb.length() > 0) {
                sb.append(" or ");
            }
            sb.append("path");
            throw new XmlPullParserException("no " + (Object)sb + " defined");
        }
    }
    
    private boolean needMirroring() {
        return Build$VERSION.SDK_INT >= 17 && (this.isAutoMirrored() && this.getLayoutDirection() == 1);
    }
    
    private static PorterDuff$Mode parseTintModeCompat(final int n, final PorterDuff$Mode porterDuff$Mode) {
        switch (n) {
            case 3: {
                return PorterDuff$Mode.SRC_OVER;
            }
            case 5: {
                return PorterDuff$Mode.SRC_IN;
            }
            case 9: {
                return PorterDuff$Mode.SRC_ATOP;
            }
            case 14: {
                return PorterDuff$Mode.MULTIPLY;
            }
            case 15: {
                return PorterDuff$Mode.SCREEN;
            }
            case 16: {
                if (Build$VERSION.SDK_INT >= 11) {
                    return PorterDuff$Mode.ADD;
                }
                break;
            }
        }
        return porterDuff$Mode;
    }
    
    private void printGroupTree(final VGroup vGroup, final int n) {
        String string = "";
        for (int i = 0; i < n; ++i) {
            string += "    ";
        }
        Log.v("VectorDrawableCompat", string + "current group is :" + vGroup.getGroupName() + " rotation is " + vGroup.mRotate);
        Log.v("VectorDrawableCompat", string + "matrix is :" + vGroup.getLocalMatrix().toString());
        for (int j = 0; j < vGroup.mChildren.size(); ++j) {
            final Object value = vGroup.mChildren.get(j);
            if (value instanceof VGroup) {
                this.printGroupTree((VGroup)value, n + 1);
            }
            else {
                ((VPath)value).printVPath(n + 1);
            }
        }
    }
    
    private void updateStateFromTypedArray(final TypedArray typedArray, final XmlPullParser xmlPullParser) throws XmlPullParserException {
        final VectorDrawableCompatState mVectorState = this.mVectorState;
        final VPathRenderer mvPathRenderer = mVectorState.mVPathRenderer;
        mVectorState.mTintMode = parseTintModeCompat(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "tintMode", 6, -1), PorterDuff$Mode.SRC_IN);
        final ColorStateList colorStateList = typedArray.getColorStateList(1);
        if (colorStateList != null) {
            mVectorState.mTint = colorStateList;
        }
        mVectorState.mAutoMirrored = TypedArrayUtils.getNamedBoolean(typedArray, xmlPullParser, "autoMirrored", 5, mVectorState.mAutoMirrored);
        mvPathRenderer.mViewportWidth = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "viewportWidth", 7, mvPathRenderer.mViewportWidth);
        mvPathRenderer.mViewportHeight = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "viewportHeight", 8, mvPathRenderer.mViewportHeight);
        if (mvPathRenderer.mViewportWidth <= 0.0f) {
            throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        }
        if (mvPathRenderer.mViewportHeight <= 0.0f) {
            throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
        }
        mvPathRenderer.mBaseWidth = typedArray.getDimension(3, mvPathRenderer.mBaseWidth);
        mvPathRenderer.mBaseHeight = typedArray.getDimension(2, mvPathRenderer.mBaseHeight);
        if (mvPathRenderer.mBaseWidth <= 0.0f) {
            throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires width > 0");
        }
        if (mvPathRenderer.mBaseHeight <= 0.0f) {
            throw new XmlPullParserException(typedArray.getPositionDescription() + "<vector> tag requires height > 0");
        }
        mvPathRenderer.setAlpha(TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "alpha", 4, mvPathRenderer.getAlpha()));
        final String string = typedArray.getString(0);
        if (string != null) {
            mvPathRenderer.mRootName = string;
            mvPathRenderer.mVGTargetsMap.put(string, mvPathRenderer);
        }
    }
    
    public boolean canApplyTheme() {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.canApplyTheme(this.mDelegateDrawable);
        }
        return false;
    }
    
    public void draw(final Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
        }
        else {
            this.copyBounds(this.mTmpBounds);
            if (this.mTmpBounds.width() > 0 && this.mTmpBounds.height() > 0) {
                Object o;
                if (this.mColorFilter == null) {
                    o = this.mTintFilter;
                }
                else {
                    o = this.mColorFilter;
                }
                canvas.getMatrix(this.mTmpMatrix);
                this.mTmpMatrix.getValues(this.mTmpFloats);
                float abs = Math.abs(this.mTmpFloats[0]);
                float abs2 = Math.abs(this.mTmpFloats[4]);
                final float abs3 = Math.abs(this.mTmpFloats[1]);
                final float abs4 = Math.abs(this.mTmpFloats[3]);
                if (abs3 != 0.0f || abs4 != 0.0f) {
                    abs = 1.0f;
                    abs2 = 1.0f;
                }
                final int n = (int)(this.mTmpBounds.width() * abs);
                final int n2 = (int)(this.mTmpBounds.height() * abs2);
                final int min = Math.min(2048, n);
                final int min2 = Math.min(2048, n2);
                if (min > 0 && min2 > 0) {
                    final int save = canvas.save();
                    canvas.translate((float)this.mTmpBounds.left, (float)this.mTmpBounds.top);
                    if (this.needMirroring()) {
                        canvas.translate((float)this.mTmpBounds.width(), 0.0f);
                        canvas.scale(-1.0f, 1.0f);
                    }
                    this.mTmpBounds.offsetTo(0, 0);
                    this.mVectorState.createCachedBitmapIfNeeded(min, min2);
                    if (!this.mAllowCaching) {
                        this.mVectorState.updateCachedBitmap(min, min2);
                    }
                    else if (!this.mVectorState.canReuseCache()) {
                        this.mVectorState.updateCachedBitmap(min, min2);
                        this.mVectorState.updateCacheStates();
                    }
                    this.mVectorState.drawCachedBitmapWithRootAlpha(canvas, (ColorFilter)o, this.mTmpBounds);
                    canvas.restoreToCount(save);
                }
            }
        }
    }
    
    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mVectorState.mVPathRenderer.getRootAlpha();
    }
    
    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
    }
    
    public Drawable$ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && Build$VERSION.SDK_INT >= 24) {
            return new VectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        this.mVectorState.mChangingConfigurations = this.getChangingConfigurations();
        return this.mVectorState;
    }
    
    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return (int)this.mVectorState.mVPathRenderer.mBaseHeight;
    }
    
    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return (int)this.mVectorState.mVPathRenderer.mBaseWidth;
    }
    
    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return -3;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public float getPixelSize() {
        if ((this.mVectorState == null && this.mVectorState.mVPathRenderer == null) || this.mVectorState.mVPathRenderer.mBaseWidth == 0.0f || this.mVectorState.mVPathRenderer.mBaseHeight == 0.0f || this.mVectorState.mVPathRenderer.mViewportHeight == 0.0f || this.mVectorState.mVPathRenderer.mViewportWidth == 0.0f) {
            return 1.0f;
        }
        return Math.min(this.mVectorState.mVPathRenderer.mViewportWidth / this.mVectorState.mVPathRenderer.mBaseWidth, this.mVectorState.mVPathRenderer.mViewportHeight / this.mVectorState.mVPathRenderer.mBaseHeight);
    }
    
    Object getTargetByName(final String s) {
        return this.mVectorState.mVPathRenderer.mVGTargetsMap.get(s);
    }
    
    public void inflate(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.inflate(resources, xmlPullParser, set);
            return;
        }
        this.inflate(resources, xmlPullParser, set, null);
    }
    
    public void inflate(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, resources, xmlPullParser, set, resources$Theme);
            return;
        }
        final VectorDrawableCompatState mVectorState = this.mVectorState;
        mVectorState.mVPathRenderer = new VPathRenderer();
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_TYPE_ARRAY);
        this.updateStateFromTypedArray(obtainAttributes, xmlPullParser);
        obtainAttributes.recycle();
        mVectorState.mChangingConfigurations = this.getChangingConfigurations();
        mVectorState.mCacheDirty = true;
        this.inflateInternal(resources, xmlPullParser, set, resources$Theme);
        this.mTintFilter = this.updateTintFilter(this.mTintFilter, mVectorState.mTint, mVectorState.mTintMode);
    }
    
    public void invalidateSelf() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.invalidateSelf();
            return;
        }
        super.invalidateSelf();
    }
    
    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mVectorState.mAutoMirrored;
    }
    
    public boolean isStateful() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        return super.isStateful() || (this.mVectorState != null && this.mVectorState.mTint != null && this.mVectorState.mTint.isStateful());
    }
    
    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
        }
        else if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableCompatState(this.mVectorState);
            this.mMutated = true;
            return this;
        }
        return this;
    }
    
    @Override
    protected void onBoundsChange(final Rect bounds) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(bounds);
        }
    }
    
    protected boolean onStateChange(final int[] state) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(state);
        }
        final VectorDrawableCompatState mVectorState = this.mVectorState;
        if (mVectorState.mTint != null && mVectorState.mTintMode != null) {
            this.mTintFilter = this.updateTintFilter(this.mTintFilter, mVectorState.mTint, mVectorState.mTintMode);
            this.invalidateSelf();
            return true;
        }
        return false;
    }
    
    public void scheduleSelf(final Runnable runnable, final long n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.scheduleSelf(runnable, n);
            return;
        }
        super.scheduleSelf(runnable, n);
    }
    
    void setAllowCaching(final boolean mAllowCaching) {
        this.mAllowCaching = mAllowCaching;
    }
    
    public void setAlpha(final int n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(n);
        }
        else if (this.mVectorState.mVPathRenderer.getRootAlpha() != n) {
            this.mVectorState.mVPathRenderer.setRootAlpha(n);
            this.invalidateSelf();
        }
    }
    
    public void setAutoMirrored(final boolean mAutoMirrored) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, mAutoMirrored);
            return;
        }
        this.mVectorState.mAutoMirrored = mAutoMirrored;
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mColorFilter = colorFilter;
        this.invalidateSelf();
    }
    
    public void setTint(final int n) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, n);
            return;
        }
        this.setTintList(ColorStateList.valueOf(n));
    }
    
    public void setTintList(final ColorStateList mTint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, mTint);
        }
        else {
            final VectorDrawableCompatState mVectorState = this.mVectorState;
            if (mVectorState.mTint != mTint) {
                mVectorState.mTint = mTint;
                this.mTintFilter = this.updateTintFilter(this.mTintFilter, mTint, mVectorState.mTintMode);
                this.invalidateSelf();
            }
        }
    }
    
    public void setTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, mTintMode);
        }
        else {
            final VectorDrawableCompatState mVectorState = this.mVectorState;
            if (mVectorState.mTintMode != mTintMode) {
                mVectorState.mTintMode = mTintMode;
                this.mTintFilter = this.updateTintFilter(this.mTintFilter, mVectorState.mTint, mTintMode);
                this.invalidateSelf();
            }
        }
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(b, b2);
        }
        return super.setVisible(b, b2);
    }
    
    public void unscheduleSelf(final Runnable runnable) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.unscheduleSelf(runnable);
            return;
        }
        super.unscheduleSelf(runnable);
    }
    
    PorterDuffColorFilter updateTintFilter(final PorterDuffColorFilter porterDuffColorFilter, final ColorStateList list, final PorterDuff$Mode porterDuff$Mode) {
        if (list == null || porterDuff$Mode == null) {
            return null;
        }
        return new PorterDuffColorFilter(list.getColorForState(this.getState(), 0), porterDuff$Mode);
    }
    
    private static class VClipPath extends VPath
    {
        public VClipPath() {
        }
        
        public VClipPath(final VClipPath vClipPath) {
            super((VPath)vClipPath);
        }
        
        private void updateStateFromTypedArray(final TypedArray typedArray) {
            final String string = typedArray.getString(0);
            if (string != null) {
                this.mPathName = string;
            }
            final String string2 = typedArray.getString(1);
            if (string2 != null) {
                this.mNodes = PathParser.createNodesFromPathData(string2);
            }
        }
        
        public void inflate(final Resources resources, final AttributeSet set, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser) {
            if (!TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
                return;
            }
            final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_CLIP_PATH);
            this.updateStateFromTypedArray(obtainAttributes);
            obtainAttributes.recycle();
        }
        
        @Override
        public boolean isClipPath() {
            return true;
        }
    }
    
    private static class VFullPath extends VPath
    {
        float mFillAlpha;
        int mFillColor;
        int mFillRule;
        float mStrokeAlpha;
        int mStrokeColor;
        Paint$Cap mStrokeLineCap;
        Paint$Join mStrokeLineJoin;
        float mStrokeMiterlimit;
        float mStrokeWidth;
        private int[] mThemeAttrs;
        float mTrimPathEnd;
        float mTrimPathOffset;
        float mTrimPathStart;
        
        public VFullPath() {
            this.mStrokeColor = 0;
            this.mStrokeWidth = 0.0f;
            this.mFillColor = 0;
            this.mStrokeAlpha = 1.0f;
            this.mFillRule = 0;
            this.mFillAlpha = 1.0f;
            this.mTrimPathStart = 0.0f;
            this.mTrimPathEnd = 1.0f;
            this.mTrimPathOffset = 0.0f;
            this.mStrokeLineCap = Paint$Cap.BUTT;
            this.mStrokeLineJoin = Paint$Join.MITER;
            this.mStrokeMiterlimit = 4.0f;
        }
        
        public VFullPath(final VFullPath vFullPath) {
            super((VPath)vFullPath);
            this.mStrokeColor = 0;
            this.mStrokeWidth = 0.0f;
            this.mFillColor = 0;
            this.mStrokeAlpha = 1.0f;
            this.mFillRule = 0;
            this.mFillAlpha = 1.0f;
            this.mTrimPathStart = 0.0f;
            this.mTrimPathEnd = 1.0f;
            this.mTrimPathOffset = 0.0f;
            this.mStrokeLineCap = Paint$Cap.BUTT;
            this.mStrokeLineJoin = Paint$Join.MITER;
            this.mStrokeMiterlimit = 4.0f;
            this.mThemeAttrs = vFullPath.mThemeAttrs;
            this.mStrokeColor = vFullPath.mStrokeColor;
            this.mStrokeWidth = vFullPath.mStrokeWidth;
            this.mStrokeAlpha = vFullPath.mStrokeAlpha;
            this.mFillColor = vFullPath.mFillColor;
            this.mFillRule = vFullPath.mFillRule;
            this.mFillAlpha = vFullPath.mFillAlpha;
            this.mTrimPathStart = vFullPath.mTrimPathStart;
            this.mTrimPathEnd = vFullPath.mTrimPathEnd;
            this.mTrimPathOffset = vFullPath.mTrimPathOffset;
            this.mStrokeLineCap = vFullPath.mStrokeLineCap;
            this.mStrokeLineJoin = vFullPath.mStrokeLineJoin;
            this.mStrokeMiterlimit = vFullPath.mStrokeMiterlimit;
        }
        
        private Paint$Cap getStrokeLineCap(final int n, final Paint$Cap paint$Cap) {
            switch (n) {
                default: {
                    return paint$Cap;
                }
                case 0: {
                    return Paint$Cap.BUTT;
                }
                case 1: {
                    return Paint$Cap.ROUND;
                }
                case 2: {
                    return Paint$Cap.SQUARE;
                }
            }
        }
        
        private Paint$Join getStrokeLineJoin(final int n, final Paint$Join paint$Join) {
            switch (n) {
                default: {
                    return paint$Join;
                }
                case 0: {
                    return Paint$Join.MITER;
                }
                case 1: {
                    return Paint$Join.ROUND;
                }
                case 2: {
                    return Paint$Join.BEVEL;
                }
            }
        }
        
        private void updateStateFromTypedArray(final TypedArray typedArray, final XmlPullParser xmlPullParser) {
            this.mThemeAttrs = null;
            if (!TypedArrayUtils.hasAttribute(xmlPullParser, "pathData")) {
                return;
            }
            final String string = typedArray.getString(0);
            if (string != null) {
                this.mPathName = string;
            }
            final String string2 = typedArray.getString(2);
            if (string2 != null) {
                this.mNodes = PathParser.createNodesFromPathData(string2);
            }
            this.mFillColor = TypedArrayUtils.getNamedColor(typedArray, xmlPullParser, "fillColor", 1, this.mFillColor);
            this.mFillAlpha = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "fillAlpha", 12, this.mFillAlpha);
            this.mStrokeLineCap = this.getStrokeLineCap(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "strokeLineCap", 8, -1), this.mStrokeLineCap);
            this.mStrokeLineJoin = this.getStrokeLineJoin(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "strokeLineJoin", 9, -1), this.mStrokeLineJoin);
            this.mStrokeMiterlimit = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "strokeMiterLimit", 10, this.mStrokeMiterlimit);
            this.mStrokeColor = TypedArrayUtils.getNamedColor(typedArray, xmlPullParser, "strokeColor", 3, this.mStrokeColor);
            this.mStrokeAlpha = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "strokeAlpha", 11, this.mStrokeAlpha);
            this.mStrokeWidth = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "strokeWidth", 4, this.mStrokeWidth);
            this.mTrimPathEnd = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "trimPathEnd", 6, this.mTrimPathEnd);
            this.mTrimPathOffset = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "trimPathOffset", 7, this.mTrimPathOffset);
            this.mTrimPathStart = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "trimPathStart", 5, this.mTrimPathStart);
            this.mFillRule = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "fillType", 13, this.mFillRule);
        }
        
        @Override
        public void applyTheme(final Resources$Theme resources$Theme) {
            if (this.mThemeAttrs == null) {}
        }
        
        @Override
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null;
        }
        
        float getFillAlpha() {
            return this.mFillAlpha;
        }
        
        int getFillColor() {
            return this.mFillColor;
        }
        
        float getStrokeAlpha() {
            return this.mStrokeAlpha;
        }
        
        int getStrokeColor() {
            return this.mStrokeColor;
        }
        
        float getStrokeWidth() {
            return this.mStrokeWidth;
        }
        
        float getTrimPathEnd() {
            return this.mTrimPathEnd;
        }
        
        float getTrimPathOffset() {
            return this.mTrimPathOffset;
        }
        
        float getTrimPathStart() {
            return this.mTrimPathStart;
        }
        
        public void inflate(final Resources resources, final AttributeSet set, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser) {
            final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_PATH);
            this.updateStateFromTypedArray(obtainAttributes, xmlPullParser);
            obtainAttributes.recycle();
        }
        
        void setFillAlpha(final float mFillAlpha) {
            this.mFillAlpha = mFillAlpha;
        }
        
        void setFillColor(final int mFillColor) {
            this.mFillColor = mFillColor;
        }
        
        void setStrokeAlpha(final float mStrokeAlpha) {
            this.mStrokeAlpha = mStrokeAlpha;
        }
        
        void setStrokeColor(final int mStrokeColor) {
            this.mStrokeColor = mStrokeColor;
        }
        
        void setStrokeWidth(final float mStrokeWidth) {
            this.mStrokeWidth = mStrokeWidth;
        }
        
        void setTrimPathEnd(final float mTrimPathEnd) {
            this.mTrimPathEnd = mTrimPathEnd;
        }
        
        void setTrimPathOffset(final float mTrimPathOffset) {
            this.mTrimPathOffset = mTrimPathOffset;
        }
        
        void setTrimPathStart(final float mTrimPathStart) {
            this.mTrimPathStart = mTrimPathStart;
        }
    }
    
    private static class VGroup
    {
        int mChangingConfigurations;
        final ArrayList<Object> mChildren;
        private String mGroupName;
        private final Matrix mLocalMatrix;
        private float mPivotX;
        private float mPivotY;
        float mRotate;
        private float mScaleX;
        private float mScaleY;
        private final Matrix mStackedMatrix;
        private int[] mThemeAttrs;
        private float mTranslateX;
        private float mTranslateY;
        
        public VGroup() {
            this.mStackedMatrix = new Matrix();
            this.mChildren = new ArrayList<Object>();
            this.mRotate = 0.0f;
            this.mPivotX = 0.0f;
            this.mPivotY = 0.0f;
            this.mScaleX = 1.0f;
            this.mScaleY = 1.0f;
            this.mTranslateX = 0.0f;
            this.mTranslateY = 0.0f;
            this.mLocalMatrix = new Matrix();
            this.mGroupName = null;
        }
        
        public VGroup(VGroup vGroup, final ArrayMap<String, Object> arrayMap) {
            this.mStackedMatrix = new Matrix();
            this.mChildren = new ArrayList<Object>();
            this.mRotate = 0.0f;
            this.mPivotX = 0.0f;
            this.mPivotY = 0.0f;
            this.mScaleX = 1.0f;
            this.mScaleY = 1.0f;
            this.mTranslateX = 0.0f;
            this.mTranslateY = 0.0f;
            this.mLocalMatrix = new Matrix();
            this.mGroupName = null;
            this.mRotate = vGroup.mRotate;
            this.mPivotX = vGroup.mPivotX;
            this.mPivotY = vGroup.mPivotY;
            this.mScaleX = vGroup.mScaleX;
            this.mScaleY = vGroup.mScaleY;
            this.mTranslateX = vGroup.mTranslateX;
            this.mTranslateY = vGroup.mTranslateY;
            this.mThemeAttrs = vGroup.mThemeAttrs;
            this.mGroupName = vGroup.mGroupName;
            this.mChangingConfigurations = vGroup.mChangingConfigurations;
            if (this.mGroupName != null) {
                arrayMap.put(this.mGroupName, this);
            }
            this.mLocalMatrix.set(vGroup.mLocalMatrix);
            final ArrayList<Object> mChildren = vGroup.mChildren;
            for (int i = 0; i < mChildren.size(); ++i) {
                final VGroup value = mChildren.get(i);
                if (value instanceof VGroup) {
                    vGroup = value;
                    this.mChildren.add(new VGroup(vGroup, arrayMap));
                }
                else {
                    VPath vPath;
                    if (value instanceof VFullPath) {
                        vPath = new VFullPath((VFullPath)value);
                    }
                    else {
                        if (!(value instanceof VClipPath)) {
                            throw new IllegalStateException("Unknown object in the tree!");
                        }
                        vPath = new VClipPath((VClipPath)value);
                    }
                    this.mChildren.add(vPath);
                    if (vPath.mPathName != null) {
                        arrayMap.put(vPath.mPathName, vPath);
                    }
                }
            }
        }
        
        private void updateLocalMatrix() {
            this.mLocalMatrix.reset();
            this.mLocalMatrix.postTranslate(-this.mPivotX, -this.mPivotY);
            this.mLocalMatrix.postScale(this.mScaleX, this.mScaleY);
            this.mLocalMatrix.postRotate(this.mRotate, 0.0f, 0.0f);
            this.mLocalMatrix.postTranslate(this.mTranslateX + this.mPivotX, this.mTranslateY + this.mPivotY);
        }
        
        private void updateStateFromTypedArray(final TypedArray typedArray, final XmlPullParser xmlPullParser) {
            this.mThemeAttrs = null;
            this.mRotate = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "rotation", 5, this.mRotate);
            this.mPivotX = typedArray.getFloat(1, this.mPivotX);
            this.mPivotY = typedArray.getFloat(2, this.mPivotY);
            this.mScaleX = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "scaleX", 3, this.mScaleX);
            this.mScaleY = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "scaleY", 4, this.mScaleY);
            this.mTranslateX = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "translateX", 6, this.mTranslateX);
            this.mTranslateY = TypedArrayUtils.getNamedFloat(typedArray, xmlPullParser, "translateY", 7, this.mTranslateY);
            final String string = typedArray.getString(0);
            if (string != null) {
                this.mGroupName = string;
            }
            this.updateLocalMatrix();
        }
        
        public String getGroupName() {
            return this.mGroupName;
        }
        
        public Matrix getLocalMatrix() {
            return this.mLocalMatrix;
        }
        
        public float getPivotX() {
            return this.mPivotX;
        }
        
        public float getPivotY() {
            return this.mPivotY;
        }
        
        public float getRotation() {
            return this.mRotate;
        }
        
        public float getScaleX() {
            return this.mScaleX;
        }
        
        public float getScaleY() {
            return this.mScaleY;
        }
        
        public float getTranslateX() {
            return this.mTranslateX;
        }
        
        public float getTranslateY() {
            return this.mTranslateY;
        }
        
        public void inflate(final Resources resources, final AttributeSet set, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser) {
            final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_VECTOR_DRAWABLE_GROUP);
            this.updateStateFromTypedArray(obtainAttributes, xmlPullParser);
            obtainAttributes.recycle();
        }
        
        public void setPivotX(final float mPivotX) {
            if (mPivotX != this.mPivotX) {
                this.mPivotX = mPivotX;
                this.updateLocalMatrix();
            }
        }
        
        public void setPivotY(final float mPivotY) {
            if (mPivotY != this.mPivotY) {
                this.mPivotY = mPivotY;
                this.updateLocalMatrix();
            }
        }
        
        public void setRotation(final float mRotate) {
            if (mRotate != this.mRotate) {
                this.mRotate = mRotate;
                this.updateLocalMatrix();
            }
        }
        
        public void setScaleX(final float mScaleX) {
            if (mScaleX != this.mScaleX) {
                this.mScaleX = mScaleX;
                this.updateLocalMatrix();
            }
        }
        
        public void setScaleY(final float mScaleY) {
            if (mScaleY != this.mScaleY) {
                this.mScaleY = mScaleY;
                this.updateLocalMatrix();
            }
        }
        
        public void setTranslateX(final float mTranslateX) {
            if (mTranslateX != this.mTranslateX) {
                this.mTranslateX = mTranslateX;
                this.updateLocalMatrix();
            }
        }
        
        public void setTranslateY(final float mTranslateY) {
            if (mTranslateY != this.mTranslateY) {
                this.mTranslateY = mTranslateY;
                this.updateLocalMatrix();
            }
        }
    }
    
    private static class VPath
    {
        int mChangingConfigurations;
        protected PathParser.PathDataNode[] mNodes;
        String mPathName;
        
        public VPath() {
            this.mNodes = null;
        }
        
        public VPath(final VPath vPath) {
            this.mNodes = null;
            this.mPathName = vPath.mPathName;
            this.mChangingConfigurations = vPath.mChangingConfigurations;
            this.mNodes = PathParser.deepCopyNodes(vPath.mNodes);
        }
        
        public void applyTheme(final Resources$Theme resources$Theme) {
        }
        
        public boolean canApplyTheme() {
            return false;
        }
        
        public PathParser.PathDataNode[] getPathData() {
            return this.mNodes;
        }
        
        public String getPathName() {
            return this.mPathName;
        }
        
        public boolean isClipPath() {
            return false;
        }
        
        public String nodesToString(final PathParser.PathDataNode[] array) {
            String s = " ";
            for (int i = 0; i < array.length; ++i) {
                s = s + array[i].mType + ":";
                final float[] mParams = array[i].mParams;
                for (int j = 0; j < mParams.length; ++j) {
                    s = s + mParams[j] + ",";
                }
            }
            return s;
        }
        
        public void printVPath(final int n) {
            String string = "";
            for (int i = 0; i < n; ++i) {
                string += "    ";
            }
            Log.v("VectorDrawableCompat", string + "current path is :" + this.mPathName + " pathData is " + this.nodesToString(this.mNodes));
        }
        
        public void setPathData(final PathParser.PathDataNode[] array) {
            if (!PathParser.canMorph(this.mNodes, array)) {
                this.mNodes = PathParser.deepCopyNodes(array);
                return;
            }
            PathParser.updateNodes(this.mNodes, array);
        }
        
        public void toPath(final Path path) {
            path.reset();
            if (this.mNodes != null) {
                PathParser.PathDataNode.nodesToPath(this.mNodes, path);
            }
        }
    }
    
    private static class VPathRenderer
    {
        private static final Matrix IDENTITY_MATRIX;
        float mBaseHeight;
        float mBaseWidth;
        private int mChangingConfigurations;
        private Paint mFillPaint;
        private final Matrix mFinalPathMatrix;
        private final Path mPath;
        private PathMeasure mPathMeasure;
        private final Path mRenderPath;
        int mRootAlpha;
        final VGroup mRootGroup;
        String mRootName;
        private Paint mStrokePaint;
        final ArrayMap<String, Object> mVGTargetsMap;
        float mViewportHeight;
        float mViewportWidth;
        
        static {
            IDENTITY_MATRIX = new Matrix();
        }
        
        public VPathRenderer() {
            this.mFinalPathMatrix = new Matrix();
            this.mBaseWidth = 0.0f;
            this.mBaseHeight = 0.0f;
            this.mViewportWidth = 0.0f;
            this.mViewportHeight = 0.0f;
            this.mRootAlpha = 255;
            this.mRootName = null;
            this.mVGTargetsMap = new ArrayMap<String, Object>();
            this.mRootGroup = new VGroup();
            this.mPath = new Path();
            this.mRenderPath = new Path();
        }
        
        public VPathRenderer(final VPathRenderer vPathRenderer) {
            this.mFinalPathMatrix = new Matrix();
            this.mBaseWidth = 0.0f;
            this.mBaseHeight = 0.0f;
            this.mViewportWidth = 0.0f;
            this.mViewportHeight = 0.0f;
            this.mRootAlpha = 255;
            this.mRootName = null;
            this.mVGTargetsMap = new ArrayMap<String, Object>();
            this.mRootGroup = new VGroup(vPathRenderer.mRootGroup, this.mVGTargetsMap);
            this.mPath = new Path(vPathRenderer.mPath);
            this.mRenderPath = new Path(vPathRenderer.mRenderPath);
            this.mBaseWidth = vPathRenderer.mBaseWidth;
            this.mBaseHeight = vPathRenderer.mBaseHeight;
            this.mViewportWidth = vPathRenderer.mViewportWidth;
            this.mViewportHeight = vPathRenderer.mViewportHeight;
            this.mChangingConfigurations = vPathRenderer.mChangingConfigurations;
            this.mRootAlpha = vPathRenderer.mRootAlpha;
            this.mRootName = vPathRenderer.mRootName;
            if (vPathRenderer.mRootName != null) {
                this.mVGTargetsMap.put(vPathRenderer.mRootName, this);
            }
        }
        
        private static float cross(final float n, final float n2, final float n3, final float n4) {
            return n * n4 - n2 * n3;
        }
        
        private void drawGroupTree(final VGroup vGroup, final Matrix matrix, final Canvas canvas, final int n, final int n2, final ColorFilter colorFilter) {
            vGroup.mStackedMatrix.set(matrix);
            vGroup.mStackedMatrix.preConcat(vGroup.mLocalMatrix);
            canvas.save();
            for (int i = 0; i < vGroup.mChildren.size(); ++i) {
                final Object value = vGroup.mChildren.get(i);
                if (value instanceof VGroup) {
                    this.drawGroupTree((VGroup)value, vGroup.mStackedMatrix, canvas, n, n2, colorFilter);
                }
                else if (value instanceof VPath) {
                    this.drawPath(vGroup, (VPath)value, canvas, n, n2, colorFilter);
                }
            }
            canvas.restore();
        }
        
        private void drawPath(final VGroup vGroup, final VPath vPath, final Canvas canvas, final int n, final int n2, final ColorFilter colorFilter) {
            final float n3 = n / this.mViewportWidth;
            final float n4 = n2 / this.mViewportHeight;
            final float min = Math.min(n3, n4);
            final Matrix access$200 = vGroup.mStackedMatrix;
            this.mFinalPathMatrix.set(access$200);
            this.mFinalPathMatrix.postScale(n3, n4);
            final float matrixScale = this.getMatrixScale(access$200);
            if (matrixScale != 0.0f) {
                vPath.toPath(this.mPath);
                final Path mPath = this.mPath;
                this.mRenderPath.reset();
                if (vPath.isClipPath()) {
                    this.mRenderPath.addPath(mPath, this.mFinalPathMatrix);
                    canvas.clipPath(this.mRenderPath);
                    return;
                }
                final VFullPath vFullPath = (VFullPath)vPath;
                if (vFullPath.mTrimPathStart != 0.0f || vFullPath.mTrimPathEnd != 1.0f) {
                    final float mTrimPathStart = vFullPath.mTrimPathStart;
                    final float mTrimPathOffset = vFullPath.mTrimPathOffset;
                    final float mTrimPathEnd = vFullPath.mTrimPathEnd;
                    final float mTrimPathOffset2 = vFullPath.mTrimPathOffset;
                    if (this.mPathMeasure == null) {
                        this.mPathMeasure = new PathMeasure();
                    }
                    this.mPathMeasure.setPath(this.mPath, false);
                    final float length = this.mPathMeasure.getLength();
                    final float n5 = (mTrimPathStart + mTrimPathOffset) % 1.0f * length;
                    final float n6 = (mTrimPathEnd + mTrimPathOffset2) % 1.0f * length;
                    mPath.reset();
                    if (n5 > n6) {
                        this.mPathMeasure.getSegment(n5, length, mPath, true);
                        this.mPathMeasure.getSegment(0.0f, n6, mPath, true);
                    }
                    else {
                        this.mPathMeasure.getSegment(n5, n6, mPath, true);
                    }
                    mPath.rLineTo(0.0f, 0.0f);
                }
                this.mRenderPath.addPath(mPath, this.mFinalPathMatrix);
                if (vFullPath.mFillColor != 0) {
                    if (this.mFillPaint == null) {
                        (this.mFillPaint = new Paint()).setStyle(Paint$Style.FILL);
                        this.mFillPaint.setAntiAlias(true);
                    }
                    final Paint mFillPaint = this.mFillPaint;
                    mFillPaint.setColor(VectorDrawableCompat.applyAlpha(vFullPath.mFillColor, vFullPath.mFillAlpha));
                    mFillPaint.setColorFilter(colorFilter);
                    final Path mRenderPath = this.mRenderPath;
                    Path$FillType fillType;
                    if (vFullPath.mFillRule == 0) {
                        fillType = Path$FillType.WINDING;
                    }
                    else {
                        fillType = Path$FillType.EVEN_ODD;
                    }
                    mRenderPath.setFillType(fillType);
                    canvas.drawPath(this.mRenderPath, mFillPaint);
                }
                if (vFullPath.mStrokeColor != 0) {
                    if (this.mStrokePaint == null) {
                        (this.mStrokePaint = new Paint()).setStyle(Paint$Style.STROKE);
                        this.mStrokePaint.setAntiAlias(true);
                    }
                    final Paint mStrokePaint = this.mStrokePaint;
                    if (vFullPath.mStrokeLineJoin != null) {
                        mStrokePaint.setStrokeJoin(vFullPath.mStrokeLineJoin);
                    }
                    if (vFullPath.mStrokeLineCap != null) {
                        mStrokePaint.setStrokeCap(vFullPath.mStrokeLineCap);
                    }
                    mStrokePaint.setStrokeMiter(vFullPath.mStrokeMiterlimit);
                    mStrokePaint.setColor(VectorDrawableCompat.applyAlpha(vFullPath.mStrokeColor, vFullPath.mStrokeAlpha));
                    mStrokePaint.setColorFilter(colorFilter);
                    mStrokePaint.setStrokeWidth(vFullPath.mStrokeWidth * (min * matrixScale));
                    canvas.drawPath(this.mRenderPath, mStrokePaint);
                }
            }
        }
        
        private float getMatrixScale(final Matrix matrix) {
            final float[] array2;
            final float[] array = array2 = new float[4];
            array2[0] = 0.0f;
            array2[2] = (array2[1] = 1.0f);
            array2[3] = 0.0f;
            matrix.mapVectors(array);
            final float n = (float)Math.hypot(array[0], array[1]);
            final float n2 = (float)Math.hypot(array[2], array[3]);
            final float cross = cross(array[0], array[1], array[2], array[3]);
            final float max = Math.max(n, n2);
            float n3 = 0.0f;
            if (max > 0.0f) {
                n3 = Math.abs(cross) / max;
            }
            return n3;
        }
        
        public void draw(final Canvas canvas, final int n, final int n2, final ColorFilter colorFilter) {
            this.drawGroupTree(this.mRootGroup, VPathRenderer.IDENTITY_MATRIX, canvas, n, n2, colorFilter);
        }
        
        public float getAlpha() {
            return this.getRootAlpha() / 255.0f;
        }
        
        public int getRootAlpha() {
            return this.mRootAlpha;
        }
        
        public void setAlpha(final float n) {
            this.setRootAlpha((int)(255.0f * n));
        }
        
        public void setRootAlpha(final int mRootAlpha) {
            this.mRootAlpha = mRootAlpha;
        }
    }
    
    private static class VectorDrawableCompatState extends Drawable$ConstantState
    {
        boolean mAutoMirrored;
        boolean mCacheDirty;
        boolean mCachedAutoMirrored;
        Bitmap mCachedBitmap;
        int mCachedRootAlpha;
        int[] mCachedThemeAttrs;
        ColorStateList mCachedTint;
        PorterDuff$Mode mCachedTintMode;
        int mChangingConfigurations;
        Paint mTempPaint;
        ColorStateList mTint;
        PorterDuff$Mode mTintMode;
        VPathRenderer mVPathRenderer;
        
        public VectorDrawableCompatState() {
            this.mTint = null;
            this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
            this.mVPathRenderer = new VPathRenderer();
        }
        
        public VectorDrawableCompatState(final VectorDrawableCompatState vectorDrawableCompatState) {
            this.mTint = null;
            this.mTintMode = VectorDrawableCompat.DEFAULT_TINT_MODE;
            if (vectorDrawableCompatState != null) {
                this.mChangingConfigurations = vectorDrawableCompatState.mChangingConfigurations;
                this.mVPathRenderer = new VPathRenderer(vectorDrawableCompatState.mVPathRenderer);
                if (vectorDrawableCompatState.mVPathRenderer.mFillPaint != null) {
                    this.mVPathRenderer.mFillPaint = new Paint(vectorDrawableCompatState.mVPathRenderer.mFillPaint);
                }
                if (vectorDrawableCompatState.mVPathRenderer.mStrokePaint != null) {
                    this.mVPathRenderer.mStrokePaint = new Paint(vectorDrawableCompatState.mVPathRenderer.mStrokePaint);
                }
                this.mTint = vectorDrawableCompatState.mTint;
                this.mTintMode = vectorDrawableCompatState.mTintMode;
                this.mAutoMirrored = vectorDrawableCompatState.mAutoMirrored;
            }
        }
        
        public boolean canReuseBitmap(final int n, final int n2) {
            return n == this.mCachedBitmap.getWidth() && n2 == this.mCachedBitmap.getHeight();
        }
        
        public boolean canReuseCache() {
            return !this.mCacheDirty && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored && this.mCachedRootAlpha == this.mVPathRenderer.getRootAlpha();
        }
        
        public void createCachedBitmapIfNeeded(final int n, final int n2) {
            if (this.mCachedBitmap == null || !this.canReuseBitmap(n, n2)) {
                this.mCachedBitmap = Bitmap.createBitmap(n, n2, Bitmap$Config.ARGB_8888);
                this.mCacheDirty = true;
            }
        }
        
        public void drawCachedBitmapWithRootAlpha(final Canvas canvas, final ColorFilter colorFilter, final Rect rect) {
            canvas.drawBitmap(this.mCachedBitmap, (Rect)null, rect, this.getPaint(colorFilter));
        }
        
        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
        
        public Paint getPaint(final ColorFilter colorFilter) {
            if (!this.hasTranslucentRoot() && colorFilter == null) {
                return null;
            }
            if (this.mTempPaint == null) {
                (this.mTempPaint = new Paint()).setFilterBitmap(true);
            }
            this.mTempPaint.setAlpha(this.mVPathRenderer.getRootAlpha());
            this.mTempPaint.setColorFilter(colorFilter);
            return this.mTempPaint;
        }
        
        public boolean hasTranslucentRoot() {
            return this.mVPathRenderer.getRootAlpha() < 255;
        }
        
        public Drawable newDrawable() {
            return new VectorDrawableCompat(this);
        }
        
        public Drawable newDrawable(final Resources resources) {
            return new VectorDrawableCompat(this);
        }
        
        public void updateCacheStates() {
            this.mCachedTint = this.mTint;
            this.mCachedTintMode = this.mTintMode;
            this.mCachedRootAlpha = this.mVPathRenderer.getRootAlpha();
            this.mCachedAutoMirrored = this.mAutoMirrored;
            this.mCacheDirty = false;
        }
        
        public void updateCachedBitmap(final int n, final int n2) {
            this.mCachedBitmap.eraseColor(0);
            this.mVPathRenderer.draw(new Canvas(this.mCachedBitmap), n, n2, null);
        }
    }
    
    @RequiresApi(24)
    private static class VectorDrawableDelegateState extends Drawable$ConstantState
    {
        private final Drawable$ConstantState mDelegateState;
        
        public VectorDrawableDelegateState(final Drawable$ConstantState mDelegateState) {
            this.mDelegateState = mDelegateState;
        }
        
        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }
        
        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }
        
        public Drawable newDrawable() {
            final VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable();
            return vectorDrawableCompat;
        }
        
        public Drawable newDrawable(final Resources resources) {
            final VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources);
            return vectorDrawableCompat;
        }
        
        public Drawable newDrawable(final Resources resources, final Resources$Theme resources$Theme) {
            final VectorDrawableCompat vectorDrawableCompat = new VectorDrawableCompat();
            vectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources, resources$Theme);
            return vectorDrawableCompat;
        }
    }
}
