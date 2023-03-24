package com.iamin.data.entity;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DepartmentTest {

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;
    
    @Before
    public void setup() {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-unit");
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
    }
    
    @After
    public void cleanup() {
        entityTransaction.rollback();
        entityManager.close();
        entityManagerFactory.close();
    }
    
    @Test
    public void testGetAndSetDepartmentName() {
        Department department = new Department();
        department.setDepartmentName("Sales");
        assertEquals("Sales", department.getDepartmentName());
    }
    
    @Test
    public void testGetAndSetManager() {
        Department department = new Department();
        SamplePerson manager = mock(SamplePerson.class);
        department.setManager(manager);
        assertEquals(manager, department.getManager());
    }
    
    @Test
    public void testGetAndSetEmployees() {
        Department department = new Department();
        SamplePerson employee1 = mock(SamplePerson.class);
        SamplePerson employee2 = mock(SamplePerson.class);
        List<SamplePerson> employees = Arrays.asList(employee1, employee2);
        department.setEmployees(employees);
        assertEquals(employees, department.getEmployees());
    }
    

    @Test
    public void testPersist() {
        SamplePerson manager = new SamplePerson();
        manager.setFirstName("John");
        manager.setLastName("Doe");
        entityManager.persist(manager); // persist the manager first
        Department department = new Department();
        department.setDepartmentName("Sales");
        department.setManager(manager);
        SamplePerson employee1 = new SamplePerson();
        employee1.setFirstName("Jane");
        employee1.setLastName("Doe");
        employee1.setDepartment(department);
        SamplePerson employee2 = new SamplePerson();
        employee2.setFirstName("Bob");
        employee2.setLastName("Smith");
        employee2.setDepartment(department);
        List<SamplePerson> employees = Arrays.asList(employee1, employee2);
        department.setEmployees(employees);
        entityManager.persist(department);
        entityManager.flush();
        entityManager.clear();
        Department persistedDepartment = entityManager.find(Department.class, department.getId());
        assertEquals(department, persistedDepartment);
    }

}
