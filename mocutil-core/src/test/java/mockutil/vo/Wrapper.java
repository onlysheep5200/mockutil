package mockutil.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Wrapper implements Serializable {

//    private String propertyA;
//
//    private Long propertyB;
//
//    private Integer propertyC;

    private List itemsNoGeneric;
    private List<Item> items;
}
