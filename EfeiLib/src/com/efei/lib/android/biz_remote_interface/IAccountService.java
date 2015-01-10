package com.efei.lib.android.biz_remote_interface;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.bean.net.BaseRespBean;

public interface IAccountService
{
	public static class Factory
	{
		private Factory()
		{
		}

		private static IAccountService service;

		public synchronized static IAccountService getService()
		{
			if (null == service)
				service = new AccountImpl();
			return service;
		}
	}
	
	/**
	 * ��¼��post@account/sessions �� account/sessions_controller#create</br>
	 * <b>����</b>
	 * <li>email_mobile����������ֻ�
	 * <li>password������</br>
	 * <b>����ֵ</b>
	 * <li>code��BLANK_EMAIL_MOBILE��USER_NOT_EXIST��WRONG_PASSWORD
	 * <li>auth_key����Ȩ�룬����δ���������ʹ��
	 */
	BaseRespBean post0account$sessions(String email_mobile , String password);
	public static class ReqLogin extends ABaseReqBean
	{
		private String email_mobile;
		private String password;
		public String getEmail_mobile()
		{
			return email_mobile;
		}
		public void setEmail_mobile(String email_mobile)
		{
			this.email_mobile = email_mobile;
		}
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password = password;
		}
	}
	
	/**
	 * ע�᣺post@account/registrations �� account/registrations_controller#create</br>
	 * <b>����</b>
	 * <li>email_mobile���ֻ���
	 * <li>password������
	 * <li>name������</br>
	 * <b>����ֵ</b>
	 * <li>code��BLANK_EMAIL_MOBILE��USER_EXIST
	 * <li>auth_key����Ȩ�루�Զ���¼��
	 */
	BaseRespBean post0account$registrations(String email_mobile , String password , String name);
	public static class ReqRegister extends ReqLogin
	{
		private String name;
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
	}
	
	/**
	 * �һ�����ʱ�ύ�˺���Ϣ��post@account/passwords �� account/passwords_controller#create</br>
	 * <b>����</b>
	 * <li>email_mobile���ֻ���</br>
	 * <b>����ֵ</b>
	 * <li>code��USER_NOT_EXIST
	 */
	BaseRespBean post0account$passwords(String email_mobile);
	
	/**
	 * �һ�����ʱ���������֤�룺post@account/passwords/verify_code �� account/passwords_controller#verify_code</br>
	 * <b>����</b>
	 * <li>mobile���ֻ���
	 * <li>verify_code����֤�루����ʱ��֤��һ��Ϊ111111��</br>
	 * <b>����ֵ</b>
	 * <li>code��USER_NOT_EXIST��WRONG_VERIFY_CODE
	 * <li>reset_password_token����ֵ�����key
	 */
	RespVerifyPwd post0account$passwords$verify_code(String mobile , String verify_code);
	public static class ReqVerifyPwd extends ABaseReqBean
	{
		private String mobile;
		private String verify_code;
		public String getMobile()
		{
			return mobile;
		}
		public void setMobile(String mobile)
		{
			this.mobile = mobile;
		}
		public String getVerify_code()
		{
			return verify_code;
		}
		public void setVerify_code(String verify_code)
		{
			this.verify_code = verify_code;
		}
	}
	public static class RespVerifyPwd extends BaseRespBean
	{
		private String reset_password_token;
		public String getReset_password_token()
		{
			return reset_password_token;
		}
		public void setReset_password_token(String reset_password_token)
		{
			this.reset_password_token = reset_password_token;
		}
	}
	
	/**
	 * �ֻ��һ�����ʱ�������룺put@account/passwords �� 	account/passwords_controller#update</br>
	 * <b>����</b>
	 * <li>password
	 * <li>reset_password_token����ֵ�����key</br>
	 * <b>����ֵ</b>
	 * <li>code��WRONG_TOKEN��EXPIRED
	 */
	BaseRespBean put0account$passwords(String password , String reset_password_token);
	public static class ReqResetPwd extends ABaseReqBean
	{
		private String password;
		private String reset_password_token;
		public String getPassword()
		{
			return password;
		}
		public void setPassword(String password)
		{
			this.password = password;
		}
		public String getReset_password_token()
		{
			return reset_password_token;
		}
		public void setReset_password_token(String reset_password_token)
		{
			this.reset_password_token = reset_password_token;
		}
	}
}
