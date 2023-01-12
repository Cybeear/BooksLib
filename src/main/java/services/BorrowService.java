package services;

import entities.Borrow;
import exceptions.BorrowRepositoryException;
import exceptions.BorrowServiceException;
import exceptions.LibraryServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.BorrowRepository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class BorrowService {
    @Autowired
    BorrowRepository borrowRepository;
    @Autowired
    ParserService parserService;

    /**
     * Function call parser function, show error message
     * and return to menu if string of arguments contains any characters other than numbers
     * add to borrowList if string not contains any characters other than numbers
     */
    public Optional<Borrow> borrowBook(Long bookId, Long readerId) {
        try {
            return borrowRepository.save(bookId, readerId);
        } catch (BorrowRepositoryException borrowDaoException) {
            throw new BorrowServiceException(borrowDaoException.getMessage());
        }
    }


    public void returnBook(Long readerId, Long bookId) {
        if (borrowRepository.returnBook(readerId, bookId) == 0) {
            throw new BorrowServiceException("Book or a Reader is not exists!");
        }
    }

    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }


    /**
     *
     */
    public List<Borrow> getAllReadersWithTheirBorrows() {
        return borrowRepository.findAllReadersWithTheirBorrows();
    }

    /**
     *
     */
    public List<Borrow> getAllBooksWithTheirBorrowers() {
        return borrowRepository.findAllBooksWithTheirBorrowers();
    }
}
