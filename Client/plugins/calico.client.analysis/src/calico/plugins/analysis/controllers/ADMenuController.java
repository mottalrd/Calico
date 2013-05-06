package calico.plugins.analysis.controllers;

import java.util.List;

import calico.components.tags.Tag;
import calico.controllers.CConnectorController;
import calico.controllers.CGroupController;
import calico.plugins.analysis.components.tags.ConnectorTag;
import calico.plugins.analysis.components.tags.GroupTag;

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
		Tag tag=Tag.makeNewInstance(tag_type, guuid);
		
		if(tag instanceof GroupTag){
			if(!CGroupController.groupdb.get(guuid).getTags().contains(tag)){
				//Add the tag to the scrap
				CGroupController.groupdb.get(guuid).addTag(tag);
			}
		}else if(tag instanceof ConnectorTag){
			if(!CConnectorController.connectors.get(guuid).getTags().contains(tag)){
				//Add the tag to the connector
				CConnectorController.connectors.get(guuid).addTag(tag);
			}
		}
	}
	
	public static void no_notify_remove_tag(long guuid, String tag_type){
		//First get the tag type we want to add to this scrap
		Tag tag=Tag.makeNewInstance(tag_type, guuid);
		
		if(tag instanceof GroupTag){
			//If the tag is already in there, remove it
			if(CGroupController.groupdb.get(guuid).getTags().contains(tag)){
				//Remove the tag from the scrap
				List<Tag> tags=CGroupController.groupdb.get(guuid).getTags();
				Tag alreadyInTag=tags.get(tags.indexOf(tag));
				CGroupController.groupdb.get(guuid).removeTag(alreadyInTag);
			}			
		}else if(tag instanceof ConnectorTag){
			//If the tag is already in there, remove it
			if(CConnectorController.connectors.get(guuid).getTags().contains(tag)){
				//Remove the tag from the scrap
				List<Tag> tags=CConnectorController.connectors.get(guuid).getTags();
				Tag alreadyInTag=tags.get(tags.indexOf(tag));
				CConnectorController.connectors.get(guuid).removeTag(alreadyInTag);
			}	
		}
	}	
}
