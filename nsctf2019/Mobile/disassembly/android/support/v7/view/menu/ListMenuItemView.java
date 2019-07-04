// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v7.view.menu;

import android.widget.CompoundButton;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.RadioButton;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.widget.LinearLayout;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class ListMenuItemView extends LinearLayout implements ItemView
{
    private static final String TAG = "ListMenuItemView";
    private Drawable mBackground;
    private CheckBox mCheckBox;
    private boolean mForceShowIcon;
    private ImageView mIconView;
    private LayoutInflater mInflater;
    private MenuItemImpl mItemData;
    private int mMenuType;
    private boolean mPreserveIconSpacing;
    private RadioButton mRadioButton;
    private TextView mShortcutView;
    private Drawable mSubMenuArrow;
    private ImageView mSubMenuArrowView;
    private int mTextAppearance;
    private Context mTextAppearanceContext;
    private TextView mTitleView;
    
    public ListMenuItemView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.listMenuViewStyle);
    }
    
    public ListMenuItemView(final Context mTextAppearanceContext, final AttributeSet set, final int n) {
        super(mTextAppearanceContext, set);
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.getContext(), set, R.styleable.MenuView, n, 0);
        this.mBackground = obtainStyledAttributes.getDrawable(R.styleable.MenuView_android_itemBackground);
        this.mTextAppearance = obtainStyledAttributes.getResourceId(R.styleable.MenuView_android_itemTextAppearance, -1);
        this.mPreserveIconSpacing = obtainStyledAttributes.getBoolean(R.styleable.MenuView_preserveIconSpacing, false);
        this.mTextAppearanceContext = mTextAppearanceContext;
        this.mSubMenuArrow = obtainStyledAttributes.getDrawable(R.styleable.MenuView_subMenuArrow);
        obtainStyledAttributes.recycle();
    }
    
    private LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.getContext());
        }
        return this.mInflater;
    }
    
    private void insertCheckBox() {
        this.addView((View)(this.mCheckBox = (CheckBox)this.getInflater().inflate(R.layout.abc_list_menu_item_checkbox, (ViewGroup)this, false)));
    }
    
    private void insertIconView() {
        this.addView((View)(this.mIconView = (ImageView)this.getInflater().inflate(R.layout.abc_list_menu_item_icon, (ViewGroup)this, false)), 0);
    }
    
    private void insertRadioButton() {
        this.addView((View)(this.mRadioButton = (RadioButton)this.getInflater().inflate(R.layout.abc_list_menu_item_radio, (ViewGroup)this, false)));
    }
    
    private void setSubMenuArrowVisible(final boolean b) {
        if (this.mSubMenuArrowView != null) {
            final ImageView mSubMenuArrowView = this.mSubMenuArrowView;
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            mSubMenuArrowView.setVisibility(visibility);
        }
    }
    
    public MenuItemImpl getItemData() {
        return this.mItemData;
    }
    
    public void initialize(final MenuItemImpl mItemData, int n) {
        this.mItemData = mItemData;
        this.mMenuType = n;
        if (mItemData.isVisible()) {
            n = 0;
        }
        else {
            n = 8;
        }
        this.setVisibility(n);
        this.setTitle(mItemData.getTitleForItemView(this));
        this.setCheckable(mItemData.isCheckable());
        this.setShortcut(mItemData.shouldShowShortcut(), mItemData.getShortcut());
        this.setIcon(mItemData.getIcon());
        ((MenuView.ItemView)this).setEnabled(mItemData.isEnabled());
        this.setSubMenuArrowVisible(mItemData.hasSubMenu());
        this.setContentDescription(mItemData.getContentDescription());
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        ViewCompat.setBackground((View)this, this.mBackground);
        this.mTitleView = (TextView)this.findViewById(R.id.title);
        if (this.mTextAppearance != -1) {
            this.mTitleView.setTextAppearance(this.mTextAppearanceContext, this.mTextAppearance);
        }
        this.mShortcutView = (TextView)this.findViewById(R.id.shortcut);
        this.mSubMenuArrowView = (ImageView)this.findViewById(R.id.submenuarrow);
        if (this.mSubMenuArrowView != null) {
            this.mSubMenuArrowView.setImageDrawable(this.mSubMenuArrow);
        }
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            final ViewGroup$LayoutParams layoutParams = this.getLayoutParams();
            final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.mIconView.getLayoutParams();
            if (layoutParams.height > 0 && linearLayout$LayoutParams.width <= 0) {
                linearLayout$LayoutParams.width = layoutParams.height;
            }
        }
        super.onMeasure(n, n2);
    }
    
    public boolean prefersCondensedTitle() {
        return false;
    }
    
    public void setCheckable(final boolean b) {
        if (b || this.mRadioButton != null || this.mCheckBox != null) {
            Object o;
            Object o2;
            if (this.mItemData.isExclusiveCheckable()) {
                if (this.mRadioButton == null) {
                    this.insertRadioButton();
                }
                o = this.mRadioButton;
                o2 = this.mCheckBox;
            }
            else {
                if (this.mCheckBox == null) {
                    this.insertCheckBox();
                }
                o = this.mCheckBox;
                o2 = this.mRadioButton;
            }
            if (b) {
                ((CompoundButton)o).setChecked(this.mItemData.isChecked());
                int visibility;
                if (b) {
                    visibility = 0;
                }
                else {
                    visibility = 8;
                }
                if (((CompoundButton)o).getVisibility() != visibility) {
                    ((CompoundButton)o).setVisibility(visibility);
                }
                if (o2 != null && ((CompoundButton)o2).getVisibility() != 8) {
                    ((CompoundButton)o2).setVisibility(8);
                }
            }
            else {
                if (this.mCheckBox != null) {
                    this.mCheckBox.setVisibility(8);
                }
                if (this.mRadioButton != null) {
                    this.mRadioButton.setVisibility(8);
                }
            }
        }
    }
    
    public void setChecked(final boolean checked) {
        Object o;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                this.insertRadioButton();
            }
            o = this.mRadioButton;
        }
        else {
            if (this.mCheckBox == null) {
                this.insertCheckBox();
            }
            o = this.mCheckBox;
        }
        ((CompoundButton)o).setChecked(checked);
    }
    
    public void setForceShowIcon(final boolean b) {
        this.mForceShowIcon = b;
        this.mPreserveIconSpacing = b;
    }
    
    public void setIcon(Drawable imageDrawable) {
        boolean b;
        if (this.mItemData.shouldShowIcon() || this.mForceShowIcon) {
            b = true;
        }
        else {
            b = false;
        }
        if ((b || this.mPreserveIconSpacing) && (this.mIconView != null || imageDrawable != null || this.mPreserveIconSpacing)) {
            if (this.mIconView == null) {
                this.insertIconView();
            }
            if (imageDrawable == null && !this.mPreserveIconSpacing) {
                this.mIconView.setVisibility(8);
                return;
            }
            final ImageView mIconView = this.mIconView;
            if (!b) {
                imageDrawable = null;
            }
            mIconView.setImageDrawable(imageDrawable);
            if (this.mIconView.getVisibility() != 0) {
                this.mIconView.setVisibility(0);
            }
        }
    }
    
    public void setShortcut(final boolean b, final char c) {
        int visibility;
        if (b && this.mItemData.shouldShowShortcut()) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        if (visibility == 0) {
            this.mShortcutView.setText((CharSequence)this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != visibility) {
            this.mShortcutView.setVisibility(visibility);
        }
    }
    
    public void setTitle(final CharSequence text) {
        if (text != null) {
            this.mTitleView.setText(text);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        }
        else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
    }
    
    public boolean showsIcon() {
        return this.mForceShowIcon;
    }
}
