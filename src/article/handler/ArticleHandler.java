package article.handler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

public class ArticleHandler implements CommandHandler{
	private WriteArticleService writeService=new WriteArticleService();
	private static final String FORM_VIEW="/WEB-INF/view/newArticleForm.jsp";
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req,res);
		}else if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req,res);
		}
		res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		return null;
	}
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws SQLException {
		Map<String,Boolean> errors=new HashMap<>();
		req.setAttribute("errors", errors);
		User user=(User)req.getSession(false).getAttribute("authUser");
		WriteRequest writeReq=new WriteRequest(new Writer(user.getId(),user.getName()),req.getParameter("title"),req.getParameter("content"));
		writeReq.validate(errors);
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		int newArticleNum=writeService.write(writeReq);
		req.setAttribute("newArticleNum", newArticleNum);
		return "/WEB-INF/view/newArticleSuccess.jsp";
	}
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
}
