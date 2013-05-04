package calico.plugins.analysis.transformation.uml2prism;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.Model;
import calico.plugins.analysis.transformation.uml2prism.prism.Solver;
import calico.plugins.analysis.transformation.uml2prism.translators.*;
import calico.plugins.analysis.transformation.uml2prism.uml.ActivityDecorator;
import calico.plugins.analysis.transformation.uml2prism.uml.ModelDecorator;

public class Main {
	
	/**
	 * Solves and annotates the provided UML model with Prism
	 * The provided node is the one I am interested for the target property
	 * @param m
	 */
	public void solve(Model m, ActivityNode node, double distance){
		ByteArrayOutputStream prismModelStream = new ByteArrayOutputStream();
		AtopTranslator translator = new AtopTranslator(m, prismModelStream);
		try {
			translator.translate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Generate the properties
		PropertyGenerator pg=new PropertyGenerator(translator);
		Map<ActivityNode, String> props=pg.getPropertyList(node,distance);
		
		//Solve and annotate
		Solver solver=new Solver();
		solver.solve(prismModelStream, props);
	}
		
}
