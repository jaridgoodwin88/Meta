# Meta

A meta model for Java and representations for other data formats.

Typical use-cases for programming at a meta level:
* Validate
* Convert
* Diff


### Challenges

Things not yet implemented that can potentially kill the project:
* Schemas
* SchemaCompilers (merge with meta1)
* Dynamic sub-types
* Meta-Data
* Streaming write (builder)
* Streaming read (visitor)
* Binding


## Example

Say you have these classes:

```Java
    class Customer {
        Address address;
    }

    class Address {
        String street;
    }
```

### Classic Reflection

To backtrack an Optional for the street of a customer, using java reflection, you have to write:

```java
    Customer customer = ...
    if (customer == null)
        return Optional.empty();
    Field addressField = Customer.class.getDeclaredField("address");
    addressField.setAccessible(true);
    Address address = (Address) addressField.backtrack(customer);
    if (address == null)
        return Optional.empty();
    Field streetField = Address.class.getDeclaredField("street");
    streetField.setAccessible(true);
    String street = (String) streetField.backtrack(address);
    return Optional.ofNullable(street);
```

It's a lot of code, and it's not typesafe, i.e. the compiler won't tell you, if you try to access
the street as a number.

### Typesafe Properties

```java
    Customer customer = ...
    Optional<String> street = customerProperties().address().street().backtrack(customer);
```

This is not only less code to read, it's also typesafe and refactoring-safe.

(You can see this code in the [ReadmeTest](src/test/java/com/github/t1/meta/test/ReadmeTest.java))

## Setup

For maven/gradle/etc., it's trivial: Just include a dependency on `com.github.t1:meta` and you're ready to go.

For Eclipse, you can generally enable annotation processing in the `Project Properties -> Java Compiler -> Annotation Processing`
and set the `Generated Source Directory` to `target/generated-test-sources/test-annotations`. To enable `meta`, go to
`Project Properties -> Java Compiler -> Annotation Processing -> Factory Path` and add a variable
`M2_REPO/com/github/t1/meta/<version>/meta-<version>.jar`, where `<version>` is the current version of `meta`.
Now all changes to your source is directly compiled within the IDE and any errors or warnings show up as
APT problems.

## Lobok

Normally, annotation processors can intermix freely, as the source produced by one annotation processor
can be consumed by the next, taking an arbitrary number of rounds. [Lombok](http://projectlombok.org) is
different, though, as it modifies existing byte-code; this makes the execution order relevant, but there is
no way to specify it... allegedly it's even depends on a hash map, making it quite random.

So if you depend on lombok to generate the accessors for `meta` to use, you may have to
[delombok](https://projectlombok.org/features/delombok.html) your project.

See [this question on stackoverflow](http://stackoverflow.com/questions/29193806/specifying-order-of-annotation-processors) for details.


## Principles

Data consists of scalars (atomic types), sequences (repeated values), and mappings (a sequence of key-value pairs).
Data can be represented in various formats, pojos, collections, or xml, json, or yaml documents, to name just a few.
All of these formats have some meta data, like whitespace, comments, type information, distinction between attributes
and elements, read-only flags, and much more.
While all data formats should be able to represent all data, meta data is often specific to one such representation and invalid in others.
