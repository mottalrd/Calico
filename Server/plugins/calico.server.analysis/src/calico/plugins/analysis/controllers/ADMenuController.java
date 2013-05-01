package calico.plugins.analysis.controllers;

import java.awt.Polygon;

import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.plugins.analysis.components.activitydiagram.FinalNode;
import calico.plugins.analysis.components.activitydiagram.InitialNode;
import calico.plugins.analysis.utils.ActivityShape;
import calico.plugins.analysis.utils.PolygonUtil;

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
		if(!CGroupController.groups.get(guuid).getTags().contains(tag)){
			//Add the tag to the scrap
			CGroupController.add_tag(tag, guuid);
		}else{
			//Remove the tag from the scrap
			CGroupController.remove_tag(tag, guuid);
		}
	}
	
	
	public static void create_initial_node(long new_uuid, long cuuid, int x, int y){
		no_notify_create_activitydiagram_node(new_uuid, cuuid, x, y, InitialNode.class, ActivityShape.INITIALNODE, "");	
	}	
	
	public static void create_final_node(long new_uuid, long cuuid, int x, int y){
		no_notify_create_activitydiagram_node(new_uuid, cuuid, x, y, FinalNode.class, ActivityShape.FINALNODE, "");
	}	
	
	public static void no_notify_create_activitydiagram_node(long uuid, long cuuid, int x, int y, Class<?> activity, ActivityShape as, String optText){

		Polygon p = as.getShape(x, y);
		no_notify_create_activitydiagram_node(uuid, cuuid, p, activity, as, optText);
	}
	
	public static void no_notify_create_activitydiagram_node(long uuid, long cuuid, Polygon p, Class<?> activity, ActivityShape as, String optText){
		no_notify_start(uuid, cuuid, 0l, true, activity);
		//this is done client side
		//CGroupController.setCurrentUUID(uuid);
		PolygonUtil.create_custom_shape(uuid, p);
		//Set the optional text to identify the scrap
		CGroupController.set_text(uuid, optText);
		CGroupController.finish(uuid, true);
		CGroupController.set_permanent(uuid, true);
		CGroupController.recheck_parent(uuid);
	}
	
	
	/*************************************************
	 * UTILITY METHODS
	 *************************************************/		
	//Starts the creation of any of the activity diagram scrap
	public static void no_notify_start(long uuid, long cuid, long puid, boolean isperm, Class<?> activity)
	{
		if (!CCanvasController.exists(cuid))
			return;
		
		// Add to the GroupDB
		try {
			CGroupController.groups.put(uuid, (CGroup)activity.getConstructor(long.class,long.class,long.class).newInstance(uuid, cuid, puid));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CCanvasController.canvases.get(cuid).addChildGroup(uuid);
	}

}
