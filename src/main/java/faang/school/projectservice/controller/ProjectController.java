package faang.school.projectservice.controller;

import faang.school.projectservice.dto.project.ProjectDto;
import faang.school.projectservice.exception.DataValidationException;
import faang.school.projectservice.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class ProjectController {

    private final ProjectService projectService;
    private static final int MAX_NAME_LENGTH = 128;
    private static final int MAX_DESCRIPTION_LENGTH = 4096;

    public ProjectDto create(ProjectDto projectDto){
        validateProject(projectDto);
        return projectService.create(projectDto);
    }

    private void validateProject(ProjectDto projectDto) {
        if (projectDto.getName() == null || projectDto.getName().isBlank()) {
            throw new DataValidationException("Project can't be created with empty name");
        }
        if (projectDto.getName().length() > MAX_NAME_LENGTH){
            throw new DataValidationException("Project's name length can't be more than 128 symbols");
        }
        if (projectDto.getDescription() == null || projectDto.getDescription().isBlank()) {
            throw new DataValidationException("Project can't be created with empty description");
        }
        if (projectDto.getDescription().length() > MAX_DESCRIPTION_LENGTH){
            throw new DataValidationException("Project's description length can't be more than 4096 symbols");
        }
    }
}