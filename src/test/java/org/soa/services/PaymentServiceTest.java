//package org.soa.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.soa.reservation_service.model.Payment;
//import org.soa.reservation_service.repository.PaymentRepository;
//import org.soa.reservation_service.service.PaymentService;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class PaymentServiceTest {
//
//    @InjectMocks
//    private PaymentService paymentService;
//
//    @Mock
//    private PaymentRepository paymentRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testGetAllPayments() {
//        paymentService.getAllPayments();
//        verify(paymentRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testGetPaymentById() {
//        Long id = 1L;
//        Payment payment = new Payment();
//        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
//
//        Optional<Payment> result = paymentService.getPaymentById(id);
//
//        assertTrue(result.isPresent());
//        assertEquals(payment, result.get());
//    }
//
//    @Test
//    void testCreatePayment() {
//        Payment payment = new Payment();
//        when(paymentRepository.save(payment)).thenReturn(payment);
//
//        Payment result = paymentService.createPayment(payment);
//
//        assertEquals(payment, result);
//    }
//
//    @Test
//    void testUpdatePayment() {
//        Long id = 1L;
//        Payment payment = new Payment();
//        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));
//        when(paymentRepository.save(any())).thenReturn(payment);
//
//        Payment updatedPayment = paymentService.updatePayment(id, payment);
//
//        assertEquals(payment, updatedPayment);
//    }
//
//    @Test
//    void testDeletePayment() {
//        Long id = 1L;
//        paymentService.deletePayment(id);
//        verify(paymentRepository, times(1)).deleteById(id);
//    }
//}
