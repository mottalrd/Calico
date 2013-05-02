package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.CalicoDraw;
import calico.CalicoOptions;
import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.inputhandlers.CalicoInputManager;
import calico.plugins.analysis.iconsets.CalicoIconManager;
import calico.plugins.analysis.inputhandlers.TagInputHandler;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class PerformanceTag extends Tag{

	//TODO[mottalrd][bug] Deleting a scrap not working well on server

	//The image corresponding to the decision/fork menu
	private PImage menuDecisionFork_DecisionSelected;
	private PImage menuDecisionFork_ForkSelected;
	private PImage menuDecisionFork=null;
	
	//TODO[mottalrd][improvement] hard-coded image size
	private static int menuDecisionForkWIDTH=80;
	private static int menuDecisionForkHEIGHT=26;
	
	//TODO[mottalrd] add logic
	private boolean isDecisionForkMenuON;

	//true if DECISION is on, false if FORK is on
	private boolean isDecision;
	
	/** The image representing the tag **/
	protected PImage iconImage;
	
	/** The response time of this activity node */
	protected double responseTime = 1.0d;
	private int imagePosition;
	
	public PerformanceTag(){
		Image img=CalicoIconManager.getIconImage("tags.buttons.menu_decision_fork_fork");
		this.menuDecisionFork_ForkSelected=new PImage();
		this.menuDecisionFork_ForkSelected.setImage(img);
		
		img=CalicoIconManager.getIconImage("tags.buttons.menu_decision_fork_decision");
		this.menuDecisionFork_DecisionSelected=new PImage();
		this.menuDecisionFork_DecisionSelected.setImage(img);
		
		//set default
		menuDecisionFork=menuDecisionFork_DecisionSelected;
		this.isDecision=true;
	}
	
	public PBounds getMenuDecisionForkBounds(){
		return menuDecisionFork.getBounds();
	}
	
	public boolean isDecisionForkMenuON() {
		return isDecisionForkMenuON;
	}
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	
	public void groupMoved(long uuid){
		if(uuid==this.guuid) this.move();
	}
	
	public void groupDeleted(long uuid){
		//TODO[mottalrd][improvement] really nothing to do?
		//nothing to do
	}
	
	public void groupHasNewConnector(long uuid){
		if(uuid==this.guuid){
			//check if we need to show the decision/fork menu
			CGroup group=CGroupController.groupdb.get(this.guuid);
			if( group.getChildConnectors().length>1 ){
				//Draw the decision/fork menu
				PBounds bounds=group.getBounds();
				menuDecisionFork.setBounds(bounds.x+bounds.width-menuDecisionForkWIDTH,bounds.y+bounds.height-menuDecisionForkHEIGHT,menuDecisionForkWIDTH,menuDecisionForkHEIGHT);
				menuDecisionFork.repaint();
				CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), menuDecisionFork);
				
				this.isDecisionForkMenuON=true;
			}
		}
	}
	
	public void groupHasLostAConnector(long uuid){
		if(uuid==this.guuid){
			//TODO[mottalrd] when we delete a connector we must check if we need to delete the decision fork menu
		}
	}
	
	@Override
	public void create() {
		CGroup group=CGroupController.groupdb.get(this.guuid);
		
		//input stuffs
		CalicoInputManager.addCustomInputHandler(this.guuid, new TagInputHandler(this.guuid, this));
		
		//drawing stuffs
		PBounds bounds=group.getBounds();
		iconImage.setBounds(bounds.x,bounds.y,CalicoOptions.menu.icon_size,CalicoOptions.menu.icon_size);
		iconImage.repaint();
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), iconImage);
	}

	@Override
	public void move() {
		
		PBounds bounds=CGroupController.groupdb.get(this.guuid).getBounds();
		iconImage.setBounds(bounds.x,bounds.y,CalicoOptions.menu.icon_size,CalicoOptions.menu.icon_size);
		iconImage.repaint();
		
		//check if we need to move the decision fork menu
		if(this.isDecisionForkMenuON==true){
			
			this.menuDecisionFork.setBounds(bounds.x+bounds.width-menuDecisionForkWIDTH,bounds.y+bounds.height-menuDecisionForkHEIGHT,menuDecisionForkWIDTH,menuDecisionForkHEIGHT);
			this.menuDecisionFork.repaint();
		}
	}

	@Override
	public void delete() {
		CalicoDraw.removeNodeFromParent(iconImage);
		iconImage.repaint();
	}
	
	public void switchDecisionForkMenu() {
		CGroup group=CGroupController.groupdb.get(this.guuid);
		PBounds bounds=group.getBounds();
		if(this.isDecision==true) {
			CalicoDraw.removeNodeFromParent(menuDecisionFork_DecisionSelected);
			iconImage.repaint();
			
			menuDecisionFork=this.menuDecisionFork_ForkSelected;
			this.isDecision=false;
		}
		else{
			CalicoDraw.removeNodeFromParent(menuDecisionFork_ForkSelected);
			iconImage.repaint();
			
			menuDecisionFork=this.menuDecisionFork_DecisionSelected;
			this.isDecision=true;
		}
		
		menuDecisionFork.setBounds(bounds.x+bounds.width-menuDecisionForkWIDTH,bounds.y+bounds.height-menuDecisionForkHEIGHT,menuDecisionForkWIDTH,menuDecisionForkHEIGHT);
		menuDecisionFork.repaint();
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), menuDecisionFork);
	}
	
	public boolean equals(Object o){
		if(!(o instanceof PerformanceTag)) return false;
		PerformanceTag tag=(PerformanceTag) o;
		return tag.getClass().getName().equals(this.getClass().getName());
	}
	
	public int hashCode(){
		return this.getClass().getName().hashCode();
	}


	
}
