package com.esen.bookstore.repository;

import com.esen.bookstore.model.Book;
import lombok.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
}
