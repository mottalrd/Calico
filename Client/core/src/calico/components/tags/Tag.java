package calico.components.tags;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import calico.CalicoOptions;
import calico.iconsets.CalicoIconManager;
import edu.umd.cs.piccolo.nodes.PImage;

public class Tag {

	/** The image representing the tag **/
	private Image iconImage = CalicoIconManager.getIconImage("tags.speedtag");
	
	/** The position where this image must be displayed **/
	private Point tagPosition = new Point(0,0);
	
	/** The bounds of this Tag Image **/
	private Rectangle bounds=null;
	
	/**
	 * 
	 * @return the Piccolo Image displaying the tag
	 */
	public PImage getPImage()
	{
		try
		{	
			PImage img = new PImage();
			
			img.setImage( iconImage );
			img.setBounds(tagPosition.x,tagPosition.y,CalicoOptions.menu.icon_size,CalicoOptions.menu.icon_size);
			img.repaint();
			return img;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPosition(Point point)
	{
		tagPosition = point;
		bounds = new Rectangle(point.x, point.y, CalicoOptions.menu.icon_size, CalicoOptions.menu.icon_size);
	}
	
	public void setBounds(Rectangle r){
		this.bounds=r;
	}
	
	public Rectangle getBounds(){
		return this.bounds;
	}
	
}
