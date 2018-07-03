package org.flow.boot.process.repository;

import java.util.List;

import org.flow.boot.process.entity.FlowStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowStepRepository extends JpaRepository<FlowStep, String> {

	List<FlowStep> findByProcessIdOrderByStepRank(String processId);

	List<FlowStep> findByProcessIdAndStepRankLessThanEqualOrderByStepRank(String processId, int stepRank);

	FlowStep findByStepKey(String stepKey);

}