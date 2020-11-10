package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
        String sql = "SELECT * FROM GIFT_CERTIFICATE where CERTIFICATE_ID = 15";
        GiftCertificate certificate = (GiftCertificate) session.createNativeQuery(sql).addEntity(GiftCertificate.class).getSingleResult();
        order1.setGiftCertificate(certificate);
        session.update(order1);

        session.close();

        session = HibernateAnnotationUtil.getSessionFactory().openSession();

        String sql3 = "SELECT * FROM ORDERS where ORDER_ID = 1";
        Order order = (Order) session.createNativeQuery(sql3).addEntity(Order.class).getSingleResult();

        List<User> userList = userDao.getAllUsers();
        for (User u : userList) {
            System.out.println(u);
        }
//        System.out.println(userList.get(3).getOrders().get(0).getGiftCertificate());
//        assertEquals(4L, userList.size());
//        session.close();


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
