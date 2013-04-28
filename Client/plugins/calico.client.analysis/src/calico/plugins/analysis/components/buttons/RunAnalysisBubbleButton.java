package calico.plugins.analysis.components.buttons;

import calico.components.piemenu.PieMenuButton;

public class RunAnalysisBubbleButton extends PieMenuButton {
//	public static int SHOWON = PieMenuButton.SHOWON_SCRAP_MENU;
//	private boolean isActive = false;
//
	
	//TODO[mottalrd] Please implement the run analysis tag? (Maybe Nick?)
	public RunAnalysisBubbleButton(long uuid) {
		super(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage("analysis.runanalysis"));

		this.uuid = uuid;

	}
//
//	public void onPressed(InputEventInfo ev) {
//		if (!CGroupController.exists(this.uuid) || isActive) {
//			return;
//		}
//
//		isActive = true;
//	}
//
//	public void onReleased(InputEventInfo ev) {
//		ev.stop();
//		
//		JFrame parent = new JFrame();
//	    JOptionPane optionPane = new JOptionPane();
//	    JSlider slider = JSliderOnJOptionPane.getSliderAnalysis(optionPane);
//	    optionPane.setMessage(new Object[] { "Select a value: ", slider });
//	    optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
//	    optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
//	    JDialog dialog = optionPane.createDialog(parent, "My Slider");
//	    dialog.setVisible(true);
//
//	    Integer response=(Integer)optionPane.getInputValue();
//		
//		AnalysisPlugin.UI_send_command(AnalysisNetworkCommands.ANALYSIS_RUN_ANALYSIS, uuid, response.doubleValue());
//		isActive = false;
//	}

}