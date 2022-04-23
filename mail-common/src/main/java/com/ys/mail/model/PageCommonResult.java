package com.ys.mail.model;

import java.io.Serializable;

/**
 * @author 24
 * @date 2022/1/22 12:59
 * @description 分页返回对象
 */
public class PageCommonResult extends CommonResult implements Serializable {

    private Integer currentPage;
    private Integer size;
    private Integer total;

    public PageCommonResult(Integer currentPage, Integer size, Integer total) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;
    }

    public PageCommonResult() {

    }


    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
