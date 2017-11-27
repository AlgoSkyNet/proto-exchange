package net.parasec.pan.exchange;

public class ExchangeOrderResponse extends ExchangeResponse {
	private String exchangeOrderId;


	public ExchangeOrderResponse(String exchangeOrderId, String message) {
    		super(message);
    		this.exchangeOrderId = exchangeOrderId;
  	}

  	public ExchangeOrderResponse(boolean ok, ExchangeError e, String message) {
    		super(ok, e, message);
    		exchangeOrderId = null;
  	}
  
  	public String getExchangeOrderId() {
    		return exchangeOrderId;
 	}
}
