package calico.plugins.analysis;

import java.awt.Polygon;

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
import calico.plugins.analysis.components.activitydiagram.FinalNode;
import calico.plugins.analysis.components.activitydiagram.InitialNode;
import calico.plugins.analysis.components.buttons.CPUTagButton;
import calico.plugins.analysis.components.buttons.CreateFinalNodeButton;
import calico.plugins.analysis.components.buttons.CreateInitialNodeButton;
import calico.plugins.analysis.components.buttons.DBTagButton;
import calico.plugins.analysis.components.buttons.NETTagButton;
import calico.plugins.analysis.components.buttons.RAMTagButton;
import calico.plugins.analysis.components.buttons.RunTagButton;
import calico.plugins.analysis.controllers.ADMenuController;
import calico.plugins.analysis.utils.ActivityShape;

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
			case AnalysisNetworkCommands.ANALYSIS_INITIAL_NODE_LOAD:
				ANALYSIS_INITIAL_NODE_LOAD(p);
				break;		
			case AnalysisNetworkCommands.ANALYSIS_FINAL_NODE_LOAD:
				ANALYSIS_FINAL_NODE_LOAD(p);
				break;	
			case AnalysisNetworkCommands.ANALYSIS_CREATE_ACTIVITY_NODE_TYPE:
				this.ANALYSIS_CREATE_ACTIVITY_NODE_TYPE(p);
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
		CanvasStatusBar.addMenuButtonRightAligned(CreateInitialNodeButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(CreateFinalNodeButton.class);
		CanvasStatusBar.addMenuButtonRightAligned(RunTagButton.class);
		
		
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
	
	//TODO[mottalrd] Probability +/- buttons
	//TODO[mottalrd] Attach the analysis procedure
	//TODO[mottalrd] Fix server side of the plugin
	
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
	
	private void ANALYSIS_FINAL_NODE_LOAD(CalicoPacket p) {
		p.rewind();
		p.getInt();
		//taken from PacketHandler.GROUP_LOAD()		
		long uuid = p.getLong();
		long cuid = p.getLong();
		//TODO[mottalrd][improvement] do we need to load the other packet information if we are not using them?
		long puid = p.getLong();
		boolean isperm = p.getBoolean();
		int count = p.getCharInt();
		
		if(count<=0)
		{
			return;
		}
		
		int[] xArr = new int[count], yArr = new int[count];
		for(int i=0;i<count;i++)
		{
			xArr[i] = p.getInt();
			yArr[i] = p.getInt();
		}
		boolean captureChildren = p.getBoolean();
		double rotation = p.getDouble();
		double scaleX = p.getDouble();
		double scaleY = p.getDouble();
		String text = p.getString();
		//begin analysis node
		
		Polygon poly = new Polygon(xArr, yArr, count);
		
		ADMenuController.no_notify_create_activitydiagram_node(uuid, cuid, poly, FinalNode.class, ActivityShape.FINALNODE, "");
		
	}

	private void ANALYSIS_INITIAL_NODE_LOAD(CalicoPacket p) {
		p.rewind();
		p.getInt();
		//taken from PacketHandler.GROUP_LOAD()		
		long uuid = p.getLong();
		long cuid = p.getLong();
		//TODO[mottalrd][improvement] do we need to load the other packet information if we are not using them?
		long puid = p.getLong();
		boolean isperm = p.getBoolean();
		int count = p.getCharInt();
		
		if(count<=0)
		{
			return;
		}
		
		int[] xArr = new int[count], yArr = new int[count];
		for(int i=0;i<count;i++)
		{
			xArr[i] = p.getInt();
			yArr[i] = p.getInt();
		}
		boolean captureChildren = p.getBoolean();
		double rotation = p.getDouble();
		double scaleX = p.getDouble();
		double scaleY = p.getDouble();
		String text = p.getString();
		//begin analysis node
		
		Polygon poly = new Polygon(xArr, yArr, count);
		
		ADMenuController.no_notify_create_activitydiagram_node(uuid, cuid, poly, InitialNode.class, ActivityShape.INITIALNODE, "");
		
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
