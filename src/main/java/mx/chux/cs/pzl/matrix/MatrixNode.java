package mx.chux.cs.pzl.matrix;

import java.util.Objects;

public class MatrixNode<T extends Comparable<T>> extends Node<T> {

	public static <T extends Comparable<T>> MatrixNode<T> fromValue(T value, int i, int j) {
		return new MatrixNode<>(value, i, j);
	}
	
	private int i;
	private int j;
	
	protected MatrixNode(T value, int i, int j) {
		super(value);
		this.i = i;
		this.j = j;
	}
	
	public int i() {
		return this.i;
	}
	
	public int j() {
		return this.j;
	}
	
	protected void setI(int i) {
		this.i = i;
	}
	
	protected void setJ(int j) {
		this.j = j;
	}
	
	@Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(super.toString());
        builder.append("@[").append(this.i).append(',').append(this.j).append(']');
        return builder.toString();
	}
	
    @Override
    public int hashCode() {
    	return Objects.hash(super.hashCode(), this.i, this.j);
    }
    
    @Override 
    public boolean equals(Object other) {
    	if( other instanceof MatrixNode<?> ) {
    		final MatrixNode<?> n = (MatrixNode<?>) other;
            return n.value.equals(this.value) 
            		&& n.i == this.i && n.j == this.j;
        }
        return false;
    }
	
}
