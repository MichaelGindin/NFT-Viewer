package back;

import java.util.Comparator;

public class NFTCollectionView implements Comparator<NFTCollectionView>{
	private String collection_name;
	private float opensea_price;
	private float magic_eden_price;
	private float diff;

	public NFTCollectionView(String collection_name, float opensea_price, float magic_eden_price, float diff) {
		this.collection_name = collection_name;
		this.opensea_price = opensea_price;
		this.magic_eden_price = magic_eden_price;
		this.diff = diff;
	}

	public String getCollection_name() {
		return collection_name;
	}

	public void setCollection_name(String collection_name) {
		this.collection_name = collection_name;
	}

	public float getOpensea_price() {
		return opensea_price;
	}

	public void setOpensea_price(float opensea_price) {
		this.opensea_price = opensea_price;
	}

	public float getMagic_eden_price() {
		return magic_eden_price;
	}

	public void setMagic_eden_price(float magic_eden_price) {
		this.magic_eden_price = magic_eden_price;
	}

	public float getDiff() {
		return diff;
	}

	public void setDiff(float diff) {
		this.diff = diff;
	}

	@Override
	public int compare(NFTCollectionView o1, NFTCollectionView o2) {
		return o1.getCollection_name().compareTo(o2.getCollection_name());
	}

}
