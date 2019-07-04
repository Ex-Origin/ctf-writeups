// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget
{
    protected ArrayList<ConstraintWidget> mChildren;
    
    public WidgetContainer() {
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2) {
        super(n, n2);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public WidgetContainer(final int n, final int n2, final int n3, final int n4) {
        super(n, n2, n3, n4);
        this.mChildren = new ArrayList<ConstraintWidget>();
    }
    
    public static Rectangle getBounds(final ArrayList<ConstraintWidget> list) {
        final Rectangle rectangle = new Rectangle();
        if (list.size() == 0) {
            return rectangle;
        }
        int n = Integer.MAX_VALUE;
        int n2 = 0;
        int n3 = Integer.MAX_VALUE;
        int n4 = 0;
        int x;
        int y;
        int right;
        int bottom;
        for (int i = 0; i < list.size(); ++i, n2 = right, n4 = bottom, n = x, n3 = y) {
            final ConstraintWidget constraintWidget = list.get(i);
            if (constraintWidget.getX() < (x = n)) {
                x = constraintWidget.getX();
            }
            if (constraintWidget.getY() < (y = n3)) {
                y = constraintWidget.getY();
            }
            if (constraintWidget.getRight() > (right = n2)) {
                right = constraintWidget.getRight();
            }
            if (constraintWidget.getBottom() > (bottom = n4)) {
                bottom = constraintWidget.getBottom();
            }
        }
        rectangle.setBounds(n, n3, n2 - n, n4 - n3);
        return rectangle;
    }
    
    public void add(final ConstraintWidget constraintWidget) {
        this.mChildren.add(constraintWidget);
        if (constraintWidget.getParent() != null) {
            ((WidgetContainer)constraintWidget.getParent()).remove(constraintWidget);
        }
        constraintWidget.setParent(this);
    }
    
    public ConstraintWidget findWidget(final float n, final float n2) {
        final WidgetContainer widgetContainer = null;
        final int drawX = this.getDrawX();
        final int drawY = this.getDrawY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        WidgetContainer widgetContainer2 = widgetContainer;
        if (n >= drawX) {
            widgetContainer2 = widgetContainer;
            if (n <= drawX + width) {
                widgetContainer2 = widgetContainer;
                if (n2 >= drawY) {
                    widgetContainer2 = widgetContainer;
                    if (n2 <= drawY + height) {
                        widgetContainer2 = this;
                    }
                }
            }
        }
        int i = 0;
        final int size = this.mChildren.size();
        WidgetContainer widgetContainer3 = widgetContainer2;
        while (i < size) {
            final ConstraintWidget constraintWidget = this.mChildren.get(i);
            WidgetContainer widgetContainer4;
            if (constraintWidget instanceof WidgetContainer) {
                final ConstraintWidget widget = ((WidgetContainer)constraintWidget).findWidget(n, n2);
                widgetContainer4 = widgetContainer3;
                if (widget != null) {
                    widgetContainer4 = (WidgetContainer)widget;
                }
            }
            else {
                final int drawX2 = constraintWidget.getDrawX();
                final int drawY2 = constraintWidget.getDrawY();
                final int width2 = constraintWidget.getWidth();
                final int height2 = constraintWidget.getHeight();
                widgetContainer4 = widgetContainer3;
                if (n >= drawX2) {
                    widgetContainer4 = widgetContainer3;
                    if (n <= drawX2 + width2) {
                        widgetContainer4 = widgetContainer3;
                        if (n2 >= drawY2) {
                            widgetContainer4 = widgetContainer3;
                            if (n2 <= drawY2 + height2) {
                                widgetContainer4 = (WidgetContainer)constraintWidget;
                            }
                        }
                    }
                }
            }
            ++i;
            widgetContainer3 = widgetContainer4;
        }
        return widgetContainer3;
    }
    
    public ArrayList<ConstraintWidget> findWidgets(int i, int size, final int n, final int n2) {
        final ArrayList<ConstraintWidget> list = new ArrayList<ConstraintWidget>();
        final Rectangle rectangle = new Rectangle();
        rectangle.setBounds(i, size, n, n2);
        ConstraintWidget constraintWidget;
        Rectangle rectangle2;
        for (i = 0, size = this.mChildren.size(); i < size; ++i) {
            constraintWidget = this.mChildren.get(i);
            rectangle2 = new Rectangle();
            rectangle2.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
            if (rectangle.intersects(rectangle2)) {
                list.add(constraintWidget);
            }
        }
        return list;
    }
    
    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }
    
    public ConstraintWidgetContainer getRootConstraintContainer() {
        final ConstraintWidget parent = this.getParent();
        ConstraintWidgetContainer constraintWidgetContainer = null;
        ConstraintWidget parent2 = parent;
        if (this instanceof ConstraintWidgetContainer) {
            constraintWidgetContainer = (ConstraintWidgetContainer)this;
            parent2 = parent;
        }
        while (true) {
            final ConstraintWidgetContainer constraintWidgetContainer2 = (ConstraintWidgetContainer)parent2;
            if (constraintWidgetContainer2 == null) {
                break;
            }
            parent2 = constraintWidgetContainer2.getParent();
            if (!(constraintWidgetContainer2 instanceof ConstraintWidgetContainer)) {
                continue;
            }
            constraintWidgetContainer = constraintWidgetContainer2;
            parent2 = parent2;
        }
        return constraintWidgetContainer;
    }
    
    public void layout() {
        this.updateDrawPosition();
        if (this.mChildren != null) {
            for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
                final ConstraintWidget constraintWidget = this.mChildren.get(i);
                if (constraintWidget instanceof WidgetContainer) {
                    ((WidgetContainer)constraintWidget).layout();
                }
            }
        }
    }
    
    public void remove(final ConstraintWidget constraintWidget) {
        this.mChildren.remove(constraintWidget);
        constraintWidget.setParent(null);
    }
    
    public void removeAllChildren() {
        this.mChildren.clear();
    }
    
    @Override
    public void reset() {
        this.mChildren.clear();
        super.reset();
    }
    
    @Override
    public void resetGroups() {
        super.resetGroups();
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetGroups();
        }
    }
    
    @Override
    public void resetSolverVariables(final Cache cache) {
        super.resetSolverVariables(cache);
        for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).resetSolverVariables(cache);
        }
    }
    
    @Override
    public void setOffset(int i, int size) {
        super.setOffset(i, size);
        for (size = this.mChildren.size(), i = 0; i < size; ++i) {
            this.mChildren.get(i).setOffset(this.getRootX(), this.getRootY());
        }
    }
    
    @Override
    public void updateDrawPosition() {
        super.updateDrawPosition();
        if (this.mChildren != null) {
            for (int size = this.mChildren.size(), i = 0; i < size; ++i) {
                final ConstraintWidget constraintWidget = this.mChildren.get(i);
                constraintWidget.setOffset(this.getDrawX(), this.getDrawY());
                if (!(constraintWidget instanceof ConstraintWidgetContainer)) {
                    constraintWidget.updateDrawPosition();
                }
            }
        }
    }
}
