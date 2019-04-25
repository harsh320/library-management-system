package com.example.navigation.others;

public class add_book_data {
    private String name,author,isbn,email,branch;

    public add_book_data(String name, String author, String isbn,String email,String branch) {
        this.name = name;
        this.branch=branch;
        this.author = author;
        this.isbn = isbn;
        this.email=email;
    }

    public String getBranch() {
        return branch;
    }

    public String getName() {
        return name;
    }

    public String getEmail()
    {
        return  email;
    }
    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}
