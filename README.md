Eclectic-ORC
=====

Eclectic-ORC is a Java object writer for creating ORC files by simply annotating your class files as necessary. 
The framework uses runtime code generation to create a fast customized ORC writer taking care of all the details. 

## Feature Highlights

- Declarative Schema Definition
- Annotated column specification
   - Use custom `@Orc` annotations or
   - Use existing JPA `@Column` annotations

# Getting Started

Download the eclectic-orc jar from Maven central:

```
    <dependency>
	    <groupId>com.eclecticlogic</groupId>
	    <artifactId>eclectic-orc</artifactId>
	    <version>1.0.0</version>
    </dependency>
```

Minimum dependencies that you need to provide in your application:

1. Java 8 or above (the design leverages method references and lambdas extensively)
1. slf4j (over logback or log4j) v1.7.23 or higher

## A simple example

Consider a simple class that you want to serialize to an ORC file:
  
```java
    public class Student {
        int year;
        String name;
        
        public String getName() {
            ...
        }
        
        public int getYear() {
            ...
        } 
        
        ...
    }    
```  

To write a collection of Students to an ORC file, you first have to provide a schema definition. 
The eclectic-orc library makes doing this trivially easy:

```java
    import com.eclecticlogic.orc.Factory;
    import com.eclecticlogic.orc.Schema;

    ...
    
    public void schemaSetup() {
        Schema<Student> schema = Factory.createSchema(Student.class)
            .column(Student::getName) //
            .column(Student::getYear);
    }
```

The above schema definition implicitly does three things:
1. It defines the order of the columns (first name then year)
1. It defines the data types of the columns (String, int)
1. It defines the names of the columns (name, year)

The library allows you to customize aspects of the schema. Let us start with column names. 
If you wanted your column to be called 'graduationYear' instead of year, simply change the 
schema column definition. 

```java
        Schema<Student> schema = Factory.createSchema(Student.class)
            .column(Student::getName) //
            .column("graduationYear", Student::getYear);
```

You can also define columns based on properties of other classes that you reference. 
So if the `Student` class referenced a Club class as shown below:

```java

    public class Club {
        String name;
        
        public String sanitizedClubName() {
            return ...
        }
    }

    public class Student {
        Club club;
        
        public Club getClub() {
            return club;
        }
    }
```
You can reference the club name in your schema definition by chaining the call as `getClub().sanitizedClubName()`.
The astute reader would have noticed that sanitizedClubName() is not a java-bean compliant getter. That is right. 
eclectic-orc does not restrict you to just java-bean getters. Any method that takes no parameters and returns a non-void
type can be used for a column definition. A schema to incorporate the above definition would look like this.

```java
    Schema<Student> schema = Factory.createSchema(Student.class)
                .column(Student::getName) //
                .column("graduationYear", Student::getYear)
                .column(it -> it.getClub().sanitizedClubName());
```
We've now defined a third column of type `String` and given it an implicit name of "santitizedClubName." Of course, just like
before you can choose to change the name to something else. The same definition in Groovy could be written as:
 
```groovy
    Schema schema = Factory.createSchema(Student)
                .column { it.name }
                .column('graduationYear') { it.year }
                .column { it.club.santizedClubName() }
``` 

 To write a collection of `Student` objects, we simply create an OrcHandle reference, configure it, open it to get an OrcWriter 
 reference and write our collection.
  
```java
    import org.apache.hadoop.fs.Path
     
    // First get an OrcHandle reference.
    OrcHandle<Student> handle = Factory.createWriter(schema);
    // Customize it by calling one of the withXYZ() methods. This is optional as defaults are provided.
    
    // Create an OrcWriter by calling open.
    Path path = new Path("/home/kabram/temp/dp/graduate.orc");
    
    OrcWriter<Student> writer = handle.open(path);
    List<Student> students = ...
    // The write method may be called multiple times if you are retrieving objects in batches.
    writer.write(students);
    writer.close();
```  

In simple cases, the above code can be written as:

```java
    Factory.createWriter(schema) //
        .open(new Path("/home/kabram/temp/dp/graduate.orc")) //
        .write(students) //
        .close();
```

### Data Type Support

The following data types are **supported** in the current release:

