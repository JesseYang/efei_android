package com.efei.android.module.question;

import java.util.ArrayList;
import java.util.List;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespNestQueOrNoteArray;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespNoteId_UpdateTimeEntry;

final class BizRunner_QueList implements IBusinessCallback<List<RespQueOrNote>>
{
	@Override
	public List<RespQueOrNote> onBusinessLogic(IJob job)
	{
		IQueOrNoteLookUpService service = IQueOrNoteLookUpService.Factory.getService();
		RespNoteId_UpdateTimeEntry entrySet = service.get0student$notes();
		List<Object[]> entries = entrySet.getNotes();
		List<String> queIds = new ArrayList<String>();
		for (Object[] entry : entries)
			queIds.add(entry[0].toString());
		RespNestQueOrNoteArray queNoteArray = service.get0student$notes$list(queIds.toArray(new String[0]));
		return queNoteArray.getNotes();
	}
};
