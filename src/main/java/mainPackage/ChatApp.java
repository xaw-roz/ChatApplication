package mainPackage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import mainPackage.Domain.Users;
import mainPackage.Service.DbService;
import mainPackage.Service.UserService;

import java.sql.SQLException;
import java.util.Map;


public class ChatApp extends AbstractVerticle{


    private String code="";

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new ChatApp());
    }

    public void start(){
        Router router = Router.router(vertx);
        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
        router.route("/").handler(rtx->{
            HttpServerResponse response = rtx.response();
            response.setChunked(true);
            response.sendFile("webroot/index.html");
//            response.sendFile("webroot/chat.html");
//            router.route().handler(StaticHandler.create());

        });
        vertx.executeBlocking(future->{
            DbService.buildConnection(vertx);
            future.complete("Connected to Database");
        },res->{});

        router.route("/Js/*").handler(StaticHandler.create("webroot/Js"));
        router.route("/Css/*").handler(StaticHandler.create("webroot/Css"));
        router.route("/fbhome").handler(this::oResult);


        router.route().handler(StaticHandler.create());

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
        /*UserService userService = new UserService();

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

        };*/

       // context.response().putHeader("content-type","text/html;charset=UTF-8").end("HI Welcome "+fbProfileData.get("first_name")+"("+fbProfileData.get("email")+")" +"<br><a href=\"https://www.facebook.com/logout.php?next="+"http://localhost:8080/"+"/&access_token="+ftoken[1]+"\">log out with Facebook</a>"+"<br><a href=>chat</a>");
        context.response().sendFile("webroot/chat.html");
    }
}
