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
			
			System.out.println("waiting");

			// raw in
            		byte[] in = socket.recv(0);

			System.out.println("message received");
			System.out.println(java.util.Arrays.toString(in));
			
			OMSWire.OMSResponse.Status status = OMSWire.OMSResponse.Status.OK;
			try {
			// request 
			OMSWire.OMSRequest omsRequest = OMSWire.OMSRequest.parseFrom(in);
			OMSWire.OMSRequest.Command command = omsRequest.getCommand();
            		System.out.println("command: " + command.name());
			
			} catch(Exception e) {
				e.printStackTrace();
				status = OMSWire.OMSResponse.Status.NOK;
			}

			// response
			OMSWire.OMSResponse omsResponse = OMSWire.OMSResponse.newBuilder()
					.setStatus(status)
					.setInternalOrderId(orderId++)
					.build();
	
			// raw out
			// .getBytes(ZMQ.CHARSET) ?
			byte[] out = omsResponse.toByteArray();
            		boolean sent = socket.send(out, 0);
			System.out.println("sent " + sent + " bytes");
        	}
        	socket.close();
        	context.term();
	}
}
