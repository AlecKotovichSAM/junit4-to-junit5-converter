# junit4-to-junit5-converter

Simple line-to-line text converter. Does not update Maven pom.xml or Gradle build junit dependency version (change to JUnit 5 by yourself)

## Usage

```
mvn clean package shade:shade
java -jar ./target/junit4-to-junit5-converter-0.1.5.jar

Usage: 1st argument MUST be file or directory.
Optionally supply --recursive to enable scanning a directory recursively.
Quitting.

java -jar ./target/junit4-to-junit5-converter-0.1.5.jar <path_to_your_file_or_folder> <--recursive (optional)>

```

## Program arguments
| Argument        | Description       |
| --------------- |:-----------------:|
| /path/to/your/file/or/folder        |
| --recursive     | Scan a folder recursively|

## IDE
It is possible to run class 'com.aleck.Main' with arguments from your IDE
