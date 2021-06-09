import groovy.transform.CompileStatic
import ratpack.exec.Blocking
import ratpack.exec.Promise
import com.google.inject.Inject
import com.j256.ormlite.dao.Dao
import com.j256.ormlite.dao.DaoManager

/**
* UserService: This class will be used to manage user's User in the future releases.
*/

@CompileStatic
class UserService {
    final Dao<User, String> UserDao

    @Inject
    UserService(H2ConnectionDataSource connectionDataSource) {
        this.UserDao = DaoManager.createDao(connectionDataSource.connectionSource, User) as Dao<User, String>
    }

    Promise<Integer> create(User User) {
        Blocking.get {
            UserDao.create(User)
        }
    }

    Promise<Integer> update(User User) {
        Blocking.get {
            UserDao.update(User)
        }
    }

    Promise<User> get(String id) {
        Blocking.get {
            UserDao.queryForId(id)
        }
    }

     Promise<List<User>> findByUsernameAndPassword(String username, String password) {
         Blocking.get {
           UserDao.queryForFieldValues( ['username': username, 'password': password] as Map<String, Object> )
         }
     }

    Promise<List<User>> getAll() {
        Blocking.get {
            UserDao.queryForAll()
        }
    }

    Promise<Integer> delete(String id){
        Blocking.get {
            UserDao.deleteById(id)
        }
    }
}
