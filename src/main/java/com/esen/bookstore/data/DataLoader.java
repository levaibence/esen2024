package com.esen.bookstore.data;

import com.esen.bookstore.repository.BookRepository;
import com.esen.bookstore.repository.BookstoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataLoader {

    private final BookRepository bookRepository;
    private final BookstoreRepository bookstoreRepository;
}
