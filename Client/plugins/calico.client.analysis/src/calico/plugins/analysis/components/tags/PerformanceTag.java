package calico.plugins.analysis.components.tags;

import calico.components.tags.Tag;

public abstract class PerformanceTag extends Tag{

	//TODO[mottalrd][bug] Deleting a scrap not working well on server
	
	/** The response time of this activity node */
	protected double responseTime = 1.0d;
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	
	public boolean equals(Object o){
		if(!(o instanceof PerformanceTag)) return false;
		PerformanceTag tag=(PerformanceTag) o;
		return tag.getClass().getName().equals(this.getClass().getName());
	}
	
	public int hashCode(){
		return this.getClass().getName().hashCode();
	}
	
}
