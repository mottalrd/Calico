package calico.components.tags;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

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

	/** The list of tags to be shown **/
	private static ObjectArrayList<Tag> tagsList = new ObjectArrayList<Tag>();
	
	/** The container displaying the tags **/
	private static TagsMenuContainer tagsContainer = null;
	
	/** The UUID of the element owning the tags which are currently displayed **/
	private static long activeUUID = 0l;
	
	/** The current bounds of the menu **/
	/** TODO[mottalrd] not sure this should be here since the displaying element is the TagsMenuContainer **/
	private static PBounds bounds;
	
	/** True if the bubble menu is currently displayed , false otherwise **/
	private static boolean isTagsMenuActive = false;
	
	/**
	 * Shows the tags menu for the provided uuid
	 * @param uuid
	 */
	public static void show_tags(long uuid) {
		if (isTagsMenuActive())
		{
			// Clear out the old one, then try this again
			clearMenu();
		}
		
		//from now on the active uuid is this one
		activeUUID=uuid;
		CGroup group=CGroupController.groupdb.get(activeUUID);
		
		//Get the bounds of the uuid such that 
		//the menu is shown in the correct place
		//Here we are assuming that only groups have tags
		bounds=group.getBounds();
		tagsList=(ObjectArrayList<Tag>) group.getTags();
		
		//Set icon positions from the bounds of the element
		//where we are showing the tags menu
		setIconPositions(new Point((int)group.getX(), (int)group.getY()));
		
		//Shows the container of the icons
		show_tags_container();
		
		isTagsMenuActive = true;
	}

	/**
	 * Hides the tags menu
	 */
	public static void clearMenu() {
		CalicoDraw.removeAllChildrenFromNode(tagsContainer);
		CalicoDraw.removeNodeFromParent(tagsContainer);
		CalicoDraw.repaintNode(tagsContainer);
	}
	
	/**
	 * @return the number of tags currently in the menu
	 */
	public static int getTagsCount(){
		return tagsList.size();
	}
	
	/**
	 * @param i the tag we are interested in
	 * @return the tag at the i^th position
	 */
	public static Tag getTag(int i){
		return tagsList.get(i);
	}
	
	/**
	 * Setup the position of the tags to be displayed
	 */
	private static void setIconPositions(Point p)
	{
		for(int i=0;i<tagsList.size();i++)
		{
			tagsList.get(i).setPosition(p);
		}
	}
	
	/**
	 * Shows the container of the tags
	 */
	private static void show_tags_container() {
		//Initialize container
		tagsContainer = new TagsMenuContainer();
		tagsContainer.addTagsToBeDisplayed();
		
		//Update the bounds of the menu
		updateContainerBounds();
		
		//Add to canvas
		long cuid=CCanvasController.getCurrentUUID();
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(cuid).getLayer(), tagsContainer);
		
		CalicoDraw.setNodeTransparency(tagsContainer, 1.0f);
		CalicoDraw.repaintNode(tagsContainer);
	}
	
	/**
	 * Updates the bounds of the menu
	 */
	private static void updateContainerBounds()
	{
		double lowX = java.lang.Double.MAX_VALUE, lowY = java.lang.Double.MAX_VALUE, highX = java.lang.Double.MIN_VALUE, highY = java.lang.Double.MIN_VALUE;
		
		Rectangle bounds;
		for (Tag button : tagsList)
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
	
	private static boolean isTagsMenuActive(){
		return isTagsMenuActive;
	}

	public static void moveIconPositions(PBounds bounds2) {
		for(int i=0;i<tagsList.size();i++)
		{
			Point pos=new Point((int)bounds2.getX(), (int)bounds2.getY());
			tagsList.get(i).setPosition(pos);
			CalicoDraw.setNodeBounds(tagsContainer.getChild(i), pos.getX(), pos.getY(), CalicoOptions.menu.icon_size, CalicoOptions.menu.icon_size);
		}
	}

}
