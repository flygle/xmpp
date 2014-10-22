<<<<<<< HEAD
package zhy.flygle.xmpp;

import java.io.IOException;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Packet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {
	
	private int SERVER_PORT = 5222;  
	private String SERVER_HOST = "127.0.0.1";  
	private XMPPConnection connection = null;  
	private String SERVER_NAME = "zhy.flygle.android";  


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
	}

	/**
	 * 连接服务器
	 * 
	 * @return
	 */
	public boolean conServer() {
		ConnectionConfiguration config = new ConnectionConfiguration(SERVER_HOST, SERVER_PORT, SERVER_NAME);
		/** 设置是否在线 */
		config.setSendPresence(false);
		/** 是否启用调试 */
		// config.setDebuggerEnabled(true);
		try {
			connection = new XMPPConnection(config) {
				
				@Override
				protected void shutdown() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				protected void sendPacketInternal(Packet arg0) throws NotConnectedException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void loginAnonymously() throws XMPPException, SmackException,
						SaslException, IOException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void login(String arg0, String arg1, String arg2)
						throws XMPPException, SmackException, SaslException, IOException {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean isUsingCompression() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean isSecureConnection() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean isConnected() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean isAuthenticated() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public boolean isAnonymous() {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public String getUser() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public String getConnectionID() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				protected void connectInternal() throws SmackException, IOException,
						XMPPException {
					// TODO Auto-generated method stub
					
				}
			};
			connection.connect();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
=======
package zhy.flygle.xmpp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
>>>>>>> branch 'master' of https://github.com/flygle/xmpp.git
