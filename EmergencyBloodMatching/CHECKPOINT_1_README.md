# Emergency Blood Matching System (Checkpoint 1 - JavaFX Frontend)

**Course**: CSE 213 - Advanced Java Programming  
**Institution**: SRM University-AP  
**Author**: CSE 213 Student  

---

## 1. Project Name
**Emergency Blood Matching System** (JavaFX Desktop Application - Checkpoint 1 Frontend & UI Skeleton)

---

## 2. Problem Statement
During medical emergencies, finding compatible blood donors quickly in a specific location is critical. Delays occur due to manual searching or mismatching blood groups. The goal of this application is to store registered donors, accept emergency blood requests, and determine compatible blood donors using prioritized ranking.

---

## 3. Objective
The objective of **Checkpoint 1** is to create a clean, modern, and modular **JavaFX Desktop User Interface (UI)** and application skeleton. The UI is designed so that backend databases (MySQL/JDBC), multithreading, and ranking algorithms can be connected seamlessly in later checkpoints without redesigning the frontend.

---

## 4. Technologies Used
- **Language**: Java 21 / 17
- **UI Framework**: JavaFX (Controls, Layout Panes, Property Bindings)
- **Build & Dependency Management**: Apache Maven
- **Styling**: Custom CSS (`styles.css` with dark slate & medical crimson emergency theme)

---

## 5. Application Screens & Features

1. **Dashboard / Home**:
   - Displays application title, emergency readiness banner, and quick action buttons.
   - Summary metric cards showing Registered Donors (`125`), Available Donors (`82`), Active Requests (`4`), and Critical Requests (`2`).
2. **Emergency Blood Request**:
   - Form with Blood Group ComboBox (`A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-`), Units Required numeric input, Location, and Urgency level.
   - Includes input validation and navigates to Matching Results upon submission.
3. **Donor Registration**:
   - Form capturing Name, Age (18–65), Blood Group, Phone Number, Location, and Availability status.
   - Input validation dialogs preventing empty fields or invalid numeric inputs.
   - Appends valid donors dynamically to the shared in-memory list.
4. **Donor Management**:
   - `TableView<Donor>` displaying registered donors.
   - Supports live row selection, toggling availability status, deleting records, and refreshing table items.
5. **Matching Results**:
   - Displays active emergency request summary banner.
   - Renders ranked donor card layout showing mock priority scores, distances, response rates, and availability.

---

## 6. Project Structure

```text
EmergencyBloodMatching/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── emergencyblood/
        │           ├── Main.java
        │           ├── model/
        │           │   ├── Donor.java
        │           │   └── BloodRequest.java
        │           ├── controller/
        │           │   ├── NavigationController.java
        │           │   ├── DashboardView.java
        │           │   ├── EmergencyRequestView.java
        │           │   ├── DonorRegistrationView.java
        │           │   ├── DonorManagementView.java
        │           │   └── MatchingResultsView.java
        │           └── util/
        │               └── AlertUtil.java
        └── resources/
            └── css/
                └── styles.css
```

---

## 7. OOP Concepts Demonstrated
- **Encapsulation**: Private JavaFX properties (`StringProperty`, `IntegerProperty`) in `Donor` and `BloodRequest` exposed via public getters/setters.
- **Model-View-Controller (MVC) Pattern**: Clean separation between data models (`model`), screen layouts (`controller`/`view`), and application entry (`Main`).
- **Polymorphism & JavaFX Layout Nodes**: Reusable UI node construction (`BorderPane`, `VBox`, `GridPane`, `TableView`).

---

## 8. Exception Handling & Input Validation
- **Numeric Validation**: Intercepts `NumberFormatException` when parsing age or units required, displaying user-friendly JavaFX `Alert` dialogs instead of crashing.
- **Range & Form Guards**: Validates donor age range (18–65), positive units requirement, non-empty text fields, and selected ComboBox items.

---

## 9. Mock Data Explanation
For Checkpoint 1, all donor records and request summaries use **in-memory mock data** stored in an `ObservableList<Donor>`. No real patient or donor data is used.

---

## 10. How to Run the Application

Using Maven from the project root directory (`EmergencyBloodMatching`):

```bash
mvn clean compile javafx:run
```

Or using `mvn exec:java`:

```bash
mvn compile exec:java -Dexec.mainClass="com.emergencyblood.Main"
```

---

## 11. Intentionally NOT Implemented Yet (Planned for Future Checkpoints)

The following backend features are **explicitly deferred** to Checkpoint 2 and beyond:
- ❌ **MySQL & JDBC**: Persistent database integration (currently in-memory `ObservableList`).
- ❌ **Multithreading**: Concurrent emergency request background workers.
- ❌ **Real Donor Matching Engine**: Dynamic distance & priority score calculation algorithms.
- ❌ **REST / Web APIs / Sockets**: External network integration.
