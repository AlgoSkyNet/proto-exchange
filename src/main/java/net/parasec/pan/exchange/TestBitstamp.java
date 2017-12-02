// test bitstamp

package net.parasec.pan.exchange;

public class TestBitstamp {
	public static void main(String[] args) {
		Exchange exchange = new RemoteExchange("localhost", 5555);

		for(int i = 0; i < 100; i++) {
			// buy 1 btc @ $5
			ExchangeOrderResponse eor = exchange.limitOrder("btcusd", Direction.BID, 100000000, 500);
			if(eor.isOk()) {
				String orderId = eor.getExchangeOrderId();
				System.out.println(orderId);
				// cancel
				//ExchangeResponse er = exchange.cancel(orderId);
				//if(er.isOk()) {
				//	System.out.println("canceled");
				//} else {
				//	System.out.println("could not cancel");
				//	System.exit(0);
				//}
			} else {
				System.out.println("could not place order");
				System.exit(0);
			}
		}
	}
}
