package org.telosys.tools.eclipse.plugin.editors.dbrep;

import java.util.Collections;
import java.util.LinkedList;

import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.JoinTable;
import org.telosys.tools.repository.model.EntityInDbModel;
import org.telosys.tools.repository.model.ForeignKeyInDbModel;
import org.telosys.tools.repository.model.LinkInDbModel;
import org.telosys.tools.repository.model.RelationLinksInDbModel;
import org.telosys.tools.repository.model.RepositoryModel;

public class LinksManager {

	private RepositoryModel repositoryModel ;
	
	//----------------------------------------------------------------------------------------
	public LinksManager(RepositoryModel repositoryModel) {
		super();
		this.repositoryModel = repositoryModel;
	}

	//----------------------------------------------------------------------------------------
//	public Link getLinkById(String id)
	public LinkInDbModel getLinkById(String id) // v 3.0.0
	{
		return repositoryModel.getLinkById(id);
	}
	
	//----------------------------------------------------------------------------------------
	public void removeLink(String id)
	{
		repositoryModel.removeLinkById(id);
	}
	//----------------------------------------------------------------------------------------
//	public void removeRelation(RelationLinks relation)
	public void removeRelation(RelationLinksInDbModel relation) // v 3.0.0
	{
//		Link link = relation.getInverseSideLink();
		LinkInDbModel link = relation.getInverseSideLink();
		if ( link != null ) {
			repositoryModel.removeLinkById( link.getId() );
		}
		link = relation.getOwningSideLink();
		if ( link != null ) {
			repositoryModel.removeLinkById( link.getId() );
		}		
	}
	//----------------------------------------------------------------------------------------
//	public RelationLinks getRelationByLinkId(String id)
	public RelationLinksInDbModel getRelationByLinkId(String id) // v 3.0.0
	{
		return repositoryModel.getRelationByLinkId(id);
	}
	
	//----------------------------------------------------------------------------------------
//	private void sortLinks( LinkedList<Link> linksList )
	private void sortLinks( LinkedList<LinkInDbModel> linksList )
	{
        Collections.sort(linksList, new LinksComparer( LinksComparer.ASC ) );
	}
	//----------------------------------------------------------------------------------------
//	public LinkedList<Link> getAllLinks()
	public LinkedList<LinkInDbModel> getAllLinks()
	{
//		LinkedList<Link> linksList = new LinkedList<Link>();
		LinkedList<LinkInDbModel> linksList = new LinkedList<LinkInDbModel>();
//		Entity [] entities = repositoryModel.getEntities();
//		for ( int i = 0 ; i < entities.length ; i++ ) {
		for ( EntityInDbModel entity : repositoryModel.getEntitiesArray() ) {
//			Entity entity = entities[i];
//			Link [] links = entity.getLinks();
//			for ( int j = 0 ; j < links.length ; j++ ) {
//				linksList.add(links[j]);
//			}
			for ( LinkInDbModel link : entity.getLinksArray() ) {
				linksList.add(link);
			}
		}
		//log("getAllLinks() : list size = " + linksList.size());
		
		sortLinks( linksList );

		return linksList ;
	}
	
	//----------------------------------------------------------------------------------------
	public int countAllLinks()
	{
		int count = 0 ;
//		Entity [] entities = repositoryModel.getEntities();
//		for ( int i = 0 ; i < entities.length ; i++ ) {
//			Entity entity = entities[i];
//			Link [] links = entity.getLinks();
//			for ( int j = 0 ; j < links.length ; j++ ) {
//				count++;
//			}
//		}
		// ver 3.0.0
		for ( EntityInDbModel entity : repositoryModel.getEntitiesArray() ) {
			count = count + entity.getLinksCount();
		}
		return count ;
	}
	
	//----------------------------------------------------------------------------------------
//	public LinkedList<Link> getLinks(LinksCriteria criteria)
	public LinkedList<LinkInDbModel> getLinks(LinksCriteria criteria) // v 3.0.0
	{
//		LinkedList<Link> linksList = new LinkedList<Link>();
//		Entity [] entities = repositoryModel.getEntities();
//		for ( int i = 0 ; i < entities.length ; i++ ) {
//			Entity entity = entities[i];
//			Link [] links = entity.getLinks();
//			for ( int j = 0 ; j < links.length ; j++ ) {
//				Link link = links[j];
//				if ( checkCriteria(link, criteria ) ) {
//					linksList.add(link);
//				}
//			}
//		}
		// ver 3.0.0
		LinkedList<LinkInDbModel> linksList = new LinkedList<LinkInDbModel>();
		for ( EntityInDbModel entity : repositoryModel.getEntitiesArray() ) {
			for ( LinkInDbModel link : entity.getLinksArray() ) {
				if ( checkCriteria(link, criteria ) ) {
					linksList.add(link);
				}
			}
		}
		sortLinks( linksList );		
		return linksList ;
	}
	
