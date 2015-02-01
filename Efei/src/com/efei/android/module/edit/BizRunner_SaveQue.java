package com.efei.android.module.edit;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueScanService;

public final class BizRunner_SaveQue implements IBusinessCallback<BaseRespBean>
{
	private final RespQueOrNote queOrNote;
	private final boolean bCreate;

	public BizRunner_SaveQue(RespQueOrNote queOrNote, boolean create)
	{
		this.queOrNote = queOrNote;
		this.bCreate = create;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job)
	{
		if (bCreate)
			return IQueScanService.Factory.getService().post0student$note(queOrNote.get_id(), queOrNote.getTag(), queOrNote.getTopics(),
					queOrNote.getSummary());
		else
			return IQueOrNoteLookUpService.Factory.getService().put0student$notes(queOrNote.get_id(), queOrNote.getTag(), queOrNote.getTopics(),
					queOrNote.getSummary());
	}
}
