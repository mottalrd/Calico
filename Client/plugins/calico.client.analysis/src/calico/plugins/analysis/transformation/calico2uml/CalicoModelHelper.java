package calico.plugins.analysis.transformation.calico2uml;

import java.util.HashSet;

import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.plugins.analysis.components.tags.InitialNodeTag;
import calico.plugins.analysis.transformation.uml.CActivityNode;
import calico.plugins.analysis.transformation.uml.CControlFlow;

public class CalicoModelHelper {

	public static CActivityNode getInitialNode(){
        for(long uuid: CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getChildGroups()){
        	for(Tag tag: CGroupController.groupdb.get(uuid).getTags()){
            	if(tag instanceof InitialNodeTag){
    				return (CActivityNode) new CActivityNode(uuid);
    			}        		
        	}

        }
        return null;
	}
	
	public static HashSet<CControlFlow> getControlFlows(){
		HashSet<CControlFlow> flows=new HashSet<CControlFlow>();
		for(long uuid: CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getChildConnectors()){
			//TODO[mottalrd][improvement] add more controls if this is a valid control flow (i.e. start and end nodes are activity nodes)
			flows.add(new CControlFlow(uuid));	
		}	
		return flows;
	}
	
}
