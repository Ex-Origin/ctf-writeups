// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view;

import android.support.v7.widget.DrawableUtils;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.view.SubMenu;
import android.support.v4.view.MenuItemCompat;
import android.view.View;
import android.support.v7.view.menu.MenuItemWrapperICS;
import android.support.v7.view.menu.MenuItemImpl;
import java.lang.reflect.Constructor;
import android.util.Log;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import java.lang.reflect.Method;
import android.view.MenuItem$OnMenuItemClickListener;
import android.content.res.XmlResourceParser;
import android.view.InflateException;
import android.util.Xml;
import android.support.v4.internal.view.SupportMenu;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.view.Menu;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import android.content.ContextWrapper;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.RestrictTo;
import android.view.MenuInflater;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class SupportMenuInflater extends MenuInflater
{
    static final Class<?>[] ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE;
    static final Class<?>[] ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
    static final String LOG_TAG = "SupportMenuInflater";
    static final int NO_ID = 0;
    private static final String XML_GROUP = "group";
    private static final String XML_ITEM = "item";
    private static final String XML_MENU = "menu";
    final Object[] mActionProviderConstructorArguments;
    final Object[] mActionViewConstructorArguments;
    Context mContext;
    private Object mRealOwner;
    
    static {
        ACTION_VIEW_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class };
        ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE = SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE;
    }
    
    public SupportMenuInflater(final Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mActionViewConstructorArguments = new Object[] { mContext };
        this.mActionProviderConstructorArguments = this.mActionViewConstructorArguments;
    }
    
    private Object findRealOwner(final Object o) {
        if (!(o instanceof Activity) && o instanceof ContextWrapper) {
            return this.findRealOwner(((ContextWrapper)o).getBaseContext());
        }
        return o;
    }
    
    private void parseMenu(final XmlPullParser xmlPullParser, final AttributeSet set, final Menu menu) throws XmlPullParserException, IOException {
        final MenuState menuState = new MenuState(menu);
        int i = xmlPullParser.getEventType();
        int n = 0;
        String s = null;
        String name3;
        while (true) {
            while (i != 2) {
                final int next = xmlPullParser.next();
                if ((i = next) == 1) {
                    final int next2 = next;
                    int j = 0;
                    int next3 = next2;
                    while (j == 0) {
                        String name = null;
                        int n2 = 0;
                        int n3 = 0;
                        switch (next3) {
                            default: {
                                name = s;
                                n2 = j;
                                n3 = n;
                                break;
                            }
                            case 2: {
                                n3 = n;
                                n2 = j;
                                name = s;
                                if (n != 0) {
                                    break;
                                }
                                name = xmlPullParser.getName();
                                if (name.equals("group")) {
                                    menuState.readGroup(set);
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    break;
                                }
                                if (name.equals("item")) {
                                    menuState.readItem(set);
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    break;
                                }
                                if (name.equals("menu")) {
                                    this.parseMenu(xmlPullParser, set, (Menu)menuState.addSubMenuItem());
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    break;
                                }
                                n3 = 1;
                                n2 = j;
                                break;
                            }
                            case 3: {
                                final String name2 = xmlPullParser.getName();
                                if (n != 0 && name2.equals(s)) {
                                    n3 = 0;
                                    name = null;
                                    n2 = j;
                                    break;
                                }
                                if (name2.equals("group")) {
                                    menuState.resetGroup();
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    break;
                                }
                                if (name2.equals("item")) {
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    if (menuState.hasAddedItem()) {
                                        break;
                                    }
                                    if (menuState.itemActionProvider != null && menuState.itemActionProvider.hasSubMenu()) {
                                        menuState.addSubMenuItem();
                                        n3 = n;
                                        n2 = j;
                                        name = s;
                                        break;
                                    }
                                    menuState.addItem();
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    break;
                                }
                                else {
                                    n3 = n;
                                    n2 = j;
                                    name = s;
                                    if (name2.equals("menu")) {
                                        n2 = 1;
                                        n3 = n;
                                        name = s;
                                        break;
                                    }
                                    break;
                                }
                                break;
                            }
                            case 1: {
                                throw new RuntimeException("Unexpected end of document");
                            }
                        }
                        next3 = xmlPullParser.next();
                        n = n3;
                        j = n2;
                        s = name;
                    }
                    return;
                }
            }
            name3 = xmlPullParser.getName();
            if (name3.equals("menu")) {
                final int next2 = xmlPullParser.next();
                continue;
            }
            break;
        }
        throw new RuntimeException("Expecting menu, got " + name3);
    }
    
    Object getRealOwner() {
        if (this.mRealOwner == null) {
            this.mRealOwner = this.findRealOwner(this.mContext);
        }
        return this.mRealOwner;
    }
    
    public void inflate(final int n, final Menu menu) {
        if (!(menu instanceof SupportMenu)) {
            super.inflate(n, menu);
            return;
        }
        XmlResourceParser xmlResourceParser = null;
        XmlResourceParser xmlResourceParser2 = null;
        XmlResourceParser layout = null;
        try {
            final XmlResourceParser xmlResourceParser3 = xmlResourceParser2 = (xmlResourceParser = (layout = this.mContext.getResources().getLayout(n)));
            this.parseMenu((XmlPullParser)xmlResourceParser3, Xml.asAttributeSet((XmlPullParser)xmlResourceParser3), menu);
        }
        catch (XmlPullParserException ex) {
            xmlResourceParser = layout;
            throw new InflateException("Error inflating menu XML", (Throwable)ex);
        }
        catch (IOException ex2) {
            xmlResourceParser = xmlResourceParser2;
            throw new InflateException("Error inflating menu XML", (Throwable)ex2);
        }
        finally {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }
    
    private static class InflatedOnMenuItemClickListener implements MenuItem$OnMenuItemClickListener
    {
        private static final Class<?>[] PARAM_TYPES;
        private Method mMethod;
        private Object mRealOwner;
        
        static {
            PARAM_TYPES = new Class[] { MenuItem.class };
        }
        
        public InflatedOnMenuItemClickListener(final Object mRealOwner, final String s) {
            this.mRealOwner = mRealOwner;
            final Class<?> class1 = mRealOwner.getClass();
            try {
                this.mMethod = class1.getMethod(s, InflatedOnMenuItemClickListener.PARAM_TYPES);
            }
            catch (Exception ex2) {
                final InflateException ex = new InflateException("Couldn't resolve menu item onClick handler " + s + " in class " + class1.getName());
                ex.initCause((Throwable)ex2);
                throw ex;
            }
        }
        
        public boolean onMenuItemClick(final MenuItem menuItem) {
            try {
                if (this.mMethod.getReturnType() == Boolean.TYPE) {
                    return (boolean)this.mMethod.invoke(this.mRealOwner, menuItem);
                }
                this.mMethod.invoke(this.mRealOwner, menuItem);
                return true;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    
    private class MenuState
    {
        private static final int defaultGroupId = 0;
        private static final int defaultItemCategory = 0;
        private static final int defaultItemCheckable = 0;
        private static final boolean defaultItemChecked = false;
        private static final boolean defaultItemEnabled = true;
        private static final int defaultItemId = 0;
        private static final int defaultItemOrder = 0;
        private static final boolean defaultItemVisible = true;
        private int groupCategory;
        private int groupCheckable;
        private boolean groupEnabled;
        private int groupId;
        private int groupOrder;
        private boolean groupVisible;
        ActionProvider itemActionProvider;
        private String itemActionProviderClassName;
        private String itemActionViewClassName;
        private int itemActionViewLayout;
        private boolean itemAdded;
        private int itemAlphabeticModifiers;
        private char itemAlphabeticShortcut;
        private int itemCategoryOrder;
        private int itemCheckable;
        private boolean itemChecked;
        private CharSequence itemContentDescription;
        private boolean itemEnabled;
        private int itemIconResId;
        private ColorStateList itemIconTintList;
        private PorterDuff$Mode itemIconTintMode;
        private int itemId;
        private String itemListenerMethodName;
        private int itemNumericModifiers;
        private char itemNumericShortcut;
        private int itemShowAsAction;
        private CharSequence itemTitle;
        private CharSequence itemTitleCondensed;
        private CharSequence itemTooltipText;
        private boolean itemVisible;
        private Menu menu;
        
        public MenuState(final Menu menu) {
            this.itemIconTintList = null;
            this.itemIconTintMode = null;
            this.menu = menu;
            this.resetGroup();
        }
        
        private char getShortcut(final String s) {
            if (s == null) {
                return '\0';
            }
            return s.charAt(0);
        }
        
        private <T> T newInstance(final String s, final Class<?>[] array, final Object[] array2) {
            try {
                final Constructor<?> constructor = SupportMenuInflater.this.mContext.getClassLoader().loadClass(s).getConstructor(array);
                constructor.setAccessible(true);
                return (T)constructor.newInstance(array2);
            }
            catch (Exception ex) {
                Log.w("SupportMenuInflater", "Cannot instantiate class: " + s, (Throwable)ex);
                return null;
            }
        }
        
        private void setItem(final MenuItem menuItem) {
            menuItem.setChecked(this.itemChecked).setVisible(this.itemVisible).setEnabled(this.itemEnabled).setCheckable(this.itemCheckable >= 1).setTitleCondensed(this.itemTitleCondensed).setIcon(this.itemIconResId);
            if (this.itemShowAsAction >= 0) {
                menuItem.setShowAsAction(this.itemShowAsAction);
            }
            if (this.itemListenerMethodName != null) {
                if (SupportMenuInflater.this.mContext.isRestricted()) {
                    throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
                }
                menuItem.setOnMenuItemClickListener((MenuItem$OnMenuItemClickListener)new InflatedOnMenuItemClickListener(SupportMenuInflater.this.getRealOwner(), this.itemListenerMethodName));
            }
            if (menuItem instanceof MenuItemImpl) {
                final MenuItemImpl menuItemImpl = (MenuItemImpl)menuItem;
            }
            if (this.itemCheckable >= 2) {
                if (menuItem instanceof MenuItemImpl) {
                    ((MenuItemImpl)menuItem).setExclusiveCheckable(true);
                }
                else if (menuItem instanceof MenuItemWrapperICS) {
                    ((MenuItemWrapperICS)menuItem).setExclusiveCheckable(true);
                }
            }
            boolean b = false;
            if (this.itemActionViewClassName != null) {
                menuItem.setActionView((View)this.newInstance(this.itemActionViewClassName, SupportMenuInflater.ACTION_VIEW_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionViewConstructorArguments));
                b = true;
            }
            if (this.itemActionViewLayout > 0) {
                if (!b) {
                    menuItem.setActionView(this.itemActionViewLayout);
                }
                else {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'itemActionViewLayout'. Action view already specified.");
                }
            }
            if (this.itemActionProvider != null) {
                MenuItemCompat.setActionProvider(menuItem, this.itemActionProvider);
            }
            MenuItemCompat.setContentDescription(menuItem, this.itemContentDescription);
            MenuItemCompat.setTooltipText(menuItem, this.itemTooltipText);
            MenuItemCompat.setAlphabeticShortcut(menuItem, this.itemAlphabeticShortcut, this.itemAlphabeticModifiers);
            MenuItemCompat.setNumericShortcut(menuItem, this.itemNumericShortcut, this.itemNumericModifiers);
            if (this.itemIconTintMode != null) {
                MenuItemCompat.setIconTintMode(menuItem, this.itemIconTintMode);
            }
            if (this.itemIconTintList != null) {
                MenuItemCompat.setIconTintList(menuItem, this.itemIconTintList);
            }
        }
        
        public void addItem() {
            this.itemAdded = true;
            this.setItem(this.menu.add(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle));
        }
        
        public SubMenu addSubMenuItem() {
            this.itemAdded = true;
            final SubMenu addSubMenu = this.menu.addSubMenu(this.groupId, this.itemId, this.itemCategoryOrder, this.itemTitle);
            this.setItem(addSubMenu.getItem());
            return addSubMenu;
        }
        
        public boolean hasAddedItem() {
            return this.itemAdded;
        }
        
        public void readGroup(final AttributeSet set) {
            final TypedArray obtainStyledAttributes = SupportMenuInflater.this.mContext.obtainStyledAttributes(set, R.styleable.MenuGroup);
            this.groupId = obtainStyledAttributes.getResourceId(R.styleable.MenuGroup_android_id, 0);
            this.groupCategory = obtainStyledAttributes.getInt(R.styleable.MenuGroup_android_menuCategory, 0);
            this.groupOrder = obtainStyledAttributes.getInt(R.styleable.MenuGroup_android_orderInCategory, 0);
            this.groupCheckable = obtainStyledAttributes.getInt(R.styleable.MenuGroup_android_checkableBehavior, 0);
            this.groupVisible = obtainStyledAttributes.getBoolean(R.styleable.MenuGroup_android_visible, true);
            this.groupEnabled = obtainStyledAttributes.getBoolean(R.styleable.MenuGroup_android_enabled, true);
            obtainStyledAttributes.recycle();
        }
        
        public void readItem(final AttributeSet set) {
            final TypedArray obtainStyledAttributes = SupportMenuInflater.this.mContext.obtainStyledAttributes(set, R.styleable.MenuItem);
            this.itemId = obtainStyledAttributes.getResourceId(R.styleable.MenuItem_android_id, 0);
            this.itemCategoryOrder = ((0xFFFF0000 & obtainStyledAttributes.getInt(R.styleable.MenuItem_android_menuCategory, this.groupCategory)) | (0xFFFF & obtainStyledAttributes.getInt(R.styleable.MenuItem_android_orderInCategory, this.groupOrder)));
            this.itemTitle = obtainStyledAttributes.getText(R.styleable.MenuItem_android_title);
            this.itemTitleCondensed = obtainStyledAttributes.getText(R.styleable.MenuItem_android_titleCondensed);
            this.itemIconResId = obtainStyledAttributes.getResourceId(R.styleable.MenuItem_android_icon, 0);
            this.itemAlphabeticShortcut = this.getShortcut(obtainStyledAttributes.getString(R.styleable.MenuItem_android_alphabeticShortcut));
            this.itemAlphabeticModifiers = obtainStyledAttributes.getInt(R.styleable.MenuItem_alphabeticModifiers, 4096);
            this.itemNumericShortcut = this.getShortcut(obtainStyledAttributes.getString(R.styleable.MenuItem_android_numericShortcut));
            this.itemNumericModifiers = obtainStyledAttributes.getInt(R.styleable.MenuItem_numericModifiers, 4096);
            if (obtainStyledAttributes.hasValue(R.styleable.MenuItem_android_checkable)) {
                int itemCheckable;
                if (obtainStyledAttributes.getBoolean(R.styleable.MenuItem_android_checkable, false)) {
                    itemCheckable = 1;
                }
                else {
                    itemCheckable = 0;
                }
                this.itemCheckable = itemCheckable;
            }
            else {
                this.itemCheckable = this.groupCheckable;
            }
            this.itemChecked = obtainStyledAttributes.getBoolean(R.styleable.MenuItem_android_checked, false);
            this.itemVisible = obtainStyledAttributes.getBoolean(R.styleable.MenuItem_android_visible, this.groupVisible);
            this.itemEnabled = obtainStyledAttributes.getBoolean(R.styleable.MenuItem_android_enabled, this.groupEnabled);
            this.itemShowAsAction = obtainStyledAttributes.getInt(R.styleable.MenuItem_showAsAction, -1);
            this.itemListenerMethodName = obtainStyledAttributes.getString(R.styleable.MenuItem_android_onClick);
            this.itemActionViewLayout = obtainStyledAttributes.getResourceId(R.styleable.MenuItem_actionLayout, 0);
            this.itemActionViewClassName = obtainStyledAttributes.getString(R.styleable.MenuItem_actionViewClass);
            this.itemActionProviderClassName = obtainStyledAttributes.getString(R.styleable.MenuItem_actionProviderClass);
            boolean b;
            if (this.itemActionProviderClassName != null) {
                b = true;
            }
            else {
                b = false;
            }
            if (b && this.itemActionViewLayout == 0 && this.itemActionViewClassName == null) {
                this.itemActionProvider = this.newInstance(this.itemActionProviderClassName, SupportMenuInflater.ACTION_PROVIDER_CONSTRUCTOR_SIGNATURE, SupportMenuInflater.this.mActionProviderConstructorArguments);
            }
            else {
                if (b) {
                    Log.w("SupportMenuInflater", "Ignoring attribute 'actionProviderClass'. Action view already specified.");
                }
                this.itemActionProvider = null;
            }
            this.itemContentDescription = obtainStyledAttributes.getText(R.styleable.MenuItem_contentDescription);
            this.itemTooltipText = obtainStyledAttributes.getText(R.styleable.MenuItem_tooltipText);
            if (obtainStyledAttributes.hasValue(R.styleable.MenuItem_iconTintMode)) {
                this.itemIconTintMode = DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.MenuItem_iconTintMode, -1), this.itemIconTintMode);
            }
            else {
                this.itemIconTintMode = null;
            }
            if (obtainStyledAttributes.hasValue(R.styleable.MenuItem_iconTint)) {
                this.itemIconTintList = obtainStyledAttributes.getColorStateList(R.styleable.MenuItem_iconTint);
            }
            else {
                this.itemIconTintList = null;
            }
            obtainStyledAttributes.recycle();
            this.itemAdded = false;
        }
        
        public void resetGroup() {
            this.groupId = 0;
            this.groupCategory = 0;
            this.groupOrder = 0;
            this.groupCheckable = 0;
            this.groupVisible = true;
            this.groupEnabled = true;
        }
    }
}
