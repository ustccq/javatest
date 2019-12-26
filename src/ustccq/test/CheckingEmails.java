package ustccq.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Properties;
import java.util.stream.Stream;

import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public class CheckingEmails {

	public static void doMailAction(String host, String storeType, String user, String password, String action) {
		try {

			// create properties field
			Properties properties = new Properties();
			properties.put("mail.store.protocol", storeType);
			if (action.equals("get"))
				if (storeType.equalsIgnoreCase("imap")) {
					properties.put("mail.imap.host", host);
					properties.put("mail.imap.port", "993");
					properties.put("mail.imap.ssl.enable", "true");
//					properties.put("mail.debug", "true");//设置debug模式
	//				properties.put("mail.smtp.port", "587");
	//				properties.put("mail.pop3.starttls.enable", "true");
					
	
					Session session = Session.getInstance(properties,
							new Authenticator() {
								protected PasswordAuthentication getPasswordAuthentication() {
									return new PasswordAuthentication(user,
											password);
								}
							});
					// create the POP3/IMAP store object and connect with the pop server
					Store store = session.getStore("imap");
					System.err.println("trying to connect imap server...");
					store.connect();
					
//					Session emailSession = Session.getDefaultInstance(properties);
//					Store store = emailSession.getStore("imap");
//					store.connect(host, user, password);
					
					
					// create the folder object and open it
					Folder emailFolder = store.getFolder("INBOX");
					emailFolder.open(Folder.READ_ONLY);
	
					// retrieve the messages from the folder in an array and print it
					Message[] messages = emailFolder.getMessages();
					System.out.println("messages.length---" + messages.length);
	
					for (int i = 0, n = messages.length; i < n; i++) {
						Message message = messages[i];
						System.out.println("-------------------------------------------------------------------------------");
						System.out.println("MessageId: " + message.getMessageNumber());

						Enumeration<Header> enumer = message.getAllHeaders();
						while(enumer.hasMoreElements()) {
							Header header = enumer.nextElement();
							System.out.println("Header.Name:  " + header.getName());
							System.out.println("Header.Value: " + gb2312ToGBK(header.getValue()));
							System.out.println();
						}
						MailcapCommandMap.getDefaultCommandMap();
						System.out.println("Email Number " + (i + 1));
						System.out.println("ContentType: " + message.getContentType());
						System.out.println("Description: " + message.getDescription());
						System.out.println("Disposition: " + message.getDisposition());
						System.out.println("FileName: " + message.getFileName());
						System.out.println("LineCount: " + message.getLineCount());
						System.out.println("Subject: " + message.getSubject());
						Address[] recipients = message.getAllRecipients();
						if (null != recipients)
							for(Address recipient : recipients) {
								System.out.println("Recipient: " + gb2312ToGBK(recipient.toString()));			
								System.out.println("	Recipient.Type: " + recipient.getType());
							}
						System.out.println("Folders " + message.getFolder());
						System.out.println("SentDate: " + message.getSentDate());
						Address[] froms = message.getFrom();
						if (null != froms)
							for(Address from : froms) {
								System.out.println("From: " + gb2312ToGBK(from.toString()));//From.getType   rfc822    不知道这是个啥标准				
								System.out.println("	From.Type: " + from.getType());
							}
						
						Object mailContent = message.getContent();
						if (mailContent instanceof MimeMultipart) {
							MimeMultipart mailMimeMultipart = (MimeMultipart)mailContent;
							int bodyCount = mailMimeMultipart.getCount();
							for (int j = 0; j < bodyCount; ++j) {
								BodyPart mimePart = mailMimeMultipart.getBodyPart(j);
								
								if (Part.ATTACHMENT.equalsIgnoreCase(mimePart.getDisposition())) {
									// this part is attachment
									// code to save attachment...
									if (mimePart instanceof MimeBodyPart) {
										MimeBodyPart mimeBodyPart = (MimeBodyPart)mimePart;
										System.out.println("附件MD5:" + mimeBodyPart.getContentMD5());
										String attachmentName = gb2312ToGBK(mimeBodyPart.getFileName());
										String[] names = attachmentName.split("\\.");
										File tmp = File.createTempFile(names[0], "." + names[1]);
//										mimeBodyPart.saveFile("Attachment/" + );
										mimeBodyPart.saveFile(tmp);
										
										//shit, this way doesn't work
										MessageDigest md = MessageDigest.getInstance("MD5");
										try (InputStream is = Files.newInputStream(tmp.toPath());
												DigestInputStream dis = new DigestInputStream(is, md)) {
											/* Read decorated stream (dis) to EOF as normal... */
										}
										byte[] digest = md.digest();
										BigInteger bi = new BigInteger(1, digest);
										System.out.println(bi);
										StringBuilder sb = new StringBuilder();
										int d;
										for (int k = 0; k < digest.length; k++) {
											d = digest[k];
											if (d < 0) {
												d = digest[k] & 0xff;
											}
//											if (d < 16)
//												sb.append("0");
											sb.append(Integer.toHexString(d));
										}
										System.out.println("Calculated MD5: " + sb.toString());

										String md5 = "";
										try (InputStream is = Files.newInputStream(tmp.toPath())) {
										    md5 = DigestUtils.md5Hex(is);
										}
										System.out.println("Calculated MD5 by codec:" + md5);
										
										System.out.println("附件:" + mimeBodyPart.getLineCount());
									}
								}else {
									System.out.println("正文:" + mimePart.getLineCount());
								}
							}
						}
						
						System.out.println("Text: " + message.getContent().getClass());
						System.out.println();
					}
					
					// close the store and folder objects
					emailFolder.close(false);
					store.close();
				} else {
					return;
				}
			else if (action.equals("send"))
				if (storeType.equalsIgnoreCase("imap")) {
					properties.put("mail.transport.protocol", "smtp");// 发送邮件协议
					properties.put("mail.smtp.auth", "true");// 需要验证
					properties.put("mail.smtp.port", "587");
					properties.put("mail.smtp.host", host);
					properties.put("mail.transport.protocol", "smtp");
					properties.put("mail.smtp.starttls.enable", "true");
					properties.put("mail.debug", "true");//设置debug模式
					
					// 后台输出邮件发送的过程
					Session session = Session.getInstance(properties,
							new Authenticator() {
								protected PasswordAuthentication getPasswordAuthentication() {
									return new PasswordAuthentication(user,
											password);
								}
							});
					// 邮件信息
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(user));// 设置发送人
//					message.setHeader("Content-Type", "text/html; charset=UTF-8");
					message.setContent(readLineByLineJava8("resource/mainPage.html"), "text/html; charset=UTF-8");
//					message.setText("世界那么大,我要去看看");// 设置邮件内容
//					message.setContent("<html>\n" +
//		                    "<body>\n" +
//		                    "\n" +
//		                    "<a href=\"http://www.google.com\">\n" +
//		                    "世界那么大用谷歌看看</a>\n" +
//		                    "\n" +
//		                    "</body>\n" +
//		                    "</html>", "text/html; charset=UTF-8");
					message.setSubject("测试javax邮件发送");// 设置邮件主题
//					MimeMessage.setSubject(MimeUtility.encodeText("测试javax邮件发送","gb2312", "B"))); //B为base64方式
					// 发送邮件
					Transport tran = session.getTransport();
					tran.connect(host, host, password);
					tran.sendMessage(message, new Address[] { new InternetAddress(user) });// 设置邮件接收人
					tran.close();
				} else {
					return;
				}

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String host = "mail.meowlomo.email";
		String mailStoreType = "IMAP";
		String username = "test.development@meowlomo.email";
		String password = args[0];
		ClassLoader cl = Session.class.getClassLoader();
		System.out.println("Session's class loader is:" + cl);
//		doMailAction(host, mailStoreType, username, password, "send");
		doMailAction(host, mailStoreType, username, password, "get");
	}

	private static String readLineByLineJava8(String filePath) 
	{
	    StringBuilder contentBuilder = new StringBuilder();
	    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
	    {
	        stream.forEach(s -> contentBuilder.append(s).append("\n"));
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	private static String gb2312ToGBK(String content) {
		if (StringUtils.isNotEmpty(content)) {
			if (content.contains("=?gb2312?") || content.contains("=?GB2312?")) {
				content = content.replaceFirst("gb2312", "GBK").replaceFirst("GB2312", "GBK");
				try {
					content = MimeUtility.decodeText(content);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return content;
	}
	
	private static String gb2312Transfer(BodyPart bodyPart) throws IOException, MessagingException {
		String content = (String) bodyPart.getContent();
		String[] s = bodyPart.getHeader("Content-Type");
		InputStream in = bodyPart.getInputStream();
		StringBuffer sb = new StringBuffer();
		String value = null;
		try {
			InputStreamReader isr = new InputStreamReader(in, "gbk");
			BufferedReader br = new BufferedReader(isr);
			while (StringUtils.isNotEmpty(value = br.readLine())) {
				sb.append(value);
			}
			isr.close();
			content = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			in.close();
		}
		return content;
	}
}
