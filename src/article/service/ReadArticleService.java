package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;

public class ReadArticleService {
	private ArticleDao articleDao=new ArticleDao();
	private ArticleContentDao contentDao= new ArticleContentDao();
	public ArticleData getArticleData(int articleNum,boolean increaseReadCnt) throws SQLException {
		try(Connection conn=ConnectionProvider.getConnection();){
			Article article=articleDao.selectById(conn, articleNum);
			if(article==null) {
				throw new ArticleNotFoundException();
			}
			ArticleContent content=contentDao.selectById(conn, articleNum);
			if(content==null) {
				throw new ArticleContentNotFoundException();
			}
			if(increaseReadCnt) {
				articleDao.increaseReadCount(conn, articleNum);
			}
			return new ArticleData(article,content);
		}
	}
}
