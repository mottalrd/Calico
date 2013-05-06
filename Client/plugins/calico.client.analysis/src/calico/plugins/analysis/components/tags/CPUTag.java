package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.plugins.analysis.AnalysisConfiguration;
import calico.plugins.analysis.iconsets.CalicoIconManager;
import edu.umd.cs.piccolo.nodes.PImage;

public class CPUTag extends PerformanceTag{

	public CPUTag(long guuid){
		super(guuid);
		Image img=CalicoIconManager.getIconImage("tags.buttons.cpu");
		this.iconImage=new PImage();
		this.iconImage.setImage(img);
		
		this.time=AnalysisConfiguration.CPU_TIME;
	}

	
}
