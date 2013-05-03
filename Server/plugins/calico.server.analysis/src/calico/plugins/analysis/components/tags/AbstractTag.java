package calico.plugins.analysis.components.tags;

import calico.components.tags.Tag;

public abstract class AbstractTag extends Tag{

	public boolean equals(Object o){
		if(!(o instanceof AbstractTag)) return false;
		AbstractTag tag=(AbstractTag) o;
		return tag.getClass().getName().equals(this.getClass().getName());
	}
	
	public int hashCode(){
		return this.getClass().getName().hashCode();
	}
	
}
