import groovy.transform.CompileStatic
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.util.CommonHelper
import com.google.inject.Inject
import ratpack.exec.Blocking

/**
* AuthenticatorService: This class is used to authenticate users.
*/
@CompileStatic
class AuthenticatorService {

  @Inject
  UserService userService

    CommonProfile authenticate(def credentials) {
        if (credentials == null) {
            throwsException("No credential")
        }
        String username = credentials["username"]
        String password = credentials["password"]

        if (CommonHelper.isBlank(username) || CommonHelper.isBlank(password)) {
            throwsException("Credentials cannot be blank")
        }

        // final CommonProfile profile = new CommonProfile()
        Blocking.get {
            userService.get("1").then { User user ->
              println(user.toString())
            if (user.username != user.password) {
                // throwsException("Username or password does not match!")
            } else {

              // profile.setId(username)
              // profile.addAttribute("username", user.username)
              // profile.addAttribute("password", user.password)
            }
          }

        }
        // TODO: use the same username and password to login (this will be changed in prod)
        // if (CommonHelper.areNotEquals(username, password)) {
        //     throwsException("Username : '" + username + "' does not match password")
        // }
         final CommonProfile profile = new CommonProfile()
         profile.setId(username)
         profile.addAttribute("username", username)
         profile.addAttribute("password", password)
        return profile
    }

    static throwsException(String message) {
        throw new CredentialsException(message)
    }

}
