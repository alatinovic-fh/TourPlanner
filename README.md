# TourPlanner (Latinovic, Asuncion)

TourPlanner ist eine JavaFX-Anwendung zur Verwalten von Touren.
Die Anwendung folgt dem MVVM-Architekturmuster und ermöglicht es Benutzer:innen, Touren zu erstellen,
zu bearbeiten, zu löschen und mit detaillierten TourLogs zu versehen.

## Requirements
```
Java-Version: 21
```

## UX Documentation

This section describes the User Experience (UX) of the TourPlanner application.
It provides an overview of the UI elements and illustrates how users can interact with the application.

### Wireframe UI elements
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
 

### User/Interactive Flow Wireframe  
![Pic Wireframe](TPWireframe.png)






# HOWTO - Integrate Spring Boot into a JavaFX application
- JavaFX is a module-based application, with strong limitations on access.
- Spring Boot (on default) does not use modules - as most frameworks don't do

## I. Integrate SpringBoot
Step-by-step guide

[x] 1. Update [pom.xml](pom.xml) - dependencies, plugins,..
    - add dependencyManagement from spring-boot
    - add spring boot starters
    - add spring boot plugings

   run `mvn package` to check if it still works

[x] 2. provide the [docker-compose.yml](docker-compose.yml) containing the postgresql db engine

   start the container `docker compose up -d`

   Create a database for your application, which is used in the JDBC-URL later.

[x] 3. create [application.properties](src/main/resources/application.properties) in the resources directory
   containing the datasource configuration

   ATTENTION: Ensure that the URL is working correctly.

[x] 4. create a [TaskPlannerConfig](src/main/java/at/fhtw/bif/swen/taskplannerfx/TaskPlannerConfig.java) class
   in order to tell spring-boot where to look for its classes

[x] 5. Update the [TaskPlannerApplication](src/main/java/at/fhtw/bif/swen/taskplannerfx/TaskPlannerApplication.java) class to manually create and destroy the spring boot context.

    - attribute `springContext`
    - initialized in `init()`
    - cleaned up in `stop()`

[x] 6. Update the [modules-info.java](/src/main/java/module-info.java) and add the corresponding `requires` for spring boot.

   Try if ok with `mvn compile`. Resolve all warnings!


[] 7. Add the `contextLoads` test

   Check it out with `mvn test`.

## II. Refactor application
in order to use Spring Boot DAL

1. Refactor model --> entity

    - use class instead of record
    - add spring jpa entity annotations `@Entity`, `@Id`,...

   Attentions: it should compile (`mvn compile`) but it won't run in that stage.

2. Create repository interface, extends `JpaRepository<>` and is annotated with `@Repository`

3. Refactor service, inject repository and annotate it with `@Service`

4. Annotate controller classes in view package with `@Controller`

5. Annotate viewmodel classes in viewmodel package with `@Component`

6. Refactor application class, in order to let spring-boot inject the "real" instances

   --> get rid of creation for service, view (controller classes), and viewmodels.

7. Add missing requires, opens in module-info.java

    - don't forget: org.hibernate.orm.core

# REST API - TO DO list
1. save Json File
2. connect that Json to the Leaflet
3. save Json into DB or just reconnect to API for every call? 
4. 