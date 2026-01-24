A) Project Overview
This Java API manages a digital game store with PostgreSQL. It supports games, users, and libraries
Entities:Game (abstract) – base for all games
DigitalGame – digital games with download size
PhysicalGame – physical games with disc count
User – store customers
Library – user game collections

B) OOP Design Documentation
Abstract Class and Subclasses
Game – abstract class with common fields
DigitalGame – adds platform and downloadSize
PhysicalGame – adds platform and discCount
Interfaces
1)Validatable – validate() method
2)PricedItem – getPrice() and applyDiscount()
Composition
Library contains User and Game objects

D)Controller (Main Class)
Operations:
List all games
Add new game (digital/physical)
Update game
Delete game
List users
Add user
Add game to user library
View user library
Demonstrate polymorphism
Demonstrate interfaces
Test validation

E)Instructions to Compile and Run
Requirements:
Java 17+
PostgreSQL 14+
PostgreSQL JDBC driver
Database Setup:
Create database
Execute schema.sql
Update DatabaseConnection credentials

G)Reflection
Learned:
Multi-layer architecture
JDBC with PreparedStatement
Custom exception hierarchies
Practical OOP application
PostgreSQL integration

Challenges:
Object-relational mapping
Database connection management
Validation implementation
Complex relationships

Benefits:
Clear separation of concerns
Easy feature expansion
Independent layer testing
Professional structure







All scrrenshots in package docs!










