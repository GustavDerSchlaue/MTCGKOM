package Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.sql.SQLException;

public class User {



    @Getter
    public String Username;
    @Getter
    public String Password;
    @JsonIgnore
    private static String token;




    public static boolean addUserToDB(String json) throws JsonProcessingException, SQLException {

        ObjectMapper oM = new ObjectMapper();
        User user = oM.readValue(json, User.class);
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        if(DB.checkIfUserExists(user.Username)){
            return false;
        }

        DB.createUser(user.Username, user.Password);
        DB.closeConnection();

        return true;

    }

    public static String Login(String json) throws SQLException, JsonProcessingException {
        ObjectMapper oM = new ObjectMapper();
        User user = oM.readValue(json, User.class);
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        token="error";
        if(DB.checkPassword(user.Username, user.Password)){
             token= DB.getTokenByUsername(user.Username);
        }
        DB.closeConnection();
        return token;

    }



    public static String getUserData(String name, String token) throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String response= DB.getUserBio(name,token);
        DB.closeConnection();
        return response;
    }

    public static String changeUserData(String name, String token, String json) throws SQLException, JsonProcessingException {

        ObjectMapper oM = new ObjectMapper();
        BioContent changes = oM.readValue(json, BioContent.class);

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String response= DB.updateUserBio(name,token,changes);
        DB.closeConnection();
        return response;
    }

    public static String addToQueue(String token) throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String response= DB.addToBattleQueue(token);
        DB.closeConnection();
        return response;

    }

    public static String getStats(String token) throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String response= DB.getElo(token);
        DB.closeConnection();
        return response;


    }


    public static String Reset(String token) throws SQLException  {
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String response= DB.reset(token);
        DB.closeConnection();
        return response;
    }

    public static String getBoard(String token) throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String response= DB.getScoreboard(token);
        DB.closeConnection();
        return response;
    }
}
