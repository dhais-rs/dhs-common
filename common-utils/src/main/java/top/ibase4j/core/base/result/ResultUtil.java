package top.ibase4j.core.base.result;

public class ResultUtil {

    public static <T> QueryResultModel<T> success(Long total, T t) {
        QueryResultModel<T> rm = new QueryResultModel<>();
        rm.setCode(1);
        rm.setInfo("查询成功");
        rm.setTimestamp(System.currentTimeMillis());
        rm.setTotal(total);
        rm.setRows(t);
        return rm;
    }


//	public static <T> QueryResultModel<T> success(Long total, T t, String msg){
//		QueryResultModel<T> rm =  new QueryResultModel<>();
//		rm.setCode(1);
//		rm.setInfo(msg);
//		rm.setTimestamp(System.currentTimeMillis());
//		rm.setTotal(total);
//		rm.setRows(t);
//		return rm;
//	}

    public static <T> QueryResultModel<T> success(Integer code, Long total, T t, String msg) {
        QueryResultModel<T> rm = new QueryResultModel<>();
        rm.setCode(code);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        rm.setTotal(total);
        rm.setRows(t);
        return rm;
    }


    public static ResultModel success(String msg) {
        ResultModel rm = new ResultModel();
        rm.setCode(1);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        return rm;
    }

    public static ResultModel success(Integer code, String msg) {
        ResultModel rm = new ResultModel();
        rm.setCode(code);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        return rm;
    }

    public static ResultModel fail(String msg) {
        ResultModel rm = new ResultModel();
        rm.setCode(0);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        return rm;
    }

    public static ResultModel fail(Integer code, String msg) {
        ResultModel rm = new ResultModel();
        rm.setCode(code);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        return rm;
    }

    public static <T> QueryResultModel<T> success(Long total, T t, String msg) {
        QueryResultModel<T> rm = new QueryResultModel<>();
        rm.setCode(1);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        rm.setTotal(Long.valueOf(String.valueOf(total)));
        rm.setRows(t);
        return rm;
    }

    public static <T> QueryResultModel<T> failModel(Integer code, String msg) {
        QueryResultModel<T> rm = new QueryResultModel<>();
        rm.setCode(code);
        rm.setInfo(msg);
        rm.setTimestamp(System.currentTimeMillis());
        return rm;
    }

}