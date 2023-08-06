package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile ListAttribute<Product, OrderItem> orderItem;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile ListAttribute<Product, Color> colors;
	public static volatile SingularAttribute<Product, WishList> wishList;
	public static volatile ListAttribute<Product, Size> sizes;
	public static volatile ListAttribute<Product, Review> reviews;
	public static volatile SingularAttribute<Product, Double> price;
	public static volatile SingularAttribute<Product, String> imageUrl;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, Integer> stock;
	public static volatile SingularAttribute<Product, Category> category;
	public static volatile SingularAttribute<Product, Brand> brand;

	public static final String ORDER_ITEM = "orderItem";
	public static final String DESCRIPTION = "description";
	public static final String COLORS = "colors";
	public static final String WISH_LIST = "wishList";
	public static final String SIZES = "sizes";
	public static final String REVIEWS = "reviews";
	public static final String PRICE = "price";
	public static final String IMAGE_URL = "imageUrl";
	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String STOCK = "stock";
	public static final String CATEGORY = "category";
	public static final String BRAND = "brand";

}

