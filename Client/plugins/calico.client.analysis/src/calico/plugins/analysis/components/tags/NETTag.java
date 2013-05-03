package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.plugins.analysis.iconsets.CalicoIconManager;
import edu.umd.cs.piccolo.nodes.PImage;

public class NETTag extends PerformanceTag{
	
	public NETTag(long guuid){
		super(guuid);
		Image img=CalicoIconManager.getIconImage("tags.buttons.net");
		this.iconImage=new PImage();
		this.iconImage.setImage(img);
	}	
}
