package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Brand.class)
public abstract class Brand_ {

	public static volatile SingularAttribute<Brand, String> name;
	public static volatile SingularAttribute<Brand, Long> id;
	public static volatile ListAttribute<Brand, Category> categories;
	public static volatile ListAttribute<Brand, Product> products;

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String CATEGORIES = "categories";
	public static final String PRODUCTS = "products";

}

