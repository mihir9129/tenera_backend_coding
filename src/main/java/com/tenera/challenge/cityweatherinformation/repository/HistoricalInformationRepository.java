package com.tenera.challenge.cityweatherinformation.repository;

import com.tenera.challenge.cityweatherinformation.entity.HistoricalInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

public interface HistoricalInformationRepository extends JpaRepository<HistoricalInformationEntity, UUID> {

//    @Query("Select h from HistoricalInformationEntity where h.cityName = :city_name order by h.id")
    List<HistoricalInformationEntity> findByCityNameOrderByIdDesc(String cityName);
}
