package com.myprojects.demo.services;

import com.myprojects.demo.dto.movie.MovieFilter;
import com.myprojects.demo.dto.movie.MovieTableItem;
import com.myprojects.demo.utilities.PaginationUtils;
import org.mvel2.templates.TemplateRuntime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class MovieFilterService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final String QUERY_TEMPLATE = """
                SELECT m.id, m.title, m.description, m.creation_date, mu.id as user_id, mu.username as username,
                SUM(CASE WHEN r.is_like = true THEN 1 ELSE 0 END) AS likes,
                SUM(CASE WHEN r.is_like = false THEN 1 ELSE 0 END) AS hates
                FROM movie m
                JOIN movie_user mu on mu.id = m.uploaded_by
                LEFT JOIN reaction r on r.movie_id = m.id
                GROUP BY m.id, m.title, m.description, m.creation_date, mu.id
                ORDER BY likes desc
            """;

    public MovieFilterService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Page<MovieTableItem> filter(MovieFilter movieFilter) {
        MovieResult result = find(movieFilter);
        return new PageImpl<>(result.getMovies(), movieFilter.getPageRequest(), result.getRowCount());
    }

    protected MovieResult find(MovieFilter filter) {
        Map<String, Object> queryParams = new HashMap<>();

        StringBuilder whereClause = new StringBuilder();
        if (filter.getMovieUser() != null) {
            whereClause.append(" WHERE m.uploaded_by = (:movieUserId)");
            queryParams.put("movieUserId", filter.getMovieUser().getId());
        }
        queryParams.put("whereClause", whereClause);

        StringBuilder queryBuilder = new StringBuilder("with dt as (")
                .append(TemplateRuntime.eval(QUERY_TEMPLATE, queryParams))
                .append(")");
        queryBuilder.append(" select * from (table dt) sub ")
                .append(" right join (select count(1) from dt) c(row_count) on true");
        appendSortBy(filter.getSort(), queryBuilder);
        String backUpQuery = queryBuilder.toString();
        paginate(filter.getPageRequest(), queryBuilder);

        List<MovieTableItem> movies = namedParameterJdbcTemplate.query(queryBuilder.toString(),
                queryParams,
                new BeanPropertyRowMapper<>(MovieTableItem.class));

        if (movies.isEmpty()) {
            if (filter.getPageRequest().getPageNumber() > 0) {
                StringBuilder checkQueryBuilder = new StringBuilder(backUpQuery);
                paginateForEmpty(filter.getPageRequest(), checkQueryBuilder);
                List<MovieTableItem> tempMovies = namedParameterJdbcTemplate.query(checkQueryBuilder.toString(),
                        queryParams,
                        new BeanPropertyRowMapper<>(MovieTableItem.class));

                if (!tempMovies.isEmpty()) {
                    return new MovieResult(Collections.emptyList(), tempMovies.get(0).getRowCount());
                }
            }
            return new MovieResult(Collections.emptyList(), 0L);
        }

        Long rowCount = movies.get(0).getRowCount();
        if (rowCount == 0) {
            return new MovieResult(Collections.emptyList(), rowCount);
        }

        if (movies.size() == 1 &&
            movies.get(0).getRowCount() >= 1 &&
            movies.get(0).getId() == null) {
            return new MovieResult(Collections.emptyList(), rowCount);
        }

        return new MovieResult(movies, rowCount);

    }

    protected void appendSortBy(Sort sort, StringBuilder query) {
        if (sort == null) {
            return;
        }

        Iterator<Sort.Order> iterator = sort.iterator();
        query.append(" order by ");
        while (iterator.hasNext()) {
            Sort.Order sortOrder = iterator.next();
            query.append(sortOrder.getProperty())
                    .append(" ")
                    .append(sortOrder.getDirection().name().toLowerCase());
            if (iterator.hasNext()) {
                query.append(", ");
            }
        }
    }

    protected void paginate(PageRequest pageRequest, StringBuilder query) {
        if (pageRequest == null) {
            return;
        }

        PaginationUtils.OffSetLimit offSetLimit = PaginationUtils.paginate(pageRequest);
        query.append(" offset ").append(offSetLimit.getOffset()).append(" limit ").append(offSetLimit.getLimit());
    }

    private void paginateForEmpty(PageRequest pageRequest, StringBuilder query) {
        query.append("  offset ").append(0L).append(" limit ").append(pageRequest.getPageSize());
    }

    protected static class MovieResult {
        private final List<MovieTableItem> movies;
        private final Long rowCount;

        public MovieResult(List<MovieTableItem> movies, Long rowCount) {
            this.movies = movies;
            this.rowCount = rowCount;
        }

        public List<MovieTableItem> getMovies() {
            return movies;
        }

        public Long getRowCount() {
            return rowCount;
        }

        public boolean isEmpty() {
            return CollectionUtils.isEmpty(movies);
        }
    }
}
