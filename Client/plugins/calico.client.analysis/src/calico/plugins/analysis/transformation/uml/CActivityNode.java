package calico.plugins.analysis.transformation.uml;

import java.util.HashSet;
import java.util.Stack;

import calico.components.CConnector;
import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CConnectorController;
import calico.controllers.CGroupController;
import calico.plugins.analysis.components.tags.FinalNodeTag;
import calico.plugins.analysis.components.tags.ForkDecisionTag;
import calico.plugins.analysis.components.tags.InitialNodeTag;
import calico.plugins.analysis.components.tags.PerformanceTag;
import calico.plugins.analysis.transformation.calico2uml.Calico2UML.BracketType;


public class CActivityNode {

	private long guuid;

	public CActivityNode(long guuid) {
		this.guuid=guuid;
	}
	
	public long getUUID(){
		return guuid;
	}

	public boolean isActivityNode(){
		CGroup group=CGroupController.groupdb.get(guuid);
		for(Tag tag:group.getTags()){
			if(tag instanceof PerformanceTag) return true;
		}
		return false;
	}
	
	public boolean isInitialNode(){
		CGroup group=CGroupController.groupdb.get(guuid);
		for(Tag tag:group.getTags()){
			if(tag instanceof InitialNodeTag) return true;
		}
		return false;
	}
	
	public boolean isFinalNode(){
		CGroup group=CGroupController.groupdb.get(guuid);
		for(Tag tag:group.getTags()){
			if(tag instanceof FinalNodeTag) return true;
		}
		return false;
	}
	
	public boolean isFork(){
		CGroup group=CGroupController.groupdb.get(guuid);
		for(Tag tag:group.getTags()){
			if(tag instanceof ForkDecisionTag && ((ForkDecisionTag)tag).isFork()){
				return true;
			}else{
				return false; //we can't have more then one
			}
		}
		return false;
	}
	
	public boolean isDecision(){
		CGroup group=CGroupController.groupdb.get(guuid);
		for(Tag tag:group.getTags()){
			if(tag instanceof ForkDecisionTag && ((ForkDecisionTag)tag).isDecision()){
				return true;
			}else{
				return false; //we can't have more then one
			}
		}
		return false;
	}
	
	public HashSet<CControlFlow> getIncomingConnectors() {
		HashSet<CControlFlow> returnSet=new HashSet<CControlFlow>();
		CGroup group=CGroupController.groupdb.get(guuid);
		
		for (long cuuid : group.getChildConnectors()) {
			CConnector connector=CConnectorController.connectors.get(cuuid);
			// if I am the head of this connector
			if (connector.getAnchorUUID(CConnector.TYPE_HEAD) == this.guuid) {
				returnSet.add(new CControlFlow(cuuid));
			}
		}
		// no connector found
		return returnSet;
	}

	public HashSet<CControlFlow> getOutgoingConnectors() {
		HashSet<CControlFlow> returnSet=new HashSet<CControlFlow>();
		CGroup group=CGroupController.groupdb.get(guuid);
		
		for (long cuuid : group.getChildConnectors()) {
			CConnector connector=CConnectorController.connectors.get(cuuid);
			// if I am the head of this connector
			if (connector.getAnchorUUID(CConnector.TYPE_TAIL) == this.guuid) {
				returnSet.add(new CControlFlow(cuuid));
			}
		}
		// no connector found
		return returnSet;
	}

	public boolean isMerge(Stack<BracketType> brackets) {
		if(brackets.empty()) return false;
		if(this.getIncomingConnectors().size()>1 && brackets.peek()==BracketType.DECISION) return true;
		else return false;
	}

	public boolean isJoin(Stack<BracketType> brackets) {
		if(brackets.empty()) return false;
		if(this.getIncomingConnectors().size()>1 && brackets.peek()==BracketType.FORK) return true;
		else return false;
	}

	public double getResponseTime() {
		// TODO[mottalrd] add response time
		return 1.0;
	}
}
