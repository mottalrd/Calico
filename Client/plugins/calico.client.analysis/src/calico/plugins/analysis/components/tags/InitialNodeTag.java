package calico.plugins.analysis.components.tags;

import java.awt.Polygon;

import calico.CalicoDraw;
import calico.components.CGroup;
import calico.controllers.CGroupController;
import calico.plugins.analysis.utils.ActivityShape;

public class InitialNodeTag extends NodeTypeTag{

	@Override
	public void create() {
		//TODO[mottalrd][improvement] move this stuff to a CGroupController method
		
		//Get the Group
		CGroup group=CGroupController.groupdb.get(this.guuid);

		//Reset the points and set the new ones
		group.resetPoints();
		Polygon p = get_group_shape(ActivityShape.INITIALNODE, (int)group.getX(), (int)group.getY());
		create_custom_shape(this.guuid, p);
		
		//Repaint
		CalicoDraw.setNodePaintInvalid(group, true);
		CalicoDraw.repaint(group);
	}


}
