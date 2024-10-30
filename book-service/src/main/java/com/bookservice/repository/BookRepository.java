package com.bookservice.repository;

import com.bookservice.domain.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("FROM Book b LEFT JOIN FETCH b.categories c where c.id = :categoryId")
    List<Book> findByCategoryId(Long categoryId);
}
