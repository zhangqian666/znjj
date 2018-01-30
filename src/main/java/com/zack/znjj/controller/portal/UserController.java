package com.zack.znjj.controller.portal;


import com.zack.znjj.common.restful.Const;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.User;
import com.zack.znjj.service.IRedisService;
import com.zack.znjj.service.IUserService;
import com.zack.znjj.util.JWTUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

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
     * @param session
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        log.info("login");
        ServerResponse<User> userServerResponse = iUserService.login(username, password);
        if (userServerResponse.isSuccess()) {
            try {
                String jwtToken = JWTUtil.createJWT(userServerResponse.getData().getId(), userServerResponse.getData().getUsername());
                iRedisService.hmSet("user", userServerResponse.getData().getId().toString(), jwtToken);
                userServerResponse.getData().setToken(jwtToken);
            } catch (Exception e) {
                e.printStackTrace();
                return ServerResponse.createByErrorMessage("token生成失败，检查redis服务器" + e.getMessage());
            }
        }
        return userServerResponse;
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }


    @RequestMapping(value = "available", method = RequestMethod.POST)
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }


    @RequestMapping(value = "self/info", method = RequestMethod.POST)
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(request);
        if (userServerResponse.isSuccess()) {
            return userServerResponse;
        }
        return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
    }


    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }


    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }


    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }


    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }


    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
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

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    public ServerResponse<User> get_information(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "未登录,需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }


}
