package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;

public class Jitin_StockinLotSpace  implements Serializable{
	private static final long serialVersionUID = 1L;
	protected Long id;
	private Long stockinlotid_link;
	private String spaceepcid_link;
	private Integer totalpackage;

	//Transient
	private String space;
	private String spaceInfo;
	private String stockspaceStockrow_code;
	private String stockspaceSpacename;
	private Integer stockspaceFloorid;
	
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

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getSpaceInfo() {
		return spaceInfo;
	}

	public void setSpaceInfo(String spaceInfo) {
		this.spaceInfo = spaceInfo;
	}

	public String getStockspaceStockrow_code() {
		return stockspaceStockrow_code;
	}

	public void setStockspaceStockrow_code(String stockspaceStockrow_code) {
		this.stockspaceStockrow_code = stockspaceStockrow_code;
	}

	public String getStockspaceSpacename() {
		return stockspaceSpacename;
	}

	public void setStockspaceSpacename(String stockspaceSpacename) {
		this.stockspaceSpacename = stockspaceSpacename;
	}

	public Integer getStockspaceFloorid() {
		return stockspaceFloorid;
	}

	public void setStockspaceFloorid(Integer stockspaceFloorid) {
		this.stockspaceFloorid = stockspaceFloorid;
	}
	
	

}
