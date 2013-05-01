package calico.plugins.analysis.controllers;

import java.awt.Polygon;

import calico.CalicoDraw;
import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.plugins.analysis.components.activitydiagram.FinalNode;
import calico.plugins.analysis.components.activitydiagram.InitialNode;
import calico.plugins.analysis.utils.ActivityShape;

/**
 * The Activity Diagram Menu Controller is in charge of managing the actions related to 
 * the bottom right analysis menu for the creation of the analysis elements
 * @author motta
 *
 */
public class ADMenuController {
	
	public static ADMenuController getInstance()
	{
		return INSTANCE;
	}

	public static void initialize()
	{
		INSTANCE = new ADMenuController();
	}

	private static ADMenuController INSTANCE;
	
	/*************************************************
	 * BEHAVIORS OF THE CONTROLLER
	 * 
	 * The summary of the actions that can be
	 * invoked by the view elements
	 *************************************************/	
	public static void add_tag(long guuid, String tag_type){
		no_notify_add_tag(guuid, tag_type);
	}
	
	/*************************************************
	 * NO NOTIFY METHODS
	 * 
	 * Methods which use the CGroupController
	 * to create the actual nodes
	 *************************************************/		
	public static void no_notify_add_tag(long guuid, String tag_type){
		//First get the tag type we want to add to this scrap
		Tag tag=null;
		try {
			tag = (Tag) Class.forName(tag_type).newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		//If the tag is already in there, remove it
		if(!CGroupController.groupdb.get(guuid).getTags().contains(tag)){
			//Add the tag to the scrap
			CGroupController.groupdb.get(guuid).addTag(tag);
		}else{
			//Remove the tag from the scrap
			CGroupController.groupdb.get(guuid).removeTag(tag);
		}
		
	}
	
	public static void create_initial_node(long new_uuid, long cuuid, int x, int y){
		no_notify_create_activitydiagram_node(new_uuid, cuuid, x, y, InitialNode.class, ActivityShape.INITIALNODE, "");	
	}		
	
	public static void create_final_node(long new_uuid, long cuuid, int x, int y){
		no_notify_create_activitydiagram_node(new_uuid, cuuid, x, y, FinalNode.class, ActivityShape.FINALNODE, "");
	}	
	
	public static void no_notify_create_activitydiagram_node(long uuid, long cuuid, int x, int y, Class<?> activity, ActivityShape as, String optText){

		Polygon p = get_group_shape(uuid, as, x,y);

		no_notify_create_activitydiagram_node(uuid, cuuid, p, activity, as, optText);
	}
	
	public static void no_notify_create_activitydiagram_node(long uuid, long cuuid, Polygon p, Class<?> activity, ActivityShape as, String optText){
		no_notify_start(uuid, cuuid, 0l, true, activity);
		CGroupController.setCurrentUUID(uuid);
		create_custom_shape(uuid, p);
		//Set the optional text to identify the scrap
		CGroupController.no_notify_set_text(uuid, optText);
		CGroupController.no_notify_finish(uuid, false, false, true);
		CGroupController.no_notify_set_permanent(uuid, true);
		CGroupController.recheck_parent(uuid);
	}
	
	/*******************************************************
	 * 
	 * PRIVATE METHODS
	 * 
	********************************************************/
	private static void no_notify_start(long uuid, long cuid, long puid, boolean isperm, Class<?> activity)
	{
		if (!CCanvasController.exists(cuid))
			return;
		if(CGroupController.exists(uuid))
		{
			CGroupController.logger.debug("Need to delete group "+uuid);
			// WHOAA WE NEED TO DELETE THIS SHIT
			//CCanvasController.canvasdb.get(cuid).getLayer().removeChild(groupdb.get(uuid));
			CalicoDraw.removeChildFromNode(CCanvasController.canvasdb.get(cuid).getLayer(), CGroupController.groupdb.get(uuid));
			//CCanvasController.canvasdb.get(cuid).getCamera().repaint();
		}
		
		// Add to the GroupDB
		try {
			CGroupController.groupdb.put(uuid, (CGroup)activity.getConstructor(long.class,long.class,long.class).newInstance(uuid, cuid, puid));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CCanvasController.canvasdb.get(cuid).addChildGroup(uuid);
		//CCanvasController.canvasdb.get(cuid).getLayer().addChild(groupdb.get(uuid));
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(cuid).getLayer(), CGroupController.groupdb.get(uuid));
		CGroupController.groupdb.get(uuid).drawPermTemp(true);
		//CCanvasController.canvasdb.get(cuid).repaint();
	}

	//The scrap with id uuid will have the shape defined by as at position x,y
	private static Polygon get_group_shape(long uuid, ActivityShape as, int x, int y){
		//Getting the shape of the polygon and centering it in x,y
		Polygon p= as.getShape(x,y);
		
		return p;
	}
	
	//Add the points defined in p to the scrap with id uuid
	private static void create_custom_shape(long uuid, Polygon p){
		for (int i = 0; i < p.npoints; i++)
		{
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
			CGroupController.no_notify_append(uuid, p.xpoints[i], p.ypoints[i]);
		}
	}

}
