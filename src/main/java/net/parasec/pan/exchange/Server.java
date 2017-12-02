package net.parasec.pan.exchange;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.zeromq.ZMQ;

import net.parasec.pan.exchange.wire.ExchangeWire;


public class Server { 

	private Exchange exchange;

	
	public Server(Exchange exchange) {
		this.exchange = exchange;
	}

	private void handleCancel(ExchangeWire.Response.Builder resBuilder,
				  ExchangeWire.Command command) {
		ExchangeWire.Command.Cancel cancel = command.getCancel();
                String orderId = cancel.getOrderId();
                ExchangeResponse er = exchange.cancel(orderId);
                if(er.isOk()) {
                	resBuilder.setStatus(ExchangeWire.Response.Status.OK);
                } else {
                	resBuilder.setStatus(ExchangeWire.Response.Status.NOK);
                }
	}

	private void handleLimit(ExchangeWire.Response.Builder resBuilder,
                                 ExchangeWire.Command command) {
		ExchangeWire.Limit limit = command.getLimit();
                String market = limit.getAsset();
                Direction dir = limit.getSide().equals(ExchangeWire.Limit.Side.BID)
                		? Direction.BID : Direction.ASK;
                long vol = limit.getVolume();
                long price = limit.getPrice();
                ExchangeOrderResponse eor = exchange.limitOrder(market, dir, vol, price);
                if(eor.isOk()) {
                	String orderId = eor.getExchangeOrderId();
                        resBuilder.setOrderId(orderId).setStatus(ExchangeWire.Response.Status.OK);
                } else {
                	resBuilder.setStatus(ExchangeWire.Response.Status.NOK);
                }
	}

	public void start(String host, int port) throws Exception {

        	ZMQ.Context context = ZMQ.context(1);
        	ZMQ.Socket socket = context.socket(ZMQ.REP);

		try {
        		socket.bind("tcp://" + host + ":" + port);

        		while(!Thread.currentThread().isInterrupted()) {
			
            			byte[] req = socket.recv(0);
		
				ExchangeWire.Response.Status status = ExchangeWire.Response.Status.NOK;
				ExchangeWire.Response.Builder resBuilder = ExchangeWire.Response.newBuilder();

				try {
					ExchangeWire.Command command = ExchangeWire.Command.parseFrom(req);
				
					switch(command.getType()) {
						case CANCEL: handleCancel(resBuilder, command); break;
						case LIMIT:  handleLimit(resBuilder, command);
					}
				} catch(Exception e) {
					System.err.println(e);
				}
				ExchangeWire.Response exchangeRes = resBuilder.build();
				byte[] res = exchangeRes.toByteArray();
				socket.send(res, 0);
        		}
		} finally {
        		socket.close();
        		context.term();
		}
	}

	public static void main(String[] args) throws Exception {
		if(args.length == 0) {
			System.out.println("<json conf>");
			System.exit(0);
		}
		String jsonConf = args[0];
		
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(new FileReader(jsonConf));
		JSONObject serverConf = (JSONObject) jsonObj.get("server");
		JSONObject exchangeConf = (JSONObject) jsonObj.get("exchange");
		
		String host = (String) serverConf.get("host");
		int port = Integer.parseInt((String) serverConf.get("port"));

		String cid = (String) exchangeConf.get("cid");
		String key = (String) exchangeConf.get("key");
		String sec = (String) exchangeConf.get("sec");

		Exchange exchange = new FakeBitstampExchange(cid, key, sec);
		Server server = new Server(exchange);
		server.start(host, port);
	}
}
