package com.efei.android.module.scan;

import java.util.ArrayList;
import java.util.List;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueScanService;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddBatchQues;

public final class BizRunner_SaveQues implements IBusinessCallback<RespAddBatchQues>
{
	private List<QuestionOrNote2> queOrNotes;

	public BizRunner_SaveQues(List<QuestionOrNote2> queOrNotes)
	{
		this.queOrNotes = queOrNotes;
	}

	@Override
	public RespAddBatchQues onBusinessLogic(IJob job)
	{
		List<String> question_ids = new ArrayList<String>();
		for (QuestionOrNote2 note2 : queOrNotes)
			question_ids.add(note2.metaData.get_id());
		return IQueScanService.Factory.getService().post0student$notes$batch(question_ids.toArray(new String[0]));
	}
}
