package calico.plugins.analysis.inputhandlers;

import calico.inputhandlers.CGroupInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.components.tags.PerformanceTag;

public class TagInputHandler extends CGroupInputHandler{

	private PerformanceTag performanceTag;

	public TagInputHandler(long u, PerformanceTag performanceTag) {
		super(u);
		this.performanceTag=performanceTag;
	}

	public void actionReleased(InputEventInfo e){
		super.actionReleased(e);
		if(performanceTag.isDecisionForkMenuON())
			performanceTag.switchDecisionForkMenu();
	}

	
}
