package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.ListAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Order.class)
public abstract class Order_ {

	public static volatile SingularAttribute<Order, Shipping> shipping;
	public static volatile SingularAttribute<Order, Long> orderId;
	public static volatile SingularAttribute<Order, Double> totalPrice;
	public static volatile SingularAttribute<Order, Payment> payment;
	public static volatile SingularAttribute<Order, LocalDate> orderDate;
	public static volatile ListAttribute<Order, OrderItem> orderItems;
	public static volatile SingularAttribute<Order, Customer> customer;

	public static final String SHIPPING = "shipping";
	public static final String ORDER_ID = "orderId";
	public static final String TOTAL_PRICE = "totalPrice";
	public static final String PAYMENT = "payment";
	public static final String ORDER_DATE = "orderDate";
	public static final String ORDER_ITEMS = "orderItems";
	public static final String CUSTOMER = "customer";

}

