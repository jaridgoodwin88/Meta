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
