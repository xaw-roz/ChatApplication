package mainPackage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import mainPackage.Domain.Users;
import mainPackage.Service.UserService;

import java.awt.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by anons on 5/3/16.
 */
public class OAuth extends AbstractVerticle{
    private static final long serialVersionUID = 1L;
    private String code="";
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new OAuth());
    }
    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        router.route("/auth").handler(this::auth);
        router.route("/fbhome").handler(this::oResult);
    }
//    public void auth(RoutingContext routingContext){
//
//            mainPackage.FBConnection fbConnection = new mainPackage.FBConnection();
//        fbConnection.getFBAuthUrl();
//        code=routingContext.request().getParam("code");
//        if (code == null || code.equals("")) {
//            throw new RuntimeException(
//                    "ERROR: Didn't get code parameter in callback.");
//        }
//        System.out.println(code);
//            String accessToken =fbConnection.getAccessToken(code);
//            mainPackage.FBGraph fbGraph = new mainPackage.FBGraph(accessToken);
//            String graph = fbGraph.getFBGraph();
//            Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
//
//        System.out.println("Username: "+fbProfileData.get("first_name"));
//
//        routingContext.response().setChunked(true);
//        routingContext.response().sendFile("webroot/oauth.html");
//
//    }
//}
    public void auth(RoutingContext routingContext){

        FBConnection fbConnection = new FBConnection();
        String url=fbConnection.getFBAuthUrl();
//        try {
//            Desktop desktop = java.awt.Desktop.getDesktop();
//            URI oURL = new URI(url);
//            desktop.browse(oURL);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        routingContext.response().setChunked(true).sendFile("webroot/index.html");
    }
    
    public void oResult(RoutingContext context){
        code=context.request().getParam("code");
        if (code == null || code.equals("")) {
            throw new RuntimeException(
                    "ERROR: Didn't get code parameter in callback.");
        }
        System.out.println(code);
        FBConnection fbConnection = new FBConnection();
        String accessToken =fbConnection.getAccessToken(code);
        FBGraph fbGraph = new FBGraph(accessToken);
        String graph = fbGraph.getFBGraph();
        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);

        System.out.println("Username: "+fbProfileData.get("first_name"));

        /*context.response().putHeader("content-type","text/html;charset=UTF-8").end("Welcome "+fbProfileData.get("first_name")+"("+fbProfileData.get("email")+")");*/
        String [] tokens = accessToken.split("&");
        String [] ftoken = tokens[0].split("=");
        //System.out.println("The final token is --> "+ ftoken[1]);
        UserService userService = new UserService();

        if(!(userService.searchUserId(Long.parseLong(fbProfileData.get("id"))))){

            Users user = new Users() ;
            System.out.println("check to see if long parse correct: "+Long.parseLong(fbProfileData.get("id")));

            user.setId(Long.parseLong(fbProfileData.get("id")));

            System.out.println(user.getId());


            user.setEmail(fbProfileData.get("email"));
            user.setName(fbProfileData.get("first_name"));



            //insert into database if user does not exit
            try {
                userService.storeUser(user);
                System.out.println("info of user stored successful");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        };

        context.response().putHeader("content-type","text/html;charset=UTF-8").end("HI Welcome "+fbProfileData.get("first_name")+"("+fbProfileData.get("email")+")" +"<br><a href=\"https://www.facebook.com/logout.php?next="+"http://localhost:8080/auth"+"/&access_token="+ftoken[1]+"\">log out with Facebook</a>");

    }

}
