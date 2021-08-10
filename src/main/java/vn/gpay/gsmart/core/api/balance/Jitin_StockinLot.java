package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jitin_StockinLot implements Serializable{
	private static final long serialVersionUID = 1L;
	protected Long id;
	private Long orgrootid_link;
	private Long stockindid_link;
	private Long stockinid_link;
	private Long materialid_link;
    private String lot_number;
	private Integer totalpackage;
	private Integer totalpackagecheck;
	private Integer totalpackagepklist;
    private String space;
    private Float totalyds;
    private Float totalmet;
    private Float totalydscheck;
    private Float totalmetcheck;
    private Float grossweight;
    private Float grossweight_check;
    private Float grossweight_lbs;
    private Float grossweight_lbs_check;
    private Integer status;
	private List<Jitin_StockinLotSpace>  stockin_lot_space  = new ArrayList<Jitin_StockinLotSpace>();
	private String stockinLotSpace;
	private String skucode;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getStockindid_link() {
		return stockindid_link;
	}
	public void setStockindid_link(Long stockindid_link) {
		this.stockindid_link = stockindid_link;
	}
	public Long getStockinid_link() {
		return stockinid_link;
	}
	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}
	public Long getMaterialid_link() {
		return materialid_link;
	}
	public void setMaterialid_link(Long materialid_link) {
		this.materialid_link = materialid_link;
	}
	public String getLot_number() {
		return lot_number;
	}
	public void setLot_number(String lot_number) {
		this.lot_number = lot_number;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public Integer getTotalpackagepklist() {
		return totalpackagepklist;
	}
	public void setTotalpackagepklist(Integer totalpackagepklist) {
		this.totalpackagepklist = totalpackagepklist;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public Float getTotalmet() {
		return totalmet;
	}
	public void setTotalmet(Float totalmet) {
		this.totalmet = totalmet;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}
	public Float getTotalmetcheck() {
		return totalmetcheck;
	}
	public void setTotalmetcheck(Float totalmetcheck) {
		this.totalmetcheck = totalmetcheck;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	public Float getGrossweight_check() {
		return grossweight_check;
	}
	public void setGrossweight_check(Float grossweight_check) {
		this.grossweight_check = grossweight_check;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public List<Jitin_StockinLotSpace> getStockin_lot_space() {
		return stockin_lot_space;
	}
	public void setStockin_lot_space(List<Jitin_StockinLotSpace> stockin_lot_space) {
		this.stockin_lot_space = stockin_lot_space;
	}
	public String getStockinLotSpace() {
		return stockinLotSpace;
	}
	public void setStockinLotSpace(String stockinLotSpace) {
		this.stockinLotSpace = stockinLotSpace;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public Float getGrossweight_lbs() {
		return grossweight_lbs;
	}
	public void setGrossweight_lbs(Float grossweight_lbs) {
		this.grossweight_lbs = grossweight_lbs;
	}
	public Float getGrossweight_lbs_check() {
		return grossweight_lbs_check;
	}
	public void setGrossweight_lbs_check(Float grossweight_lbs_check) {
		this.grossweight_lbs_check = grossweight_lbs_check;
	}

	
}
