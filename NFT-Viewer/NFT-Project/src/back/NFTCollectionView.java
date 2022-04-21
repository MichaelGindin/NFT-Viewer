package back;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class NFTCollectionView {
	private SimpleStringProperty collection_name;
	private SimpleFloatProperty opensea_price;

	public SimpleStringProperty getCollection_name() {
		return collection_name;
	}

	private SimpleFloatProperty magic_eden_price;
	private SimpleFloatProperty diff;

	public NFTCollectionView(String name, float opensea, float magic, float diff) {
		this.collection_name = new SimpleStringProperty(name);
		this.opensea_price = new SimpleFloatProperty(opensea);
		this.magic_eden_price = new SimpleFloatProperty(magic);
		this.diff = new SimpleFloatProperty(diff);
	}

	// Auto generated
	public void setCollection_name(SimpleStringProperty collection_name) {
		this.collection_name = collection_name;
	}

	public SimpleFloatProperty getOpensea_price() {
		return opensea_price;
	}

	public void setOpensea_price(SimpleFloatProperty opensea_price) {
		this.opensea_price = opensea_price;
	}

	public SimpleFloatProperty getMagic_eden_price() {
		return magic_eden_price;
	}

	public void setMagic_eden_price(SimpleFloatProperty magic_eden_price) {
		this.magic_eden_price = magic_eden_price;
	}

	public SimpleFloatProperty getDiff() {
		return diff;
	}

	public void setDiff(SimpleFloatProperty diff) {
		this.diff = diff;
	}
}