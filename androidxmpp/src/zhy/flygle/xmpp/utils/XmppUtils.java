package zhy.flygle.xmpp.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.util.StringUtils;
import android.util.Log;

public class XmppUtils {
	
	private static XMPPConnection connection=null;
	private ConnectionListener connectionListener=null;
	private static XmppUtils xmppUtils=null;
	
	public static XmppUtils getInatance() {
		if(xmppUtils==null){
			xmppUtils=new XmppUtils();
		}
		return xmppUtils;
	}
	
	private XMPPConnection getConnection() {
		if(connection==null){
			conserver();
		}
		return connection;
	}
	
	public boolean conserver() {
		ConnectionConfiguration config=new ConnectionConfiguration(Threadutils.SERVER_HOST,Threadutils.SERVER_PORT,Threadutils.SERVER_NAME);
		config.setSecurityMode(SecurityMode.disabled);
		config.setDebuggerEnabled(true);
		config.setSendPresence(true);//离线
		connection=new XMPPConnection(config) {
			
			@Override
			protected void shutdown() {
				System.out.println("-----------shutdown");
				
			}
			
			@Override
			protected void sendPacketInternal(Packet arg0) throws NotConnectedException {
				System.out.println("-----------sendPacketInternal"+arg0);
				
			}
			
			@Override
			public void loginAnonymously() throws XMPPException, SmackException,
					SaslException, IOException {
				System.out.println("-----------loginAnonymously");
				
			}
			
			@Override
			public void login(String account, String password, String attributes)
					throws XMPPException, SmackException, SaslException, IOException {
				System.out.println("-----------login--帐号："+account+"---密码："+password+"---服务名："+attributes);
				
			}
			
			@Override
			public boolean isUsingCompression() {
				System.out.println("-----------isUsingCompression");
				return false;
			}
			
			@Override
			public boolean isSecureConnection() {
				System.out.println("-----------isSecureConnection");
				return false;
			}
			
			@Override
			public boolean isConnected() {
				System.out.println("-----------isConnected");
				return true;
			}
			
			@Override
			public boolean isAuthenticated() {
				System.out.println("-----------isAuthenticated");
				return false;
			}
			
			@Override
			public boolean isAnonymous() {
				System.out.println("-----------isAnonymous");
				return false;
			}
			
			@Override
			public String getUser() {
				System.out.println("-----------getUser");
				return null;
			}
			
			@Override
			public String getConnectionID() {
				System.out.println("-----------getConnectionID");
				return null;
			}
			
			@Override
			protected void connectInternal() throws SmackException, IOException,
					XMPPException {
				System.out.println("-----------connectInternal");
				
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
	 * @return -1、无连接 0、服务器没有返回结果1、注册失败2、注册成功
	 */
	public int regist(String account, String password) {
		if (getConnection() == null)
			return -1;
		
		Registration reg = new Registration();
		reg.setType(IQ.Type.SET);
		reg.setTo(getConnection().getServiceName());
		
		Map<String, String> attributes=new HashMap<String, String>();
		attributes.put("username", account);
		attributes.put("password", password);
		attributes.put("android", "createUser_android");
		reg.setAttributes(attributes);
		
		PacketFilter packetFilter = new IQReplyFilter(reg,getConnection());
		// Create the packet collector before sending the packet
		PacketCollector packetCollector = getConnection().createPacketCollector(packetFilter);
		// Now we can send the packet as the collector has been created
		try {
			getConnection().sendPacket(reg);
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		
		Packet result = packetCollector.nextResult(getConnection().getPacketReplyTimeout());
		packetCollector.cancel();
		if (result == null) {
			Log.e("XmppUtils", "No response from server.");
			return 0;
		}
		XMPPError xmppError = result.getError();
		if (xmppError != null) {
			Log.e("RegistActivity", "IQ.Type.ERROR: "
					+ result.getError().toString());
			return 1;
		}
		return 2;
		
	}
	

	public boolean login(String account, String password) {
		try {
			connection.login(account,password);
			Presence presence = new Presence(Presence.Type.available); 
			getConnection().sendPacket(presence);
			connectionListener=new ConnectionListener() {
				
				@Override
				public void reconnectionSuccessful() {
					System.out.println("ConnectionListener-----reconnectionSuccessful");
					
				}
				
				@Override
				public void reconnectionFailed(Exception arg0) {
					System.out.println("ConnectionListener-----reconnectionFailed-----"+arg0);
				}
				
				@Override
				public void reconnectingIn(int arg0) {
					System.out.println("ConnectionListener-----reconnectingIn-----"+arg0);
				}
				
				@Override
				public void connectionClosedOnError(Exception arg0) {
					System.out.println("ConnectionListener-----connectionClosedOnError------"+arg0);
				}
				
				@Override
				public void connectionClosed() {
					System.out.println("ConnectionListener-----connectionClosed");
				}
				
				@Override
				public void connected(XMPPConnection arg0) {
					System.out.println("ConnectionListener-----connected-----XMPPConnection------"+arg0);
				}
				
				@Override
				public void authenticated(XMPPConnection arg0) {
					System.out.println("ConnectionListener-------authenticated-------XMPPConnection-----"+arg0);
				}
			};
			getConnection().addConnectionListener(connectionListener);
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

	public boolean deleteAccount(String account) {
		try {    
			AccountManager.getInstance(getConnection()).deleteAccount();
	        return true;    
	    } catch (Exception e) {    
	        return false;    
	    }
	}

	public boolean changePassword(String password) {
		try {    
			AccountManager.getInstance(getConnection()).changePassword(password);
	        return true;    
	    } catch (Exception e) {    
	        return false;    
	    }
	}

	public void setPresence(int status) throws NotConnectedException {
	    Presence presence;  
	    switch (status) {  
	        case 0:  
	            presence = new Presence(Presence.Type.available);  
	            getConnection().sendPacket(presence);  
	            Log.v("state", "设置在线");  
	            break;  
	        case 1:  
	            presence = new Presence(Presence.Type.available);  
	            presence.setMode(Presence.Mode.chat);  
	            getConnection().sendPacket(presence);  
	            Log.v("state", "设置Q我吧");  
	            System.out.println(presence.toXML());  
	            break;  
	        case 2:  
	            presence = new Presence(Presence.Type.available);  
	            presence.setMode(Presence.Mode.dnd);  
	            getConnection().sendPacket(presence);  
	            Log.v("state", "设置忙碌");  
	            System.out.println(presence.toXML());  
	            break;  
	        case 3:  
	            presence = new Presence(Presence.Type.available);  
	            presence.setMode(Presence.Mode.away);  
	            getConnection().sendPacket(presence);  
	            Log.v("state", "设置离开");  
	            System.out.println(presence.toXML());  
	            break;  
	        case 4:  
	            Roster roster = connection.getRoster();  
	            Collection<RosterEntry> entries = roster.getEntries();  
	            for (RosterEntry entry : entries) {  
	                presence = new Presence(Presence.Type.unavailable);  
	                presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
	                presence.setFrom(getConnection().getUser());  
	                presence.setTo(entry.getUser());  
	                getConnection().sendPacket(presence);  
	                System.out.println(presence.toXML());  
	            }  
	            // 向同一用户的其他客户端发送隐身状态  
	            presence = new Presence(Presence.Type.unavailable);  
	            presence.setPacketID(Packet.ID_NOT_AVAILABLE);  
	            presence.setFrom(connection.getUser());  
	            presence.setTo(StringUtils.parseBareAddress(getConnection().getUser()));  
	            getConnection().sendPacket(presence);  
	            Log.v("state", "设置隐身");  
	            break;  
	        case 5:  
	            presence = new Presence(Presence.Type.unavailable);  
	            getConnection().sendPacket(presence);  
	            Log.v("state", "设置离线");  
	            break;  
	        default:  
	            break;  
	        }  
	    }

}
