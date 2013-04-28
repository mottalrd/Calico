package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.RAMTag;

public class RAMTagButton extends AbstractTagButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RAMTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=RAMTag.class.getName();
		this.iconString = "tags.buttons.ram";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
