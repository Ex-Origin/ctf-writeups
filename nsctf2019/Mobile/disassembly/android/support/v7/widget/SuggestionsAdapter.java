// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.widget;

import android.net.Uri$Builder;
import android.view.ViewGroup;
import java.util.List;
import android.content.res.Resources;
import java.io.FileNotFoundException;
import android.view.View;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.res.Resources$NotFoundException;
import android.support.v4.content.ContextCompat;
import android.net.Uri;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.content.ComponentName;
import android.text.style.TextAppearanceSpan;
import android.text.SpannableString;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.graphics.drawable.Drawable;
import android.database.Cursor;
import android.content.res.ColorStateList;
import android.app.SearchableInfo;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.Drawable$ConstantState;
import java.util.WeakHashMap;
import android.view.View$OnClickListener;
import android.support.v4.widget.ResourceCursorAdapter;

class SuggestionsAdapter extends ResourceCursorAdapter implements View$OnClickListener
{
    private static final boolean DBG = false;
    static final int INVALID_INDEX = -1;
    private static final String LOG_TAG = "SuggestionsAdapter";
    private static final int QUERY_LIMIT = 50;
    static final int REFINE_ALL = 2;
    static final int REFINE_BY_ENTRY = 1;
    static final int REFINE_NONE = 0;
    private boolean mClosed;
    private final int mCommitIconResId;
    private int mFlagsCol;
    private int mIconName1Col;
    private int mIconName2Col;
    private final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache;
    private final Context mProviderContext;
    private int mQueryRefinement;
    private final SearchManager mSearchManager;
    private final SearchView mSearchView;
    private final SearchableInfo mSearchable;
    private int mText1Col;
    private int mText2Col;
    private int mText2UrlCol;
    private ColorStateList mUrlColor;
    
    public SuggestionsAdapter(final Context mProviderContext, final SearchView mSearchView, final SearchableInfo mSearchable, final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache) {
        super(mProviderContext, mSearchView.getSuggestionRowLayout(), null, true);
        this.mClosed = false;
        this.mQueryRefinement = 1;
        this.mText1Col = -1;
        this.mText2Col = -1;
        this.mText2UrlCol = -1;
        this.mIconName1Col = -1;
        this.mIconName2Col = -1;
        this.mFlagsCol = -1;
        this.mSearchManager = (SearchManager)this.mContext.getSystemService("search");
        this.mSearchView = mSearchView;
        this.mSearchable = mSearchable;
        this.mCommitIconResId = mSearchView.getSuggestionCommitIconResId();
        this.mProviderContext = mProviderContext;
        this.mOutsideDrawablesCache = mOutsideDrawablesCache;
    }
    
    private Drawable checkIconCache(final String s) {
        final Drawable$ConstantState drawable$ConstantState = this.mOutsideDrawablesCache.get(s);
        if (drawable$ConstantState == null) {
            return null;
        }
        return drawable$ConstantState.newDrawable();
    }
    
    private CharSequence formatUrl(final CharSequence charSequence) {
        if (this.mUrlColor == null) {
            final TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(R.attr.textColorSearchUrl, typedValue, true);
            this.mUrlColor = this.mContext.getResources().getColorStateList(typedValue.resourceId);
        }
        final SpannableString spannableString = new SpannableString(charSequence);
        spannableString.setSpan((Object)new TextAppearanceSpan((String)null, 0, 0, this.mUrlColor, (ColorStateList)null), 0, charSequence.length(), 33);
        return (CharSequence)spannableString;
    }
    
    private Drawable getActivityIcon(final ComponentName componentName) {
        int iconResource;
        while (true) {
            final PackageManager packageManager = this.mContext.getPackageManager();
            ActivityInfo activityInfo;
            try {
                activityInfo = packageManager.getActivityInfo(componentName, 128);
                iconResource = activityInfo.getIconResource();
                if (iconResource == 0) {
                    return null;
                }
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.w("SuggestionsAdapter", ex.toString());
                return null;
            }
            Drawable drawable;
            if ((drawable = packageManager.getDrawable(componentName.getPackageName(), iconResource, activityInfo.applicationInfo)) == null) {
                break;
            }
            return drawable;
        }
        Log.w("SuggestionsAdapter", "Invalid icon resource " + iconResource + " for " + componentName.flattenToShortString());
        return null;
    }
    
