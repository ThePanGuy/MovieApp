package com.myprojects.demo.utilities;

import org.springframework.data.domain.PageRequest;

public class PaginationUtils {

    public static OffSetLimit paginate(PageRequest pageRequest) {
        Integer offSet =
                pageRequest.getPageSize() * pageRequest.getPageNumber();
        return new OffSetLimit(offSet, pageRequest.getPageSize());
    }

    public static class OffSetLimit {
        private final Integer offset;
        private final Integer limit;

        public OffSetLimit(Integer offset, Integer limit) {
            this.offset = offset;
            this.limit = limit;
        }

        public Integer getOffset() {
            return offset;
        }

        public Integer getLimit() {
            return limit;
        }
    }

}