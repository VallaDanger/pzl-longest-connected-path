package mx.chux.cs.pzl.matrix;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    
    public static void main( String[] args ) {
    	
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
	
	    final LongestConnectedPath<Integer> lcp = LongestConnectedPath.inMatrix(matrix);
	    
	    final Map<Integer, Set<MatrixNode<Integer>>> paths = lcp.findLongestConnectedPaths();
		
	    LOGGER.log(Level.INFO, "LCP size: {0}", paths.size());
	    LOGGER.log(Level.INFO, "LCP: {0}", paths);
	    
    }
    
}