    private Drawable getActivityIconWithCache(final ComponentName componentName) {
        final String flattenToShortString = componentName.flattenToShortString();
        if (!this.mOutsideDrawablesCache.containsKey(flattenToShortString)) {
            final Drawable activityIcon = this.getActivityIcon(componentName);
            Drawable$ConstantState constantState;
            if (activityIcon == null) {
                constantState = null;
            }
            else {
                constantState = activityIcon.getConstantState();
            }
            this.mOutsideDrawablesCache.put(flattenToShortString, constantState);
            return activityIcon;
        }
        final Drawable$ConstantState drawable$ConstantState = this.mOutsideDrawablesCache.get(flattenToShortString);
        if (drawable$ConstantState == null) {
            return null;
        }
        return drawable$ConstantState.newDrawable(this.mProviderContext.getResources());
    }
    
    public static String getColumnString(final Cursor cursor, final String s) {
        return getStringOrNull(cursor, cursor.getColumnIndex(s));
    }
    
    private Drawable getDefaultIcon1(final Cursor cursor) {
        final Drawable activityIconWithCache = this.getActivityIconWithCache(this.mSearchable.getSearchActivity());
        if (activityIconWithCache != null) {
            return activityIconWithCache;
        }
        return this.mContext.getPackageManager().getDefaultActivityIcon();
    }
    
