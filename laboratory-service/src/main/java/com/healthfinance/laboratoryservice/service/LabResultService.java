package com.healthfinance.laboratoryservice.service;

import com.healthfinance.laboratoryservice.dto.LabResultRequest;
import com.healthfinance.laboratoryservice.model.LabResult;
import com.healthfinance.laboratoryservice.repository.LabResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LabResultService {
    
    private final LabResultRepository labResultRepository;
    
    @Transactional
    public LabResult createLabResult(LabResultRequest request) {
        LabResult labResult = LabResult.builder()
                .orderId(request.getOrderId())
                .testParameter(request.getTestParameter())
                .resultValue(request.getResultValue())
                .unit(request.getUnit())
                .referenceRange(request.getReferenceRange())
                .resultStatus(request.getResultStatus())
                .interpretation(request.getInterpretation())
                .notes(request.getNotes())
                .build();
        
        return labResultRepository.save(labResult);
    }
    
    public LabResult getLabResultById(String id) {
        return labResultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lab result not found"));
    }
    
    public List<LabResult> getResultsByOrder(String orderId) {
        return labResultRepository.findByOrderId(orderId);
    }
    
    @Transactional
    public void deleteLabResult(String id) {
        labResultRepository.deleteById(id);
    }
}
