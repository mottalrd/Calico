package calico.plugins.analysis.inputhandlers;

import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CGroupController;
import calico.inputhandlers.CGroupInputHandler;
import calico.inputhandlers.InputEventInfo;
import calico.plugins.analysis.AnalysisNetworkCommands;
import calico.plugins.analysis.AnalysisPlugin;
import calico.plugins.analysis.components.tags.ForkDecisionTag;
import calico.plugins.analysis.components.tags.RunTag;
import calico.plugins.analysis.components.tags.TagWithImage;

public class AnalysisInputHandler extends CGroupInputHandler{

	private long guuid;

	public AnalysisInputHandler(long uuid) {
		super(uuid);
		this.guuid=uuid;
	}

	public void actionReleased(InputEventInfo e){
		CGroup group=CGroupController.groupdb.get(guuid);
		TagWithImage tag=null;
		
		if((tag=this.getRunTag(group))!=null){
			if(tag.getIconImageBounds().contains(e.getX(), e.getY())) 
				AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_RUN_ANALYSIS, this.uuid, (double)50.0);
		}
		if((tag=this.getForkDecisionTag(group))!=null){
			if(tag.getIconImageBounds().contains(e.getX(), e.getY())) 
				((ForkDecisionTag)tag).switchForkDecisionMenu();
		}
		
		super.actionReleased(e);
	}

	private RunTag getRunTag(CGroup group) {
		for(Tag tag: group.getTags()){
			if(tag instanceof RunTag){
				return (RunTag) tag;
			}
		}
		return null;
	}
	
	private ForkDecisionTag getForkDecisionTag(CGroup group) {
		for(Tag tag: group.getTags()){
			if(tag instanceof ForkDecisionTag){
				return (ForkDecisionTag) tag;
			}
		}
		return null;
	}

	
}
