package vn.gpay.gsmart.core.stock;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Table(name="Stockspace")
@Entity
public class Stockspace implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name ="spaceepc",length=50)
    protected String spaceepc;
	
	@Column(name ="orgid_link")
    private Long orgid_link;
	
	@Column(name ="spacename",length=100)
    private String spacename = "";
	
	@Column(name ="rowid_link")
    private Long rowid_link;
	
	@Column(name ="floorid")
    private Long floorid;

	public String getSpaceepc() {
		return spaceepc;
	}

	public void setSpaceepc(String spaceepc) {
		this.spaceepc = spaceepc;
	}

	public Long getOrgid_link() {
		return orgid_link;
	}

	public void setOrgid_link(Long orgid_link) {
		this.orgid_link = orgid_link;
	}

	public String getSpacename() {
		return spacename;
	}

	public void setSpacename(String spacename) {
		this.spacename = spacename;
	}

	public Long getRowid_link() {
		return rowid_link;
	}

	public void setRowid_link(Long rowid_link) {
		this.rowid_link = rowid_link;
	}

	public Long getFloorid() {
		return floorid;
	}

	public void setFloorid(Long floorid) {
		this.floorid = floorid;
	}
	
}
