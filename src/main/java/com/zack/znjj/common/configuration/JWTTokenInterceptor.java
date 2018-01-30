package com.zack.znjj.common.configuration;

import com.google.common.collect.Maps;
import com.zack.znjj.common.restful.ResponseCode;
import com.zack.znjj.service.IRedisService;
import com.zack.znjj.util.JWTUtil;
import com.zack.znjj.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class JWTTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private IRedisService iRedisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数,具体的参数key以及value是什么，我们打印日志
        StringBuffer params = addParams(request);
        if (StringUtils.equals(methodName, "login") ||
                StringUtils.equals(methodName, "register")) {
            log.info("权限拦截器拦截到请求,className:{},methodName:{}", className, methodName);
            //如果是拦截到登录请求，不打印参数，因为参数里面有密码，全部会打印到日志中，防止日志泄露
            return true;
        }
        String jwtToken = null;
        String redisToken = null;
        try {
            jwtToken = request.getHeader("token");
            Integer uid = (Integer) JWTUtil.parseJWT(jwtToken).get("uid");
            redisToken = (String) iRedisService.get(uid + "");
        } catch (Exception e) {
            e.printStackTrace();
            resultCodeMsg(response, ResponseCode.ERROR.getCode(), "token错误");
            return false;
        }
        if (StringUtils.equals(redisToken, jwtToken)) {
            return true;
        }
        resultCodeMsg(response, ResponseCode.ERROR.getCode(), "未登录");
        return false;
    }

    /**
     * 添加输入的参数
     *
     * @param request
     */
    private StringBuffer addParams(HttpServletRequest request) {
        StringBuffer requestParamBuffer = new StringBuffer();
        Map paramMap = request.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();

            String mapValue = StringUtils.EMPTY;

            //request这个参数的map，里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if (obj instanceof String[]) {
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }
        return requestParamBuffer;
    }

    /**
     * 返回输入的json数据
     *
     * @param response
     * @param code
     * @param msg
     * @throws IOException
     */
    private void resultCodeMsg(HttpServletResponse response, Integer code, String msg) throws IOException {
        Map resultMap = Maps.newHashMap();
        resultMap.put("code", code);
        resultMap.put("msg", msg);

        //返回false.即不会调用controller里的方法
        response.reset();//geelynote 这里要添加reset，否则报异常 getWriter() has already been called for this response.
        response.setCharacterEncoding("UTF-8");//geelynote 这里要设置编码，否则会乱码
        response.setContentType("application/json;charset=UTF-8");//geelynote 这里要设置返回值的类型，因为全部是json接口。
        PrintWriter out = response.getWriter();
        out.print(JsonUtil.getJsonFromObject(resultMap));
        out.flush();
        out.close();//geelynote 这里要关闭
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
