package functions;

public class Input {
    
    private String role;
    private String method;
    private String resource;


    public Input() {}

    public Input(String role,String method,String resource) {
        this.role = role;
        this.method=method;
        this.resource=resource;
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

}
