package com.alibaba.jvm.sandbox.repater.plugin.reactornetty;

import com.alibaba.jvm.sandbox.api.event.Event;
//import com.alibaba.jvm.sandbox.api.listener.EventListener;
//import com.alibaba.jvm.sandbox.repeater.plugin.api.InvocationListener;
import com.alibaba.jvm.sandbox.repeater.plugin.api.InvocationProcessor;
import com.alibaba.jvm.sandbox.repeater.plugin.core.impl.AbstractInvokePluginAdapter;
import com.alibaba.jvm.sandbox.repeater.plugin.core.model.EnhanceModel;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.InvokeType;
import com.alibaba.jvm.sandbox.repeater.plugin.spi.InvokePlugin;
import com.google.common.collect.Lists;
import org.kohsuke.MetaInfServices;

import java.util.List;

/**
 * {@link HttpPlugin} http入口流量类型插件
 * <p>
 *
 * @author zhaoyb1990
 */
@MetaInfServices(InvokePlugin.class)
public class HttpPlugin extends AbstractInvokePluginAdapter {

    @Override
    protected List<EnhanceModel> getEnhanceModels() {
        // 拦截org.springframework.http.server.reactive.HttpHandler#handle(org.springframework.http.server.reactive.ServerHttpRequest, org.springframework.http.server.reactive.ServerHttpResponse)
        log.info(" start to get enhance model of webflux, type={}", getType());
        EnhanceModel.MethodPattern mp = EnhanceModel.MethodPattern.builder()
                .methodName("handle")
                .parameterType(new String[]{"org.springframework.http.server.reactive.ServerHttpRequest", "org.springframework.http.server.reactive.ServerHttpResponse"})
                .build();
        EnhanceModel em = EnhanceModel.builder()
                .classPattern("org.springframework.http.server.reactive.HttpHandler")
                .methodPatterns(new EnhanceModel.MethodPattern[]{mp})
                .watchTypes(Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS)
                .build();
        EnhanceModel em2 = EnhanceModel.builder()
                .classPattern("org.springframework.boot.web.reactive.context.WebManager.DelayedInitializationHttpHandler")
                .methodPatterns(EnhanceModel.MethodPattern.transform("handle"))
                .watchTypes(Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS)
                .build();
        return Lists.newArrayList(em,em2);
    }

    @Override
    protected InvocationProcessor getInvocationProcessor() {
        return new NettyReactorProcessor(getType());
//        // return null cause we override getEventListener
//        return null;
    }

//    @Override
//    protected EventListener getEventListener(InvocationListener listener) {
////        return new HttpStandaloneListener(getType(), isEntrance(), listener, getInvocationProcessor());
//
//    }

    @Override
    public InvokeType getType() {
        return InvokeType.HTTP;
    }

    @Override
    public String identity() {
        return "http-new";
    }

    @Override
    public boolean isEntrance() {
        return true;
    }
}
