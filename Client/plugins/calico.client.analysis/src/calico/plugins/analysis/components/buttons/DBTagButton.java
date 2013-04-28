package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.DBTag;

public class DBTagButton extends AbstractTagButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DBTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=DBTag.class.getName();
		this.iconString = "tags.buttons.db";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
