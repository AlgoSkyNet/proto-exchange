package net.parasec.pan.exchange;

public class ExchangeResponse {
  
	private boolean ok;
	private ExchangeError exchangeError;
	private String message;
  
  
	public ExchangeResponse() {
    		this(true, null, null);  
  	}

  	public ExchangeResponse(final String message) {
    		this(true, null, message);
  	}

  	public ExchangeResponse(boolean ok, ExchangeError exchangeError, String message) {
    		this.ok = ok;
    		this.exchangeError = exchangeError;
    		this.message = message;
  	}

	public boolean isOk() {
    		return ok;
  	}

  	public ExchangeError getExchangeError() {
    		return exchangeError;
  	}

  	public String getMessage() {
    		return message;
  	}
}
