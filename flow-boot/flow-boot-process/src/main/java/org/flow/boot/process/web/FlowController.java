package org.flow.boot.process.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.flow.boot.common.ErrorCode;
import org.flow.boot.common.Response;
import org.flow.boot.common.enums.EntityType;
import org.flow.boot.common.vo.process.FlowInstanceVO;
import org.flow.boot.process.entity.FlowInstance;
import org.flow.boot.process.repository.FlowProcessRepository;
import org.flow.boot.process.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("flow")
public class FlowController {

	@Autowired
	private FlowService flowService;
	@Autowired
	private FlowProcessRepository flowProcessRepository;

	@PostMapping(value = "deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<String> deploy(@RequestParam(required = false) MultipartFile file, EntityType entityType)
			throws IOException {
		if (Objects.isNull(file) || Objects.isNull(entityType))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		flowService.deploy(file.getInputStream(), entityType);
		return Response.okResponse("成功");
	}

	@GetMapping("query/{processId}")
	public Response<?> findById(@PathVariable String processId) {
		if (Objects.isNull(processId))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		return Response.okResponse(flowProcessRepository.getOne(processId));
	}

	@PostMapping("start")
	public Response<FlowInstanceVO> start(EntityType entityType, String processId, String entityId,
			@RequestBody HashMap<String, Object> variables) {
		if (Objects.isNull(entityType) || StringUtils.isAnyEmpty(processId, entityId))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		return Response.okResponse(flowService.start(processId, entityId, entityType, variables));
	}

	@GetMapping("step/html")
	public Response<String> stepHtml(EntityType entityType, String entityId) {
		if (Objects.isNull(entityType) || StringUtils.isAnyEmpty(entityId))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		return Response.okResponse(flowService.getRenderedHtml(entityId, entityType));
	}

	@PostMapping("step/complete")
	public Response<FlowInstance> stepComplete(EntityType entityType, String entityId) {
		if (Objects.isNull(entityType) || StringUtils.isAnyEmpty(entityId))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		return Response.okResponse(flowService.completeTask(entityId, entityType));
	}

	@PostMapping("step/cancel")
	public Response<FlowInstance> cancelTask(EntityType entityType, String entityId) {
		if (Objects.isNull(entityType) || StringUtils.isAnyEmpty(entityId))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		return Response.okResponse(flowService.cancelTask(entityId, entityType));
	}

	@PostMapping("step/jumpTo")
	public Response<FlowInstance> jumpToTask(EntityType entityType, String entityId, String stepId) {
		if (Objects.isNull(entityType) || StringUtils.isAnyEmpty(entityId, stepId))
			return Response.errorResponse(ErrorCode.PARAM_MISS);

		return Response.okResponse(flowService.jumpToTask(entityId, entityType, stepId));
	}

}
