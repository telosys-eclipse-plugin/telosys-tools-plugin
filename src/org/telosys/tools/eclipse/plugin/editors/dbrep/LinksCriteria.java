package org.telosys.tools.eclipse.plugin.editors.dbrep;

public class LinksCriteria {

	private boolean bOwningSide  = true ;
	private boolean bInverseSide = true ;

	private boolean bManyToMany = true ;
	private boolean bManyToOne  = true ;
	private boolean bOneToMany  = true ;
	private boolean bOneToOne   = true ;
	
	
	public void setTypeManyToMany(boolean v) {
		bManyToMany = v;
	}

	public void setTypeManyToOne(boolean v) {
		bManyToOne = v;
	}

	public void setTypeOneToMany(boolean v) {
		bOneToMany = v;
	}

	public void setTypeOneToOne(boolean v) {
		bOneToOne = v;
	}

	public void setOwningSide(boolean v) {
		bOwningSide = v;
	}

	public void setInverseSide(boolean v) {
		bInverseSide = v;
	}

	public boolean isOwningSide() {
		return bOwningSide;
	}
	public boolean isInverseSide() {
		return bInverseSide;
	}
	
	public boolean isTypeManyToMany() {
		return bManyToMany ;
	}
	public boolean isTypeManyToOne()  {
		return bManyToOne;
	}
	public boolean isTypeOneToMany()  {
		return bOneToMany;
	}
	public boolean isTypeOneToOne()   {
		return bOneToOne ;
	}

	public String toString() {
		return "Owning Side = " + bOwningSide 
			+ ", Inverse Side = " + bInverseSide
			+ ", ManyToMany = " + bManyToMany
			+ ", ManyToOne = " + bManyToOne
			+ ", OneToMany = " + bOneToMany
			+ ", OneToOne = " + bOneToOne 
			;
	}
}
