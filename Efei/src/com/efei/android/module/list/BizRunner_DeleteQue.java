package com.efei.android.module.list;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespDeletedOrPuttedNotes;

final class BizRunner_DeleteQue implements IBusinessCallback<RespDeletedOrPuttedNotes>
{
	private String note_id;

	public BizRunner_DeleteQue(String note_id)
	{
		this.note_id = note_id;
	}

	@Override
	public RespDeletedOrPuttedNotes onBusinessLogic(IJob job)
	{
		IQueOrNoteLookUpService service = IQueOrNoteLookUpService.Factory.getService();
		return service.delete0student$notes(note_id);
	}

}
