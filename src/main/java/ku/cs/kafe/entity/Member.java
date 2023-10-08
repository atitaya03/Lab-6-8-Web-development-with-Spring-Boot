//atitaya promlers 6410406924
package ku.cs.kafe.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


import java.util.UUID;


@Data //include get set
@Entity
public class Member {


    @Id
    @GeneratedValue
    private UUID id;


    private String username;
    private String password;
    private String name;
    private String role;
}
