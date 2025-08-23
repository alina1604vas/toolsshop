package org.example.tools.infra;

import org.example.tools.SystemConfig;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.Optional;

public class SprintCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        int selectedSprint = SystemConfig.getSelectedSprint();

        Optional<EnabledForSprint> annotation = context.getElement()
                .flatMap(el -> Optional.ofNullable(el.getAnnotation(EnabledForSprint.class)));

        if (!annotation.isPresent()) {
            return ConditionEvaluationResult.enabled("No sprint restriction");
        }

        int actualValue = annotation.get().value();
        if (actualValue <= selectedSprint) {
            return ConditionEvaluationResult.enabled("Enabled for sprint " + selectedSprint);
        } else {
            return ConditionEvaluationResult.disabled("Disabled for sprint " + selectedSprint);
        }
    }

}
