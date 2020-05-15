package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import article.model.ArticleContent;

public class ArticleContentDao {
	public ArticleContent insert(Connection conn,ArticleContent content) throws SQLException {
		try (PreparedStatement pstmt=conn.prepareStatement("insert into article_content (article_no, content) values(?,?)");){
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			int insertCnt=pstmt.executeUpdate();
			if(insertCnt>0) {
				return content;
			}
			return null;
		}
	}
}
