package net.parasec.pan.exchange;


public class FakeBitstampExchange implements Exchange {

	private String cid;
	private String key;
	private String sec;

	private int lastNonce = 0;

	private int fakeId = 31337;
	

	public FakeBitstampExchange(String cid, String key, String sec) {
		this.cid = cid;
		this.key = key;
		this.sec = sec;
	}

	public ExchangeOrderResponse limitOrder(String market, Direction direction, long volume, long price) {
		//System.out.println(market + "/" + direction + "/" + volume + "/" + price);
		return new ExchangeOrderResponse(Integer.toString(fakeId++), "OK");
	}

	public ExchangeResponse cancel(String orderId) { 
		System.out.println("cancel " + orderId);
		return new ExchangeResponse();
	}
}
