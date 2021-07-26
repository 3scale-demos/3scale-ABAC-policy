package functions;

import io.quarkus.funqy.Funq;

public class Function {

    @Funq
    public Output function(Input input) {
        System.out.println("role="+input.getRole());
        System.out.println("method="+input.getMethod());
        System.out.println("resource="+input.getResource());
        Rule r=Rule.findAllMatched(input.getRole(), input.getMethod(), input.getResource());
        boolean isAllowed=false;
        if (r!=null)
         isAllowed=true;
         System.out.println("isAllowed="+isAllowed);
    return new Output(isAllowed);
    }

}
