package com.myprojects.demo.dto.movie;

import com.myprojects.demo.entities.MovieUser;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class MovieFilter {
    private final MovieUser movieUser;
    private final Sort sort;
    private final PageRequest pageRequest;

    public MovieFilter(Builder builder) {
        this.movieUser = builder.movieUser;
        this.sort = builder.sort;
        this.pageRequest = builder.pageRequest;
    }

    public MovieUser getMovieUser() {
        return movieUser;
    }

    public Sort getSort() {
        return sort;
    }

    public PageRequest getPageRequest() {
        return pageRequest;
    }

    public static class Builder {
        private MovieUser movieUser;
        private Sort sort;
        private PageRequest pageRequest;

        public Builder setMovieUser(MovieUser movieUser) {
            this.movieUser = movieUser;
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