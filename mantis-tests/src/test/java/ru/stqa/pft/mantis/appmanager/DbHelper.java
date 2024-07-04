package ru.stqa.pft.mantis.appmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.stqa.pft.mantis.model.User;

import java.util.List;

public class DbHelper {
    private final ApplicationManager app;
    public SessionFactory sessionFactory;

    public DbHelper(ApplicationManager app) {
        this.app = app;
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        sessionFactory =
                new MetadataSources(registry)
                        .addAnnotatedClass(User.class)
                        .buildMetadata()
                        .buildSessionFactory();
    }

    public List<User> users() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<User> result = session.createSelectionQuery("from User", User.class).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}