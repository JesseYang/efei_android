package com.efei.lib.android.engine;

import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.bean.net.ReqLogin;
import com.efei.lib.android.bean.net.ReqRegister;
import com.efei.lib.android.bean.net.RespLogin;

public interface ILoginService {
	IJob register(ReqRegister reqRegister , IUICallback<RespLogin> uiCallback);
	IJob login(ReqLogin reqLogin, IUICallback<RespLogin> uiCallback);
}
