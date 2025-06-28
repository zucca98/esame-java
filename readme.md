# Sistema di Gestione Biblioteca

**Progetto Finale - Programmazione Orientata agli Oggetti**  
**EPICODE Institute of Technology**

---

## a. Panoramica dell'Applicazione e Funzionalità

### Descrizione del Sistema

Il Sistema di Gestione Biblioteca è un'applicazione Java SE completa che dimostra principi di progettazione orientata agli oggetti, design pattern e tecnologie Java core. L'applicazione permette la gestione di una collezione bibliotecaria con libri e riviste, offrendo funzionalità di ricerca avanzata e persistenza dei dati.

### Funzionalità Principali

**Gestione Collezione:**
- Aggiunta di libri con validazione completa (ISBN, titolo, autore, pagine)
- Aggiunta di riviste con controllo dati (ISSN, titolo, numero)
- Validazione automatica dei dati di input per prevenire errori

**Sistema di Ricerca:**
- Ricerca per titolo (ricerca parziale, case-insensitive)
- Ricerca per ID (ISBN per libri, ISSN per riviste)
- Algoritmi di ricerca intercambiabili tramite Strategy Pattern

**Persistenza Dati:**
- Salvataggio automatico della collezione su file
- Caricamento dati all'avvio dell'applicazione
- Gestione robusta di errori I/O

**Interfaccia Utente:**
- Menu interattivo con opzioni intuitive
- Validazione input utente con messaggi di errore chiari
- Visualizzazione formattata degli elementi della collezione

**Logging e Debugging:**
- Sistema di logging completo per tutte le operazioni
- Tracciamento errori e operazioni per debugging
- File di log strutturati per analisi

---

## b. Tecnologie e Pattern Utilizzati, con Giustificazioni

### Design Pattern Obbligatori

#### 1. Factory Pattern (LibraryItemFactory)
**Implementazione:** Classe factory per creare oggetti Book e Magazine  
**Giustificazione:** Centralizza la logica di creazione degli oggetti e la validazione, permettendo facile estensione per nuovi tipi di elementi bibliotecari. Rispetta il principio Single Responsibility e facilita il mantenimento del codice.

#### 2. Composite Pattern (Category, ItemLeaf, LibraryComponent)
**Implementazione:** Struttura gerarchica per organizzare elementi in categorie  
**Giustificazione:** Permette il trattamento uniforme di elementi singoli e collezioni di elementi. Facilita l'organizzazione della biblioteca in categorie e sottocategorie, rendendo il sistema scalabile per biblioteche di grandi dimensioni.

#### 3. Iterator Pattern (LibraryIterator, LibraryCollection)
**Implementazione:** Iterator personalizzato per attraversare collezioni di LibraryItem  
**Giustificazione:** Fornisce accesso sequenziale agli elementi senza esporre la struttura interna delle collezioni. Permette controllo granulare sull'iterazione e supporta operazioni come reset e posizione corrente.

#### 4. Exception Shielding (LibraryException, BookNotFoundException, InvalidDataException)
**Implementazione:** Gerarchia di eccezioni personalizzate che nascondono errori interni  
**Giustificazione:** Protegge l'utente finale da dettagli tecnici interni, fornendo messaggi di errore comprensibili. Migliora la sicurezza prevenendo la divulgazione di informazioni sensibili del sistema.

### Design Pattern Opzionali

#### 5. Builder Pattern (Book.BookBuilder)
**Implementazione:** Builder per costruzione fluida di oggetti Book complessi  
**Giustificazione:** Facilita la creazione di oggetti con molti parametri opzionali. Rende il codice più leggibile e manutenibile, permettendo validazione durante la costruzione.

#### 6. Strategy Pattern (SearchStrategy, TitleSearchStrategy, IdSearchStrategy)
**Implementazione:** Algoritmi di ricerca intercambiabili  
**Giustificazione:** Permette di cambiare algoritmo di ricerca a runtime. Rispetta il principio Open/Closed - aperto per estensione, chiuso per modifica. Facilita l'aggiunta di nuovi tipi di ricerca.

