// test

package net.parasec.pan.exchange;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import net.parasec.pan.exchange.wire.ExchangeWire;

public class Client {

	public static void main(String[] args) {
		String id = args[0];
		System.out.println(id);

		Context context = ZMQ.context(1);

        	Socket req = context.socket(ZMQ.REQ);
        	req.connect("tcp://localhost:5555");

		ExchangeWire.Limit limit = ExchangeWire.Limit.newBuilder()
				.setSide(ExchangeWire.Limit.Side.BID)
				.setPrice(123L)
				.setVolume(123L)
				.setAsset("ETHUSD")
				.build();
		ExchangeWire.Command command = ExchangeWire.Command.newBuilder()
				.setType(ExchangeWire.Type.LIMIT)
				.setLimit(limit)
				.build();

		int m = 1000000;
		long l = System.currentTimeMillis();
		for(int i = 0; i < m; i++) {
			byte[] commandRaw = command.toByteArray();
			req.send(commandRaw);
			byte[] rep = req.recv(0);
			try {
				ExchangeWire.Response response = ExchangeWire.Response.parseFrom(rep);
				if(response.getStatus().equals(ExchangeWire.Response.Status.OK)) {
					int orderId = response.getOrderId();
					assert orderId == 123;
				} else {
					System.err.println("nope.");
				}
			} catch(Exception e) {
				System.err.println(e);
			}
		}
		long f = (System.currentTimeMillis() - l);
		System.out.println(m + " messages in " + f + " ms.");
		System.out.println((m / (f*0.001)) + " mesages per second.");
        	req.close();
        	context.term();
	}



}
