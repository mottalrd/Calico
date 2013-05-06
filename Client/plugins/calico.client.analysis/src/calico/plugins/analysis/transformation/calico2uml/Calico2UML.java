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
import org.eclipse.uml2.uml.FinalNode;
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

        return model;
	}
	
	public Long2ReferenceOpenHashMap<ActivityNode> getTraceabilityMap(){
		return traceabilitymap;
	}
	
	public Model getModel(){
		return model;
	}
	

	private ActivityNode visit(CActivityNode node) {
		//If you are not know I am done with this path
		if(visited.contains(node.getUUID())){
			ActivityNode temp=this.traceabilitymap.get(node.getUUID());
			
			//TODO[mottalrd][improvement] this is a crazy fix due to the fact that we do not have a direct mapping between calico nodes and uml nodes
			ActivityNode source=temp.getIncomings().iterator().next().getSource();
			if(source instanceof MergeNode) return source;
			if(source instanceof JoinNode) return source;
			return temp;
		}
		
		//Otherwise let-s check who you are
		if(node.isInitialNode()){
			visited.add(node.getUUID());
			ActivityNode thisNode=this.addInitialNode(node);
			ActivityNode nextNode=this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());
			this.addControlFlow(thisNode, nextNode);
			return thisNode;
		}
		else if(node.isFinalNode()){
			visited.add(node.getUUID());
			FinalNode thisNode=this.addFinalNode(node);
			//no further visit is needed
			return thisNode;
		}
		else if(node.isActivityNode()){ //has a performance tag
			if(!node.isDecision() && !node.isFork() && !node.isMerge(this.brackets) && !node.isJoin(this.brackets)){
				//I have only one outgoing node
				visited.add(node.getUUID());
				ActivityNode thisNode=this.addActivityNode(node);
				ActivityNode nextNode=this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());
				this.addControlFlow(thisNode, nextNode);
				return thisNode;
			}else if(node.isDecision()){ 
				brackets.push(BracketType.DECISION);
				visited.add(node.getUUID());
				ActivityNode thisNode=this.addActivityNode(node);
				ActivityNode intermediateNode=this.addDecisionNode();
				this.addControlFlow(thisNode, intermediateNode);
				for(CControlFlow cf: node.getOutgoingConnectors()){
					ActivityNode nextNode=this.visit(cf.getOutgoingNode());
					this.addControlFlowWithProbability(intermediateNode, nextNode);
				}
				return thisNode;
			}else if(node.isFork()){
				brackets.push(BracketType.FORK);
				visited.add(node.getUUID());
				ActivityNode thisNode=this.addActivityNode(node);
				ActivityNode intermediateNode=this.addForkNode();
				this.addControlFlow(thisNode, intermediateNode);
				for(CControlFlow cf: node.getOutgoingConnectors()){
					ActivityNode nextNode=this.visit(cf.getOutgoingNode());
					this.addControlFlow(intermediateNode, nextNode);
				}
				return thisNode;
			}else if(node.isMerge(this.brackets)){
				visited.add(node.getUUID());
				ActivityNode thisNode=this.addMergeNode();
				ActivityNode intermediateNode=this.addActivityNode(node);
				this.addControlFlow(thisNode, intermediateNode);	
				ActivityNode nextNode=this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());
				this.addControlFlow(intermediateNode, nextNode);	
				return thisNode;
			}else if(node.isJoin(this.brackets)){
				visited.add(node.getUUID());
				ActivityNode thisNode=this.addJoinNode();
				ActivityNode intermediateNode=this.addActivityNode(node);
				this.addControlFlow(thisNode, intermediateNode);	
				ActivityNode nextNode=this.visit(node.getOutgoingConnectors().iterator().next().getOutgoingNode());		
				this.addControlFlow(intermediateNode, nextNode);	
				return thisNode;
			}else{
				try{throw new Exception("Node type not recognized");}
				catch(Exception e){e.printStackTrace();}
			}
		}else{
			try{throw new Exception("Node type not recognized");}
			catch(Exception e){e.printStackTrace();}
		}
		
		try{throw new Exception("Something went wrong");}
		catch(Exception e){e.printStackTrace();}
		return null;
	}	

	private MergeNode addMergeNode() {
		MergeNode newmerge=UMLFactory.eINSTANCE.createMergeNode();
		activity.getNodes().add(newmerge);
		return newmerge;
	}

	private ForkNode addForkNode() {
		ForkNode newdec=UMLFactory.eINSTANCE.createForkNode();
		activity.getNodes().add(newdec);
		return newdec;
	}
	
	private JoinNode addJoinNode() {
		JoinNode newdec=UMLFactory.eINSTANCE.createJoinNode();
		activity.getNodes().add(newdec);
		return newdec;
	}

	private FinalNode addFinalNode(CActivityNode finalNode) {
		ActivityFinalNode newfinal=UMLFactory.eINSTANCE.createActivityFinalNode();
		activity.getNodes().add(newfinal);
		traceabilitymap.put(finalNode.getUUID(), newfinal);
		return newfinal;
	}

	private InitialNode addInitialNode(CActivityNode initialNode){
		InitialNode initialNode_=UMLFactory.eINSTANCE.createInitialNode();
		activity.getNodes().add(initialNode_);
		traceabilitymap.put(initialNode.getUUID(), initialNode_);
		return initialNode_;
	}
	
	private Action addActivityNode(CActivityNode activityNode){
		Action newaction=UMLFactory.eINSTANCE.createOpaqueAction();
		
		Comment c=newaction.createOwnedComment();
		c.setBody("Speed: "+Double.toString(activityNode.getResponseTime()));
		
		activity.getNodes().add(newaction);
		traceabilitymap.put(activityNode.getUUID(), newaction);
		return newaction;
	}
	
	private DecisionNode addDecisionNode(){
		DecisionNode newdec=UMLFactory.eINSTANCE.createDecisionNode();
		activity.getNodes().add(newdec);
		return newdec;
	}	
	
	private void addControlFlowWithProbability(ActivityNode source, ActivityNode target){
		ControlFlow cf=this.addControlFlow(source, target);
		Comment c=cf.createOwnedComment();
		//TODO[mottalrd] implement probability
		c.setBody("Probability: "+0.5);
	}
	
	private ControlFlow addControlFlow(ActivityNode source, ActivityNode target){
		ControlFlow newcf=UMLFactory.eINSTANCE.createControlFlow();
		
		newcf.setSource(source);
		newcf.setTarget(target);
		activity.getEdges().add(newcf);
		
		return newcf;
	}
	
}
