package com.example.mybookcatalog;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String description;
    private String category;
    private String content;
    private String isbn;
    private String publisher;
    private String edition;
    private int publicationYear;
    private String language;
    private double distributorPrice;
    private double retailPrice;
    private double discount;
    private int stockQuantity;
    private int pages;
    private double rating;
    private int coverImageRes; // New field for drawable resource

    public Book(String title, String author, String description, String category, String content,
                String isbn, String publisher, String edition, int publicationYear, String language,
                double distributorPrice, double retailPrice, double discount, int stockQuantity,
                int pages, double rating) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.category = category;
        this.content = content;
        this.isbn = isbn;
        this.publisher = publisher;
        this.edition = edition;
        this.publicationYear = publicationYear;
        this.language = language;
        this.distributorPrice = distributorPrice;
        this.retailPrice = retailPrice;
        this.discount = discount;
        this.stockQuantity = stockQuantity;
        this.pages = pages;
        this.rating = rating;
        this.coverImageRes = 0; // Default
    }

    public Book(String title, String author, String description, String category, String content,
                String isbn, String publisher, String edition, int publicationYear, String language,
                double distributorPrice, double retailPrice, double discount, int stockQuantity,
                int pages, double rating, int coverImageRes) {
        this(title, author, description, category, content, isbn, publisher, edition, publicationYear, 
             language, distributorPrice, retailPrice, discount, stockQuantity, pages, rating);
        this.coverImageRes = coverImageRes;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getContent() { return content; }
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
    public String getEdition() { return edition; }
    public int getPublicationYear() { return publicationYear; }
    public String getLanguage() { return language; }
    public double getDistributorPrice() { return distributorPrice; }
    public double getRetailPrice() { return retailPrice; }
    public double getDiscount() { return discount; }
    public int getStockQuantity() { return stockQuantity; }
    public int getPages() { return pages; }
    public double getRating() { return rating; }
    public int getCoverImageRes() { return coverImageRes; }

    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setCoverImageRes(int coverImageRes) { this.coverImageRes = coverImageRes; }
}
