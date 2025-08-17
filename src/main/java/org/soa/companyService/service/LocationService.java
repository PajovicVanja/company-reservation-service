package org.soa.companyService.service;

import org.soa.companyService.exception.NotFoundException;
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

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location updatedLocation) {
        return locationRepository.findById(id)
                .map(existingLocation -> {
                    existingLocation.setStreet(updatedLocation.getStreet());
                    existingLocation.setNumber(updatedLocation.getNumber());
                    existingLocation.setParentLocation(updatedLocation.getParentLocation());
                    return locationRepository.save(existingLocation);
                })
                .orElseThrow(() -> new NotFoundException("Location not found with id " + id));
    }

    public void deleteLocation(Long id) {
        locationRepository.findById(id).orElseThrow(() -> new NotFoundException("Location not found with id " + id));
        locationRepository.deleteById(id);
    }
}
