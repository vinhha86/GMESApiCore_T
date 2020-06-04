package vn.gpay.gsmart.core.stockout;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

public class StockoutReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	protected Long id;
	
	@Transient
    private String skucode;
	
	@Transient
    private Integer skutypeid_link;
	
	@Transient
    private String skutype;
	
	@Transient
    private String color_name;

	@Transient
    private Integer packageprocessed;
	
	@Transient
    private Integer packagecheck;
	
	@Transient
    private Double ydsorigin;
	
	@Transient
    private Double ydscheck;

	@Transient
    private Double ydsprocessed;	

	@Transient
    private Double totalerror;
	
	@Transient
	private List<StockOutPklist>  processedpklist  = new ArrayList<>();
	
	@Transient
	private List<StockOutPklist>  checkpklist  = new ArrayList<>();

	public List<StockOutPklist> getProcessedpklist() {
		return processedpklist;
	}

	public void setProcessedpklist(List<StockOutPklist> processedpklist) {
		this.processedpklist = processedpklist;
	}

	public List<StockOutPklist> getCheckpklist() {
		return checkpklist;
	}

	public void setCheckpklist(List<StockOutPklist> checkpklist) {
		this.checkpklist = checkpklist;
	}

	public String getSkucode() {
		return skucode;
	}

	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}

	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}

	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}

	public String getColor_name() {
		return color_name;
	}

	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}

	public Double getYdsorigin() {
		return ydsorigin;
	}

	public void setYdsorigin(Double ydsorigin) {
		this.ydsorigin = ydsorigin;
	}

	public Double getYdscheck() {
		return ydscheck;
	}

	public void setYdscheck(Double ydscheck) {
		this.ydscheck = ydscheck;
	}

	public Double getYdsprocessed() {
		return ydsprocessed;
	}

	public void setYdsprocessed(Double ydsprocessed) {
		this.ydsprocessed = ydsprocessed;
	}

	public Double getTotalerror() {
		return totalerror;
	}

	public void setTotalerror(Double totalerror) {
		this.totalerror = totalerror;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getSkutype() {
		return skutype;
	}

	public void setSkutype(String skutype) {
		this.skutype = skutype;
	}

	public Integer getPackageprocessed() {
		return packageprocessed;
	}

	public void setPackageprocessed(Integer packageprocessed) {
		this.packageprocessed = packageprocessed;
	}

	public Integer getPackagecheck() {
		return packagecheck;
	}

	public void setPackagecheck(Integer packagecheck) {
		this.packagecheck = packagecheck;
	}

	
}
