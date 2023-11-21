package com.ooadprojectserver.restaurantmanagement.dto.common.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Collection;

@Getter
public class PagedResponse<T> implements Serializable {
    @JsonProperty("data")
    Collection<T> data;
    @JsonProperty("page_meta")
    PageMeta pageMeta;

    PagedResponse(Collection<T> data, PageMeta pageMeta) {
        this.data = data;
        this.pageMeta = pageMeta;
    }

    public static PagedResponseBuilder builder() {
        return new PagedResponseBuilder();
    }

    public String toString() {
        return "PagedResponse(data=" + this.getData() + ", pageMeta=" + this.getPageMeta() + ")";
    }

    public static class PagedResponseBuilder<T> {
        private Page<T> page;

        PagedResponseBuilder() {
        }

        public PagedResponseBuilder page(final Page<T> page) {
            this.page = page;
            return this;
        }

        public PagedResponse<T> build() {
            return new PagedResponse(this.page.getContent(), new PageMeta(this.page.getNumber(), this.page.getSize(), this.page.getTotalElements(), this.page.getTotalPages()));
        }

        public String toString() {
            return "PagedResponse.PagedResponseBuilder(page=" + this.page + ")";
        }
    }
}
