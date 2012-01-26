package test;

import java.io.IOException;

import algo.Greedy;

import common.Utility;
import common.Data;
import common.Graph;

public class Test {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		Data.maxPValuesOnData();
		Data graphData = Data.getGraphData();
		// Map<String, Vertex> v = graphData.getVertices();
		// System.out.println("VertexSizeIs: " + v.size());
		// Vertex s = v.get("HSPG2");
		// System.out.println(s.getVertexZScore());

		// for (Vertex ver : v.values()) {
		// ArrayList<String> arr = ver.getNeighbors();
		// System.out.println(ver.getCode());
		// System.out.println("====================");
		// for (String s1 : arr) {
		// System.out.print(s1 + ", ");
		// }
		// }

		Graph graph = new Graph(Utility.getInitialVertex());
		Greedy greedy = new Greedy(graph);
	}

}
