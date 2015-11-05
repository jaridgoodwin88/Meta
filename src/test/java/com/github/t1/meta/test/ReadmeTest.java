package com.github.t1.meta.test;

import static com.github.t1.meta.test.ReadmeTest_CustomerProperties.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.*;

import com.github.t1.meta.GenerateMeta;

/** This is the sample code for the README.md */
public class ReadmeTest {
    @GenerateMeta
    public class Customer {
        public Address address;
    }

    public class Address {
        public String street;
    }

    Customer customer = new Customer();

    @Before
    public void setup() {
        customer.address = new Address();
        customer.address.street = "foo";
    }

    @Test
    public void shouldGetAddressViaReflection() throws Exception {
        Optional<String> address = getAddressViaReflection(customer);

        assertThat(address).contains("foo");
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

    @Test
    public void shouldGetAddressViaMeta() {
        // if you have compiler errors here, then the meta generator is not properly configured. see README.md
        Optional<String> street = readmeTest_CustomerProperties().address().street().get(customer);

        assertThat(street).contains("foo");
    }
}
