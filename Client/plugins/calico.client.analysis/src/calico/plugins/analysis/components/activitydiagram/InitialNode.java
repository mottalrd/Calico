package calico.plugins.analysis.components.activitydiagram;

import calico.components.CGroup;
import calico.plugins.analysis.AnalysisNetworkCommands;

public class InitialNode extends CGroup implements ActivityScrap{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitialNode(long uuid, long cuid, long puid) {
		super(uuid, cuid, puid);
		networkLoadCommand = AnalysisNetworkCommands.ANALYSIS_INITIAL_NODE_LOAD;
		// TODO Auto-generated constructor stub
		//color = new Color(245,245,245);
	}
	
	public String toString(){
		return "Initial Node: " + this.uuid + "\n";
	}


}

