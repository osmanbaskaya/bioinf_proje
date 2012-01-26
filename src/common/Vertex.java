package common;

/**
 * @author Osman Baskaya
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Vertex implements Comparable<Vertex> {

	private final String code;
	private ArrayList<String> neighbors = new ArrayList<String>();

	private static final byte numberOfZScores = 2;
	private double[] zScores;
	private double[] neighborsZScore = null;
	private boolean isChanged = true;

	// weights[0] = current node's weight, weights[1] = node's neighbors'
	// weights.
	private static final double[] weights = { 0.5, 0.5 };

	private final double vertexZScore;

	public Vertex(String code, double[] zScores) {
		this.code = code;
		this.zScores = zScores;
		vertexZScore = calculateEuclideanSquare(zScores);

		// Arrays.fill(neighborsZScore, Double.NaN);
	}

	public void addNeighbors(String neighborsCode) {
		if (!neighbors.contains(neighborsCode)) {
			neighbors.add(neighborsCode);
			isChanged = true;
		}
	}

	// public double[] getMeanTotalZScore() {
	// double[] meanZScore = getTotalZScore();
	// int numberOfNeighbors = neighbors.size();
	// for (byte i = 0; i < numberOfZScores; i++) {
	// meanZScore[i] = meanZScore[i] / (numberOfNeighbors + 1);
	// }
	// return meanZScore.clone();
	// }

	public double[] getTotalZScore() throws IOException {
		double[] meanZScore = getNeighborsZScore();
		for (byte i = 0; i < numberOfZScores; i++) {
			meanZScore[i] += zScores[i];
		}
		return meanZScore;
	}

	public double[] getMeanNeighborsZScore() throws IOException {
		double[] meanZScore = getNeighborsZScore();
		int numberOfNeighbors = neighbors.size();
		for (byte i = 0; i < numberOfZScores; i++) {
			meanZScore[i] = meanZScore[i] / numberOfNeighbors;
		}

		return meanZScore;
	}
	
	public String getCode(){
		return code;
	}

	public double[] getNeighborsZScore() throws IOException {
		Map<String, Vertex> vertices = Data.getGraphData().getVertices();
		if (neighborsZScore == null || isChanged) {
			neighborsZScore = new double[numberOfZScores];
			for (String nCode : neighbors) {
				Vertex neighbor = vertices.get(nCode);
				for (byte i = 0; i < neighbor.zScores.length; i++) {
					neighborsZScore[i] += neighbor.zScores[i];
				}

			}
		}
		this.isChanged = false;
		return neighborsZScore.clone();
	}
	
	
	public ArrayList<String> getNeighbors(){
		return neighbors;
	}

	@Override
	public int compareTo(final Vertex o) {
		double self = 0;
		double other = 0;

		try {
			self = this.calculateMeanWithWeights();
			other = o.calculateMeanWithWeights();
		} catch (Exception e) {
			System.exit(-1);
		}
		if (self > other) {
			return 1;
		} else if (self < other) {
			return -1;
		} else
			return 0;

	}

	private double calculateMeanWithWeights() throws IOException {

		double[] acc = zScores.clone();
		double[] scores = this.getMeanNeighborsZScore();
		for (int i = 0; i < numberOfZScores; i++) {
			acc[i] = acc[i] * weights[0] + scores[i] * weights[1];
		}

		return calculateEuclideanSquare(acc);
	}

	public static double calculateEuclideanSquare(final double[] arr) {

		double numerator = 0.0;

		for (int i = 0; i < numberOfZScores; i++) {
			numerator += Math.pow(arr[i], 2);
		}

		return numerator;
	}

	public static byte getNumberOfZScores() {
		return numberOfZScores;
	}

	public double getVertexZScore() {
		return vertexZScore;
	}
}
