package top.ibase4j.core.filter;

import top.ibase4j.core.support.http.SessionUser;
import top.ibase4j.core.util.WebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * SESSION会话转存REQUEST
 * @author ShenHuaJie
 * @since 2018年7月22日 上午9:29:55
 */
public class SessionFilter implements Filter {
    private Logger logger = LogManager.getLogger();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("init SessionFilter.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        SessionUser sessionUser = (SessionUser)SecurityUtils.getSubject().getPrincipal();
        if (sessionUser != null) {
            WebUtil.saveCurrentUser((HttpServletRequest)request, sessionUser);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("destroy SessionFilter.");
    }
}
