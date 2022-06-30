package pl.nazwa.arzieba.dtnetworkproject.dto;

import lombok.*;
import pl.nazwa.arzieba.dtnetworkproject.model.LeaveType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LeaveApplyPreparer {

    private String username;
    private LeaveType leaveType;
    @NotBlank(message = "Ustaw datę początkową")
    private String startDate;
    @NotNull(message = "Ustaw datę końcową")
    private String endDate = startDate;
    private String text;
    @NotNull(message = "Pole nie moze być puste")
    private String sign;


}
