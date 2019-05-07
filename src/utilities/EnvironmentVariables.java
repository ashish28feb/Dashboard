package utilities;

import java.util.ArrayList;

public class EnvironmentVariables {
	public static final String EXCEL_DATA_FILE_LOCATION = System.getProperty("user.dir")+"/data/Data.xlsx";
	public static final String EXECUTION_STATUS = "Execution Status";
	public static final String EXECUTION_SUMMARY_SHEET = "Execution Status Summary";
	public static final String EXECUTION_STATUS_SHEET = "Execution Status Details";
	public static final String EXCLUDED_STATUS = "OOS";
	public static final String PASSED_STATUS = "Passed";
	public static final String FAILED_STATUS = "Failed";

	public static final ArrayList<String> ALLOWED_COLUMN_NAMES = new ArrayList<String>();
	public static final ArrayList<String> OPTIONAL_COLUMN_NAMES = new ArrayList<String>();
	public static final ArrayList<String> ALLOWED_COUNTRIES = new ArrayList<String>();

	public static ArrayList<String> getAllowedNames() {
		ALLOWED_COLUMN_NAMES.add("Module");
		ALLOWED_COLUMN_NAMES.add("Test Script");
		ALLOWED_COLUMN_NAMES.add(EXECUTION_STATUS);
		ALLOWED_COLUMN_NAMES.add("Execution Duration");
		ALLOWED_COLUMN_NAMES.add("Area");
		ALLOWED_COLUMN_NAMES.add("Unit");
		ALLOWED_COLUMN_NAMES.add("Execution Progress");
		ALLOWED_COLUMN_NAMES.add("Scripts");
		ALLOWED_COLUMN_NAMES.add("Not Started");
		ALLOWED_COLUMN_NAMES.add("Failed");
		ALLOWED_COLUMN_NAMES.add("Passed");
		ALLOWED_COLUMN_NAMES.add("Comments");
		ALLOWED_COLUMN_NAMES.add("Tester");
		return ALLOWED_COLUMN_NAMES;
	}
	
	public static ArrayList<String> getOptionalNames(){
		OPTIONAL_COLUMN_NAMES.add("Converted to WS?");
		OPTIONAL_COLUMN_NAMES.add("Created Data");
		OPTIONAL_COLUMN_NAMES.add("Remarks");
		OPTIONAL_COLUMN_NAMES.add("Sub Area");
		OPTIONAL_COLUMN_NAMES.add("Passed %");
		return OPTIONAL_COLUMN_NAMES;
	}
	
	public static ArrayList<String> getTestStatusNames(){
		OPTIONAL_COLUMN_NAMES.add("Not Started");
		OPTIONAL_COLUMN_NAMES.add("Failed");
		OPTIONAL_COLUMN_NAMES.add("Passed");
		OPTIONAL_COLUMN_NAMES.add("Scripts");
		return OPTIONAL_COLUMN_NAMES;
	}
	
	public static ArrayList<String> getAllowedCountries() {
		ALLOWED_COLUMN_NAMES.add("Australia");
		ALLOWED_COLUMN_NAMES.add("China FL");
		ALLOWED_COLUMN_NAMES.add("China SL");
		ALLOWED_COLUMN_NAMES.add("Estonia");
		ALLOWED_COLUMN_NAMES.add("Finland FL");
		ALLOWED_COLUMN_NAMES.add("Finland SL");
		ALLOWED_COLUMN_NAMES.add("France");
		ALLOWED_COLUMN_NAMES.add("GSS");
		ALLOWED_COLUMN_NAMES.add("Hong Kong");
		ALLOWED_COLUMN_NAMES.add("Germany");
		ALLOWED_COLUMN_NAMES.add("India FL");
		ALLOWED_COLUMN_NAMES.add("India SL");
		ALLOWED_COLUMN_NAMES.add("Italy FL");
		ALLOWED_COLUMN_NAMES.add("Italy SL");
		ALLOWED_COLUMN_NAMES.add("Latvia");
		ALLOWED_COLUMN_NAMES.add("Lithuania");
		ALLOWED_COLUMN_NAMES.add("New Zealand");
		ALLOWED_COLUMN_NAMES.add("Russia");
		ALLOWED_COLUMN_NAMES.add("Saudi Arabia");
		ALLOWED_COLUMN_NAMES.add("South Africa");
		ALLOWED_COLUMN_NAMES.add("Spain");
		ALLOWED_COLUMN_NAMES.add("Sweden");
		ALLOWED_COLUMN_NAMES.add("Turkey");
		ALLOWED_COLUMN_NAMES.add("Thailand");
		ALLOWED_COLUMN_NAMES.add("UAE");		
		ALLOWED_COLUMN_NAMES.add("UK");		
		ALLOWED_COLUMN_NAMES.add("USA FL");
		ALLOWED_COLUMN_NAMES.add("USA SL");
		return ALLOWED_COLUMN_NAMES;
	}
}
