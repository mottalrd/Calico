package calico.plugins.analysis.controllers;

import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CGroupController;

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
		Class<?> tag=null;
		try {
			tag = Class.forName(tag_type);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		//Add the tag to the scrap
		try {
			CGroupController.add_tag((Tag)tag.newInstance(), guuid);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
