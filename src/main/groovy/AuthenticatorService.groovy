import com.fasterxml.jackson.databind.JsonNode
import groovy.transform.CompileStatic
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.util.CommonHelper
//import javax.inject.Inject


/**
* AuthenticatorService: This class is used to authenticate users.
*/
@CompileStatic
class AuthenticatorService {

//   private final UserService userService

//    @Inject
//    AuthenticatorService(UserService userService){
//        this.userService = userService
//    }

    CommonProfile authenticate(JsonNode credentials, User user) {

        if (credentials == null || user == null) {
            throwsException("No credential")
        }
        String username = credentials.get("username").asText()
        String password = credentials.get("password").asText()

        if (CommonHelper.isBlank(username) || CommonHelper.isBlank(password)) {
            throwsException("Credentials cannot be blank")
        }
        // TODO: Here we use the raw password comparison (this should be changed in prod)
        if (CommonHelper.areNotEquals(username, user.username) ||
                CommonHelper.areNotEquals(password, user.password) ) {
            throwsException("Username : '" + username + "' does not match password")
        }

         final CommonProfile profile = new CommonProfile()
//         Blocking.get {
//             userService.get("1").then { User user ->
//               println("********************** User: " + user.toString())
//             if (user.username != user.password) {
//                 throwsException("Username or password does not match!")
//             } else {
                 profile.setId(username)
                 profile.addAttribute("username", user.username)
                 profile.addAttribute("password", user.password)
//                 profile.addAttribute("username", username)
//                 profile.addAttribute("password", password)
//             }
//           }
             return profile
//         }
    }

    static throwsException(String message) {
        throw new CredentialsException(message)
    }

}
