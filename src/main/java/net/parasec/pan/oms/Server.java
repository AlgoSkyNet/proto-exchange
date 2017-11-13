package net.parasec.pan.oms;

import org.zeromq.ZMQ;

import net.parasec.pan.oms.wire.OMSWire;


public class Server { 

	public static void main(String[] args) throws Exception {

		int orderId = 0;

        	ZMQ.Context context = ZMQ.context(1);
        	ZMQ.Socket socket = context.socket(ZMQ.REP);
        	socket.bind("tcp://*:5555");

		System.out.println("johnny 5 alive");
		
        	while (!Thread.currentThread().isInterrupted()) {

			// raw in
            		byte[] in = socket.recv(0);

			// request 
			OMSWire.OMSRequest omsRequest = OMSWire.OMSRequest.parseFrom(in);
			OMSWire.OMSRequest.Command command = omsRequest.getCommand();
            		System.out.println("command: " + command.name());

			// response
			OMSWire.OMSResponse.Status status = OMSWire.OMSResponse.Status.OK;
			OMSWire.OMSResponse omsResponse = OMSWire.OMSResponse.newBuilder()
					.setStatus(status)
					.setInternalOrderId(orderId++)
					.build();
	
			// raw out
			// .getBytes(ZMQ.CHARSET) ?
			byte[] out = omsResponse.toByteArray();
            		socket.send(out, 0);
        	}
        	socket.close();
        	context.term();
	}
}
