package com.chenfeng.symptom.domain.common.pagehelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.chenfeng.symptom.domain.common.serializer.CountablePageSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = CountablePageSerializer.class)
public class CountablePage<E> extends ArrayList<E> implements Page<E> {
    private static final long serialVersionUID = 1L;

        private static final int NO_SQL_COUNT = -1;
        private static final int SQL_COUNT = 0;
        private int pageNum;
        private int pageSize;
        private int startRow;
        private int endRow;
        private long total;
        private int pages;
        private String orderBy;
        private Boolean reasonable;
        private Boolean pageSizeZero;

    public CountablePage() {
        super();
    }

    public CountablePage(int pageNum, int pageSize) {
        this(pageNum, pageSize, SQL_COUNT, null);
    }

    public CountablePage(int pageNum, int pageSize, boolean count) {
        this(pageNum, pageSize, count ? CountablePage.SQL_COUNT : CountablePage.NO_SQL_COUNT, null);
    }

    private CountablePage(int pageNum, int pageSize, int total, Boolean reasonable) {
        super(pageSize > -1 ? pageSize : 0);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        calculateStartAndEndRow();
        setReasonable(reasonable);
    }

    public CountablePage(RowBounds rowBounds, boolean count) {
        this(rowBounds, count ? CountablePage.SQL_COUNT : CountablePage.NO_SQL_COUNT);
    }


    public CountablePage(RowBounds rowBounds, int total) {
        super(rowBounds.getLimit() > -1 ? rowBounds.getLimit() : 0);
        this.pageSize = rowBounds.getLimit();
        this.startRow = rowBounds.getOffset();
        this.pageNum = rowBounds.getOffset() / rowBounds.getLimit();

        this.total = total;
        this.endRow = this.startRow + this.pageSize;
    }

    public List<E> getResult() {
        return this;
    }

    public int getPages() {
        return pages;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {

    	this.pageNum = ((reasonable != null && reasonable) && pageNum <= 0) ? 1 : pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        if (pageSize > 0) {
            pages = (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
        } else {
            pages = 0;
        }
        if ((reasonable != null && reasonable) && pageNum > pages) {
            pageNum = pages;
            calculateStartAndEndRow();
        }
    }

    public void setReasonable(Boolean reasonable) {
        if (reasonable == null) {
            return;
        }
        this.reasonable = reasonable;
        if (this.reasonable && this.pageNum <= 0) {
            this.pageNum = 1;
            calculateStartAndEndRow();
        }
    }

    public Boolean getReasonable() {
        return reasonable;
    }

    public Boolean getPageSizeZero() {
        return pageSizeZero;
    }

    public void setPageSizeZero(Boolean pageSizeZero) {
        this.pageSizeZero = pageSizeZero;
    }

    public String getOrderBy() {
        return orderBy;
    }

        private void calculateStartAndEndRow() {
        this.startRow = this.pageNum > 0 ? (this.pageNum - 1) * this.pageSize : 0;
        this.endRow = this.startRow + this.pageSize * (this.pageNum > 0 ? 1 : 0);
    }

    public boolean isCount() {
        return this.total > NO_SQL_COUNT;
    }

        public CountablePage<E> pageNum(int pageNum) {
        this.pageNum = ((reasonable != null && reasonable) && pageNum <= 0) ? 1 : pageNum;
        return this;
    }

        public CountablePage<E> pageSize(int pageSize) {
        this.pageSize = pageSize;
        calculateStartAndEndRow();
        return this;
    }

        public CountablePage<E> count(Boolean count) {
        this.total = count ? CountablePage.SQL_COUNT : CountablePage.NO_SQL_COUNT;
        return this;
    }

        public CountablePage<E> orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

        public CountablePage<E> reasonable(Boolean reasonable) {
        setReasonable(reasonable);
        return this;
    }

        public CountablePage<E> pageSizeZero(Boolean pageSizeZero) {
        setPageSizeZero(pageSizeZero);
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Page{");
        sb.append("pageNum=").append(pageNum);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", startRow=").append(startRow);
        sb.append(", endRow=").append(endRow);
        sb.append(", total=").append(total);
        sb.append(", pages=").append(pages);
        sb.append(", orderBy='").append(orderBy).append('\'');
        sb.append(", reasonable=").append(reasonable);
        sb.append(", pageSizeZero=").append(pageSizeZero);
        sb.append('}');
        return sb.toString();
    }

	@Override
	public List<E> getContent() {
		List<E> list = new ArrayList<>();
		list.addAll(getResult());
		return list;
	}

	@Override
	public boolean isLastPage() {
		return this.pageNum == this.pages;
	}
	
}
