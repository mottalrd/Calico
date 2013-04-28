package calico.plugins.analysis.components.tags;

import calico.components.tags.Tag;

public class PerformanceTag extends Tag{

	//TODO[mottalrd] How should I remove a tag? Better idea is to use the same menu for adding/removing
	
	/** The response time of this activity node */
	protected double responseTime = 1.0d;
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	
}
