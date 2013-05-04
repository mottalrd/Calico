package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.components.CGroup;
import calico.controllers.CGroupController;
import calico.inputhandlers.CalicoInputManager;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;
import calico.plugins.analysis.iconsets.CalicoIconManager;
import calico.plugins.analysis.inputhandlers.TagInputHandler;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;


public class ForkDecisionTag extends TagWithImage{

	//TODO[mottalrd][improvement] hard-coded image size
	private static int MENU_DECISION_FORK_WIDTH=80;
	private static int MENU_DECISION_FORK_HEIGHT=26;
	
	private static String DECISION_IMAGE_NAME="tags.buttons.menu_decision_fork_decision";
	private static String FORK_IMAGE_NAME="tags.buttons.menu_decision_fork_fork";
	
	private boolean isDecision;
	
	public ForkDecisionTag(long guuid){
		super(guuid);
		
		this.iconImage=this.getDecisionImage();
		this.isDecision=true;
		
		this.iconWidth=MENU_DECISION_FORK_WIDTH;
		this.iconHeight=MENU_DECISION_FORK_HEIGHT;
		
	}
	
	public boolean isFork(){
		return !isDecision;
	}
	
	public boolean isDecision(){
		return isDecision;
	}
	
	private PImage getDecisionImage(){
		Image img=CalicoIconManager.getIconImage(DECISION_IMAGE_NAME);
		PImage iconImage=new PImage();
		iconImage.setImage(img);
		return iconImage;
	}
	
	private PImage getForkImage(){
		Image img=CalicoIconManager.getIconImage(FORK_IMAGE_NAME);
		PImage iconImage=new PImage();
		iconImage.setImage(img);
		return iconImage;
	}
	
	@Override
	public void show(){
		CGroup group=CGroupController.groupdb.get(this.guuid);
		PBounds bounds=group.getBounds();
		this.xShift=bounds.width-MENU_DECISION_FORK_WIDTH;
		this.yShift=bounds.height-MENU_DECISION_FORK_HEIGHT;
		
		super.show();
		
		//TODO[mottalrd][improvement] why not in the constructor?
		CalicoInputManager.addCustomInputHandler(this.guuid, new TagInputHandler(this.guuid, this));
	}
	
	@Override
	public void groupDeleted(long uuid) {
		// nothing to do
	}

	@Override
	public void groupHasNewConnector(long uuid) {
		// nothing to do
		
	}

	@Override
	public void groupHasLostAConnector(long uuid) {
		CGroup group=CGroupController.groupdb.get(this.guuid);
		if(uuid==this.guuid &&  group.getIncomingPaths().size()<2){
			AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_REMOVE_TAG ,guuid, ForkDecisionTag.class.getName());
		}
	}

	public void switchForkDecisionMenu() {
		if(this.isDecision==true) {
			this.removeIconImage();
			
			iconImage=this.getForkImage();
			this.isDecision=false;
		}
		else{
			this.removeIconImage();
			
			iconImage=this.getDecisionImage();
			this.isDecision=true;
		}
		this.addIconImage();
	}
	


}
