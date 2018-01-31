package com.zack.znjj.controller.portal;


import com.zack.znjj.common.restful.Const;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.User;
import com.zack.znjj.service.IRedisService;
import com.zack.znjj.service.IUserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */
@RestController
@RequestMapping("/user/")
@Log4j
public class UserController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IRedisService iRedisService;

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "login/", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password) {

        return iUserService.login(username, password);
    }

    @RequestMapping(value = "logout/", method = RequestMethod.POST)
    public ServerResponse<User> logout(HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        if (userServerResponse.isSuccess()) {
            iRedisService.hmDel("user", userServerResponse.getData().getId().toString());
        }
        return ServerResponse.createBySuccessMessage("登出成功");
    }

    @RequestMapping(value = "register/", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }


    /**
     * @param str
     * @param type username 用户名  email  邮箱
     * @return
     */
    @RequestMapping(value = "available/", method = RequestMethod.POST)
    public ServerResponse<String> available(String str, String type) {
        return iUserService.available(str, type);
    }


    @RequestMapping(value = "self/info/", method = RequestMethod.POST)
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(request);
        userServerResponse.getData().setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return userServerResponse;
    }


    @RequestMapping(value = "forget/question/", method = RequestMethod.POST)
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }


    @RequestMapping(value = "forget/check/answer/", method = RequestMethod.POST)
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }


    @RequestMapping(value = "forget/reset/password/", method = RequestMethod.POST)
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }


    @RequestMapping(value = "/reset/password/", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(HttpServletRequest httpServletRequest, String passwordOld, String passwordNew) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        User user = userServerResponse.getData();
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }


    @RequestMapping(value = "/update/information/", method = RequestMethod.POST)
    public ServerResponse<User> update_information(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "get/information/", method = RequestMethod.POST)
    public ServerResponse<User> get_information(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }


}
