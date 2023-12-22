package com.demo.survey.util;

import com.demo.survey.model.Member;
import com.demo.survey.service.SurveyService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class MemberFieldDeserializer extends JsonDeserializer<Member> {

    @Override
    public Member deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
        final Member member = SurveyService.memberMap.get(Long.valueOf(jsonParser.getText()));
        if (member == null) {
            log.error("Cannot find member with id: " + jsonParser.getText());
        }
        return member;
    }
}
