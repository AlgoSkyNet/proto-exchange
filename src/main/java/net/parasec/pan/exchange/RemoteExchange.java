package net.parasec.pan.exchange;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.protobuf.InvalidProtocolBufferException;

import net.parasec.pan.exchange.wire.ExchangeWire;

public class RemoteExchange implements Exchange {

	private Socket socket;


	public RemoteExchange(String host, int port) {
		Context context = ZMQ.context(1);
		this.socket = context.socket(ZMQ.REQ);	
		this.socket.connect("tcp://" + host + ":" + port);
	}

	private ExchangeWire.Response reqRep(ExchangeWire.Command command) 
			throws InvalidProtocolBufferException {
		byte[] commandRaw = command.toByteArray();
                socket.send(commandRaw);
		byte[] reply = socket.recv(0);
		return ExchangeWire.Response.parseFrom(reply);
	}

	public ExchangeOrderResponse limitOrder(String market, 
						Direction direction,
						long volume,
						long price) {

		ExchangeWire.Limit limit = ExchangeWire.Limit.newBuilder()
                                .setSide(direction.equals(Direction.BID) 
						? ExchangeWire.Limit.Side.BID
						: ExchangeWire.Limit.Side.ASK)
                                .setPrice(price)
                                .setVolume(volume)
                                .setAsset(market)
                                .build();

		ExchangeWire.Command command = ExchangeWire.Command.newBuilder()
                                .setType(ExchangeWire.Type.LIMIT)
                                .setLimit(limit)
                                .build();
		try {
			ExchangeWire.Response response = reqRep(command);	
			ExchangeWire.Response.Status status = response.getStatus();

			if(status.equals(ExchangeWire.Response.Status.OK)) {
                		String orderId = response.getOrderId();
				return new ExchangeOrderResponse(orderId, null);
                	} else {
				return new ExchangeOrderResponse(false, ExchangeError.UNKNOWN, null);
                	}
		} catch(InvalidProtocolBufferException e) {
			return new ExchangeOrderResponse(false, ExchangeError.UNKNOWN, e.getMessage());
		}	
	}	
        
	public ExchangeResponse cancel(String orderId) {
		
		ExchangeWire.Command.Cancel cancel = ExchangeWire.Command.Cancel.newBuilder()
				.setOrderId(orderId)
				.build();
		
		ExchangeWire.Command command = ExchangeWire.Command.newBuilder()
                                .setType(ExchangeWire.Type.CANCEL)
                                .setCancel(cancel)
                                .build();		
		try {
			ExchangeWire.Response response = reqRep(command); 
                	ExchangeWire.Response.Status status = response.getStatus();
                
                	if(status.equals(ExchangeWire.Response.Status.OK)) {
                        	return new ExchangeResponse();
                	} else {
                        	return new ExchangeResponse(false, ExchangeError.UNKNOWN, null);
                	}
		} catch(InvalidProtocolBufferException e) {
			return new ExchangeOrderResponse(false, ExchangeError.UNKNOWN, e.getMessage());
		}
	}
}
