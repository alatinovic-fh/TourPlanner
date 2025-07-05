# TourPlanner (Latinovic, Asuncion)

TourPlanner ist eine JavaFX-Anwendung zur Verwalten von Touren.
Die Anwendung folgt dem MVVM-Architekturmuster und ermöglicht es Benutzer:innen, Touren zu erstellen,
zu bearbeiten, zu löschen und mit detaillierten TourLogs zu versehen.

## Requirements
```
* Version: 21
* JavaFX-web maybe has to be added manually 
    -IntelliJ Maven-Dependencies-(right click javafx-web)-Download sources

```

# Architecture
With the TourPlanner, we aimed to implement a JavaFX-based desktop application, enhanced by a Spring Boot backend that orchestrates the application’s overall logic and dependency management. This hybrid approach leverages the strengths of both frameworks: JavaFX for building a responsive GUI and Spring Boot for structuring backend services, managing configurations, and ensuring dependency injection.

We placed a strong emphasis on adhering to SOLID principles throughout the development process, resulting in a modular and maintainable codebase. Furthermore, the application design follows the MVVM (Model-View-ViewModel) pattern, ensuring a clean separation between the UI (View), the data-binding and UI logic (ViewModel), and the business logic.

The project is structured using a clear and conventional package organization, typical for Spring Boot applications:

* config: Handles application configuration such as bean definitions, data source settings, or other Spring-related setup.

* integration: Deals with external APIs or services (e.g., routing, mapping, or file exports).

* persistence: Manages data access logic, repositories, and database entities.

* service: Contains business logic and core service classes that are used by the ViewModels.

* view: Includes the JavaFX UI components controller classes.

* viewmodel: Implements the MVVM bridge between the views and the service layer, providing bindings and observable properties.

![UML-TourPlanner](UML.png)

# Use-cases

# UX Documentation

This section describes the User Experience (UX) of the TourPlanner application.
It provides an overview of the UI elements and illustrates how users can interact with the application.

## Wireframe UI elements
![Picture UI elements](TPUIelements.png)

| Label               | Description                                              |
| ------------------- |----------------------------------------------------------|
| **A FILE**          | Menu button to create a new tour or exit the application |
| **B EDIT**          | – *(currently unused)*                                   |
| **C OPTIONS**       | – *(currently unused)*                                   |
| **D HELP**          | – *(currently unused)*                                   |
| **E SEARCH**        | Text field to search for any a tour                      |
| **F List of Tours** | Displays all saved tours                                 |

#### G GENERAL Tab where the details of a selected Tour will be shown  
| Element            | Description                                                                                                                                                                                                                                      |
|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **H WebView**      | Displays a map (Leaflet) of the selected tour                                                                                                                                                                                                    |
| **I Tour Details** | Displays information about the selected tour:<br>– Tour Name<br>– Description<br>– From: Starting point<br>– To: Destination<br>– Transport: Type (Car, Train, None)<br>– Distance in kilometers<br>– Estimated duration based on transport type |

#### J MANAGE TOUR Tab where the details of saved Tours can be edited or saved  
| Element              | Description                                                       |
| -------------------- |-------------------------------------------------------------------|
| **K Tour Details**   | Input fields for editing tour data such as name, description, etc. |
| **L Transport Type** | Dropdown menu to select the mode of transport (CAR, NONE, TRAIN)  |
| **M ADD TOUR**       | Button to save the changes                                        |
| **N CANCEL**         | Button to cancel editing                                          |
| **O DELETE TOUR**    | Button to delete the selected tour                    |

#### P TOUR LOGS	Tab where all the logs of a selected Tour are to be found. Multiple logs can be saved for one tour.  
| Element            | Description                                                            |
|--------------------| ---------------------------------------------------------------------- |
| **Q ADD LOG**      | Button to save a newly entered tour log                                |
| **R SAVE CHANGES** | Button to save edits to a selected tour log                            |
| **S TableView**    | Displays all logs associated with the selected tour in a column format |
| **T Log Details**  | Input fields to add or update a specific tour log                      |
 

## User/Interactive Flow Wireframe  
![Pic Wireframe](TPWireframe.png)


# Library decisions
In this section the library used in this project are explained
## Text (PDF Generation
**iText (PDF Generation)**
We chose iText because it provides a powerful and flexible API for generating and manipulating PDF documents programmatically.

Why did we use this:
* Fine-grained control over layout and styling.
* Reliable and well-documented.

Supports advanced features like tables, images, fonts, and metadata.
## @Slf4j (Logging with Lombok)
We use @Slf4j, a Lombok annotation, to automatically inject a Logger instance (log) into our classes using Simple Logging Facade for Java (SLF4J). This standardizes logging and makes it easy to output debug, info, or error messages without manual logger setup.

Why did we use this:

* Avoids manual logger instantiation (e.g., LoggerFactory.getLogger(...)).
* Encourages consistent logging practices across the project.
* Simplifies debugging and traceability of runtime behavior.

Works seamlessly with logging frameworks like Logback or Log4j.
## Lombok (Code Simplification)
We integrated Lombok to reduce boilerplate code such as getters, setters, constructors, equals, hashCode, and toString methods. It enhances code readability and maintainability by automatically generating these common methods via annotations like @Getter, @Setter, @Data, and @AllArgsConstructor.

Why did we use this:
* Cleaner and more readable codebase.
* Fewer lines of repetitive code.

# Design pattern

# Unit-Tests

# Tracked time

# See the code on Github



