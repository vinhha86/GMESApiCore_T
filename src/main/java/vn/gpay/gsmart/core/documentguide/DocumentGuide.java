package vn.gpay.gsmart.core.documentguide;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="documentguide")
@Entity
public class DocumentGuide implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documentguide_generator")
	@SequenceGenerator(name="documentguide_generator", sequenceName = "documentguide_id_seq", allocationSize=1)
	protected Long id;
	
	private String name;
	private String filename;
	private Long orgrootid_link;
	private Integer doctype;
	
	
	public Long getOrgrootid_linkl() {
		return orgrootid_link;
	}
	public void setOrgrootid_linkl(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
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
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Integer getDoctype() {
		return doctype;
	}
	public void setDoctype(Integer doctype) {
		this.doctype = doctype;
	}
	
	
}
