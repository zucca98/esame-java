# Documentazione Tecnica - Sistema di Gestione Biblioteca Java

## Indice
1. [Panoramica del Progetto](#1-panoramica-del-progetto)
2. [Architettura e Design Pattern](#2-architettura-e-design-pattern)
3. [Struttura del Codice](#3-struttura-del-codice)
4. [Funzionalità Implementate](#4-funzionalità-implementate)
5. [Validazione Autori](#5-validazione-autori)
6. [Guida all'Uso](#6-guida-alluso)
7. [Testing](#7-testing)
8. [Appendici](#8-appendici)

---

## 1. Panoramica del Progetto

### 1.1 Descrizione Generale
Il Sistema di Gestione Biblioteca è un'applicazione Java che implementa una soluzione completa per la gestione di una collezione bibliotecaria. Il sistema permette di gestire libri e riviste, offrendo funzionalità di ricerca avanzate e persistenza dei dati.

### 1.2 Obiettivi del Sistema
- **Gestione Collezione**: Aggiunta e organizzazione di libri e riviste
- **Ricerca Avanzata**: Sistema di ricerca flessibile per titolo e identificatori
- **Validazione Dati**: Controllo rigoroso della qualità dei dati inseriti
- **Persistenza**: Salvataggio e caricamento automatico dei dati
- **Robustezza**: Gestione completa degli errori e delle eccezioni

### 1.3 Funzionalità Principali
- Aggiunta di libri con validazione completa (ISBN, titolo, autore, pagine)
- Aggiunta di riviste con controllo dati (ISSN, titolo, numero)
- Ricerca per titolo (ricerca parziale, case-insensitive)
- Ricerca per ID (ISBN/ISSN) con supporto per ricerca parziale
- Validazione avanzata degli autori per prevenire input numerici
- Salvataggio automatico su file
- Interfaccia utente interattiva con menu

### 1.4 Tecnologie Utilizzate
- **Java 21**: Linguaggio di programmazione principale
- **JUnit 5.10.1**: Framework per unit testing moderno
- **Mockito 5.12.0**: Framework per mock objects e testing avanzato
- **Maven**: Gestione dipendenze e build
- **SLF4J + Logback**: Sistema di logging
- **Design Patterns**: Factory, Strategy, Iterator, Builder, Exception Shielding

---

## 2. Architettura e Design Pattern

### 2.1 Architettura Generale
Il sistema segue un'architettura a livelli (layered architecture) con separazione chiara delle responsabilità:

```
┌─────────────────────────────────────────────────────┐
│                PRESENTATION LAYER                   │
│                   (Main.java)                       │
│              Menu Interattivo                       │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│               BUSINESS LAYER                        │
│  ┌─────────────┬─────────────┬─────────────────────┐│
│  │   Factory   │  Strategy   │    Manager          ││
│  │   Pattern   │   Pattern   │    (Singleton)      ││
│  └─────────────┴─────────────┴─────────────────────┘│
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                MODEL LAYER                          │
│           (Domain Objects)                          │
│  - Book, Magazine, LibraryItem                      │
│  - Business Entities                                │
│  - Data Validation                                  │
└─────────────────────────────────────────────────────┘
```

### 2.2 Design Pattern Implementati

#### 2.2.1 Factory Pattern (LibraryItemFactory)
**Scopo**: Centralizzare la creazione di oggetti LibraryItem con validazione integrata.

**Implementazione**:
```java
public class LibraryItemFactory {
    public static LibraryItem createBook(String isbn, String title, 
                                       String author, int pages) 
            throws InvalidDataException {
        validateBookData(isbn, title, author, pages);
        
        return new Book.BookBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .setAuthor(author)
                .setPages(pages)
                .build();
    }
    
    private static void validateBookData(String isbn, String title, 
                                       String author, int pages) 
            throws InvalidDataException {
        // Validazione ISBN, titolo, autore, pagine
        if (!AuthorValidator.isValidAuthor(author)) {
            throw new InvalidDataException(
                AuthorValidator.getValidationErrorMessage(author));
        }
    }
}
```

**Vantaggi**:
- Validazione centralizzata e consistente
- Facilita l'estensione per nuovi tipi di elementi
- Nasconde la complessità di costruzione degli oggetti

#### 2.2.2 Strategy Pattern (SearchStrategy)
**Scopo**: Permettere algoritmi di ricerca intercambiabili a runtime.

**Implementazione**:
```java
public interface SearchStrategy {
    List<LibraryItem> search(List<LibraryItem> items, String query);
}

public class TitleSearchStrategy implements SearchStrategy {
    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        return items.stream()
                .filter(item -> item.getTitle().toLowerCase()
                        .contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }
}

public class IdSearchStrategy implements SearchStrategy {
    @Override
    public List<LibraryItem> search(List<LibraryItem> items, String query) {
        // Ricerca esatta e parziale per ID
        // Supporta ricerca parziale con minimo 3 cifre
    }
}
```

**Vantaggi**:
- Algoritmi di ricerca facilmente estensibili
- Cambio di strategia a runtime
- Codice più pulito e manutenibile

#### 2.2.3 Iterator Pattern (LibraryCollection)
**Scopo**: Fornire accesso sequenziale agli elementi senza esporre la struttura interna.

**Implementazione**:
```java
public class LibraryCollection implements Iterable<LibraryItem> {
    private List<LibraryItem> items = new ArrayList<>();
    
    @Override
    public Iterator<LibraryItem> iterator() {
        return new LibraryIterator();
    }
    
    private class LibraryIterator implements Iterator<LibraryItem> {
        private int currentIndex = 0;
        
        @Override
        public boolean hasNext() {
            return currentIndex < items.size();
        }
        
        @Override
        public LibraryItem next() {
            return items.get(currentIndex++);
        }
    }
}
```

#### 2.2.4 Builder Pattern (Book.BookBuilder)
**Scopo**: Costruzione fluida di oggetti complessi con molti parametri.

**Implementazione**:
```java
public static class BookBuilder {
    private String isbn;
    private String title;
    private String author;
    private int pages;
    
    public BookBuilder setIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }
    
    public BookBuilder setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public Book build() {
        if (isbn == null || title == null || author == null) {
            throw new IllegalArgumentException(
                "ISBN, title, and author are required");
        }
        return new Book(this);
    }
}
```

#### 2.2.5 Exception Shielding
**Scopo**: Proteggere l'utente finale da dettagli tecnici interni.

**Implementazione**:
```java
public class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class InvalidDataException extends LibraryException {
    public InvalidDataException(String message) {
        super("Invalid data: " + message);
    }
}
```

---

## 3. Struttura del Codice

### 3.1 Organizzazione dei Package

```
src/main/java/com/biblioteca/
├── Main.java                          # Entry point dell'applicazione
├── exceptions/                        # Gestione eccezioni
│   ├── LibraryException.java         # Eccezione base
│   ├── InvalidDataException.java     # Dati non validi
│   └── BookNotFoundException.java    # Libro non trovato
├── factory/                          # Factory Pattern
│   └── LibraryItemFactory.java      # Creazione elementi biblioteca
├── iterator/                         # Iterator Pattern
│   └── LibraryCollection.java       # Collezione iterabile
├── manager/                          # Business Logic
│   └── LibraryManager.java          # Gestione principale (Singleton)
├── model/                            # Domain Objects
│   ├── LibraryItem.java             # Interfaccia base
│   ├── Book.java                    # Implementazione libro
│   └── Magazine.java                # Implementazione rivista
├── strategy/                         # Strategy Pattern
│   ├── SearchStrategy.java          # Interfaccia strategia ricerca
│   ├── TitleSearchStrategy.java     # Ricerca per titolo
│   └── IdSearchStrategy.java        # Ricerca per ID
└── util/                            # Utility Classes
    └── AuthorValidator.java         # Validazione autori
```

### 3.2 Responsabilità dei Package

#### 3.2.1 Package `model`
Contiene le entità del dominio e la logica di business core:
- **LibraryItem**: Interfaccia che definisce il contratto per tutti gli elementi della biblioteca
- **Book**: Implementazione concreta per i libri con Builder Pattern
- **Magazine**: Implementazione concreta per le riviste

#### 3.2.2 Package `factory`
Implementa il Factory Pattern per la creazione centralizzata degli oggetti:
- **LibraryItemFactory**: Factory principale con validazione integrata

#### 3.2.3 Package `strategy`
Implementa il Strategy Pattern per algoritmi di ricerca intercambiabili:
- **SearchStrategy**: Interfaccia per le strategie di ricerca
- **TitleSearchStrategy**: Ricerca case-insensitive per titolo
- **IdSearchStrategy**: Ricerca per ID con supporto parziale

#### 3.2.4 Package `manager`
Contiene la logica di business principale:
- **LibraryManager**: Singleton che coordina tutte le operazioni

#### 3.2.5 Package `exceptions`
Gestione centralizzata delle eccezioni con Exception Shielding:
- Gerarchia di eccezioni personalizzate
- Messaggi user-friendly

#### 3.2.6 Package `util`
Classi di utilità per funzionalità trasversali:
- **AuthorValidator**: Validazione avanzata dei nomi degli autori

---

## 4. Funzionalità Implementate

### 4.1 Gestione Libri e Riviste

#### 4.1.1 Aggiunta Libri
Il sistema permette l'aggiunta di libri con validazione completa:

```java
// Esempio di aggiunta libro
manager.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 416);
```

**Validazioni applicate**:
- ISBN non vuoto
- Titolo non vuoto  
- Autore valido (non puramente numerico)
- Numero pagine positivo
- ISBN univoco nella collezione

#### 4.1.2 Aggiunta Riviste
Gestione delle riviste con controlli specifici:

```java
// Esempio di aggiunta rivista
manager.addMagazine("1234-5678", "Java Magazine", 45);
```

**Validazioni applicate**:
- ISSN non vuoto
- Titolo non vuoto
- Numero issue positivo
- ISSN univoco nella collezione

### 4.2 Sistema di Ricerca Avanzato

#### 4.2.1 Ricerca per Titolo
Ricerca case-insensitive con supporto per corrispondenze parziali:

```java
SearchStrategy titleStrategy = new TitleSearchStrategy();
List<LibraryItem> results = manager.search(titleStrategy, "Java");
// Trova tutti gli elementi con "Java" nel titolo
```

#### 4.2.2 Ricerca per ID (ISBN/ISSN)
Ricerca con supporto per corrispondenze esatte e parziali:

```java
SearchStrategy idStrategy = new IdSearchStrategy();
List<LibraryItem> results = manager.search(idStrategy, "134");
// Trova elementi con "134" nell'ID (minimo 3 cifre)
```

**Caratteristiche della ricerca per ID**:
- Corrispondenza esatta per ID completi
- Ricerca parziale con minimo 3 cifre
- Estrazione automatica delle cifre da query miste
- Supporto per formati con separatori (978-0134685991)

### 4.3 Persistenza Dati

#### 4.3.1 Salvataggio Automatico
Il sistema salva automaticamente i dati in formato testuale:

```
Book|978-0134685991|Effective Java|Joshua Bloch|416|true
Magazine|1234-5678|Java Magazine|45|true
```

#### 4.3.2 Caricamento all'Avvio
Caricamento automatico dei dati salvati con gestione di formati legacy:

```java
// Caricamento automatico all'avvio
manager.loadFromFile();
```

### 4.4 Gestione Eccezioni Robusta

Il sistema implementa una gestione completa delle eccezioni:

```java
try {
    manager.addBook(isbn, title, author, pages);
    System.out.println("Libro aggiunto con successo!");
} catch (InvalidDataException e) {
    System.err.println("Errore nei dati: " + e.getMessage());
} catch (LibraryException e) {
    System.err.println("Errore del sistema: " + e.getMessage());
}
```

---

## 5. Validazione Autori

### 5.1 Panoramica della Funzionalità
La validazione degli autori è una funzionalità avanzata che previene l'inserimento di nomi puramente numerici, garantendo che i campi autore contengano nomi significativi piuttosto che identificatori numerici.

### 5.2 Implementazione Tecnica

#### 5.2.1 Classe AuthorValidator
```java
public class AuthorValidator {
    // Pattern per identificare stringhe puramente numeriche
    private static final Pattern NUMERIC_ONLY_PATTERN = 
        Pattern.compile("^[0-9\\s\\-\\.\\,\\(\\)\\[\\]\\{\\}\\_\\+\\=\\|\\\\\\/@#$%^&*~`]+$");
    
    // Pattern per verificare presenza di caratteri alfabetici
    private static final Pattern CONTAINS_LETTER_PATTERN = 
        Pattern.compile(".*[a-zA-Z].*");
    
    public static boolean isValidAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return false;
        }
        
        String trimmedAuthor = author.trim();
        return CONTAINS_LETTER_PATTERN.matcher(trimmedAuthor).matches();
    }
}
```

#### 5.2.2 Integrazione nel Factory
```java
private static void validateBookData(String isbn, String title, 
                                   String author, int pages) 
        throws InvalidDataException {
    // ... altre validazioni ...
    
    if (!AuthorValidator.isValidAuthor(author)) {
        throw new InvalidDataException(
            AuthorValidator.getValidationErrorMessage(author));
    }
}
```

### 5.3 Esempi di Input

#### 5.3.1 Input Validi ✅
```java
// Nomi tradizionali
"John Smith"
"Jane Doe"
"Mary O'Connor"

// Nomi con numeri come parte legittima
"John Smith Jr. 3rd"
"O'Connor-2"
"Smith III"
"Author123"

// Nomi internazionali
"José María"
"François"
"Müller"
```

#### 5.3.2 Input Non Validi ❌
```java
// Puramente numerici
"123"
"456789"
"0"

// Numerici con separatori
"123-456"
"456 789"
"12-34"
"123.456"
"(123) 456-7890"

// Solo caratteri speciali e numeri
"123@456#789"
"!@#$%^&*()"
```

### 5.4 Messaggi di Errore
Il sistema fornisce messaggi di errore specifici e user-friendly:

```java
// Per input puramente numerici
"Author name cannot consist entirely of numbers and separators (like '123-456'). 
Please enter a valid author name with alphabetic characters."

// Per input vuoti
"Author name cannot be empty"

// Messaggio generico
"Author name must contain at least some alphabetic characters"
```

### 5.5 Vantaggi della Validazione
- **Qualità dei Dati**: Previene l'inserimento di identificatori numerici come nomi
- **User Experience**: Messaggi di errore chiari e specifici
- **Flessibilità**: Accetta nomi legittimi che contengono numeri
- **Robustezza**: Gestisce edge cases e caratteri speciali
- **Internazionalizzazione**: Supporta caratteri non ASCII

---

## 6. Guida all'Uso

### 6.1 Requisiti di Sistema
- **Java**: JDK 17 o superiore
- **Maven**: 3.6.0 o superiore
- **Sistema Operativo**: Windows, macOS, Linux

### 6.2 Installazione e Compilazione

#### 6.2.1 Clone del Repository
```bash
git clone https://github.com/zucca98/esame-java.git
cd esame-java
```

#### 6.2.2 Compilazione con Maven
```bash
# Compilazione completa
mvn clean compile

# Compilazione con test
mvn clean test

# Creazione JAR eseguibile
mvn clean package
```

### 6.3 Esecuzione dell'Applicazione

#### 6.3.1 Esecuzione con Maven
```bash
mvn exec:java -Dexec.mainClass="com.biblioteca.Main"
```

#### 6.3.2 Esecuzione con Java
```bash
# Dopo la compilazione
java -cp target/classes com.biblioteca.Main

# Con JAR
java -jar target/biblioteca-management-1.0.jar
```

### 6.4 Utilizzo dell'Interfaccia

#### 6.4.1 Menu Principale
```
=== Library Management System ===
1. Add Book
2. Add Magazine  
3. Search Items
4. Display All Items
5. Save Data
6. Show Statistics
0. Exit
Choice: 
```

#### 6.4.2 Aggiunta di un Libro
```
=== Add New Book ===
ISBN: 978-0134685991
Title: Effective Java
Author: Joshua Bloch
Pages: 416
Book added successfully!
```

#### 6.4.3 Ricerca Elementi
```
=== Search Items ===
1. Search by Title (partial matching supported)
2. Search by ID (ISBN/ISSN - partial matching with 3+ digits)
Search type: 2

ID Search Info:
- Enter complete ISBN/ISSN for exact match
- Enter at least 3 digits for partial matching
- Example: '134' will find ISBN '978-0134685991'

Search query: 134
Found 1 items:
Book: Effective Java by Joshua Bloch (ISBN: 978-0134685991, Pages: 416, Available: Yes)
```

### 6.5 Gestione dei File
- **File di dati**: `library_data.txt` (creato automaticamente)
- **Formato**: Testo delimitato da pipe (|)
- **Backup**: Consigliato backup periodico del file dati

---

## 7. Testing

### 7.1 Struttura dei Test
Il progetto include una suite completa di test JUnit 5 con supporto Mockito per testing avanzato:

```
src/test/java/com/biblioteca/
├── factory/
│   └── LibraryItemFactoryTest.java    # Test Factory Pattern
├── strategy/
│   └── SearchStrategyTest.java        # Test Strategy Pattern
├── util/
│   └── AuthorValidatorTest.java       # Test validazione autori
└── iterator/
    └── LibraryIteratorTest.java       # Test Iterator Pattern
```

#### 7.1.1 Capacità di Testing Avanzato con Mockito
Il framework Mockito 5.12.0 integrato permette:
- **Mock Objects**: Creazione di oggetti mock per isolare unità di test
- **Stubbing**: Definizione del comportamento di metodi mockati
- **Verification**: Verifica delle interazioni con oggetti mock
- **Spy Objects**: Partial mocking di oggetti reali
- **Argument Matchers**: Matching flessibile degli argomenti

**Componenti ideali per mocking futuro**:
- `LibraryManager` (Singleton): Mock per test di integrazione
- `SearchStrategy` implementations: Mock per test di algoritmi alternativi
- `AuthorValidator`: Mock per test di validazione personalizzata
- `LibraryCollection`: Mock per test di persistenza

### 7.2 Esecuzione dei Test

#### 7.2.1 Tutti i Test
```bash
mvn test
```

#### 7.2.2 Test Specifici
```bash
# Test di una classe specifica
mvn test -Dtest=AuthorValidatorTest

# Test di un metodo specifico
mvn test -Dtest=AuthorValidatorTest#testValidAuthorNames
```

### 7.3 Copertura dei Test

#### 7.3.1 AuthorValidatorTest
- Test per nomi validi con caratteri alfabetici
- Test per nomi validi con numeri legittimi
- Test per input puramente numerici (rifiutati)
- Test per input con separatori numerici (rifiutati)
- Test per edge cases (null, vuoto, whitespace)
- Test per caratteri speciali e internazionali

#### 7.3.2 LibraryItemFactoryTest
- Test creazione libri validi
- Test validazione ISBN, titolo, pagine
- Test validazione autori (integrazione con AuthorValidator)
- Test creazione riviste
- Test gestione eccezioni

#### 7.3.3 SearchStrategyTest
- Test ricerca per titolo (case-insensitive)
- Test ricerca per ID (esatta e parziale)
- Test ricerca con query di lunghezza insufficiente
- Test edge cases (query vuote, null)

### 7.4 Report dei Test
```bash
# Generazione report con Surefire
mvn surefire-report:report

# Report disponibile in: target/site/surefire-report.html
```

---

## 8. Appendici

### 8.1 Diagramma delle Classi UML

```
┌─────────────────────────────────────────────────────┐
│                LibraryItem                          │
│                <<interface>>                        │
├─────────────────────────────────────────────────────┤
│+ getId(): String                                    │
│+ getTitle(): String                                 │
│+ getType(): String                                  │
│+ isAvailable(): boolean                             │
│+ setAvailable(boolean): void                        │
│+ displayInfo(): void                                │
└─────────────────┬───────────────────────────────────┘
                  │
         ┌────────┴────────┐
         │                 │
┌────────▼────────┐ ┌──────▼──────┐
│      Book       │ │   Magazine  │
├─────────────────┤ ├─────────────┤
│- isbn: String   │ │- issn: String│
│- title: String  │ │- title: String│
│- author: String │ │- issueNumber: int│
│- pages: int     │ │- available: boolean│
│- available: boolean│ └─────────────┘
└─────────────────┘
```

### 8.2 Configurazione Maven (pom.xml)
```xml
<properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
    <!-- JUnit 5 for testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>

    <!-- Mockito for advanced testing with mock objects -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.12.0</version>
        <scope>test</scope>
    </dependency>

    <!-- Logging with SLF4J and Logback -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.9</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.14</version>
    </dependency>
</dependencies>
```

### 8.3 Configurazione Logging (logback.xml)
```xml
<configuration>
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>biblioteca.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```


---

**Versione Documento**: 1.0  
**Data**: 2025-06-25  
**Autore**: Zuccolin Alex

