package com.zack.znjj.service;

import com.zack.znjj.common.restful.ServerResponse;

public interface IMessagePushService {
    ServerResponse pushMessage(String message);
}
