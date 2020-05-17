package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ModifyArticleService {
	private ArticleDao articleDao=new ArticleDao();
	private ArticleContentDao contentDao=new ArticleContentDao();
	public void modify(ModifyRequest req) {
		Connection conn=null;
		try {
			conn=ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			Article article=articleDao.selectById(conn, req.getArticleNumber());
			if(article==null) {
				throw new ArticleNotFoundException();
			}
			if(!canModify(req.getUserId(),article)) {
				throw new PermissionDeniedException();
			}
			articleDao.titleUpdate(conn, req.getArticleNumber(), req.getTitle());
			contentDao.contentUpdate(conn, req.getArticleNumber(), req.getContent());
			conn.commit();
		}catch(SQLException e) {
			JdbcUtil.rollback(conn);
			throw new RuntimeException();
		}catch(PermissionDeniedException e) {
			JdbcUtil.rollback(conn);
			throw e;
		}finally {
			JdbcUtil.close(conn);
		}
	}
	private boolean canModify(String userId, Article article) {
		return article.getWriter().getId().equals(userId);
	}
}
