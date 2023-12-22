package com.demo.survey.model;

import lombok.Getter;

@Getter
public enum StatusEnum {
    NOT_ASKED(1L),
    REJECTED(2L),
    FILTERED(3L),
    COMPLETED(4L);

    StatusEnum(final Long id) {
        this.id = id;
    }

    private Long id;
}
