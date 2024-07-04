package ru.stqa.pft.addressbook.appmanager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;
import java.util.List;

public class DbHelper {
    public SessionFactory sessionFactory;

    public DbHelper() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(ContactData.class)
                        .addAnnotatedClass(GroupData.class)
                        .buildMetadata()
                        .buildSessionFactory();
    }

    public Groups groups() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<GroupData> result = session.createSelectionQuery("from GroupData", GroupData.class).list();
        session.getTransaction().commit();
        session.close();
        return new Groups(result);
    }

    public Contacts contacts() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<ContactData> result = session.createSelectionQuery("from ContactData where deprecated is null", ContactData.class).list();
        session.getTransaction().commit();
        session.close();
        return new Contacts(result);
    }
}
