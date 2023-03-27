package com.iamin.views.manageTasks;

@PageTitle("Manage Tasks")
@Route(value = "manage-tasks", layout = MainLayout.class)
@RolesAllowed("ADMIN")

public class ManagerTasksView {

    public ManagerTasksView() {
        //
    }
}
