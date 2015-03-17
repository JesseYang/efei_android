package com.efei.lib.android.biz_remote_interface;

import java.util.List;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;

public interface ISettingService
{
	public static class Factory
	{
		private Factory()
		{
		}

		private static ISettingService service;

		public synchronized static ISettingService getService()
		{
			if (null == service)
				service = new SettingImpl();
			return service;
		}
	}

	/**
	 * ��ȡ������Ϣ��get@/student/students/info �� student/students_controller#info</br> <b>������</b>��</br> <b>����ֵ��</b> <li>code��REQUIRE_SIGNIN <li>name������ <li>
	 * email�������ַ����Ϊ�ձ�ʾû�� <li>mobile���ֻ���
	 */
	RespStudentInfo get0student$students$info();

	public static class RespStudentInfo extends BaseRespBean
	{
		private String name;
		private String email;
		private String mobile;

		public String getName()
		{
			return name;
		}

		void setName(String name)
		{
			this.name = name;
		}

		public String getEmail()
		{
			return email;
		}

		void setEmail(String email)
		{
			this.email = email;
		}

		public String getMobile()
		{
			return mobile;
		}

		void setMobile(String mobile)
		{
			this.mobile = mobile;
		}
	}

	/**
	 * �޸�������put@/student/students/rename �� student/students_controller#rename <b>������</b> <li>name��������</br> <b>����ֵ��</b> <li>code��REQUIRE_SIGNIN
	 * 
	 * @return
	 */
	BaseRespBean put0student$students$rename(String name);

	public static class ReqRename extends ABaseReqBean
	{
		private String name;

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}

	/**
	 * �޸����룺put@/student/students/change_password �� student/students_controller#change_password</br> <b>������</b> <li>password������ <li>new_password��������</br>
	 * <b>����ֵ��</b> <li>code��REQUIRE_SIGNIN��WRONG_PASSWORD
	 * 
	 * @return
	 */
	BaseRespBean put0student$students$change_password(String password, String new_password);

	public static class ReqChangePwd extends ABaseReqBean
	{
		private String password;
		private String new_password;

		public void setPassword(String password)
		{
			this.password = password;
		}

		public void setNew_password(String new_password)
		{
			this.new_password = new_password;
		}

		public String getPassword()
		{
			return password;
		}

		public String getNew_password()
		{
			return new_password;
		}
	}

	/**
	 * �޸����䣺put@/student/students/change_email �� student/students_controller#change_email</br> <b>������</b> <li>email�������ַ</br> <b>����ֵ��</b> <li>
	 * code��REQUIRE_SIGNIN��USER_EXIST
	 * 
	 * @return
	 */
	BaseRespBean put0student$students$change_email(String email);

	public static class ReqChangeEmail extends ABaseReqBean
	{
		private String email;

		public void setEmail(String email)
		{
			this.email = email;
		}

		public String getEmail()
		{
			return email;
		}
	}

	/**
	 * �޸��ֻ��ţ�put@/student/students/change_mobile �� student/students_controller#change_mobile</br> <b>������</b> <li>mobile���ֻ���</br> <b>����ֵ��</b> <li>
	 * code��REQUIRE_SIGNIN��USER_EXIST
	 * 
	 * @return
	 */
	BaseRespBean put0student$students$change_mobile(String mobile);

	public static class ReqChangeMobile extends ABaseReqBean
	{
		private String mobile;

		public void setMobile(String mobile)
		{
			this.mobile = mobile;
		}

		public String getMobile()
		{
			return mobile;
		}
	}

	/**
	 * �޸��ֻ���ʱ���������֤�룺post@/student/students/verify_mobile �� student/students_controller#verify_mobile</br> <b>������</b> <li>verify_code����֤��</br> <b>����ֵ��</b>
	 * <li>code��REQUIRE_SIGNIN��WRONG_VERIFY_CODE
	 * 
	 * @return
	 */
	BaseRespBean post0student$students$verify_mobile(String verify_code);

	public static class ReqVerifyMobile extends ABaseReqBean
	{
		private String verify_code;

		public void setVerify_code(String verify_code)
		{
			this.verify_code = verify_code;
		}

		public String getVerify_code()
		{
			return verify_code;
		}
	}

