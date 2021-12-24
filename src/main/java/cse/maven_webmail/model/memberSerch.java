package cse.maven_webmail.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import cse.maven_webmail.control.DBInfo;

import cse.maven_webmail.model.UserBeans;
/**
 *
 * @author hyun
 */
public class memberSerch {
    	/**
	 * 필요한 속성 선언
	 */
            Connection con;
            Statement st;
            PreparedStatement ps;
            ResultSet rs;

            //MySQL


            //연결을 위한 생성자
            public memberSerch(){       
                try {
                    //로드
                    Class.forName(DBInfo.JdbcDriver);           
                    //연결
                    con = DriverManager.getConnection(DBInfo.Jdbcurl, DBInfo.user, DBInfo.password);                  
                } catch (ClassNotFoundException e) {           
                    System.out.println(e+"=> 로드 실패");           
                } catch (SQLException e) {           
                    System.out.println(e+"=> 연결 실패");
                }
        }   
            //DB를 닫기위한 메소드
            public void db_close(){        
                try {           
                    if (rs != null ) rs.close();
                    if (ps != null ) ps.close();       
                    if (st != null ) st.close();       
                } catch (SQLException e) {
                    System.out.println(e+"=> 닫기 오류");
                }
        }
            //사용자 리스트를 만들기 위한 메소드
            public ArrayList<UserBeans> getMemberlist(){

            ArrayList<UserBeans> list = new ArrayList<UserBeans>();

            try{//실행
                st = con.createStatement();
                rs = st.executeQuery("SELECT username, email, phone FROM users");

                while(rs.next()){
                    UserBeans ub = new UserBeans();

                    ub.setUserName(rs.getString(1));
                    ub.seteMail(rs.getString(2));
                    ub.setPhone(rs.getString(3));

                    list.add(ub);
                }
            }catch(Exception e){           
                System.out.println(e+"=> getMemberlist fail");         
            }finally{          
                db_close();
            }      
            return list;
    }    
        //검색을 위한 리스트 메소드 검색 변수가 들어왔을때는 where 이용해서 검색함
        public ArrayList<UserBeans> getMemberlist(String keyField, String keyWord){
       
            ArrayList<UserBeans> list = new ArrayList<UserBeans>();

            try{

                String sql ="SELECT username, email, phone FROM users ";

                if(keyWord != null && !keyWord.equals("") ){
                    sql +="WHERE "+keyField.trim()+" LIKE '%"+keyWord.trim()+"%' order by username";
                }else{//키워드에 값이 없을 시 (초기값) 전체 탐색
                    sql +="order by username";
                }
                System.out.println("sql = " + sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);

                while(rs.next()){
                    UserBeans ub = new UserBeans();

                    ub.setUserName(rs.getString(1));
                    ub.seteMail(rs.getString(2));
                    ub.setPhone(rs.getString(3));

                    list.add(ub);
                }
            }catch(Exception e){           
                System.out.println(e+"=> getMemberlist fail");         
            }finally{          
                db_close();
            }      
            return list;
    }
        //사용자 삭제를 위한 메소드
        public int deleteUsers(String id) {
		int status = 0;
		try {
			ps = con.prepareStatement("DELETE FROM users WHERE username = ?");
			// ?는 변수의 갯수
			ps.setString(1, id.trim());
			status = ps.executeUpdate();  //삭제 레코드 수 반환
		} catch (Exception e) {
			System.out.println(e + "=> delMemberlist fail");
		} finally {
			db_close();
		}
		return status;
	}
}
