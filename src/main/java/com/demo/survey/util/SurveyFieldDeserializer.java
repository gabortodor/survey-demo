package com.demo.survey.util;

import com.demo.survey.model.Survey;
import com.demo.survey.service.SurveyService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class SurveyFieldDeserializer extends JsonDeserializer<Survey> {

    @Override
    public Survey deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        final Survey survey = SurveyService.surveyMap.get(Long.valueOf(jsonParser.getText()));
        if (survey == null) {
            log.error("Cannot find survey with id: " + jsonParser.getText());
        }
        return survey;
    }
}
