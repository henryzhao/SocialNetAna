/**
 * @(#)SocialNetAny.java
 *
 * JFC SocialNetAny application
 *
 * @author
 * @version 1.00 2016/3/28
 */
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SocialNetAny {

    public static void main(String[] args) {

        // Create application frame.
     SocialNetAnyFrame frame = new SocialNetAnyFrame();

     
       frame.show(); 
     frame.setVisible(true);


    DBHelper help=new DBHelper();  
         if(help.TestConn())  
             System.out.println("���ӳɹ���");  
        else  
            System.out.println("����ʧ�ܣ�");  
           
     
     
     
  
       
       
       
    }
}