package article.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadArticleService;
import mvc.command.CommandHandler;

public class ReadArticleHandler implements CommandHandler{
	private ReadArticleService readService=new ReadArticleService();
	private static final String FORM_VIEW="/WEB-INF/view/readArticle.jsp";
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String noVal=req.getParameter("no");
		int articleNum=Integer.parseInt(noVal);
		try {
			ArticleData articleData=readService.getArticleData(articleNum, true);
			req.setAttribute("articleData", articleData);
			return FORM_VIEW;
		}catch(ArticleNotFoundException|ArticleContentNotFoundException e) {
			req.getServletContext().log("no article",e);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
	
}
