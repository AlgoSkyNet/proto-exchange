// test java client

package net.parasec.pan.exchange;

public class ManualOrder {

	public static void main(String[] args) {
		Exchange exchange = new RemoteExchange("localhost", 5555);
		System.out.println(java.util.Arrays.toString(args));

		String command = args[0];
		if(command.equals("bid")) {
			long vol = (long) Math.round(100000000*Double.parseDouble(args[1]));
			long price = (long) Math.round(100*Double.parseDouble(args[2]));
			ExchangeOrderResponse eor = exchange.limitOrder("btcusd", Direction.BID, vol, price);
			if(eor.isOk()) {
				System.out.println("id = " + eor.getExchangeOrderId());
			} else {
				System.err.println("nope");
			}
		} else if(command.equals("ask")) {
			long vol = (long) Math.round(100000000*Double.parseDouble(args[1]));
                        long price = (long) Math.round(100*Double.parseDouble(args[2]));
			ExchangeOrderResponse eor = exchange.limitOrder("btcusd", Direction.ASK, vol, price);
                        if(eor.isOk()) {
                                System.out.println("id = " + eor.getExchangeOrderId());
                        } else {
                                System.err.println("nope");
                        }

		} else if(command.equals("kill")) {
			String orderId = args[1];
			ExchangeResponse er = exchange.cancel(orderId);
                        if(er.isOk()) {
                                System.out.println("ok");
                        } else {
                                System.err.println("nope.");
                        }
		} else {
			System.out.println("bid, ask, kill");
		}	
	
	}

}
