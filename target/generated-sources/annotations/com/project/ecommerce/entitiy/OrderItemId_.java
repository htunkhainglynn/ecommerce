package com.project.ecommerce.entitiy;

import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import javax.annotation.processing.Generated;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(OrderItemId.class)
public abstract class OrderItemId_ {

	public static volatile SingularAttribute<OrderItemId, Long> id;
	public static volatile SingularAttribute<OrderItemId, Long> order_id;

	public static final String ID = "id";
	public static final String ORDER_ID = "order_id";

}

