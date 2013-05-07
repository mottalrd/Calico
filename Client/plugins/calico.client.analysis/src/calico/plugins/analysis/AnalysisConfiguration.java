package calico.plugins.analysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AnalysisConfiguration {

	public static float CPU_TIME = 1.0F;
	public static float RAM_TIME = 2.0F;
	public static float DB_TIME = 5.0F;
	public static float NET_TIME = 8.0F;

	public static String PRISM_EXECUTABLE = "/usr/local/prism/bin/prism";

	// TODO[mottalrd][improvement] please use a more intelligent default
	// distance value
	public static float TIME_DISTANCE = 10.0f;

	public static void load() {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("conf/calico.analysis.properties"));
			
			// get the property value and print it out
			CPU_TIME = Float.parseFloat(prop.getProperty("CPU_TIME"));
			RAM_TIME = Float.parseFloat(prop.getProperty("RAM_TIME"));
			DB_TIME = Float.parseFloat(prop.getProperty("DB_TIME"));
			NET_TIME = Float.parseFloat(prop.getProperty("NET_TIME"));

			PRISM_EXECUTABLE = prop.getProperty("PRISM_EXECUTABLE");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
