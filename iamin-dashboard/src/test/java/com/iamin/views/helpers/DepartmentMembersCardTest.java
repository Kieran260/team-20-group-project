package com.iamin.views.helpers;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.iamin.data.Role;
import com.iamin.data.entity.Login;
import com.iamin.data.entity.SamplePerson;
import com.iamin.data.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DepartmentMembersCardTest {

    @InjectMocks
    private DepartmentMembersCard departmentMembersCard;

    @Mock
    private LoginService loginService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCardContainsPeople() {
        // Prepare data
        SamplePerson person1 = new SamplePerson();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setEmail("john.doe@example.com");

        SamplePerson person2 = new SamplePerson();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");
        person2.setEmail("jane.doe@example.com");
        List<SamplePerson> people = Arrays.asList(person1, person2);

        Login login1 = new Login();
        login1.setPerson(person1);
        Login login2 = new Login();
        login2.setPerson(person2);
        List<Login> logins = Arrays.asList(login1, login2);

        // Mock loginService
        when(loginService.findAllByRole(Role.USER)).thenReturn(logins);

        // Call createCard method
        Div card = new Div();
        Div result = departmentMembersCard.createCard(card, null);

        // Verify the call to loginService
        verify(loginService, times(1)).findAllByRole(Role.USER);

        // Get the employeeTable from the result
        Grid<SamplePerson> employeeTable = (Grid<SamplePerson>) result.getChildren().filter(component -> component instanceof Grid).findFirst().get();

        // Get the dataProvider from the employeeTable
        ListDataProvider<SamplePerson> dataProvider = (ListDataProvider<SamplePerson>) employeeTable.getDataProvider();

        // Get the list of SamplePerson from dataProvider
        Collection<SamplePerson> collection = dataProvider.getItems();
        List<SamplePerson> resultPeople = new ArrayList<>(collection);

        // Verify the result
        // Expected result: card contains two sample persons created above
        assertEquals(people, resultPeople);
    }
}
