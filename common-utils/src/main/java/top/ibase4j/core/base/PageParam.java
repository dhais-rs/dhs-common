package top.ibase4j.core.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageParam implements Serializable {

    private static final long serialVersionUID = -3906452738691572509L;
    public static final PageParam DEFAULT = new PageParam();

    private Integer size = 30;
    private Integer current = 1;
    private boolean isAsc = false;
    private List<String> orderBy = new ArrayList<>();
    private Map<String, List<String>> inParams = new HashMap<>();
    private Map<String, List<String>> notInParams = new HashMap<>();


    private Map<String, List<Object>> orNewParams = new HashMap<>();
    private Map<String, List<Object>> andNewParams = new HashMap<>();
    private Map<String, Object> isNullParams = new HashMap<>();
    private Map<String, Object> isNotNullParams = new HashMap<>();

    private Map<String, Object> likeParams = new HashMap<>();
    private Map<String, Object> eqParams = new HashMap<>();
    private Map<String, Object> neParams = new HashMap<>();
    private Map<String, Object> gtParams = new HashMap<>();
    private Map<String, Object> ltParams = new HashMap<>();
    private Map<String, Object> geParams = new HashMap<>();
    private Map<String, Object> leParams = new HashMap<>();

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public boolean getAsc() {
        return isAsc;
    }

    public void setAsc(boolean isAsc) {
        this.isAsc = isAsc;
    }

    public List<String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy;
    }

    public Map<String, List<String>> getInParams() {
        return inParams;
    }

    public void setInParams(Map<String, List<String>> inParams) {
        this.inParams = inParams;
    }

    public Map<String, List<String>> getNotInParams() {
        return notInParams;
    }

    public void setNotInParams(Map<String, List<String>> notInParams) {
        this.notInParams = notInParams;
    }

    public Map<String, Object> getLikeParams() {
        return likeParams;
    }

    public void setLikeParams(Map<String, Object> likeParams) {
        this.likeParams = likeParams;
    }

    public Map<String, Object> getEqParams() {
        return eqParams;
    }

    public void setEqParams(Map<String, Object> eqParams) {
        this.eqParams = eqParams;
    }

    public Map<String, Object> getGtParams() {
        return gtParams;
    }

    public void setGtParams(Map<String, Object> gtParams) {
        this.gtParams = gtParams;
    }

    public Map<String, Object> getLtParams() {
        return ltParams;
    }

    public void setLtParams(Map<String, Object> ltParams) {
        this.ltParams = ltParams;
    }

    public Map<String, Object> getGeParams() {
        return geParams;
    }

    public void setGeParams(Map<String, Object> geParams) {
        this.geParams = geParams;
    }

    public Map<String, Object> getNeParams() {
        return neParams;
    }

    public void setNeParams(Map<String, Object> neParams) {
        this.neParams = neParams;
    }

    public Map<String, Object> getLeParams() {
        return leParams;
    }

    public void setLeParams(Map<String, Object> leParams) {
        this.leParams = leParams;
    }

    public Map<String, List<Object>> getOrNewParams() {
        return orNewParams;
    }

    public void setOrNewParams(Map<String, List<Object>> orNewParams) {
        this.orNewParams = orNewParams;
    }

    public Map<String, List<Object>> getAndNewParams() {
        return andNewParams;
    }

    public void setAndNewParams(Map<String, List<Object>> andNewParams) {
        this.andNewParams = andNewParams;
    }


    public Map<String, Object> getIsNullParams() {
        return isNullParams;
    }

    public void setIsNullParams(Map<String, Object> isNullParams) {
        this.isNullParams = isNullParams;
    }

    public Map<String, Object> getIsNotNullParams() {
        return isNotNullParams;
    }

    public void setIsNotNullParams(Map<String, Object> isNotNullParams) {
        this.isNotNullParams = isNotNullParams;
    }

}
