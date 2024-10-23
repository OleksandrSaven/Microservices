package com.shoppingcartservice.client;

import com.shoppingcartservice.dto.BookDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("book-app")
public interface BookClient {

    @GetMapping("/api/books/{id}")
    BookDto findById(@PathVariable Long id);

    @GetMapping("/api/books/specification")
    List<BookDto> findByCriteria(@RequestParam List<Long> bookIds);
}
