package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.CalicoDraw;
import calico.CalicoOptions;
import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class PerformanceTag extends Tag{

	//TODO[mottalrd][bug] Deleting a scrap not working well on server

	/** The image representing the tag **/
	protected PImage iconImage;
	
	/** The response time of this activity node */
	protected double responseTime = 1.0d;
	
	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}
	
	@Override
	public void create() {
		
		PBounds bounds=CGroupController.groupdb.get(this.guuid).getBounds();
		iconImage.setBounds(bounds.x,bounds.y,CalicoOptions.menu.icon_size,CalicoOptions.menu.icon_size);
		iconImage.repaint();
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), iconImage);
	}

	@Override
	public void move() {
		
		PBounds bounds=CGroupController.groupdb.get(this.guuid).getBounds();
		iconImage.setBounds(bounds.x,bounds.y,CalicoOptions.menu.icon_size,CalicoOptions.menu.icon_size);
		iconImage.repaint();
	}

	@Override
	public void delete() {
		//TODO[mottalrd][bug] delete is not working, the image remains there
		//TODO[mottalrd][bug] (0) add tag (1) delete tag, (2) exit, (3) enter again, => surprise, the tag is back again
		CalicoDraw.removeChildFromNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), iconImage);
		iconImage.setTransparency(0);
		iconImage.setPaintInvalid(true);
		CalicoDraw.setVisible(iconImage, false);
		CalicoDraw.removeNodeFromParent(iconImage);
		CalicoDraw.repaintNode(iconImage);
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
