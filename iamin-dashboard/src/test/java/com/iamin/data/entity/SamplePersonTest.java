package com.iamin.data.entity;

import java.time.LocalDate;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SamplePersonTest {

    @Test
    public void testGetAndSetFirstName() {
        SamplePerson person = new SamplePerson();
        person.setFirstName("John");
        assertEquals("John", person.getFirstName());
    }

    @Test
    public void testGetAndSetLastName() {
        SamplePerson person = new SamplePerson();
        person.setLastName("Doe");
        assertEquals("Doe", person.getLastName());
    }

    @Test
    public void testGetAndSetEmail() {
        SamplePerson person = new SamplePerson();
        person.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", person.getEmail());
    }

    @Test
    public void testGetAndSetPhone() {
        SamplePerson person = new SamplePerson();
        person.setPhone("123-456-7890");
        assertEquals("123-456-7890", person.getPhone());
    }

    @Test
    public void testGetAndSetDateOfBirth() {
        SamplePerson person = new SamplePerson();
        LocalDate dateOfBirth = LocalDate.of(1980, 1, 1);
        person.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, person.getDateOfBirth());
    }

    @Test
    public void testGetAndSetAddress() {
        SamplePerson person = new SamplePerson();
        person.setAddress("123 Main St");
        assertEquals("123 Main St", person.getAddress());
    }

    @Test
    public void testGetAndSetOccupation() {
        SamplePerson person = new SamplePerson();
        person.setOccupation("Software Developer");
        assertEquals("Software Developer", person.getOccupation());
    }

    @Test
    public void testGetAndSetJobTitle() {
        SamplePerson person = new SamplePerson();
        person.setJobTitle("Senior Software Engineer");
        assertEquals("Senior Software Engineer", person.getJobTitle());
    }

    @Test
    public void testGetAndSetMaxHolidays() {
        SamplePerson person = new SamplePerson();
        person.setMaxHolidays(25);
        assertEquals(25, person.getMaxHolidays().intValue());
    }

    @Test
    public void testGetAndSetDepartment() {
        SamplePerson person = new SamplePerson();
        Department department = new Department();
        department.setDepartmentName("Sales");
        person.setDepartment(department);
        assertEquals(department, person.getDepartment());
    }

}
