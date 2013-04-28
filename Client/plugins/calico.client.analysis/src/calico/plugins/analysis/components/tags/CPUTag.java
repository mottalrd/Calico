package calico.plugins.analysis.components.tags;

import calico.plugins.analysis.iconsets.CalicoIconManager;

public class CPUTag extends PerformanceTag{

	public CPUTag(){
		super();
		this.iconImage=CalicoIconManager.getIconImage("tags.buttons.cpu");
	}	
	
}
