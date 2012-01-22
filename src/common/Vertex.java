package common;

/**
 * @author Osman Baskaya
 */
import java.util.ArrayList;
import java.util.Map;
import java.util.Map;

public class Vertex implements Comparable<Vertex> {

	private final String code;
	private ArrayList<String> neighbors = new ArrayList<String>();

	private static final byte numberOfZScores = 2;
	private double[] zScores;
	private double[] neighborsZScore = null;
	private boolean isChanged = true;
	private static final double[] weights = { 1.0, 1.0 };
	private final double vertexZScore; 

	public Vertex(String code, double[] zScores) {
		this.code = code;
		this.zScores = zScores;
		vertexZScore = calculateMeanWithWeights(zScores);

		// Arrays.fill(neighborsZScore, Double.NaN);
	}
	
	public double getVertexZScore(){
		return vertexZScore;
	}

	public void addNeighbors(String neighborsCode) {
		if (!neighbors.contains(neighborsCode)) {
			neighbors.add(neighborsCode);
			isChanged = true;
		}
	}


	public double[] getMeanTotalZScore() {
		double[] meanZScore = getTotalZScore();
		int numberOfNeighbors = neighbors.size();
		for (byte i = 0; i < numberOfZScores; i++) {
			meanZScore[i] = meanZScore[i] / (numberOfNeighbors + 1);
		}
		return meanZScore.clone();
	}

	public double[] getTotalZScore() {
		double[] meanZScore = getNeighborsZScore();
		for (byte i = 0; i < numberOfZScores; i++) {
			meanZScore[i] += zScores[i];
		}
		return meanZScore;
	}

	public double[] getMeanNeighborsZScore() {
		double[] meanZScore = getNeighborsZScore();
		int numberOfNeighbors = neighbors.size();
		for (byte i = 0; i < numberOfZScores; i++) {
			meanZScore[i] = meanZScore[i] / numberOfNeighbors;
		}

		return meanZScore;
	}

	public double[] getNeighborsZScore() {
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

	@Override
	public int compareTo(final Vertex o) {
		double self = calculateMeanWithWeights(this.getMeanTotalZScore());
		double other = calculateMeanWithWeights(o.getMeanTotalZScore());
		if (self > other) {
			return 1;
		} else if (self < other) {
			return -1;
		} else
			return 0;

	}

	private static double calculateMeanWithWeights(final double[] a1) {
		double numerator = 0.0;
		double denominator = 0.0;
		for (int i = 0; i < a1.length; i++) {
			numerator += a1[i] * weights[i];
			denominator += weights[i];
		}
		return numerator / denominator;
	}

	public static byte getNumberOfZScores() {
		return numberOfZScores;
	}
}
