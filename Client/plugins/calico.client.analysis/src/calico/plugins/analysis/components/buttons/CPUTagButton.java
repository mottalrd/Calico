package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.CPUTag;

public class CPUTagButton extends AbstractTagButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CPUTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=CPUTag.class.getName();
		this.iconString = "tags.buttons.cpu";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
