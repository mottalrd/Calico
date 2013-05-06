package calico.plugins.analysis.transformation.uml;

import calico.components.CConnector;
import calico.components.tags.Tag;
import calico.controllers.CConnectorController;
import calico.plugins.analysis.components.tags.ProbabilityTag;

public class CControlFlow {

	private long cuid;

	public CControlFlow(long cuid){
		this.cuid=cuid;
	}
	
	/**
	 * Returns the incoming node
	 * @return
	 */
	public CActivityNode getIncomingNode(){
		CConnector connector=CConnectorController.connectors.get(cuid);
		return new CActivityNode(connector.getAnchorUUID(CConnector.TYPE_TAIL));
	}
	
	/**
	 * Returns the outgoing node
	 * @return
	 */
	public CActivityNode getOutgoingNode(){
		CConnector connector=CConnectorController.connectors.get(cuid);
		return new CActivityNode(connector.getAnchorUUID(CConnector.TYPE_HEAD));
	}	
	
	public double getProbability(){
		CConnector connector=CConnectorController.connectors.get(cuid);
		for(Tag tag: connector.getTags()){
			if(tag instanceof ProbabilityTag){
				ProbabilityTag ptag=(ProbabilityTag) tag;
				return ptag.getProbability();
			}
		}
		
		try{throw new Exception("Probability tag not found");}
		catch(Exception e){e.printStackTrace();}
		return 0.0f;
	}
	
}
