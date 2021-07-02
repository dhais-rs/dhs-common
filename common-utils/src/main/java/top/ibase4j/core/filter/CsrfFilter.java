package top.ibase4j.core.filter;

import top.ibase4j.core.util.DataUtil;
import top.ibase4j.core.util.DateUtil;
import top.ibase4j.core.util.FileUtil;
import top.ibase4j.core.util.WebUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 跨站请求拦截
 * 
 * @author ShenHuaJie
 * @since 2018年7月27日 上午10:58:14
 */
public class CsrfFilter implements Filter {
	private Logger logger = LogManager.getLogger();

	/**
	 * 白名单
	 */
	private List<String> whiteUrls;

	@Override
	public void init(FilterConfig filterConfig) {
		logger.info("init CsrfFilter..");
		// 读取文件
		String path = CsrfFilter.class.getResource("/").getFile();
		whiteUrls = FileUtil.readFile(path + "white/csrfWhite.txt");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			// 获取请求url地址
			String url = req.getRequestURL().toString();
			String referurl = req.getHeader("Referer");
			if (isWhiteRequest(url, referurl)) {
				chain.doFilter(request, response);
			} else {
				// 记录跨站请求日志
				logger.warn("跨站请求---->>>{} || {} || {} || {}", url, referurl, WebUtil.getHost(req),
						DateUtil.getDateTime());
				WebUtil.write(response, 308, "错误的请求头信息");
				return;
			}
		} catch (Exception e) {
			logger.error("doFilter", e);
		}
	}

	/* 判断是否是白名单 */
	private boolean isWhiteRequest(String url, String referUrl) {
		if (url.contains("swagger")) {
			return true;
		}
		for (String urlTemp : whiteUrls) {
			if (url.endsWith(urlTemp)) {
				return true;
			}
		}
		if (DataUtil.isNotEmpty(referUrl)) {
			String refHost = referUrl.toLowerCase();
			for (String urlTemp : whiteUrls) {
				if (refHost.startsWith(urlTemp.toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		logger.info("destroy CsrfFilter.");
	}
}
