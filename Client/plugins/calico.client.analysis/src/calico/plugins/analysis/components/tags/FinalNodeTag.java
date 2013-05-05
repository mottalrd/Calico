package calico.plugins.analysis.components.tags;

import calico.components.CGroup;
import calico.controllers.CGroupController;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;

public class FinalNodeTag extends NodeTypeTag{

	public FinalNodeTag(long guuid) {
		super(guuid);
		CGroup group=CGroupController.groupdb.get(guuid);
		AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_DRAW_FINAL_NODE, this.guuid, group.getX(), group.getY());
	}

}
