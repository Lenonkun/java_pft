package ru.stqa.pft.addressbook.tests;

import jdk.jfr.Event;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.GroupData;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.System.out;

public class HbConnectionTest {
    private SessionFactory sessionFactory;
    @BeforeClass
    protected void setUp() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(ContactData.class)
                            .addAnnotatedClass(GroupData.class)
                            .buildMetadata()
                            .buildSessionFactory();
        }
        catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
    @Test
    public void testHbConnection(){
        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from ContactData where deprecated is null", ContactData.class)
                    .getResultList()
                    .forEach(contact -> {
                        out.println(contact);
//                        out.println(contact.getGroups());
                    });
        });

    }
}
