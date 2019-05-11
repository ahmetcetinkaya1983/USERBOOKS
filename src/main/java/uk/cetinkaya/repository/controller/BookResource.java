package uk.cetinkaya.repository.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import uk.cetinkaya.exceptions.ResourceNotFoundException2;
import uk.cetinkaya.model.Book;
import uk.cetinkaya.repository.BookRepository;
import uk.cetinkaya.repository.UserRepository;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class BookResource {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/users/{userId}/books")
	public Page<Book> getAllBooksByUserId(@PathVariable(value="userId") Long userId, Pageable pageable) {
		return bookRepository.findByUserId(userId, pageable);
	}
	

	@PostMapping("/users/{userId}/books")
	public Book createBook(@PathVariable(value="userId") Long userId, @Valid @RequestBody Book book) {
		return userRepository.findById(userId).map(user -> {
			book.setUser(user);
			return bookRepository.save(book);
		}).orElseThrow(()-> new ResourceNotFoundException2("UserId "+userId+ " not found!"));
	}

	@PutMapping("/users/{userId}/books/{bookId}")
	public Book updateBook( @PathVariable(value="userId") Long userId,
							@PathVariable(value="bookId") Long bookId,
							@Valid @RequestBody Book bookRequest
			) {
		
		if(!bookRepository.existsById(userId)) {
			throw new ResourceNotFoundException2("UserId "+userId+ " not found!");
		}
		
		return bookRepository.findById(bookId).map(book -> {
			book.setName(bookRequest.getName());
			book.setIsbn(bookRequest.getIsbn());
			return bookRepository.save(book);
		}).orElseThrow(()-> new ResourceNotFoundException2("BookId "+bookId+ " not found!"));
		
	}
	
	@DeleteMapping("/users/{userId}/books/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable (value = "userId") Long userId,
                              @PathVariable (value = "bookId") Long bookId) {
        return bookRepository.findByIdAndUserId(bookId, userId).map(book -> {
            bookRepository.delete(book);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException2("Book not found with id " + bookId + " and userId " + userId));
    }
	
	

}
