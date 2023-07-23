package faang.school.projectservice.service.stage_invitation;

import faang.school.projectservice.dto.stage_invitation.StageInvitationDto;
import faang.school.projectservice.exception.DataValidationException;
import faang.school.projectservice.mapper.stage_invitation.StageInvitationMapper;
import faang.school.projectservice.model.stage.Stage;
import faang.school.projectservice.model.stage_invitation.StageInvitation;
import faang.school.projectservice.model.stage_invitation.StageInvitationStatus;
import faang.school.projectservice.repository.StageInvitationRepository;
import faang.school.projectservice.repository.StageRepository;
import faang.school.projectservice.repository.TeamMemberRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
public class StageInvitationService {
    private final StageInvitationRepository repository;
    private final StageRepository stageRepository;
    private final TeamMemberRepository TMRepository;
    private final StageInvitationMapper mapper;

    public StageInvitationDto create(StageInvitationDto invitationDto) {
        validate(invitationDto);
        return mapper.toDTO(repository.save(mapper.toModel(invitationDto)));
    }

    private void validate(StageInvitationDto invitationDto) {
        Stage stage = stageRepository.getById(invitationDto.getStageId());
        TMRepository.findById(invitationDto.getInvitedId());
        TMRepository.findById(invitationDto.getAuthorId());
        if (!hasStageExecutor(stage, invitationDto.getAuthorId())) {
            throw new DataValidationException("Author is not executor of stage");
        }
    }

    private boolean hasStageExecutor(Stage stage, long executorId) {
        return stage.getExecutors().stream().anyMatch(executor -> executor.getId() == executorId);
    }

    public StageInvitationDto accept(long invitationId){
        StageInvitation invitation = repository.findById(invitationId);
        invitation.getStage().getExecutors().add(invitation.getInvited());
        invitation.setStatus(StageInvitationStatus.ACCEPTED);
        return mapper.toDTO(repository.save(invitation));
    }
}
