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
		if(tag.getPlusImageBounds().contains(e.getX(), e.getY())){
			System.out.println("PLUS");
		}else if(tag.getMinusImageBounds().contains(e.getX(), e.getY())){
			System.out.println("MINUS");
		}
	}

	public void addProbabilityTag(ProbabilityTag probabilityTag) {
		tags.add(probabilityTag);
	}

}
