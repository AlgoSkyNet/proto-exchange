// test java client

package net.parasec.pan.exchange;

public class Client {

	public static void main(String[] args) {
		Exchange exchange = new RemoteExchange("localhost", 5555);
		
		int m = 10;
		long l = System.currentTimeMillis();
		for(int i = 0; i < m; i++) {
			// 1 btc (100000000 satoshi) for 1 dollar (100 cent)
			ExchangeOrderResponse eor = exchange.limitOrder("btcusd", Direction.ASK, 100000000, 100);
			if(eor.isOk()) {
				System.out.println(eor.getExchangeOrderId());
			} else {
				System.err.println("nope.");
			}



		}
		long f = (System.currentTimeMillis() - l);
		System.out.println(m + " messages in " + f + " ms.");
		System.out.println((m / (f*0.001)) + " mesages per second.");
	}



}
