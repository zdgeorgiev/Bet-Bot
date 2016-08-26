package com.bet.manager.core;

import com.bet.manager.commons.DateFormats;
import com.bet.manager.commons.ResultMessages;
import com.bet.manager.model.dao.FootballMatch;
import com.bet.manager.model.dao.MatchStatus;
import com.bet.manager.model.exceptions.MatchStatusNotExist;
import com.bet.manager.model.util.FootballMatchBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FootballDataMatchParser implements IMatchParser {

	private static final Logger log = LoggerFactory.getLogger(FootballDataMatchParser.class);

	private static final String FINISHED_TAG = "FINISHED";
	private static final String PROCESS_TAG = "IN_PLAY";
	private static final String NOT_STARTED_TAG = "SCHEDULED";
	private static final String POSTPONED_TAG = "POSTPONED";
	private static final String TIMED_TAG = "TIMED";
	private static final String CANCELED_TAG = "CANCELED";

	private static final String RESULT_DELIMITER = new FootballMatch().getResultDelimiter();
	private static final Calendar CAL = Calendar.getInstance();

	@Override
	public Map<MatchStatus, List<FootballMatch>> parse(String content) {

		Map<MatchStatus, List<FootballMatch>> matches = initMatchesStructure();

		JSONObject matchesAsJSON = new JSONObject(content);

		for (Object matchObject : (JSONArray) matchesAsJSON.get("fixtures")) {

			try {

				Date startDate = DateFormats.FOOTBALL_DATA_DATE_FORMAT.parse(getProperty(matchObject, "date"));
				CAL.setTime(startDate);

				String homeTeam = convertToBundesligaTeam(getProperty(matchObject, "homeTeamName"));
				String awayTeam = convertToBundesligaTeam(getProperty(matchObject, "awayTeamName"));

				MatchStatus status = parseStatus(getProperty(matchObject, "status"));

				Integer homeTeamGoals = -1;
				Integer awayTeamGoals = -1;

				if (status.equals(MatchStatus.FINISHED) || status.equals(MatchStatus.STARTED)) {
					JSONObject result = (JSONObject) ((JSONObject) matchObject).get("result");
					homeTeamGoals =
							result.has("goalsHomeTeam") ? Integer.parseInt(result.get("goalsHomeTeam").toString()) : -1;
					awayTeamGoals =
							result.has("goalsAwayTeam") ? Integer.parseInt(result.get("goalsAwayTeam").toString()) : -1;
				}

				Integer matchDay = Integer.parseInt(getProperty(matchObject, "matchday"));

				if (matchDay < 2) {
					log.warn("Found match for round 1.. skipping..");
					continue;
				}

				FootballMatch match = new FootballMatchBuilder()
						.setHomeTeamName(homeTeam)
						.setAwayTeamName(awayTeam)
						.setStatus(status)
						.setStartDate(startDate)
						.setRound(matchDay)
						.setYear(CAL.get(Calendar.YEAR)) // this get the year from the object
						.setResult(homeTeamGoals == -1 || awayTeamGoals == -1 ? ResultMessages.UNKNOWN_RESULT :
								String.format("%s%s%s", homeTeamGoals, RESULT_DELIMITER, awayTeamGoals))
						.build();

				matches.get(match.getMatchStatus()).add(match);

			} catch (Exception e) {
				log.error("Error occur during fetching fixture matches..", e);
			}
		}

		return matches;
	}

	private Map<MatchStatus, List<FootballMatch>> initMatchesStructure() {

		Map<MatchStatus, List<FootballMatch>> matches = new HashMap<>();
		Arrays.stream(MatchStatus.values()).forEach(status -> matches.put(status, new ArrayList<>()));
		return matches;
	}

	private String getProperty(Object jsonObject, String property) {
		return ((JSONObject) jsonObject).get(property).toString();
	}

	private String convertToBundesligaTeam(String footballDataMatchName) {
		return TeamsMapping.footballDataToBundesliga.get(footballDataMatchName);
	}

	private MatchStatus parseStatus(String status) {
		switch (status) {
		case FINISHED_TAG:
			return MatchStatus.FINISHED;
		case PROCESS_TAG:
			return MatchStatus.STARTED;
		case NOT_STARTED_TAG:
		case POSTPONED_TAG:
		case CANCELED_TAG:
		case TIMED_TAG:
			return MatchStatus.NOT_STARTED;
		}

		throw new MatchStatusNotExist(String.format("Match status %s is not correct", status));
	}
}
