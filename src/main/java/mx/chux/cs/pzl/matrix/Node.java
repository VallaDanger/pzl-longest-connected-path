package mx.chux.cs.pzl.matrix;

import java.util.function.Supplier;

public class Node<T extends Comparable<T>> implements Supplier<T> {

	protected final T value;
	protected boolean visited;
	
	public static <T extends Comparable<T>> Node<T> fromValue(T value) {
		return new Node<>(value);
	}
	
	protected Node(T value) {
		this.value = value;
		this.visited = false;
	}
	
	@Override
	public T get() {
		return this.value;
	}
	
	public boolean hasBeenVisited() {
		return this.visited;
	}
	
	public void setVisited(final boolean visited) {
		this.visited = visited;
	}
	
	@Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("(");
        return builder.append(this.value).append(")").toString();
    }
	
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override 
    public boolean equals(Object other) {
        if( other instanceof Node<?> ) {
            return ((Node<?>) other).value.equals(this.value);
        }
        return false;
    }
	
}
