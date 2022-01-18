package com.lms.api.utilities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ExcelRowData {
   private String test_Case_Scenario_Name;
   private String endpoint_Path;
   private String program_Id;
   private String json_Payload;
   private Integer expected_Status_Code;
   private String expected_Message;

}