	//----------------------------------------------------------------------------------------
//	private boolean checkCardinalityCriteria(Link link, LinksCriteria criteria ) 
	private boolean checkCardinalityCriteria(LinkInDbModel link, LinksCriteria criteria ) // v 3.0.0
	{
//		if ( criteria.isTypeManyToMany() && link.isTypeManyToMany() ) return true ;
//		if ( criteria.isTypeManyToOne()  && link.isTypeManyToOne()  ) return true ;
//		if ( criteria.isTypeOneToMany()  && link.isTypeOneToMany()  ) return true ;
//		if ( criteria.isTypeOneToOne()   && link.isTypeOneToOne()   ) return true ;
		// v 3.0.0
		if ( criteria.isTypeManyToMany() && link.getCardinality() == Cardinality.MANY_TO_MANY ) return true ;
		if ( criteria.isTypeManyToOne()  && link.getCardinality() == Cardinality.MANY_TO_ONE ) return true ;
		if ( criteria.isTypeOneToMany()  && link.getCardinality() == Cardinality.ONE_TO_MANY  ) return true ;
		if ( criteria.isTypeOneToOne()   && link.getCardinality() == Cardinality.ONE_TO_ONE   ) return true ;
		return false ;
	}
	
	//----------------------------------------------------------------------------------------
//	private boolean checkCriteria(Link link, LinksCriteria criteria ) 
	private boolean checkCriteria(LinkInDbModel link, LinksCriteria criteria ) 
	{
		if ( criteria != null ) 
		{
			if ( criteria.isOwningSide() && link.isOwningSide() ) {
				return checkCardinalityCriteria(link, criteria )  ;
			}
			if ( criteria.isInverseSide() && ( ! link.isOwningSide() ) ) {
				return checkCardinalityCriteria(link, criteria )  ;
			}
			return false ;
		}
		return true ; // No criteria 
	}
	
	//----------------------------------------------------------------------------------------
//	public ForeignKey getForeignKey(String fkName)
	public ForeignKeyInDbModel getForeignKey(String fkName) // v 3.0.0
	{
		return repositoryModel.getForeignKeyByName(fkName);
	}
	//----------------------------------------------------------------------------------------
//	public Entity getEntity(String name)
	public EntityInDbModel getEntityByTableName(String entityTableName) // v 3.0.0 
	{
		//return repositoryModel.getEntityByName(name);
		return repositoryModel.getEntityByTableName(entityTableName);
	}
	//----------------------------------------------------------------------------------------
	public final static int NO_BASIS                = 0 ;
	public final static int EXISTING_FOREIGN_KEY    = 1 ;
	public final static int NONEXISTENT_FOREIGN_KEY = 2 ;
	public final static int EXISTING_JOIN_TABLE     = 3 ;
	public final static int NONEXISTENT_JOIN_TABLE  = 4 ;
	
	//----------------------------------------------------------------------------------------
//	public int getLinkBasis(Link link)
	public int getLinkBasis(LinkInDbModel link)
	{
		if ( link.isBasedOnForeignKey() ) 
		{
			String fkName = link.getForeignKeyName();
//			ForeignKey fk = getForeignKey(fkName);
			ForeignKeyInDbModel fk = getForeignKey(fkName);
			if ( fk != null ) {
				return EXISTING_FOREIGN_KEY ;
			}
			else {
				return NONEXISTENT_FOREIGN_KEY ;
			}
		}
		else if ( link.isBasedOnJoinTable() ) {
			JoinTable joinTable = link.getJoinTable();
			if ( joinTable != null ) {
				return EXISTING_JOIN_TABLE ;
			}
			else {
				return NONEXISTENT_JOIN_TABLE ;
			}
		}
		return NO_BASIS ;
	}
	
}
