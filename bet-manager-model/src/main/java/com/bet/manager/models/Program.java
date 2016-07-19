package com.bet.manager.models;

import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.dao.MatchMetaData;
import com.bet.manager.models.util.FootballMatchBuilder;
import com.bet.manager.models.util.MatchMetaDataUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;

public class Program {

	public static void main(String[] args) {

		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(MatchMetaData.class)
				.addAnnotatedClass(MatchMetaData.TeamMetaData.class)
				.addAnnotatedClass(MatchMetaData.TeamMetaData.PreviousRoundStats.class)
				.addAnnotatedClass(MatchMetaData.TeamMetaData.LastFiveMatchesPerformance.class)
				.addAnnotatedClass(FootballMatch.class)
				.buildSessionFactory();

		Session session = factory.openSession();

		session.beginTransaction();

		FootballMatch match = new FootballMatchBuilder()
				.setHomeTeamName("CSKA")
				.setAwayTeamName("Levski")
				.setStartDate(new Date())
				.build();

		session.save(match);

		session.getTransaction().commit();

		session.beginTransaction();

		FootballMatch match2 = (FootballMatch) session.get(FootballMatch.class, 1);

		MatchMetaData matchMetaData =
				MatchMetaDataUtils.parse(
						"CSKA,LEVSKI,111,155,771,1152,323,-1,6,2,4,3,6,7,8,4,755,6,7,8,4,755,7,89,10,11,12,13,14,15,16,2,3-3");

		session.save(matchMetaData);

		new FootballMatchBuilder(match2)
				.setResult("3-1")
				.build();

		session.save(match2);

		session.getTransaction().commit();

		session.beginTransaction();

		new FootballMatchBuilder(match2)
				.setPrediction("CSKA")
				.setMatchMetaData(matchMetaData)
				.build();

		session.save(match2);

		session.getTransaction().commit();

		session.disconnect();
		session.close();
		factory.close();
	}
}