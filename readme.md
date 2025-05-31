Library Management System
Application Overview
This is a Java SE application that implements a complete Library Management System demonstrating object-oriented design principles, design patterns, and core Java technologies. The system allows users to manage books and magazines, search through the collection, and persist data to files.
Functionality
The application provides the following features:

Add Books and Magazines: Create new library items with validation
Search Functionality: Search by title or ID using different strategies
Data Persistence: Save and load library data from files
Interactive Console Interface: User-friendly menu system
Exception Handling: Robust error handling with custom exceptions
Logging: Comprehensive logging of all operations

Technologies and Patterns Used
Required Design Patterns (16 points)

Factory Pattern (3 pts): LibraryItemFactory creates different types of library items (Books, Magazines) with proper validation
Composite Pattern (4 pts): Category and ItemLeaf classes allow hierarchical organization of library items
Iterator Pattern (4 pts): Custom LibraryIterator provides controlled iteration through collections
Exception Shielding (5 pts): Custom exception hierarchy (LibraryException, BookNotFoundException, InvalidDataException) shields internal errors

Required Technologies (14 points)

Collections Framework (3 pts): Extensive use of List<T>, Map<K,V>, ArrayList, HashMap
Generics (3 pts): Type-safe collections like List<LibraryItem>, Map<String, LibraryItem>
Java I/O (3 pts): File operations for data persistence using FileWriter, BufferedReader, PrintWriter
Logging (2 pts): SLF4J with Logback for comprehensive application logging
JUnit Testing (3 pts): Complete test suite covering all major components

Optional Advanced Features (β points)

Builder Pattern (3 pts): Book.BookBuilder for constructing complex book objects
Strategy Pattern (4 pts): Different search algorithms (TitleSearchStrategy, IdSearchStrategy)
Singleton Pattern (2 pts): LibraryManager ensures single instance
Stream API & Lambdas (5 pts): Modern Java features for collection processing

Setup and Execution Instructions
Prerequisites

Java 21 or higher
Maven 3.9.9 or higher
Git (optional, for version control)

Installation Steps

Clone or Download the Project
bashgit clone <repository-url>
cd library-system

Compile the Project
bashmvn clean compile

Run Tests
bashmvn test

Execute the Application
bashmvn exec:java -Dexec.mainClass="com.biblioteca.Main"
Or compile and run with Java directly:
bashmvn clean compile
java -cp target/classes com.biblioteca.Main


File Structure
library-system/
├── src/
│   ├── main/
│   │   ├── java/com/biblioteca/
│   │   │   ├── Main.java
│   │   │   ├── composite/
│   │   │   ├── exceptions/
│   │   │   ├── factory/
│   │   │   ├── iterator/
│   │   │   ├── manager/
│   │   │   ├── model/
│   │   │   └── strategy/
│   │   └── resources/
│   │       └── logback.xml
│   └── test/java/com/biblioteca/
├── data/           # Generated data files
├── logs/           # Generated log files
├── pom.xml
└── README.md
UML Diagrams
Class Diagram (Key Classes)
LibraryItem <<interface>>
├── Book
└── Magazine

LibraryComponent <<interface>>
├── Category (Composite)
└── ItemLeaf (Leaf)

SearchStrategy <<interface>>
├── TitleSearchStrategy
└── IdSearchStrategy

LibraryManager (Singleton)
├── uses Factory Pattern
├── manages Iterator Pattern
└── handles Exception Shielding
Architectural Diagram
Presentation Layer (Main.java)
    ↓
Business Logic Layer (LibraryManager)
    ↓
Pattern Layer (Factory, Strategy, Iterator)
    ↓
Model Layer (Book, Magazine, LibraryItem)
    ↓
Persistence Layer (File I/O)
Design Decisions and Justifications
Pattern Choices

Factory Pattern: Chosen for creating different types of library items because it centralizes object creation logic and allows for easy extension with new item types.
Composite Pattern: Implemented to allow hierarchical organization of library items into categories and subcategories, enabling uniform treatment of individual items and collections.
Iterator Pattern: Provides controlled access to collections without exposing internal structure, essential for encapsulation.
Strategy Pattern: Enables different search algorithms to be used interchangeably, following the Open/Closed Principle.
Singleton Pattern: Ensures only one LibraryManager instance exists, maintaining system consistency.

Technology Justifications

SLF4J + Logback: Industry standard for logging, provides flexibility and performance.
JUnit 5: Latest testing framework with improved annotations and assertions.
Stream API: Modern Java approach for collection processing, improves code readability.
Generics: Ensures type safety and eliminates need for casting.

Security Considerations

Input Sanitization: All user inputs are validated before processing
Exception Shielding: Internal system errors are wrapped in user-friendly exceptions
No Hardcoded Credentials: No sensitive information is hardcoded
Controlled Exception Propagation: Exceptions are caught and handled appropriately
Safe File Operations: File I/O operations include proper error handling

Known Limitations and Future Work
Current Limitations

File Format: Simple text-based storage format, not suitable for large datasets
Concurrency: No thread safety mechanisms implemented
User Interface: Console-based interface only
Authentication: No user authentication or authorization system
Database: File-based storage instead of database

Future Enhancements


User Management: Add user roles and authentication
Advanced Search: Implement full-text search capabilities
Reporting: Add statistical reports and analytics
Multi-threading: Add concurrent access support
Configuration: External configuration files for system settings

Testing
The project includes comprehensive JUnit 5 tests covering:

Factory Pattern validation
Singleton Pattern verification
Iterator Pattern functionality
Exception handling scenarios
Search strategy implementations

Run tests with: mvn test
Contact and Support
For questions or issues, please refer to the project documentation or contact the development team.

This project was developed as part of an Object-Oriented Programming course final assignment, demonstrating advanced Java concepts and design patterns.