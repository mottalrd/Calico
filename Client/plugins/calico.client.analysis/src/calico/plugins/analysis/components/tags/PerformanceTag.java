package calico.plugins.analysis.components.tags;

import calico.CalicoOptions;
import calico.components.CConnector;
import calico.components.CGroup;
import calico.controllers.CGroupController;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class PerformanceTag extends TagWithImage{

	protected float time;

	public PerformanceTag(long guuid){
		super(guuid);
		CGroup group=CGroupController.groupdb.get(this.guuid);
		PBounds bounds=group.getBounds();
		this.iconWidth=CalicoOptions.menu.icon_size;
		this.iconHeight=CalicoOptions.menu.icon_size;
		this.xShift=bounds.width-CalicoOptions.menu.icon_size;
		this.yShift=0;
	}
	
	@Override
	public void groupHasNewConnector(long uuid) {
		if(uuid==this.guuid){
			//check if we need to show the decision/fork menu
			CGroup group=CGroupController.groupdb.get(this.guuid);
			if( group.getOutgoingPaths().size() >1 ){
				AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_ADD_TAG ,guuid, ForkDecisionTag.class.getName());
				//by default this is a decision therefore we add the probability tag to the connectors
				this.addProbabilityTagToConnectors();
			}
		}
	}
	
	private void addProbabilityTagToConnectors() {
		CGroup group=CGroupController.groupdb.get(this.guuid);
		for(CConnector c:group.getOutgoingPaths()){
			AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_ADD_TAG, c.getUUID(), ProbabilityTag.class.getName());
		}
	}

	@Override 
	public void move(){
		CGroup group=CGroupController.groupdb.get(this.guuid);
		PBounds bounds=group.getBounds();
		this.xShift=bounds.width-CalicoOptions.menu.icon_size;
		this.yShift=0;
		
		super.move();
	}

	public float getResponseTime() {
		return this.time;
	}

}
