package com.banquito.core.productsaccounts;

import com.banquito.core.productsaccounts.exception.CRUDException;
import com.banquito.core.productsaccounts.model.InterestRate;
import com.banquito.core.productsaccounts.repository.InterestRateRepository;
import com.banquito.core.productsaccounts.service.InterestRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InterestRateApplicationTests {

    @Mock
    private InterestRateRepository interestRateRepository;

    private InterestRateService interestRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        interestRateService = new InterestRateService(interestRateRepository);
    }

    @Test
    void testListAllActives() {
        List<InterestRate> mockRates = new ArrayList<>();
        mockRates.add(new InterestRate());
        mockRates.add(new InterestRate());

        when(interestRateRepository.findByState("ACT")).thenReturn(mockRates);

        List<InterestRate> result = interestRateService.listAllActives();

        assertEquals(mockRates.size(), result.size());
    }

    @Test
    void testObtainByIdExistingRate() throws CRUDException {
        Integer rateId = 1;
        InterestRate expectedRate = new InterestRate();
        expectedRate.setId(rateId);

        when(interestRateRepository.findById(rateId)).thenReturn(Optional.of(expectedRate));

        InterestRate result = interestRateService.obtainById(rateId);

        assertEquals(expectedRate, result);
    }

    @Test
    void testObtainByIdNonExistingRate() {
        Integer rateId = 999;

        when(interestRateRepository.findById(rateId)).thenReturn(Optional.empty());

        assertThrows(CRUDException.class, () -> interestRateService.obtainById(rateId));
    }

    @Test
    void testCreateInterestRate() throws CRUDException {
        InterestRate newRate = new InterestRate();

        interestRateService.create(newRate);

        verify(interestRateRepository).save(newRate);
    }

    @Test
    void testCreateInterestRateException() {
        InterestRate newRate = new InterestRate();

        when(interestRateRepository.save(newRate)).thenThrow(new RuntimeException("Some error"));

        assertThrows(CRUDException.class, () -> interestRateService.create(newRate));
    }

    @Test
    void testUpdateExistingRate() throws CRUDException {
        Integer rateId = 1;
        InterestRate existingRate = new InterestRate();
        existingRate.setId(rateId);

        when(interestRateRepository.findById(rateId)).thenReturn(Optional.of(existingRate));

        InterestRate updatedRate = new InterestRate();
        updatedRate.setName("Updated Name");

        interestRateService.update(rateId, updatedRate);

        assertEquals(updatedRate.getName(), existingRate.getName());
        verify(interestRateRepository).save(existingRate);
    }

    @Test
    void testUpdateNonExistingRate() {
        Integer rateId = 999;

        when(interestRateRepository.findById(rateId)).thenReturn(Optional.empty());

        assertThrows(CRUDException.class, () -> interestRateService.update(rateId, new InterestRate()));
    }

    @Test
    void testInactivateExistingRate() {
        Integer rateId = 1;
        InterestRate existingRate = new InterestRate();
        existingRate.setId(rateId);

        when(interestRateRepository.findById(rateId)).thenReturn(Optional.of(existingRate));

        interestRateService.inactivate(rateId);

        assertEquals("INA", existingRate.getState());
        assertNotNull(existingRate.getEnd());
        verify(interestRateRepository).save(existingRate);
    }

    @Test
    void testInactivateNonExistingRate() {
        Integer rateId = 999;

        when(interestRateRepository.findById(rateId)).thenReturn(Optional.empty());

        assertThrows(CRUDException.class, () -> interestRateService.inactivate(rateId));
    }

}
