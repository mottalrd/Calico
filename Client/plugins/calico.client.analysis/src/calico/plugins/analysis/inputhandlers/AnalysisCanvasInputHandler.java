package calico.plugins.analysis.inputhandlers;

import java.util.ArrayList;

import calico.inputhandlers.CCanvasInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.components.tags.ProbabilityTag;

public class AnalysisCanvasInputHandler extends CCanvasInputHandler {

	private ArrayList<ProbabilityTag> tags=new ArrayList<ProbabilityTag>();

	public AnalysisCanvasInputHandler(long cuid) {
		super(cuid);
	}
	
	public void actionReleased(InputEventInfo e){
		for(ProbabilityTag tag: this.tags){
			this.checkForPlusMinusButtons(tag, e);
		}
		super.actionReleased(e);
	}

	private void checkForPlusMinusButtons(ProbabilityTag tag, InputEventInfo e) {
		//increase or decrease the probability
		if(tag.getPlusImageBounds().contains(e.getX(), e.getY())){
			tag.increaseProbability();
		}else if(tag.getMinusImageBounds().contains(e.getX(), e.getY())){
			tag.decreaseProbability();
		}
	}

	public void addProbabilityTag(ProbabilityTag probabilityTag) {
		tags.add(probabilityTag);
	}

}
