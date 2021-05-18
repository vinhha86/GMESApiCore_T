package vn.gpay.gsmart.core.stockin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="stockin_lot_space")
@Entity
public class StockinLotSpace  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockin_lot_space_generator")
	@SequenceGenerator(name="stockin_lot_space_generator", sequenceName = "stockin_lot_space_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name = "stockinlotid_link")
	private Long stockinlotid_link;
	
	@Column(name = "spaceepcid_link")
	private String spaceepcid_link;
	
	@Column(name = "totalpackage")
	private Integer totalpackage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStockinlotid_link() {
		return stockinlotid_link;
	}

	public void setStockinlotid_link(Long stockinlotid_link) {
		this.stockinlotid_link = stockinlotid_link;
	}

	public String getSpaceepcid_link() {
		return spaceepcid_link;
	}

	public void setSpaceepcid_link(String spaceepcid_link) {
		this.spaceepcid_link = spaceepcid_link;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	
	

}
