package com.zack.znjj.controller.portal;


import com.zack.znjj.common.restful.Const;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.User;
import com.zack.znjj.service.ICartService;
import com.zack.znjj.service.IUserService;
import com.zack.znjj.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by geely
 */
@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @Autowired
    private IUserService iUserService;

    @RequestMapping("list/")
    public ServerResponse<CartVo> list(HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.list(userServerResponse.getData().getId());
    }

    @RequestMapping("add/")
    public ServerResponse<CartVo> add(HttpServletRequest httpServletRequest, Integer count, Integer productId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.add(userServerResponse.getData().getId(), productId, count);
    }


    @RequestMapping("update/")
    public ServerResponse<CartVo> update(HttpServletRequest httpServletRequest, Integer count, Integer productId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.update(userServerResponse.getData().getId(), productId, count);
    }

    @RequestMapping("delete/product/")
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest httpServletRequest, String productIds) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.deleteProduct(userServerResponse.getData().getId(), productIds);
    }


    @RequestMapping("select/all/")
    public ServerResponse<CartVo> selectAll(HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.selectOrUnSelect(userServerResponse.getData().getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping("select/un/all/")
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.selectOrUnSelect(userServerResponse.getData().getId(), null, Const.Cart.UN_CHECKED);
    }


    @RequestMapping("select/")
    public ServerResponse<CartVo> select(HttpServletRequest httpServletRequest, Integer productId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.selectOrUnSelect(userServerResponse.getData().getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("select/un")
    public ServerResponse<CartVo> unSelect(HttpServletRequest httpServletRequest, Integer productId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.selectOrUnSelect(userServerResponse.getData().getId(), productId, Const.Cart.UN_CHECKED);
    }


    @RequestMapping("get/cart/productcount/")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iCartService.getCartProductCount(userServerResponse.getData().getId());
    }


    //全选
    //全反选

    //单独选
    //单独反选

    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那么数量就是10.


}
