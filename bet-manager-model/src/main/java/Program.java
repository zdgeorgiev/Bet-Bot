import com.bet.manager.models.dao.FootballMatch;
import com.bet.manager.models.dao.MatchMetaData;
import com.bet.manager.models.dao.PreviousRoundStats;
import com.bet.manager.models.dao.TeamMetaData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Program {

	public static void main(String[] args) {

		SessionFactory factory = new Configuration()
				.configure("hibernate.cfg.xml")
				.addAnnotatedClass(MatchMetaData.class)
				.addAnnotatedClass(FootballMatch.class)
				.addAnnotatedClass(PreviousRoundStats.class)
				.addAnnotatedClass(TeamMetaData.class)
				.buildSessionFactory();

		Session session = factory.openSession();

		session.beginTransaction();
		MatchMetaData matchMetaData =
				new MatchMetaData("Chealsea", "Arsenal", 105, 206, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);

		session.save(matchMetaData);

		session.getTransaction().commit();

		session.disconnect();
		session.close();
		factory.close();
		session.clear();
	}
}
