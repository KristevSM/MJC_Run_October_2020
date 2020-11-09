package com.epam.esm.dao;

import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.hibernate.Session;
import org.junit.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserDaoImplTest {

    private Session session;

    private UserDao userDao;

    public UserDaoImplTest() {
        userDao = new UserDaoImpl();
    }


    @Test
    public void shouldSaveUsers() {
        session = HibernateAnnotationUtil.getSessionFactory().openSession();
        session.beginTransaction();

        User user1 = User.builder()
                .firstName("First name")
                .lastName("Last Name")
                .email("email@test.com")
                .password("qwerty")
                .address("Some address")
                .dateOfBirth(LocalDate.of(1990, 10, 12))
                .build();
        session.persist(user1);

        User user2 = User.builder()
                .firstName("First name")
                .lastName("Last Name")
                .email("email@test.com")
                .password("qwerty")
                .address("Some address")
                .dateOfBirth(LocalDate.of(1990, 10, 12))
                .build();
        session.persist(user2);

        List<Order> orders = new ArrayList<>();
        Order order1 = Order.builder().user(user1).orderDate(ZonedDateTime.now()).build();
        Order order2 = Order.builder().user(user1).orderDate(ZonedDateTime.now()).build();
        orders.add(order1);
        orders.add(order2);
        user1.setOrders(orders);
        user1.setOrders(orders);

        session.update(user1);

        User userFromDb = (User) session.get(User.class, 3L);
        System.out.println(userFromDb);
        assertNotNull(userFromDb);

        List<Order> orderList = userFromDb.getOrders();
        assertEquals(2, orderList.size());
        System.out.println(orderList);
        session.getTransaction().commit();
        session.close();

        session = HibernateAnnotationUtil.getSessionFactory().openSession();
        List<User> userList = userDao.getAllUsers();
        for (User u : userList) {
            System.out.println(u);
        }
        assertEquals(4L, userList.size());
        session.close();


    }

    @Test
    public void shouldGetUsersList() {

        List<User> userList = userDao.getAllUsers();
        for (User u : userList){
            System.out.println(u);
        }

        assertEquals(2L, userList.size());

    }

    @AfterClass
    public static void afterTests() {
        HibernateAnnotationUtil.shutdown();
    }
}
