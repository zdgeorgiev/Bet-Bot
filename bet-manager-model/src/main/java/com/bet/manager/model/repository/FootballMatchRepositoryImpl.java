package com.bet.manager.model.repository;

import com.bet.manager.model.entity.FootballMatch;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

public class FootballMatchRepositoryImpl implements FootballMatchRepositoryCustom {

	@Autowired
	private EntityManager em;

	/**
	 * Method which is used to retrieve {@link FootballMatch} from the db.
	 * This implementation perform search by homeTeam, awayTeam, round and year,
	 * because this is unique representation of a match.
	 *
	 * @param match football match
	 * @return match entry or null if not found
	 */
	@Override
	@SuppressWarnings("unchecked")
	public FootballMatch retrieve(FootballMatch match) {

		String retrieveQuery =
				"SELECT m FROM FootballMatch AS m WHERE "
						+ "m.homeTeam = :homeTeam AND "
						+ "m.awayTeam = :awayTeam AND "
						+ "m.year = :year AND "
						+ "m.round = :round";

		List<FootballMatch> match2 = em.createQuery(retrieveQuery)
				.setParameter("homeTeam", match.getHomeTeam())
				.setParameter("awayTeam", match.getAwayTeam())
				.setParameter("year", match.getYear())
				.setParameter("round", match.getRound())
				.getResultList();

		if (match2.size() == 1)
			return match2.get(0);

		if (match2.size() > 1)
			throw new IllegalStateException("Found more than one candidate in the db for '" + match.getSummary() + "'");

		// Not found
		return null;
	}

	@Override
	public boolean exist(FootballMatch match) {
		return retrieve(match) != null;
	}
}
