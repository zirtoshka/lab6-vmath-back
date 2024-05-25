package zir.lab6vmathback.utils;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RequestODEInfo {
    @NotNull(message = "equation must not be null")
    @DecimalMin(value = "1", message = "id function must be greater than or equal to 1")
    @DecimalMax(value = "3", message = "id function must be less than or equal to 3")
    private int equation;

    @NotNull(message = "the first boundary of the interval must not be null")
    @Pattern(regexp = "^[-+]?[0-9]*[\\.,]?[0-9]+([eE][-+]?[0-9]+)?$", message = "should be a number")
    private String firstBoundaryOfInterval;

    @NotNull(message = "the second boundary of the interval must not be null")
    @Pattern(regexp = "^[-+]?[0-9]*[\\.,]?[0-9]+([eE][-+]?[0-9]+)?$", message = "should be a number")
    private String secondBoundaryOfInterval;

    @NotNull
    @DecimalMin(value = "0", message = "inaccuracy must be greater than or equal to 0")
    @DecimalMax(value = "0.1", message = "inaccuracy must be less than or equal to 0.1")
    @Pattern(regexp = "^[-+]?[0-9]*[\\.,]?[0-9]+([eE][-+]?[0-9]+)?$", message = "should be a number")
    private String inaccuracy;
}
