package com.zack.znjj.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.service.IMessagePushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessagePushServiceImpl implements IMessagePushService {

    public ServerResponse pushMessage(String message) {
        PushPayload pushPayload = createPushPayload(message);
        if (sendPushPayload(pushPayload).isSuccess()) {
            return ServerResponse.createBySuccess("发送成功");
        }
        return ServerResponse.createBySuccess("发送失败");
    }

    private PushPayload createPushPayload(String message) {

        return PushPayload.alertAll(message);
    }

    private ServerResponse sendPushPayload(PushPayload pushPayload) {
        JPushClient jpushClient = new JPushClient("391ca2878d54c4d39a596f77 ", "94cbfb012a8f385932416b71", null, ClientConfig.getInstance());
        try {
            PushResult result = jpushClient.sendPush(pushPayload);
            log.info("Got result - " + result);
            return ServerResponse.createBySuccess("发送成功");

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);
            return ServerResponse.createByErrorMessage("发送失败");

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            return ServerResponse.createByErrorMessage("发送失败");
        }
    }
}
