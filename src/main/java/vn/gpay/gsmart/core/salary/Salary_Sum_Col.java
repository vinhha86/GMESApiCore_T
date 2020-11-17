package vn.gpay.gsmart.core.salary;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="salary_sum_col")
@Entity
public class Salary_Sum_Col implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Integer id;
	private String code;
	private String name;
	private Integer sumcoltypeid_link;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSumcoltypeid_link() {
		return sumcoltypeid_link;
	}
	public void setSumcoltypeid_link(Integer sumcoltypeid_link) {
		this.sumcoltypeid_link = sumcoltypeid_link;
	}
	
}
