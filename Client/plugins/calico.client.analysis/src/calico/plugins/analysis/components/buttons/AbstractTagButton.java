package calico.plugins.analysis.components.buttons;

import javax.swing.SwingUtilities;

import calico.CalicoDraw;
import calico.components.bubblemenu.BubbleMenu;
import calico.components.menus.CanvasMenuButton;
import calico.components.tags.Tag;
import calico.controllers.CGroupController;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;
import calico.plugins.analysis.iconsets.CalicoIconManager;

/**
 * This class is used to group the common implementations
 * of the different tag buttons for the analysis
 * @author motta
 *
 */
public class AbstractTagButton extends CanvasMenuButton {

	private static final long serialVersionUID = 1L;

	/** 
	 * The different implementations provide the followings (in their constructors)
	 * (1) iconString => The icon for this button
	 * (2) tagClassName => The name of the class of the Tag that must be added to the scrap, e.g. CPUTag.class.getName()
	 */
	protected String tagClassName;

	public void actionMouseClicked(InputEventInfo event) {
		if (event.getAction() == InputEventInfo.ACTION_PRESSED) {
			super.onMouseDown();
		} else if (event.getAction() == InputEventInfo.ACTION_RELEASED
				&& isPressed) {
			//TODO[mottalrd][improvement] better not to rely on this
			long guuid=BubbleMenu.lastUUID;
			
			//User must first select the group
			if(guuid==0l){
				super.onMouseUp();
				return;
			}
			
			Tag check=Tag.makeNewInstance(this.tagClassName);
			if(!CGroupController.groupdb.get(guuid).getTags().contains(check)){
				//If the group DOES NOT have the tag, add it
				AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_ADD_TAG ,guuid, this.tagClassName);
			}else{
				//If the group HAS the tag, remove it
				AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_REMOVE_TAG ,guuid, this.tagClassName);
			}
			
			super.onMouseUp();
		}

	}
	
	public void highlight_on()
	{
		if (!isPressed)
		{
			isPressed = true;
			setSelected(true);
			final CanvasMenuButton tempButton = this;
			SwingUtilities.invokeLater(
					new Runnable() { public void run() { 
						double tempX = tempButton.getX();
						double tempY = tempButton.getY();
								
						setImage(CalicoIconManager.getIconImage(iconString));
						tempButton.setX(tempX);
						tempButton.setY(tempY);
					}});
			//CalicoDraw.setNodeX(this, tempX);
			//CalicoDraw.setNodeY(this, tempY);
			//this.repaintFrom(this.getBounds(), this);
			CalicoDraw.repaintNode(this);
		}
	}
	
	public void highlight_off()
	{
		if (isPressed)
		{
			isPressed = false;
			setSelected(false);
			final CanvasMenuButton tempButton = this;
			SwingUtilities.invokeLater(
					new Runnable() { public void run() { 
						double tempX = tempButton.getX();
						double tempY = tempButton.getY();
								
						setImage(CalicoIconManager.getIconImage(iconString));
						tempButton.setX(tempX);
						tempButton.setY(tempY);
					}});
			//CalicoDraw.setNodeX(this, tempX);
			//CalicoDraw.setNodeY(this, tempY);
			//this.repaintFrom(this.getBounds(), this);
			CalicoDraw.repaintNode(this);
		}
	}	
	
}
