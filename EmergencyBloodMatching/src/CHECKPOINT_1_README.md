# Emergency Blood Matching System (Checkpoint 1)

**Course**: CSE 213 - Advanced Java Programming  
**Institution**: SRM University-AP  
**Author**: CSE 213 Student  

---

## 1. Project Name
**Emergency Blood Matching System** (Console Application - Foundation / Checkpoint 1)

---

## 2. Problem Statement
During medical emergencies, finding compatible blood donors quickly in a specific location is critical. Often, delays occur due to manual searching, lack of structured donor records, or mismatching blood groups. The goal of this application is to store registered donors, accept emergency blood requests, and determine compatible blood donors using standard red blood cell compatibility rules.

---

## 3. Objective
The objective of **Checkpoint 1** is to create a clean, robust, and understandable Java foundation demonstrating fundamental **Object-Oriented Programming (OOP)** principles and basic **Exception Handling** using in-memory Java Collections (`ArrayList`), without external dependencies or databases.

---

## 4. Checkpoint 1 Features
- **Donor Registration**: Add donor details including Name, Age, Blood Group, Location, and Availability.
- **View Donors**: Display all currently registered donors stored in memory.
- **Emergency Blood Request Creation**: Capture emergency requests specifying Required Blood Group, Units Required, Location, and Urgency.
- **Blood Compatibility Matching**: Evaluate each donor against red-blood-cell compatibility rules and list compatible donors.
- **Robust Input Validation**: Handle non-numeric input, invalid blood groups, empty names, and out-of-range numbers gracefully without application crashes.

---

## 5. Classes and Their Responsibilities

| Class Name | Responsibilities |
|------------|------------------|
| [`Donor.java`](Donor.java) | Models a blood donor with private fields (`donorId`, `name`, `age`, `bloodGroup`, `location`, `available`), constructors, getters/setters, and display methods. |
| [`BloodRequest.java`](BloodRequest.java) | Models an emergency blood request with private fields (`requestId`, `requiredBloodGroup`, `unitsRequired`, `location`, `urgency`), constructors, and display methods. |
| [`BloodMatcher.java`](BloodMatcher.java) | Contains standalone business logic for blood group normalization, validation (`A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-`), compatibility evaluation matrix, and filtering available donors. |
| [`Main.java`](Main.java) | Provides the program entry point, interactive console menu loop, and exception-safe user input helper methods (`readInt`, `readValidBloodGroup`, `readBoolean`, `readNonEmptyString`). |

---

## 6. OOP Concepts Used
1. **Encapsulation**: Private fields in `Donor` and `BloodRequest` accessed only through public getters and setters to protect internal state.
2. **Modular Design / Separation of Concerns**: Compatibility matrix and validation logic are completely isolated in `BloodMatcher` rather than mixed inside `Main`.
3. **Abstraction & Object Interaction**: Objects interact through clean method calls (e.g., `BloodMatcher.isCompatible(request.getRequiredBloodGroup(), donor.getBloodGroup())`).
4. **Collections Framework**: Uses `ArrayList<Donor>` to store and traverse donor objects dynamically.

---

## 7. Exception Handling Used
- **`try-catch` Blocks**: Intercepts `NumberFormatException` when parsing user numeric input (e.g. entering `abc` for age or units).
- **Validation Loops**: Keeps prompting the user until valid input is received instead of crashing the program.
- **Input Normalization**: Normalizes inputs (e.g. converting `o+` or ` a- ` to `O+` and `A-`).
- **Range & Non-Empty Checks**: Ensures age is within valid limits (18-65), units required are positive, and strings are non-blank.

---

## 8. Why Inheritance is Not Used in Checkpoint 1
Inheritance was **intentionally not used** in Checkpoint 1 because forcing artificial class hierarchies (e.g., `Person` parent class for `Donor`) creates unnecessary boilerplate without providing true code reuse at this stage. Inheritance will be evaluated in future checkpoints when distinct user roles (such as `HospitalUser`, `DonorUser`, and `AdminUser`) are introduced.

---

## 9. How to Compile

Navigate to the `EmergencyBloodMatching/src` directory in your terminal or command prompt:

```bash
cd EmergencyBloodMatching/src
javac *.java
```

---

## 10. How to Run

After compiling successfully, execute:

```bash
java Main
```

---

## 11. Example Demo Flow

### Step 1: Register Donors
1. **Donor 1**: Name: `Rahul`, Age: `20`, Blood Group: `O+`, Location: `Vijayawada`, Available: `yes`
2. **Donor 2**: Name: `Kiran`, Age: `21`, Blood Group: `A+`, Location: `Vijayawada`, Available: `yes`
3. **Donor 3**: Name: `Arjun`, Age: `22`, Blood Group: `O-`, Location: `Guntur`, Available: `yes`

### Step 2: Create Emergency Request
- Blood Group: `O+`
- Units: `2`
- Location: `Vijayawada`
- Urgency: `CRITICAL`

### Step 3: Find Compatible Donors
Expected output evaluating donors:
```
Rahul (O+) -> Compatible
Kiran (A+) -> Not compatible
Arjun (O-) -> Compatible
```

---

## 12. Intentionally NOT Implemented Yet (Planned for Future Checkpoints)

The following technologies and features are **explicitly deferred** to keep Checkpoint 1 minimal and beginner-friendly for faculty viva:
- ❌ **MySQL & JDBC**: Database persistence (currently using `ArrayList<Donor>` in-memory).
- ❌ **Multithreading & Networking**: Concurrent request handling or server sockets.
- ❌ **JavaFX / Swing GUI**: Desktop user interfaces.
- ❌ **Spring Boot / Web / REST APIs**: Web application frameworks.
- ❌ **Advanced Donor Ranking**: Distance calculation, response history weighting, or map APIs.

---

## Medical Disclaimer
*This project is a simplified educational model created for the CSE 213 Advanced Java Programming coursework. It is not medically certified and must not be used for actual real-world clinical blood transfusion decisions.*
