package org.flow.boot.process.controller;

import org.flow.boot.common.Response;
import org.flow.boot.common.enums.EntityType;
import org.flow.boot.process.entity.FlowInstance;
import org.flow.boot.process.entity.FlowProcess;
import org.flow.boot.process.repository.FlowInstanceRepository;
import org.flow.boot.process.repository.FlowProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flow/test")
public class TestController {

	@Autowired
	private FlowProcessRepository flowProcessRepository;
	@Autowired
	private FlowInstanceRepository flowInstanceRepository;

	@PostMapping("/insert")
	public Response insert(@RequestBody FlowProcess flowProcess, FlowInstance flowInstance) {
		flowProcessRepository.save(flowProcess);
		flowInstance = new FlowInstance();
		flowInstance.setEntityType(EntityType.TICKET);
		
		flowInstanceRepository.save(flowInstance);
		return Response.okResponse(flowProcess);
	}

	@GetMapping("/query/{id}")
	public Response query(@PathVariable String id) {
		return Response.okResponse(flowProcessRepository.findById(id));
	}

	@GetMapping("/query/list")
	public Response queryList() {
//		return Response.okResponse(flowProcessRepository.findAll());
		return Response.okResponse(flowInstanceRepository.findAll());
	}

	@GetMapping("/clear")
	public Response clear() {
		flowProcessRepository.deleteAll();
		return Response.okResponse(null);
	}

}