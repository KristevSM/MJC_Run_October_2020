package com.epam.esm.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tag.class)
public abstract class Tag_ {

	public static volatile ListAttribute<Tag, GiftCertificate> certificates;
	public static volatile SingularAttribute<Tag, String> name;
	public static volatile SingularAttribute<Tag, Long> id;

	public static final String CERTIFICATES = "certificates";
	public static final String NAME = "name";
	public static final String ID = "id";

}

