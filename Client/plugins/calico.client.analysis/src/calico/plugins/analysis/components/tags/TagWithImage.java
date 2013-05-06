package calico.plugins.analysis.components.tags;

import calico.CalicoDraw;
import calico.components.CGroup;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;

public abstract class TagWithImage extends AbstractTag{

	public TagWithImage(long guuid) {
		super(guuid);
	}

	/** The image representing the tag **/
	protected PImage iconImage;
	
	/** x,y to be add with respect to CGroup (0,0) **/
	protected double xShift;
	protected double yShift;
	
	/**width and height of the icon**/
	protected double iconWidth;
	protected double iconHeight;
	
	@Override
	public void groupHasNewConnector(long uuid) {
		//nothing to do
	}

	@Override
	public void groupHasLostAConnector(long uuid) {
		//nothing to do
	}
	
	@Override
	public void groupMoved(long uuid) {
		if(uuid==this.guuid) this.move();
	}
	
	@Override
	public void groupDeleted(long uuid) {
		if(uuid==this.guuid){
			this.hide();
			CGroupController.removeListener(this);
		}
	}
	
	

	@Override
	public void show() {
		this.addIconImage();
	}

	@Override
	public void move() {
		PBounds bounds=CGroupController.groupdb.get(this.guuid).getBounds();
		iconImage.setBounds(bounds.x+this.xShift,bounds.y+this.yShift, iconWidth, iconHeight);
		iconImage.repaint();
	}

	@Override
	public void hide() {
		CalicoDraw.removeNodeFromParent(iconImage);
		iconImage.repaint();
	}
	
	public PBounds getIconImageBounds(){
		return iconImage.getBounds();
	}
	
	/**
	 * Show the image on the assigned position
	 */
	protected void addIconImage(){
		//Check if there is a canvas where we can append
		if(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID())!=null){
			CGroup group=CGroupController.groupdb.get(this.guuid);
			
			PBounds bounds=group.getBounds();
			iconImage.setBounds(bounds.x+xShift,bounds.y+yShift,this.iconWidth,this.iconHeight);
			iconImage.repaint();
			
			CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), iconImage);
		}
	}
	
	/**
	 * Remove the image
	 */
	protected void removeIconImage(){
		CalicoDraw.removeNodeFromParent(this.iconImage);
		iconImage.repaint();
	}

}
