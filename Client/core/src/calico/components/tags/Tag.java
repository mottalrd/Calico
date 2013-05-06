package calico.components.tags;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public abstract class Tag{
	
	/** The group id this tag belongs to **/
	protected long guuid;
	
	public Tag(long guuid){
		this.guuid=guuid;
	}
	
	public long getGUUID(){
		return guuid;
	}
	
	public abstract void show();
	
	public abstract void move();
	
	public abstract void hide();
	
	public static Tag makeNewInstance(String tagClassName, long guuid){
		Tag newTag=null;
		
		try {
			@SuppressWarnings("unchecked")
			Constructor<Tag> c = (Constructor<Tag>) Class.forName(tagClassName).getConstructor(long.class);
			newTag = (Tag) c.newInstance(guuid);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return newTag;
	}
}
