package com.orderservice.client;

import com.orderservice.dto.BookDto;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("book-app")
public interface BookClient {

    @GetMapping("/api/books/specification")
    Set<BookDto> findByCriteria(@RequestParam List<Long> bookIds);

}
