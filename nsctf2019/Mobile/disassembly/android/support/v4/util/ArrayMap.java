// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.util;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.Map;

public class ArrayMap<K, V> extends SimpleArrayMap<K, V> implements Map<K, V>
{
    MapCollections<K, V> mCollections;
    
    public ArrayMap() {
    }
    
    public ArrayMap(final int n) {
        super(n);
    }
    
    public ArrayMap(final SimpleArrayMap simpleArrayMap) {
        super(simpleArrayMap);
    }
    
    private MapCollections<K, V> getCollection() {
        if (this.mCollections == null) {
            this.mCollections = new MapCollections<K, V>() {
                @Override
                protected void colClear() {
                    ArrayMap.this.clear();
                }
                
                @Override
                protected Object colGetEntry(final int n, final int n2) {
                    return ArrayMap.this.mArray[(n << 1) + n2];
                }
                
                @Override
                protected Map<K, V> colGetMap() {
                    return (Map<K, V>)ArrayMap.this;
                }
                
                @Override
                protected int colGetSize() {
                    return ArrayMap.this.mSize;
                }
                
                @Override
                protected int colIndexOfKey(final Object o) {
                    return ArrayMap.this.indexOfKey(o);
                }
                
                @Override
                protected int colIndexOfValue(final Object o) {
                    return ArrayMap.this.indexOfValue(o);
                }
                
                @Override
                protected void colPut(final K k, final V v) {
                    ArrayMap.this.put(k, v);
                }
                
                @Override
                protected void colRemoveAt(final int n) {
                    ArrayMap.this.removeAt(n);
                }
                
                @Override
                protected V colSetValue(final int n, final V v) {
                    return ArrayMap.this.setValueAt(n, v);
                }
            };
        }
        return this.mCollections;
    }
    
    public boolean containsAll(final Collection<?> collection) {
        return MapCollections.containsAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public Set<Entry<K, V>> entrySet() {
        return this.getCollection().getEntrySet();
    }
    
    @Override
    public Set<K> keySet() {
        return this.getCollection().getKeySet();
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        this.ensureCapacity(this.mSize + map.size());
        for (final Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put((K)entry.getKey(), (V)entry.getValue());
        }
    }
    
    public boolean removeAll(final Collection<?> collection) {
        return MapCollections.removeAllHelper((Map<Object, Object>)this, collection);
    }
    
    public boolean retainAll(final Collection<?> collection) {
        return MapCollections.retainAllHelper((Map<Object, Object>)this, collection);
    }
    
    @Override
    public Collection<V> values() {
        return this.getCollection().getValues();
    }
}
