package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.CustomerServiceModel;
import org.softuni.cardealer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CustomerServiceTests {

    private CustomerService customerService;
    private CustomerServiceModel customerForTest;

    @Autowired
    private CustomerRepository customerRepository;
    private ModelMapper modelMapper;

    @Before
    public void init() {
        this.modelMapper = new ModelMapper();
        this.customerService = new CustomerServiceImpl(this.customerRepository, this.modelMapper);
        this.customerForTest = customerForTest();
    }

    @Test
    public void customerService_saveCustomerWithCorrectValues_ReturnCorrect(){

        CustomerServiceModel actual = this.customerService.saveCustomer(customerForTest);
        CustomerServiceModel expected = this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(customerForTest.getName(), actual.getName());
        Assert.assertEquals(customerForTest.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_saveCustomerWithNullValues_ThrowException(){
        customerForTest.setName(null);
        customerService.saveCustomer(customerForTest);
    }

    @Test
    public void customerService_editCustomerWithCorrectValues_ReturnCorrect(){
        customerForTest = customerService.saveCustomer(customerForTest);

        CustomerServiceModel toBeEdited = new CustomerServiceModel();
        toBeEdited.setId(customerForTest.getId());
        toBeEdited.setName("Pesho");
        toBeEdited.setYoungDriver(true);

        CustomerServiceModel actual = customerService.editCustomer(customerForTest);
        CustomerServiceModel expected = this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);

        Assert.assertEquals(expected.getId(), toBeEdited.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_editCustomerWithNullValues_ThrowException(){
        customerForTest = customerService.saveCustomer(customerForTest);

        CustomerServiceModel toBeEdited = new CustomerServiceModel();
        toBeEdited.setId(customerForTest.getId());
        toBeEdited.setName(null);
        toBeEdited.setYoungDriver(true);

        this.customerService.editCustomer(toBeEdited);
    }

    @Test
    public void customerService_deleteCustomerWithCorrectValues_ReturnCorrect(){
        customerService.saveCustomer(customerForTest);

        CustomerServiceModel expected = this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class);
        customerService.deleteCustomer(expected.getId());

        long expectedCount = 0;
        long actualCount = this.customerRepository.findAll().size();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void customerService_deleteCustomerWithNullValues_ThrowException(){
        customerService.deleteCustomer(null);
    }

    @Test
    public void customerService_findCustomerByIdWithCorrectValues_ReturnCorrect(){

        CustomerServiceModel actual = this.customerService.saveCustomer(customerForTest);
        CustomerServiceModel expected = this.customerService.findCustomerById(actual.getId());

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isYoungDriver(), actual.isYoungDriver());
    }

    @Test(expected = Exception.class)
    public void customerService_findCustomerByIdWithNullValues_ThrowException(){
        customerService.findCustomerById(null);
    }

    private CustomerServiceModel customerForTest() {
        LocalDate birthDate = LocalDate.parse("1999-08-20", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        CustomerServiceModel customer = new CustomerServiceModel();
        customer.setName("George");
        customer.setBirthDate(birthDate);
        customer.setYoungDriver(true);

        return customer;
    }
}
