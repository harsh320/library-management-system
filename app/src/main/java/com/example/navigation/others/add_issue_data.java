package com.example.navigation.others;

public class add_issue_data {
    private String email,bookname,time,isbn,key,key1,sid,bookkey,genre;

    public add_issue_data(String email, String bookname, String time, String isbn,String key,String key1,String sid,String bookkey,String genre) {
        this.email = email;
        this.bookname = bookname;
        this.time = time;
        this.isbn = isbn;
        this.key=key;
        this.key1=key1;
        this.sid=sid;
        this.bookkey=bookkey;
        this.genre=genre;

    }

    public String getGenre() {
        return genre;
    }

    public String getSid() {
        return sid;
    }

    public String getBookkey() {
        return bookkey;
    }

    public String getEmail() {
        return email;
    }

    public String getBookname() {
        return bookname;
    }

    public String getTime() {
        return time;
    }

    public String getKey() {
        return key;
    }

    public String getKey1() {
        return key1;
    }

    public String getIsbn() {
        return isbn;
    }
}
