package com.kennel.backend.service;

import com.kennel.backend.dto.vaccineRecord.response.VaccineRecordResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VaccineRecordService {
    Page<VaccineRecordResponseDto> getVaccinationRecordByDog(String dogSlug, Pageable pageable);

    Page<VaccineRecordResponseDto> getVaccinationRecordByDoctor(Long doctorId, Pageable pageable);

    Page<VaccineRecordResponseDto> getVaccinationRecordByVetVisit(Long vetVisitId, Pageable pageable);

    Page<VaccineRecordResponseDto> getVaccinationRecordByClinic(String clinicSlug, Pageable pageable);

    Page<VaccineRecordResponseDto> getVaccinationRecordByVaccine(String vaccineSlug, Pageable pageable);

    VaccineRecordResponseDto vaccinate(Long vetVisitId, String vaccineSlug);
}
