//package org.soa.services;
//
//import org.junit.jupiter.api.Test;
//import org.soa.companyService.model.Location;
//import org.soa.companyService.repository.LocationRepository;
//import org.soa.companyService.service.LocationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class LocationServiceTest {
//
//    @Autowired
//    private LocationService locationService;
//
//    @Autowired
//    private LocationRepository locationRepository;
//
//    @Test
//    public void testCreateLocation() {
//        Location location = new Location();
//        location.setStreet("Main Office");
//        location.setNumber("12");
//        Location savedLocation = locationService.createLocation(location);
//
//        assertThat(savedLocation.getId()).isNotNull();
//        assertThat(savedLocation.getStreet()).isEqualTo("Main Office");
//    }
//
//    @Test
//    public void testGetLocationById() {
//        Location location = new Location();
//        location.setStreet("Branch Office");
//        location.setNumber("asd");
//        Location savedLocation = locationService.createLocation(location);
//
//        Optional<Location> retrievedLocation = locationService.getLocationById(savedLocation.getId());
//        assertThat(retrievedLocation).isPresent();
//        assertThat(retrievedLocation.get().getStreet()).isEqualTo("Branch Office");
//    }
//
//    @Test
//    public void testUpdateLocation() {
//        Location location = new Location();
//        location.setStreet("Old Location");
//        location.setNumber("303");
//        Location savedLocation = locationService.createLocation(location);
//
//        savedLocation.setStreet("Updated Location");
//        savedLocation.setNumber("404");
//        Location updatedLocation = locationService.updateLocation(savedLocation.getId(), savedLocation);
//
//        assertThat(updatedLocation.getStreet()).isEqualTo("Updated Location");
//        assertThat(updatedLocation.getNumber()).isEqualTo("404");
//    }
//
//    @Test
//    public void testDeleteLocation() {
//        Location location = new Location();
//        location.setStreet("Temporary Location");
//        location.setNumber("505");
//        Location savedLocation = locationService.createLocation(location);
//
//        locationService.deleteLocation(savedLocation.getId());
//
//        Optional<Location> retrievedLocation = locationService.getLocationById(savedLocation.getId());
//        assertThat(retrievedLocation).isEmpty();
//    }
//}
