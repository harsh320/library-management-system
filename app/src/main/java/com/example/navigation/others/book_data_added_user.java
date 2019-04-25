package com.example.navigation.others;

public class book_data_added_user{
String bookname,isbn,date,key,bookkey;

    public book_data_added_user(String bookname, String isbn, String date,String key,String bookkey) {
        this.bookname = bookname;
        this.isbn = isbn;
        this.date = date;
        this.key=key;
        this.bookkey=bookkey;
    }

    public String getBookkey() {
        return bookkey;
    }

    public String getKey() {
        return key;
    }

    public String getBookname() {
        return bookname;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getDate() {
        return date;
    }
}
