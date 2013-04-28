package calico.plugins.analysis.components.tags;

import calico.components.tags.Tag;

public class PerformanceTag extends Tag{

	/** The response time of this activity node */
	protected double responseTime = 1.0d;
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	
}
