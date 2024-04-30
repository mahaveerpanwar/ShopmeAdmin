package com.shopme.admin.user.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.shopme.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {
	
	
	public void setResponseHeader(HttpServletResponse httpServletResponse,String contentType,String extenision) throws IOException {
		DateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateformatter.format(new Date());

		String filename = "users_" + timestamp + extenision;

		httpServletResponse.setContentType(contentType);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + filename;
		httpServletResponse.setHeader(headerKey, headerValue);
		
	}

}
