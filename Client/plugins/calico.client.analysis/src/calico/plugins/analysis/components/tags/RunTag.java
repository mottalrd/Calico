package calico.plugins.analysis.components.tags;

import java.awt.Image;

import calico.CalicoDraw;
import calico.components.CGroup;
import calico.components.tags.Tag;
import calico.controllers.CCanvasController;
import calico.controllers.CGroupController;
import calico.plugins.analysis.iconsets.CalicoIconManager;
import edu.umd.cs.piccolo.nodes.PImage;
import edu.umd.cs.piccolo.util.PBounds;

public class RunTag extends Tag{

	//TODO[mottalrd][improvement] hard-coded image size
	private static int runTagImageWIDTH=49;
	private static int runTagImageHEIGHT=36;
	
	private PImage runTagImage;

	public RunTag(){
		Image img=CalicoIconManager.getIconImage("tags.buttons.run_tag");
		this.runTagImage=new PImage();
		this.runTagImage.setImage(img);
	}
	
	public void create(){
		CGroup group=CGroupController.groupdb.get(this.guuid);
		
		//drawing stuffs
		PBounds bounds=group.getBounds();
		runTagImage.setBounds(bounds.x, bounds.y - runTagImageHEIGHT,runTagImageWIDTH,runTagImageHEIGHT);
		runTagImage.repaint();
		CalicoDraw.addChildToNode(CCanvasController.canvasdb.get(CCanvasController.getCurrentUUID()).getLayer(), runTagImage);
	}
	
	public void move(){
		PBounds bounds=CGroupController.groupdb.get(this.guuid).getBounds();
		runTagImage.setBounds(bounds.x,bounds.y - runTagImageHEIGHT,runTagImageWIDTH,runTagImageHEIGHT);
		runTagImage.repaint();
	}
	
	public void delete(){
		CalicoDraw.removeNodeFromParent(runTagImage);
		runTagImage.repaint();
	}

	@Override
	public void groupMoved(long uuid) {
		if(uuid==this.guuid) this.move();
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
	
	public boolean equals(Object o){
		if(!(o instanceof PerformanceTag)) return false;
		PerformanceTag tag=(PerformanceTag) o;
		return tag.getClass().getName().equals(this.getClass().getName());
	}
	
	public int hashCode(){
		return this.getClass().getName().hashCode();
	}
	
}
