package calico.components.tags;

import edu.umd.cs.piccolox.nodes.PComposite;

public class TagsMenuContainer extends PComposite{

	private static final long serialVersionUID = 1L;

	/**
	 * Add the elements to be displayed with thin container
	 */
	public void addTagsToBeDisplayed()
	{
		int buttons = TagsMenu.getTagsCount();
		for(int i=0;i<buttons;i++)
		{
			//Do not use CalicoDraw here. The container is not yet added to a visible canvas.
			//The children need to be available on the active thread.
			addChild( TagsMenu.getTag(i).getPImage() );
			//CalicoDraw.addChildToNode(this, BubbleMenu.getButton(i).getPImage());
		}
	}
	
}
