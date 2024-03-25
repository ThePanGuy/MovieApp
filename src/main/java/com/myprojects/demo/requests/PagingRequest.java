package com.myprojects.demo.requests;

import org.springframework.data.domain.Sort;

import java.util.List;

public class PagingRequest {
    private int page;
    private int size;
    private Order order;
    private Filter filter;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean hasSorting() {
        return order != null;
    }

    public Sort getSorting() {
        if (hasSorting()) {
            return Sort.by(new Sort.Order(order.getIsAsc() ? Sort.Direction.ASC : Sort.Direction.DESC, order.getProperty()));
        }
        return null;
    }

    public String getFilterValue(String name) {
        if (filter == null) return null;
        return filter.getFilterValue(name);
    }

    public Long getLongFilterValue(String name) {
        if (filter == null) return null;
        return Long.parseLong(filter.getFilterValue(name));
    }

    public static class Order {
        boolean isAsc;
        String property;

        public boolean getIsAsc() {
            return isAsc;
        }

        public void setIsAsc(boolean asc) {
            isAsc = asc;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }

    public static class Filter {
        List<FilterItem> filterItems;

        public List<FilterItem> getFilterItems() {
            return filterItems;
        }

        public void setFilterItems(List<FilterItem> filterItems) {
            this.filterItems = filterItems;
        }

        public String getFilterValue(String name) {
            if (filterItems == null) return null;
            FilterItem item = filterItems.stream()
                    .filter(f -> f.getName().equals(name))
                    .findFirst().orElse(null);
            return item == null ? null : item.getValue();
        }
    }

    public static class FilterItem {
        private String name;
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
