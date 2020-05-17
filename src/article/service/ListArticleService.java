package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;

public class ListArticleService {
	private int size=10;
	private ArticleDao articleDao=new ArticleDao();
	
	public ArticlePage getArticlePage(int pageNum) throws SQLException {
		try(Connection conn=ConnectionProvider.getConnection();){
			int total=articleDao.selectCount(conn);
			List<Article> articles=articleDao.select(conn, (pageNum-1)*size, size);//현재page부터 size만큼 게시글목록 가져오기
			return new ArticlePage(total,pageNum,size,articles);//게시글하단목록
		}
	}
}
