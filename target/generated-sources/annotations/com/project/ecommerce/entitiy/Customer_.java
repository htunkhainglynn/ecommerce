package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<User, WishList> wishList;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> address;
	public static volatile SingularAttribute<User, String> phoneNumber;
	public static volatile ListAttribute<User, Shipping> shipping;
	public static volatile ListAttribute<User, Role> roles;
	public static volatile ListAttribute<User, Payment> payment;
	public static volatile SingularAttribute<User, Integer> id;
	public static volatile SingularAttribute<User, String> username;
	public static volatile ListAttribute<User, Order> order;

	public static final String WISH_LIST = "wishList";
	public static final String PASSWORD = "password";
	public static final String ADDRESS = "address";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String SHIPPING = "shipping";
	public static final String ROLES = "roles";
	public static final String PAYMENT = "payment";
	public static final String ID = "id";
	public static final String USERNAME = "username";
	public static final String ORDER = "order";

}

