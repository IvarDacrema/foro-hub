package com.alura.restapiforohubchallenge.domain.answer;

import java.util.List;
import java.time.LocalDateTime;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicRepository;
import com.alura.restapiforohubchallenge.domain.login.user.UserRepository;
import com.alura.restapiforohubchallenge.exceptions.exceptions.ValidationException;

@Service
public class AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    public AnswerEntity createNewAnswer(AnswerReceivedDTO answerReceivedDTO) {
        if (!userRepository.existsById(answerReceivedDTO.userId())) {
            throw new ValidationException("This user does not exists.");
        }
        if (!topicRepository.existsById(answerReceivedDTO.topicId())) {
            throw new ValidationException("This topic does not exists.");
        }

        AnswerEntity answerEntity = new AnswerEntity(
                null,
                userRepository.getReferenceById(answerReceivedDTO.userId()),
                topicRepository.getReferenceById(answerReceivedDTO.topicId()),
                answerReceivedDTO.message(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        return answerRepository.save(answerEntity);
    }

    public List<AnswerSavedDTO> getAnswersByTopicId(Long topicId) {
        if (topicRepository.existsById(topicId) && !topicRepository.getReferenceById(topicId).getActiveStatus()) {
            throw new ValidationException("This topic is not available.");
        }
        List<AnswerEntity> answers = answerRepository.getAnswersByIdTopic(topicId);
        return answers.stream()
            .map(answer -> new AnswerSavedDTO(
                    answer.getIdAnswer(),

                    userRepository.getReferenceById(answer.getUserEntity().getIdUser()).getUsername(),
                    topicRepository.getReferenceById(answer.getTopicEntity().getIdTopic()).getTitle(),

                    answer.getMessage(),
                    answer.getCreationDate(),
                    answer.getLastEditedAt()
            ))
            .toList();
    }

    @Transactional
    public AnswerSavedDTO updateAnswer(Long idAnswer, AnswerUpdateDTO answerUpdateDTO) {
        if (!answerRepository.existsById(idAnswer) ||
                !answerRepository.getReferenceById(idAnswer).getActiveStatus())
        {
            throw new ValidationException("This answer is not available");
        }

        AnswerEntity answerEntity = answerRepository.getReferenceById(idAnswer);

        if (!topicRepository.getReferenceById(answerEntity.getTopicEntity().getIdTopic()).getActiveStatus()) {
            throw new ValidationException("The topic for which you want to update a reply is not available.");
        }

        answerEntity.setMessage(answerUpdateDTO.message());
        answerEntity.setLastEditedAt(LocalDateTime.now());

        return new AnswerSavedDTO(
                answerEntity.getIdAnswer(),
                userRepository.getReferenceById(answerEntity.getUserEntity().getIdUser()).getUsername(),
                topicRepository.getReferenceById(answerEntity.getTopicEntity().getIdTopic()).getTitle(),
                answerEntity.getMessage(),
                answerEntity.getCreationDate(),
                answerEntity.getLastEditedAt()
        );
    }


    @Transactional
    public void deleteAnswer(Long idAnswer) {
        if (!answerRepository.existsById(idAnswer)) {
            throw new ValidationException("This answer cannot be deleted.");
        }
        answerRepository.getReferenceById(idAnswer).setActiveStatus(false);
    }
}
