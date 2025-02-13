package model;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Author {
	@JsonIgnore
    private Long id; // Add this field
    private String firstName;
    private String lastName;

    @JsonIgnore
    private Long bookId; // Exclude from JSON

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}