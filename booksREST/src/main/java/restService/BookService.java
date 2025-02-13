package restService;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import dao.BookDAO;
import dao.BookDAOImpl;
import model.Book;
import model.Category;

@Path("Books")
public class BookService {

    @Context
    private ServletContext context;

    private BookDAO bookDao;

    @PostConstruct
    public void init() {
        String dbPath = context.getRealPath("Books.db");
        bookDao = new BookDAOImpl(dbPath);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getAllBooks() {
        return bookDao.findAllBooks();
    }

    @GET
    @Path("/category")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getCategoryNames() {
        return bookDao.findAllCategories();
    }
    
    @GET
	@Path("/searchByCat/{catid}")
	@Produces (MediaType.APPLICATION_JSON)
	public List<Book> getBooksByCategory (@PathParam("catid") String id) {
		return bookDao.findBooksByCategory(id);
	}

    @GET
    @Path("/searchByKey/{keyword}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getBooksByKeyword(@PathParam("keyword") String keyword) {
        return bookDao.searchBooksByKeyword(keyword);
    }
}
