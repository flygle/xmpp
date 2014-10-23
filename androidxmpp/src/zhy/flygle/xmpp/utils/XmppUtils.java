package zhy.flygle.xmpp.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.util.StringUtils;

import android.util.Log;

public class XmppUtils {
	
	private final static String SERVER_HOST="127.0.0.1";
	private final static int SERVER_PORT=5222;
	private final static String SERVER_NAME="flygleServer";
	private static XMPPConnection connection=null;
	
	private static XmppUtils xmppUtils=null;
	
	public static XmppUtils getInatance() {
		if(xmppUtils==null){
			xmppUtils=new XmppUtils();
		}
		return xmppUtils;
	}
	
	
	public boolean login(String account, String password) {
		try {
			connection.login(account,password);
			return true;
		} catch (SaslException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean conserver() {
		ConnectionConfiguration config=new ConnectionConfiguration(SERVER_HOST,SERVER_PORT, SERVER_NAME);
		config.setDebuggerEnabled(true);
		config.setSendPresence(true);//离线
		connection=new XMPPConnection(config) {
			
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
		try {
			connection.connect();
			return true;
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void closeConnection(){
		if(connection!=null){
			if(connection.isConnected()){
				try {
					connection.disconnect();
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			connection=null;
		}
	}

	/**
	 * 注册
	 * 
	 * @param account 注册帐号
	 * @param password 注册密码
	 * @return 1、注册成功 0、服务器没有返回结果2、这个账号已经存在3、注册失败
	 */
	public int regist(String account, String password) {
		if (connection == null)
			return 0;
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(connection.getServiceName());
//		reg.s// 注意这里createAccount注册时，参数是username，不是jid，是“@”前面的部分。
//		reg.setPassword(password);
		Map<String, String> attribute=new HashMap<String, String>();
		attribute.put("android", "geolo_createUser_android");
		reg.setAttributes(attribute);// 这边addAttribute不能为空，否则出错。所以做个标志是android手机创建的吧！！！！！
		PacketFilter filter = new AndFilter(new PacketIDFilter(
				reg.getPacketID()), new PacketTypeFilter(IQ.class));
		PacketCollector collector = connection.createPacketCollector(filter);
		try {
			connection.sendPacket(reg);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		IQ result = (IQ) collector.nextResult(SmackConfiguration.getDefaultPacketReplyTimeout());
		collector.cancel();// 停止请求results（是否成功的结果）
		if (result == null) {
			Log.e("RegistActivity", "无返回值");
			return 0;
		} else if (result.getType() == IQ.Type.RESULT) {
			return 1;
		} else { // if (result.getType() == IQ.Type.ERROR)
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				Log.e("RegistActivity", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return 2;
			} else {
				Log.e("RegistActivity", "IQ.Type.ERROR: "
						+ result.getError().toString());
				return 3;
			}
		}
	}

	public boolean deleteAccount(String account) {
		if (connection == null){
			return false;
		}
		try {    
			AccountManager.getInstance(connection).deleteAccount();
	        return true;    
	    } catch (Exception e) {    
	        return false;    
	    }
	}

	public boolean changePassword(String password) {
		if (connection == null){
			return false;
		}
		try {    
			AccountManager.getInstance(connection).changePassword(password);
	        return true;    
	    } catch (Exception e) {    
	        return false;    
	    }
	}

	public void setPresence(int status) throws NotConnectedException {
		if (connection == null)  
	        return;  
	    Presence presence;  
	    switch (status) {  
	        case 0:  
	            presence = new Presence(Presence.Type.available);  
	            connection.sendPacket(presence);  
	            Log.v("state", "设置在线");  
	            break;  
	        case 1:  
	            presence = new Presence(Presence.Type.available);  
	            presence.setMode(Presence.Mode.chat);  
	            connection.sendPacket(presence);  
	            Log.v("state", "设置Q我吧");  
	            System.out.println(presence.toXML());  
	            break;  
	        case 2:  
	            presence = new Presence(Presence.Type.available);  
	            presence.setMode(Presence.Mode.dnd);  
	            connection.sendPacket(presence);  
	            Log.v("state", "设置忙碌");  
	            System.out.println(presence.toXML());  
	            break;  
	        case 3:  
	            presence = new Presence(Presence.Type.available);  
	            presence.setMode(Presence.Mode.away);  
	            connection.sendPacket(presence);  
	            Log.v("state", "设置离开");  
	            System.out.println(presence.toXML());  
	            break;  
	        case 4:  
	            Roster roster = connection.getRoster();  
	            Collection<RosterEntry> entries = roster.getEntries();  
	            for (RosterEntry entry : entries) {  
	                presence = new Presence(Presence.Type.unavailable);  
	                presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
	                presence.setFrom(connection.getUser());  
	                presence.setTo(entry.getUser());  
	                connection.sendPacket(presence);  
	                System.out.println(presence.toXML());  
	            }  
	            // 向同一用户的其他客户端发送隐身状态  
	            presence = new Presence(Presence.Type.unavailable);  
	            presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
	            presence.setFrom(connection.getUser());  
	            presence.setTo(StringUtils.parseBareAddress(connection.getUser()));  
	            connection.sendPacket(presence);  
	            Log.v("state", "设置隐身");  
	            break;  
	        case 5:  
	            presence = new Presence(Presence.Type.unavailable);  
	            connection.sendPacket(presence);  
	            Log.v("state", "设置离线");  
	            break;  
	        default:  
	            break;  
	        }  
	    }  
}
