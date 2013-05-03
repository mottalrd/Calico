package calico.components.tags;


/**
 * Tags are elements associated to the CGroups
 * that can be displayed near to them in order
 * to add additional infos
 * @author motta
 *
 */
public abstract class Tag {
	
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
