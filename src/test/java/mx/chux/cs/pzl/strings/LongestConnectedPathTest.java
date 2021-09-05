package mx.chux.cs.pzl.strings;

import org.junit.Before;
import org.junit.Test;

import mx.chux.cs.pzl.matrix.LongestConnectedPath;
import mx.chux.cs.pzl.matrix.MatrixNode;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Set;

public class LongestConnectedPathTest {

	LongestConnectedPath<Integer> testCase;
    
	private Integer[][] getTestMatrix() {
		
		/*
	        0 0 1 1
	        2 2 0 1
	        0 2 0 1
	        0 2 0 1
	    */ 
	
	    final Integer[][] matrix = new Integer[4][4];
	    
	    matrix[1][0] = Integer.valueOf(2);
	    matrix[1][1] = Integer.valueOf(2);
	    matrix[2][1] = Integer.valueOf(2);
	    matrix[3][1] = Integer.valueOf(2);
	
	    matrix[0][2] = Integer.valueOf(1);
	    matrix[0][3] = Integer.valueOf(1);
	    matrix[1][3] = Integer.valueOf(1);
	    matrix[2][3] = Integer.valueOf(1);
	    matrix[3][3] = Integer.valueOf(1);
	    
	    return matrix;
	    
	}
	
    @Before
    public void initialize() {
        testCase = LongestConnectedPath.inMatrix(getTestMatrix());
    }
    
    @Test
    public void notFoundTest() {
        assertThat(testCase.findLongestConnectedPath(3)).isEmpty();
    }
    
    @Test
    public void knownLongestPathTest() {
    	final Set<MatrixNode<Integer>> path = testCase.findLongestConnectedPath(1);
        assertThat(path).isNotEmpty().hasSize(5)
        .allMatch(node -> node.get().equals(Integer.valueOf(1)));
    }
    
    @Test
    public void unknownLongestPathTest() {
    	final Set<MatrixNode<Integer>> path = testCase.findLongestConnectedPath();
        assertThat(path).isNotEmpty().hasSize(5)
        .allMatch(node -> node.get().equals(Integer.valueOf(1)));
    }
    
    @Test
    public void longestPathTest() {
    	final Set<MatrixNode<Integer>> path = testCase.findLongestConnectedPath(2);
        assertThat(path).isNotEmpty().hasSize(4)
        .allMatch(node -> node.get().equals(Integer.valueOf(2)));
    }
    
    @Test
    public void longestPathsTest() {
    	final Map<Integer, Set<MatrixNode<Integer>>> paths = testCase.findLongestConnectedPaths();
        assertThat(paths).isNotEmpty().hasSize(2).containsOnlyKeys(1, 2);
    }
    
}
