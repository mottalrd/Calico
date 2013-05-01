package calico.components.tags;

import java.util.List;

import calico.CalicoDraw;
import edu.umd.cs.piccolox.nodes.PComposite;

public class TagsMenuContainer extends PComposite{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Clear the tags container from the screen
	 */
	public void clear() {
		CalicoDraw.removeAllChildrenFromNode(this);
		CalicoDraw.removeNodeFromParent(this);
		CalicoDraw.repaintNode(this);
	}

	/**
	 * Add the tags images to the container
	 * such that thay can be displayed on the screen
	 * @param tags
	 */
	public void addTags(List<Tag> tags) {
		//Add the tags' images to the container
		for(Tag tag: tags)
		{
			this.addChild( tag.getPImage() );
		}
	}
	
}
