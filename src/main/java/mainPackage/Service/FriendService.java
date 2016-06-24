package mainPackage.Service;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import java.util.List;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import mainPackage.Domain.Members;



/**
 * Created by saroj on 6/24/2016.
 */




public class FriendService {
    static Logger logger = LoggerFactory.getLogger(FriendService.class);

    public static void getFriendList(String userName,Vertx vertx){
        JsonObject friends = new JsonObject();
        Members member  = new Members(userName,"1");
        vertx.executeBlocking(future-> {

            DbService.getClient().getConnection(conn -> {
                if (conn.failed()) {
                    logger.error("Connection Failed With Database");
                } else {
                    SQLConnection connection = conn.result();
                    connection.queryWithParams("select id from member where username = ?", new JsonArray().add(member.getUserName()), memberId -> {

                        if (memberId.failed()) {
                            DbService.closeConnection(connection);
                            logger.error("Failed To Fetch Member Id ");
                        } else {
                            ResultSet resultSet = memberId.result();
                            int uId = resultSet.getResults().get(0).getInteger(0);
                            member.setUserId(uId);
                            String fetchFriendQuery = "select *from friends where (member_one = " + member.getUserId() + ")" +
                                    " or (member_two = " + member.getUserId() + ") and status=1";
                            final int[] index = {1};
                            connection.query(fetchFriendQuery,
                                    friendsList -> {
                                        if (friendsList.failed()) {
                                            DbService.closeConnection(connection);
                                            logger.error("Failed to fetch data from database");
                                        } else {
                                            ResultSet friendsSet = friendsList.result();
                                            if (friendsSet.getNumRows() > 0) {
                                                List<JsonArray> results = friendsSet.getResults();
                                                for (JsonArray row : results) {
                                                    int member_two = row.getInteger(2);
                                                    String friendDetails = "select *from member where id=?";
                                                    connection.queryWithParams(friendDetails, new JsonArray().add(member_two), memberDetails -> {
                                                        ResultSet memberResult = memberDetails.result();
                                                        friends.put(memberResult.getResults().get(0).getString(1), memberResult.getResults().get(0).getString(2));
                                                        if (friendsSet.getNumRows() == index[0]) {
                                                            DbService.closeConnection(connection);
                                                            friends.put("classifier", "fetchFriendList");
                                                            future.complete(friends);
                                                        }
                                                        index[0]++;
                                                    });
                                                }
                                            }else{
                                                DbService.closeConnection(connection);
                                            }
                                        }
                                    });
                        }
                    });
                }
            });
        },res->{
            logger.info("Returned Friend List is "+res.result());
            vertx.eventBus().publish("chat.to.client", res.result());
        });
    }

}
