package com.lms.api;

import com.lms.api.utilities.ExcelRowData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.lms.api.utilities.ExcelReaderUtil;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class UserPut {

	RequestSpecification requestSpec;
	Response response;
	String userId;
	String path;
	String sheetPut;
	ExcelReaderUtil excelSheetReaderUtil;


	public UserPut() {

		String excelPath = "src/test/resources/excel/put_test_data.xlsx";


		excelSheetReaderUtil = new ExcelReaderUtil(excelPath);


		//To call  Backend we need url path for backend API
		String baseUrlOfBackAPI = "https://lms-admin-rest-service.herokuapp.com";
		String userName = "admin";
		String pwd = "password";

		RestAssured.baseURI = baseUrlOfBackAPI;

		requestSpec = RestAssured.given().auth().preemptive().basic(userName, pwd);
		requestSpec.header("Content-Type", "application/json");

	}

//	@DataProvider(name = "excelRecordsArray", parallel=true)
	@DataProvider(name = "excelRecordsArray")
	public Object [] getExcelSheetRecords() throws Exception {
		String sheetName = "put";
		Object [] excelRowDataObjectArray = excelSheetReaderUtil.getSheetData(excelSheetReaderUtil.getSheet(sheetName));

		return excelRowDataObjectArray;
	}

	@Test(dataProvider = "excelRecordsArray")
	public void testUserPutPositiveScenario01(ExcelRowData excelRowData) throws Exception {

		String endpointPath = excelRowData.getEndpoint_Path(); //"/programs";
		String programId = excelRowData.getProgram_Id();  //3705;

		//	Integer expStatusCodeForCreated = 201;
		int expStatusCodeForUpdate = excelRowData.getExpected_Status_Code(); //200;
		String putJsonPayload = excelRowData.getJson_Payload();
		JsonPath jsonPath = new JsonPath(putJsonPayload);

		System.out.println("----------------> Start Scenario: "+excelRowData.getTest_Case_Scenario_Name());
		String expectedProgramName = jsonPath.getString("programName"); //"API Testing 3705";
		String expectedProgramDescription= jsonPath.getString("programDescription");//"Description for 3705";

		//Consutruct the final endpoint appending the path variable
		String finalEndPointPath = endpointPath + "/" + programId;

		requestSpec.body(putJsonPayload).log().all();

		//Call actual Backend API using put method call
//		response = requestSpec.when().log().all().put(finalEndPointPath);
		response = requestSpec.when().put(finalEndPointPath);

		//Verification steps
		String responseBody = response.prettyPrint();
		System.out.println("---> Again printing response from sysout : \\n"+responseBody);
		System.out.println();

		JsonPath jsonRespFromServer = response.jsonPath();
		System.out.println("Status code received from Server: "+response.statusCode());
		Assert.assertEquals(response.statusCode(), expStatusCodeForUpdate);

		System.out.println("programName from server after put: "+jsonRespFromServer.getString("programName"));
		System.out.println("programDescription from server after put: "+jsonRespFromServer.getString("programDescription"));

		if(programId != null && (response.statusCode() != 400)) {
			Assert.assertEquals(jsonRespFromServer.getString("programName"), expectedProgramName);
			Assert.assertEquals(jsonRespFromServer.getString("programDescription"), expectedProgramDescription);
		}else{
			Assert.assertNull(jsonRespFromServer.getString("programName"));
			Assert.assertNull(jsonRespFromServer.getString("programDescription"));
		}

		System.out.println("----------------> End Scenario: "+excelRowData.getTest_Case_Scenario_Name());

	}



/*
	@Test
	//check if we can update the non existing programId
	public void testUserPutNegativeScenario01() {

		String endpointPath = "/programs";
		Integer programId = 87766;

		//	Integer expStatusCodeForCreated = 201;
		int expStatusCodeForUpdate = 200;

		//Consutruct the final endpoint appending the path variable
		String finalEndPointPath = endpointPath + "/" + programId;


		String putBody = "   {\n"
				+ "        \"programId\": 87766,\n"
				+ "        \"programName\": \"API Testing 87766\",\n"
				+ "        \"programDescription\": \"Description for 87766\",\n"
				+ "        \"online\": true\n"
				+ "    }";

		requestSpec.body(putBody).log().all();

		//Call actual Backend API using put method call
		response = requestSpec.when().log().all().put(finalEndPointPath);

		//Verification steps
		String responseBody = response.prettyPrint();
		System.out.println(" ---> Again printing response from sysout : "+responseBody);
		System.out.println();

		JsonPath jsonRespFromServer = response.jsonPath();
		System.out.println("Status code received from Server: "+response.statusCode());
		Assert.assertEquals(expStatusCodeForUpdate, response.statusCode());

	}


	@Test
	//Program id as null
	public void testUserPutProgramidNullScenario03() {

		String endpointPath = "/programs";
		Integer programId = null;

		//	Integer expStatusCodeForCreated = 201;
		int expStatusCodeForUpdate = 400;
		String expectedProgramName = "API Testing when programId null";
		String errorMessage = "Bad Request";

		//Consutruct the final endpoint appending the path variable
		String finalEndPointPath = endpointPath + "/" + programId;


		String putBody = "   {\n"
				+ "        \"programId\": null,\n"
				+ "        \"programName\": \"API Testing when programId null\",\n"
				+ "        \"programDescription\": \"Description when programId is null\",\n"
				+ "        \"online\": true\n"
				+ "    }";

		requestSpec.body(putBody).log().all();

		//Call actual Backend API using put method call
//		response = requestSpec.when().log().all().put(finalEndPointPath);
		response = requestSpec.when().put(finalEndPointPath);

		//Verification steps
		String responseBody = response.prettyPrint();
		System.out.println("---> Again printing response from sysout : \\n"+responseBody);
		System.out.println();

		JsonPath jsonRespFromServer = response.jsonPath();
		System.out.println("Status code received from Server: "+response.statusCode());
		Assert.assertEquals(expStatusCodeForUpdate, response.statusCode());

		System.out.println("programName from server after put: "+jsonRespFromServer.getString("programName"));

		Assert.assertNull(null, jsonRespFromServer.getString("programName"));
		Assert.assertEquals(errorMessage, jsonRespFromServer.getString("error"));
	}*/

}
