package com.nexflare.testhiber.service.Comment;

import com.nexflare.testhiber.dao.AbstractDAO;
import com.nexflare.testhiber.exceptions.AbstractException;
import com.nexflare.testhiber.mapper.IRequestToDOMapper;
import com.nexflare.testhiber.pojo.Comments;
import com.nexflare.testhiber.pojo.User;
import com.nexflare.testhiber.requestModel.Comment.CreateCommentRequestObject;
import com.nexflare.testhiber.responseModel.BaseResponseModel;
import com.nexflare.testhiber.responseModel.Response;
import com.nexflare.testhiber.service.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CreateCommentService extends BaseHandler<CreateCommentRequestObject> {

    IRequestToDOMapper<CreateCommentRequestObject, Comments> commentMapper;
    AbstractDAO<Comments, UUID> commentDAO;

    public CreateCommentService(AbstractDAO<User, UUID> userDao, HttpServletRequest request,
                                IRequestToDOMapper<CreateCommentRequestObject, Comments> commentMapper,
                                AbstractDAO<Comments, UUID> commentDAO) {
        super(userDao, request);
        this.commentMapper = commentMapper;
        this.commentDAO = commentDAO;
    }

    @Override
    protected Response handleRequest(CreateCommentRequestObject object) throws AbstractException {
        if(this.validateIsUserLoggedIn()) {
            Comments commentObj = commentMapper.map(object);
            this.commentDAO.add(commentObj);
            return BaseResponseModel.<Comments>builder().response(commentObj).build();
        }
        return BaseResponseModel.builder().code(401).errorMessage("Not authenticated").build();

    }
}
