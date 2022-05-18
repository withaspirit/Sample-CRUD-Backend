# Sample CRUD Database

A project for Shopify's Fall 2022 Backend Engineer Intern Challenge. This project involves independently creating a simple CRUD application. The additional requirement satisfied for the challenge is: `When deleting, allow deletion comments and undeletion`.

## How to Run

The app has been prepared to run without the user installing anything with an online IDE called `Replit.` For more details on how to run the app this way, see the [Replit Instructions](#replit-instructions) section.

Instructions on how to run the app from terminal are provided in [General Instructions](#general-instructions).

### Replit Instructions

[![Run on Repl.it](https://repl.it/badge/github/cyberphoria/Sample-CRUD-Backend)](https://replit.com/@liamtripp/Sample-CRUD-Backend)

The badge above directs the user to the app on Replit. Once there, click the large green button. The app will ask in the `Shell` tab for user confirmation before proceeding. Various packages will then be downloaded before the application begins. 

See [Application Commands](#application-commands) for the available commands in the application. See [Shell Commands](#shell-commands) for commands to interact with the shell.

### General Instructions

The app requires at least [JDK 17](https://www.oracle.com/java/technologies/downloads/) and [Maven](https://maven.apache.org/download.cgi) 3.8.x to run. [Graphviz](https://graphviz.org/download/) must also be installed for a plugin this app uses. Once those are installed, the app can be run manually by executing the two commands below in a terminal navigated to the project's folder. To navigate in terminal to the project's folder, obtain the full file path to the project folder, and enter `cd filePathToProjectFolder` in terminal. Click [here](https://en.wikipedia.org/wiki/Cd_(command)#Usage) for more information about what `cd` is. For more details about the commands see [Maven Commands](#maven-commands)).

```mvn clean install```

```mvn compile exec:java```

### Shell Commands

These commands are meant for a Linux shell. Note that `file` refers to `java`, `javac`, or `mvn`.

* `Ctrl+C` (keyboard) - cancel a process in action
* `kill 1` - restart the application
* `[file] -version` - check the version of a file, without the brackets
* `command -v [file]` - check the filepath of a file, without the brackets

### Maven Commands 

These commands interact with the application itself. They can be run on any operating system from the terminal navigated to the project folder. On Replit, the shell is already in the project folder and the first two commands below are done automatically with the green 'run' button.

* `mvn clean install` - download the packages for the app to Replit
* `mvn compile exec:java` - execute the application
* `mvn test` - run the app's unit tests

### Application Commands

The user can use these commands while the application is running. The square brackets should be omitted.

* `CREATE [name] [dollar.cents] [stock]` - insert a row into the table `items`. The attribute `name` must be one word with alphanumeric characters

* `READ [tableName]` - view the rows from one of the following tables: `items`, `deleted_items`

* `UPDATE [id] [columnName] = [value]` - update a value corresponding to a column name in the table items. Text values must be quoted like 'this' (ex: `update name = 'GreenFresh'`)

* `DELETE [id] [optionalComment]` - delete a row in the table `items` while providing an optional comment

* `RESTORE [id]` - restores a row with the provided id to its corresponding table

* `HELP` - view the list of valid commands

* `TABLES` - view the list of tables

* `QUIT` - exit the application

Note that the `UPDATE` command is limited to updating one value on one item at a time.

## Design

### Overview

This application simulates an online store manager. It currently lacks integration as a web application. My experience with frontend languages like JS and Python is limited to scripting, so I chose to simulate a web application with Java.

The design pattern used for the GUI is [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter). This separates responsibilities for different functions among different classes.

[SQLite]((https://github.com/xerial/sqlite-jdbc)) is used as a database because it required less time to configure.

See [Classes](#classes) for more details on how the application is designed. See [Tests](#tests) for descriptions of the unit tests performed. See [Technologies](#technologies) for some plugins used.

### Details

The method to run this application is found in the file `DatabaseBackend`. It begins a loop that prompts the user for input in the form of a [Command](#application-commands) and displays an appropriate output. This loop continues until the user enters `QUIT`.

The tables for this project are `items` and `deleted_items`. Both contain `Items`, which each have an id, name, price, and stock. Id is specified by the SQL database. `DeletedItem`, a subclass of Item, may contain an optional comment.

When an Item is deleted from a table using the `DELETE` Command, it is inserted into the `deleted_items` table. The `RESTORE` Command deletes the item from the `deleted_items` table, returning the DeletedItem to its original table as an item.

Commands are processed by pattern-matching and group capturing with Regex. The captured input groups are passed and formatted for the SQL database before being executed. The Regex group capturing limits the the application to whatever is hard-coded. It favors security by disallowing input that doesn't match the required format. 

### Classes

The class descriptions and UML diagrams below provide an overview of the system. They were generated using the [UMLDoclet](https://github.com/talsma-ict/umldoclet) plugin (more details under [Technologies](#technologies)).

#### Class Descriptions

All classes and almost all methods are fully documented. Below are the class descriptions.

<img src="images/all_classes.png" alt="Class Descriptions">

#### Package Dependencies

This diagram illustarates the relationship between the application's packages. org.json.simple is a [dependency](#technology).

<img src="images/package-dependencies.png" alt="Package Overview">

### UML Class Diagrams

The packages for the project are divided amongst the `view`, `presenter`, and `model` per the Model-View-Presenter design pattern. `backend` contains the main method.

<details>
  <summary><b>Show Package Diagrams</b></summary>

#### Backend

<img src="images/backend.png" alt="Backend">

#### View

<img src="images/view.png" alt="View">

#### Presenter

<img src="images/presenter.png" alt="Presenter">

#### Model

<img src="images/model.png" alt="Model">

</details>

## Resources

* `DDL.sql` - contains the SQL statements used to define the database schema
* `items.json` - contains the information used to populate the table `items`
* `testUserInputs.json` - contains valid and invalid inputs that a user might enter. Used for testing with `CommandTest` (see below)

## Tests

Rigorous unit testing was used throughout development to verify application functions. Below is a descriptions of the test files

 * `DatabaseTest` ensures the Database's CRUD methods work properly
 * `DatabasePresenterTest` ensures the DatabasePresenter's CRUD methods work properly with the Database
 * `DatabaseCLITest` ensures that DatabaseCLI's CRUD methods work properly with the DatabasePresenter
 * `CommandTest` ensures that Command's search method work properly with the inputs found in `testUserInputs.json`
 * `ItemTest` ensures that Item's price conversion methods work properly
 * `InputFileReaderTest` ensures that inputs files are read properly

## Technologies

As this project is managed with [Maven](https://maven.apache.org/), the plugins and dependencies used are contained in the file `pom.xml`. Alternatively, an up-to-date list of dependencies can be found [on GitHub](https://github.com/cyberphoria/Sample-CRUD-Backend/network/dependencies). However, it does not include plugins.

If the user is running the application with the [General Instructions](#general-instructions], full API documentation including UML Class Diagrams can be generated using UMLDoclet. 

To use UMLDoclet, [Graphviz](https://graphviz.org/download/) must be installed. To activate it, run `mvn install` in terminal. The documents generated are located in the folder `target/apidocs`.

- [SQLite](https://github.com/xerial/sqlite-jdbc) - a relational database that is self-contained, meaning it does not require as much client-server configuration as MySQL or PostgreSQL
- [JSONSimple](https://github.com/fangyidong/json-simple) - JSON file manipulation
- [UMLDoclet](https://github.com/talsma-ict/umldoclet) - generates interactive Javadoc pages and UML Class Diagrams for packages. It is executed when the user runs ```mvn install```

## Project Demonstrates:

- Emphasis on unit testing to verify application functions 
- Thorough technical and user documentation
- Knowledge of SQL
- Application of design patterns (specifically, [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)
- Application of error detection and handling
- Sample of coding and development style
- Knowledge of GitHub

### Takeaways 

In hindsight, the Presenter's methods could have returned Strings formatted for the View instead of returning Items. Overall, I am satisfied with this project. It would be interesting to do a similar application with more frontend technologies.
