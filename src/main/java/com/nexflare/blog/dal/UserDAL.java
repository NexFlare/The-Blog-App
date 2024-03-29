package com.nexflare.blog.dal;

import com.nexflare.blog.exceptions.DataNotFoundException;
import com.nexflare.blog.pojo.User;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component("UserDAO")
public class UserDAL extends AbstractDAL<User, UUID> {
    @Override
    public User get(UUID id) throws DataNotFoundException {
            begin();
            Query q = getSession().createQuery("from User where id = :id ");
            q.setParameter("id", id);
            try{
                User user =(User) q.getSingleResult();
                commit();
                close();
                if(user == null) throw new DataNotFoundException("User not found");
                return user;
            } catch(NoResultException e) {
                rollback();
                throw new DataNotFoundException("User not found");
            }
    }

    @Override
    public List<User> _getAll() {
        String s = "FROM User";
        Query query = getSession().createQuery(s);
        List<User> list = query.getResultList();
        return list;
    }

    @Override
    public List<User> _getElementsByQuery(Map<String, Object> map) {
        return null;
    }


    @Override
    public User _getUniqueElementByQuery(Map<String, Object> map) throws DataNotFoundException {
        Query query = this.getQuery("User", map);
        try{
            User user = (User) query.getSingleResult();
            return user;
        } catch(NoResultException exception) {
            throw new DataNotFoundException(exception.getMessage());
        }
    }
}
