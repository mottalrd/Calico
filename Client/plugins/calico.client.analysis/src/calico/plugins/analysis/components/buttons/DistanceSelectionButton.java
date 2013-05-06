package calico.plugins.analysis.components.buttons;

import javax.swing.SwingUtilities;

import calico.CalicoDraw;
import calico.components.bubblemenu.BubbleMenu;
import calico.components.menus.CanvasMenuButton;
import calico.components.tags.Tag;
import calico.controllers.CGroupController;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.AnalysisConfiguration;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;
import calico.plugins.analysis.components.tags.RunTag;
import calico.plugins.analysis.iconsets.CalicoIconManager;
import calico.plugins.analysis.utils.SimpleInput;

public class DistanceSelectionButton extends CanvasMenuButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DistanceSelectionButton(long c) {
		super();
		cuid = c;

		this.iconString = "analysis.distance";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void actionMouseClicked(InputEventInfo event) {
		if (event.getAction() == InputEventInfo.ACTION_PRESSED) {
			super.onMouseDown();
		} else if (event.getAction() == InputEventInfo.ACTION_RELEASED
				&& isPressed) {

			SimpleInput in=new SimpleInput();
			float distance=in.getFloat("Analysis will compute for each node N the probability of reaching N in less then T starting from the node tagged with the run tag. More formally" +
					" P=? [ F<=T N ] from START. Please provide a value for T");
			AnalysisConfiguration.TIME_DISTANCE=distance;
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
