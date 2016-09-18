import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class DBHelper {
    Connection _CONN = null;
	public static String url="jdbc:mysql://192.168.80.67:3306/gnome";
	public static String user="root";
    public static String pwd="";
    public static Connection conn;
    public static Statement stmt;
    public static ResultSet rs;

    //ȡ������
    private boolean GetConn(String sUser, String sPwd) {
        if(_CONN!=null)return true;
        try {
//            String sDriverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            String sDriverName = "com.mysql.jdbc.Driver";
//            String sDBUrl = "jdbc:sqlserver://127.0.0.1:1433;databaseName=db1";
            String sDBUrl = url;

            Class.forName(sDriverName);
            _CONN = DriverManager.getConnection(sDBUrl, sUser, sPwd);

        } catch (Exception ex) {
            // ex.printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }

    private boolean GetConn()
    {
        return GetConn(user, pwd);
    }

    //�ر�����
    private void CloseConn()
    {
        try {
            _CONN.close();
            _CONN = null;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            _CONN=null;
        }
    }


    //��������
    public boolean TestConn() {
        if (!GetConn())
            return false;

        CloseConn();
        return true;
    }

    public ResultSet GetResultSet(String sSQL,Object[] objParams)
    {
        GetConn();
        ResultSet rs=null;
        try
        {
        	Statement stmt = _CONN.createStatement();  
         //   PreparedStatement ps = _CONN.prepareStatement(sSQL);
           // if(objParams!=null)
            //{
            //    for(int i=0;i< objParams.length;i++)
             //   {
                //    ps.setObject(i+1, objParams[i]);
              //  }
          //  }
            rs=stmt.executeQuery(sSQL);
 
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            CloseConn();
        }
        finally
        {
            //CloseConn();
        }
        return rs;
    }

    public Object GetSingle(String sSQL,Object... objParams)
    {
        GetConn();
        try
        {
            PreparedStatement ps = _CONN.prepareStatement(sSQL);
            if(objParams!=null)
            {
                for(int i=0;i< objParams.length;i++)
                {
                    ps.setObject(i+1, objParams[i]);
                }
            }
            ResultSet rs=ps.executeQuery();
            if(rs.next())
                return rs.getString(1);//������1��ʼ
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        finally
        {
            CloseConn();
        }
        return null;
    }

    public DataTable GetDataTable(String sSQL,Object... objParams)
    {
        GetConn();
        DataTable dt=null;
        try
        {
        	Statement stmt = _CONN.createStatement();  
            //   PreparedStatement ps = _CONN.prepareStatement(sSQL);
              // if(objParams!=null)
               //{
               //    for(int i=0;i< objParams.length;i++)
                //   {
                   //    ps.setObject(i+1, objParams[i]);
                 //  }
             //  }
        	ResultSet    rs=stmt.executeQuery(sSQL);
           // ResultSet rs=ps.executeQuery();
            ResultSetMetaData rsmd=rs.getMetaData();

            List<DataRow> row=new ArrayList<DataRow>(); //�������м���
            List<DataColumn> col=null; //�������м���
            DataRow r=null;// ����һ��
            DataColumn c=null;//����һ��

            String columnName;
            Object value;
            int iRowCount=0;
            while(rs.next())//��ʼѭ����ȡ��ÿ�������в���һ�м�¼
            {
                iRowCount++;
                col=new ArrayList<DataColumn>();//��ʼ���м���
                for(int i=1;i<=rsmd.getColumnCount();i++)
                {
                    columnName=rsmd.getColumnName(i);
                    value=rs.getObject(columnName);
                    c=new DataColumn(columnName,value);//��ʼ����Ԫ��
                    col.add(c); //������Ϣ���뵽�м���
                }
                r=new DataRow(col);//��ʼ����Ԫ��
                row.add(r);//������Ϣ���뵽�м���
            }
            dt = new DataTable(row);
            dt.RowCount=iRowCount;
            dt.ColumnCount = rsmd.getColumnCount();
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        finally
        {
            CloseConn();
        }
        return dt;
    }

    public int UpdateData(String sSQL,Object[] objParams)
    {
        GetConn();
        int iResult=0;

        try
        {
            PreparedStatement ps = _CONN.prepareStatement(sSQL);
            if(objParams!=null)
            {
                for(int i=0;i< objParams.length;i++)
                {
                    ps.setObject(i+1, objParams[i]);
                }
            }
            iResult = ps.executeUpdate(sSQL);
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
            return -1;
        }
        finally
        {
            CloseConn();
        }
        return iResult;
    }



}