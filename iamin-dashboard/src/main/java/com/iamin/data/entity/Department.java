package com.iamin.data.entity;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "department")
public class Department extends AbstractEntity {
	
	private String departmentName;
	
	@OneToOne
	@JoinColumn(name = "manger_id")
	private SamplePerson manager;

    
    @OneToMany(mappedBy = "department")
    private List<SamplePerson> employees;

    public List<SamplePerson> getEmployees() {
        return employees;
    }

    public void setEmployees(List<SamplePerson> employees) {
        this.employees = employees;
    }



	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public SamplePerson getManager() {
        return manager;
    }

	public void setManager(SamplePerson manager) {
	        this.manager = manager;
	}
	
}
	