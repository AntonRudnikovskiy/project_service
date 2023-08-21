package faang.school.projectservice.dto.project;

import faang.school.projectservice.model.ProjectVisibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SubProjectDto {
    private Long id;
    @Size(max = 128, message = "SubProject name cannot have more than 128 characters")
    @NotBlank(message = "SubProject can't be created with empty name")
    private String name;
    private String description;
    @NotNull(message = "SubProject must have owner")
    private long ownerId;
    @NotNull(message = "SubProject must have parentProjectId")
    private Long parentProjectId;
    @NotNull(message = "Visibility of subProject must be specified as 'private' or 'public'")
    private ProjectVisibility visibility;
}