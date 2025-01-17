package com.alura.restapiforohubchallenge.domain.topic;

public record TopicReceivedDTO(
        String title,
        Long idCourse,
        Long idUser,
        String message
) {
}
