// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.graphics.drawable.VectorDrawable;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.graphics.Path$FillType;
import android.graphics.Paint$Style;
import android.graphics.PathMeasure;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint$Join;
import android.graphics.Paint$Cap;
import android.support.v4.graphics.PathParser;
import android.support.annotation.RestrictTo;
import java.util.Stack;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuffColorFilter;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff$Mode;
import java.util.Collection;
import android.animation.AnimatorListenerAdapter;
import android.content.res.TypedArray;
import android.support.v4.content.res.TypedArrayUtils;
import android.graphics.Region;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.animation.TypeEvaluator;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;
import android.support.v4.util.ArrayMap;
import android.animation.Animator;
import android.support.annotation.RequiresApi;
import android.content.res.Resources$Theme;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import java.io.IOException;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.support.v4.content.res.ResourcesCompat;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build$VERSION;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.content.res.Resources;
import android.content.Context;
import android.graphics.drawable.Drawable$Callback;
import android.animation.ArgbEvaluator;
import android.animation.Animator$AnimatorListener;
import java.util.ArrayList;

public class AnimatedVectorDrawableCompat extends VectorDrawableCommon implements Animatable2Compat
{
    private static final String ANIMATED_VECTOR = "animated-vector";
    private static final boolean DBG_ANIMATION_VECTOR_DRAWABLE = false;
    private static final String LOGTAG = "AnimatedVDCompat";
    private static final String TARGET = "target";
    private AnimatedVectorDrawableCompatState mAnimatedVectorState;
    private ArrayList<AnimationCallback> mAnimationCallbacks;
    private Animator$AnimatorListener mAnimatorListener;
    private ArgbEvaluator mArgbEvaluator;
    AnimatedVectorDrawableDelegateState mCachedConstantStateDelegate;
    final Drawable$Callback mCallback;
    private Context mContext;
    
    AnimatedVectorDrawableCompat() {
        this(null, null, null);
    }
    
    private AnimatedVectorDrawableCompat(@Nullable final Context context) {
        this(context, null, null);
    }
    
