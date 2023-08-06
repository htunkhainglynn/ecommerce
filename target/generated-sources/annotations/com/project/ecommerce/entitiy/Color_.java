package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Color.class)
public abstract class Color_ {

	public static volatile ListAttribute<Color, Product> product;
	public static volatile SingularAttribute<Color, String> name;
	public static volatile SingularAttribute<Color, Integer> id;

	public static final String PRODUCT = "product";
	public static final String NAME = "name";
	public static final String ID = "id";

}