	/**
	 * ��ȡ��ʦ�б�get@/student/teachers �� student/teachers_controller#index</br> <b>������</b> <li>scope��ȡ1����ʾ���ҵ�ǰ�û��Ľ�ʦ</br> <b>����ֵ��</b> <li>code��REQUIRE_SIGNIN
	 * <li>teachers�����飬��ʾ��ʦ�б�ÿһ��Ԫ��Ϊһ����ϣ���ṹΪ�� id����ʦid name����ʦ���� subject����ʦ��Ŀ school����ʦѧУ���� desc����ʦ���� avatar����ʦͷ���ַ
	 * 
	 * @return
	 */
	RespSearchTeachers get0student$teachers();

	/**
	 * ������ʦ��get@/student/teachers �� student/teachers_controller#index</br> <b>������</b> <li>scope��0����ʾȫ���ʦ��Χ������ <li>subject��������ʾ�Ŀ�Ŀ <li>name����ʦ����</br>
	 * <b>����ֵ��</b> <li>code��REQUIRE_SIGNIN <li>teachers�����飬��ʾ��ʦ�б�ÿһ��Ԫ��Ϊһ����ϣ���ṹΪ�� id����ʦid name����ʦ���� subject����ʦ��Ŀ school����ʦѧУ���� desc����ʦ���� avatar����ʦͷ���ַ
	 * 
	 * @return
	 */
	RespSearchTeachers get0student$teachers(int subject, String name);

	public static class ReqSearchTeachers extends ABaseReqBean
	{
		private int scope;
		private int subject;
		private String name;

		public void setScope(int scope)
		{
			this.scope = scope;
		}

		public int getScope()
		{
			return scope;
		}

		public void setSubject(int subject)
		{
			this.subject = subject;
		}

		public int getSubject()
		{
			return subject;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getName()
		{
			return name;
		}
	}

	public static class RespSearchTeachers extends BaseRespBean
	{
		private List<Teacher> teachers;

		void setTeachers(List<Teacher> teachers)
		{
			this.teachers = teachers;
		}

		public List<Teacher> getTeachers()
		{
			return teachers;
		}
	}

	/**
	 * ��ӽ�ʦ��post@/student/teachers �� student/teachers_controller#create</br> <b>������</b> <li>teacher_id����ʦid <li>class_id���༶id����Ϊ����ָ���༶</br> <b>����ֵ��</b> <li>
	 * code��REQUIRE_SIGNIN��TEACHER_NOT_EXIST
	 * 
	 * @return
	 */
	BaseRespBean post0student$teachers(String teacher_id, String class_id);

	public static class ReqAddTeacher extends ABaseReqBean
	{
		private String teacher_id;
		private String class_id;

		public void setTeacher_id(String teacher_id)
		{
			this.teacher_id = teacher_id;
		}

		public String getTeacher_id()
		{
			return teacher_id;
		}

		public void setClass_id(String class_id)
		{
			this.class_id = class_id;
		}

		public String getClass_id()
		{
			return class_id;
		}
	}

	/**
	 * ɾ����ʦ��delete@/student/teachers/#{teacher_id} �� student/teachers_controller#destroy</br> <b>������</b> <li>teacher_id��url������ʦid</br> <b>����ֵ��</b> <li>
	 * code��REQUIRE_SIGNIN��TEACHER_NOT_EXIST
	 * 
	 * @return
	 */
	BaseRespBean delete0student$teachers(String teacher_id);

	/**
	 * �ύ���������post@student/feedbacks �� student/feedbacks_controller#create</br> <b>������</b> <li>content�������������</br> <b>����ֵ��</b> <li>code��REQUIRE_SIGNIN
	 * 
	 * @return
	 */
	BaseRespBean post0student$feedbacks(String content);

	public static class ReqFeedBacks extends ABaseReqBean
	{
		private String content;

		public void setContent(String content)
		{
			this.content = content;
		}

		public String getContent()
		{
			return content;
		}
	}

	/**
	 *  http://dev.efei.org/welcome/app_version
	 * @return 
	 */
	RespLatestVersion get0latest$version();

	public static class RespLatestVersion extends BaseRespBean
	{
		private String android;
		private String android_url;

		public String getAndroid()
		{
			return android;
		}

		void setAndroid(String android)
		{
			this.android = android;
		}

		public String getAndroid_url()
		{
			return android_url;
		}

		void setAndroid_url(String android_url)
		{
			this.android_url = android_url;
		}
	}
}
