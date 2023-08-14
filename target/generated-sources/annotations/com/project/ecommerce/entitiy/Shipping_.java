package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Shipping.class)
public abstract class Shipping_ {

	public static volatile SingularAttribute<Shipping, LocalDate> date;
	public static volatile SingularAttribute<Shipping, String> zipCode;
	public static volatile SingularAttribute<Shipping, String> country;
	public static volatile SingularAttribute<Shipping, String> address;
	public static volatile SingularAttribute<Shipping, String> city;
	public static volatile ListAttribute<Shipping, Order> orders;
	public static volatile SingularAttribute<Shipping, Long> id;
	public static volatile SingularAttribute<Shipping, String> state;
	public static volatile SingularAttribute<Shipping, Status> status;
	public static volatile SingularAttribute<Shipping, User> customer;

	public static final String DATE = "date";
	public static final String ZIP_CODE = "zipCode";
	public static final String COUNTRY = "country";
	public static final String ADDRESS = "address";
	public static final String CITY = "city";
	public static final String ORDERS = "orders";
	public static final String ID = "id";
	public static final String STATE = "state";
	public static final String STATUS = "status";
	public static final String CUSTOMER = "customer";

}

