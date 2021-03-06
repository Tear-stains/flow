package org.flow.boot.process.service.test;

import java.util.List;

import org.flow.boot.common.vo.process.test.FormDefinitionVO;
import org.flow.boot.process.form.MyForm;

public interface TestFormService {

	public void deploy(MyForm form);

	public FormDefinitionVO queryById(String formId);

	public List<FormDefinitionVO> queryList();

	public void clear();

}
