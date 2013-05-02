package calico.plugins.analysis.components.tags;

import calico.CalicoDraw;
import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class AnalysisTag extends Tag{

	/** The image representing the tag **/
	protected PImage iconImage;
	
	/** x,y to be add with respect to CGroup (0,0) **/
	protected double xShift;
	protected double yShift;
	
	/**width and height of the icon**/
	protected double iconWidth;
	protected double iconHeight;
	
	@Override
	public void groupMoved(long uuid) {
		if(uuid==this.guuid) this.move();
	}

	@Override
	public void create() {
		this.addIconImage();
	}

	@Override
	public void move() {
		PBounds bounds=CGroupController.groupdb.get(this.guuid).getBounds();
		iconImage.setBounds(bounds.x+this.xShift,bounds.y+this.yShift, iconWidth, iconHeight);
		iconImage.repaint();
	}

	@Override
	public void delete() {
		//TODO[mottalrd][bug] (1) tag element (2) delete tag (3) exit client and enter again => tag is back
		CalicoDraw.removeNodeFromParent(iconImage);
		iconImage.repaint();
	}
	
	public PBounds getIconImageBounds(){
		return iconImage.getBounds();
	}
	
	public boolean equals(Object o){
		if(!(o instanceof AnalysisTag)) return false;
		AnalysisTag tag=(AnalysisTag) o;
		return tag.getClass().getName().equals(this.getClass().getName());
	}
	
	public int hashCode(){
		return this.getClass().getName().hashCode();
	}
	
	/**
	 * Show the image on the assigned position
	 */
	protected void addIconImage(){
		CGroup group=CGroupController.groupdb.get(this.guuid);
		
		PBounds bounds=group.getBounds();
		iconImage.setBounds(bounds.x+xShift,bounds.y+yShift,this.iconWidth,this.iconHeight);
		iconImage.repaint();
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), iconImage);
	}
	
	/**
	 * Remove the image
	 */
	protected void removeIconImage(){
		CalicoDraw.removeNodeFromParent(this.iconImage);
		iconImage.repaint();
	}

}
