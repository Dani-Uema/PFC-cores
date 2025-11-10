package com.example.paintlab.dto.color;
import com.example.paintlab.dto.CompositionDTO;
import lombok.Data;
import java.util.List;


@Data
public class ColorUpdateDTO {

    private String name;
    private String brand;
    private String colorCode;
    private String hexCode;
    private List<CompositionDTO> compositions;

}

