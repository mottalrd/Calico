package calico.components.tags;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Tags are elements associated to the CGroups
 * that can be displayed near to them in order
 * to add additional infos
 * @author motta
 *
 */
public abstract class Tag {
	
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
