package calico.plugins.analysis.components.buttons;

import calico.plugins.analysis.components.tags.FinalNodeTag;

public class FinalNodeTagButton extends AbstractTagButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FinalNodeTagButton(long c) {
		super();
		cuid = c;

		this.tagClassName=FinalNodeTag.class.getName();
		this.iconString = "analysis.finalnode";
		
		try {
			setImage(calico.plugins.analysis.iconsets.CalicoIconManager.getIconImage(iconString));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
