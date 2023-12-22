package com.demo.survey.service;

import com.demo.survey.dto.CollectedPointsDTO;
import com.demo.survey.dto.MemberDTO;
import com.demo.survey.dto.SurveyDTO;
import com.demo.survey.dto.SurveyStatisticsDTO;
import com.demo.survey.model.Member;
import com.demo.survey.model.Participation;
import com.demo.survey.model.Status;
import com.demo.survey.model.StatusEnum;
import com.demo.survey.model.Survey;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Log4j2
public class SurveyService {
    public static Map<Long, Status> statusMap;
    public static Map<Long, Member> memberMap;
    public static Map<Long, Survey> surveyMap;

    public static List<Participation> participationList;

    private final ModelMapper mapper;

    @Autowired
    public SurveyService(final ModelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * This is the entry point of the SurveyService.
     * The method fills the needed maps and list with the data read from the given csv files.
     */
    @PostConstruct
    public void constructMaps() {
        statusMap = readCsvEntities(new ClassPathResource("files/OO - 2 - Statuses.csv"), Status.class)
                .stream().collect(Collectors.toMap(Status::getId, Function.identity()));
        memberMap = readCsvEntities(new ClassPathResource("files/OO - 2 - Members.csv"), Member.class)
                .stream().collect(Collectors.toMap(Member::getId, Function.identity()));
        surveyMap = readCsvEntities(new ClassPathResource("files/OO - 2 - Surveys.csv"), Survey.class)
                .stream().collect(Collectors.toMap(Survey::getId, Function.identity()));
        participationList = readCsvEntities(new ClassPathResource("files/OO - 2 - Participation.csv"), Participation.class);
    }

    /**
     * Retrieves a list of members who completed the survey with the given id.
     *
     * @param surveyId The id of the survey.
     * @return A {@link List} of {@link MemberDTO}.
     * @throws ResponseStatusException if no survey can be found with the given id (HTTP 404 Not Found).
     */
    public List<MemberDTO> getSurveyMembers(final Long surveyId) {
        if (!surveyMap.containsKey(surveyId)) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return participationList.stream().filter(p -> surveyId.equals(p.getSurvey().getId())
                        && StatusEnum.COMPLETED.getId().equals(p.getStatus().getId()))
                .map(p -> mapToMemberDTO(p.getMember())).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of surveys that were completed by the member with the given id.
     *
     * @param memberId The id of the member.
     * @return A {@link List} of {@link SurveyDTO}.
     * @throws ResponseStatusException if no member can be found with the given id (HTTP 404 Not Found).
     */
    public List<SurveyDTO> getCompletedSurveysForMember(final Long memberId) {
        if (!memberMap.containsKey(memberId)) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return participationList.stream().filter(p -> memberId.equals(p.getMember().getId())
                        && StatusEnum.COMPLETED.getId().equals(p.getStatus().getId()))
                .map(p -> mapToSurveyDTO(p.getSurvey())).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of surveyId-points pairs that represent
     * the points collected by the member with the given id.
     *
     * @param memberId The id of the member.
     * @return A {@link List} of {@link CollectedPointsDTO}.
     * @throws ResponseStatusException if no member can be found with the given id (HTTP 404 Not Found).
     */
    public List<CollectedPointsDTO> getPointsForMember(final Long memberId) {
        if (!memberMap.containsKey(memberId)) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        return participationList.stream()
                .filter(p -> memberId.equals(p.getMember().getId())
                        && (StatusEnum.COMPLETED.getId().equals(p.getStatus().getId())
                        || StatusEnum.FILTERED.getId().equals(p.getStatus().getId())))
                .map(this::constructCollectedPointsDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of currently active members who can be
     * invited to participate in the survey with the given id.
     *
     * @param surveyId The id of the survey.
     * @return A {@link List} of {@link MemberDTO}.
     * @throws ResponseStatusException if no survey can be found with the given id (HTTP 404 Not Found).
     */
    public List<MemberDTO> getInvitableMemberForSurvey(final Long surveyId) {
        if (!surveyMap.containsKey(surveyId)) {
            throw new ResponseStatusException(NOT_FOUND);
        }
        List<Member> alreadyParticipated = participationList.stream().filter(
                        p -> surveyId.equals(p.getSurvey().getId())
                                || StatusEnum.NOT_ASKED.getId().equals(p.getStatus().getId()))
                .map(Participation::getMember).toList();
        List<Member> activeMembers = new ArrayList<>(memberMap.values().stream().filter(Member::getActive).toList());
        activeMembers.removeAll(alreadyParticipated);
        return activeMembers.stream().map(this::mapToMemberDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of statistics that contains information about each survey.
     *
     * @return A {@link List} of {@link SurveyStatisticsDTO}.
     */
    public List<SurveyStatisticsDTO> getSurveyStatistics() {
        return participationList.stream().collect(groupingBy(p -> p.getSurvey().getId(),
                collectingAndThen(toList(), this::constructSurveyStatisticsDTO))).values().stream().toList();
    }

    private CollectedPointsDTO constructCollectedPointsDTO(final Participation participation) {
        Integer points = 0;
        if (StatusEnum.FILTERED.getId().equals(participation.getStatus().getId())) {
            points = participation.getSurvey().getFilteredPoints();
        } else if (StatusEnum.COMPLETED.getId().equals(participation.getStatus().getId())) {
            points = participation.getSurvey().getCompletionPoints();
        }
        return CollectedPointsDTO.builder()
                .surveyId(participation.getSurvey().getId())
                .collectedPoints(points)
                .build();
    }

    private SurveyStatisticsDTO constructSurveyStatisticsDTO(final List<Participation> list) {
        final Survey currentSurvey = list.get(0).getSurvey();
        return SurveyStatisticsDTO.builder()
                .surveyId(currentSurvey.getId())
                .surveyName(currentSurvey.getName())
                .numberOfCompletes(
                        list.stream().filter(p -> StatusEnum.COMPLETED.getId().equals(p.getStatus().getId())).count())
                .numberOfFiltered(
                        list.stream().filter(p -> StatusEnum.FILTERED.getId().equals(p.getStatus().getId())).count())
                .numberOfRejected(
                        list.stream().filter(p -> StatusEnum.REJECTED.getId().equals(p.getStatus().getId())).count())
                .averageLengthSpent(
                        list.stream().filter(p -> p.getLength() != null)
                                .mapToLong(Participation::getLength).average().orElse(Double.NaN))
                .build();
    }

    private <T> List<T> readCsvEntities(final Resource resource, final Class<T> clazz) {
        try (MappingIterator<T> iterator = new CsvMapper().readerFor(clazz)
                .with(CsvSchema.emptySchema().withHeader()).readValues(resource.getInputStream())) {
            return iterator.readAll();
        } catch (IOException exception) {
            log.error(String.format("Cannot parse file %s. Exception: %s", resource.getFilename(), exception));
            return Collections.emptyList();
        }
    }

    private MemberDTO mapToMemberDTO(final Member member) {
        return mapper.map(member, MemberDTO.class);
    }

    private SurveyDTO mapToSurveyDTO(final Survey survey) {
        return mapper.map(survey, SurveyDTO.class);
    }
}
