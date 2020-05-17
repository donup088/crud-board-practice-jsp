package article.handler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

public class ListArticleHandler implements CommandHandler{
	private ListArticleService listService=new ListArticleService();
	private static final String FORM_VIEW="/WEB-INF/view/listArticle.jsp";
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pageNoVal=req.getParameter("pageNo");
		int pageNo=1;
		if(pageNoVal!=null) {
			pageNo=Integer.parseInt(pageNoVal);
		}
		ArticlePage articlePage=listService.getArticlePage(pageNo);
		req.setAttribute("articlePage", articlePage);
		return FORM_VIEW;
	}
	
}
