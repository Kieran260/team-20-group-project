package com.iamin.data.entity;
import java.time.LocalDate;
import java.time.LocalTime;

public class AssignTasks extends AbstractEntity{
    
	private String name;
    private String task;
    private LocalDate assignedDate;
    private String dueDate;
    private Integer daysRemaining;

    public String getTask() {
        return task;
    }
    public void setTask(String task) {
        this.task = task;
    }

    public LocalDate getAssignedDate() {
        return assignedDate;
    }
    public void setAssignedDate(LocalTime assignedDate) {
        this.assignedDate = LocalDate.now();
    }

}