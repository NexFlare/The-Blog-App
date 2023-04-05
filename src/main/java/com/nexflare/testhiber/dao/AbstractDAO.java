package com.nexflare.testhiber.dao;

import com.nexflare.testhiber.pojo.AbstractDO;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDAO<K> implements IDataRetrieval<K>{

    private static final Logger log = Logger.getAnonymousLogger();
    private static final ThreadLocal<Session> sessionThread = new ThreadLocal<>();
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public static Session getSession() {
        if(sessionThread.get() == null) {
            Session s =  AbstractDAO.sessionFactory.openSession();
            sessionThread.set(s);
        }
        return sessionThread.get();
    }

    protected void rollback() {
        try {
            getSession().getTransaction().rollback();
        } catch (HibernateException e) {
            log.log(Level.WARNING, "Cannot rollback", e);
        }
        try {
            getSession().close();
        } catch (HibernateException e) {
            log.log(Level.WARNING, "Cannot close", e);
        }
        AbstractDAO.sessionThread.set(null);
    }


    protected void begin() {
        getSession().beginTransaction();
    }

    protected void commit() {
        getSession().getTransaction().commit();
    }

    public static void close() {
        getSession().close();
        AbstractDAO.sessionThread.set(null);
    }
    @Override
    public K get(K obj) {
        return null;
    }

    @Override
    public List<K> getAll() {
        return null;
    }

    @Override
    public void update(K obj) {
        try {
            //save category to the database
            begin();
            getSession().merge(obj);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            log.log(Level.SEVERE, e.getMessage());
            System.out.println("Error has occurred update" + e);
            //throw new AdException("Could not create the category", e);
//            throw new CategoryException("Exception while deleting category: " + e.getMessage());
        }
    }

    @Override
    public void delete(K obj) {
        try {
            //save category to the database
            begin();
            getSession().remove(obj);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            //throw new AdException("Could not create the category", e);
//            throw new CategoryException("Exception while deleting category: " + e.getMessage());
        }
    }

    @Override
    public void add(K obj) {
        try {
            //save category to the database
            begin();
            getSession().save(obj);
            commit();
            close();
        } catch (HibernateException e) {
            rollback();
            log.log(Level.SEVERE, e.getMessage());
            System.out.println("Error has occurred " + e);
            //throw new AdException("Could not create the category", e);
//            throw new CategoryException("Exception while deleting category: " + e.getMessage());
        }
    }

    public abstract List<K> getElementByQuery(String property, String value);
}