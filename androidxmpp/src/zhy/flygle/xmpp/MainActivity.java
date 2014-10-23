package zhy.flygle.xmpp;

import org.jivesoftware.smack.SmackException.NotConnectedException;

import zhy.flygle.xmpp.utils.XmppUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener{
	
	/**
	 * 注册
	 */
	private Button button01;
	/**
	 * 登录
	 */
	private Button button02;
	/**
	 * 退出
	 */
	private Button button03;
	/**
	 * 密码
	 */
	private Button button04;
	/**
	 * 状态
	 */
	private Button button05;
	
	private EditText account_edit,password_edit;
	private TextView account_status_text;
	
	private String account,password;
	
	private int status=0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		
		
		
		if(XmppUtils.getInatance().conserver()){
			showToast("连接成功");
		}
		
	}

	private void showToast(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	private void initView() {
		button01=(Button)findViewById(R.id.button01);
		button02=(Button)findViewById(R.id.button02);
		button03=(Button)findViewById(R.id.button03);
		button04=(Button)findViewById(R.id.button04);
		button05=(Button)findViewById(R.id.button05);
		account_edit=(EditText)findViewById(R.id.account_edit);
		password_edit=(EditText)findViewById(R.id.password_edit);
		account_status_text=(TextView)findViewById(R.id.account_status_text);
		
		button01.setOnClickListener(this);
		button02.setOnClickListener(this);
		button03.setOnClickListener(this);
		button04.setOnClickListener(this);
		button05.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * button1~5-----> 注册，登录，退出，密码，状态
		 */
		case R.id.button01:
			account=account_edit.getText().toString();
			password=password_edit.getText().toString();
			if(account!=null&&account.trim().equals("")&&password!=null&&password.trim().equals("")){
				int status=XmppUtils.getInatance().regist(account,password);
				if(status==0){
					showToast("注册成功");
				}
			}
			break;
		case R.id.button02:
			account=account_edit.getText().toString();
			password=password_edit.getText().toString();
			if(account!=null&&account.trim().equals("")&&password!=null&&password.trim().equals("")){
				if(XmppUtils.getInatance().login(account,password)){
					showToast("登录成功");
				}
			}
			break;
		case R.id.button03:
			account=account_edit.getText().toString();
			if(account!=null&&account.trim().equals("")){
				if(XmppUtils.getInatance().deleteAccount(account)){
					showToast("退出成功");
				}
			}
			break;
		case R.id.button04:
			password=password_edit.getText().toString();
			if(password!=null&&password.trim().equals("")){
				if(XmppUtils.getInatance().changePassword(password)){
					showToast("改密码成功");
				}
			}
			break;
		case R.id.button05:
			if(status!=5){
				status+=1;
			}else{
				status=0;
			}
			try {
				XmppUtils.getInatance().setPresence(status);
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
			switch (status) {
			case 0:
				account_status_text.setText("在线");
				break;
			case 1:
				account_status_text.setText("Q我吧");
				break;
			case 2:
				account_status_text.setText("忙碌");
				break;
			case 3:
				account_status_text.setText("离开");
				break;
			case 4:
				account_status_text.setText("隐身");
				break;
			case 5:
				account_status_text.setText("离线");
				break;

			default:
				break;
			}
			break;

		default:
			break;
		}
		
	}
	
	
	

	

}
