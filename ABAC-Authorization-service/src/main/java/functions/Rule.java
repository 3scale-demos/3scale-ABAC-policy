package functions;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;


@Entity
@NamedQuery(name = "Rule.getAllMatched", query = "from Rule where role = :role  and allow = true and method = :method and resource = :resource ")

public class Rule extends PanacheEntityBase {
 
    private String role;
    private String method;
    private String resource;
    private boolean allow;
    @Id 
    @GeneratedValue
    @Column(name = "id")
    private int id;
    public Rule() {
    }

    public Rule(String role, String method, String resource, boolean allow) {
        this.role = role;
        this.method = method;
        this.resource = resource;
        this.allow = allow;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isAllow() {
        return this.allow;
    }

    public boolean getAllow() {
        return this.allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }

    public static Rule findAllMatched(String role,String method,String resource){
        Map<String, Object> params=new HashMap<String, Object>();
        params.put("role", role);
        params.put("method", method);
        params.put("resource", resource);

        return  find("#Rule.getAllMatched", params).firstResult();
    }
   

}
