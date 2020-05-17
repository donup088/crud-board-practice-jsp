package article.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

public class ModifyArticleHandler implements CommandHandler{
	private ModifyArticleService modifyService=new ModifyArticleService();
	private ReadArticleService readService=new ReadArticleService();
	private static final String FORM_VIEW="/WEB-INF/view/modifyForm.jsp";
	
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
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		User authUser=(User) req.getSession().getAttribute("authUser");
		String noVal=req.getParameter("no");
		int no=Integer.parseInt(noVal);
		ModifyRequest modReq=new ModifyRequest(authUser.getId(),no,req.getParameter("title"),req.getParameter("content"));
		req.setAttribute("modReq", modReq);
		Map<String,Boolean>errors=new HashMap<>();
		req.setAttribute("errors", errors);
		modReq.validate(errors);
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		try {
			modifyService.modify(modReq);
			return "/WEB-INF/view/modifySuccess.jsp";
		}catch(ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws IOException, SQLException {
		try {
			String noVal=req.getParameter("no");
			int no=Integer.parseInt(noVal);
			ArticleData articleData=readService.getArticleData(no, false);
			User authUser=(User) req.getSession().getAttribute("authUser");
			if(!canModify(authUser,articleData)) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}
			ModifyRequest modReq=new ModifyRequest(authUser.getId(),no,articleData.getArticle().getTitle(),articleData.getContent());
			req.setAttribute("modReq", modReq);
			return FORM_VIEW;
		}catch(ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}catch(PermissionDeniedException e) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}
	private boolean canModify(User authUser, ArticleData articleData) {
		String writerId=articleData.getArticle().getWriter().getId();
		return authUser.getId().equals(writerId);
	}
		
}
