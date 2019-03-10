package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.models.service.CarServiceModel;
import org.softuni.cardealer.domain.models.service.PartServiceModel;
import org.softuni.cardealer.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CarServiceTests {

    private CarService carService;
    private CarServiceModel testCar;

    @Autowired
    private CarRepository carRepository;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.carService = new CarServiceImpl(this.carRepository, this.modelMapper);
        this.testCar = createTestCar();
    }

    @Test
    public void carService_saveCarWithCorrectValues_ReturnCorrect(){
        CarServiceModel actual = carService.saveCar(testCar);
        CarServiceModel expected = this.modelMapper.map(this.carRepository.findAll().get(0), CarServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
    }

    @Test(expected = Exception.class)
    public void carService_saveCarWithNullValues_ThrowException(){
        testCar.setMake(null);
        this.carService.saveCar(testCar);
    }

    @Test
    public void carService_editCarWithCorrectValues_ReturnCorrect(){
        testCar = carService.saveCar(testCar);

        CarServiceModel toBeEdited = new CarServiceModel();
        toBeEdited.setId(testCar.getId());
        toBeEdited.setMake("BMW");
        toBeEdited.setModel("320");
        toBeEdited.setTravelledDistance(147000L);

        CarServiceModel actual = this.carService.editCar(toBeEdited);
        CarServiceModel expected = this.modelMapper.map(this.carRepository.findAll().get(0), CarServiceModel.class);

        this.compareCars(actual, expected);
    }

    @Test(expected = Exception.class)
    public void carService_editCarWithNullValues_ThrowException(){
        testCar = carService.saveCar(testCar);

        CarServiceModel toBeEdited = new CarServiceModel();
        toBeEdited.setId(testCar.getId());
        toBeEdited.setMake(null);
        toBeEdited.setModel("320");
        toBeEdited.setTravelledDistance(147000L);

       this.carService.editCar(toBeEdited);
    }

    @Test
    public void carService_deleteCarWithCorrectValues_ReturnCorrect(){
        testCar = carService.saveCar(testCar);
        CarServiceModel expected = this.modelMapper.map(this.carRepository.findAll().get(0), CarServiceModel.class);
        this.carService.deleteCar(expected.getId());

        long expectedCount = 0;
        long actualCount = this.carRepository.findAll().size();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void carService_deleteCarWithNullValues_ThrowException(){
        this.carService.deleteCar(null);
    }

    @Test
    public void carService_findCarByIdCarWithCorrectValues_ReturnCorrect(){
        CarServiceModel actual = this.carService.saveCar(testCar);
        CarServiceModel expected = this.carService.findCarById(actual.getId());

        this.compareCars(actual, expected);
    }

    @Test(expected = Exception.class)
    public void carService_findCarByIdCarWithNullValues_ThrowException(){
        this.carService.findCarById(null);
    }

    private CarServiceModel createTestCar(){
        CarServiceModel car = new CarServiceModel();
        List<PartServiceModel> parts = new ArrayList<>();

        car.setMake("Lada");
        car.setModel("2101");
        car.setTravelledDistance(123L);
        car.setParts(parts);

        return car;
    }

    private void compareCars(CarServiceModel actual, CarServiceModel expected){
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getMake(), actual.getMake());
        Assert.assertEquals(expected.getModel(), actual.getModel());
        Assert.assertEquals(expected.getTravelledDistance(), actual.getTravelledDistance());
    }
}
