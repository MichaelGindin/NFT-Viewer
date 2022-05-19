package back;

public class Collection {
	private String symbol;
	private double floorPriceMagicEden = 0;
	private double floorPriceOpenSea = 0;
	private String name = "";
	private double diff = 0;

	public Collection(String symbol, String name, double floorPriceMagicEden, double floorPriceOpenSea) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.floorPriceMagicEden = floorPriceMagicEden;
		this.floorPriceOpenSea = floorPriceOpenSea;

		if (floorPriceMagicEden != 0)
			diff = ((floorPriceMagicEden * 100) / floorPriceOpenSea) - 100;
	}

	public double getDiff() {
		return diff;
	}

	public void setDiff(double diff) {
		this.diff = diff;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public double getFloorPriceMagicEden() {
		return floorPriceMagicEden;
	}

	public void setFloorPriceMagicEden(double floorPriceMagicEden) {
		this.floorPriceMagicEden = floorPriceMagicEden;
	}

	public double getFloorPriceOpenSea() {
		return floorPriceOpenSea;
	}

	public void setFloorPriceOpenSea(double floorPriceOpenSea) {
		this.floorPriceOpenSea = floorPriceOpenSea;
	}

}
