package com.alura.restapiforohubchallenge.domain.answer;

// Librerias utilizadas.
import java.net.URI;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import com.alura.restapiforohubchallenge.domain.topic.TopicService;
import com.alura.restapiforohubchallenge.domain.login.user.UserService;

// Endpoint.
@RestController
@RequestMapping("/answers")
public class AnswerController {

    // Inyeccion de dependencias.
    @Autowired
    private AnswerService answerService;

    @Autowired
    UserService userService;

    @Autowired
    TopicService topicService;


    @PostMapping("/create")
    public ResponseEntity<AnswerSavedDTO> createNewAnswer(
            @RequestBody
            AnswerReceivedDTO answerReceivedDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {

        AnswerEntity answerEntity = answerService.createNewAnswer(answerReceivedDTO);


        URI url = uriComponentsBuilder.path("/answers/{idAnswer}")
                .buildAndExpand(answerEntity.getIdAnswer())
                .toUri();


        AnswerSavedDTO answerSavedDTO = new AnswerSavedDTO(
                answerEntity.getIdAnswer(),
                userService.findById(answerReceivedDTO.userId()).getUsername(),
                topicService.findTopicById(answerReceivedDTO.topicId()).getTitle(),
                answerEntity.getMessage(),
                answerEntity.getCreationDate(),
                answerEntity.getLastEditedAt()
        );
        return ResponseEntity.created(url).body(answerSavedDTO);
    }

    @GetMapping("/topic/{idTopic}")
    public ResponseEntity<List<AnswerSavedDTO>> getAnswersByTopicId(
            @PathVariable @NotNull Long idTopic
    ) {
        List<AnswerSavedDTO> answers = answerService.getAnswersByTopicId(idTopic);

        if (answers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(answers);
    }

    @PutMapping("/update/{idAnswer}")
    public ResponseEntity<AnswerSavedDTO> updateTopic(
            @PathVariable @NotNull Long idAnswer,
            @RequestBody @NotNull AnswerUpdateDTO answerUpdateDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        return ResponseEntity.ok(answerService.updateAnswer(idAnswer, answerUpdateDTO));
    }

    @DeleteMapping("/delete/{idAnswer}")
    public ResponseEntity deleteAnswer(@PathVariable @NotNull Long idAnswer) {
        answerService.deleteAnswer(idAnswer);
        return ResponseEntity.ok().build();
    }
}
