package net.parasec.pan.exchange;

public interface Exchange {
	ExchangeOrderResponse limitOrder(String market, Direction direction, long volume, long price);
	ExchangeResponse cancel(int orderId);
}
