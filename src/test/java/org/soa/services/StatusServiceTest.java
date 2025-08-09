//package org.soa.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.soa.reservation_service.model.Status;
//import org.soa.reservation_service.repository.StatusRepository;
//import org.soa.reservation_service.service.StatusService;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.when;
//
//class StatusServiceTest {
//
//    @InjectMocks
//    private StatusService statusService;
//
//    @Mock
//    private StatusRepository statusRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    // Existing tests remain unchanged
//
//    @Test
//    void testFindStatusByName_Success() {
//        String statusName = "Preklicano";
//        Status status = new Status();
//        status.setName(statusName);
//
//        when(statusRepository.findByName(statusName)).thenReturn(Optional.of(status));
//
//        Optional<Status> result = statusService.findStatusByName(statusName);
//
//        assertTrue(result.isPresent());
//        assertEquals(statusName, result.get().getName());
//    }
//
//    @Test
//    void testFindStatusByName_NotFound() {
//        String statusName = "NonExistent";
//        when(statusRepository.findByName(statusName)).thenReturn(Optional.empty());
//
//        Optional<Status> result = statusService.findStatusByName(statusName);
//
//        assertTrue(result.isEmpty());
//    }
//}