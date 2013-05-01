package calico.plugins.analysis;

import it.unimi.dsi.fastutil.longs.LongSet;

import java.awt.Polygon;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import calico.clients.Client;
import calico.clients.ClientManager;
import calico.controllers.CCanvasController;
import calico.events.CalicoEventHandler;
import calico.events.CalicoEventListener;
import calico.networking.netstuff.CalicoPacket;
import calico.plugins.AbstractCalicoPlugin;
import calico.plugins.analysis.components.activitydiagram.FinalNode;
import calico.plugins.analysis.components.activitydiagram.InitialNode;
import calico.plugins.analysis.controllers.ADMenuController;
import calico.plugins.analysis.utils.ActivityShape;


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
			case AnalysisNetworkCommands.ANALYSIS_CREATE_ACTIVITY_NODE_TYPE:
				this.ANALYSIS_CREATE_ACTIVITY_NODE_TYPE(p);
				break;
			case AnalysisNetworkCommands.ANALYSIS_INITIAL_NODE_LOAD:
				ANALYSIS_INITIAL_NODE_LOAD(p, c);
				break;
			case AnalysisNetworkCommands.ANALYSIS_FINAL_NODE_LOAD:
				ANALYSIS_FINAL_NODE_LOAD(p, c);
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
	
	private void ANALYSIS_FINAL_NODE_LOAD(CalicoPacket p, Client c) {
		p.rewind();
		p.getInt();
		// taken from PacketHandler.GROUP_LOAD()
		long uuid = p.getLong();
		long cuid = p.getLong();
		long puid = p.getLong();
		boolean isperm = p.getBoolean();
		int count = p.getCharInt();

		if (count <= 0) {
			return;
		}

		int[] xArr = new int[count], yArr = new int[count];
		for (int i = 0; i < count; i++) {
			xArr[i] = p.getInt();
			yArr[i] = p.getInt();
		}
		boolean captureChildren = p.getBoolean();
		double rotation = p.getDouble();
		double scaleX = p.getDouble();
		double scaleY = p.getDouble();
		String text = p.getString();
		// begin analysis node

		Polygon poly = new Polygon(xArr, yArr, count);

		ADMenuController.no_notify_create_activitydiagram_node(uuid, cuid,
				poly, FinalNode.class, ActivityShape.FINALNODE, "");

		if (c != null) {
			ClientManager.send_except(c, p);
		}
		// for undo
		CCanvasController.snapshot_group(uuid);
	}

	private void ANALYSIS_INITIAL_NODE_LOAD(CalicoPacket p, Client c) {
		p.rewind();
		p.getInt();
		// taken from PacketHandler.GROUP_LOAD()
		long uuid = p.getLong();
		long cuid = p.getLong();
		long puid = p.getLong();
		boolean isperm = p.getBoolean();
		int count = p.getCharInt();

		if (count <= 0) {
			return;
		}

		int[] xArr = new int[count], yArr = new int[count];
		for (int i = 0; i < count; i++) {
			xArr[i] = p.getInt();
			yArr[i] = p.getInt();
		}
		boolean captureChildren = p.getBoolean();
		double rotation = p.getDouble();
		double scaleX = p.getDouble();
		double scaleY = p.getDouble();
		String text = p.getString();
		// begin analysis node

		Polygon poly = new Polygon(xArr, yArr, count);

		ADMenuController.no_notify_create_activitydiagram_node(uuid, cuid,
				poly, InitialNode.class, ActivityShape.INITIALNODE, "");

		if (c != null) {
			ClientManager.send_except(c, p);
		}
		// for undo
		CCanvasController.snapshot_group(uuid);
	}
	
	private void ANALYSIS_CREATE_ACTIVITY_NODE_TYPE(CalicoPacket p){
		p.rewind();
		p.getInt();
		long new_uuid=p.getLong();
		long cuuid=p.getLong();
		int x=p.getInt();
		int y=p.getInt();
		String type_name=p.getString();
		
		if(type_name.equals(InitialNode.class.getName())){
			ADMenuController.create_initial_node(new_uuid,cuuid,x,y);
		}
		else if(type_name.equals(FinalNode.class.getName())){
			ADMenuController.create_final_node(new_uuid,cuuid,x,y);
		}
		
	}

}
