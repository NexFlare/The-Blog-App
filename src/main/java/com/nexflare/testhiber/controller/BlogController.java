package com.nexflare.testhiber.controller;


import com.nexflare.testhiber.dal.BlogDAL;
import com.nexflare.testhiber.dal.LikesDAL;
import com.nexflare.testhiber.dal.NotificationDAL;
import com.nexflare.testhiber.dal.UserDAL;
import com.nexflare.testhiber.enums.BlogStatus;
import com.nexflare.testhiber.enums.BlogType;
import com.nexflare.testhiber.mapper.Blog.BlogDOToGetBlogResponseModel;
import com.nexflare.testhiber.mapper.Blog.BlogDOToResponseBlogItemMapper;
import com.nexflare.testhiber.mapper.Blog.CreateBlogRequestToBlogMapper;
import com.nexflare.testhiber.mapper.Blog.UpdateBlogRequestToBlogMapper;
import com.nexflare.testhiber.requestModel.Blog.ActionBlogRequestObject;
import com.nexflare.testhiber.requestModel.Blog.CreateBlogRequestObject;
import com.nexflare.testhiber.requestModel.Blog.GetBlogRequestObject;
import com.nexflare.testhiber.requestModel.Blog.UpdateBlogRequestObject;
import com.nexflare.testhiber.requestModel.GetByIdRequestObject;
import com.nexflare.testhiber.responseModel.Response;
import com.nexflare.testhiber.service.BaseHandler;
import com.nexflare.testhiber.service.Blog.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://10.110.131.218:3000"}, allowCredentials = "true")
@RequestMapping("/api/v1/blog")
public class BlogController {

    @PostMapping("/")
    public Response addBlog(@RequestBody CreateBlogRequestObject blog, UserDAL userDAL, HttpServletRequest request, BlogDAL blogDao, CreateBlogRequestToBlogMapper mapper) {
        BaseHandler<CreateBlogRequestObject> createBlogService = new CreateBlogService(userDAL,request,blogDao,mapper);
        return createBlogService.handle(blog);
    }

    @GetMapping("/")
    public Response getBlogDetail(@RequestParam(required = false) UUID blogId,
                                  @RequestParam(required = false) BlogType type,
                                  @RequestParam(required = false) String location,
                                  @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                  UserDAL userDAL, HttpServletRequest request,
                                  BlogDAL blogDAL, BlogDOToResponseBlogItemMapper responseMapper) {
        GetBlogRequestObject requestObject = GetBlogRequestObject.builder()
                .type(type).blogId(blogId)
                .location(location)
                .page(pageNumber)
                .build();
        BaseHandler<GetBlogRequestObject> getBlogService = new GetBlogService(userDAL,request,blogDAL, responseMapper);
        return getBlogService.handle(requestObject);
    }


    @PutMapping("/updatestatus")
    public Response updateBlogStatus(@RequestBody ActionBlogRequestObject obj, UserDAL userDAL,
                                     HttpServletRequest request, BlogDAL blogDal, NotificationDAL notificationDAL) {
        BaseHandler<ActionBlogRequestObject> handler = new ActionBlogService(userDAL,request,blogDal, notificationDAL);
        return handler.handle(obj);
    }

    @PutMapping("/")
    public Response updateBlog(@RequestBody UpdateBlogRequestObject obj, UserDAL userDAL, HttpServletRequest reqeust, BlogDAL blogDAL, UpdateBlogRequestToBlogMapper mapper) {
        BaseHandler<UpdateBlogRequestObject> handler = new UpdateBlogService(userDAL, reqeust, blogDAL, mapper);
        return handler.handle(obj);
    }

    @DeleteMapping("/")
    public Response deleteBlog(@RequestBody GetByIdRequestObject obj, UserDAL userDAL, HttpServletRequest request, BlogDAL blogDAL, LikesDAL likesDAL) {
        BaseHandler<GetByIdRequestObject> handler = new DeleteBlogService(userDAL,request,blogDAL,likesDAL);
        return handler.handle(obj);
    }

    @GetMapping("/{id}")
    public Response getBlogDetail(@PathVariable UUID id, UserDAL userDAL, HttpServletRequest request, BlogDAL blogDAL, BlogDOToGetBlogResponseModel mapper) {
        BaseHandler<GetByIdRequestObject> handler = new GetBlogByIDService(userDAL, request, blogDAL, mapper);
        GetByIdRequestObject obj = new GetByIdRequestObject(id);
        return handler.handle(obj);
    }

    @GetMapping("/pending")
    public Response getPendingBlogs(UserDAL userDAL, HttpServletRequest request, BlogDAL blogDAL, BlogDOToResponseBlogItemMapper responseMapper) {
        BaseHandler<GetByIdRequestObject> handler = new GetPendingBlogs(userDAL,request,blogDAL, responseMapper);
        return handler.handle(null);
    }
}
