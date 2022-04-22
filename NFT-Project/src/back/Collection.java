package back;

public class Collection {
	private String symbol;
	private double floorPrice;
	
	
	public Collection(String symbol, double floorPrice2) {
		super();
		this.symbol = symbol;
		this.floorPrice = floorPrice2;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getFloorPrice() {
		return floorPrice;
	}
	public void setFloorPrice(double floorPrice) {
		this.floorPrice = floorPrice;
	}
	
}
