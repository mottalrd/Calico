package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.InitialNodeTag;

public class InitialNodeTagButton extends AbstractTagButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InitialNodeTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=InitialNodeTag.class.getName();
		this.iconString = "analysis.initialnode";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
