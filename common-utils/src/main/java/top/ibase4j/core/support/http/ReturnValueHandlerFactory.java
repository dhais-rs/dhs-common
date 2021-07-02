package top.ibase4j.core.support.http;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * @author ShenHuaJie
 * @since 2018年8月29日 下午2:05:29
 */
public class ReturnValueHandlerFactory implements InitializingBean {
    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        adapter.setReturnValueHandlers(
            decorateHandler(new ArrayList<HandlerMethodReturnValueHandler>(adapter.getReturnValueHandlers())));
    }

    private List<HandlerMethodReturnValueHandler> decorateHandler(
        List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                handlers.set(handlers.indexOf(handler), new ReturnValueHandler(handler));
                break;
            }
        }
        return handlers;
    }
}
