package member.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.User;
import member.service.ChangePasswordService;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import mvc.command.CommandHandler;

public class ChangePasswordHandler implements CommandHandler{
	private static final String FORM_VIEW="/WEB-INF/view/changePwdForm.jsp";
	private ChangePasswordService changePasswordService=new ChangePasswordService();
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req,res);
		}
		else if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req,res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws SQLException {
		User user=(User)req.getSession().getAttribute("authUser");
		Map<String,Boolean>errors=new HashMap<>();
		req.setAttribute("errors", errors);
		String curPwd=req.getParameter("curPwd");
		String newPwd=req.getParameter("newPwd");
		if(curPwd==null||curPwd.isEmpty()) {
			errors.put("curPwd", Boolean.TRUE);
		}
		if(newPwd==null||newPwd.isEmpty()) {
			errors.put("newPwd", Boolean.TRUE);
		}
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		try {
			changePasswordService.change(user.getId(), curPwd, newPwd);
			return "/WEB-INF/view/changePwdSuccess.jsp";
		}catch(InvalidPasswordException e) {
			errors.put("badCurPwd", Boolean.TRUE);
			return FORM_VIEW;
		}catch(MemberNotFoundException e) {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
	}
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
}	
