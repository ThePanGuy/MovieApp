package com.myprojects.demo.repositories;

import com.myprojects.demo.dto.MovieRecord;
import com.myprojects.demo.entities.Movie;
import com.myprojects.demo.entities.MovieUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title);

    @Query("""
              select new com.myprojects.demo.dto.MovieRecord(m.id, m.title, m.description, m.creationDate,m.uploadedBy,sum(case when r.isLike = true then 1 else 0 end),sum(case when r.isLike = false then 1 else 0 end))\s
              from Movie m\s
              left join Reaction r on m.id = r.movie.id\s
              group by m.id, m.title, m.description, m.creationDate, m.uploadedBy""")
    Page<MovieRecord> findAllBy(Pageable pageable);


    @Query("select new com.myprojects.demo.dto.MovieRecord(m.id, m.title, m.description, m.creationDate, m.uploadedBy," +
           " sum(case when r.isLike = true then 1 else 0 end), sum(case when r.isLike = false then 1 else 0 end)) " +
           "from Movie m left join m.reactions r where m.uploadedBy = :user group by m.id, m.title, m.description, m.creationDate, m.uploadedBy")
    Page<MovieRecord> findAllByUploadedByUser(@Param("user") MovieUser movieUser, Pageable pageable);


    Page<Movie> findAllByUploadedBy(MovieUser uploadedBy, Pageable pageable);
}
