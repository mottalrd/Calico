package calico.plugins.analysis.inputhandlers;

import calico.inputhandlers.CGroupInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;
import calico.plugins.analysis.components.tags.RunTag;

public class RunInputHandler extends CGroupInputHandler{

	private RunTag tag;

	public RunInputHandler(long u, RunTag tag) {
		super(u);
		this.tag=tag;
	}

	public void actionReleased(InputEventInfo e){
		
		if(tag.getIconImageBounds().contains(e.getX(), e.getY()))
			//TODO[mottalrd] implement run analysis distance
			AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_RUN_ANALYSIS, this.uuid, (double)50.0);
		else
			super.actionReleased(e);
	}

	
}
