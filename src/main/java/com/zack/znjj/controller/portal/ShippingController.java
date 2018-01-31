package com.zack.znjj.controller.portal;

import com.github.pagehelper.PageInfo;

import com.zack.znjj.common.restful.Const;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.Shipping;
import com.zack.znjj.model.User;
import com.zack.znjj.service.IShippingService;
import com.zack.znjj.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */

@RestController
@RequestMapping("/shipping/")
public class ShippingController {


    @Autowired
    private IShippingService iShippingService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping("add/")
    public ServerResponse add(HttpServletRequest httpServletRequest, Shipping shipping) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iShippingService.add(userServerResponse.getData().getId(), shipping);
    }


    @RequestMapping("del/")
    public ServerResponse del(HttpServletRequest httpServletRequest, Integer shippingId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iShippingService.del(userServerResponse.getData().getId(), shippingId);
    }

    @RequestMapping("update/")
    public ServerResponse update(HttpServletRequest httpServletRequest, Shipping shipping) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iShippingService.update(userServerResponse.getData().getId(), shipping);
    }


    @RequestMapping("select/")
    public ServerResponse<Shipping> select(HttpServletRequest httpServletRequest, Integer shippingId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iShippingService.select(userServerResponse.getData().getId(), shippingId);
    }


    @RequestMapping("list/")
    public ServerResponse<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iShippingService.list(userServerResponse.getData().getId(), pageNum, pageSize);
    }


}
