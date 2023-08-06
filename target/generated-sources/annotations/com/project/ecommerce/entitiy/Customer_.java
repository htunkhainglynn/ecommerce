package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<Customer, WishList> wishList;
	public static volatile SingularAttribute<Customer, String> password;
	public static volatile SingularAttribute<Customer, String> address;
	public static volatile SingularAttribute<Customer, String> phoneNumber;
	public static volatile ListAttribute<Customer, Shipping> shipping;
	public static volatile ListAttribute<Customer, Role> roles;
	public static volatile ListAttribute<Customer, Payment> payment;
	public static volatile SingularAttribute<Customer, Integer> id;
	public static volatile SingularAttribute<Customer, String> username;
	public static volatile ListAttribute<Customer, Order> order;

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

