package top.ibase4j.core.base.result;

public class QueryResultModel<T> extends ResultModel {

    private Long total;
    private T rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public T getRows() {
        return rows;
    }

    public void setRows(T rows) {
        this.rows = rows;
    }

}
