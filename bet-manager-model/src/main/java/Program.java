import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.dao.MatchMetaData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
		MatchMetaData matchMetaData =
				new MatchMetaData("Chealsea", "Arsenal", 1, 206, 1, 2, 3, 4, 755, 6, 7, 8, 4, 755, 6, 7, 8, 4, 755, 6, 7, 8, 9, 10,
						11, 12, 13, 14, 15, 16);

		System.out.println(matchMetaData);

		session.save(matchMetaData);

		session.getTransaction().commit();

		session.disconnect();
		session.close();
		factory.close();
		session.clear();
	}
}