#### 7. Singleton Pattern (LibraryManager)
**Implementazione:** Singola istanza del manager della biblioteca  
**Giustificazione:** Garantisce un punto di accesso globale al sistema e previene problemi di concorrenza. Assicura coerenza dei dati e controllo centralizzato delle operazioni.

### Tecnologie Core

#### Collections Framework
**Utilizzo:** List<LibraryItem>, Map<String, LibraryItem>, ArrayList, HashMap  
**Giustificazione:** Fornisce strutture dati efficienti e type-safe. ArrayList per ricerche sequenziali, HashMap per accesso diretto per ID. Sfrutta le ottimizzazioni della JVM.

#### Generics
**Utilizzo:** Tutte le collezioni sono parametrizzate  
**Giustificazione:** Garantisce type safety a compile-time, elimina cast e migliora performance. Rende il codice più leggibile e manutenibile.

#### Java I/O
**Utilizzo:** FileWriter, BufferedReader, PrintWriter per persistenza  
**Giustificazione:** Implementazione semplice e affidabile per persistenza dei dati. BufferedReader per efficienza in lettura, PrintWriter per formattazione output.

#### Logging (SLF4J + Logback)
**Utilizzo:** Logging strutturato di tutte le operazioni del sistema  
**Giustificazione:** SLF4J è lo standard de facto per logging in Java. Logback offre performance superiori e configurabilità avanzata rispetto a Java Util Logging.

#### JUnit Testing
**Utilizzo:** Suite completa di test per tutti i componenti
**Giustificazione:** JUnit 5 offre annotazioni moderne e assertion potenti. Testing automatizzato garantisce qualità del codice e facilita refactoring sicuro.

#### Mockito Framework
**Utilizzo:** Testing avanzato con mock objects per componenti isolati
**Giustificazione:** Mockito 5.12.0 permette testing di unità isolate mockando dipendenze esterne. Essenziale per testare Singleton (LibraryManager), Strategy implementations, e validatori senza dipendenze reali.

### Tecnologie Avanzate

#### Stream API & Lambdas
**Utilizzo:** Nelle strategie di ricerca e filtri collezioni  
**Giustificazione:** Approccio funzionale moderno per elaborazione collezioni. Codice più conciso e leggibile, sfrutta ottimizzazioni parallele della JVM.

---

## c. Istruzioni per Setup ed Esecuzione

### Prerequisiti

- **Java Development Kit (JDK) 21** o superiore
- **Apache Maven 3.9.9** o superiore
- **Sistema Operativo:** Windows 11, Linux, o macOS
- **IDE Consigliato:** IntelliJ IDEA, Eclipse, o Visual Studio Code con estensioni Java

### Dipendenze Maven

Il progetto utilizza le seguenti dipendenze principali gestite automaticamente da Maven:

- **JUnit Jupiter 5.10.1**: Framework di testing moderno per Java
- **Mockito 5.12.0**: Framework per mock objects e testing avanzato
- **SLF4J 2.0.9**: API di logging standard per Java
- **Logback 1.4.14**: Implementazione performante di logging

Tutte le dipendenze vengono scaricate automaticamente durante la compilazione con `mvn compile` o `mvn test`.

### Installazione e Setup

#### 1. Preparazione Ambiente
```bash
# Verifica installazione Java
java -version
javac -version

# Verifica installazione Maven
mvn -version
```

#### 2. Download e Setup Progetto
```bash
# Clona o scarica il progetto
git clone [repository-url]
cd library-system

# Oppure estrai da archivio ZIP e naviga nella cartella
cd library-system
```

#### 3. Verifica Struttura Progetto
```bash
# Verifica presenza file essenziali
ls -la pom.xml
ls -la src/main/java/com/biblioteca/Main.java
```

### Compilazione ed Esecuzione

#### Metodo 1: Esecuzione Standard con Maven
```bash
# Pulisci e compila il progetto
mvn clean compile

# Esegui tutti i test
mvn test

# Esegui l'applicazione
mvn exec:java -Dexec.mainClass="com.biblioteca.Main"
```

#### Metodo 2: Creazione JAR Eseguibile
```bash
# Crea JAR con tutte le dipendenze
mvn clean package

# Esegui il JAR
java -jar target/library-system-1.0-SNAPSHOT.jar
```

