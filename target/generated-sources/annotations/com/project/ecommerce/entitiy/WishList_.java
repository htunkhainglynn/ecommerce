package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WishList.class)
public abstract class WishList_ {

	public static volatile SingularAttribute<WishList, Long> id;
	public static volatile SingularAttribute<WishList, User> customer;
	public static volatile ListAttribute<WishList, Product> products;

	public static final String ID = "id";
	public static final String CUSTOMER = "customer";
	public static final String PRODUCTS = "products";

}

