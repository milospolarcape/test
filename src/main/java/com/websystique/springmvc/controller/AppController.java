package com.websystique.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.websystique.springmvc.form.FileUploadForm;
import com.websystique.springmvc.model.Employee;
import com.websystique.springmvc.service.EmployeeService;



@Controller
@RequestMapping("/")
public class AppController {

	@Autowired
	EmployeeService service;
	
	@Autowired
	MessageSource messageSource;

	/*
	 * This method will list all existing employees.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listEmployees(ModelMap model) {

		List<Employee> employees = service.findAllEmployees();
		model.addAttribute("employees", employees);
		return "allemployees";
	}

	/*
	 * This method will provide the medium to add a new employee.
	 */
	@RequestMapping(value = { "/new" }, method = RequestMethod.GET)
	public String newEmployee(ModelMap model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		model.addAttribute("edit", false);
		return "registration";
	}

	/*
	 * This method will be called on form submission, handling POST request for
	 * saving employee in database. It also validates the user input
	 */
	@RequestMapping(value = { "/new" }, method = RequestMethod.POST)
	public String saveEmployee(@Valid Employee employee, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		
		if(!service.isEmployeeEmailUnique(employee.getId(), employee.getEmail())){
			FieldError EmailError =new FieldError("employee","name",messageSource.getMessage("non.unique.Email", new String[]{employee.getEmail()}, Locale.getDefault()));
		    result.addError(EmailError);
			return "registration";
		}
		
service.saveEmployee(employee);

		model.addAttribute("success", "Employee " + employee.getName() + " registered successfully");
		return "success";
	}


	
	/*
	 * This method will delete an employee by it's name value.
	 */
	@RequestMapping(value = { "/delete-{email}-employee" }, method = RequestMethod.GET)
	public String deleteEmployee(@PathVariable String email) {
		service.deleteEmployeeByEmail(email);
		return "redirect:/list";
	}
	
	
	@RequestMapping(value ={"/Upload"} , method = RequestMethod.POST)
	public String save(@ModelAttribute("uploadForm") FileUploadForm uploadForm, Model map) {
		
		
		MultipartFile files= uploadForm.getFiles();
		String fileName = files.getOriginalFilename();
		if(null != files&& !files.isEmpty()&&fileName.endsWith(".pdf")) { 
				
PdfReader reader;
try {
	files.transferTo(new File("C:\\Users\\Verica\\Documents\\workspace\\uploads\\"+ fileName));
reader = new PdfReader("C:/Users/Verica/Documents/workspace/uploads/"+fileName);
String page = PdfTextExtractor.getTextFromPage(reader, 1);
int name=page.indexOf("name: ");
int email=page.indexOf("email: ");
int position=page.indexOf("position: ");
int id=page.indexOf("Thank");
String Name=page.substring(name+6, email).trim();
String Email=page.substring(email+7, position).trim();
String Position=page.substring(position+10, id).trim();
map.addAttribute("name",Name);
map.addAttribute("email",Email);
map.addAttribute("position",Position);
map.addAttribute("files", fileName);
reader.close();
FileUtils.cleanDirectory(new File("C:/Users/Verica/Documents/workspace/uploads/")); 
service.pdftoData(Name, Email, Position);
} catch (IOException e) {
	e.printStackTrace();
}

return "success";		
		}
		else{
			map.addAttribute("message", "You have to upload a txt file");
			
				return "allemployees";
		}
	}
	
	
	@RequestMapping(value ={"/mail"}, method = RequestMethod.POST)
	public String EmailAttachmentReceiver(@RequestParam(value="Host") String Host,@RequestParam(value="Port") String Port,
		@RequestParam(value="Username") String Username, @RequestParam(value="Password") String Password,
		@RequestParam(value="From") String From,@RequestParam(value="To") String To,Model map) throws IOException{
		String host = Host;
        String port = Port;
        String userName = Username;
        String password = Password;
        String saveDirectory = "C:/Users/Verica/Documents/workspace/uploads/";
        
       // int mailID=0;
	        Properties properties = new Properties();	 
	        // server setting
	        properties.put("mail.pop3.host", host);
	        properties.put("mail.pop3.port", port);
	        // SSL setting
	        properties.setProperty("mail.pop3.socketFactory.class",
	                "javax.net.ssl.SSLSocketFactory");
	        properties.setProperty("mail.pop3.socketFactory.fallback", "false");
	        properties.setProperty("mail.pop3.socketFactory.port",
	                String.valueOf(port));
	        Session session = Session.getDefaultInstance(properties);
	        try {
	            // connects to the message store
	            Store store = session.getStore("pop3");
	            store.connect(userName, password);
	            // opens the inbox folder
	            Folder folderInbox = store.getFolder("INBOX");
	            folderInbox.open(Folder.READ_ONLY);
	            // fetches new messages from server
	            Message[] arrayMessages = folderInbox.getMessages();
	    int to=Integer.parseInt(To);
	    int from= Integer.parseInt(From);
	            if(to>arrayMessages.length)
	            	to=arrayMessages.length;
	            if(to<=from)from=0;
	            for (int i = from; i < to; i++) {
	            Message message = arrayMessages[i];
	                String contentType = message.getContentType();
	           // store attachment file name, separated by comma
	                String attachFiles = "";
	 	                if (contentType.contains("multipart")) {
	                    // content may contain attachments
	                    Multipart multiPart = (Multipart) message.getContent();
	                    int numberOfParts = multiPart.getCount();
	                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
	                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
	                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
	                            // this part is attachment
	                            String fileName = part.getFileName();
	                            attachFiles += fileName + ", ";
	                            part.saveFile(saveDirectory + File.separator + fileName);
	                         } 
	                    }
	                    			if (attachFiles.length() > 1) {
	                    				attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
	                    				}
	                } 
	}
	            // disconnect
	            folderInbox.close(false);
	            store.close();
	        } catch (NoSuchProviderException ex) {
	            System.out.println("No provider for pop3.");
	            ex.printStackTrace();
	        } catch (MessagingException ex) {
	            System.out.println("Could not connect to the message store");
	            ex.printStackTrace();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        File folder = new File(saveDirectory);
	        File[] listOfFiles = folder.listFiles();
	        boolean done=false;
	        boolean inside=false;
	        String Names = "",Emails = "",Filenames = "",Positions = "";
	        
	        for (int i = 0; i < listOfFiles.length; i++) {
	        if (listOfFiles[i].isFile()) {  	  
	        File files=listOfFiles[i];
	        String fileName=files.getName();
	        if(files.exists()&& fileName.endsWith(".pdf")) { 
	        	PdfReader reader;
	        	try {
	        	reader = new PdfReader(saveDirectory+fileName);
	        	String page = PdfTextExtractor.getTextFromPage(reader, 1);
	        	int name=page.indexOf("name: ");
	        	int email=page.indexOf("email: ");
	        	int position=page.indexOf("position: ");
	        	int id=page.indexOf("Thank");
	        	if(!(name==-1||email==-1||position==-1||id==-1)){//PATTERN
	        	String Name=page.substring(name+6, email).trim();
	        	String Email=page.substring(email+7, position).trim();
	        	String Position=page.substring(position+10, id).trim();
	        	 inside=true;
	        	if(service.doesEmployeeExists(Email)){
	        		Names+=Name+" & ";
		        	Emails+=Email+" & ";
		        	Positions+=Position+" & ";
		        	Filenames+=fileName+" & ";
		        	map.addAttribute("name",Names);
		        	map.addAttribute("email",Emails);
		        	map.addAttribute("position",Positions);
		        	map.addAttribute("files", Filenames);
		        	service.pdftoData(Name, Email, Position);
		        	done=true;
	        	}
	        	}// PATTERN CHECK
	        		reader.close();      	
	        	} catch (IOException e) {//try
	        		e.printStackTrace();
	        							}//catch
	        		}//file exist
	              }//if it is a file
	            }//for loop
	              if(done==true){
	FileUtils.cleanDirectory(new File(saveDirectory));
	        	return "success";		
	        			}
	              			if(inside==true){
	              				FileUtils.cleanDirectory(new File(saveDirectory));
	        				map.addAttribute("message", "allready in database");
	        				}
	              			else 
		        				map.addAttribute("message", "No pdf attachments with pattern found in the emails");
	        					return "allemployees";
}
}	
	

