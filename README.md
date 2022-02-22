# junit4-to-junit5-converter

Simple line-to-line text converter. Does not update Maven pom.xml or Gradle build junit dependency version (change to JUnit 5 by yourself)

## Usage

```
mvn clean package shade:shade
java -jar ./target/junit4-to-junit5-converter-0.1.4.jar

Usage: 1st argument must be file or directory.
2nd can be 'r' to enable scanning a directory recursively (optional).
Quitting.

java -jar ./target/junit4-to-junit5-converter-0.1.4.jar <path_to_your_file_or_folder> <r (optional)>

```

## Program arguments
| Argument        | Description       |
| --------------- |:-----------------:|
| /path/to/your/file/or/folder        |
| r        | Scan a folder recursively|

## IDE
It is possible to run class 'com.aleck.Main' with arguments from your IDE
