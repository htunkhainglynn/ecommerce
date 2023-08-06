package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrderItem.class)
public abstract class OrderItem_ {

	public static volatile SingularAttribute<OrderItem, Product> product;
	public static volatile SingularAttribute<OrderItem, Integer> quantity;
	public static volatile SingularAttribute<OrderItem, Double> totalPrice;
	public static volatile SingularAttribute<OrderItem, OrderItemId> id;
	public static volatile SingularAttribute<OrderItem, LocalDate> orderDate;
	public static volatile SingularAttribute<OrderItem, Order> order;

	public static final String PRODUCT = "product";
	public static final String QUANTITY = "quantity";
	public static final String TOTAL_PRICE = "totalPrice";
	public static final String ID = "id";
	public static final String ORDER_DATE = "orderDate";
	public static final String ORDER = "order";

}

