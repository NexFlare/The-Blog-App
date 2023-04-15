package com.nexflare.testhiber.service.Blog;

import com.nexflare.testhiber.dao.AbstractDAO;
import com.nexflare.testhiber.enums.BlogStatus;
import com.nexflare.testhiber.enums.UserType;
import com.nexflare.testhiber.exceptions.AbstractException;
import com.nexflare.testhiber.pojo.Blog;
import com.nexflare.testhiber.pojo.User;
import com.nexflare.testhiber.requestModel.Blog.ActionBlogRequestObject;
import com.nexflare.testhiber.responseModel.BaseResponseModel;
import com.nexflare.testhiber.responseModel.Response;
import com.nexflare.testhiber.service.AuthenticatedBaseHandler;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public class ActionBlogService extends AuthenticatedBaseHandler<ActionBlogRequestObject> {

    private final AbstractDAO<Blog, UUID> blogDao;

    public ActionBlogService(AbstractDAO<User, UUID> userDao, HttpServletRequest request, AbstractDAO<Blog, UUID> blogDao) {
        super(userDao, request);
        this.blogDao = blogDao;
    }

    @Override
    protected Response handleRequest(ActionBlogRequestObject object) throws AbstractException {
        if(validateIsUserLoggedIn()) {
            User user = this.getUserFromSession();
            if(user.getUserType() != UserType.ADMIN) {
                return BaseResponseModel.builder().code(401).errorMessage("User not authorized").build();
            }
            Blog blog = this.blogDao.get(object.getBlogId());
            blog.setBlogStatus(object.isApproved() ? BlogStatus.APPROVED : BlogStatus.REJECTED);
            this.blogDao.update(blog);
            return BaseResponseModel.<Blog>builder().response(blog).code(200).build();
        } else {
           return BaseResponseModel.builder().code(401).errorMessage("User not authenticated").code(401).build();
        }
    }
}