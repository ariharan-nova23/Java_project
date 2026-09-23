# Faculty Viva Preparation Guide - Checkpoint 1 (JavaFX)

This document provides concise, code-accurate questions and answers for the faculty viva evaluation for the **Emergency Blood Matching System** (CSE 213 Advanced Java Programming).

---

### Q1: What is JavaFX?
**Answer**:  
JavaFX is a modern Java GUI (Graphical User Interface) toolkit used to build rich desktop applications. It provides layout panes (`BorderPane`, `VBox`, `GridPane`), UI controls (`TableView`, `ComboBox`, `Button`), property bindings, and CSS styling support.

---

### Q2: Why are we using JavaFX for this project?
**Answer**:  
JavaFX allows us to create a clean, responsive, event-driven desktop interface. By building the interface in Checkpoint 1, we establish the layout, user navigation, and data models first, making it straightforward to attach MySQL database persistence, multithreading, and matching engines in later checkpoints.

---

### Q3: What is a Class in Java?
**Answer**:  
A class is a blueprint or template that defines the properties (fields) and behaviors (methods) of objects created from it.  
*In our code*: `Donor` is a class defining properties like `name`, `bloodGroup`, `age`, and `location`.

---

### Q4: What is an Object in Java?
**Answer**:  
An object is an instance of a class instantiated in memory holding actual state values.  
*In our code*: `new Donor(1, "Rahul", "O+", 20, "9876543210", "Vijayawada", "Available")` creates a specific donor object.

---

### Q5: What is a Constructor and why do we use it?
**Answer**:  
A constructor is a special block of code invoked when an object is instantiated (`new`). Its purpose is to initialize object fields with default or provided values.

---

### Q6: What is Encapsulation and how is it used in our code?
**Answer**:  
Encapsulation is the practice of hiding an object's internal state using `private` fields and providing access through public getter and setter methods.  
*In our code*: `Donor.java` encapsulates state inside `private final StringProperty bloodGroup` and exposes public methods like `getBloodGroup()` and `setBloodGroup()`.

---

### Q7: Why are `Donor` and `BloodRequest` separate classes?
**Answer**:  
Separating them follows the **Single Responsibility Principle (SRP)**. `Donor` models blood donor entities, while `BloodRequest` models emergency request entities. Separating model concerns keeps data handling modular and readable.

---

### Q8: What is a `ComboBox` in JavaFX?
**Answer**:  
A `ComboBox` is a JavaFX UI drop-down control that allows users to select a single item from a list of options.  
*In our code*: We use `ComboBox<String>` for selecting blood groups (`A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-`) and urgency levels (`LOW`, `MEDIUM`, `CRITICAL`).

---

### Q9: What is a `TableView` in JavaFX and how does it display data?
**Answer**:  
`TableView` is a grid-based control used to render tabular data. Columns (`TableColumn`) are mapped to model properties using `PropertyValueFactory`.  
*In our code*: `DonorManagementView.java` uses `TableView<Donor>` bound to an `ObservableList<Donor>`, allowing live rendering and row updates.

---

### Q10: What is Exception Handling and how is it implemented in our UI?
**Answer**:  
Exception handling intercepts runtime errors (such as non-numeric text in integer input fields) to prevent application crashes.  
*In our code*: We wrap input parsing inside `try { Integer.parseInt(ageStr); } catch (NumberFormatException e) { AlertUtil.showError(...); }` to show user error dialogs cleanly.

---

### Q11: Why are we using mock data for Checkpoint 1?
**Answer**:  
Using mock data allows us to verify UI components, layout responsiveness, navigation, and table bindings before introducing database connectivity.

---

### Q12: Why are we not using MySQL/JDBC yet?
**Answer**:  
Checkpoint 1 focuses strictly on the front-end user interface and model skeleton. Database connections (MySQL/JDBC) are deferred to Checkpoint 2 to follow an incremental, structured development workflow.

---

### Q13: What will be added in Checkpoint 2?
**Answer**:  
- MySQL database schema and JDBC connection utility (`DBConnection.java`).
- Data Access Object (DAO) pattern to replace in-memory mock lists with real database queries (`SELECT`, `INSERT`, `UPDATE`, `DELETE`).

---

### Q14: What will be added in Checkpoint 3 and future checkpoints?
**Answer**:  
- Multithreaded emergency request processing.
- Real donor matching & ranking engine (calculating distance scores, compatibility matrices, and response rates).
