// import groovy.transform.CompileStatic
import org.pac4j.core.exception.CredentialsException
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.util.CommonHelper
//import org.pac4j.http.credentials.UsernamePasswordCredentials
//import org.pac4j.http.profile.HttpProfile


/**
* AuthenticatorService: This class is used to authenticate users.
*/
// @CompileStatic
class AuthenticatorService {

    def authenticate(def credentials) {
        if (credentials == null) {
            throwsException("No credential")
        }
        String username = credentials.get("username")
        String password = credentials.get("password")

        if (CommonHelper.isBlank(username) || CommonHelper.isBlank(password)) {
            throwsException("Credentials cannot be blank")
        }
        // TODO: use the same username and password to login (this will be changed in prod)
        if (CommonHelper.areNotEquals(username, password)) {
            throwsException("Username : '" + username + "' does not match password")
        }
//         final HttpProfile profile = new HttpProfile()
//         final CommonProfile profile = new CommonProfile()
//         profile.setId(username)
//         profile.addAttribute("username", username)
//         profile.addAttribute("password", password)
//         credentials.setUserProfile(profile)
//        return profile
        def claims = ['username':username, 'password':password]
        return claims
    }

    static throwsException(String message) {
        throw new CredentialsException(message)
    }

}
