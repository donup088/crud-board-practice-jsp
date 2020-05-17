package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.JdbcUtil;

public class ArticleDao {
	public Article insert(Connection conn,Article article) throws SQLException {
		PreparedStatement pstmt=null;
		Statement stmt=null;
		ResultSet rs=null;
		try {
			pstmt= conn.prepareStatement("insert into article (writer_id, writer_name, title, regdate, moddate, read_cnt) values (?,?,?,?,?,0)");
			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			pstmt.setTimestamp(4, toTimeStamp(article.getRegDate()));
			pstmt.setTimestamp(5, toTimeStamp(article.getModifiedDate()));
			int insertCnt=pstmt.executeUpdate();
			if(insertCnt>0) {
				stmt=conn.createStatement();
				rs=stmt.executeQuery("select last_insert_id() from article");
				if(rs.next()) {
					Integer newNum=rs.getInt(1);
					return new Article(newNum,article.getWriter(),article.getTitle(),article.getRegDate(),article.getModifiedDate(),0);
				}
			}
			return null;
		}finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
			JdbcUtil.close(stmt);
		}
	}
	public Article selectById(Connection conn,int no) throws SQLException {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement("select *from article where article_no=?");
			pstmt.setInt(1, no);
			rs=pstmt.executeQuery();
			Article article=null;
			if(rs.next()) {
				article=new Article(rs.getInt("article_no"),
						new Writer(rs.getString("writer_id"),
						rs.getString("writer_name")),
						rs.getString("title"),
						toDate(rs.getTimestamp("regDate")),
						toDate(rs.getTimestamp("moddate")),
						rs.getInt("read_cnt"));
			}
			return article;
		}finally {
			JdbcUtil.close(pstmt);
			JdbcUtil.close(rs);
		}
	}
	public void increaseReadCount(Connection conn,int no) throws SQLException {
		try(PreparedStatement pstmt=conn.prepareStatement("update article set read_cnt=read_cnt+1 where article_no=?");){
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}
	public int selectCount(Connection conn) throws SQLException {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement("select count(*) from article");
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
			return 0;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	public List<Article> select(Connection conn,int startRow,int size) throws SQLException{
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			pstmt=conn.prepareStatement("select * from article order by article_no desc limit ?,?");
			pstmt.setInt(1,startRow);
			pstmt.setInt(2, size);
			rs=pstmt.executeQuery();
			List<Article> articles=new ArrayList<>();
			while(rs.next()) {
				articles.add(new Article(rs.getInt("article_no"),
						new Writer(rs.getString("writer_id"),
						rs.getString("writer_name")),
						rs.getString("title"),
						toDate(rs.getTimestamp("regDate")),
						toDate(rs.getTimestamp("moddate")),
						rs.getInt("read_cnt")));
			}
			return articles;
		}finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
	}
	public void titleUpdate(Connection conn,int no,String title) throws SQLException {
		try(PreparedStatement pstmt=conn.prepareStatement("update article set title=?,moddate=now() where article_no=?");){
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			pstmt.executeUpdate();
		}
	}
	private Date toDate(Timestamp timestamp) {
		return new Date(timestamp.getTime());
	}
	private Timestamp toTimeStamp(Date date) {
		return new Timestamp(date.getTime());
	}
}
