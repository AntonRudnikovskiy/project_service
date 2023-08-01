package faang.school.projectservice.service;

import faang.school.projectservice.dto.client.InternshipDto;
import faang.school.projectservice.jpa.TaskRepository;
import faang.school.projectservice.jpa.TeamMemberJpaRepository;
import faang.school.projectservice.mapper.InternshipMapper;
import faang.school.projectservice.model.*;
import faang.school.projectservice.repository.InternshipRepository;
import faang.school.projectservice.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;

import static faang.school.projectservice.model.InternshipStatus.IN_PROGRESS;

@Service
@RequiredArgsConstructor
public class InternshipService {
    private final TeamMemberJpaRepository teamMemberJpaRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final InternshipRepository internshipRepository;
    private final TaskRepository taskRepository;
    private final InternshipMapper internshipMapper;


    public InternshipDto internshipCreation(InternshipDto internshipDto) {
        if (internshipDto.getProjectId() == null) {

// Стажировка ВСЕГДА относится к какому-то одному проекту.
//                internshipDto.getProjectId() > 1 &&
//                        internshipDto.getProjectId() < 1

            throw new IllegalArgumentException("Invalid project id");
        }
        if (internshipDto.getInternsId().size() == 0) {
            throw new IllegalArgumentException("Empty list of interns");
        }
        if (Period.between(internshipDto.getStartDate().toLocalDate(),
                internshipDto.getEndDate().toLocalDate()).getMonths() > 3) {
            throw new IllegalArgumentException("Invalid internship period");
        }
        if (teamMemberJpaRepository.findByUserIdAndProjectId(internshipDto.getMentorId(),
                internshipDto.getProjectId()) == null) {
            throw new IllegalArgumentException("Invalid mentor");
        }
        return internshipMapper.toInternshipDto(internshipRepository.save(internshipMapper.toInternship(internshipDto)));
    }

    public InternshipDto internshipUpdate(InternshipDto internshipDto) {
        Internship internship = internshipRepository.findById(internshipDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid internship"));
        Project project = internship.getProject();
        if (internship.getStatus().equals(IN_PROGRESS)) {
            List<TeamMember> interns = internship.getInterns();
            interns.forEach(intern -> {
                List<TaskStatus> tasksOfIntern = taskRepository.findAllByProjectIdAndPerformerId(internship.getProject().getId(), intern.getId())
                        .stream()
                        .map(task -> task.getStatus())
                        .toList();
                if (tasksOfIntern.stream().allMatch(task -> task.equals(TaskStatus.DONE))) {
                    //переписать будто стажер получает статус как у ментора
                    intern.addRole(TeamRole.JUNIOR);
                    intern.addRole(TeamRole.INTERN);

                } else {
                    project.getTeam().getTeamMembers().remove(intern);
                }
            });
        }
        return internshipDto;
    }
}
//        internshipDto.getUpdatedAt()
//        Long project = internshipDto.getProjectId();;
//        project.setId(internshipDto.getProjectId());
//        List<Task> teamTasks = project.getTasks();
//        () -> teamTasks.forEach(task -> {
//            task.getPerformerUserId();
//        })
//                .filter(task -> task.getPerformerUserId() == internshipDto.getId())
//                .toList();
//internshipDto.setStatus();

//        internshipRepository.findById(id).stream().forEach(task -> {
//            task.getPerformerUserId();
//        })
//    }


//private boolean internshipValidation(InternshipDto internshipDto) {
//        return internshipDto.getProjectId() != null &&
//                internshipDto.getProjectId() < 2 &&
//                internshipDto.getInternsId().size() > 0 &&
//                Period.between(internshipDto.getStartDate().toLocalDate(),
//                        internshipDto.getEndDate().toLocalDate()).getMonths() < 3 &&
//        (teamMemberJpaRepository.findByUserIdAndProjectId(internshipDto.getMentorId(), internshipDto.getProjectId()) != null);
//    }

//❤️