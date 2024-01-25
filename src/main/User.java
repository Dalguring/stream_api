package main;

import java.util.List;

public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String nickName;
    private String email;
    private Integer level;
    private List<String> journeyBook;

    public User(Long id, String firstName, String lastName, String nickName, String email, Integer level, List<String> journeyBook) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.email = email;
        this.level = level;
        this.journeyBook = journeyBook;
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<String> getJourneyBook() {
        return journeyBook;
    }

    public void setJourneyBook(List<String> journeyBook) {
        this.journeyBook = journeyBook;
    }
}
