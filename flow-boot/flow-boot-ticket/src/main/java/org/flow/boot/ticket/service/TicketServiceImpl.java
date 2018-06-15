package org.flow.boot.ticket.service;

import java.util.Date;

import org.flow.boot.common.enums.EntityType;
import org.flow.boot.common.vo.process.FlowInstanceVO;
import org.flow.boot.common.vo.process.FlowProcessVO;
import org.flow.boot.common.vo.process.FlowStepVO;
import org.flow.boot.ticket.entity.SysTicket;
import org.flow.boot.ticket.form.TicketForm;
import org.flow.boot.ticket.repository.SysTicketRepository;
import org.flow.boot.ticket.service.feignclient.FlowControllerService;
import org.flow.boot.ticket.service.feignclient.StepControllerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private SysTicketRepository sysTicketRepository;
	@Autowired
	private FlowControllerService flowControllerService;
	@Autowired
	private StepControllerService stepControllerService;

	@Transactional
	public void openTicket(TicketForm form) {
		Date now = new Date();
		SysTicket ticket = new SysTicket();
		ticket.setCreateTime(now);
		ticket.setUpdateTime(now);
		BeanUtils.copyProperties(form, ticket);
		sysTicketRepository.save(ticket);

		String ticketId = ticket.getTicketId();
		String processId = form.getProcessId();
		FlowInstanceVO flowInstanceVO = flowControllerService.start(EntityType.TICKET, processId, ticketId).getData();
		FlowProcessVO flowProcessVO = flowControllerService.findById(processId).getData();
		FlowStepVO flowStepVO = stepControllerService.findById(flowInstanceVO.getStepId()).getData();
		ticket.setProcessName(flowProcessVO.getProcessName());
		ticket.setSoStatus("已开单");// TODO 从扩展中取值
		ticket.setStepId(flowStepVO.getStepId());
		ticket.setStepName(flowStepVO.getStepName());
		ticket.setStepType(flowStepVO.getStepType());
		sysTicketRepository.save(ticket);
	}

	public String ticketFlow(String ticketId) {
		return flowControllerService.getRenderedHtml(EntityType.TICKET, ticketId).getData();
	}

	@Transactional
	public void completeStep(String ticketId) {
		SysTicket ticket = sysTicketRepository.findOne(ticketId);
		FlowInstanceVO flowInstanceVO = flowControllerService.completeStep(EntityType.TICKET, ticketId).getData();
		FlowStepVO flowStepVO = stepControllerService.findById(flowInstanceVO.getStepId()).getData();
		ticket.setSoStatus("---");// TODO 从扩展中取值
		ticket.setStepId(flowStepVO.getStepId());
		ticket.setStepName(flowStepVO.getStepName());
		ticket.setStepType(flowStepVO.getStepType());
		ticket.setUpdateTime(new Date());
		sysTicketRepository.save(ticket);
	}

}
