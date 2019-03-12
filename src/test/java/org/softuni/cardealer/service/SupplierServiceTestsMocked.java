//package org.softuni.cardealer.service;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mockito;
//import org.modelmapper.ModelMapper;
//import org.softuni.cardealer.domain.entities.Supplier;
//import org.softuni.cardealer.domain.models.service.SupplierServiceModel;
//import org.softuni.cardealer.repository.SupplierRepository;
//import org.softuni.cardealer.repository.SupplierRepositoryImpl;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//public class SupplierServiceTestsMocked {
//
//    private SaleService saleService;
//    private SupplierRepository mockedRepository;
//    private ModelMapper modelMapper;
//
//    @Before
//    public void init() {
//        this.modelMapper = new ModelMapper();
//        this.mockedRepository = Mockito.mock(SupplierRepositoryImpl.class);
//    }
//
//    @Test
//    public void t01(){
//        SupplierServiceModel supplier = new SupplierServiceModel();
//        SupplierServiceModel supplier1 = new SupplierServiceModel();
//        supplier.setName("Ivan");
//        supplier.setImporter(true);
//        supplier.setId("abc");
//
//        SupplierService supplierService = new SupplierServiceImpl(this.mockedRepository, this.modelMapper);
//
//        Mockito.when(this.mockedRepository.saveAndFlush(this.modelMapper.map(supplier1, Supplier.class)))
//                .thenReturn(this.modelMapper.map(supplier, Supplier.class));
//
//
////        Mockito.when(this.mockedRepository.findAll().get(0))
////                .thenReturn(this.modelMapper.map(supplier, Supplier.class));
//
//        //todo
//
//
//
//
//    }
//
//
//
//
//
//
//
//
//
//}
