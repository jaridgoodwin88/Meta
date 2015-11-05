# Meta

A typesafe meta model for Java.

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

To get an Optional for the street, using reflection, you'll have to write:

```java
    Customer customer = ...
    if (customer == null)
        return Optional.empty();
    Field addressField = Customer.class.getDeclaredField("address");
    addressField.setAccessible(true);
    Address address = (Address) addressField.get(customer);
    if (address == null)
        return Optional.empty();
    Field streetField = Address.class.getDeclaredField("street");
    streetField.setAccessible(true);
    String street = (String) streetField.get(address);
    return Optional.ofNullable(street);
```

With this project, you can write:

```java
    Customer customer = ...
    Optional<String> street = customerProperties().address().street().get(customer);
```

This is not only less code to read, it's also typesafe and refactoring-safe.

(You can see this code in the [ReadmeTest](src/test/java/com/github/t1/meta/test/ReadmeTest.java))

## Setup

For maven/gradle/etc., it's trivial: Just include a dependency on `com.github.t1:meta` and you're ready to go. 

For Eclipse, you can generally enable annotation processing in the `Project Properties -> Java Compiler -> Annotation Processing`
and set the `Generated Source Directory` to `target/generated-test-sources/test-annotations`. To enable `meta`, go to
`Project Properties -> Java Compiler -> Annotation Processing -> Factory Path` and add a variable
`M2_REPO/com/github/t1/meta/<version>/meta-<version>.jar`, where `<version>` is the current version of `meta`.

## Lobok

If you use `meta` together with [lombok](http://projectlombok.org), the execution order may be quite random,
and things may not work out. You'll have to `delombok` your project
(see [this question on stackoverflow](http://stackoverflow.com/questions/29193806/specifying-order-of-annotation-processors) for details).


