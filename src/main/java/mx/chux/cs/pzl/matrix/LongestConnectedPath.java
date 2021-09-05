package mx.chux.cs.pzl.matrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class LongestConnectedPath<T extends Comparable<T>> {

	public static <T extends Comparable<T>> LongestConnectedPath<T> inMatrix(final T[][] matrix) {
		return new LongestConnectedPath<>(matrix);
	}
	
	private final ArrayList<ArrayList<MatrixNode<T>>> matrix;
	
	private final int sizeOfRows;
	private final int sizeOfCols;
	
	// frequencies will be lazy loaded
	final Map<T, Set<MatrixNode<T>>> frequencies;
	
	private LongestConnectedPath(final T[][] matrix) {
		
		this.sizeOfRows = matrix.length;
        this.sizeOfCols = matrix[0].length;
        
        this.matrix = new ArrayList<>(this.sizeOfRows);
        
        translateMatrix(matrix);
        
        // frequencies are just a memoization mechanism
        this.frequencies = new HashMap<>();
	}
	
	/*
	 * Translate matrix of values to matrix of Nodes
	 */
	private void translateMatrix(final T[][] matrix) {
		for( int i = 0 ; i < this.sizeOfRows ; i++ ) {
        	this.matrix.add(i, new ArrayList<>(this.sizeOfCols));
            for( int j = 0 ; j < this.sizeOfCols ; j++ ) {
            	this.matrix.get(i).add(j, buildNode(matrix[i][j], i, j));
            }
        }
	}
	
	private MatrixNode<T> buildNode(final T value, final int i, final int j) {
        return MatrixNode.fromValue(value, i, j);
    }
	
	public void reset() {
		this.frequencies.clear();
	}
	
	/*
	 * Will find the longest path for all values.
	 * not thread-safe
	 */
	public Map<T, Set<MatrixNode<T>>> findLongestConnectedPaths() { 
		
		if( !this.frequencies.isEmpty() ) {
			return Collections.unmodifiableMap(this.frequencies);
		}
		
		// perform DFS on every node inside the matrix
		for( int i = 0 ; i < this.sizeOfRows ; i++ ) {
            for( int j = 0 ; (j < this.sizeOfCols) ; j++ ) {
            	
            	final T value = this.matrix.get(i).get(j).get();
            	
            	// skip nulls
            	if( value == null ) {
            		continue;
            	}
            	
            	// initialize if necessary
            	this.frequencies.putIfAbsent(value, Collections.emptySet());
            	
            	// find path for current node using DFS
            	final Set<MatrixNode<T>> path = dfs(value, i, j);
            	
            	// update max if new Set is larger then previous one
            	this.frequencies.put(value, enforceMax(value, path));
            	
            }
        }
		
		resetMatrix();
		
		return Collections.unmodifiableMap(this.frequencies);
	}
	
	/*
	 * Will find the longest path of them all
	 */
    public Set<MatrixNode<T>> findLongestConnectedPath() {
        
        final Map<T, Set<MatrixNode<T>>> values = findLongestConnectedPaths();

        return findLongestConnectedPath(values);

    }
	
	/*
	 * Will find the longest path for specific value
	 */
    public Set<MatrixNode<T>> findLongestConnectedPath(final T value) {
        
        final Map<T, Set<MatrixNode<T>>> values = findLongestConnectedPaths();
        
        return values.containsKey(value)? values.get(value) : Collections.emptySet();

    }
    
	private Set<MatrixNode<T>> findLongestConnectedPath(final Map<T, Set<MatrixNode<T>>> values) { 
		Set<MatrixNode<T>> max = Collections.emptySet();
		for( final Set<MatrixNode<T>> value : values.values() ) {
			max = enforceMax(max, value);
		}
		return max;
	}

	private Set<MatrixNode<T>> enforceMax(T value, Set<MatrixNode<T>> path) {
		final Set<MatrixNode<T>> max = this.frequencies.get(value);
        return enforceMax(max, path);
    }
	
    private Set<MatrixNode<T>> enforceMax(final Set<MatrixNode<T>> max, Set<MatrixNode<T>> path) {
        if( (max == null) || (path.size() > max.size()) ) {
            return path;
        }
        return max;
    }

    /*
     * simple Depth First Search
     */
    private Set<MatrixNode<T>> dfs(T value, int i, int j) {
        
        final MatrixNode<T> root = this.matrix.get(i).get(j);

        if( root.hasBeenVisited() || (root.get().compareTo(value) != 0) ) {
            root.setVisited(true);
            return Collections.emptySet();
        }

        // backtrack nodes in path
        final Set<MatrixNode<T>> nodes = new HashSet<>();

        final Deque<MatrixNode<T>> s = new LinkedList<>();

        s.push(root);
   
        while( !s.isEmpty() ) {

        	MatrixNode<T> n = s.pop();

            if( n.hasBeenVisited() ) {
                continue;
            }

            n.setVisited(true);
            
            addAdjacents(s, n);

            nodes.add(n);
        }
       
        return nodes;

    }

    /*
     * Will try to add all possible adjacent nodes
     */
    private void addAdjacents(Deque<MatrixNode<T>> s, MatrixNode<T> n) {
        // left
        addAjacent(s, n.get(), n.i(), n.j()-1);
        // top
        addAjacent(s, n.get(), n.i()-1, n.j());
        // right
        addAjacent(s, n.get(), n.i(), n.j()+1);
        // bottom
        addAjacent(s, n.get(), n.i()+1, n.j());
    }

    private void addAjacent(Deque<MatrixNode<T>> s, T value, int i, int j) {
    
        // check if adjacency is within matrix bounds
        if( (i < 0) || (j < 0) || (i >= sizeOfRows) || (j >= sizeOfCols) ) {
            return;
        } 

        final MatrixNode<T> n = this.matrix.get(i).get(j);
       
        // check if node has already been visited in order to avoid loops
        // one non visited node could be adjacency of multiple visited nodes
        if( (n.get() == null) || n.hasBeenVisited() || s.contains(n) ) {
            return;
        }

        // Check if adjacency is valid
        // Only adjacent nodes with same value as current are valid
        // if this validation is omitted then the hole matrix will be traversed
        if( n.get().compareTo(value) == 0 ) {
            s.push(n);
        }

    }
    
    private void resetMatrix() {
    	for( int i = 0 ; i < this.sizeOfRows ; i++ ) {
            for( int j = 0 ; (j < this.sizeOfCols) ; j++ ) {
                this.matrix.get(i).get(j).setVisited(false);
            }
        }
    }

}
