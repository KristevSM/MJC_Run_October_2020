package com.epam.esm.model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> firstName;
	public static volatile SingularAttribute<User, String> lastName;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> address;
	public static volatile SingularAttribute<User, LocalDate> dateOfBirth;
	public static volatile ListAttribute<User, Order> orders;
	public static volatile SingularAttribute<User, Long> id;
	public static volatile SingularAttribute<User, String> email;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PASSWORD = "password";
	public static final String ADDRESS = "address";
	public static final String DATE_OF_BIRTH = "dateOfBirth";
	public static final String ORDERS = "orders";
	public static final String ID = "id";
	public static final String EMAIL = "email";

}
