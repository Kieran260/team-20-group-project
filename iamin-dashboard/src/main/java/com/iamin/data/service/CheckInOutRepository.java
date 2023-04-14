package com.iamin.data.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iamin.data.entity.CheckInOut;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;

@Repository
public interface CheckInOutRepository extends JpaRepository<CheckInOut, Long> , JpaSpecificationExecutor<Login>{
	 @Query("SELECT c.checkInTime FROM CheckInOut c WHERE c.person = :person AND c.date = :date AND c.checkInTime IS NOT NULL")
	    Optional<CheckInOut> findCheckInTimeByPersonAndDate(@Param("person") SamplePerson person, @Param("date") LocalDate date);
	 @Query("SELECT c.checkOutTime FROM CheckInOut c WHERE c.person = :person AND c.date = :date AND c.checkInTime IS NOT NULL")
	 Optional<CheckInOut> findCheckOutTimeByPersonAndDate(@Param("person") SamplePerson person, @Param("date") LocalDate date);
	 @Query("SELECT c FROM CheckInOut c WHERE c.person = :person AND c.date = :date AND c.checkInTime IS NOT NULL")
	 CheckInOut findCheckInOutByPersonAndDate(@Param("person") SamplePerson person, @Param("date") LocalDate date);
	 List<CheckInOut> findByDateOrderByCheckInTimeDesc(LocalDate date);
	 List<CheckInOut> findByPersonAndDateBetween(SamplePerson person, LocalDate startDate, LocalDate endDate);
	 List<CheckInOut> findByDateBetween(LocalDate startDate, LocalDate endDate);

}