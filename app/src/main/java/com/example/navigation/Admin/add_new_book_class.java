package com.example.navigation.Admin;

public class add_new_book_class {
    private String name,key,author, genre,isbn, copies, location;

    public add_new_book_class(String name, String key, String author, String genre, String isbn, String copies, String location) {
        this.name = name;
        this.key = key;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.copies = copies;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getCopies() {
        return copies;
    }

    public String getLocation() {
        return location;
    }
}
