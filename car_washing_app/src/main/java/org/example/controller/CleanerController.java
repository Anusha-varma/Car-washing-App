package org.example.controller;

import org.example.model.Cleaner;
import org.example.model.Area;
import org.example.repository.CleanerJpaRepository;
import org.example.exception.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Cleaner CRUD operations
 */
@RestController
@RequestMapping("/api/cleaners")
@Tag(name = "Cleaner Management", description = "APIs for managing cleaners")
public class CleanerController {

    @Autowired
    private CleanerJpaRepository cleanerRepository;

    /**
     * Get all cleaners
     */
    @GetMapping
    @Operation(summary = "Get all cleaners", description = "Retrieve all registered cleaners")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all cleaners")
    public ResponseEntity<List<Cleaner>> getAllCleaners() {
        List<Cleaner> cleaners = cleanerRepository.findAll();
        return ResponseEntity.ok(cleaners);
    }

    /**
     * Get cleaner by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get cleaner by ID", description = "Retrieve a specific cleaner by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cleaner found"),
        @ApiResponse(responseCode = "404", description = "Cleaner not found")
    })
    public ResponseEntity<Cleaner> getCleanerById(@PathVariable int id) {
        try {
            Optional<Cleaner> cleaner = cleanerRepository.findById(id);
            if (cleaner.isPresent()) {
                return ResponseEntity.ok(cleaner.get());
            } else {
                throw new CleanerNotFoundException(id);
            }
        } catch (CleanerNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Get cleaners by area
     */
    @GetMapping("/area/{area}")
    @Operation(summary = "Get cleaners by area", description = "Retrieve all cleaners in a specific area")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved cleaners")
    public ResponseEntity<List<Cleaner>> getCleanersByArea(@PathVariable Area area) {
        List<Cleaner> cleaners = cleanerRepository.findByArea(area);
        return ResponseEntity.ok(cleaners);
    }

    /**
     * Get available cleaners by area
     */
    @GetMapping("/area/{area}/available")
    @Operation(summary = "Get available cleaners by area", description = "Retrieve all available cleaners in a specific area")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved available cleaners")
    public ResponseEntity<List<Cleaner>> getAvailableCleanersByArea(@PathVariable Area area) {
        List<Cleaner> cleaners = cleanerRepository.findByAreaAndAvailableTrue(area);
        return ResponseEntity.ok(cleaners);
    }

    /**
     * Create a new cleaner
     */
    @PostMapping
    @Operation(summary = "Register a new cleaner", description = "Create a new cleaner profile")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cleaner registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<Cleaner> createCleaner(@RequestBody Cleaner cleaner) {
        try {
            if (cleaner.getName() == null || cleaner.getName().trim().isEmpty()) {
                throw new InvalidInputException("Cleaner name cannot be empty");
            }
            if (cleaner.getExperience() < 0) {
                throw new InvalidInputException("Experience cannot be negative");
            }
            if (cleaner.getSalary() < 0) {
                throw new InvalidInputException("Salary cannot be negative");
            }

            cleaner.setAvailable(true);
            Cleaner savedCleaner = cleanerRepository.save(cleaner);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCleaner);
        } catch (InvalidInputException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Update an existing cleaner
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update cleaner information", description = "Update an existing cleaner's details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cleaner updated successfully"),
        @ApiResponse(responseCode = "404", description = "Cleaner not found")
    })
    public ResponseEntity<Cleaner> updateCleaner(@PathVariable int id, @RequestBody Cleaner cleanerDetails) {
        try {
            Optional<Cleaner> cleaner = cleanerRepository.findById(id);
            if (cleaner.isPresent()) {
                Cleaner existingCleaner = cleaner.get();
                if (cleanerDetails.getName() != null) {
                    existingCleaner.setName(cleanerDetails.getName());
                }
                if (cleanerDetails.getExperience() >= 0) {
                    existingCleaner.setExperience(cleanerDetails.getExperience());
                }
                if (cleanerDetails.getSalary() >= 0) {
                    existingCleaner.setSalary(cleanerDetails.getSalary());
                }
                if (cleanerDetails.getArea() != null) {
                    existingCleaner.setArea(cleanerDetails.getArea());
                }
                existingCleaner.setAvailable(cleanerDetails.isAvailable());

                Cleaner updatedCleaner = cleanerRepository.save(existingCleaner);
                return ResponseEntity.ok(updatedCleaner);
            } else {
                throw new CleanerNotFoundException(id);
            }
        } catch (CleanerNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete a cleaner
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cleaner", description = "Remove a cleaner from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cleaner deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Cleaner not found")
    })
    public ResponseEntity<Void> deleteCleaner(@PathVariable int id) {
        try {
            if (cleanerRepository.existsById(id)) {
                cleanerRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                throw new CleanerNotFoundException(id);
            }
        } catch (CleanerNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Update cleaner availability
     */
    @PatchMapping("/{id}/availability")
    @Operation(summary = "Update cleaner availability", description = "Update the availability status of a cleaner")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Availability updated successfully"),
        @ApiResponse(responseCode = "404", description = "Cleaner not found")
    })
    public ResponseEntity<Cleaner> updateAvailability(@PathVariable int id, @RequestParam boolean available) {
        try {
            Optional<Cleaner> cleaner = cleanerRepository.findById(id);
            if (cleaner.isPresent()) {
                Cleaner existingCleaner = cleaner.get();
                existingCleaner.setAvailable(available);
                Cleaner updatedCleaner = cleanerRepository.save(existingCleaner);
                return ResponseEntity.ok(updatedCleaner);
            } else {
                throw new CleanerNotFoundException(id);
            }
        } catch (CleanerNotFoundException e) {
            AppExceptionHandler.handleException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
