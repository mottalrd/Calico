package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.CalicoDraw;
import calico.CalicoOptions;
import calico.components.CConnector;
import calico.components.CGroup;
import calico.controllers.CCanvasController;
import calico.controllers.CConnectorController;
import calico.inputhandlers.CalicoAbstractInputHandler;
import calico.inputhandlers.CalicoInputManager;
import calico.plugins.analysis.iconsets.CalicoIconManager;
import calico.plugins.analysis.inputhandlers.AnalysisCanvasInputHandler;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;

public class ProbabilityTag extends AbstractTag implements ConnectorTag{

	private int iconWidth;
	private int iconHeight;
	private PImage plusImage;
	private PImage minusImage;
	private double probability;
	
	private static float BASIC_STROKE_THICKNESS=6.0f;
	private static float STROKE_THICKNESS_DELTA=1.0f;

	public ProbabilityTag(long guuid) {
		super(guuid);
		Image img=CalicoIconManager.getIconImage("tags.buttons.plus");
		this.plusImage=new PImage();
		this.plusImage.setImage(img);
		
		img=CalicoIconManager.getIconImage("tags.buttons.minus");
		this.minusImage=new PImage();
		this.minusImage.setImage(img);
		
		this.iconWidth=CalicoOptions.menu.icon_size;
		this.iconHeight=CalicoOptions.menu.icon_size;
		
		this.probability=0.5;
		CConnector connector=CConnectorController.connectors.get(this.guuid);
		connector.setThickness(BASIC_STROKE_THICKNESS);
	}

	@Override
	public void show() {
		//Check if there is a canvas where we can append
		if(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID())!=null){
			CConnector connector=CConnectorController.connectors.get(this.guuid);
			
			PBounds bounds=connector.getBounds();
			plusImage.setBounds(bounds.x+bounds.width/2,bounds.y+bounds.height/2,this.iconWidth,this.iconHeight);
			plusImage.repaint();
			minusImage.setBounds(bounds.x+bounds.width/2+this.iconWidth,bounds.y+bounds.height/2,this.iconWidth,this.iconHeight);
			minusImage.repaint();
			
			CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), plusImage);
			CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), minusImage);
			
			this.addInputHandler();
		}
	}
	
	private void addInputHandler() {
		long currentCanvas=CCanvasController.getCurrentUUID();
		CalicoAbstractInputHandler canvasInputHandler=CalicoInputManager.getInputHandler(currentCanvas);
		
		if(canvasInputHandler!=null){
			if(!(canvasInputHandler instanceof AnalysisCanvasInputHandler)){
				//replace the default input handler with the one provided with this plugin
				AnalysisCanvasInputHandler handler=new AnalysisCanvasInputHandler(CCanvasController.getCurrentUUID());
				CalicoInputManager.addCustomInputHandler(CCanvasController.getCurrentUUID(), handler);
				handler.addProbabilityTag(this);
			}else{
				//there is already the analysis input handler for the canvas, let's add the tag
				AnalysisCanvasInputHandler handler=(AnalysisCanvasInputHandler) CalicoInputManager.getInputHandler(CCanvasController.getCurrentUUID());
				handler.addProbabilityTag(this);
			}			
		}
	}

	public PBounds getPlusImageBounds(){
		return plusImage.getBounds();
	}
	
	public PBounds getMinusImageBounds(){
		return minusImage.getBounds();
	}

	@Override
	public void move() {
		CConnector connector=CConnectorController.connectors.get(this.guuid);
		
		PBounds bounds=connector.getBounds();
		plusImage.setBounds(bounds.x+bounds.width/2,bounds.y+bounds.height/2,this.iconWidth,this.iconHeight);
		plusImage.repaint();
		minusImage.setBounds(bounds.x+bounds.width/2+this.iconWidth,bounds.y+bounds.height/2,this.iconWidth,this.iconHeight);
		minusImage.repaint();
	}

	@Override
	public void hide() {
		CalicoDraw.removeNodeFromParent(plusImage);
		plusImage.repaint();
		CalicoDraw.removeNodeFromParent(minusImage);
		minusImage.repaint();
		
		//reset connector thickness
		CGroup group=CConnectorController.connectors.get(this.guuid).getIncomingGroup();
		for(CConnector connector: group.getOutgoingPaths()){
			connector.setThickness(1.0f);
		}
	}

	@Override
	public void connectorMoved(long uuid) {
		if(uuid==this.guuid){
			this.move();
		}
	}

	public void increaseProbability() {
		//TODO[mottalrd][bug] what happens if I have more than 2 outgoing connectors?
		//If I have two connectors and I add another one, reset the probabilities
		
		CGroup group=CConnectorController.connectors.get(this.guuid).getIncomingGroup();
		for(CConnector connector: group.getOutgoingPaths()){
			ProbabilityTag ptag=(ProbabilityTag)connector.getTags().iterator().next();
			if(ptag.guuid==this.guuid && this.probability<=0.9){
				//this is the tag I want to increase
				ptag.probability+=0.1;
				float newThickness=connector.getThickness()+STROKE_THICKNESS_DELTA;
				connector.setThickness(newThickness);
			}
			else if(ptag.guuid!=this.guuid && ptag.probability>=0.1){
				//this is the tag I want to decrease
				ptag.probability-=0.1;
				float newThickness=connector.getThickness()-STROKE_THICKNESS_DELTA;
				connector.setThickness(newThickness);
			}
		}
	}

	public void decreaseProbability() {
		CGroup group=CConnectorController.connectors.get(this.guuid).getIncomingGroup();
		for(CConnector connector: group.getOutgoingPaths()){
			ProbabilityTag ptag=(ProbabilityTag)connector.getTags().iterator().next();
			if(ptag.guuid==this.guuid && this.probability>=0.1){
				//this is the tag I want to decrease
				ptag.probability-=0.1;
				float newThickness=connector.getThickness()-STROKE_THICKNESS_DELTA;
				connector.setThickness(newThickness);
			}
			else if(ptag.guuid!=this.guuid && ptag.probability<=0.9){
				//this is the tag I want to decrease
				ptag.probability+=0.1;
				float newThickness=connector.getThickness()+STROKE_THICKNESS_DELTA;
				connector.setThickness(newThickness);
			}
		}
	}

	public double getProbability() {
		return this.probability;
	}

}
