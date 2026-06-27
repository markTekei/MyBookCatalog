package com.example.mybookcatalog;

import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private static BookRepository instance;
    private final List<Book> allBooks;
    private final List<Book> cartItems;

    private BookRepository() {
        allBooks = new ArrayList<>();
        cartItems = new ArrayList<>();
        
        // --- Fiction ---
        allBooks.add(new Book("The Alchemist", "Paulo Coelho", 
                "Santiago, a young shepherd, travels from Spain to the Egyptian desert in search of a treasure. Along the way, he learns that true treasure lies in following your heart.", "Fiction", 
                "Santiago's journey is a magical quest for his Personal Legend. He learns to listen to his heart and understands that when you want something, the universe conspires to help you achieve it.", "978-0062315007", "HarperOne", "1st", 1988, "English", 0.0, 0.0, 0, 150, 208, 4.8, R.drawable.f1));

        allBooks.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", 
                "The mysterious Jay Gatsby throws lavish parties to win back his former love, Daisy Buchanan, in a world of wealth and obsession.", "Fiction", 
                "A tragic tale of the American Dream, seen through the eyes of Nick Carraway, revealing the emptiness behind the glamour of the 1920s.", "978-0743273565", "Scribner", "1st", 1925, "English", 0.0, 0.0, 0, 80, 180, 4.4, R.drawable.f2));

        allBooks.add(new Book("1984", "George Orwell", 
                "Winston Smith rebels against a totalitarian government led by Big Brother in a society where thought and freedom are forbidden.", "Fiction", 
                "A chilling look at a world of constant surveillance and psychological manipulation, where truth is whatever the state says it is.", "978-0451524935", "Signet Classic", "1st", 1949, "English", 0.0, 0.0, 0, 120, 328, 4.7, R.drawable.f3));

        allBooks.add(new Book("The Hobbit", "J.R.R. Tolkien", 
                "Bilbo Baggins, a home-loving hobbit, is swept into a quest to raid the treasure-hoard of Smaug the Magnificent, a large and very dangerous dragon.", "Fiction", 
                "A classic adventure that sets the stage for The Lord of the Rings, filled with trolls, goblins, and a very famous ring.", "978-0547928227", "Houghton Mifflin", "1st", 1937, "English", 0.0, 0.0, 0, 90, 310, 4.9, R.drawable.f1));

        // --- Mystery ---
        allBooks.add(new Book("Sherlock Holmes", "Arthur Conan Doyle", 
                "Detective Sherlock Holmes and Dr. Watson solve the most impossible mysteries in Victorian London using logic and observation.", "Mystery", 
                "Enter 221B Baker Street and follow the world's most famous detective as he proves that even the smallest clue can solve a big crime.", "978-0140437713", "George Newnes", "1st", 1892, "English", 0.0, 0.0, 0, 50, 307, 4.9, R.drawable.m1));

        allBooks.add(new Book("The Girl with the Dragon Tattoo", "Stieg Larsson", 
                "A journalist and a brilliant computer hacker team up to solve the mystery of a disappearance that has haunted a wealthy family for decades.", "Mystery", 
                "Mikael Blomkvist and Lisbeth Salander uncover dark secrets and a web of corruption in this high-stakes psychological thriller.", "978-0307269751", "Norstedts", "1st", 2005, "English", 0.0, 0.0, 0, 40, 465, 4.7, R.drawable.m2));

        allBooks.add(new Book("Gone Girl", "Gillian Flynn", 
                "When Amy Dunne disappears on her wedding anniversary, the investigation turns on her husband Nick, revealing secrets in their marriage.", "Mystery", 
                "A twisted tale of secrets and lies where nothing is as it seems, keeping you guessing about the truth behind Amy's disappearance.", "978-0307588364", "Crown", "1st", 2012, "English", 0.0, 0.0, 0, 60, 415, 4.5, R.drawable.m3));

        allBooks.add(new Book("The Da Vinci Code", "Dan Brown", 
                "A murder in the Louvre leads to a quest through history to uncover a secret that could change the foundations of the modern world.", "Mystery", 
                "Robert Langdon follows a trail of clues hidden in works of art to solve a mystery involving ancient societies and hidden truths.", "978-0385504201", "Doubleday", "1st", 2003, "English", 0.0, 0.0, 0, 100, 454, 4.3, R.drawable.m4));

        // --- Technology ---
        allBooks.add(new Book("Clean Code", "Robert C. Martin", 
                "A guide to writing code that is clean, readable, and easy to maintain. It teaches the craft of software development through practical rules.", "Technology", 
                "Master the essential disciplines of software craftsmanship and learn how to turn messy code into something professional and robust.", "978-0132350884", "Prentice Hall", "1st", 2008, "English", 0.0, 0.0, 0, 40, 464, 4.8, R.drawable.t1));

        allBooks.add(new Book("The Pragmatic Programmer", "Andrew Hunt", 
                "Practical advice for software developers to improve their skills and productivity, focusing on the mindset of an effective coder.", "Technology", 
                "Follow a journey from apprentice to master by adopting the habits and techniques of a truly effective, pragmatic developer.", "978-0135957059", "Addison-Wesley", "2nd", 2019, "English", 0.0, 0.0, 0, 35, 352, 4.9, R.drawable.t2));

        allBooks.add(new Book("Introduction to Algorithms", "Thomas H. Cormen", 
                "The comprehensive reference for understanding and implementing computer algorithms, covering everything from basics to advanced topics.", "Technology", 
                "Master the mathematical foundations and implementation details of the algorithms that power modern computing systems.", "978-0262033848", "MIT Press", "3rd", 2009, "English", 0.0, 0.0, 0, 20, 1312, 4.7, R.drawable.t3));

        allBooks.add(new Book("Design Patterns", "Erich Gamma", 
                "A foundational text describing reusable solutions to common software design problems in object-oriented programming.", "Technology", 
                "Learn the time-tested patterns for creating flexible and maintainable software used by professional developers worldwide.", "978-0201633610", "Addison-Wesley", "1st", 1994, "English", 0.0, 0.0, 0, 25, 395, 4.6, R.drawable.t4));

        // --- Business ---
        allBooks.add(new Book("Zero to One", "Peter Thiel", 
                "A guide to building the future by creating new things that don't yet exist, rather than just copying what is already there.", "Business", 
                "Explore the mindset of a successful entrepreneur and learn how to find value in unexpected places to build a better tomorrow.", "978-0804139297", "Currency", "1st", 2014, "English", 0.0, 0.0, 0, 30, 224, 4.5, R.drawable.b1));

        allBooks.add(new Book("The Lean Startup", "Eric Ries", 
                "A modern approach to building businesses through continuous innovation, fast learning, and adapting to customer needs.", "Business", 
                "Discover how to build a successful startup by testing your ideas early and pivoting based on real-world feedback.", "978-0307887894", "Crown", "1st", 2011, "English", 0.0, 0.0, 0, 45, 336, 4.6, R.drawable.b2));

        allBooks.add(new Book("Good to Great", "Jim Collins", 
                "A study of how average companies can transition to greatness and sustain those results through disciplined leadership and strategy.", "Business", 
                "Learn the key qualities and strategic decisions that transform ordinary organizations into extraordinary, long-lasting successes.", "978-0066620992", "Harper", "1st", 2001, "English", 0.0, 0.0, 0, 25, 320, 4.8, R.drawable.b3));

        allBooks.add(new Book("The Intelligent Investor", "Benjamin Graham", 
                "The definitive book on value investing, providing principles for long-term stock market success while minimizing financial risk.", "Business", 
                "Master the discipline needed to be a successful investor by focusing on intrinsic value and safety in the market.", "978-0060555665", "Harper", "Rev Ed", 2003, "English", 0.0, 0.0, 0, 55, 640, 4.9, R.drawable.b4));

        // --- Kids ---
        allBooks.add(new Book("Charlotte's Web", "E.B. White", 
                "A heartwarming story of the friendship between a pig named Wilbur and a clever barn spider named Charlotte who saves his life.", "Kids", 
                "Discover a timeless tale of loyalty and sacrifice, showing how even a small spider can change a friend's destiny on a farm.", "978-0064400558", "Harper", "1st", 1952, "English", 0.0, 0.0, 0, 70, 192, 4.8, R.drawable.k1));

        allBooks.add(new Book("Where the Wild Things Are", "Maurice Sendak", 
                "A young boy named Max sails away to an island where monsters live and becomes their king before realizing he misses home.", "Kids", 
                "Join Max on a wild journey of imagination, exploring the big feelings of childhood and the warmth of unconditional love.", "978-0060254926", "Harper", "1st", 1963, "English", 0.0, 0.0, 0, 60, 48, 4.8, R.drawable.k2));

        allBooks.add(new Book("Green Eggs and Ham", "Dr. Seuss", 
                "Sam-I-Am tries to convince a grumpy character to try a plate of green eggs and ham in many different places.", "Kids", 
                "A playful and rhythmic story about trying new things and discovering that you might actually like what you were afraid to taste.", "978-0394800165", "Random House", "1st", 1960, "English", 0.0, 0.0, 0, 120, 62, 4.8, R.drawable.k3));

        allBooks.add(new Book("The Very Hungry Caterpillar", "Eric Carle", 
                "A tiny caterpillar eats his way through a variety of foods before finally turning into a beautiful butterfly.", "Kids", 
                "Follow a colorful journey of growth and transformation that teaches children about the life cycle of a butterfly in a fun way.", "978-0399226908", "World", "1st", 1969, "English", 0.0, 0.0, 0, 150, 22, 4.9, R.drawable.k4));

        // --- Self Help ---
        allBooks.add(new Book("Atomic Habits", "James Clear", 
                "A practical system for building good habits and breaking bad ones by making small 1% improvements every day.", "Self Help", 
                "Learn the science of small wins and how daily systems—not big goals—are the key to long-term personal and professional success.", "978-0735211292", "Avery", "1st", 2018, "English", 0.0, 0.0, 0, 100, 320, 4.8, R.drawable.s1));

        allBooks.add(new Book("Ego is the Enemy", "Ryan Holiday", 
                "An exploration of how our own ego often stands in the way of our success and happiness, and how to master it.", "Self Help", 
                "Discover ancient wisdom to help you stay grounded, humble, and effective in your daily life while pursuing your biggest goals.", "978-1591847816", "Portfolio", "1st", 2016, "English", 0.0, 0.0, 0, 40, 256, 4.8, R.drawable.s2));

        allBooks.add(new Book("The Obstacle Is The Way", "Ryan Holiday", 
                "A guide to turning trials into triumphs by using ancient wisdom to flip obstacles into opportunities for growth.", "Self Help", 
                "Learn how to develop the resilience and perspective needed to turn every challenge you face into an advantage.", "978-1591846352", "Portfolio", "1st", 2014, "English", 0.0, 0.0, 0, 45, 224, 4.9, R.drawable.s3));

        allBooks.add(new Book("Designing Your Life", "Bill Burnett", 
                "How to use design thinking to build a meaningful and joyful life, regardless of your career or stage in life.", "Self Help", 
                "Adopt the mindset of a designer to prototype different versions of your future and build a path that is truly fulfilling.", "978-1101875322", "Knopf", "1st", 2016, "English", 0.0, 0.0, 0, 25, 272, 4.6, R.drawable.s4));
    }

    public static BookRepository getInstance() {
        if (instance == null) instance = new BookRepository();
        return instance;
    }

    public List<Book> getAllBooks() { return allBooks; }
    public void addBook(Book book) { allBooks.add(book); }
    public List<Book> getCartItems() { return cartItems; }
    public void addToCart(Book book) { cartItems.add(book); }
    public void removeFromCart(Book book) { cartItems.remove(book); }
    public void clearCart() { cartItems.clear(); }
}
