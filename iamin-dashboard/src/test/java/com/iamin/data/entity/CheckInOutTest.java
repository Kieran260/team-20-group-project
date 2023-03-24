package com.iamin.data.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CheckInOutTest {
	
	private static EntityManagerFactory emf;
	private static EntityManager em;
	
	@BeforeAll
	public static void setUp() {
		emf = Persistence.createEntityManagerFactory("test-unit");
		em = emf.createEntityManager();
	}
	
	@AfterAll
	public static void tearDown() {
		em.close();
		emf.close();
	}
	
	@Test
	public void testPersist() {
		SamplePerson person = new SamplePerson();
		person.setFirstName("John Doe");
		em.getTransaction().begin();
		em.persist(person);
		em.getTransaction().commit();
		
		CheckInOut checkInOut = new CheckInOut();
		checkInOut.setPerson(person);
		LocalTime checkInTime = LocalTime.now();
		checkInOut.setcheckInTime(checkInTime);
		LocalDate date = LocalDate.now();
		checkInOut.setdate(date);
		em.getTransaction().begin();
		em.persist(checkInOut);
		em.getTransaction().commit();
		
		assertNotNull(checkInOut.getId());
		
		CheckInOut persistedCheckInOut = em.find(CheckInOut.class, checkInOut.getId());
		assertEquals(checkInOut.getPerson().getFirstName(), persistedCheckInOut.getPerson().getFirstName());
		assertEquals(checkInOut.getcheckInTime(), persistedCheckInOut.getcheckInTime());
		assertEquals(checkInOut.getdate(), persistedCheckInOut.getdate());
	}
}
