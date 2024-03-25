package com.myprojects.demo.dto.movie;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class MovieFilter {
    private final Long uploadedById;
    private final Sort sort;
    private final PageRequest pageRequest;

    public MovieFilter(Builder builder) {
        this.uploadedById = builder.uploadedById;
        this.sort = builder.sort;
        this.pageRequest = builder.pageRequest;
    }

    public Long getUploadedById() {
        return uploadedById;
    }

    public Sort getSort() {
        return sort;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public static class Builder {
        private Long uploadedById;
        private Sort sort;
        private PageRequest pageRequest;

        public Builder setUploadedById(Long uploadedById) {
            this.uploadedById = uploadedById;
            return this;
        }

        public Builder setSort(Sort sort) {
            this.sort = sort;
            return this;
        }

        public Builder setPageRequest(PageRequest pageRequest) {
            this.pageRequest = pageRequest;
            return this;
        }

        public MovieFilter build() {
            return new MovieFilter(this);
        }
    }
}