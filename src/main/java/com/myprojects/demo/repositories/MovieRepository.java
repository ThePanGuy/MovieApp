package com.myprojects.demo.repositories;

import com.myprojects.demo.dto.movie.MovieRecord;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Optional<Movie> findByTitle(String title);

    @Query("""
            select m
            from Movie m
            left join Reaction r on m.id = r.movie.id\s
            group by m.id, m.title, m.description, m.creationDate, m.uploadedBy""")
    Page<Movie> findAllBy(Pageable pageable);


    //todo: try to make this query work for efficiency
    @Query("select new com.myprojects.demo.dto.movie.MovieRecord(m) " +
           "from Movie m left join m.reactions r where m.uploadedBy = :user group by m.id, m.title, m.description, m.creationDate, m.uploadedBy")
    Page<MovieRecord> findAllByUploadedByUser(@Param("user") MovieUser movieUser, Pageable pageable);


    Page<Movie> findAllByUploadedBy(MovieUser uploadedBy, Pageable pageable);
}
