package common;

/**
 * @author Osman Baskaya
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public final class Data {

	private static String dataPath = "/home/tyr/codes/javaws/Bio/data/";
	private static String graphFileDelimiter = " ";
	private static String pvalueFileDelimiter = "\t";

	private static final String pvalueFile = "pvalues.txt";
	private static final String graphFile = "graph.sif";

	private static final byte numberOfPvalues = Vertex.getNumberOfZScores();
	private static NormalDistributionImpl nd = new NormalDistributionImpl();
	private static double[] maxPValues = new double[numberOfPvalues];

	private static Data graphData = null;

	private Map<String, Vertex> vertices = new HashMap<String, Vertex>();

	static String getDataPath() {
		return dataPath;
	}

	public Data(Map<String, Vertex> v) {
		this.vertices = v;
	}

	public static void setupPath(String[] args) {
		if (args.length > 0) {
			dataPath = args[0];
			if (!dataPath.endsWith("/")) {
				dataPath += '/';
			}
		}
	}

	public static void maxPValuesOnData() throws IOException {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(dataPath + pvalueFile));
			String line;
			String pValue;
			line = reader.readLine(); // First line in the file is explanation.
										// It should be skipped.
			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,
						pvalueFileDelimiter);
				st.nextToken(); // First column is name of the node. It should
								// be skipped.
				for (int i = 0; i < maxPValues.length; i++) {
					pValue = st.nextToken();
					double actual = Double.parseDouble(pValue);
					if (maxPValues[i] < actual) {
						maxPValues[i] = actual;
					}
				}
			}
		} catch (Exception e) {
			System.out.println("File read error - maxPValuesOnData");
			System.exit(-1);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public Map<String, Vertex> getVertices() {
		return vertices;
	}

	public static final Data getGraphData() {
		if (graphData == null) {
			createData();
		}
		return graphData;
	}

	private static void createData() {
		graphData = readData();
	}

	private static Data readData() {

		BufferedReader reader = null;
		Map<String, Vertex> vertices = new HashMap<String, Vertex>();

		try {
			reader = new BufferedReader(new FileReader(dataPath + pvalueFile));
			String line;
			line = reader.readLine(); // First line in the file is explanation.
										// It should be skipped.

			while ((line = reader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line,
						pvalueFileDelimiter);

				String code = st.nextToken();

				double[] pValues = new double[numberOfPvalues];
				double[] zScores = new double[numberOfPvalues];
				for (int i = 0; i < numberOfPvalues; i++) {

					pValues[i] = Double.parseDouble(st.nextToken());
					 try {
						zScores[i] = nd.inverseCumulativeProbability(1.0 -  pValues[i]);
					} catch (MathException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					zScores[i] = pValues[i];
				}

				Vertex v = new Vertex(code, zScores);
				vertices.put(code, v);

			}

		} catch (IOException e) {
			System.out.println("File read error");
			System.exit(-1);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		reader = null;
		Vertex v = null;
		int counter = 0;
		String s;

		try {
			reader = new BufferedReader(new FileReader(dataPath + graphFile));
			String line;

			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line,
						graphFileDelimiter);

				s = st.nextToken();
				try {
//					System.out.println(s);
					v = vertices.get(s);
					s = st.nextToken(); // 'pp' in graph file should be skipped.
//					System.out.println(s);
					s = st.nextToken();
//					System.out.println(s);
				} catch (NoSuchElementException e) {

				}

				try {
					v.addNeighbors(s);
				} catch (NullPointerException e) {
					counter += 1;
//					System.out.println("Skipping");
				}

			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println("Counter: " + counter);
		return new Data(vertices);
	}

}