1. Java primitive types - `boolean`, `char`, `byte`, `short`, `int`, `long`, `float`, `double`. These map to their corresponding counterparts 
with the exception of `char` which maps to `varchar(1)` The exception for `char` is because AWS Athena is currently unable to handle char column types.
2. BigDecimal mapping to ORC Decimal type.
3. LocalDate mapping to ORC Date type.
4. Date, LocalDateTime, ZonedDateTime mapping to ORC Timestamp type unless there is either a JPA @Temporal or @OrcTemporal annotation
that defines the TemporalType (or OrcTemporalType) as DATE.
5. String mapping to ORC string type.
6. List mapping to ORC List type, currently supporting only simple types as the member. See below for how to use lists.

The following data types are **not supported** in the current release:
1. Binary data type.
2. Map
3. Union
4. Sub-structures (Struct within your table, map of structs, list of structs, etc.)

#### Special cases

##### String length specification

To specify the number of characters for a String column type, simply use the @Orc annotation. If the framework finds 
an existing JPA @Column annotation, it will use the length property of that as well. If both annotations are present, 
the @Orc annotation takes precedence. The @Orc annotation is only supported on methods.

```java
    public class Student {
        String name;
        
        @Orc(length = 50)
        public String getName() {
            return name;
        }
    }
```

##### Decimal precision/scale specification

You can also specify the precision and scale of BigDecimal data type by using the JPA @Column or @Orc annotations. 
By default, the precision is 38 and scale is 10. This can be changed via annotation:

```java
    public class Employee {
        BigDecimal salary;
        
        @Orc(precision = 10, scale = 2)
        public BigDecimal getSalary() {
            
        }        
    }
```

##### Converting data types

There may be times you want to write a data type that is not a supported type. For example, you may have a birthday property
that only records the year and month using the `java.time.YearMonth` class. You can handle these column types by defining a type
converter, a class that implements the `Converter` interface. In our example, to convert `YearMonth` to `LocalDate`, 
defaulting to the first day of the month, we could write:
  
```java
    public class YearMonthConverter implements Converter<YearMonth, LocalDate> {
    
        @Override
        public Class<LocalDate> getConvertedClass() {
            return LocalDate.class;
        }
    
    
        @Override
        public LocalDate convert(YearMonth yearMonth) {
            return yearMonth.atDay(1);
        }
    }
```

We can now annotate the YearMonth accessor with the `@OrcConverter` annotation:

```java
    public class Employee {
        YearMonth birthday;
        
        @OrcConverter(YearMonthConverter.class)
        public YearMonth getBirthday() {
            ...
        }
    }
    
    ...
    
    Schema<Employee> schema = Factory.createSchema(Employee.class) //
                    .column(Employee::getBirthday) // This is now a LocalDate data type.
```

##### Java Enum
Java Enums require special handling to convert them to a specific data type. There are three ways to handle enums.
1. Do nothing: If your schema column is an `Enum` derivative, then the column will be treated as a `String` with the `name()`
method being called to get the value.
1. Annotation: Annotate a custom enum method with `@Orc`. If you have a method in your `Enum` class that provides the value 
you would like to store, you can add the `@Orc` annotation to it.
1. Converter: Annotate your accessor method that returns an `Enum` with `@OrcConverter` specifying a converter that takes your 
enum and returns a supported data-type.
 
##### Handling lists 

Eclectic-orc supports creation of list columns that can hold a single scalar data type. To include a list column in the schema 
definition, annotate the accessor method with the @OrcList annotation. Strictly speaking, any derivative of java.lang.Iterable
is supported. The @OrcList annotation requires you to specify the Class of the entries of the Iterable. This is because the 
type information is lost at runtime due to type-erasure. You also need to specify the average number of entries you expect to 
 see in the list. This is a technical implementation detail due to the way lists are stored in ORC files. Finally, there is 
 a converter attribute you can use to convert each item of the Iterable to a different type. Note: If you annotate the list 
 accessor with @OrcConverer, you will be modifying the List/Iterable itself into some other data type. 
  
  If your Iterable consists of Enum instances, the existing strategy for enums is automatically used - using an enum method 
  annotated with @Orc or calling name().  
 

# Release Notes

### 1.0.0

- Initial release