    private AnimatedVectorDrawableCompat(@Nullable final Context mContext, @Nullable final AnimatedVectorDrawableCompatState mAnimatedVectorState, @Nullable final Resources resources) {
        this.mArgbEvaluator = null;
        this.mAnimatorListener = null;
        this.mAnimationCallbacks = null;
        this.mCallback = (Drawable$Callback)new Drawable$Callback() {
            public void invalidateDrawable(final Drawable drawable) {
                AnimatedVectorDrawableCompat.this.invalidateSelf();
            }
            
            public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
                AnimatedVectorDrawableCompat.this.scheduleSelf(runnable, n);
            }
            
            public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
                AnimatedVectorDrawableCompat.this.unscheduleSelf(runnable);
            }
        };
        this.mContext = mContext;
        if (mAnimatedVectorState != null) {
            this.mAnimatedVectorState = mAnimatedVectorState;
            return;
        }
        this.mAnimatedVectorState = new AnimatedVectorDrawableCompatState(mContext, mAnimatedVectorState, this.mCallback, resources);
    }
    
    public static void clearAnimationCallbacks(final Drawable drawable) {
        if (drawable == null || !(drawable instanceof Animatable)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            ((AnimatedVectorDrawable)drawable).clearAnimationCallbacks();
            return;
        }
        ((AnimatedVectorDrawableCompat)drawable).clearAnimationCallbacks();
    }
    
    @Nullable
    public static AnimatedVectorDrawableCompat create(@NonNull final Context context, @DrawableRes int next) {
        if (Build$VERSION.SDK_INT >= 24) {
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(context);
            (animatedVectorDrawableCompat.mDelegateDrawable = ResourcesCompat.getDrawable(context.getResources(), next, context.getTheme())).setCallback(animatedVectorDrawableCompat.mCallback);
            animatedVectorDrawableCompat.mCachedConstantStateDelegate = new AnimatedVectorDrawableDelegateState(animatedVectorDrawableCompat.mDelegateDrawable.getConstantState());
            return animatedVectorDrawableCompat;
        }
        final Resources resources = context.getResources();
        try {
            final XmlResourceParser xml = resources.getXml(next);
            Xml.asAttributeSet((XmlPullParser)xml);
            do {
                next = ((XmlPullParser)xml).next();
            } while (next != 2 && next != 1);
            if (next != 2) {
                throw new XmlPullParserException("No start tag found");
            }
            goto Label_0124;
        }
        catch (XmlPullParserException ex) {
            Log.e("AnimatedVDCompat", "parser error", (Throwable)ex);
        }
        catch (IOException ex2) {
            Log.e("AnimatedVDCompat", "parser error", (Throwable)ex2);
            goto Label_0122;
        }
    }
    
    public static AnimatedVectorDrawableCompat createFromXmlInner(final Context context, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat(context);
        animatedVectorDrawableCompat.inflate(resources, xmlPullParser, set, resources$Theme);
        return animatedVectorDrawableCompat;
    }
    
    public static void registerAnimationCallback(final Drawable drawable, final AnimationCallback animationCallback) {
        if (drawable == null || animationCallback == null || !(drawable instanceof Animatable)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            registerPlatformCallback((AnimatedVectorDrawable)drawable, animationCallback);
            return;
        }
        ((AnimatedVectorDrawableCompat)drawable).registerAnimationCallback(animationCallback);
    }
    
    @RequiresApi(23)
    private static void registerPlatformCallback(@NonNull final AnimatedVectorDrawable animatedVectorDrawable, @NonNull final AnimationCallback animationCallback) {
        animatedVectorDrawable.registerAnimationCallback(animationCallback.getPlatformCallback());
    }
    
    private void removeAnimatorSetListener() {
        if (this.mAnimatorListener != null) {
            this.mAnimatedVectorState.mAnimatorSet.removeListener(this.mAnimatorListener);
            this.mAnimatorListener = null;
        }
    }
    
    private void setupAnimatorsForTarget(final String s, final Animator animator) {
        animator.setTarget(this.mAnimatedVectorState.mVectorDrawable.getTargetByName(s));
        if (Build$VERSION.SDK_INT < 21) {
            this.setupColorAnimator(animator);
        }
        if (this.mAnimatedVectorState.mAnimators == null) {
            this.mAnimatedVectorState.mAnimators = (ArrayList<Animator>)new ArrayList();
            this.mAnimatedVectorState.mTargetNameMap = new ArrayMap<Animator, String>();
        }
        this.mAnimatedVectorState.mAnimators.add(animator);
        this.mAnimatedVectorState.mTargetNameMap.put(animator, s);
    }
    
    private void setupColorAnimator(final Animator animator) {
        if (animator instanceof AnimatorSet) {
            final ArrayList childAnimations = ((AnimatorSet)animator).getChildAnimations();
            if (childAnimations != null) {
                for (int i = 0; i < childAnimations.size(); ++i) {
                    this.setupColorAnimator((Animator)childAnimations.get(i));
                }
            }
        }
        if (animator instanceof ObjectAnimator) {
            final ObjectAnimator objectAnimator = (ObjectAnimator)animator;
            final String propertyName = objectAnimator.getPropertyName();
            if ("fillColor".equals(propertyName) || "strokeColor".equals(propertyName)) {
                if (this.mArgbEvaluator == null) {
                    this.mArgbEvaluator = new ArgbEvaluator();
                }
                objectAnimator.setEvaluator((TypeEvaluator)this.mArgbEvaluator);
            }
        }
    }
    
    public static boolean unregisterAnimationCallback(final Drawable drawable, final AnimationCallback animationCallback) {
        if (drawable == null || animationCallback == null || !(drawable instanceof Animatable)) {
            return false;
        }
        if (Build$VERSION.SDK_INT >= 24) {
            return unregisterPlatformCallback((AnimatedVectorDrawable)drawable, animationCallback);
        }
        return ((AnimatedVectorDrawableCompat)drawable).unregisterAnimationCallback(animationCallback);
    }
    
    @RequiresApi(23)
    private static boolean unregisterPlatformCallback(final AnimatedVectorDrawable animatedVectorDrawable, final AnimationCallback animationCallback) {
        return animatedVectorDrawable.unregisterAnimationCallback(animationCallback.getPlatformCallback());
    }
    
    @Override
    public void applyTheme(final Resources$Theme resources$Theme) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.applyTheme(this.mDelegateDrawable, resources$Theme);
        }
    }
    
    public boolean canApplyTheme() {
        return this.mDelegateDrawable != null && DrawableCompat.canApplyTheme(this.mDelegateDrawable);
    }
    
    @Override
    public void clearAnimationCallbacks() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).clearAnimationCallbacks();
        }
        else {
            this.removeAnimatorSetListener();
            if (this.mAnimationCallbacks != null) {
                this.mAnimationCallbacks.clear();
            }
        }
    }
    
    public void draw(final Canvas canvas) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.draw(canvas);
        }
        else {
            this.mAnimatedVectorState.mVectorDrawable.draw(canvas);
            if (this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
                this.invalidateSelf();
            }
        }
    }
    
    public int getAlpha() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.getAlpha(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.getAlpha();
    }
    
    public int getChangingConfigurations() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getChangingConfigurations();
        }
        return super.getChangingConfigurations() | this.mAnimatedVectorState.mChangingConfigurations;
    }
    
    public Drawable$ConstantState getConstantState() {
        if (this.mDelegateDrawable != null && Build$VERSION.SDK_INT >= 24) {
            return new AnimatedVectorDrawableDelegateState(this.mDelegateDrawable.getConstantState());
        }
        return null;
    }
    
    public int getIntrinsicHeight() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicHeight();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicHeight();
    }
    
    public int getIntrinsicWidth() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getIntrinsicWidth();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getIntrinsicWidth();
    }
    
    public int getOpacity() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.getOpacity();
        }
        return this.mAnimatedVectorState.mVectorDrawable.getOpacity();
    }
    
    public void inflate(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set) throws XmlPullParserException, IOException {
        this.inflate(resources, xmlPullParser, set, null);
    }
    
    public void inflate(final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.inflate(this.mDelegateDrawable, resources, xmlPullParser, set, resources$Theme);
            return;
        }
        for (int n = xmlPullParser.getEventType(), depth = xmlPullParser.getDepth(); n != 1 && (xmlPullParser.getDepth() >= depth + 1 || n != 3); n = xmlPullParser.next()) {
            if (n == 2) {
                final String name = xmlPullParser.getName();
                if ("animated-vector".equals(name)) {
                    final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE);
                    final int resourceId = obtainAttributes.getResourceId(0, 0);
                    if (resourceId != 0) {
                        final VectorDrawableCompat create = VectorDrawableCompat.create(resources, resourceId, resources$Theme);
                        create.setAllowCaching(false);
                        create.setCallback(this.mCallback);
                        if (this.mAnimatedVectorState.mVectorDrawable != null) {
                            this.mAnimatedVectorState.mVectorDrawable.setCallback((Drawable$Callback)null);
                        }
                        this.mAnimatedVectorState.mVectorDrawable = create;
                    }
                    obtainAttributes.recycle();
                }
                else if ("target".equals(name)) {
                    final TypedArray obtainAttributes2 = resources.obtainAttributes(set, AndroidResources.STYLEABLE_ANIMATED_VECTOR_DRAWABLE_TARGET);
                    final String string = obtainAttributes2.getString(0);
                    final int resourceId2 = obtainAttributes2.getResourceId(1, 0);
                    if (resourceId2 != 0) {
                        if (this.mContext == null) {
                            obtainAttributes2.recycle();
                            throw new IllegalStateException("Context can't be null when inflating animators");
                        }
                        this.setupAnimatorsForTarget(string, AnimatorInflaterCompat.loadAnimator(this.mContext, resourceId2));
                    }
                    obtainAttributes2.recycle();
                }
            }
        }
        this.mAnimatedVectorState.setupAnimatorSet();
    }
    
    public boolean isAutoMirrored() {
        if (this.mDelegateDrawable != null) {
            return DrawableCompat.isAutoMirrored(this.mDelegateDrawable);
        }
        return this.mAnimatedVectorState.mVectorDrawable.isAutoMirrored();
    }
    
    public boolean isRunning() {
        if (this.mDelegateDrawable != null) {
            return ((AnimatedVectorDrawable)this.mDelegateDrawable).isRunning();
        }
        return this.mAnimatedVectorState.mAnimatorSet.isRunning();
    }
    
    public boolean isStateful() {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.isStateful();
        }
        return this.mAnimatedVectorState.mVectorDrawable.isStateful();
    }
    
    public Drawable mutate() {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.mutate();
        }
        return this;
    }
    
    @Override
    protected void onBoundsChange(final Rect rect) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setBounds(rect);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setBounds(rect);
    }
    
    @Override
    protected boolean onLevelChange(final int n) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setLevel(n);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setLevel(n);
    }
    
    protected boolean onStateChange(final int[] array) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setState(array);
        }
        return this.mAnimatedVectorState.mVectorDrawable.setState(array);
    }
    
    @Override
    public void registerAnimationCallback(@NonNull final AnimationCallback animationCallback) {
        if (this.mDelegateDrawable != null) {
            registerPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, animationCallback);
        }
        else if (animationCallback != null) {
            if (this.mAnimationCallbacks == null) {
                this.mAnimationCallbacks = new ArrayList<AnimationCallback>();
            }
            if (!this.mAnimationCallbacks.contains(animationCallback)) {
                this.mAnimationCallbacks.add(animationCallback);
                if (this.mAnimatorListener == null) {
                    this.mAnimatorListener = (Animator$AnimatorListener)new AnimatorListenerAdapter() {
                        public void onAnimationEnd(final Animator animator) {
                            final ArrayList<AnimationCallback> list = new ArrayList<AnimationCallback>(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                            for (int size = list.size(), i = 0; i < size; ++i) {
                                list.get(i).onAnimationEnd(AnimatedVectorDrawableCompat.this);
                            }
                        }
                        
                        public void onAnimationStart(final Animator animator) {
                            final ArrayList<AnimationCallback> list = new ArrayList<AnimationCallback>(AnimatedVectorDrawableCompat.this.mAnimationCallbacks);
                            for (int size = list.size(), i = 0; i < size; ++i) {
                                list.get(i).onAnimationStart(AnimatedVectorDrawableCompat.this);
                            }
                        }
                    };
                }
                this.mAnimatedVectorState.mAnimatorSet.addListener(this.mAnimatorListener);
            }
        }
    }
    
    public void setAlpha(final int n) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setAlpha(n);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setAlpha(n);
    }
    
    public void setAutoMirrored(final boolean autoMirrored) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setAutoMirrored(this.mDelegateDrawable, autoMirrored);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setAutoMirrored(autoMirrored);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        if (this.mDelegateDrawable != null) {
            this.mDelegateDrawable.setColorFilter(colorFilter);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setColorFilter(colorFilter);
    }
    
    public void setTint(final int tint) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTint(this.mDelegateDrawable, tint);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTint(tint);
    }
    
    public void setTintList(final ColorStateList tintList) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintList(this.mDelegateDrawable, tintList);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTintList(tintList);
    }
    
    public void setTintMode(final PorterDuff$Mode tintMode) {
        if (this.mDelegateDrawable != null) {
            DrawableCompat.setTintMode(this.mDelegateDrawable, tintMode);
            return;
        }
        this.mAnimatedVectorState.mVectorDrawable.setTintMode(tintMode);
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        if (this.mDelegateDrawable != null) {
            return this.mDelegateDrawable.setVisible(b, b2);
        }
        this.mAnimatedVectorState.mVectorDrawable.setVisible(b, b2);
        return super.setVisible(b, b2);
    }
    
    public void start() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).start();
        }
        else if (!this.mAnimatedVectorState.mAnimatorSet.isStarted()) {
            this.mAnimatedVectorState.mAnimatorSet.start();
            this.invalidateSelf();
        }
    }
    
    public void stop() {
        if (this.mDelegateDrawable != null) {
            ((AnimatedVectorDrawable)this.mDelegateDrawable).stop();
            return;
        }
        this.mAnimatedVectorState.mAnimatorSet.end();
    }
    
    @Override
    public boolean unregisterAnimationCallback(@NonNull final AnimationCallback animationCallback) {
        if (this.mDelegateDrawable != null) {
            unregisterPlatformCallback((AnimatedVectorDrawable)this.mDelegateDrawable, animationCallback);
        }
        boolean remove;
        if (this.mAnimationCallbacks == null || animationCallback == null) {
            remove = false;
        }
        else {
            remove = this.mAnimationCallbacks.remove(animationCallback);
            if (this.mAnimationCallbacks.size() == 0) {
                this.removeAnimatorSetListener();
                return remove;
            }
        }
        return remove;
    }
    
    private static class AnimatedVectorDrawableCompatState extends Drawable$ConstantState
    {
        AnimatorSet mAnimatorSet;
        private ArrayList<Animator> mAnimators;
        int mChangingConfigurations;
        ArrayMap<Animator, String> mTargetNameMap;
        VectorDrawableCompat mVectorDrawable;
        
        public AnimatedVectorDrawableCompatState(final Context context, final AnimatedVectorDrawableCompatState animatedVectorDrawableCompatState, final Drawable$Callback callback, final Resources resources) {
            if (animatedVectorDrawableCompatState != null) {
                this.mChangingConfigurations = animatedVectorDrawableCompatState.mChangingConfigurations;
                if (animatedVectorDrawableCompatState.mVectorDrawable != null) {
                    final Drawable$ConstantState constantState = animatedVectorDrawableCompatState.mVectorDrawable.getConstantState();
                    if (resources != null) {
                        this.mVectorDrawable = (VectorDrawableCompat)constantState.newDrawable(resources);
                    }
                    else {
                        this.mVectorDrawable = (VectorDrawableCompat)constantState.newDrawable();
                    }
                    (this.mVectorDrawable = (VectorDrawableCompat)this.mVectorDrawable.mutate()).setCallback(callback);
                    this.mVectorDrawable.setBounds(animatedVectorDrawableCompatState.mVectorDrawable.getBounds());
                    this.mVectorDrawable.setAllowCaching(false);
                }
                if (animatedVectorDrawableCompatState.mAnimators != null) {
                    final int size = animatedVectorDrawableCompatState.mAnimators.size();
                    this.mAnimators = new ArrayList<Animator>(size);
                    this.mTargetNameMap = new ArrayMap<Animator, String>(size);
                    for (int i = 0; i < size; ++i) {
                        final Animator animator = animatedVectorDrawableCompatState.mAnimators.get(i);
                        final Animator clone = animator.clone();
                        final String s = animatedVectorDrawableCompatState.mTargetNameMap.get(animator);
                        clone.setTarget(this.mVectorDrawable.getTargetByName(s));
                        this.mAnimators.add(clone);
                        this.mTargetNameMap.put(clone, s);
                    }
                    this.setupAnimatorSet();
                }
            }
        }
        
        public int getChangingConfigurations() {
            return this.mChangingConfigurations;
        }
        
        public Drawable newDrawable() {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }
        
        public Drawable newDrawable(final Resources resources) {
            throw new IllegalStateException("No constant state support for SDK < 24.");
        }
        
        public void setupAnimatorSet() {
            if (this.mAnimatorSet == null) {
                this.mAnimatorSet = new AnimatorSet();
            }
            this.mAnimatorSet.playTogether((Collection)this.mAnimators);
        }
    }
    
    @RequiresApi(24)
    private static class AnimatedVectorDrawableDelegateState extends Drawable$ConstantState
    {
        private final Drawable$ConstantState mDelegateState;
        
        public AnimatedVectorDrawableDelegateState(final Drawable$ConstantState mDelegateState) {
            this.mDelegateState = mDelegateState;
        }
        
        public boolean canApplyTheme() {
            return this.mDelegateState.canApplyTheme();
        }
        
        public int getChangingConfigurations() {
            return this.mDelegateState.getChangingConfigurations();
        }
        
        public Drawable newDrawable() {
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            (animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable()).setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }
        
        public Drawable newDrawable(final Resources resources) {
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            (animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources)).setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }
        
        public Drawable newDrawable(final Resources resources, final Resources$Theme resources$Theme) {
            final AnimatedVectorDrawableCompat animatedVectorDrawableCompat = new AnimatedVectorDrawableCompat();
            (animatedVectorDrawableCompat.mDelegateDrawable = this.mDelegateState.newDrawable(resources, resources$Theme)).setCallback(animatedVectorDrawableCompat.mCallback);
            return animatedVectorDrawableCompat;
        }
    }
}
