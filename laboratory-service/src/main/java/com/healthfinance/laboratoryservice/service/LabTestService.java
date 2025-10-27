package com.healthfinance.laboratoryservice.service;

import com.healthfinance.laboratoryservice.dto.LabTestRequest;
import com.healthfinance.laboratoryservice.model.LabTest;
import com.healthfinance.laboratoryservice.repository.LabTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabTestService {
    
    private final LabTestRepository labTestRepository;
    
    @Transactional
    public LabTest createLabTest(LabTestRequest request) {
        LabTest labTest = LabTest.builder()
                .testCode(request.getTestCode())
                .testName(request.getTestName())
                .category(request.getCategory())
                .description(request.getDescription())
                .price(request.getPrice())
                .sampleType(request.getSampleType())
                .preparationTimeMinutes(request.getPreparationTimeMinutes())
                .processingTimeHours(request.getProcessingTimeHours())
                .preparationInstructions(request.getPreparationInstructions())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        
        return labTestRepository.save(labTest);
    }
    
    public LabTest getLabTestById(String id) {
        return labTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab test not found"));
    }
    
    public List<LabTest> getAllLabTests() {
        return labTestRepository.findAll();
    }
    
    public List<LabTest> getActiveLabTests() {
        return labTestRepository.findByIsActive(true);
    }
    
    @Transactional
    public void deleteLabTest(String id) {
        labTestRepository.deleteById(id);
    }
}
