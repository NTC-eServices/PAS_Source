import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;

import com.sun.corba.se.impl.encoding.TypeCodeOutputStream;

import lk.informatics.ntc.model.exception.ApplicationException;
import lk.informatics.ntc.view.util.ConnectionManager;
import lk.informatics.ntc.view.util.PropertyReader;
import lk.informatics.roc.utils.common.Utils;

public class NTCBacklogManagement {

	static boolean renameDone;
	
	public static void main(String[] args) {
		
		String backlogfilepath = null;
    	try {
    		backlogfilepath = PropertyReader
    				.getPropertyValue("documentManagement.backlog.file.path");// Path to move folders
    	} catch (ApplicationException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
	      
		File file = new File(backlogfilepath);//Path to NTC folders that already exists
        File[] files = file.listFiles();
        for(File f: files){
            System.out.println(f.getName());
            
            String mainPath=null;
            
            
            String propertyFilePath = null;
        	try {
        		 propertyFilePath = PropertyReader
        				.getPropertyValue("documentManagement.upload.file.path");// Path to move folders
        	} catch (ApplicationException e) {
        		// TODO Auto-generated catch block
        		e.printStackTrace();
        	}
        	String originalPath=propertyFilePath;
            
            String newCreatedPath = originalPath + File.separator + f.getName();
        	System.out.println("Trying to create directory "+	newCreatedPath);	

        	File file1 = new File(newCreatedPath);
        	if (!file1.exists()) {
        		if (file1.mkdir()) {
        			System.out.println("Permit Folder Created");
        		} else {
        			System.out.println("Failed to create ");
        		}
        	} else {
        		System.out.println("folder already exists");

        	}
        	
        	String optionalPath = newCreatedPath + File.separator + "Optional Documents";
        	System.out.println("Trying to create directory "+	optionalPath);	

        	File file2 = new File(optionalPath);
        	if (!file2.exists()) {
        		if (file2.mkdir()) {
        			System.out.println("Optional Folder Created");
        		} else {
        			System.out.println("Failed to create");
        		}
        	} else {
        		System.out.println("folder already exists");

        	}
        	
        	String backlogpath = optionalPath + File.separator + "Backlog Management";
        	System.out.println("Trying to create directory "+	optionalPath);	

        	File file3 = new File(backlogpath);
        	if (!file3.exists()) {
        		if (file3.mkdir()) {
        			System.out.println("Optional Folder Created");
        		} else {
        			System.out.println("Failed to create");
        		}
        	} else {
        		System.out.println("folder already exists");

        	}
        	
        	copyDocument(f.getName(),backlogpath);
        	
        	String ntcpath =backlogfilepath;
        	String ntcPermitPath = ntcpath + File.separator + f.getName();
        	
        	File fileNtc = new File(ntcPermitPath);
            File[] filesNtc = fileNtc.listFiles();
            int i =0;
            for(File fNtc: filesNtc) {
            	i++;
            	System.out.println(fNtc.getName());
            	String str = fNtc.getName();
            	String ext = str.substring(str.lastIndexOf('.'), str.length()).toLowerCase();
            	String newDocName = "SCAN DOCUMENT_"+i+ext;
            	
            	String oldpath = backlogpath+File.separator+fNtc.getName();
            	String newPath = backlogpath+File.separator+newDocName;
            	
            	mainPath=newPath;
            	
            	File oldName = new File(oldpath);
      	      File newName = new File(newPath);
      	      
      	      if(oldName.renameTo(newName)) {
      	         System.out.println("renamed");
      	         renameDone = true;
      	      } else {
      	    	renameDone = false;
      	         System.out.println("Error");
      	      }
      	      
      	      if(renameDone==true) {
      	    	  
      	    	Connection conn = null;
      			PreparedStatement stmt = null;
      			ResultSet rs = null;

      			java.util.Date date = new java.util.Date();
      			Timestamp timestamp = new Timestamp(date.getTime());

      			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

      			try {
      				conn = ConnectionManager.getConnection();

      				long seqNo = Utils.getNextValBySeqName(conn, "seq_nt_t_application_document_version");

      				/*String sql = "INSERT INTO nt_t_application_document_version\r\n" + 
      						"(seqno,  apd_transaction_type, apd_application_no,  apd_permit_no,apd_file_path, apd_doc_type, apd_created_date,  apd_versionno)\r\n" + 
      						"VALUES('"+seqNo+"', '10','"+f.getName()+"','"+f.getName()+"','"+newPath+"', 'O','"+timestamp+"', '"+i+"');";
      						
*/
      				
      				String sql = "INSERT INTO nt_t_application_document_version " + 
      						"(seqno, apd_transaction_type,apd_permit_no,apd_file_path, apd_doc_type, apd_created_date, apd_versionno,apd_doc_code,apd_document_des) " + 
      						"VALUES(?,?,?,?,?,?,?,?,?)";
      				
      				stmt = conn.prepareStatement(sql);

    				stmt.setLong(1, seqNo);
    				stmt.setString(2, "10");
    				stmt.setString(3, f.getName());
    				stmt.setString(4, newPath);
    				stmt.setString(5, "O");
    				stmt.setTimestamp(6, timestamp);
    				stmt.setInt(7, i);
    				stmt.setString(8, "SCAN");
    				stmt.setString(9, "SCAN DOCUMENT");
    				

      				stmt.executeUpdate();

      				if (stmt != null) {
      					stmt.close();
      				}
      				conn.commit();

      			} catch (Exception ex) {
      				ex.printStackTrace();
      				
      			} finally {
      				if (rs != null) {
      					try {
      						rs.close();
      					} catch (SQLException e) {
      						e.printStackTrace();
      					}
      				}
      				if (stmt != null) {
      					try {
      						stmt.close();
      					} catch (SQLException e) {
      						e.printStackTrace();
      					}
      				}
      				if (conn != null) {
      					try {
      						conn.close();
      					} catch (SQLException e) {
      						e.printStackTrace();
      					}
      	    	  
      				}
      			
      	      
      			}
      	     }
      	      
      	      
          }
            
            
            
            
            Connection conn = null;
  			PreparedStatement stmt = null;
  			ResultSet rs = null;

  			java.util.Date date = new java.util.Date();
  			Timestamp timestamp = new Timestamp(date.getTime());

  			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

  			try {
  				conn = ConnectionManager.getConnection();

  				long seqNo = Utils.getNextValBySeqName(conn, "seq_nt_t_application_document");

  				
  				
  				String sql = "INSERT INTO nt_t_application_document " + 
  						"(seqno, apd_transaction_type,apd_permit_no,apd_file_path, apd_doc_type, apd_created_date,apd_doc_code,apd_document_des) " + 
  						"VALUES(?,?,?,?,?,?,?,?)";
  				
  				stmt = conn.prepareStatement(sql);

				stmt.setLong(1, seqNo);
				stmt.setString(2, "10");
				stmt.setString(3, f.getName());
				stmt.setString(4, mainPath);
				stmt.setString(5, "O");
				stmt.setTimestamp(6, timestamp);
				stmt.setString(7, "SCAN");
				stmt.setString(8, "SCAN DOCUMENT");
				

  				stmt.executeUpdate();

  				if (stmt != null) {
  					stmt.close();
  				}
  				conn.commit();

  			} catch (Exception ex) {
  				ex.printStackTrace();
  				
  			} finally {
  				if (rs != null) {
  					try {
  						rs.close();
  					} catch (SQLException e) {
  						e.printStackTrace();
  					}
  				}
  				if (stmt != null) {
  					try {
  						stmt.close();
  					} catch (SQLException e) {
  						e.printStackTrace();
  					}
  				}
  				if (conn != null) {
  					try {
  						conn.close();
  					} catch (SQLException e) {
  						e.printStackTrace();
  					}
  	    	  
  				}
  			
  	      
  			}
            
        }
	}
	
	public static void copyDocument(String permitNo,String DestPath) {

		String backlogfilepath = null;
    	try {
    		backlogfilepath = PropertyReader
    				.getPropertyValue("documentManagement.backlog.file.path");// Path to move folders
    	} catch (ApplicationException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		
		String path = backlogfilepath;
		
		String source = path + File.separator + permitNo;
		File srcDir = new File(source);

		String destination =DestPath;
		File destDir = new File(destination);

		try {
			FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	

}
