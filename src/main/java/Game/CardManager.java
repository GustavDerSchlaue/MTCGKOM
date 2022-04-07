package Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.List;

public class CardManager {

    public String Name;

    public String Id;

    public Integer Damage;


    @JsonIgnore
    public String element;
    @JsonIgnore
    public String type;

    public static String seeOwnedCards(String token) throws SQLException {

        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String returnMsg = DB.AllOwnedCards(token);
        DB.closeConnection();
        return returnMsg;
    }


    public static void createPackage(String json) throws JsonProcessingException, SQLException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<CardManager> pack= objectMapper.readValue(json, new TypeReference<List<CardManager>>(){});
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");

        for(int i= 0; i<5; i++){
            if(pack.get(i).Name.contains("Spell")){
                pack.get(i).type="Spell";
            }else{
                pack.get(i).type="Monster";
            }

            if(pack.get(i).Name.contains("Fire")){
                pack.get(i).element="Fire";
            }else if(pack.get(i).Name.contains("Water")){
                pack.get(i).element="Water";
            }else{
                pack.get(i).element="Normal";
            }
        }

            DB.addPack(pack);


        DB.closeConnection();


    }

    public static String configureDeck(String token,String body) throws JsonProcessingException, SQLException {

        ObjectMapper objectMapper = new ObjectMapper();
        String returnMsg="Error,not enough cards";
        List<String> ids = objectMapper.readValue(body,new TypeReference<List<String>>(){});

        if(ids.size()==4){
            DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
            returnMsg= DB.createDeck(token, ids);
            DB.closeConnection();

        }

        return returnMsg;

    }


    public static String buyPack(String token) throws SQLException {
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        String returnMsg= DB.buyPack(token);
        DB.closeConnection();

        return returnMsg;
    }

    public static String seeDeck(String token) throws SQLException {
        DB.openConnection("jdbc:postgresql://localhost:5432/mtcg", "postgres", "");
        
        String returnMsg= DB.getDeck(token);
        DB.closeConnection();
        return returnMsg;
    }

}
