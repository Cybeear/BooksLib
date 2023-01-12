package services;

import entities.Borrow;
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
        return borrowRepository.save(bookId, readerId);
    }

    /**
     * @param readerAndBookIds Function call parser function, show error message
     *                         and return to menu if string of arguments contains any characters other than numbers
     *                         delete object from borrowList if string not contains any characters other than numbers
     */
    public void returnBook(String readerAndBookIds) {
        if (StringUtils.countMatches(readerAndBookIds, "/") != 1
                || StringUtils.equals("/", readerAndBookIds)
                || StringUtils.isBlank(readerAndBookIds)) {
            throw new LibraryServiceException("Your input is incorrect, you need to write name and author separated by '/'!");
        }
        var inputSplit = readerAndBookIds.split("/");
        var readerId = parserService.parseLong(inputSplit[0].trim());
        var bookId = parserService.parseLong(inputSplit[1].trim());
        if (borrowRepository.returnBook(readerId, bookId) == 0) {
            throw new LibraryServiceException("Book or a Reader is not exists!");
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
