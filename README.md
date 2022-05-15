# Sample CRUD Database

A project satisfying the requirements for Shopify's Fall 2022 Backend Engineer Intern Challenge of independently creating a simple CRUD application. The additional requirement satisfied for this submission is: `When deleting, allow deletion comments and undeletion`.

### Prerequisites

This project requires at least [JDK17](https://www.oracle.com/java/technologies/downloads/) and [Maven](https://maven.apache.org/download.cgi) 3.8.x to run.

### Project Demonstrates:

- Knowledge of SQL
- Application of basic design patterns (specifically, [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)
- Emphasis on unit testing to verify program functions
- Knowledge of GitHub
- Extensive technical documentation
- Error detection and handling
- Coding style

### Design

This program emulates an online store manager. The method to run this program is found in the file `DatabaseBackend`. Running it begins a loop that prompts the user for input in the form of a Command (see below) and displays an appropriate output. This loop continues until the user enters the Command `QUIT`.

The design pattern used is [Model-View-Presenter](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter). This separates concerns associated with the client and server side of the application.

The tables for this project are `items` and `deleted_items`. Both contain Items which each have an id, name, price, and stock. DeletedItem, a subclass of Item, may contain an optional comment.

When an Item is deleted from a table using the `DELETE` command, it is inserted into the `deleted_items` table. The `RESTORE` command  deletes it from the `deleted_items` table and returns the DeletedItem to its original table.

Commands are captured by pattern-matching with Regex. The captured inputs are passed and formatted for an SQL command before being executed. The Regex capturing also prevents the user from entering a disallowed input. However, it also limits the user's use of the program to whatever is hard-coded. 

### Commands

A list of commands the user may enter is provided below. The square brackets should be omitted.

* `CREATE [name] [dollar.cents] [stock]` - insert a row into the table `items`

* `READ [tableName]` - view the rows from one of the following tables: `items`, `deleted_items`

* `UPDATE [id] [columnName] = [value]` - update a value corresponding to a column name in the table items. Text values must be quoted like 'this'

* `DELETE [id] [optionalComment]` - delete a row in the table `items` while providing an optional comment

* `RESTORE [id]` - restores a row with the provided id to its corresponding table

* `HELP` - view the list of valid commands

* `TABLES` - view the list of tables

* `QUIT` - exit the command-line interface

The `UPDATE` command is currently limited to updating one value on one item at a time.

### Resources

- `DDL.sql` contains the SQL statements used to define the database schema.
- `items.json` contains the information used to populate the table `items`.
- `testUserInpust.json` - contains valid and invalid inputs that a user might enter. Used for testing with CommandTest.

### Tests

Rigorous unit testing that was used throughout development to verify project functions. Below is a descriptions of the test files.

 - `DatabaseTest` ensures the Database's CRUD methods work properly.
 - `DatabasePresenterTest` ensures the DatabasePresenter's CRUD methods work properly with the Database.
 - `DatabaseCLITest` ensures that DatabaseCLI's CRUD methods work properly with the DatabasePresenter.
 - `CommandTest` ensures that Command's search method work properly with the inputs found in `testUserInputs.json`.
 - `ItemTest` ensures that Item's price conversion methods work properly.
 - `InputFileReaderTest` ensures that inputs files are read properly.

### Technologies

As this project is managed with [Maven](https://maven.apache.org/), the plugins and dependencies and plugins used are contained in the file `pom.xml`. Alternatively, an up-to-date list of dependencies can be found [on GitHub](https://github.com/cyberphoria/Sample-CRUD-Backend/network/dependencies). However, it does not include plugins.

- [SQLite](https://github.com/xerial/sqlite-jdbc) - a relational database that is self-contained, meaning it does not require client-server configuration like MySQL or PostgreSQL
- [JSONSimple](https://github.com/fangyidong/json-simple) - JSON file manipulation
- [UMLDoclet](https://github.com/talsma-ict/umldoclet) - that generates interactive Javadoc pages and UML Diagrams for packages. It is executed when the user runs ```mvn install```

It currently lacks integration as a web application. My background is mainly in backend-type languages. Therefore, I built a Java command-line application instead.
