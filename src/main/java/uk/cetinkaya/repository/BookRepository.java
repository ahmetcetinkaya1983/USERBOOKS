package uk.cetinkaya.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uk.cetinkaya.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>{

	Optional<Book> findByIdAndUserId(Long id, Long userId);
	Page<Book> findByUserId(Long userId, Pageable pageable);
	
}
