package business.service;

import java.io.Serializable;
import java.util.List;

public interface CRUDService {
	<T> List<T> getAll(Class<T> klass);

	<T> void Save(T klass);

	<T> T findByPrimaryKey(Class<T> klass, Serializable id);
	<T> T findByEmail(Class<T> klass, String email);

	<T> void delete(T klass);

	public <T> T GetUniqueEntityByNamedQuery(String query, Object... params);

	<T> List<T> GetListByNamedQuery(String query, Object... params);

	<T> Long getQueryCount(String query, Object... params);
	
	
}
