package Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

public class Card {

    @Getter
    private String id;
    @Getter
    private String name;

    @Getter
    private Integer damage;
    @Getter
    private String element;
    @Getter
    private String type;


    public Card(String rec_id, String rec_name, Integer rec_damage, String rec_element, String rec_type) {
        this.id=rec_id;
        this.name=rec_name;
        this.damage=rec_damage;
        this.element=rec_element;
        this.type=rec_type;
    }


}
