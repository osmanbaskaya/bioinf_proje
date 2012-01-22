package common;

/**
 * @author Osman Baskaya
 */

import java.util.ArrayList;

public class Graph {

	private ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
	private double graphZScore;
	private boolean isChanged = true;

	public double getGraphZScore() {

		if (isChanged) {
			graphZScore = 0;
			for (Vertex v : vertexList) {
				graphZScore += v.getVertexZScore();
			}

			graphZScore /= Math.sqrt(vertexList.size());
		}

		return graphZScore;

	}

}
