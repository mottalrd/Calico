package calico.plugins.analysis;

import org.apache.log4j.Logger;

import calico.CalicoOptions;
import calico.components.menus.CanvasStatusBar;
import calico.events.CalicoEventHandler;
import calico.events.CalicoEventListener;
import calico.networking.Networking;
import calico.networking.PacketHandler;
import calico.networking.netstuff.CalicoPacket;
import calico.networking.netstuff.NetworkCommand;
import calico.plugins.CalicoPlugin;
import calico.plugins.analysis.components.buttons.CPUTagButton;
import calico.plugins.analysis.components.buttons.DBTagButton;
import calico.plugins.analysis.components.buttons.NETTagButton;
import calico.plugins.analysis.components.buttons.RAMTagButton;
import calico.plugins.analysis.controllers.ADMenuController;

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
	
	//TODO[mottalrd] check if we still need them
//	private void ANALYSIS_ADD_PROBABILITY_TO_DECISION_NODE(CalicoPacket p){
//		p.rewind();
//		p.getInt();
//		long uuid=p.getLong();
//		double probability=p.getDouble();
//		
//		ADBubbleMenuController.add_probability(uuid, probability);
//	}
//	
//
//	private void ANALYSIS_RUN_ANALYSIS(CalicoPacket p){
//		p.rewind();
//		p.getInt();
//		long uuid=p.getLong();
//		double distance=p.getDouble();
//		
//		ADAnalysisController.runAnalysis(uuid, distance);
//	}
	
	
	private void VIEWING_SINGLE_CANVAS(CalicoPacket p) {
		p.rewind();
		p.getInt();
		long cuid = p.getLong();
		
		//If you have to do some actions
		//when the canvas is opened do it here (using one of your controllers)
		
		//[mottalrd] Nothing to do.
		
		//CGroup.registerPieMenuButton(ServiceTimeBubbleButton.class);
		//CGroup.registerPieMenuButton(RunAnalysisBubbleButton.class);
		//example: MenuController.getInstance().showMenu(cuid);
	}





}
