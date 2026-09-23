# Faculty Viva Preparation Guide - Checkpoint 1

This document provides concise, code-accurate questions and answers for the faculty viva evaluation for the **Emergency Blood Matching System** (CSE 213 Advanced Java Programming).

---

### Q1: What is a Class in Java?
**Answer**:  
A class is a blueprint or template used to create objects. It defines the state (fields/variables) and behavior (methods) that objects created from it will possess.  
*In our code*: `Donor` is a class that defines fields like `name`, `age`, `bloodGroup`, and methods like `getBloodGroup()`.

---

### Q2: What is an Object in Java?
**Answer**:  
An object is an instance of a class that holds actual data in memory.  
*In our code*: When we register a donor, `new Donor(1, "Rahul", 20, "O+", "Vijayawada", true)` creates an object holding Rahul's specific donor data.

---

### Q3: What is a Constructor and why do we use it?
**Answer**:  
A constructor is a special method called automatically when an object is instantiated using the `new` keyword. Its primary purpose is to initialize the object's instance variables.  
*In our code*: In `Donor.java`, we have both a default constructor `public Donor()` and a parameterized constructor `public Donor(int donorId, String name, int age, String bloodGroup, String location, boolean available)`.

---

### Q4: Why are fields declared as `private`?
**Answer**:  
Fields are declared `private` to restrict direct access from outside the class. This prevents unintended data corruption or unauthorized modifications from other classes.  
*In our code*: In `Donor.java`, `private String bloodGroup;` prevents external code from setting invalid values directly without going through controlled getter/setter or validation methods.

---

### Q5: What is Encapsulation and how is it implemented here?
**Answer**:  
Encapsulation is the OOP principle of bundling data (fields) and methods that operate on that data into a single unit (class), while hiding the internal implementation details using access modifiers (`private`).  
*In our code*: `Donor.java` encapsulates all donor attributes as private fields, exposing controlled access via public methods like `getName()`, `setName()`, and `displayDonorDetails()`.

---

### Q6: What is a Method in Java?
**Answer**:  
A method is a block of code inside a class designed to perform a specific task when invoked.  
*In our code*: `BloodMatcher.isCompatible(recipientGroup, donorGroup)` is a static method that takes two blood groups as arguments and returns a boolean result.

---

### Q7: Why did we create separate classes (`Donor`, `BloodRequest`, `BloodMatcher`, `Main`) instead of writing everything in `Main.java`?
**Answer**:  
We split the application into separate classes to adhere to the **Single Responsibility Principle (SRP)** and maintain clean architecture:
- `Donor`: Holds donor entity data.
- `BloodRequest`: Holds request entity data.
- `BloodMatcher`: Contains pure business and matching logic.
- `Main`: Manages user interface, menu options, and input loop.

---

### Q8: What is `ArrayList` in Java?
**Answer**:  
`ArrayList` is a dynamic resizable array implementation provided by Java's `java.util` package. Unlike standard arrays with fixed size, an `ArrayList` grows automatically as elements are added.

---

### Q9: Why are we using `ArrayList` for donor storage in Checkpoint 1?
**Answer**:  
We use `ArrayList<Donor>` because in Checkpoint 1 we do not use an external MySQL database yet. `ArrayList` provides an easy, in-memory collection to add donors (`donorList.add(donor)`) and iterate over them efficiently during runtime.

---

### Q10: What is Exception Handling?
**Answer**:  
Exception handling is a mechanism in Java to handle runtime errors (such as bad user input, bad number formats, or missing files) gracefully without abruptly crashing the application.

---

### Q11: What is the difference between `try` and `catch`?
**Answer**:  
- `try`: Encloses the code that might throw an exception (e.g. `Integer.parseInt(input)`).
- `catch`: Specifies the block of code executed if a specific exception occurs, allowing the program to recover cleanly.

---

### Q12: Why do we validate blood groups in `BloodMatcher`?
**Answer**:  
Blood group validation ensures that only medically valid groups (`A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-`) enter the system. It normalizes inputs (e.g., converting lowercase `o+` to `O+`) so compatibility checks work consistently.

---

### Q13: How does `BloodMatcher.isCompatible()` work?
**Answer**:  
It takes `recipientGroup` and `donorGroup` as strings, normalizes them, and evaluates them using a `switch` statement based on red-blood-cell compatibility rules (e.g., `O-` is a universal donor compatible with all recipients, while `AB+` can receive from all groups).

---

### Q14: Why is compatibility logic separated from `Main.java`?
**Answer**:  
Separating compatibility logic into `BloodMatcher.java` decouples business logic from console user interface code. This makes the code modular, reusable, and easy to test or upgrade (e.g., when adding a GUI or database in future checkpoints).

---

### Q15: What features are intentionally deferred for later checkpoints?
**Answer**:  
- **Checkpoint 2 / Future**:
  - MySQL database integration via JDBC for persistent donor storage.
  - Multithreading for handling concurrent emergency requests.
  - JavaFX/Swing desktop GUI.
  - Advanced donor ranking based on distance, response history, and urgency.
