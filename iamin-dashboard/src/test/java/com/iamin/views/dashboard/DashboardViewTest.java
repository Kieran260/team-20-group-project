package com.iamin.views.dashboard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.iamin.data.service.LoginRepository;
import com.iamin.views.helpers.AnalyticsCard;
import com.iamin.views.helpers.AverageAttendanceCard;
import com.iamin.views.helpers.CalendarCard;
import com.iamin.views.helpers.DepartmentMembersCard;
import com.iamin.views.helpers.EmployeeAttendanceCard;
import com.iamin.views.helpers.EmployeeAverageAttendanceCard;
import com.iamin.views.helpers.EmployeeTasksCard;
import com.iamin.views.helpers.EmployeesTableCard;
import com.iamin.views.helpers.NotificationsCard;
import com.iamin.views.helpers.PasswordDialog;
import com.iamin.views.helpers.PersonFormDialog;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardViewTest {
    private DashboardView dashboardView;
    private PersonFormDialog personFormDialog;
    private LoginRepository loginRepository;
    private EmployeeAttendanceCard employeeAttendanceCard;
    private DepartmentMembersCard departmentMembersCard;
    private NotificationsCard notificationsCard;
    private EmployeesTableCard employeesTableCard;
    private EmployeeTasksCard employeeTasksCard;
    private PasswordEncoder passwordEncoder;
    private PasswordDialog passwordDialog;
    private AverageAttendanceCard averageAttendanceCard;
    private EmployeeAverageAttendanceCard employeeAverageAttendanceCard;
    private CalendarCard calendarCard;
    private AnalyticsCard analyticsCard;

    private Authentication authentication;

    

    @BeforeEach
    void setUp() {
        personFormDialog = Mockito.mock(PersonFormDialog.class);
        loginRepository = Mockito.mock(LoginRepository.class);
        employeeAttendanceCard = Mockito.mock(EmployeeAttendanceCard.class);
        departmentMembersCard = Mockito.mock(DepartmentMembersCard.class);
        notificationsCard = Mockito.mock(NotificationsCard.class);
        employeesTableCard = Mockito.mock(EmployeesTableCard.class);
        employeeTasksCard = Mockito.mock(EmployeeTasksCard.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        passwordDialog = Mockito.mock(PasswordDialog.class);
        averageAttendanceCard = Mockito.mock(AverageAttendanceCard.class);
        employeeAverageAttendanceCard = Mockito.mock(EmployeeAverageAttendanceCard.class);
        calendarCard = Mockito.mock(CalendarCard.class);
        analyticsCard = Mockito.mock(AnalyticsCard.class);

        // Create mock Authentication object
        authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("mockUsername");

        // Set up SecurityContextHolder to return the mock Authentication object
        SecurityContextHolder.getContext().setAuthentication(authentication);

        dashboardView = new DashboardView(personFormDialog, loginRepository, employeeAttendanceCard, departmentMembersCard,
                                          notificationsCard, employeesTableCard, employeeTasksCard, passwordEncoder, passwordDialog,
                                          averageAttendanceCard, employeeAverageAttendanceCard, calendarCard, analyticsCard);
    }

    @Test
    void testGetUserRole() {
        Authentication authentication = Mockito.mock(Authentication.class);
        
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        
        ArgumentCaptor<Collection<? extends GrantedAuthority>> captor = ArgumentCaptor.forClass(Collection.class);
        Mockito.doReturn(authorities).when(authentication).getAuthorities();
        
        String role = dashboardView.getUserRole(authentication);
        assertEquals("ROLE_ADMIN", role);
    }


    @Test
    void testAllCardsCanBeCreated() {
        assertNotNull(employeeAttendanceCard);
        assertNotNull(departmentMembersCard);
        assertNotNull(notificationsCard);
        assertNotNull(employeesTableCard);
        assertNotNull(employeeTasksCard);
        assertNotNull(passwordEncoder);
        assertNotNull(passwordDialog);
        assertNotNull(personFormDialog);
        assertNotNull(loginRepository);
        assertNotNull(averageAttendanceCard);
        assertNotNull(employeeAverageAttendanceCard);
        assertNotNull(calendarCard);
        assertNotNull(analyticsCard);
    }

}
