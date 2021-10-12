package com.tenera.challenge.cityweatherinformation.repository;

import com.tenera.challenge.cityweatherinformation.entity.HistoricalInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoricalInformationRepository extends JpaRepository<HistoricalInformationEntity, UUID> {

    List<HistoricalInformationEntity> findByCityNameOrderByCreatedAtDesc(String cityName);
}
