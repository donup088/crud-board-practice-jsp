package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.JdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class ChangePasswordService {
	private MemberDao memberDao=new MemberDao();
	public void change(String id,String curPwd,String newPwd) throws SQLException {
		try(Connection conn=ConnectionProvider.getConnection();){
			conn.setAutoCommit(false);
			Member member=memberDao.selectById(conn, id);
			if(member==null) {
				throw new MemberNotFoundException();
			}
			if(!member.matchPassword(curPwd)){
				throw new InvalidPasswordException();
			}
			member.changePassword(newPwd);
			memberDao.update(conn, member);
			conn.commit();
		}
	}
}
