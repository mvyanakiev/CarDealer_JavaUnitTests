package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.CarSaleServiceModel;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.softuni.cardealer.repository.CarSaleRepository;
import org.softuni.cardealer.repository.PartSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SaleServiceTests {

    private SaleService saleService;
    private CarService carService;

    @Autowired
    private CarSaleRepository carSaleRepository;
    private PartSaleRepository partSaleRepository;
    private CarRepository carRepository;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.saleService = new SaleServiceImpl(this.carSaleRepository, this.partSaleRepository, this.modelMapper);
        this.carService = new CarServiceImpl(this.carRepository, this.modelMapper);

    }

    @Test
    public void saleService_saleCarWithCorrectValues_ReturnCorrect(){
        CarServiceModel car = new CarServiceModel();
        List<PartServiceModel> parts = new ArrayList<>();
        PartServiceModel part = new PartServiceModel();
        part.setName("door");
        part.setPrice(BigDecimal.TEN);
        parts.add(part);

        car.setMake("Lada");
        car.setModel("2101");
        car.setTravelledDistance(123L);
        car.setParts(parts);

        carService.saveCar(car);


        CarSaleServiceModel carForSale = this.modelMapper.map(this.carRepository.findAll().get(0), CarSaleServiceModel.class);

        CarSaleServiceModel actual = this.saleService.saleCar(carForSale);


        CarSaleServiceModel expected = this.modelMapper.map(this.carSaleRepository.findAll().get(0), CarSaleServiceModel.class);

        Assert.assertEquals(expected.getCar().getId(), actual.getCar().getId());


    }






}