    private Drawable getDrawable(final Uri p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: ldc_w           "android.resource"
        //     3: aload_1        
        //     4: invokevirtual   android/net/Uri.getScheme:()Ljava/lang/String;
        //     7: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    10: istore_2       
        //    11: iload_2        
        //    12: ifeq            94
        //    15: aload_0        
        //    16: aload_1        
        //    17: invokevirtual   android/support/v7/widget/SuggestionsAdapter.getDrawableFromResourceUri:(Landroid/net/Uri;)Landroid/graphics/drawable/Drawable;
        //    20: astore_3       
        //    21: aload_3        
        //    22: areturn        
        //    23: astore_3       
        //    24: new             Ljava/io/FileNotFoundException;
        //    27: dup            
        //    28: new             Ljava/lang/StringBuilder;
        //    31: dup            
        //    32: invokespecial   java/lang/StringBuilder.<init>:()V
        //    35: ldc_w           "Resource does not exist: "
        //    38: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    41: aload_1        
        //    42: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    45: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    48: invokespecial   java/io/FileNotFoundException.<init>:(Ljava/lang/String;)V
        //    51: athrow         
        //    52: astore_3       
        //    53: ldc             "SuggestionsAdapter"
        //    55: new             Ljava/lang/StringBuilder;
        //    58: dup            
        //    59: invokespecial   java/lang/StringBuilder.<init>:()V
        //    62: ldc_w           "Icon not found: "
        //    65: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    68: aload_1        
        //    69: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    72: ldc_w           ", "
        //    75: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    78: aload_3        
        //    79: invokevirtual   java/io/FileNotFoundException.getMessage:()Ljava/lang/String;
        //    82: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    85: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    88: invokestatic    android/util/Log.w:(Ljava/lang/String;Ljava/lang/String;)I
        //    91: pop            
        //    92: aconst_null    
        //    93: areturn        
        //    94: aload_0        
        //    95: getfield        android/support/v7/widget/SuggestionsAdapter.mProviderContext:Landroid/content/Context;
        //    98: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //   101: aload_1        
        //   102: invokevirtual   android/content/ContentResolver.openInputStream:(Landroid/net/Uri;)Ljava/io/InputStream;
        //   105: astore_3       
        //   106: aload_3        
        //   107: ifnonnull       138
        //   110: new             Ljava/io/FileNotFoundException;
        //   113: dup            
        //   114: new             Ljava/lang/StringBuilder;
        //   117: dup            
        //   118: invokespecial   java/lang/StringBuilder.<init>:()V
        //   121: ldc_w           "Failed to open "
        //   124: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   127: aload_1        
        //   128: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   131: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   134: invokespecial   java/io/FileNotFoundException.<init>:(Ljava/lang/String;)V
        //   137: athrow         
        //   138: aload_3        
        //   139: aconst_null    
        //   140: invokestatic    android/graphics/drawable/Drawable.createFromStream:(Ljava/io/InputStream;Ljava/lang/String;)Landroid/graphics/drawable/Drawable;
        //   143: astore          4
        //   145: aload_3        
        //   146: invokevirtual   java/io/InputStream.close:()V
        //   149: aload           4
        //   151: areturn        
        //   152: astore_3       
        //   153: ldc             "SuggestionsAdapter"
        //   155: new             Ljava/lang/StringBuilder;
        //   158: dup            
        //   159: invokespecial   java/lang/StringBuilder.<init>:()V
        //   162: ldc_w           "Error closing icon stream for "
        //   165: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   168: aload_1        
        //   169: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   172: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   175: aload_3        
        //   176: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   179: pop            
        //   180: aload           4
        //   182: areturn        
        //   183: astore          4
        //   185: aload_3        
        //   186: invokevirtual   java/io/InputStream.close:()V
        //   189: aload           4
        //   191: athrow         
        //   192: astore_3       
        //   193: ldc             "SuggestionsAdapter"
        //   195: new             Ljava/lang/StringBuilder;
        //   198: dup            
        //   199: invokespecial   java/lang/StringBuilder.<init>:()V
        //   202: ldc_w           "Error closing icon stream for "
        //   205: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   208: aload_1        
        //   209: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   212: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   215: aload_3        
        //   216: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   219: pop            
        //   220: goto            189
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                             
        //  -----  -----  -----  -----  -------------------------------------------------
        //  0      11     52     94     Ljava/io/FileNotFoundException;
        //  15     21     23     52     Landroid/content/res/Resources$NotFoundException;
        //  15     21     52     94     Ljava/io/FileNotFoundException;
        //  24     52     52     94     Ljava/io/FileNotFoundException;
        //  94     106    52     94     Ljava/io/FileNotFoundException;
        //  110    138    52     94     Ljava/io/FileNotFoundException;
        //  138    145    183    223    Any
        //  145    149    152    183    Ljava/io/IOException;
        //  145    149    52     94     Ljava/io/FileNotFoundException;
        //  153    180    52     94     Ljava/io/FileNotFoundException;
        //  185    189    192    223    Ljava/io/IOException;
        //  185    189    52     94     Ljava/io/FileNotFoundException;
        //  189    192    52     94     Ljava/io/FileNotFoundException;
        //  193    220    52     94     Ljava/io/FileNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 107, Size: 107
        //     at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        //     at java.util.ArrayList.get(ArrayList.java:433)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3303)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3551)
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
    
    private Drawable getDrawableFromResourceValue(final String s) {
        Drawable drawable;
        if (s == null || s.length() == 0 || "0".equals(s)) {
            drawable = null;
        }
        else {
            try {
                final int int1 = Integer.parseInt(s);
                final String string = "android.resource://" + this.mProviderContext.getPackageName() + "/" + int1;
                if ((drawable = this.checkIconCache(string)) == null) {
                    final Drawable drawable2 = ContextCompat.getDrawable(this.mProviderContext, int1);
                    this.storeInIconCache(string, drawable2);
                    return drawable2;
                }
            }
            catch (NumberFormatException ex) {
                if ((drawable = this.checkIconCache(s)) == null) {
                    final Drawable drawable3 = this.getDrawable(Uri.parse(s));
                    this.storeInIconCache(s, drawable3);
                    return drawable3;
                }
            }
            catch (Resources$NotFoundException ex2) {
                Log.w("SuggestionsAdapter", "Icon resource not found: " + s);
                return null;
            }
        }
        return drawable;
    }
    
    private Drawable getIcon1(final Cursor cursor) {
        Drawable drawableFromResourceValue;
        if (this.mIconName1Col == -1) {
            drawableFromResourceValue = null;
        }
        else if ((drawableFromResourceValue = this.getDrawableFromResourceValue(cursor.getString(this.mIconName1Col))) == null) {
            return this.getDefaultIcon1(cursor);
        }
        return drawableFromResourceValue;
    }
    
    private Drawable getIcon2(final Cursor cursor) {
        if (this.mIconName2Col == -1) {
            return null;
        }
        return this.getDrawableFromResourceValue(cursor.getString(this.mIconName2Col));
    }
    
    private static String getStringOrNull(final Cursor cursor, final int n) {
        if (n == -1) {
            return null;
        }
        try {
            return cursor.getString(n);
        }
        catch (Exception ex) {
            Log.e("SuggestionsAdapter", "unexpected error retrieving valid column from cursor, did the remote process die?", (Throwable)ex);
            return null;
        }
    }
    
    private void setViewDrawable(final ImageView imageView, final Drawable imageDrawable, final int visibility) {
        imageView.setImageDrawable(imageDrawable);
        if (imageDrawable == null) {
            imageView.setVisibility(visibility);
            return;
        }
        imageView.setVisibility(0);
        imageDrawable.setVisible(false, false);
        imageDrawable.setVisible(true, false);
    }
    
    private void setViewText(final TextView textView, final CharSequence text) {
        textView.setText(text);
        if (TextUtils.isEmpty(text)) {
            textView.setVisibility(8);
            return;
        }
        textView.setVisibility(0);
    }
    
    private void storeInIconCache(final String s, final Drawable drawable) {
        if (drawable != null) {
            this.mOutsideDrawablesCache.put(s, drawable.getConstantState());
        }
    }
    
    private void updateSpinnerState(final Cursor cursor) {
        Bundle extras;
        if (cursor != null) {
            extras = cursor.getExtras();
        }
        else {
            extras = null;
        }
        if (extras == null || extras.getBoolean("in_progress")) {}
    }
    
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ChildViewCache childViewCache = (ChildViewCache)view.getTag();
        int int1 = 0;
        if (this.mFlagsCol != -1) {
            int1 = cursor.getInt(this.mFlagsCol);
        }
        if (childViewCache.mText1 != null) {
            this.setViewText(childViewCache.mText1, getStringOrNull(cursor, this.mText1Col));
        }
        if (childViewCache.mText2 != null) {
            final String stringOrNull = getStringOrNull(cursor, this.mText2UrlCol);
            CharSequence charSequence;
            if (stringOrNull != null) {
                charSequence = this.formatUrl(stringOrNull);
            }
            else {
                charSequence = getStringOrNull(cursor, this.mText2Col);
            }
            if (TextUtils.isEmpty(charSequence)) {
                if (childViewCache.mText1 != null) {
                    childViewCache.mText1.setSingleLine(false);
                    childViewCache.mText1.setMaxLines(2);
                }
            }
            else if (childViewCache.mText1 != null) {
                childViewCache.mText1.setSingleLine(true);
                childViewCache.mText1.setMaxLines(1);
            }
            this.setViewText(childViewCache.mText2, charSequence);
        }
        if (childViewCache.mIcon1 != null) {
            this.setViewDrawable(childViewCache.mIcon1, this.getIcon1(cursor), 4);
        }
        if (childViewCache.mIcon2 != null) {
            this.setViewDrawable(childViewCache.mIcon2, this.getIcon2(cursor), 8);
        }
        if (this.mQueryRefinement == 2 || (this.mQueryRefinement == 1 && (int1 & 0x1) != 0x0)) {
            childViewCache.mIconRefine.setVisibility(0);
            childViewCache.mIconRefine.setTag((Object)childViewCache.mText1.getText());
            childViewCache.mIconRefine.setOnClickListener((View$OnClickListener)this);
            return;
        }
        childViewCache.mIconRefine.setVisibility(8);
    }
    
