package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.CPUTag;
import calico.plugins.analysis.components.tags.RunTag;

public class RunTagButton extends AbstractTagButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RunTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=RunTag.class.getName();
		this.iconString = "tags.buttons.run_tag";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
