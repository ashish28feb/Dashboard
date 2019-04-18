package request.processor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.EnvironmentVariables;
import utilities.ExcelManager;

@WebServlet("/DataRequest")
public class RequestProcessor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if(request.getHeader("request_type").equals("overall")) {
				String[][] executionData = ExcelManager.readOverallData(EnvironmentVariables.EXECUTION_SUMMARY_SHEET);
				response.getWriter().println(formatData(executionData, true));
			} else if(request.getHeader("request_type").equals("user")) {
				String[][] executionData = ExcelManager.readOverallData(EnvironmentVariables.EXECUTION_STATUS_SHEET);
				response.getWriter().println(formatData(executionData, true));
			} else {
				String[][] executionData = ExcelManager.readCountryData(request.getHeader("request_type"));
				response.getWriter().println(formatData(executionData, false));
			}			
		} catch (Throwable e) {
			e.printStackTrace();
			response.getWriter().println("Country Not Found Or Data doesn't exist for the country");
		}
		System.out.println("Request processed "+request.getHeader("request_type"));
	}

	private LinkedHashMap<String, Integer> getExecutionStatusDetails(String[][] executionData) {
		LinkedHashMap<String, Integer> executionStatus = new LinkedHashMap<String, Integer>();
		int executionStatusIndex = -1;
		for (int headerColumnIndex = 0; headerColumnIndex < executionData[0].length; headerColumnIndex++) {
			if (executionData[0][headerColumnIndex].equals(EnvironmentVariables.EXECUTION_STATUS)) {
				executionStatusIndex = headerColumnIndex;
				break;
			}
		}
		for (int rowNumber = 1; rowNumber < executionData.length; rowNumber++) {
			if(executionData[rowNumber][executionStatusIndex].equals(EnvironmentVariables.EXCLUDED_STATUS)) {
				continue;
			}
			if(executionStatus.containsKey(executionData[rowNumber][executionStatusIndex])) {
				int newValue = executionStatus.get(executionData[rowNumber][executionStatusIndex]) + 1;
				executionStatus.put(executionData[rowNumber][executionStatusIndex], newValue);
			} else {
				executionStatus.put(executionData[rowNumber][executionStatusIndex], 1);
			}
		}
		return executionStatus;
	}
	
	private LinkedHashMap<String, Integer> getSummaryDetails(String[][] executionData) {
		LinkedHashMap<String, Integer> executionStatus = new LinkedHashMap<String, Integer>();
		for (int headerColumnIndex = 0; headerColumnIndex < executionData[0].length; headerColumnIndex++) {
			if (EnvironmentVariables.getTestStatusNames().contains(executionData[0][headerColumnIndex])) {
				int totalValue = 0;
				for(int rowNumber = 1; rowNumber < executionData.length; rowNumber++) {
					totalValue = totalValue + Integer.parseInt(executionData[rowNumber][headerColumnIndex]);
				}
				if(executionData[0][headerColumnIndex].equals("Scripts")) {
					ExcelManager.TOTAL_NUMBER_SCRIPTS = totalValue;
				} else {
					executionStatus.put(executionData[0][headerColumnIndex], totalValue);
				}
			}
		}
		return executionStatus;
	}

	private String formatData(String[][] countryData, boolean summaryData) {
		LinkedHashMap<String, Integer> executionStatusDetails;
		if(summaryData) {
			executionStatusDetails = getSummaryDetails(countryData);
		} else {
			executionStatusDetails = getExecutionStatusDetails(countryData);
		}
		String htmlOutput = "<html>";
		htmlOutput = htmlOutput + "<head>    <link rel=\"stylesheet\" href=\"css\\custom.css\">\r\n";
		htmlOutput = htmlOutput + "<script type=\"text/javascript\">function createChart() {anychart.onDocumentReady(function() {var data = [ ";
		Set<String> statusKeys = executionStatusDetails.keySet();
		boolean firstKey = true;
		for (String statusKey : statusKeys) {
			if(firstKey) {
				htmlOutput = htmlOutput + "{x : \""+statusKey+"\",value : "+executionStatusDetails.get(statusKey)+"}";
				firstKey = false;
			} else {
				htmlOutput = htmlOutput + ",{x : \""+statusKey+"\",value : "+executionStatusDetails.get(statusKey)+"}";
			}
		}
		htmlOutput = htmlOutput + "];";
		htmlOutput = htmlOutput + "var chart = anychart.pie();chart.title(\"Execution Status\");chart.data(data);chart.container('container');chart.draw();});}</script>";
		htmlOutput = htmlOutput + "<script src=\"https://cdn.anychart.com/js/8.0.1/anychart-core.min.js\"></script>\r\n" + 
				"<script src=\"https://cdn.anychart.com/js/8.0.1/anychart-pie.min.js\"></script>";
		htmlOutput = htmlOutput + "</head>";
		htmlOutput = htmlOutput + "<body>";
		htmlOutput = htmlOutput + "<div id=\"container\" style=\"height: 370px; width: 100%;\"></div>";
		if(summaryData) {
			htmlOutput = htmlOutput + "<div style=\"height: 50px; width: 100%;\"><h1>Total Number of scripts : "+ExcelManager.TOTAL_NUMBER_SCRIPTS+"</h1></div>";
		} else {
			String formattedString = ExcelManager.TOTAL_EXECUTION_TIME/3600+" Hours "+(ExcelManager.TOTAL_EXECUTION_TIME%3600)/60+" Minutes and "+(ExcelManager.TOTAL_EXECUTION_TIME%3600)%60+" Seconds";
			htmlOutput = htmlOutput + "<div style=\"height: 50px; width: 100%;\"><h1>Total Execution Time : "+formattedString+"</h1></div>";
		}
		htmlOutput = htmlOutput + "<table id=\"report_table\">";
		boolean headerColumn = true;
		for (String[] outputRow : countryData) {
			htmlOutput = htmlOutput + "<tr>";
			for (String outputValue : outputRow) {
				if(headerColumn) {
					htmlOutput = htmlOutput + "<th>" + outputValue + "</th>";
				} else {
					htmlOutput = htmlOutput + "<td>" + outputValue + "</td>";
				}
			}
			htmlOutput = htmlOutput + "</tr>";
			headerColumn = false;
		}
		htmlOutput = htmlOutput + "</table>";
		htmlOutput = htmlOutput + "</body></html>";
		return htmlOutput;
	}

}