package common;

import java.util.Random;


public class Utility {

	
	public static Vertex getInitialVertex() {


		Data graphData = Data.getGraphData();

		Random r = new Random();
		int d = r.nextInt(graphData.getVertices().size());
		Object[] k = graphData.getVertices().keySet().toArray();

		// System.out.println("Number value d is " + d);
		// System.out.println("Number of vertex is " + k.length);

		return graphData.getVertices().get(k[d]);

	}
	
	
}
