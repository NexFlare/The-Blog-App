package com.nexflare.testhiber.service.User;

import com.nexflare.testhiber.dao.AbstractDAO;
import com.nexflare.testhiber.exceptions.AbstractException;
import com.nexflare.testhiber.pojo.User;
import com.nexflare.testhiber.requestModel.GetByIdRequestObject;
import com.nexflare.testhiber.responseModel.BaseResponseModel;
import com.nexflare.testhiber.responseModel.Response;
import com.nexflare.testhiber.service.BaseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("GetUserByIDService")
public class GetUserByIDService extends BaseHandler<GetByIdRequestObject> {

    public GetUserByIDService(AbstractDAO<User, UUID> userDao, HttpServletRequest request) {
        super(userDao, request);
    }

    @Override
    protected Response handleRequest(GetByIdRequestObject object) throws AbstractException {
        User user = User.builder().id(object.getId()).build();
        user = this.userDao.get(user.getId());
        return BaseResponseModel.<User>builder().response(user).code(200).build();
    }
}