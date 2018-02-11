package com.zack.znjj.controller.backend;

import com.zack.znjj.common.restful.ServerResponse;
import com.zack.znjj.service.IMessagePushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/push/")
@RestController
public class MessagePushController {

    @Autowired
    IMessagePushService iMessagePushService;

    @RequestMapping(value = "/message/", method = RequestMethod.POST)
    public ServerResponse pushMessage(String message) {

        return iMessagePushService.pushMessage(message);
    }

}
