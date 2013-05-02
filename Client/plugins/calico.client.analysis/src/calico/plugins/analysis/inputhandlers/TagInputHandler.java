package calico.plugins.analysis.inputhandlers;

import calico.inputhandlers.CGroupInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.components.tags.ForkDecisionTag;

public class TagInputHandler extends CGroupInputHandler{

	private ForkDecisionTag tag;

	public TagInputHandler(long u, ForkDecisionTag tag) {
		super(u);
		this.tag=tag;
	}

	public void actionReleased(InputEventInfo e){
		
		if(tag.getIconImageBounds().contains(e.getX(), e.getY()))
			tag.switchForkDecisionMenu();
		else
			super.actionReleased(e);
	}

	
}
