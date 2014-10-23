package zhy.flygle.xmpp.utils;

import java.io.IOException;

public abstract class Threadutils extends Thread{
	
	public final static String SERVER_HOST="192.168.2.196";
	public final static int SERVER_PORT=5222;
	public final static String SERVER_NAME="flygle_server";
	
	abstract void open() throws IOException;
	abstract void close() throws IOException;

}
