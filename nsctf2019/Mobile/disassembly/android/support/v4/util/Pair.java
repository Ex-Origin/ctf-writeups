// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

public class Pair<F, S>
{
    public final F first;
    public final S second;
    
    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }
    
    public static <A, B> Pair<A, B> create(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }
    
    private static boolean objectsEqual(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof Pair) {
            final Pair pair = (Pair)o;
            if (objectsEqual(pair.first, this.first) && objectsEqual(pair.second, this.second)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.first == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.first.hashCode();
        }
        if (this.second != null) {
            hashCode = this.second.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
    
    @Override
    public String toString() {
        return "Pair{" + String.valueOf(this.first) + " " + String.valueOf(this.second) + "}";
    }
}
