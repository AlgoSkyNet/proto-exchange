// test

package net.parasec.pan.oms;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import net.parasec.pan.oms.wire.OMSWire;


public class Client {

	public static void main(String[] args) {

		Context context = ZMQ.context(1);

        	Socket req = context.socket(ZMQ.REQ);
        	req.connect("tcp://localhost:5555");

		int m = 1000000;
		long l = System.currentTimeMillis();
		for(int i = 0; i < m; i++) {
			req.send("REQ", 0);
			byte[] rep = req.recv(0);
			//System.out.println(java.util.Arrays.toString(rep));
		}
		long f = (System.currentTimeMillis() - l);
		System.out.println(m + " messages in " + f + " ms.");
		System.out.println((m / (f*0.001)) + " mesages per second.");
        	req.close();
        	context.term();
	}



}
