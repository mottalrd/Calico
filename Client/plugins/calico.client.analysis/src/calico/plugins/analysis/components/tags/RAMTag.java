package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.plugins.analysis.iconsets.CalicoIconManager;
import edu.umd.cs.piccolo.nodes.PImage;

public class RAMTag extends PerformanceTag{

	public RAMTag(long guuid){
		super(guuid);
		Image img=CalicoIconManager.getIconImage("tags.buttons.ram");
		this.iconImage=new PImage();
		this.iconImage.setImage(img);
	}	
	
}
