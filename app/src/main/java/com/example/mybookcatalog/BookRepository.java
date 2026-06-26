package com.example.mybookcatalog;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private List<Book> allBooks;
    private List<Book> cartItems;

    private BookRepository() {
        allBooks = new ArrayList<>();
        cartItems = new ArrayList<>();
        
        // Mock data with Fiction books using f1-f7 drawables
        allBooks.add(new Book("milk and honey", "rupi kaur", 
                "Poetry and prose about survival.", "Fiction", 
                "", "978-1449474256", "Andrews McMeel", "1st", 2014, "English", 8.00, 14.99, 0, 100, 208, 4.8, R.drawable.f1));
        
        allBooks.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 
                "The story of the mysteriously wealthy Jay Gatsby.", "Fiction", 
                "", "978-0743273565", "Scribner", "1st", 1925, "English", 10.00, 15.00, 0, 80, 180, 4.4, R.drawable.f2));

        allBooks.add(new Book("1984", "George Orwell", 
                "Dystopian social science fiction novel.", "Fiction", 
                "", "978-0451524935", "Signet Classic", "1st", 1949, "English", 9.00, 14.00, 0, 120, 328, 4.7, R.drawable.f3));

        allBooks.add(new Book("To Kill a Mockingbird", "Harper Lee", 
                "The unforgettable novel of a childhood in a sleepy Southern town.", "Fiction", 
                "", "978-0061120084", "Harper Perennial", "1st", 1960, "English", 11.00, 16.00, 0, 90, 281, 4.9, R.drawable.f4));

        allBooks.add(new Book("The Catcher in the Rye", "J.D. Salinger", 
                "A story about teenager Holden Caulfield's experiences in New York City.", "Fiction", 
                "", "978-0316769488", "Little, Brown", "1st", 1951, "English", 10.00, 14.95, 0, 70, 277, 4.1, R.drawable.f5));

        allBooks.add(new Book("Brave New World", "Aldous Huxley", 
                "A dystopian novel written in 1931.", "Fiction", 
                "", "978-0060850524", "Harper Perennial", "1st", 1932, "English", 12.00, 17.00, 0, 60, 268, 4.6, R.drawable.f6));

        allBooks.add(new Book("The Alchemist", "Paulo Coelho", 
                "A novel about a shepherd boy named Santiago.", "Fiction", 
                "", "978-0062315007", "HarperOne", "1st", 1988, "English", 10.00, 15.00, 0, 150, 208, 4.8, R.drawable.f7));

        // Mystery category with m1-m8 drawables
        allBooks.add(new Book("The Adventures of Sherlock Holmes", "Arthur Conan Doyle", 
                "A collection of twelve short stories by Arthur Conan Doyle.", "Mystery", 
                "", "978-0140437713", "George Newnes", "1st", 1892, "English", 12.00, 18.00, 0, 50, 307, 4.9, R.drawable.m1));

        allBooks.add(new Book("The Girl with the Dragon Tattoo", "Stieg Larsson", 
                "A psychological thriller novel by Swedish author and journalist Stieg Larsson.", "Mystery", 
                "", "978-0307269751", "Norstedts Förlag", "1st", 2005, "English", 15.00, 24.99, 0, 40, 465, 4.7, R.drawable.m2));

        allBooks.add(new Book("Gone Girl", "Gillian Flynn", 
                "A thriller novel by American writer Gillian Flynn.", "Mystery", 
                "", "978-0307588364", "Crown Publishing Group", "1st", 2012, "English", 14.00, 22.00, 0, 60, 415, 4.5, R.drawable.m3));

        allBooks.add(new Book("The Da Vinci Code", "Dan Brown", 
                "A 2003 mystery thriller novel by Dan Brown.", "Mystery", 
                "", "978-0385504201", "Doubleday", "1st", 2003, "English", 16.00, 26.00, 0, 100, 454, 4.3, R.drawable.m4));

        allBooks.add(new Book("And Then There Were None", "Agatha Christie", 
                "A mystery novel by the English writer Agatha Christie.", "Mystery", 
                "", "978-0007119356", "Collins Crime Club", "1st", 1939, "English", 10.00, 15.99, 0, 75, 272, 4.9, R.drawable.m5));

        allBooks.add(new Book("Big Little Lies", "Liane Moriarty", 
                "A 2014 novel by Liane Moriarty.", "Mystery", 
                "", "978-0399167065", "Amy Einhorn Books", "1st", 2014, "English", 13.00, 21.00, 0, 35, 460, 4.6, R.drawable.m6));

        allBooks.add(new Book("The Silence of the Lambs", "Thomas Harris", 
                "A psychological horror novel by Thomas Harris.", "Mystery", 
                "", "978-0312022822", "St. Martin's Press", "1st", 1988, "English", 12.00, 19.99, 0, 45, 338, 4.8, R.drawable.m7));

        allBooks.add(new Book("The Silent Patient", "Alex Michaelides", 
                "A 2019 psychological thriller novel by British-Cypriot author Alex Michaelides.", "Mystery", 
                "", "978-1250301697", "Celadon Books", "1st", 2019, "English", 15.00, 25.00, 0, 80, 336, 4.7, R.drawable.m8));

        // Technology category with t1-t5 drawables
        allBooks.add(new Book("Clean Code", "Robert C. Martin", 
                "A Handbook of Agile Software Craftsmanship.", "Technology", 
                "", "978-0132350884", "Prentice Hall", "1st", 2008, "English", 25.00, 45.00, 0, 40, 464, 4.8, R.drawable.t1));
        
        allBooks.add(new Book("The Pragmatic Programmer", "Andrew Hunt", 
                "Your Journey To Mastery.", "Technology", 
                "", "978-0135957059", "Addison-Wesley", "2nd", 2019, "English", 28.00, 50.00, 0, 35, 352, 4.9, R.drawable.t2));

        allBooks.add(new Book("Introduction to Algorithms", "Thomas H. Cormen", 
                "The standard reference for algorithms.", "Technology", 
                "", "978-0262033848", "MIT Press", "3rd", 2009, "English", 35.00, 75.00, 0, 20, 1312, 4.7, R.drawable.t3));

        allBooks.add(new Book("Design Patterns", "Erich Gamma", 
                "Elements of Reusable Object-Oriented Software.", "Technology", 
                "", "978-0201633610", "Addison-Wesley", "1st", 1994, "English", 30.00, 55.00, 0, 25, 395, 4.6, R.drawable.t4));

        allBooks.add(new Book("Artificial Intelligence", "Stuart Russell", 
                "A Modern Approach.", "Technology", 
                "", "978-0136042594", "Pearson", "3rd", 2009, "English", 40.00, 85.00, 0, 15, 1152, 4.5, R.drawable.t5));

        // Business category with b1-b4 drawables
        allBooks.add(new Book("Zero to One", "Peter Thiel", 
                "Notes on Startups, or How to Build the Future.", "Business", 
                "", "978-0804139297", "Currency", "1st", 2014, "English", 15.00, 27.00, 0, 30, 224, 4.5, R.drawable.b1));
        
        allBooks.add(new Book("The Lean Startup", "Eric Ries", 
                "How Today's Entrepreneurs Use Continuous Innovation to Create Radically Successful Businesses.", "Business", 
                "", "978-0307887894", "Crown Business", "1st", 2011, "English", 18.00, 30.00, 0, 45, 336, 4.6, R.drawable.b2));

        allBooks.add(new Book("Good to Great", "Jim Collins", 
                "Why Some Companies Make the Leap... and Others Don't.", "Business", 
                "", "978-0066620992", "HarperBusiness", "1st", 2001, "English", 20.00, 35.00, 0, 25, 320, 4.8, R.drawable.b3));

        allBooks.add(new Book("The Intelligent Investor", "Benjamin Graham", 
                "The Definitive Book on Value Investing.", "Business", 
                "", "978-0060555665", "Harper Business", "Rev Ed", 2003, "English", 14.00, 25.00, 0, 55, 640, 4.9, R.drawable.b4));

        // Kids category with k1-k6 drawables
        allBooks.add(new Book("The Cat in the Hat", "Dr. Seuss", 
                "A tall anthropomorphic cat shows up at the house of Sally and her brother.", "Kids", 
                "", "978-0394800011", "Random House", "1st", 1957, "English", 5.00, 9.99, 0, 100, 61, 4.9, R.drawable.k1));

        allBooks.add(new Book("Where the Wild Things Are", "Maurice Sendak", 
                "The story of a young boy named Max who travels to an island inhabited by wild creatures.", "Kids", 
                "", "978-0060254926", "Harper & Row", "1st", 1963, "English", 8.00, 18.00, 0, 60, 48, 4.8, R.drawable.k2));

        allBooks.add(new Book("Green Eggs and Ham", "Dr. Seuss", 
                "Sam-I-Am tries to convince the narrator to try green eggs and ham.", "Kids", 
                "", "978-0394800165", "Random House", "1st", 1960, "English", 6.00, 10.99, 0, 120, 62, 4.8, R.drawable.k3));

        allBooks.add(new Book("The Very Hungry Caterpillar", "Eric Carle", 
                "A caterpillar eats its way through a wide variety of foodstuffs before pupating.", "Kids", 
                "", "978-0399226908", "World Publishing Company", "1st", 1969, "English", 7.00, 12.00, 0, 150, 22, 4.9, R.drawable.k4));

        allBooks.add(new Book("Goodnight Moon", "Margaret Wise Brown", 
                "A rabbit's routine of bidding goodnight to various objects in his room.", "Kids", 
                "", "978-0060775858", "Harper & Brothers", "1st", 1947, "English", 5.50, 8.99, 0, 90, 32, 4.7, R.drawable.k5));

        allBooks.add(new Book("Charlotte's Web", "E.B. White", 
                "A pig named Wilbur and his friendship with a barn spider named Charlotte.", "Kids", 
                "", "978-0064400558", "Harper & Brothers", "1st", 1952, "English", 8.00, 14.00, 0, 70, 192, 4.8, R.drawable.k6));

        // Self Help category with s1-s7 drawables
        allBooks.add(new Book("The Brain", "David Eagleman", 
                "The story of you.", "Self Help", 
                "", "978-1101870532", "Pantheon", "1st", 2015, "English", 12.00, 20.00, 0, 50, 224, 4.7, R.drawable.s1));

        allBooks.add(new Book("Ego is the Enemy", "Ryan Holiday", 
                "The fight to master our greatest opponent.", "Self Help", 
                "", "978-1591847816", "Portfolio", "1st", 2016, "English", 12.00, 25.00, 0, 40, 256, 4.8, R.drawable.s2));
        
        allBooks.add(new Book("The Obstacle Is The Way", "Ryan Holiday", 
                "The Timeless Art of Turning Trials into Triumph.", "Self Help", 
                "", "978-1591846352", "Portfolio", "1st", 2014, "English", 12.00, 25.00, 0, 45, 224, 4.9, R.drawable.s3));

        allBooks.add(new Book("Designing Your Life", "Bill Burnett", 
                "How to Build a Well-Lived, Joyful Life.", "Self Help",
                "", "978-1101875322", "Knopf", "1st", 2016, "English", 15.00, 26.00, 0, 25, 272, 4.6, R.drawable.s4));

        allBooks.add(new Book("Atomic Habits", "James Clear", 
                "An Easy & Proven Way to Build Good Habits & Break Bad Ones.", "Self Help",
                "", "978-0735211292", "Avery", "1st", 2018, "English", 16.00, 27.00, 0, 100, 320, 4.8, R.drawable.s5));

        allBooks.add(new Book("Deep Work", "Cal Newport", 
                "Rules for Focused Success in a Distracted World.", "Self Help",
                "", "978-1455586691", "Grand Central Publishing", "1st", 2016, "English", 14.00, 25.00, 0, 60, 304, 4.6, R.drawable.s6));

        allBooks.add(new Book("The 7 Habits of Highly Effective People", "Stephen R. Covey", 
                "Powerful Lessons in Personal Change.", "Self Help",
                "", "978-0743269513", "Free Press", "1st", 1989, "English", 15.00, 28.00, 0, 80, 381, 4.7, R.drawable.s7));
    }

    public static BookRepository getInstance() {
        if (instance == null) instance = new BookRepository();
        return instance;
    }

    public List<Book> getAllBooks() { return allBooks; }
    
    public void addBook(Book book) { allBooks.add(book); }

    public List<Book> getCartItems() { return cartItems; }

    public void addToCart(Book book) {
        cartItems.add(book);
    }

    public void removeFromCart(Book book) {
        cartItems.remove(book);
    }

    public void clearCart() {
        cartItems.clear();
    }
}
