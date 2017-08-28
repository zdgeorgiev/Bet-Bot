package com.bet.manager.services;

import com.bet.manager.commons.util.ClasspathUtils;
import com.bet.manager.model.entity.FootballMatch;
import com.bet.manager.model.entity.MatchStatus;
import com.bet.manager.model.entity.PredictionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SearchService {

	@Autowired
	private EntityManager em;

	@SuppressWarnings("unchecked")
	public List<FootballMatch> retrieveMatches(String homeTeam, String awayTeam, Integer year, Integer round,
			PredictionType predictionType, MatchStatus matchStatus, int limit, int offset) {

		String matchQuery = ClasspathUtils.getContentUTF8("queries/matchSearchQuery.rq");

		return (List<FootballMatch>) em.createQuery(
				String.format(matchQuery,
						homeTeam == null ? "homeTeam" : homeTeam,
						awayTeam == null ? "awayTeam" : awayTeam,
						round == null ? "round" : round,
						year == null ? "year" : year,
						predictionType == null ? "predictionType" : predictionType.ordinal(),
						matchStatus == null ? "matchStatus" : matchStatus.ordinal()))
				.setMaxResults(limit)
				.setFirstResult(offset)
				.getResultList();
	}
}
