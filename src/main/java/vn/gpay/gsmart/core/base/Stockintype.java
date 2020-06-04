package vn.gpay.gsmart.core.base;

public enum Stockintype {
	STOCKIN_TYPE_FROM_NEW(1, "Stockin by Invoice"),
	STOCKIN_TYPE_FROM_CUTHOUSE(2, "Stock in by return from cuthouse"),
	STOCKIN_TYPE_FROM_OUTSOURCE(3, "Stock in from outsource company"),
	STOCKIN_TYPE_FROM_MOVE(4, "Stock in from internal stockout"),
	STOCKIN_TYPE_FROM_ADDBYPROVIDER(5, "Stockin by Provider add"),
	STOCKIN_TYPE_FROM_PRODUCT(6, "Stockin by Product complete");
	
	private final Integer value;

	private final String reasonPhrase;


	Stockintype(Integer value, String reasonPhrase) {
		this.value = value;
		this.reasonPhrase = reasonPhrase;
	}


	/**
	 * Return the integer value of this status code.
	 */
	public Integer value() {
		return this.value;
	}

	/**
	 * Return the reason phrase of this status code.
	 */
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}
}
