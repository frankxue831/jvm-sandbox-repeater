package com.alibaba.jvm.sandbox.repater.plugin.reactornetty;

import com.alibaba.jvm.sandbox.repeater.plugin.core.impl.api.DefaultInvocationProcessor;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.InvokeType;

public class NettyReactorProcessor extends DefaultInvocationProcessor {
    public NettyReactorProcessor(InvokeType type) {
        super(type);
    }
}
