package business.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class CRUDDaoImpl implements CRUDDao {

	@Autowired
	SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getTodos(Class<T> klass){
		return getCurrentSession().createQuery("from "+klass.getName())
				.list();
	}
	protected final Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	public <T> void Save(T klass) throws DataAccessException {
		getCurrentSession().saveOrUpdate(klass);
		
	}
	public <T> void eliminar(T klass) throws DataAccessException {
		getCurrentSession().delete(klass);
	}
	@SuppressWarnings("unchecked")
	public <T> T encontrarPorId(Class<T> klass, Serializable id) {
		return (T) getCurrentSession().get(klass, id);
	}
	@SuppressWarnings("unchecked")
	public <T> T encontrarPorCorreo(Class<T> klass, String correo) {
		return (T) getCurrentSession().get(klass, correo);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T GetUniqueEntityByNamedQuery(String query, Object... params) {

		Query q = getCurrentSession().getNamedQuery(query);

		int i = 1;
		String arg = "arg";
		for (Object o : params) {
			q.setParameter(arg + i, o);
			i++;
		}

		List<T> results = q.list();

		T foundentity = null;
		if (!results.isEmpty()) {
			// ignores multiple results
			foundentity = results.get(0);
		}
		return foundentity;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> ObtenerNombrePorNombreQuery(String query, Object... params) {
		// TODO Auto-generated method stub
		Query q = getCurrentSession().getNamedQuery(query);

		int i = 1;
		String arg = "arg";
		if (params != null) {
			for (Object o : params) {
				if (o != null) {
					q.setParameter(arg + i, o);
					i++;
				}
			}
		}

		List<T> list = (List<T>) q.list();
		return list;
	}



	public <T> Long getQueryCount(String query, Object... params) {
		// TODO Auto-generated method stub
		Query q = getCurrentSession().getNamedQuery(query);
		int i = 1;
		String arg = "arg";
		Long count = (long) 0;

		if (params != null) {
			for (Object o : params) {
				if (o != null) {
					q.setParameter(arg + i, o);
					i++;
				}
			}
		}
		count = (Long) q.uniqueResult();
		return count;
	}
}
