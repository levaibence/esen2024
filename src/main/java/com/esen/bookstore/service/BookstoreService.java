package com.esen.bookstore.service;

import com.esen.bookstore.model.Book;
import com.esen.bookstore.model.Bookstore;
import com.esen.bookstore.repository.BookRepository;
import com.esen.bookstore.repository.BookstoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookstoreService {

    private final BookstoreRepository bookstoreRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void removeBookFromInventories(Book book) {
        bookstoreRepository.findAll()
                .forEach(bookstore -> {
                    bookstore.getInventory().remove(book);
                    bookstoreRepository.save(bookstore);
                });
    }

    public void save(String location, Double priceModifier, Double moneyInCashRegister) {
        bookstoreRepository.save(Bookstore.builder()
                .location(location)
                .priceModifier(priceModifier)
                .moneyInCashRegister(moneyInCashRegister)
                .build());
    }

    public List<Bookstore> findAll() {
        return bookstoreRepository.findAll();
    }

    public void deleteBookstore(Long id) {
        var bookstore = bookstoreRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find bookstore"));
        bookstoreRepository.delete(bookstore);
    }

    public Map<Bookstore, Double> findPrices(Long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("no such book"));
        var bookStores = bookstoreRepository.findAll();

        Map<Bookstore, Double> bookstoreMap = new HashMap<>();
        for (var b : bookStores) {
            if (b.getInventory().containsKey(book)) {
                Double currPrice = book.getPrice() * b.getPriceModifier();
                bookstoreMap.put(b, currPrice);
            }
        }
        return bookstoreMap;
    }

    public Map<Book, Integer> getStock(Long id) {
        var bookstore = bookstoreRepository.findById(id).orElseThrow(() -> new RuntimeException("no such bookstore"));
        return bookstore.getInventory();
    }

    public void changeStock(Long bookstoreId, Long bookId, Integer amount) {
        var bookstore = bookstoreRepository.findById(bookstoreId).orElseThrow(() -> new RuntimeException("no such bookstore"));
        var book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("no such book"));
        if (bookstore.getInventory().containsKey(book)) {
            var entry = bookstore.getInventory().get(book);
            if (entry + amount < 0) throw new UnsupportedOperationException("Invalid amount, book count would become negative");
            else bookstore.getInventory().replace(book, entry + amount);
        }
        else {
            if (amount < 0) throw new UnsupportedOperationException("Cannot add new book with a negative amount of copies");
            bookstore.getInventory().put(book, amount);
        }
        bookstoreRepository.save(bookstore);
    }
}
