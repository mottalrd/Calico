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
		
		
		//If we clicked on the decision/fork menu, switch the menu
		if(performanceTag.isDecisionForkMenuON() && performanceTag.getMenuDecisionForkBounds().contains(e.getX(), e.getY())){
			performanceTag.switchDecisionForkMenu();
		}else{
			//the default behavior
			super.actionReleased(e);
		}
			
	}

	
}
