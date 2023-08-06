package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Review.class)
public abstract class Review_ {

	public static volatile SingularAttribute<Review, Product> product;
	public static volatile SingularAttribute<Review, Integer> rating;
	public static volatile SingularAttribute<Review, Long> id;
	public static volatile SingularAttribute<Review, String> content;

	public static final String PRODUCT = "product";
	public static final String RATING = "rating";
	public static final String ID = "id";
	public static final String CONTENT = "content";

}

