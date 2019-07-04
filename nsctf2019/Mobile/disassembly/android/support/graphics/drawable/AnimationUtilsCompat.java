// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.content.res.XmlResourceParser;
import android.content.res.Resources$NotFoundException;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.animation.AnimationUtils;
import android.os.Build$VERSION;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.util.AttributeSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.util.Xml;
import android.view.animation.Interpolator;
import org.xmlpull.v1.XmlPullParser;
import android.content.res.Resources$Theme;
import android.content.res.Resources;
import android.content.Context;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AnimationUtilsCompat
{
    private static Interpolator createInterpolatorFromXml(final Context context, final Resources resources, final Resources$Theme resources$Theme, final XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        Object o = null;
        final int depth = xmlPullParser.getDepth();
        while (true) {
            final int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return (Interpolator)o;
            }
            if (next != 2) {
                continue;
            }
            final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
            final String name = xmlPullParser.getName();
            if (name.equals("linearInterpolator")) {
                o = new LinearInterpolator();
            }
            else if (name.equals("accelerateInterpolator")) {
                o = new AccelerateInterpolator(context, attributeSet);
            }
            else if (name.equals("decelerateInterpolator")) {
                o = new DecelerateInterpolator(context, attributeSet);
            }
            else if (name.equals("accelerateDecelerateInterpolator")) {
                o = new AccelerateDecelerateInterpolator();
            }
            else if (name.equals("cycleInterpolator")) {
                o = new CycleInterpolator(context, attributeSet);
            }
            else if (name.equals("anticipateInterpolator")) {
                o = new AnticipateInterpolator(context, attributeSet);
            }
            else if (name.equals("overshootInterpolator")) {
                o = new OvershootInterpolator(context, attributeSet);
            }
            else if (name.equals("anticipateOvershootInterpolator")) {
                o = new AnticipateOvershootInterpolator(context, attributeSet);
            }
            else if (name.equals("bounceInterpolator")) {
                o = new BounceInterpolator();
            }
            else {
                if (!name.equals("pathInterpolator")) {
                    throw new RuntimeException("Unknown interpolator name: " + xmlPullParser.getName());
                }
                o = new PathInterpolatorCompat(context, attributeSet, xmlPullParser);
            }
        }
    }
    
    public static Interpolator loadInterpolator(final Context context, final int n) throws Resources$NotFoundException {
        final Interpolator loadInterpolator;
        if (Build$VERSION.SDK_INT >= 21) {
            loadInterpolator = AnimationUtils.loadInterpolator(context, n);
        }
        else {
            Interpolator interpolatorFromXml = null;
            XmlResourceParser xmlResourceParser = null;
            XmlResourceParser animation = null;
            Label_0049: {
                if (n != 17563663) {
                    break Label_0049;
                }
                try {
                    final FastOutLinearInInterpolator fastOutLinearInInterpolator = new FastOutLinearInInterpolator();
                    if (false) {
                        throw new NullPointerException();
                    }
                    return loadInterpolator;
                    // iftrue(Label_0075:, n != 17563661)
                    // iftrue(Label_0014:, !false)
                    // iftrue(Label_0101:, n != 17563662)
                    Block_5: {
                        break Block_5;
                        Label_0101: {
                            interpolatorFromXml = createInterpolatorFromXml(context, context.getResources(), context.getTheme(), (XmlPullParser)(xmlResourceParser = (XmlResourceParser)(interpolatorFromXml = (Interpolator)(animation = context.getResources().getAnimation(n)))));
                        }
                        return interpolatorFromXml;
                        while (true) {
                            final LinearOutSlowInInterpolator linearOutSlowInInterpolator = new LinearOutSlowInInterpolator();
                            throw new NullPointerException();
                            Label_0075:
                            continue;
                        }
                    }
                    final FastOutSlowInInterpolator fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
                    // iftrue(Label_0014:, !false)
                    throw new NullPointerException();
                }
                catch (XmlPullParserException ex2) {
                    interpolatorFromXml = (Interpolator)animation;
                    final Resources$NotFoundException ex = new Resources$NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(n));
                    interpolatorFromXml = (Interpolator)animation;
                    ex.initCause((Throwable)ex2);
                    interpolatorFromXml = (Interpolator)animation;
                    throw ex;
                }
                catch (IOException ex4) {
                    interpolatorFromXml = (Interpolator)xmlResourceParser;
                    final Resources$NotFoundException ex3 = new Resources$NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(n));
                    interpolatorFromXml = (Interpolator)xmlResourceParser;
                    ex3.initCause((Throwable)ex4);
                    interpolatorFromXml = (Interpolator)xmlResourceParser;
                    throw ex3;
                }
                finally {
                    if (interpolatorFromXml != null) {
                        ((XmlResourceParser)interpolatorFromXml).close();
                    }
                }
            }
        }
        Label_0014: {
            return loadInterpolator;
        }
    }
}
