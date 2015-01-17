package com.efei.android.module.edit;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.biz_remote_interface.IQueScanService;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddSingleQue;

public final class BizRunner_SaveQue implements IBusinessCallback<RespAddSingleQue>
{
	private RespQueOrNote queOrNote;

	public BizRunner_SaveQue(RespQueOrNote queOrNote)
	{
		this.queOrNote = queOrNote;
	}

	@Override
	public RespAddSingleQue onBusinessLogic(IJob job)
	{
		return IQueScanService.Factory.getService().post0student$note(queOrNote.get_id(), queOrNote.getTag(), queOrNote.getTopics(),
				queOrNote.getSummary());
	}
}
