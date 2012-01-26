package common;

/**
 * @author Osman Baskaya
 */

import java.util.ArrayList;

public class Graph {

	private ArrayList<Vertex> vertexList = new ArrayList<Vertex>();
	private double graphZScore;
	private boolean isChanged = true;

	private Vertex lastAddedVertex;

	public double getGraphZScore() {

		if (isChanged) {
			graphZScore = 0;
			for (Vertex v : vertexList) {
				graphZScore += v.getVertexZScore();
			}

			graphZScore /= Math.sqrt(vertexList.size());
			isChanged = false;
		}

		return graphZScore;
	}

	public boolean contains(Vertex v) {
		return vertexList.contains(v);
	}

	public Graph(Vertex startingVertex) {
		lastAddedVertex = startingVertex;
	}

	public final Vertex getLastAddedVertex() {
		return lastAddedVertex;
	}

	public void addVertex(Vertex v) {

		if (this.contains(v)) {
			throw new AssertionError("This vertex is already added!");
		}
		vertexList.add(v);
		lastAddedVertex = v;
		isChanged = true;
	}

}
