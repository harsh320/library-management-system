package com.example.navigation.lending;

public class add_book_class {
    private String name,author,isbn,rent_price,key;

    public add_book_class(String name, String author, String isbn, String rent_price,String key) {
        this.name = name;
        this.key=key;
        this.author = author;
        this.isbn = isbn;
        this.rent_price = rent_price;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getRent_price() {
        return rent_price;
    }

    public String getKey() {
        return key;
    }
}
