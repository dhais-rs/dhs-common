package top.ibase4j.core;

/**
 * 常量表
 *
 * @author ShenHuaJie
 * @version $Id: Constants.java, v 0.1 2014-2-28 上午11:18:28 ShenHuaJie Exp $
 */
public interface Constants {
	/**
	 * 异常信息统一头信息<br>
	 * 非常遗憾的通知您,程序发生了异常
	 */
	static final String EXCEPTION_HEAD = "OH,MY GOD! SOME ERRORS OCCURED! AS FOLLOWS :";
	/** 操作名称 */
	static final String OPERATION_NAME = "OPERATION_NAME";
	/** 客户端语言 */
	static final String USERLANGUAGE = "userLanguage";
	/** 客户端主题 */
	static final String WEBTHEME = "webTheme";
	/** 当前用户 */
	static final String CURRENT_USER = "CURRENT_USER";
	/** 客户端信息 */
	static final String USER_AGENT = "USER-AGENT";
	/** 客户端信息 */
	static final String USER_IP = "USER_IP";
	/** 登录地址 */
	static final String LOGIN_URL = "/login.html";
	/** 缓存命名空间 */
	static final String CACHE_NAMESPACE = "iBase4J:";
	/** 缓存命名空间 */
	static final String SYSTEM_CACHE_NAMESPACE = "S:iBase4J:";
	/** 缓存命名空间 */
	static final String CACHE_NAMESPACE_LOCK = "L:iBase4J:";
	/** 上次请求地址 */
	static final String PREREQUEST = CACHE_NAMESPACE + "PREREQUEST";
	/** 上次请求时间 */
	static final String PREREQUEST_TIME = CACHE_NAMESPACE + "PREREQUEST_TIME";
	/** 非法请求次数 */
	static final String MALICIOUS_REQUEST_TIMES = CACHE_NAMESPACE + "MALICIOUS_REQUEST_TIMES";
	/** 在线用户数量 */
	static final String ALLUSER_NUMBER = SYSTEM_CACHE_NAMESPACE + "ALLUSER_NUMBER";
	/** TOKEN */
	static final String TOKEN_KEY = SYSTEM_CACHE_NAMESPACE + "TOKEN_KEY:";
	/** shiro cache */
	static final String REDIS_SHIRO_CACHE = SYSTEM_CACHE_NAMESPACE + "SHIRO-CACHE:";
	/** SESSION */
	static final String REDIS_SHIRO_SESSION = SYSTEM_CACHE_NAMESPACE + "SHIRO-SESSION:";
	/** CACHE */
	static final String MYBATIS_CACHE = "D:iBase4J:MYBATIS:";
	/** 默认数据库密码加密key */
	static final String DB_KEY = "90139119";
	/** 临时目录 */
	static final String TEMP_DIR = "/temp/";
	/** 请求报文体 */
	static final String REQUEST_BODY = "iBase4J.requestBody";

	/** 日志表状态 */
	interface JobState {
		/**
		 * 日志表状态，初始状态，插入
		 */
		static final String INIT_STATS = "I";
		/**
		 * 日志表状态，成功
		 */
		static final String SUCCESS_STATS = "S";
		/**
		 * 日志表状态，失败
		 */
		static final String ERROR_STATS = "E";
		/**
		 * 日志表状态，未执行
		 */
		static final String UN_STATS = "N";
	}

	/** 短信验证码类型 */
	public interface MsgChkType {
		/** 注册 */
		static final String REGISTER = CACHE_NAMESPACE + "REGISTER:";
		/** 登录 */
		static final String LOGIN = CACHE_NAMESPACE + "LOGIN:";
		/** 修改密码验证码 */
		static final String CHGPWD = CACHE_NAMESPACE + "CHGPWD:";
		/** 身份验证验证码 */
		static final String VLDID = CACHE_NAMESPACE + "VLDID:";
		/** 信息变更验证码 */
		static final String CHGINFO = CACHE_NAMESPACE + "CHGINFO:";
		/** 活动确认验证码 */
		static final String AVTCMF = CACHE_NAMESPACE + "AVTCMF:";
	}

	public interface Times {
		/** 一秒 */
		static final long SECOND = 1000;
		/** 一分钟 */
		static final long MINUTE = SECOND * 60;
		/** 一小时 */
		static final long HOUR = MINUTE * 60;
		/** 一天 */
		static final long DAY = HOUR * 24;
		/** 一周 */
		static final long WEEK = DAY * 7;
		/** 一年 */
		static final long YEAR = DAY * 365;
	}

	/** 交易方式 */
	public static enum Payment {
		/** 零钱 */
		POCKET("1"),
		/** 微信-付款码 */
		WECHAT_MICRO("21", "", ""),
		/** 微信-JSAPI */
		WECHAT_JSAPI("22", "JSAPI", ""),
		/** 微信-NATIVE */
		WECHAT_NATIVE("23", "NATIVE", ""),
		/** 微信-APP */
		WECHAT_APP("24", "APP", ".app"),
		/** 微信-H5 */
		WECHAT_H5("25", "MWEB", ""),
		/** 微信-小程序 */
		WECHAT_WXA("26", "JSAPI", ".mip"),
		/** 微信-人脸 */
		WECHAT_FACE("27", "", ""),
		/** 支付宝 */
		ALIPAY("3"),
		/** 银行卡 */
		CARD("4");

		String value;
		String type;
		String appid;

		Payment(String value) {
			this.value = value;
		}

		Payment(String value, String type) {
			this.value = value;
			this.type = type;
		}

		Payment(String value, String type, String appid) {
			this.value = value;
			this.type = type;
			this.appid = appid;
		}

		@Override
		public String toString() {
			return this.value;
		}

		public String value() {
			return this.value;
		}

		public String type() {
			return this.type;
		}

		public String appid() {
			return this.appid;
		}
	}
}
