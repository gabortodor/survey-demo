package com.demo.survey.util;

import com.demo.survey.model.Status;
import com.demo.survey.service.SurveyService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class StatusFieldDeserializer extends JsonDeserializer<Status> {

    @Override
    public Status deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        final Status status = SurveyService.statusMap.get(Long.valueOf(jsonParser.getText()));
        if (status == null) {
            log.error("Cannot find status with id: " + jsonParser.getText());
        }
        return status;
    }
}
