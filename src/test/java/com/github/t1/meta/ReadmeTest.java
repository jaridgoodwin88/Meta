package com.github.t1.meta;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.Test;

public class ReadmeTest {
    class Customer {
        Address address;
    }

    class Address {
        String street;
    }

    @Test
    public void shouldGetAddressViaReflection() throws Exception {
        Customer customer = new Customer();
        customer.address = new Address();
        customer.address.street = "foo";

        assertThat(getAddressViaReflection(customer)).contains("foo");
    }

    private Optional<String> getAddressViaReflection(Customer customer) throws ReflectiveOperationException {
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
    }
}
