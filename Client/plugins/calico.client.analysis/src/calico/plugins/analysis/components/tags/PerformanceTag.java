package calico.plugins.analysis.components.tags;

import calico.CalicoOptions;
import calico.components.CGroup;
import calico.controllers.CGroupController;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;

public abstract class PerformanceTag extends AnalysisTag{

	public PerformanceTag(){
		this.iconWidth=CalicoOptions.menu.icon_size;
		this.iconHeight=CalicoOptions.menu.icon_size;
		this.xShift=0;
		this.yShift=0;
	}
	
	@Override
	public void groupDeleted(long uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupHasNewConnector(long uuid) {
		if(uuid==this.guuid){
			//check if we need to show the decision/fork menu
			CGroup group=CGroupController.groupdb.get(this.guuid);
			if( group.getOutgoingPaths().size() >1 ){
				AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_ADD_TAG ,guuid, ForkDecisionTag.class.getName());
			}
		}
	}

	@Override
	public void groupHasLostAConnector(long uuid) {
		// TODO Auto-generated method stub
		
	}

}
