package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class WriteArticleService {
	
	private ArticleDao articleDao=new ArticleDao();
	private ArticleContentDao contentDao=new ArticleContentDao();
	public Integer write(WriteRequest req) throws SQLException {
		Connection conn=null;
		try {
			conn=ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Article article=new Article(null,req.getWriter(),req.getTitle(),new Date(),new Date(),0);
			Article savedArticle=articleDao.insert(conn, article);
			if(savedArticle==null) {
				throw new RuntimeException();
			}
			ArticleContent content=new ArticleContent(savedArticle.getNumber(),req.getContent());
			ArticleContent savedArticleContent=contentDao.insert(conn, content);
			if(savedArticleContent==null) {
				throw new RuntimeException();
			}
			conn.commit();
			return savedArticle.getNumber();
		}catch(SQLException e){
			JdbcUtil.rollback(conn);
			throw e;
		}
		finally {
			JdbcUtil.close(conn);
		}
	}
}