    public void changeCursor(final Cursor cursor) {
        if (this.mClosed) {
            Log.w("SuggestionsAdapter", "Tried to change cursor after adapter was closed.");
            if (cursor != null) {
                cursor.close();
            }
        }
        else {
            try {
                super.changeCursor(cursor);
                if (cursor != null) {
                    this.mText1Col = cursor.getColumnIndex("suggest_text_1");
                    this.mText2Col = cursor.getColumnIndex("suggest_text_2");
                    this.mText2UrlCol = cursor.getColumnIndex("suggest_text_2_url");
                    this.mIconName1Col = cursor.getColumnIndex("suggest_icon_1");
                    this.mIconName2Col = cursor.getColumnIndex("suggest_icon_2");
                    this.mFlagsCol = cursor.getColumnIndex("suggest_flags");
                }
            }
            catch (Exception ex) {
                Log.e("SuggestionsAdapter", "error changing cursor and caching columns", (Throwable)ex);
            }
        }
    }
    
    public void close() {
        this.changeCursor(null);
        this.mClosed = true;
    }
    
    public CharSequence convertToString(final Cursor cursor) {
        CharSequence columnString;
        if (cursor == null) {
            columnString = null;
        }
        else if ((columnString = getColumnString(cursor, "suggest_intent_query")) == null) {
            if (this.mSearchable.shouldRewriteQueryFromData()) {
                final String columnString2 = getColumnString(cursor, "suggest_intent_data");
                if (columnString2 != null) {
                    return columnString2;
                }
            }
            if (this.mSearchable.shouldRewriteQueryFromText()) {
                final String columnString3 = getColumnString(cursor, "suggest_text_1");
                if (columnString3 != null) {
                    return columnString3;
                }
            }
            return null;
        }
        return columnString;
    }
    
