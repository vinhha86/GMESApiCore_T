package vn.gpay.gsmart.core.stockout_type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="stockouttype")
@Entity
public class StockoutType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockouttype_generator")
	@SequenceGenerator(name="stockouttype_generator", sequenceName = "stockouttype_id_seq", allocationSize=1)
	private Long id;
	private String name;
	private String name_en;
	private String comment;
	private String comment_en;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getComment_en() {
		return comment_en;
	}
	public void setComment_en(String comment_en) {
		this.comment_en = comment_en;
	}
}