#### Metodo 3: Esecuzione Diretta Java
```bash
# Compila con Maven
mvn clean compile

# Esegui direttamente con Java
java -cp target/classes com.biblioteca.Main
```

### Utilizzo dell'Applicazione

#### Menu Principale
1. **Add Book** - Aggiunge un nuovo libro alla collezione
2. **Add Magazine** - Aggiunge una nuova rivista alla collezione  
3. **Search Items** - Ricerca elementi per titolo o ID
4. **Display All Items** - Mostra tutta la collezione
5. **Save Data** - Salva i dati su file
6. **Show Statistics** - Mostra statistiche della collezione
0. **Exit** - Esci dal programma

#### Esempi di Utilizzo
**Aggiunta Libro:**
- ISBN: 978-0134685991
- Titolo: Effective Java
- Autore: Joshua Bloch
- Pagine: 416

**Ricerca per Titolo:**
- Inserisci: "Java" (trova tutti gli elementi con "Java" nel titolo)

**Ricerca per ID:**
- Inserisci: "978-0134685991" (trova il libro specifico)

---

## d. Diagrammi UML (Classi + Architetturale)

### Diagramma delle Classi Principale

```
┌─────────────────────────┐
│     <<interface>>       │
│     LibraryItem         │
├─────────────────────────┤
│ + getId(): String       │
│ + getTitle(): String    │
│ + getType(): String     │
│ + isAvailable(): boolean│
│ + setAvailable(boolean) │
│ + displayInfo(): void   │
└─────────┬───────────────┘
          △
          │
    ┌─────┴─────┐
    │           │
┌───▼────┐  ┌───▼────────┐
│  Book  │  │  Magazine  │
├────────┤  ├────────────┤
│- isbn  │  │- issn      │
│- title │  │- title     │
│- author│  │- issueNum  │
│- pages │  │- available │
│- avail │  └────────────┘
└────────┘
```

### Diagramma Pattern Factory

```
┌────────────────────────┐
│  LibraryItemFactory    │
├────────────────────────┤
│+ createBook(...): Book │
│+ createMag(...): Mag   │
│- validateBook(...)     │
│- validateMag(...)      │
└────────────────────────┘
           │ creates
           ▼
┌────────────────────────┐
│     LibraryItem        │
│    <<interface>>       │
└────────────────────────┘
```

### Diagramma Pattern Composite

```
┌─────────────────────────┐
│   LibraryComponent      │
│    <<interface>>        │
├─────────────────────────┤
│+ displayInfo(): void    │
│+ getItemCount(): int    │
│+ add(component): void   │
│+ remove(component)      │
│+ getChildren(): List    │
└────────────┬────────────┘
             △
             │
    ┌────────┴────────┐
    │                 │
┌───▼────┐     ┌─────▼─────┐
│Category│     │  ItemLeaf │
│────────│     │───────────│
│-name   │     │-item      │
│-children│    └───────────┘
└────────┘
```

### Diagramma Pattern Strategy

```
┌─────────────────────────┐
│   SearchStrategy        │
│   <<interface>>         │
├─────────────────────────┤
│+ search(items, query):  │
│  List<LibraryItem>      │
└─────────────┬───────────┘
              △
              │
    ┌─────────┴─────────┐
    │                   │
┌───▼────────┐   ┌─────▼──────┐
│TitleSearch │   │ IdSearch   │
│Strategy    │   │ Strategy   │
└────────────┘   └────────────┘
```

### Diagramma Architetturale del Sistema

```
┌─────────────────────────────────────────────────┐
│                PRESENTATION LAYER               |
│              (Main.java)                        │
│  - Menu Interface                               │
│  - Input Validation                             │
│  - User Interaction                             │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│              BUSINESS LOGIC LAYER               │
│            (LibraryManager)                     │
│  - Business Rules                               │
│  - Transaction Control                          │
│  - Exception Handling                           │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│               PATTERN LAYER                     │
│  ┌─────────────┬─────────────┬─────────────────┐│
│  │   Factory   │  Strategy   │    Iterator     ││
│  │   Pattern   │   Pattern   │    Pattern      ││
│  └─────────────┴─────────────┴─────────────────┘│
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│                MODEL LAYER                      │
│           (Domain Objects)                      │
│  - Book, Magazine, LibraryItem                  │
│  - Business Entities                            │
│  - Data Validation                              │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│              PERSISTENCE LAYER                  │
│               (File I/O)                        │
│  - Data Storage                                 │
│  - File Operations                              │
│  - Data Serialization                           │
└─────────────────────────────────────────────────┘
```

