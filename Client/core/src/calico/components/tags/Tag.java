package calico.components.tags;

import calico.controllers.CGroupController.Listener;


public abstract class Tag implements Listener{

	/** The group id this tag belongs to **/
	protected long guuid;
	
	public long getGUUID() {
		return guuid;
	}

	public void setGUUID(long guuid) {
		this.guuid = guuid;
	}
	
	public abstract void create();
	
	public abstract void move();
	
	public abstract void delete();
	
	public static Tag makeNewInstance(String tagClassName){
		Tag newTag=null;;
		try {
			newTag = (Tag)Class.forName(tagClassName).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return newTag;
	}
}
