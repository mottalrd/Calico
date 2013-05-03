package calico.plugins.analysis.components.tags;

import java.awt.Polygon;

import calico.CalicoDraw;
import calico.components.CGroup;
import calico.controllers.CGroupController;
import calico.plugins.analysis.utils.ActivityShape;

public class FinalNodeTag extends NodeTypeTag{

	//TODO[mottalrd][bug] loss of sync when using initial/final node tags
	
	public FinalNodeTag(long guuid) {
		super(guuid);
		
		this.applyNewShapeToGroup();
	}

	private void applyNewShapeToGroup() {
		//TODO[mottalrd][improvement] move this stuff to a CGroupController method
		
		//Get the Group
		CGroup group=CGroupController.groupdb.get(this.guuid);

		//Reset the points and set the new ones
		group.resetPoints();
		Polygon p = get_group_shape(ActivityShape.FINALNODE, (int)group.getX(), (int)group.getY());
		create_custom_shape(this.guuid, p);
		
		//Repaint
		CalicoDraw.setNodePaintInvalid(group, true);
		CalicoDraw.repaint(group);
	}



}
