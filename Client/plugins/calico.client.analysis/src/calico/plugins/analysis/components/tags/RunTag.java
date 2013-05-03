package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.plugins.analysis.iconsets.CalicoIconManager;
import edu.umd.cs.piccolo.nodes.PImage;

public class RunTag extends TagWithImage{

	//TODO[mottalrd][improvement] hard-coded image size
	private static int RUN_TAG_IMAGE_WIDTH=49;
	private static int RUN_TAG_IMAGE_HEIGHT=36;

	public RunTag(long guuid){
		super(guuid);
		
		Image img=CalicoIconManager.getIconImage("tags.buttons.run_tag");
		this.iconImage=new PImage();
		this.iconImage.setImage(img);
		
		this.iconWidth=RUN_TAG_IMAGE_WIDTH;
		this.iconHeight=RUN_TAG_IMAGE_HEIGHT;
		
		this.xShift=0;
		this.yShift=(-1)*RUN_TAG_IMAGE_HEIGHT;

	}

	@Override
	public void groupDeleted(long uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupHasNewConnector(long uuid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupHasLostAConnector(long uuid) {
		// TODO Auto-generated method stub
		
	}
	
}
