package calico.components.tags;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import calico.CalicoDraw;
import calico.CalicoOptions;
import calico.components.CGroup;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import edu.umd.cs.piccolo.util.PBounds;

public class TagsMenu {
	
	/** The container displaying the tags **/
	private TagsMenuContainer tagsContainer = null;
	
	/** The group id this menu belongs to **/
	private long guuid;
	
	public TagsMenu(long guuid){
		this.guuid=guuid;
		tagsContainer = new TagsMenuContainer();
	}
	
	public void update() {
		CGroup group=CGroupController.groupdb.get(guuid);
		setIconPositions(new Point((int)group.getX(), (int)group.getY()));
		update_container();
	}
	
	/**
	 * Move the tags
	 * @param bounds2
	 */
	public void moveIconPositions(PBounds bounds2) {
		for(int i=0;i<CGroupController.groupdb.get(guuid).getTags().size();i++)
		{
			Point pos=new Point((int)bounds2.getX(), (int)bounds2.getY());
			CGroupController.groupdb.get(guuid).getTags().get(i).setPosition(pos);
			CalicoDraw.setNodeBounds(tagsContainer.getChild(i), pos.getX(), pos.getY(), CalicoOptions.menu.icon_size, CalicoOptions.menu.icon_size);
		}
	}
	
	/**
	 * Setup the position of the tags to be displayed
	 */
	private void setIconPositions(Point p)
	{
		for(int i=0;i<CGroupController.groupdb.get(guuid).getTags().size();i++)
		{
			CGroupController.groupdb.get(guuid).getTags().get(i).setPosition(p);
		}
	}
	
	/**
	 * Get the tags 
	 * @return
	 */
	public int getTagsCount() {
		return CGroupController.groupdb.get(guuid).getTags().size();
	}

	/**
	 * Get the tag at position i
	 * @param i
	 * @return
	 */
	public Tag getTag(int i) {
		return CGroupController.groupdb.get(guuid).getTags().get(i);
	}
	
	/**
	 * Shows the container of the tags
	 */
	private void update_container() {
		tagsContainer.clear();
		//TODO[mottalrd][improvement] Each time I move I recreate the container for the images, not really good
		//TODO[mottalrd][bug] When I restart the client the canvas goes out of sync and reloads the scraps (on console I see [!=== FOUND MISMATCH ===!])
		
		tagsContainer=new TagsMenuContainer();
		tagsContainer.addTags(CGroupController.groupdb.get(guuid).getTags());
		
		//Update the bounds of the menu
		updateContainerBounds();
		
		//Add to canvas
		long cuid=CCanvasController.getCurrentUUID();
		//TODO[mottalrd][bug] error when restarting client, no current canvas
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(cuid).getLayer(), tagsContainer);
		
		CalicoDraw.setNodeTransparency(tagsContainer, 1.0f);
		CalicoDraw.repaintNode(tagsContainer);
	}
	
	/**
	 * Updates the bounds of the menu
	 */
	private void updateContainerBounds()
	{
		double lowX = java.lang.Double.MAX_VALUE, lowY = java.lang.Double.MAX_VALUE, highX = java.lang.Double.MIN_VALUE, highY = java.lang.Double.MIN_VALUE;
		
		Rectangle bounds;
		for (Tag button : CGroupController.groupdb.get(guuid).getTags())
		{
			bounds = button.getBounds();
			if (lowX > bounds.x)
				lowX = bounds.x;
			if (lowY > bounds.y)
				lowY = bounds.y;
			if (highX < bounds.x + bounds.width)
				highX = bounds.x + bounds.width;
			if (highY < bounds.y + bounds.height)
				highY = bounds.y + bounds.height;
		}
		
		
		//bubbleContainer.setBounds(new Rectangle2D.Double(lowX, lowY, highX - lowX, highY - lowY));
		CalicoDraw.setNodeBounds(tagsContainer, new Rectangle2D.Double(lowX, lowY, highX - lowX, highY - lowY));
	}







}
