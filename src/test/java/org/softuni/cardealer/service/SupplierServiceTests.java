package org.softuni.cardealer.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.softuni.cardealer.domain.entities.Supplier;
import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
import org.softuni.cardealer.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class SupplierServiceTests {

    private SupplierService supplierService;

    @Autowired
    private SupplierRepository supplierRepository;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
         this.supplierService = new SupplierServiceImpl(this.supplierRepository, this.modelMapper);
    }

    @Test
    public void supplierService_saveSupplierWithCorrectValues_ReturnCorrect(){

        SupplierServiceModel toBeSaved = new SupplierServiceModel();
        toBeSaved.setName("pesho");
        toBeSaved.setImporter(true);

        SupplierServiceModel actual = supplierService.saveSupplier(toBeSaved);
        SupplierServiceModel expected = this.modelMapper.map(this.supplierRepository.findAll().get(0),
                SupplierServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(toBeSaved.getName(), actual.getName());
        Assert.assertEquals(toBeSaved.isImporter(), actual.isImporter());
    }

    @Test(expected = Exception.class)
    public void supplierService_saveSupplierWithNullValues_ThrowException(){

        SupplierServiceModel toBeSaved = new SupplierServiceModel();
        toBeSaved.setName(null);
        toBeSaved.setImporter(true);

        supplierService.saveSupplier(toBeSaved);
    }

    @Test
    public void supplierService_editSupplierServiceWithCorrectValues_ReturnCorrect(){
        Supplier supplier = new Supplier();
        supplier.setName("Pesho");
        supplier.setImporter(true);

        supplier = this.supplierRepository.saveAndFlush(supplier);

        SupplierServiceModel toBeEdited = new SupplierServiceModel();
        toBeEdited.setId(supplier.getId());
        toBeEdited.setName("George");
        toBeEdited.setImporter(false);

        SupplierServiceModel actual = supplierService.editSupplier(toBeEdited);
        SupplierServiceModel expected = this.modelMapper.map(this.supplierRepository.findAll().get(0),
                SupplierServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isImporter(), actual.isImporter());
    }

    @Test(expected = Exception.class)
    public void supplierService_editSupplierServiceWithNullValues_ThrowsException(){
        Supplier supplier = new Supplier();
        supplier.setName("Pesho");
        supplier.setImporter(true);

        supplier = this.supplierRepository.saveAndFlush(supplier);

        SupplierServiceModel toBeEdited = new SupplierServiceModel();
        toBeEdited.setId(supplier.getId());
        toBeEdited.setName(null);
        toBeEdited.setImporter(false);

        SupplierServiceModel actual = supplierService.editSupplier(toBeEdited);
    }

    @Test
    public void supplierService_deleteSupplierServiceWithCorrectId_ReturnCorrect(){
        Supplier supplier = new Supplier();
        supplier.setName("Pesho");
        supplier.setImporter(true);

        this.supplierRepository.saveAndFlush(supplier);
        SupplierServiceModel toBeDeleted = this.modelMapper.map(this.supplierRepository.findAll().get(0),
                SupplierServiceModel.class);

        supplierService.deleteSupplier(toBeDeleted.getId());

        long expectedCount = 0;
        long actualCount = this.supplierRepository.findAll().size();

        Assert.assertEquals(expectedCount, actualCount);
    }

    @Test(expected = Exception.class)
    public void supplierService_deleteSupplierServiceWithInvalidId_ReturnException(){
        supplierService.deleteSupplier("Invalid Id");
    }

    @Test
    public void supplierService_findByIdWithCorrectId_ReturnCorrect(){
        Supplier supplier = new Supplier();
        supplier.setName("Pesho");
        supplier.setImporter(true);

        SupplierServiceModel actual = this.modelMapper.map(this.supplierRepository.saveAndFlush(supplier),
                SupplierServiceModel.class);
        SupplierServiceModel expected = this.modelMapper.map(supplierService.findSupplierById(actual.getId()),
                SupplierServiceModel.class);

        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.isImporter(), actual.isImporter());
    }

    @Test(expected = Exception.class)
    public void supplierService_findByIdWithInvalidId_ReturnException(){
        supplierService.findSupplierById("Invalid Id");
    }
}
