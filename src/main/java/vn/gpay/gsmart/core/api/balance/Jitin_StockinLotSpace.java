package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
public class Jitin_StockinLotSpace  implements Serializable{
	private static final long serialVersionUID = 1L;
	protected Long id;
	private Long stockinlotid_link;
	private String spaceepcid_link;
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
