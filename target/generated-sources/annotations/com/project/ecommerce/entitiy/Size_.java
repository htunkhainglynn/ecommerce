package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Size.class)
public abstract class Size_ {

	public static volatile ListAttribute<Size, Product> product;
	public static volatile SingularAttribute<Size, String> name;
	public static volatile SingularAttribute<Size, Integer> id;

	public static final String PRODUCT = "product";
	public static final String NAME = "name";
	public static final String ID = "id";

}

