package cho.carbon.imodel.model.neo4j.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

import cho.carbon.meta.enun.RelationType;

@RelationshipEntity
public class ItemRelation {

	 @Id @GeneratedValue  private Long relationshipId;
	
	@StartNode
	Item StartItem;
	
	@EndNode
	Item endItem;
	
	@Property String typeCode;
	@Property String name;
	@Property String relationType;
	@Property String usingState;
	@Property String giant;
	@Property String leftModelCode;
	@Property String rightModelCode;
	public ItemRelation(Item startItem, Item endItem, String typeCode, String name) {
		super();
		StartItem = startItem;
		this.endItem = endItem;
		this.typeCode = typeCode;
		this.name = name;
	}

	public ItemRelation() {}

	public Long getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(Long relationshipId) {
		this.relationshipId = relationshipId;
	}

	public Item getStartItem() {
		return StartItem;
	}

	public void setStartItem(Item startItem) {
		StartItem = startItem;
	}

	public Item getEndItem() {
		return endItem;
	}

	public void setEndItem(Item endItem) {
		this.endItem = endItem;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRelationType(String relationType) {
		 RelationType relationType2 = RelationType.getRelationType(Integer.parseInt(relationType));
		 
		 if (relationType2 != null) {
			 this.relationType = relationType2.getCName();
		 } else {
			 this.relationType = relationType;
		 }
	}

	public void setUsingState(String usingState) {
		
		if ("1".equals(usingState)) {
			this.usingState = "启用";
		} else if ("0".equals(usingState)) {
			this.usingState = "弃用";
		} else {
			this.usingState = usingState;			
		}
	}

	public void setGiant(String giant) {
		if ("1".equals(giant)) {
			this.giant = "巨型";
		} else if ("0".equals(giant)) {
			this.giant = "普通";
		} else {
			this.giant = giant;			
		}
	}

	public void setLeftModelCode(String leftModelCode) {
		this.leftModelCode = leftModelCode;
	}

	public void setRightModelCode(String rightModelCode) {
		this.rightModelCode = rightModelCode;
	}
	
	
}
