package com.efei.android.module.edit;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueScanService;

public final class BizRunner_SaveQue implements IBusinessCallback<BaseRespBean>
{
	private final QuestionOrNote2 queOrNote;
	private final boolean bCreate;

	public BizRunner_SaveQue(QuestionOrNote2 queOrNote, boolean create)
	{
		this.queOrNote = queOrNote;
		this.bCreate = create;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job)
	{
		RespQueOrNote metaData = queOrNote.metaData;
		if (bCreate)
			return IQueScanService.Factory.getService().post0student$note(metaData.get_id(), metaData.getTag(), metaData.getTopic_str(),
					metaData.getSummary(),queOrNote.homework_id);
		else
			return IQueOrNoteLookUpService.Factory.getService().put0student$notes(metaData.get_id(), metaData.getTag(), metaData.getTopic_str(),
					metaData.getSummary());
	}
}
