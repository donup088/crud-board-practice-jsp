package article.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleNotFoundException;
import article.service.DeleteArticleService;
import article.service.DeleteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

public class DeleteArticleHandler implements CommandHandler{
	private DeleteArticleService deleteService=new DeleteArticleService();
	private static final String FORM_VIEW="/WEB-INF/view/deleteSuccess.jsp";
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String noVal=req.getParameter("no");
		int no=Integer.parseInt(noVal);
		User authUser=(User) req.getSession().getAttribute("authUser");
		DeleteRequest delReq=new DeleteRequest(authUser.getId(),no,req.getParameter("title"),req.getParameter("content"));
		try {
			deleteService.delete(delReq);
			return FORM_VIEW;
		}catch(ArticleNotFoundException|ArticleContentNotFoundException e) {
			req.getServletContext().log("no article",e);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
}
