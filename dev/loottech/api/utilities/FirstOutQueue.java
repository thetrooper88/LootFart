package dev.loottech.api.utilities;

import com.google.common.base.Preconditions;
import com.google.common.collect.ForwardingQueue;
import com.google.common.collect.Iterables;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;

/*
 * Duplicate member names - consider using --renamedupmembers true
 */
public final class FirstOutQueue<E>
extends ForwardingQueue<E>
implements Serializable {
    final int maxSize;
    private final ArrayDeque<E> delegate;

    public FirstOutQueue(int maxSize) {
        Preconditions.checkArgument((maxSize >= 0 ? 1 : 0) != 0, (String)"maxSize (%s) must >= 0", (int)maxSize);
        this.delegate = new ArrayDeque(maxSize);
        this.maxSize = maxSize;
    }

    public int remainingCapacity() {
        return this.maxSize - this.size();
    }

    @NotNull
    protected ArrayDeque<E> delegate() {
        return this.delegate;
    }

    public boolean offer(E e) {
        return this.add(e);
    }

    public boolean add(E e) {
        Preconditions.checkNotNull(e);
        if (this.maxSize == 0) {
            return true;
        }
        if (this.size() == this.maxSize) {
            this.delegate.remove();
        }
        this.delegate.add(e);
        return true;
    }

    public E addFirst(E e) {
        Preconditions.checkNotNull(e);
        if (this.maxSize == 0) {
            return null;
        }
        Object removed = null;
        if (this.size() == this.maxSize) {
            removed = this.delegate.remove();
        }
        this.delegate.addFirst(e);
        return (E)removed;
    }

    public E getFirst() {
        return (E)this.delegate.getFirst();
    }

    public E getLast() {
        return (E)this.delegate.getLast();
    }

    public boolean addAll(Collection<? extends E> collection) {
        int size = collection.size();
        if (size >= this.maxSize) {
            this.clear();
            return Iterables.addAll((Collection)this, (Iterable)Iterables.skip(collection, (int)(size - this.maxSize)));
        }
        return this.standardAddAll(collection);
    }

    public Object[] toArray() {
        return super.toArray();
    }

    @NotNull
    protected Queue delegate() {
        return this.delegate();
    }

    @NotNull
    protected Collection delegate() {
        return this.delegate();
    }

    @NotNull
    protected Object delegate() {
        return this.delegate();
    }
}
