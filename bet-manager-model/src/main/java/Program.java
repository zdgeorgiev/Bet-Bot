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
				.addAnnotatedClass(FootballMatch.class)
				.buildSessionFactory();

		Session session = factory.getCurrentSession();

		session.beginTransaction();

		MatchMetaData matchMetaData = new MatchMetaData();

		session.save(matchMetaData);

		session.getTransaction().commit();

		session.disconnect();
		session.close();
		factory.close();
	}
}
