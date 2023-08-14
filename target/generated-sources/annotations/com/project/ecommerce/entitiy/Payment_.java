package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Payment.class)
public abstract class Payment_ {

	public static volatile SingularAttribute<Payment, LocalDate> date;
	public static volatile SingularAttribute<Payment, Double> amount;
	public static volatile SingularAttribute<Payment, String> method;
	public static volatile ListAttribute<Payment, Order> orders;
	public static volatile SingularAttribute<Payment, Long> id;
	public static volatile SingularAttribute<Payment, User> customer;

	public static final String DATE = "date";
	public static final String AMOUNT = "amount";
	public static final String METHOD = "method";
	public static final String ORDERS = "orders";
	public static final String ID = "id";
	public static final String CUSTOMER = "customer";

}

