package com.alibaba.jvm.sandbox.repater.plugin.http;

import com.alibaba.jvm.sandbox.repeater.plugin.domain.HttpInvocation;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.Invocation;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.RecordModel;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.RepeatContext;
import com.alibaba.jvm.sandbox.repeater.plugin.domain.RepeatMeta;
import com.alibaba.jvm.sandbox.repeater.plugin.exception.RepeatException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpRepeaterTest {

    @Test
    public void testExecuteRepeat() throws Exception {
        // Create a RepeatContext object with a RecordModel and HttpInvocation
        RepeatMeta meta = new RepeatMeta();
        RecordModel recordModel = new RecordModel();
        String traceId = "my-trace-id";
        RepeatContext context = new RepeatContext(meta, recordModel, traceId);
        HttpInvocation invocation = new HttpInvocation();
        invocation.setPort(8080);
        invocation.setRequestURI("/test");
        invocation.setMethod("GET");

        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        invocation.setHeaders(headers);
        String[] values = {"value1"};
        invocation.setParamsMap(Collections.singletonMap("param1", values));
        invocation.setBody(null);
        recordModel.setEntranceInvocation(invocation);
        context.setRecordModel(recordModel);


        // Create a new HttpRepeater object and call executeRepeat on it
        HttpRepeater repeater = new HttpRepeater();
        Object result = repeater.executeRepeat(context);


        // Verify that the result is correct
        String expect = "Invoke occurred exception, request=Request{method=GET, url=http://127.0.0.1:8080/test?param1=value1, tags={}};message=Failed to connect to /127.0.0.1:8080";
        Assertions.assertEquals(expect, result);
    }

    @Test
    public void testExecuteRepeatWithWrongInvocationType() {
        // Create a RepeatContext object with a RecordModel and a non-HttpInvocation invocation
        RepeatMeta meta = new RepeatMeta();
        RecordModel recordModel = new RecordModel();
        String traceId = "my-trace-id";
        RepeatContext context = new RepeatContext(meta, recordModel, traceId);
        recordModel.setEntranceInvocation(new Invocation());
        context.setRecordModel(recordModel);

        // Create a new HttpRepeater object and call executeRepeat on it
        HttpRepeater repeater = new HttpRepeater();

        // Verify that executeRepeat throws a RepeatException
        Assertions.assertThrows(RepeatException.class, () -> repeater.executeRepeat(context));
    }

}