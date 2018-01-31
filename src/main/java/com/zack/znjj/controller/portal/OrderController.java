package com.zack.znjj.controller.portal;


import com.zack.znjj.common.restful.Const;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.model.User;
import com.zack.znjj.service.IOrderService;
import com.zack.znjj.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/order/")
public class OrderController {


    @Autowired
    private IOrderService iOrderService;

    @Autowired
    private IUserService iUserService;


    @RequestMapping("create/")
    public ServerResponse create(HttpServletRequest httpServletRequest, Integer shippingId) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iOrderService.createOrder(userServerResponse.getData().getId(), shippingId);
    }


    @RequestMapping("cancel/")
    public ServerResponse cancel(HttpServletRequest httpServletRequest, Long orderNo) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iOrderService.cancel(userServerResponse.getData().getId(), orderNo);
    }


    @RequestMapping("get/order_cart_product/")
    public ServerResponse getOrderCartProduct(HttpServletRequest httpServletRequest) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iOrderService.getOrderCartProduct(userServerResponse.getData().getId());
    }


    @RequestMapping("detail/")
    public ServerResponse detail(HttpServletRequest httpServletRequest, Long orderNo) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iOrderService.getOrderDetail(userServerResponse.getData().getId(), orderNo);
    }

    @RequestMapping("list/")
    public ServerResponse list(HttpServletRequest httpServletRequest, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        return iOrderService.getOrderList(userServerResponse.getData().getId(), pageNum, pageSize);
    }


    @RequestMapping("pay/")
    public ServerResponse pay(HttpServletRequest httpServletRequest, Long orderNo, HttpServletRequest request) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, userServerResponse.getData().getId(), path);
    }

//    @RequestMapping("alipay_callback.do")
//
//    public Object alipayCallback(HttpServletRequest request){
//        Map<String,String> params = Maps.newHashMap();
//
//        Map requestParams = request.getParameterMap();
//        for(Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
//            String name = (String)iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for(int i = 0 ; i <values.length;i++){
//
//                valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
//            }
//            params.put(name,valueStr);
//        }
//        logger.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
//
//        //非常重要,验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
//
//        params.remove("sign_type");
//        try {
//            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
//
//            if(!alipayRSACheckedV2){
//                return ServerResponse.createByErrorMessage("非法请求,验证不通过,再恶意请求我就报警找网警了");
//            }
//        } catch (AlipayApiException e) {
//            logger.error("支付宝验证回调异常",e);
//        }
//
//        //todo 验证各种数据
//
//
//        //
//        ServerResponse serverResponse = iOrderService.aliCallback(params);
//        if(serverResponse.isSuccess()){
//            return Const.AlipayCallback.RESPONSE_SUCCESS;
//        }
//        return Const.AlipayCallback.RESPONSE_FAILED;
//    }


    @RequestMapping("query/order_pay_status/")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest httpServletRequest, Long orderNo) {
        ServerResponse<User> userServerResponse = iUserService.parseRequest(httpServletRequest);

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(userServerResponse.getData().getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


}
