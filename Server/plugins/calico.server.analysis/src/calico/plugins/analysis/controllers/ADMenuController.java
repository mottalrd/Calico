package calico.plugins.analysis.controllers;

import java.util.List;

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
	
	public static void remove_tag(long guuid, String tag_type){
		no_notify_remove_tag(guuid, tag_type);
	}
	
	/*************************************************
	 * NO NOTIFY METHODS
	 * 
	 * Methods which use the CGroupController
	 * to create the actual nodes
	 *************************************************/		
	public static void no_notify_add_tag(long guuid, String tag_type){
		//First get the tag type we want to add to this scrap
		Tag tag=Tag.makeNewInstance(tag_type);
		
		if(!CGroupController.groups.get(guuid).getTags().contains(tag)){
			//Add the tag to the scrap
			CGroupController.groups.get(guuid).addTag(tag);
		}else{
			//The scrap has already this tag, nothing to do
		}
		
	}
	
	public static void no_notify_remove_tag(long guuid, String tag_type){
		//First get the tag type we want to add to this scrap
		Tag tag=Tag.makeNewInstance(tag_type);
		
		//If the tag is already in there, remove it
		if(CGroupController.groups.get(guuid).getTags().contains(tag)){
			//Remove the tag from the scrap
			List<Tag> tags=CGroupController.groups.get(guuid).getTags();
			Tag alreadyInTag=tags.get(tags.indexOf(tag));
			CGroupController.groups.get(guuid).removeTag(alreadyInTag);
		}else{
			//The tag is not in, nothing to do
		}
		
	}	
}
