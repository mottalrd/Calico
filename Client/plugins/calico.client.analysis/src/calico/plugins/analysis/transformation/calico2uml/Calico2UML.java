package calico.plugins.analysis.transformation.calico2uml;

import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;

import java.util.HashSet;
import java.util.Stack;

import org.eclipse.uml2.uml.Action;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityFinalNode;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.ControlFlow;
import org.eclipse.uml2.uml.DecisionNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.JoinNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLFactory;

import calico.plugins.analysis.transformation.uml.CActivityNode;
import calico.plugins.analysis.transformation.uml.CControlFlow;


public class Calico2UML {

	/** records the mapping between the id of the calico groups and the uml entities */
	private Long2ReferenceOpenHashMap<ActivityNode> traceabilitymap = new Long2ReferenceOpenHashMap<ActivityNode>();
	
	/** the Eclipse UML2 model that I am going to build */
	private Model model;
	
	/** the Eclipse UML2 activity that I am going to build */
	private Activity activity;
	
	/** Used for the exploration of the calico uml activity */
    private HashSet<Long> visited=new HashSet<Long>();
    
    public enum BracketType{
    	DECISION, FORK;
    }
    
    public Stack<BracketType> brackets=new Stack<BracketType>();
	
	public Model translate(){
		//Create the model
        model = UMLFactory.eINSTANCE.createModel();
        model.setName("Calico model");

        //Create the activity
        activity=UMLFactory.eINSTANCE.createActivity();
        model.getPackagedElements().add(activity);
        
        //Get the initial node
        CActivityNode initialNode=CalicoModelHelper.getInitialNode();
        //Go visit it, this should add all the nodes starting from it
        visit(initialNode);
        
        //Add the connectors
        createControlFlows();

        return model;
	}
	
	public Long2ReferenceOpenHashMap<ActivityNode> getTraceabilityMap(){
		return traceabilitymap;
	}
	
	public Model getModel(){
		return model;
	}
	
	private void createControlFlows() {
		for(CControlFlow cf: CalicoModelHelper.getControlFlows()){
			this.addControlFlow(cf);
		}
	}

	private void visit(CActivityNode node) {
		//If you are not know I am done with this path
		if(visited.contains(node.getUUID())) return;
		
		//Otherwise let-s check who you are
		if(node.isInitialNode()){
			visited.add(node.getUUID());
			this.addInitialNode(node);
			this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());
		}
		else if(node.isFinalNode()){
			visited.add(node.getUUID());
			this.addFinalNode(node);
			//no further visit is needed
		}
		else if(node.isActivityNode()){ //has a performance tag
			if(!node.isDecision() && !node.isFork() && !node.isMerge(this.brackets) && !node.isJoin(this.brackets)){
				//I have only one outgoing node
				visited.add(node.getUUID());
				this.addActivityNode(node);
				this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());
			}else if(node.isDecision()){ 
				brackets.push(BracketType.DECISION);
				visited.add(node.getUUID());
				this.addDecisionNode(node);
				for(CControlFlow cf: node.getOutgoingConnectors()) this.visit(cf.getOutgoingNode());
			}else if(node.isFork()){
				brackets.push(BracketType.FORK);
				visited.add(node.getUUID());
				this.addForkNode(node);
				for(CControlFlow cf: node.getOutgoingConnectors()) this.visit(cf.getOutgoingNode());
			}else if(node.isMerge(this.brackets)){
				visited.add(node.getUUID());
				this.addMergeNode(node);
				this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());
			}else if(node.isJoin(this.brackets)){
				visited.add(node.getUUID());
				this.addJoinNode(node);
				this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());				
			}else{
				try{throw new Exception("Node type not recognized");}
				catch(Exception e){e.printStackTrace();}
			}
		}else{
			try{throw new Exception("Node type not recognized");}
			catch(Exception e){e.printStackTrace();}
		}
	}	

	private void addMergeNode(CActivityNode node) {
		MergeNode newmerge=UMLFactory.eINSTANCE.createMergeNode();
		activity.getNodes().add(newmerge);
		traceabilitymap.put(node.getUUID(), newmerge);
	}

	private void addForkNode(CActivityNode forkNode) {
		ForkNode newdec=UMLFactory.eINSTANCE.createForkNode();
		activity.getNodes().add(newdec);
		traceabilitymap.put(forkNode.getUUID(), newdec);
	}
	
	private void addJoinNode(CActivityNode joinNode) {
		JoinNode newdec=UMLFactory.eINSTANCE.createJoinNode();
		activity.getNodes().add(newdec);
		traceabilitymap.put(joinNode.getUUID(), newdec);
	}

	private void addFinalNode(CActivityNode finalNode) {
		ActivityFinalNode newfinal=UMLFactory.eINSTANCE.createActivityFinalNode();
		activity.getNodes().add(newfinal);
		traceabilitymap.put(finalNode.getUUID(), newfinal);
	}

	private void addInitialNode(CActivityNode initialNode){
		InitialNode initialNode_=UMLFactory.eINSTANCE.createInitialNode();
		activity.getNodes().add(initialNode_);
		traceabilitymap.put(initialNode.getUUID(), initialNode_);
	}
	
	private void addActivityNode(CActivityNode activityNode){
		Action newaction=UMLFactory.eINSTANCE.createOpaqueAction();
		
		Comment c=newaction.createOwnedComment();
		c.setBody("Speed: "+Double.toString(activityNode.getResponseTime()));
		
		activity.getNodes().add(newaction);
		traceabilitymap.put(activityNode.getUUID(), newaction);
	}
	
	private void addDecisionNode(CActivityNode decisionNode){
		DecisionNode newdec=UMLFactory.eINSTANCE.createDecisionNode();
		activity.getNodes().add(newdec);
		traceabilitymap.put(decisionNode.getUUID(), newdec);
	}	
	
	private void addControlFlow(CControlFlow controlFlow){
		ControlFlow newcf=UMLFactory.eINSTANCE.createControlFlow();
		
		if(controlFlow.getIncomingNode().isDecision()){
			Comment c=newcf.createOwnedComment();
			//TODO[mottalrd] implement probability
			c.setBody("Probability: "+0.5);
		}
		
		newcf.setSource(traceabilitymap.get(controlFlow.getIncomingNode().getUUID()));
		newcf.setTarget(traceabilitymap.get(controlFlow.getOutgoingNode().getUUID()));
		activity.getEdges().add(newcf);
	}
	
}
