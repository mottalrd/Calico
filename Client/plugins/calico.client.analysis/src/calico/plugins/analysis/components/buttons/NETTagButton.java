package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.NETTag;

public class NETTagButton extends AbstractTagButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NETTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=NETTag.class.getName();
		this.iconString = "tags.buttons.net";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
