import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import groovy.transform.Canonical
import groovy.transform.CompileStatic
import groovy.transform.ToString

@DatabaseTable(tableName = "users")
@Canonical
@CompileStatic
class User {
    @DatabaseField(generatedId = true)
    int id
    @DatabaseField( unique = true)
    String username
    @DatabaseField
    String password
    @DatabaseField
    Boolean isActive
}