    Drawable getDrawableFromResourceUri(final Uri uri) throws FileNotFoundException {
        final String authority = uri.getAuthority();
        if (TextUtils.isEmpty((CharSequence)authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        }
        Resources resourcesForApplication;
        List pathSegments;
        try {
            resourcesForApplication = this.mContext.getPackageManager().getResourcesForApplication(authority);
            pathSegments = uri.getPathSegments();
            if (pathSegments == null) {
                throw new FileNotFoundException("No path: " + uri);
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            throw new FileNotFoundException("No package found for authority: " + uri);
        }
        final int size = pathSegments.size();
        while (true) {
            Label_0210: {
                if (size != 1) {
                    break Label_0210;
                }
                try {
                    final int n = Integer.parseInt(pathSegments.get(0));
                    if (n == 0) {
                        throw new FileNotFoundException("No resource found for: " + uri);
                    }
                    return resourcesForApplication.getDrawable(n);
                }
                catch (NumberFormatException ex2) {
                    throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                }
            }
            if (size == 2) {
                final int n = resourcesForApplication.getIdentifier((String)pathSegments.get(1), (String)pathSegments.get(0), authority);
                continue;
            }
            break;
        }
        throw new FileNotFoundException("More than two path segments: " + uri);
    }
    
    public View getDropDownView(final int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getDropDownView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException ex) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", (Throwable)ex);
            viewGroup = (ViewGroup)(view = this.newDropDownView(this.mContext, this.mCursor, viewGroup));
            if (viewGroup != null) {
                ((ChildViewCache)((View)viewGroup).getTag()).mText1.setText((CharSequence)ex.toString());
                return (View)viewGroup;
            }
            return view;
        }
    }
    
