package net.parasec.pan.oms;

import org.zeromq.ZMQ;

public class Server { 
	public static void main(String[] args) throws Exception {
        	ZMQ.Context context = ZMQ.context(1);
        	ZMQ.Socket socket = context.socket(ZMQ.REP);
        	socket.bind("tcp://*:5555");

        	while (!Thread.currentThread().isInterrupted()) {
            		byte[] in = socket.recv(0);
            		System.out.println("in [" + new String(in, ZMQ.CHARSET) + "]");
            		socket.send("ok".getBytes(ZMQ.CHARSET), 0);
        	}
        	socket.close();
        	context.term();
	}
}
