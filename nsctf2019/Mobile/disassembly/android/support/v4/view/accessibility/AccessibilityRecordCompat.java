// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view.accessibility;

import android.support.annotation.RequiresApi;
import java.util.List;
import android.os.Parcelable;
import android.view.View;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.view.accessibility.AccessibilityRecord;

public class AccessibilityRecordCompat
{
    private static final AccessibilityRecordCompatBaseImpl IMPL;
    private final AccessibilityRecord mRecord;
    
    static {
        if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (AccessibilityRecordCompatBaseImpl)new AccessibilityRecordCompatApi16Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 15) {
            IMPL = (AccessibilityRecordCompatBaseImpl)new AccessibilityRecordCompatApi15Impl();
            return;
        }
        IMPL = new AccessibilityRecordCompatBaseImpl();
    }
    
    public AccessibilityRecordCompat(final Object o) {
        this.mRecord = (AccessibilityRecord)o;
    }
    
    public static int getMaxScrollX(final AccessibilityRecord accessibilityRecord) {
        return AccessibilityRecordCompat.IMPL.getMaxScrollX(accessibilityRecord);
    }
    
    public static int getMaxScrollY(final AccessibilityRecord accessibilityRecord) {
        return AccessibilityRecordCompat.IMPL.getMaxScrollY(accessibilityRecord);
    }
    
    @Deprecated
    public static AccessibilityRecordCompat obtain() {
        return new AccessibilityRecordCompat(AccessibilityRecord.obtain());
    }
    
    @Deprecated
    public static AccessibilityRecordCompat obtain(final AccessibilityRecordCompat accessibilityRecordCompat) {
        return new AccessibilityRecordCompat(AccessibilityRecord.obtain(accessibilityRecordCompat.mRecord));
    }
    
    public static void setMaxScrollX(final AccessibilityRecord accessibilityRecord, final int n) {
        AccessibilityRecordCompat.IMPL.setMaxScrollX(accessibilityRecord, n);
    }
    
    public static void setMaxScrollY(final AccessibilityRecord accessibilityRecord, final int n) {
        AccessibilityRecordCompat.IMPL.setMaxScrollY(accessibilityRecord, n);
    }
    
    public static void setSource(@NonNull final AccessibilityRecord accessibilityRecord, final View view, final int n) {
        AccessibilityRecordCompat.IMPL.setSource(accessibilityRecord, view, n);
    }
    
    @Deprecated
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final AccessibilityRecordCompat accessibilityRecordCompat = (AccessibilityRecordCompat)o;
            if (this.mRecord == null) {
                if (accessibilityRecordCompat.mRecord != null) {
                    return false;
                }
            }
            else if (!this.mRecord.equals(accessibilityRecordCompat.mRecord)) {
                return false;
            }
        }
        return true;
    }
    
    @Deprecated
    public int getAddedCount() {
        return this.mRecord.getAddedCount();
    }
    
    @Deprecated
    public CharSequence getBeforeText() {
        return this.mRecord.getBeforeText();
    }
    
    @Deprecated
    public CharSequence getClassName() {
        return this.mRecord.getClassName();
    }
    
    @Deprecated
    public CharSequence getContentDescription() {
        return this.mRecord.getContentDescription();
    }
    
    @Deprecated
    public int getCurrentItemIndex() {
        return this.mRecord.getCurrentItemIndex();
    }
    
    @Deprecated
    public int getFromIndex() {
        return this.mRecord.getFromIndex();
    }
    
    @Deprecated
    public Object getImpl() {
        return this.mRecord;
    }
    
    @Deprecated
    public int getItemCount() {
        return this.mRecord.getItemCount();
    }
    
    @Deprecated
    public int getMaxScrollX() {
        return getMaxScrollX(this.mRecord);
    }
    
    @Deprecated
    public int getMaxScrollY() {
        return getMaxScrollY(this.mRecord);
    }
    
    @Deprecated
    public Parcelable getParcelableData() {
        return this.mRecord.getParcelableData();
    }
    
    @Deprecated
    public int getRemovedCount() {
        return this.mRecord.getRemovedCount();
    }
    
    @Deprecated
    public int getScrollX() {
        return this.mRecord.getScrollX();
    }
    
    @Deprecated
    public int getScrollY() {
        return this.mRecord.getScrollY();
    }
    
    @Deprecated
    public AccessibilityNodeInfoCompat getSource() {
        return AccessibilityNodeInfoCompat.wrapNonNullInstance(this.mRecord.getSource());
    }
    
    @Deprecated
    public List<CharSequence> getText() {
        return (List<CharSequence>)this.mRecord.getText();
    }
    
    @Deprecated
    public int getToIndex() {
        return this.mRecord.getToIndex();
    }
    
    @Deprecated
    public int getWindowId() {
        return this.mRecord.getWindowId();
    }
    
    @Deprecated
    @Override
    public int hashCode() {
        if (this.mRecord == null) {
            return 0;
        }
        return this.mRecord.hashCode();
    }
    
    @Deprecated
    public boolean isChecked() {
        return this.mRecord.isChecked();
    }
    
    @Deprecated
    public boolean isEnabled() {
        return this.mRecord.isEnabled();
    }
    
    @Deprecated
    public boolean isFullScreen() {
        return this.mRecord.isFullScreen();
    }
    
    @Deprecated
    public boolean isPassword() {
        return this.mRecord.isPassword();
    }
    
    @Deprecated
    public boolean isScrollable() {
        return this.mRecord.isScrollable();
    }
    
    @Deprecated
    public void recycle() {
        this.mRecord.recycle();
    }
    
    @Deprecated
    public void setAddedCount(final int addedCount) {
        this.mRecord.setAddedCount(addedCount);
    }
    
    @Deprecated
    public void setBeforeText(final CharSequence beforeText) {
        this.mRecord.setBeforeText(beforeText);
    }
    
    @Deprecated
    public void setChecked(final boolean checked) {
        this.mRecord.setChecked(checked);
    }
    
    @Deprecated
    public void setClassName(final CharSequence className) {
        this.mRecord.setClassName(className);
    }
    
    @Deprecated
    public void setContentDescription(final CharSequence contentDescription) {
        this.mRecord.setContentDescription(contentDescription);
    }
    
    @Deprecated
    public void setCurrentItemIndex(final int currentItemIndex) {
        this.mRecord.setCurrentItemIndex(currentItemIndex);
    }
    
    @Deprecated
    public void setEnabled(final boolean enabled) {
        this.mRecord.setEnabled(enabled);
    }
    
    @Deprecated
    public void setFromIndex(final int fromIndex) {
        this.mRecord.setFromIndex(fromIndex);
    }
    
    @Deprecated
    public void setFullScreen(final boolean fullScreen) {
        this.mRecord.setFullScreen(fullScreen);
    }
    
    @Deprecated
    public void setItemCount(final int itemCount) {
        this.mRecord.setItemCount(itemCount);
    }
    
    @Deprecated
    public void setMaxScrollX(final int n) {
        setMaxScrollX(this.mRecord, n);
    }
    
    @Deprecated
    public void setMaxScrollY(final int n) {
        setMaxScrollY(this.mRecord, n);
    }
    
    @Deprecated
    public void setParcelableData(final Parcelable parcelableData) {
        this.mRecord.setParcelableData(parcelableData);
    }
    
    @Deprecated
    public void setPassword(final boolean password) {
        this.mRecord.setPassword(password);
    }
    
    @Deprecated
    public void setRemovedCount(final int removedCount) {
        this.mRecord.setRemovedCount(removedCount);
    }
    
    @Deprecated
    public void setScrollX(final int scrollX) {
        this.mRecord.setScrollX(scrollX);
    }
    
    @Deprecated
    public void setScrollY(final int scrollY) {
        this.mRecord.setScrollY(scrollY);
    }
    
    @Deprecated
    public void setScrollable(final boolean scrollable) {
        this.mRecord.setScrollable(scrollable);
    }
    
    @Deprecated
    public void setSource(final View source) {
        this.mRecord.setSource(source);
    }
    
    @Deprecated
    public void setSource(final View view, final int n) {
        setSource(this.mRecord, view, n);
    }
    
    @Deprecated
    public void setToIndex(final int toIndex) {
        this.mRecord.setToIndex(toIndex);
    }
    
    @RequiresApi(15)
    static class AccessibilityRecordCompatApi15Impl extends AccessibilityRecordCompatBaseImpl
    {
        @Override
        public int getMaxScrollX(final AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollX();
        }
        
        @Override
        public int getMaxScrollY(final AccessibilityRecord accessibilityRecord) {
            return accessibilityRecord.getMaxScrollY();
        }
        
        @Override
        public void setMaxScrollX(final AccessibilityRecord accessibilityRecord, final int maxScrollX) {
            accessibilityRecord.setMaxScrollX(maxScrollX);
        }
        
        @Override
        public void setMaxScrollY(final AccessibilityRecord accessibilityRecord, final int maxScrollY) {
            accessibilityRecord.setMaxScrollY(maxScrollY);
        }
    }
    
    @RequiresApi(16)
    static class AccessibilityRecordCompatApi16Impl extends AccessibilityRecordCompatApi15Impl
    {
        @Override
        public void setSource(final AccessibilityRecord accessibilityRecord, final View view, final int n) {
            accessibilityRecord.setSource(view, n);
        }
    }
    
    static class AccessibilityRecordCompatBaseImpl
    {
        public int getMaxScrollX(final AccessibilityRecord accessibilityRecord) {
            return 0;
        }
        
        public int getMaxScrollY(final AccessibilityRecord accessibilityRecord) {
            return 0;
        }
        
        public void setMaxScrollX(final AccessibilityRecord accessibilityRecord, final int n) {
        }
        
        public void setMaxScrollY(final AccessibilityRecord accessibilityRecord, final int n) {
        }
        
        public void setSource(final AccessibilityRecord accessibilityRecord, final View view, final int n) {
        }
    }
}
