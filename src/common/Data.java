package common;


/**
 * @author Osman Baskaya
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.math.distribution.NormalDistributionImpl;



public final class Data {

	private static String dataPath = "/home/tyr/codes/javaws/Bioinformatics/data/";
	private static String graphFileDelimiter = " ";
	private static String pvalueFileDelimiter = "\t";
	private static final byte numberOfPvalues = Vertex.getNumberOfZScores();
	private static NormalDistributionImpl nd = new NormalDistributionImpl();

	private static Data graphData;

	private Map<String, Vertex> vertices = new HashMap<String, Vertex>();

	static String getDataPath() {
		return dataPath;
	}

	public static void setupPath(String[] args) {
		if (args.length > 0) {
			dataPath = args[0];
			if (!dataPath.endsWith("/")) {
				dataPath += '/';
			}
		}
	}

	public Map<String, Vertex> getVertices() {
		return vertices;
	}

	public static Data getGraphData() {
		return graphData;
	}

	private static Data readData(String fileName, String delimiter)
			throws IOException {
		
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while ((line = reader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(line, delimiter);
				String code = st.nextToken();
				double[] pValues = new double[numberOfPvalues];
				double[] zScores = new double[numberOfPvalues];
				for (int i = 0; i < numberOfPvalues; i++) {
					pValues[i] = Double.parseDouble(st.nextToken());
					zScores[i] = nd.inverseCumulativeProbability(pValues[i]);
				}
				
				Vertex v = new Vertex(code, zScores);

			}

		} catch (Exception e) {
			System.out.println("File error");
			System.exit(-1);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		return graphData;

	}

}
