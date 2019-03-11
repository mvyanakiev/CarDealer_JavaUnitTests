package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.*;
import org.softuni.cardealer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SaleServiceTests {

    private CarService carService;
    private SaleService saleService;
    private CustomerService customerService;
    private PartService partService;
    private SupplierService supplierService;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CarSaleRepository carSaleRepository;
    @Autowired
    private PartSaleRepository partSaleRepository;
    @Autowired
    private PartRepository partRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    private ModelMapper modelMapper;

    @Before
    public void init() {
        this.modelMapper = new ModelMapper();
        this.saleService = new SaleServiceImpl(this.carSaleRepository, this.partSaleRepository, this.modelMapper);
        this.carService = new CarServiceImpl(this.carRepository, this.modelMapper);
        this.customerService = new CustomerServiceImpl(this.customerRepository, this.modelMapper);
        this.partService = new PartServiceImpl(this.partRepository, this.modelMapper);
        this.supplierService = new SupplierServiceImpl(this.supplierRepository, this.modelMapper);

    }

    @Test
    public void saleService_saleCarWithCorrectValues_ReturnCorrect() {
        SupplierServiceModel supplier = new SupplierServiceModel();
        supplier.setName("Ivan");
        supplier.setImporter(true);
        supplier = this.supplierService.saveSupplier(supplier);

        List<PartServiceModel> parts = new ArrayList<>();
        PartServiceModel part = new PartServiceModel();
        part.setName("door");
        part.setPrice(BigDecimal.TEN);
        part.setSupplier(supplier);
        part = this.partService.savePart(part);
        parts.add(part);

        CarServiceModel car = new CarServiceModel();
        car.setMake("Lada");
        car.setModel("2101");
        car.setTravelledDistance(123L);
        car.setParts(parts);
        car = this.carService.saveCar(car);

        CustomerServiceModel customer = new CustomerServiceModel();
        LocalDate birthDate = LocalDate.parse("1999-08-20", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        customer.setName("George");
        customer.setBirthDate(birthDate);
        customer.setYoungDriver(true);
        customer = this.customerService.saveCustomer(customer);


        CarSaleServiceModel carForSale = this.modelMapper.map(this.carRepository.findAll().get(0), CarSaleServiceModel.class);
        carForSale.setDiscount(10.0);
        carForSale.setCustomer(this.modelMapper.map(this.customerRepository.findAll().get(0), CustomerServiceModel.class));


        CarSaleServiceModel actual = this.saleService.saleCar(this.modelMapper.map(carForSale, CarSaleServiceModel.class));


        CarSaleServiceModel expected = this.modelMapper.map(this.carSaleRepository.findAll().get(0), CarSaleServiceModel.class);

        Assert.assertEquals(expected.getCar().getId(), actual.getCar().getId());


    }


}
