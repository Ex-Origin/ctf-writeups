// 
// Decompiled by Procyon v0.5.30
// 

package android.support.constraint.solver.widgets;

public class Rectangle
{
    public int height;
    public int width;
    public int x;
    public int y;
    
    public boolean contains(final int n, final int n2) {
        return n >= this.x && n < this.x + this.width && n2 >= this.y && n2 < this.y + this.height;
    }
    
    public int getCenterX() {
        return (this.x + this.width) / 2;
    }
    
    public int getCenterY() {
        return (this.y + this.height) / 2;
    }
    
    void grow(final int n, final int n2) {
        this.x -= n;
        this.y -= n2;
        this.width += n * 2;
        this.height += n2 * 2;
    }
    
    boolean intersects(final Rectangle rectangle) {
        return this.x >= rectangle.x && this.x < rectangle.x + rectangle.width && this.y >= rectangle.y && this.y < rectangle.y + rectangle.height;
    }
    
    public void setBounds(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