### Diagramma di Sequenza - Aggiunta Libro

```
User → Main: addBook()
Main → LibraryManager: addBook(isbn, title, author, pages)
LibraryManager → LibraryItemFactory: createBook(...)
LibraryItemFactory → Book.Builder: build()
Book.Builder → Book: new Book()
Book → LibraryItemFactory: return book
LibraryItemFactory → LibraryManager: return book
LibraryManager → LibraryCollection: addItem(book)
LibraryManager → Map: put(isbn, book)
LibraryManager → Logger: info("Book added")
LibraryManager → Main: success
Main → User: "Book added successfully!"
```

---

## e. Limitazioni Conosciute e Lavoro Futuro

### Limitazioni Attuali

#### Limitazioni Tecniche

**1. Persistenza Dati**
- **Formato File Semplice:** Utilizza formato testo custom invece di JSON/XML standardizzati
- **Prestazioni:** Non ottimizzato per collezioni molto grandi (>10.000 elementi)
- **Backup:** Nessun sistema di backup automatico o versioning dei dati

**2. Concorrenza**
- **Thread Safety:** Non supporta accesso concorrente da più thread
- **Scalabilità:** Non adatto per sistemi multi-utente
- **Locking:** Nessun meccanismo di lock per accesso ai dati

**3. Interfaccia Utente**
- **Solo Console:** Interfaccia testuale limitata, non adatta per utenti non tecnici
- **Nessuna GUI:** Manca interfaccia grafica moderna
- **Accessibilità:** Limitata per utenti con disabilità

#### Limitazioni Funzionali

**4. Gestione Utenti**
- **Nessuna Autenticazione:** Tutti possono accedere a tutte le funzioni
- **Autorizzazioni:** Non esiste sistema di ruoli (admin, utente, guest)
- **Audit Trail:** Nessun tracciamento delle azioni per utente

**5. Ricerca e Filtri**
- **Ricerca Limitata:** Solo per titolo e ID, manca ricerca per autore, anno, categoria
- **Filtri Avanzati:** Nessun supporto per filtri combinati o ricerca fuzzy
- **Ordinamento:** Nessuna opzione di ordinamento personalizzato

**6. Gestione Prestiti**
- **Sistema Prestiti:** Non implementato - funzionalità core di una biblioteca
- **Date Scadenza:** Nessuna gestione di prenotazioni o scadenze
- **Utenti Biblioteca:** Non esiste anagrafica utenti/membri

### Roadmap Sviluppi Futuri

#### Fase 1: Miglioramenti Immediati (1-2 mesi)

**Database Integration**
- Migrazione da file a database H2 embedded
- Schema relazionale normalizzato per prestazioni migliori
- Supporto transazioni ACID per integrità dati

**Enhanced Security**
- Implementazione sistema di autenticazione base (username/password)
- Crittografia password con bcrypt
- Logging sicurezza per tentaivi accesso

**Improved Search**
- Ricerca per autore, anno pubblicazione, categoria
- Ricerca fuzzy per tollerare errori di digitazione
- Filtri combinabili (es: libri Java pubblicati dopo 2020)

#### Fase 2: Espansioni Funzionali

**Sistema Prestiti Completo**
- Gestione membri biblioteca con anagrafica
- Prestiti con date scadenza e rinnovi
- Sistema prenotazioni per libri non disponibili
- Calcolo multe per ritardi

**Web Interface**
- API REST con Spring Boot
- Frontend web responsive (React/Angular)
- Autenticazione JWT per API security

**Advanced Features**
- Import/Export dati in formati standard (CSV, JSON, XML)
- Sistema di backup automatico schedulato
- Reporting e statistiche avanzate (libri più prestati, utenti attivi)
