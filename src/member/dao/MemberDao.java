package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import jdbc.JdbcUtil;
import member.model.Member;

public class MemberDao {
	public Member selectById(Connection conn,String id) throws SQLException {
		ResultSet rs=null;
		try(PreparedStatement pstmt=conn.prepareStatement("select *from member where memberid=?");) {
			pstmt.setString(1, id);
			rs=pstmt.executeQuery();
			Member member=null;
			if(rs.next()) {
				member= new Member(rs.getString("memberid"),rs.getString("name"),rs.getString("password"),rs.getTimestamp("regdate"));
			}
			return member;
		}finally {
			JdbcUtil.close(rs);
			}
		}
	
	public void insert(Connection conn,Member member) throws SQLException {
		try(PreparedStatement pstmt=conn.prepareStatement("insert into member values(?,?,?,?)")){
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getPassword());
			pstmt.setTimestamp(4, new Timestamp(member.getRegDate().getTime()));
			pstmt.executeUpdate();
		}
	}
}
