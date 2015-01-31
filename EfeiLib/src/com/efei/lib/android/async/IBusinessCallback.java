package com.efei.lib.android.async;

public interface IBusinessCallback<Result>
{
	Result onBusinessLogic(IJob job) throws Exception;
}