    public int getQueryRefinement() {
        return this.mQueryRefinement;
    }
    
    Cursor getSearchManagerSuggestions(final SearchableInfo searchableInfo, final String s, final int n) {
        if (searchableInfo != null) {
            final String suggestAuthority = searchableInfo.getSuggestAuthority();
            if (suggestAuthority != null) {
                final Uri$Builder fragment = new Uri$Builder().scheme("content").authority(suggestAuthority).query("").fragment("");
                final String suggestPath = searchableInfo.getSuggestPath();
                if (suggestPath != null) {
                    fragment.appendEncodedPath(suggestPath);
                }
                fragment.appendPath("search_suggest_query");
                final String suggestSelection = searchableInfo.getSuggestSelection();
                String[] array = null;
                if (suggestSelection != null) {
                    array = new String[] { s };
                }
                else {
                    fragment.appendPath(s);
                }
                if (n > 0) {
                    fragment.appendQueryParameter("limit", String.valueOf(n));
                }
                return this.mContext.getContentResolver().query(fragment.build(), (String[])null, suggestSelection, array, (String)null);
            }
        }
        return null;
    }
    
    public View getView(final int n, View view, ViewGroup viewGroup) {
        try {
            view = super.getView(n, view, viewGroup);
            return view;
        }
        catch (RuntimeException ex) {
            Log.w("SuggestionsAdapter", "Search suggestions cursor threw exception.", (Throwable)ex);
            viewGroup = (ViewGroup)(view = this.newView(this.mContext, this.mCursor, viewGroup));
            if (viewGroup != null) {
                ((ChildViewCache)((View)viewGroup).getTag()).mText1.setText((CharSequence)ex.toString());
                return (View)viewGroup;
            }
            return view;
        }
    }
    
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
        final View view = super.newView(context, cursor, viewGroup);
        view.setTag((Object)new ChildViewCache(view));
        ((ImageView)view.findViewById(R.id.edit_query)).setImageResource(this.mCommitIconResId);
        return view;
    }
    
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.updateSpinnerState(this.getCursor());
    }
    
    public void notifyDataSetInvalidated() {
        super.notifyDataSetInvalidated();
        this.updateSpinnerState(this.getCursor());
    }
    
    public void onClick(final View view) {
        final Object tag = view.getTag();
        if (tag instanceof CharSequence) {
            this.mSearchView.onQueryRefine((CharSequence)tag);
        }
    }
    
    public Cursor runQueryOnBackgroundThread(final CharSequence charSequence) {
        String string;
        if (charSequence == null) {
            string = "";
        }
        else {
            string = charSequence.toString();
        }
        if (this.mSearchView.getVisibility() == 0 && this.mSearchView.getWindowVisibility() == 0) {
            try {
                final Cursor searchManagerSuggestions = this.getSearchManagerSuggestions(this.mSearchable, string, 50);
                if (searchManagerSuggestions != null) {
                    searchManagerSuggestions.getCount();
                    return searchManagerSuggestions;
                }
            }
            catch (RuntimeException ex) {
                Log.w("SuggestionsAdapter", "Search suggestions query threw an exception.", (Throwable)ex);
                return null;
            }
        }
        return null;
    }
    
    public void setQueryRefinement(final int mQueryRefinement) {
        this.mQueryRefinement = mQueryRefinement;
    }
    
    private static final class ChildViewCache
    {
        public final ImageView mIcon1;
        public final ImageView mIcon2;
        public final ImageView mIconRefine;
        public final TextView mText1;
        public final TextView mText2;
        
        public ChildViewCache(final View view) {
            this.mText1 = (TextView)view.findViewById(16908308);
            this.mText2 = (TextView)view.findViewById(16908309);
            this.mIcon1 = (ImageView)view.findViewById(16908295);
            this.mIcon2 = (ImageView)view.findViewById(16908296);
            this.mIconRefine = (ImageView)view.findViewById(R.id.edit_query);
        }
    }
}
