package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Category.class)
public abstract class Category_ {

	public static volatile ListAttribute<Category, Brand> brands;
	public static volatile SingularAttribute<Category, String> name;
	public static volatile SingularAttribute<Category, Long> id;
	public static volatile ListAttribute<Category, Product> products;

	public static final String BRANDS = "brands";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String PRODUCTS = "products";

}

