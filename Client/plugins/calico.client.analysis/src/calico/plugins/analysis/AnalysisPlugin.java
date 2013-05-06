package calico.plugins.analysis;

import java.awt.Polygon;
import java.awt.geom.Ellipse2D;

import org.apache.log4j.Logger;

import calico.CalicoOptions;
import calico.CalicoUtils;
import calico.components.CGroup;
import calico.components.menus.CanvasStatusBar;
import calico.components.tags.Tag;
import calico.controllers.CGroupController;
import calico.events.CalicoEventHandler;
import calico.events.CalicoEventListener;
import calico.inputhandlers.CalicoInputManager;
import calico.networking.Networking;
import calico.networking.PacketHandler;
import calico.networking.netstuff.CalicoPacket;
import calico.networking.netstuff.NetworkCommand;
import calico.plugins.CalicoPlugin;
import calico.plugins.analysis.components.buttons.CPUTagButton;
import calico.plugins.analysis.components.buttons.DBTagButton;
import calico.plugins.analysis.components.buttons.FinalNodeTagButton;
import calico.plugins.analysis.components.buttons.InitialNodeTagButton;
import calico.plugins.analysis.components.buttons.NETTagButton;
import calico.plugins.analysis.components.buttons.RAMTagButton;
import calico.plugins.analysis.components.buttons.RunTagButton;
import calico.plugins.analysis.controllers.ADAnalysisController;
import calico.plugins.analysis.controllers.ADMenuController;
import calico.plugins.analysis.inputhandlers.AnalysisInputHandler;
import calico.plugins.analysis.utils.ActivityShape;
import calico.utils.Geometry;

/*
 * The entry point to load the plugin and the main logic is here
 * Contains all the static references to the graphical objects
 * and it is in care of modifying how they appear
 */
public class AnalysisPlugin extends CalicoPlugin implements CalicoEventListener {

	//the logger for this plugin
	public static Logger logger = Logger.getLogger(AnalysisPlugin.class.getName());
	
	public AnalysisPlugin() {
		super();
		PluginInfo.name = "Analysis";
		calico.plugins.analysis.iconsets.CalicoIconManager.setIconTheme(this.getClass(), CalicoOptions.core.icontheme);
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
	public void handleCalicoEvent(int event, CalicoPacket p) {
		switch (event) {
			case NetworkCommand.VIEWING_SINGLE_CANVAS:
				VIEWING_SINGLE_CANVAS(p);
				break;	
			case AnalysisNetworkCommands.ANALYSIS_ADD_TAG:
				ANALYSIS_ADD_TAG(p);
				break;
			case AnalysisNetworkCommands.ANALYSIS_REMOVE_TAG:
				ANALYSIS_REMOVE_TAG(p);
				break;	
			case AnalysisNetworkCommands.ANALYSIS_RUN_ANALYSIS:
				this.ANALYSIS_RUN_ANALYSIS(p);
				break;	
			case AnalysisNetworkCommands.ANALYSIS_DRAW_INITIAL_NODE:
				this.ANALYSIS_DRAW_INITIAL_NODE(p);
				break;
			case AnalysisNetworkCommands.ANALYSIS_DRAW_FINAL_NODE:
				this.ANALYSIS_DRAW_FINAL_NODE(p);
				break;
		}
	}

	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPluginEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPluginStart() {
		//Add the analysis button to the status bar at the bottom
		CanvasStatusBar.addMenuButtonRightAligned(CPUTagButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(DBTagButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(RAMTagButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(NETTagButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(RunTagButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(InitialNodeTagButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(FinalNodeTagButton.class);
		
		//Register to the events I am interested in 
		CalicoEventHandler.getInstance().addListener(NetworkCommand.VIEWING_SINGLE_CANVAS, this, CalicoEventHandler.PASSIVE_LISTENER);
		for (Integer event : this.getNetworkCommands())
			CalicoEventHandler.getInstance().addListener(event.intValue(), this, CalicoEventHandler.ACTION_PERFORMER_LISTENER);
		
		//Initialize the controllers
		ADMenuController.initialize();

	}

	@Override
	public Class<?> getNetworkCommandsClass() {
		return AnalysisNetworkCommands.class;
	}
	
	/*************************************************
	 * UI ENTRY POINTS
	 * The user interface calls those methods
	 * which create the command packets to send
	 * to the network
	 *************************************************/
	
	public static void UI_send_command(int com, Object... params){
		//Create the packet
		CalicoPacket p;
		if(params!=null){
			p=CalicoPacket.getPacket(com, params);
		}
		else{
			p=CalicoPacket.getPacket(com);
		}
		p.rewind();
		//Send the packet locally
		PacketHandler.receive(p);
		//Send the packet to the network (server)
		Networking.send(p);	
	}
	
	/*************************************************
	 * COMMANDS
	 * This is where the actual commands are 
	 * received and processed by the different
	 * controllers
	 *************************************************/

	private void ANALYSIS_ADD_TAG(CalicoPacket p){
		p.rewind();
		p.getInt();
		long guuid=p.getLong();
		String type_name=p.getString();
		
		ADMenuController.add_tag(guuid, type_name);		
	}
	
	private void ANALYSIS_REMOVE_TAG(CalicoPacket p){
		p.rewind();
		p.getInt();
		long guuid=p.getLong();
		String type_name=p.getString();
		
		ADMenuController.remove_tag(guuid, type_name);		
	}
	
	//TODO[mottalrd] Probability +/- buttons
	//TODO[mottalrd] Run distance selector
	
	private void VIEWING_SINGLE_CANVAS(CalicoPacket p) {
		p.rewind();
		p.getInt();
		long cuid = p.getLong();
		
		//Show all the tags
		for(long key: CGroupController.groupdb.keySet()){
			CGroup group=CGroupController.groupdb.get(key);
			for(Tag tag: group.getTags()){
				tag.show();
			}
		}
	}
	
	private void ANALYSIS_RUN_ANALYSIS(CalicoPacket p){
		p.rewind();
		p.getInt();
		long uuid=p.getLong();
		double distance=p.getDouble();
		
		ADAnalysisController.runAnalysis(uuid, distance);
	}
	
	private void ANALYSIS_DRAW_FINAL_NODE(CalicoPacket p) {
		p.rewind();
		p.getInt();
		long uuid=p.getLong();
		double x= p.getDouble();
		double y= p.getDouble();
		
		Ellipse2D elip = new Ellipse2D.Double(x, y, 40, 40);
		Polygon poly=Geometry.getPolyFromPath(elip.getPathIterator(null));
		//The old way
		//Polygon poly=ActivityShape.INITIALNODE.getShape((int)x,(int)y);
		
		CGroupController.no_notify_apply_new_shape(uuid, poly);
	}

	private void ANALYSIS_DRAW_INITIAL_NODE(CalicoPacket p) {
		p.rewind();
		p.getInt();
		long uuid=p.getLong();
		double x= p.getDouble();
		double y= p.getDouble();
		
		Ellipse2D elip = new Ellipse2D.Double(x, y, 40, 40);
		Polygon poly=Geometry.getPolyFromPath(elip.getPathIterator(null));
		//The old way
		//Polygon poly=ActivityShape.INITIALNODE.getShape((int)x,(int)y);
		
		CGroupController.no_notify_apply_new_shape(uuid, poly);
	}

}
