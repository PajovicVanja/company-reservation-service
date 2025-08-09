//package org.soa.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.soa.reservation_service.model.PaymentTypes;
//import org.soa.reservation_service.repository.PaymentTypesRepository;
//import org.soa.reservation_service.service.PaymentTypesService;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class PaymentTypesServiceTest {
//
//    @InjectMocks
//    private PaymentTypesService paymentTypesService;
//
//    @Mock
//    private PaymentTypesRepository paymentTypesRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllPaymentTypes() {
//        paymentTypesService.getAllPaymentTypes();
//        verify(paymentTypesRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetPaymentTypeById() {
//        Long id = 1L;
//        PaymentTypes paymentType = new PaymentTypes();
//        when(paymentTypesRepository.findById(id)).thenReturn(Optional.of(paymentType));
//
//        Optional<PaymentTypes> result = paymentTypesService.getPaymentTypeById(id);
//
//        assertTrue(result.isPresent());
//        assertEquals(paymentType, result.get());
//    }
//
//    @Test
//    void testCreatePaymentType() {
//        PaymentTypes paymentType = new PaymentTypes();
//        when(paymentTypesRepository.save(paymentType)).thenReturn(paymentType);
//
//        PaymentTypes result = paymentTypesService.createPaymentType(paymentType);
//
//        assertEquals(paymentType, result);
//    }
//
//    @Test
//    void testUpdatePaymentType() {
//        Long id = 1L;
//        PaymentTypes paymentType = new PaymentTypes();
//        when(paymentTypesRepository.findById(id)).thenReturn(Optional.of(paymentType));
//        when(paymentTypesRepository.save(any())).thenReturn(paymentType);
//
//        PaymentTypes updatedPaymentType = paymentTypesService.updatePaymentType(id, paymentType);
//
//        assertEquals(paymentType, updatedPaymentType);
//    }
//
//    @Test
//    void testDeletePaymentType() {
//        Long id = 1L;
//        paymentTypesService.deletePaymentType(id);
//        verify(paymentTypesRepository, times(1)).deleteById(id);
//    }
//}
