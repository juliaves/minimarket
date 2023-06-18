# Minimarket workshop back-end project
This project was created for the _'Debugging and clean code'_ workshop.

## Installation

This project was developed using Java v11, Spring boot with H2 in-memory database and Gradle.

Firstly install: [Java v11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)  and [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows)

If you have Git installed then clone the project:
```  
git clone https://github.com/juliaves/minimarket.git  
```  
or download the project's [ZIP](https://github.com/juliaves/minimarket/archive/refs/heads/main.zip) from _Code_ -> _Download ZIP_.

After that import your project into IntelliJ IDEA:
_File_ -> _New_ -> _Project from Existing Sources..._ -> choose cloned or downloaded _minimarket_ project folder -> _OK_ -> choose _Import project from external model_ and _Gradle_ -> _Create_

When project has been imported to IntelliJ IDEA and all dependencies installations completed, then run the application in debug mode:
Navigate to _src/main/java/MinimarketApplication_ class file, right-click it and choose _Debug 'MinimarketApplication'_

Wait till the application server starts at `http://localhost:8085/`.