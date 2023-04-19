package com.nexflare.testhiber.dal;

import com.nexflare.testhiber.exceptions.DataNotFoundException;
import com.nexflare.testhiber.pojo.Blog;
import jakarta.persistence.NoResultException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequestScope
public class BlogDAL extends AbstractDAL<Blog, UUID> {

    @Override
    public Blog get(UUID id) throws DataNotFoundException {
        begin();
        Query q = getSession().createQuery("from Blog where blogId = :id ");
        q.setParameter("id", id);
        try{
            Blog blog =(Blog) q.getSingleResult();
            commit();
            return blog;
        } catch (NoResultException exp) {
            throw new DataNotFoundException("No result found");
        }

    }

    @Override
    public List<Blog> _getAll() throws DataNotFoundException {
        String s = "FROM Blog";
        Query query = getSession().createQuery(s);
        List<Blog> list = query.list();
        return list;
    }

    @Override
    public List<Blog> getElementByQuery(String property, String value) {
        return null;
    }

    @Override
    public List<Blog> _getElementsByQuery(Map<String, Object> map) {
        Query q = getQuery("Blog", map);
        List<Blog> blogs = q.getResultList();
        return blogs;
    }

    @Override
    public Blog _getUniqueElementByQuery(Map<String, Object> map) {
        Query q = getQuery("Blog", map);
        Blog blog = (Blog) q.getSingleResult();
        return blog;
    }
}