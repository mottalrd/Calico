package calico.plugins.analysis;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import calico.clients.Client;
import calico.clients.ClientManager;
import calico.components.CGroup;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.events.CalicoEventHandler;
import calico.events.CalicoEventListener;
import calico.networking.netstuff.CalicoPacket;
import calico.plugins.AbstractCalicoPlugin;
import calico.plugins.analysis.controllers.ADMenuController;
import calico.plugins.analysis.utils.ActivityShape;
import calico.utils.Geometry;


/*
 * The entry point to load the plugin and the main logic is here
 * Contains all the static references to the graphical objects
 * and it is in care of modifying how they appear
 */
public class AnalysisPlugin extends AbstractCalicoPlugin implements CalicoEventListener {

	//the logger for this plugin
	public static Logger logger = Logger.getLogger(AnalysisPlugin.class.getName());
	
	public AnalysisPlugin() {
		super();

		PluginInfo.name = "Analysis Plugin";
	}

	/*************************************************
	 * CALICO PLUGIN METHODS
	 * The standard methods of an entry point
	 * class for a Calico Plugin
	 *************************************************/
	
	@Override
	/**
	 * Plugin specific commands can be defined
	 * in the analysis network commands class
	 */
	public void handleCalicoEvent(int event, CalicoPacket p, Client c) {
		switch (event) {
			case AnalysisNetworkCommands.ANALYSIS_ADD_TAG:
				ANALYSIS_ADD_TAG(p,c);
				break;
			case AnalysisNetworkCommands.ANALYSIS_REMOVE_TAG:
				ANALYSIS_REMOVE_TAG(p,c);
				break;		
			case AnalysisNetworkCommands.ANALYSIS_DRAW_INITIAL_NODE:
				this.ANALYSIS_DRAW_INITIAL_NODE(p,c);
				break;
			case AnalysisNetworkCommands.ANALYSIS_DRAW_FINAL_NODE:
				this.ANALYSIS_DRAW_FINAL_NODE(p,c);
				break;
		}
	}

	public void onPluginStart() {
		// register for analysis events
		for (Integer event : this.getNetworkCommands()) {
			// debug output to let you know which events are being listened for
			// when you start the server
			System.out.println("AnalysisPlugin: attempting to listen for "
					+ event.intValue());

			// this tells the plugin manager that this pllugin is listening for
			// these events (defined in AnalysisNetworkCommands.java)
			CalicoEventHandler.getInstance().addListener(event.intValue(),
					this, CalicoEventHandler.ACTION_PERFORMER_LISTENER);

			// this adds a listener to the canvas so that the canvas updates it
			// signature (related to maintaining Synchronization between the
			// client and server)
			LongSet canvasKeys = CCanvasController.canvases.keySet();
			for (Long canvaskey : canvasKeys) {
				CalicoEventHandler.getInstance().addListener(event.intValue(),
						CCanvasController.canvases.get(canvaskey.longValue()),
						CalicoEventHandler.PASSIVE_LISTENER);
			}
		}
	}
	
	@Override
	public void onPluginEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onException(Exception e) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Integer> getNetworkCommands()
	{
		return getNetworkCommands(getNetworkCommandsClass());
	}
	
	@Override
	public Class<?> getNetworkCommandsClass() {
		return AnalysisNetworkCommands.class;
	}
	
	/*************************************************
	 * COMMANDS
	 * This is where the actual commands are 
	 * received and processed by the different
	 * controllers
	 *************************************************/

	private void ANALYSIS_ADD_TAG(CalicoPacket p, Client c){
		p.rewind();
		p.getInt();
		long guuid=p.getLong();
		String type_name=p.getString();
		
		ADMenuController.add_tag(guuid, type_name);
		
		if (c != null) {
			ClientManager.send_except(c, p);
		}
	}
	
	private void ANALYSIS_REMOVE_TAG(CalicoPacket p, Client c){
		p.rewind();
		p.getInt();
		long guuid=p.getLong();
		String type_name=p.getString();
		
		ADMenuController.remove_tag(guuid, type_name);		
		
		if (c != null) {
			ClientManager.send_except(c, p);
		}
	}
	
	private void ANALYSIS_DRAW_FINAL_NODE(CalicoPacket p, Client c) {
		p.rewind();
		p.getInt();
		long uuid=p.getLong();
		double x= p.getDouble();
		double y= p.getDouble();
		
		Ellipse2D elip = new Ellipse2D.Double(x, y, 40, 40);
		Polygon poly=Geometry.getPolyFromPath(elip.getPathIterator(null));
		//Polygon poly=ActivityShape.FINALNODE.getShape((int)x,(int)y);
		
		CGroupController.no_notify_apply_new_shape(uuid, poly);
		
		if (c != null) {
			ClientManager.send_except(c, p);
		}
	}

	private void ANALYSIS_DRAW_INITIAL_NODE(CalicoPacket p, Client c) {
		p.rewind();
		p.getInt();
		long uuid=p.getLong();
		double x= p.getDouble();
		double y= p.getDouble();
		
		Ellipse2D elip = new Ellipse2D.Double(x, y, 40, 40);
		Polygon poly=Geometry.getPolyFromPath(elip.getPathIterator(null));
		//Polygon poly=ActivityShape.INITIALNODE.getShape((int)x,(int)y);
		
		CGroupController.no_notify_apply_new_shape(uuid, poly);
		
		if (c != null) {
			ClientManager.send_except(c, p);
		}
	}

}
