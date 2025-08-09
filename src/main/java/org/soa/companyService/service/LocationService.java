package org.soa.companyService.service;

import org.soa.companyService.model.Location;
import org.soa.companyService.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    // Get all locations
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    // Get a location by ID
    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    // Create a new location
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    // Update an existing location
    public Location updateLocation(Long id, Location updatedLocation) {
        return locationRepository.findById(id)
                .map(existingLocation -> {
                    existingLocation.setStreet(updatedLocation.getStreet());
                    existingLocation.setNumber(updatedLocation.getNumber());
                    existingLocation.setParentLocation(updatedLocation.getParentLocation());
                    return locationRepository.save(existingLocation);
                })
                .orElseThrow(() -> new RuntimeException("Location not found with id " + id));
    }

    // Delete a location by ID
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }
}
