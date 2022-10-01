package cn.iyunbei.handself.model;

public class ShareEntity {
    public Integer getPage() {
        return page;
    }

    public ShareEntity(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    Integer page;
    Integer pageSize;
}
