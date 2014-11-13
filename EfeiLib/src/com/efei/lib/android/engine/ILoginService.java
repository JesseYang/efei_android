package com.efei.lib.android.engine;

import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.ReqLogin;

public interface ILoginService {
	IJob login(ReqLogin reqLogin);
}
