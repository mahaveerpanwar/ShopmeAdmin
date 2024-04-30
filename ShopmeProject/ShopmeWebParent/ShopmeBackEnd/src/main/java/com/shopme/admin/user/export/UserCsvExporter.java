package com.shopme.admin.user.export;

import java.io.IOException;
import java.util.List;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter  extends AbstractExporter{

	public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException {
		
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv");
//		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//		String timestamp = dateformatter.format(new Date());
//
//		String filename = "users_" + timestamp + ".csv";
//
//		httpServletResponse.setContentType("text/csv");
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=" + filename;
//		httpServletResponse.setHeader(headerKey, headerValue);
		
		

		ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = { "User ID", "Email", "FirstName", "LastName", "Enabled" };

		String[] feildMapping = { "id", "email", "firstname", "lastname", "enabled" };

		csvWriter.writeHeader(csvHeader);
		
		for(User user: listUsers) {
			
			csvWriter.write(user, feildMapping);
		}
		
		csvWriter.close();

	}

}
