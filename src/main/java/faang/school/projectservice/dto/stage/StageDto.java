package faang.school.projectservice.dto.stage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageDto {

    private Long stageId;
    @NotBlank(message = "Stage name can't be blank")
    private String stageName;
    @NotNull
    private Long projectId;
    @Valid
    @NotEmpty(message = "Stage roles can't be null or empty")
    private List<StageRolesDto> stageRoles;
    private List<Long> taskIds;
    private List<Long> executorIds;
}