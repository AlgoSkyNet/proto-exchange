package net.parasec.pan.exchange;

import org.zeromq.ZMQ;

import net.parasec.pan.exchange.wire.ExchangeWire;


public class Server { 

	public static void main(String[] args) throws Exception {

        	ZMQ.Context context = ZMQ.context(1);
        	ZMQ.Socket socket = context.socket(ZMQ.REP);
        	socket.bind("tcp://*:5555");

		System.out.println("johnny 5 alive");

		byte[] out = "REP".getBytes(ZMQ.CHARSET);		

        	while(!Thread.currentThread().isInterrupted()) {
			
			System.out.println("waiting");
            		byte[] req = socket.recv(0);
		
			ExchangeWire.Response.Status status = ExchangeWire.Response.Status.NOK;
			ExchangeWire.Response.Builder resBuilder = ExchangeWire.Response.newBuilder();

			try {
				ExchangeWire.Command command = ExchangeWire.Command.parseFrom(req);
				
				switch(command.getType()) {
					case CANCEL: 
						resBuilder.setStatus(ExchangeWire.Response.Status.OK);
					break;
					case LIMIT:
						int orderId = 123;
						resBuilder.setOrderId(orderId).setStatus(ExchangeWire.Response.Status.OK);
					break;
					default:
						resBuilder.setStatus(ExchangeWire.Response.Status.NOT_SUPPORTED);
				}

			} catch(Exception e) {
				System.err.println(e);
			}
			ExchangeWire.Response exchangeRes = resBuilder.build();
			byte[] res = exchangeRes.toByteArray();
			socket.send(res, 0);
        	}
        	socket.close();
        	context.term();
	}


}
