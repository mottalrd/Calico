package calico.plugins.analysis.components.tags;

import java.awt.Polygon;

import calico.controllers.CGroupController;
import calico.plugins.analysis.utils.ActivityShape;


public abstract class NodeTypeTag extends AbstractTag{

	@Override
	public void groupMoved(long uuid) {
		//Nothing to do
	}

	@Override
	public void groupDeleted(long uuid) {
		//Nothing to do
	}

	@Override
	public void groupHasNewConnector(long uuid) {
		//Nothing to do
	}

	@Override
	public void groupHasLostAConnector(long uuid) {
		//Nothing to do
	}
	
	@Override
	public void move() {
		//Nothing to do
	}

	@Override
	public void delete() {
		//Nothing to do
	}
	
	protected Polygon get_group_shape(ActivityShape as, int x, int y){
		Polygon p= as.getShape(x,y);
		return p;
	}
	
	protected void create_custom_shape(long uuid, Polygon p){
		for (int i = 0; i < p.npoints; i++)
		{
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
		}
	}
	
}
