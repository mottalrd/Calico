package calico.plugins.analysis.transformation.uml;

import calico.components.CConnector;
import calico.controllers.CConnectorController;

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
	
}
