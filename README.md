# Sample CRUD Database

This is project is configured with Maven. The language used is Java.

The additional requirement satisfied for this submission is: `When deleting, allow deletion comments and undeletion.`

This currently lacks integration as a web application. My background is mainly in backend-type languages. Therefore I built a Java command-line application instead. It demonstrates knowledge of SQL, design patterns provide a quality coding style example.

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

Testing is done with JUnit 5. Below are descriptions of the test files.

 - `DatabaseTest` ensures the Database's CRUD methods work properly.
 - `DatabasePresenterTest` ensures the DatabasePresenter's CRUD methods work properly with the Database.
 - `DatabaseCLITest` ensures that DatabaseCLI's CRUD methods work properly with the DatabasePresenter.
 - `CommandTest` ensures that Command's search method work properly with the inputs found in testUserInputs.json.
 - `ItemTest` ensures that Item's price conversion methods work properly.
 - `InputFileReaderTest` ensures that inputs files are read properly.

### Plugins and Dependencies

The most up-to-date list of dependencies can be found [here](https://github.com/cyberphoria/Sample-CRUD-Backend/network/dependencies). Provided below are the important ones.

- [SQLite](https://github.com/xerial/sqlite-jdbc) - a relational database that is self-contained, meaning it does not require client-server configuration like MySQL or PostgreSQL
- [JSONSimple](https://github.com/fangyidong/json-simple) - JSON file manipulation
- [UMLDoclet](https://github.com/talsma-ict/umldoclet) - that generates Javadoc pages and UML Diagrams for packages. It is executed when the user runs ```mvn install```
